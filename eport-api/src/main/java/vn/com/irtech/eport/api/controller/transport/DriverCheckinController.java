package vn.com.irtech.eport.api.controller.transport;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.api.form.QrCodeReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.api.service.transport.IDriverCheckinService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.system.dto.NotificationReq;

@RestController
@RequestMapping("/transport/checkin")
public class DriverCheckinController extends BaseController  {
	
	private final Logger logger = LoggerFactory.getLogger(DriverCheckinController.class);
	
	@Autowired
	private IDriverCheckinService driverCheckinService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private ILogisticTruckService logisticTruckService;
	
	@Log(title = "Tài Xế Check-in", businessType = BusinessType.UPDATE, operatorType = OperatorType.MOBILE)
	@PostMapping("")
	@ResponseBody
	public AjaxResult checkin(@Valid @RequestBody QrCodeReq req) throws Exception{
		
		AjaxResult ajaxResult = AjaxResult.success();
		
		String sessionId = getSession().getId();
		
		ajaxResult.put("sessionId", sessionId);
		
		ajaxResult.put("qrString", driverCheckinService.checkin(req, sessionId));
		
		sendCheckinReq(SecurityUtils.getCurrentUser().getUser().getUserId(), sessionId);
		
		return ajaxResult;
	}
	
	public void sendCheckinReq(Long driverId, String sessionId) {
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(driverId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		
		if (CollectionUtils.isEmpty(pickupHistories)) {
			throw new BusinessException("Quý khách chưa đăng ký vận chuyển container ra/vào cảng.");
		}
		
		// contSendCount variable to count the number of cont send to gate in
		// same to contReceivecount variable, depend on the number set to container1 or container2 attribute
		int contSendCount = 0;
		int contReceiveCount = 0;
		
		// Set pre data for gate notification check in request to notification app by gate user
		// All flag and option of the object be false and will be true when meet condition
		GateNotificationCheckInReq gateNotificationCheckInReq = new GateNotificationCheckInReq();
		gateNotificationCheckInReq.setReceiveOption(false);
		gateNotificationCheckInReq.setSendOption(false);
		gateNotificationCheckInReq.setRefFlg1(false);
		gateNotificationCheckInReq.setRefFlg2(false);
		gateNotificationCheckInReq.setSessionId(sessionId);
		
		// Set general infor up all pickup history in list
		// It's supposed to be same with all element in list
		PickupHistory pickHistoryGeneral = pickupHistories.get(0);
		gateNotificationCheckInReq.setGatePass(pickHistoryGeneral.getGatePass());
		gateNotificationCheckInReq.setTruckNo(pickHistoryGeneral.getTruckNo());
		gateNotificationCheckInReq.setChassisNo(pickHistoryGeneral.getChassisNo());
		
		// Get weight and self weight by truck no
		LogisticTruck logisticTruckParam = new LogisticTruck();
		logisticTruckParam.setPlateNumber(pickHistoryGeneral.getTruckNo());
		logisticTruckParam.setType(EportConstants.TRUCK_TYPE_TRUCK_NO);
		List<LogisticTruck> truckNos = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
		
		logisticTruckParam.setPlateNumber(pickHistoryGeneral.getChassisNo());
		logisticTruckParam.setType(EportConstants.TRUCK_TYPE_CHASSIS_NO);
		List<LogisticTruck> chassisNos = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
		
		if (CollectionUtils.isNotEmpty(chassisNos)) {
			gateNotificationCheckInReq.setLoadableWgt(chassisNos.get(0).getWgt());
			
			if (CollectionUtils.isNotEmpty(truckNos)) {
				gateNotificationCheckInReq.setDeduct(truckNos.get(0).getSelfWgt() + chassisNos.get(0).getSelfWgt());
			}
		}
		
		// Begin interate pickup history list get by driver id
		for (PickupHistory pickupHistory : pickupHistories) {
			
			// Get service type of pickup history
			Integer serviceType = pickupHistory.getShipment().getServiceType();
			
			// Check if pickup is receive cont (out mode in gate)
			// true then receive option of gate notification req true
			// set other data for receive option
			if (EportConstants.SERVICE_PICKUP_FULL == serviceType || EportConstants.SERVICE_PICKUP_EMPTY == serviceType) {
				contReceiveCount++;
				gateNotificationCheckInReq.setReceiveOption(true);
				
				// Check if cont count is 1 then that should be the first container out
				if (contReceiveCount == 1) {
					
					// Check if is pickup by job order no or pickup by container
					if (pickupHistory.getJobOrderFlg()) {
						gateNotificationCheckInReq.setRefFlg1(true);
						gateNotificationCheckInReq.setRefNo1(pickupHistory.getJobOrderNo());
					} else {
						gateNotificationCheckInReq.setContainerReceive1(pickupHistory.getContainerNo());
					}
					
				// Not 1 then that definitely 2 -> container 2 or job 2
				} else {
					
					// Check if is pickup by job order no or pickup by container
					if (pickupHistory.getJobOrderFlg()) {
						gateNotificationCheckInReq.setRefFlg2(true);
						gateNotificationCheckInReq.setRefNo2(pickupHistory.getJobOrderNo());
					} else {
						gateNotificationCheckInReq.setContainerReceive2(pickupHistory.getContainerNo());
					}
				}
			// the data will be always pickup or drop container so no need for check for service type
			// If it not pickup full or empty then it definitely is drop if exception then the data is stored wrong
			// that should be bad
			} else {
				contSendCount++;
				gateNotificationCheckInReq.setSendOption(true);
				
				// The count for this option will be always 1 or 2, so 1 container no should be set for 1
				// If the exception happen then data is saved is wrong
				if (contSendCount == 1) {
					gateNotificationCheckInReq.setContainerSend1(pickupHistory.getContainerNo());
				} else {
					gateNotificationCheckInReq.setContainerSend2(pickupHistory.getContainerNo());
				}
			}
		}
		
		// Finally finish a long term set data for just one object to send to gate notification app
		// this long set data may cause some exception and eventually corrupt the function
		// Now parse the object to string to send via mqtt 
		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setTitle("ePort: Yêu cầu check in tại cổng.");
		notificationReq.setMsg("Có yếu cầu check in tại cổng xe " + gateNotificationCheckInReq.getTruckNo());
		notificationReq.setType(EportConstants.APP_USER_TYPE_GATE);
		notificationReq.setLink("");
		notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
		notificationReq.setGateData(gateNotificationCheckInReq);
		
		String msg = new Gson().toJson(notificationReq);
		try {
			mqttService.publish(MqttConsts.NOTIFICATION_GATE_TOPIC, new MqttMessage(msg.getBytes()));
		} catch (MqttException e) {
			logger.error("Error when try sending notification request check in for gate: " + e);
		}
	}
}
