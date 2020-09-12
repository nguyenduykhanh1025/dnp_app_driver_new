package vn.com.irtech.eport.api.controller.gate.detection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.DetectionInfo;
import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.api.form.MeasurementDataReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysConfigService;

@RestController
@RequestMapping("/gate")
public class GateDetectionController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(GateDetectionController.class);
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private ILogisticTruckService logisticTruckService;
	
	@Autowired ISysConfigService sysConfigService;
	
	@PostMapping("/detection")
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfo detectionInfo) {
		
		try {
			sendCheckinReq(detectionInfo.getTruckNo());
		} catch (Exception e) {
			logger.error("Error when send check in req by gate auto detection " + e);
		}
		
		if ("1".equals(sysConfigService.selectConfigByKey("gate.permission"))) {
			try {
				updateData(detectionInfo);
			} catch (Exception e) {
				logger.error("Error when update data from gate: " + e);
			}
			
			String detectJson = new Gson().toJson(detectionInfo);
			logger.debug(">>>> Receive detection info:" + detectJson);
			// Save detection info to cache
			CacheUtils.put("detectionInfo_" + detectionInfo.getGateId(), detectionInfo);
			// Prepare request for check-in
			List<MeasurementDataReq> measurementDataReqs = new ArrayList<>();
			MeasurementDataReq measurementDataReq = new MeasurementDataReq();
			measurementDataReq.setTruckNo(detectionInfo.getTruckNo());
			measurementDataReq.setChassisNo(detectionInfo.getChassisNo());
			if (detectionInfo.getWgt() != null) {
				measurementDataReq.setWeight(detectionInfo.getWgt().toString());
			}
			// If detect container 1
			if (StringUtils.isNotEmpty(detectionInfo.getContainerNo1())) {
				measurementDataReq.setContNo(detectionInfo.getContainerNo1());
			}
			// add the first container
			measurementDataReqs.add(measurementDataReq);
			// If detect 2 containers
			if (StringUtils.isNotEmpty(detectionInfo.getContainerNo2())) {
				MeasurementDataReq measurementDataReq2 = new MeasurementDataReq();
				measurementDataReq2.setTruckNo(detectionInfo.getTruckNo());
				measurementDataReq2.setChassisNo(detectionInfo.getChassisNo());
				measurementDataReq2.setContNo(detectionInfo.getContainerNo2());
				if (detectionInfo.getWgt() != null) {
					measurementDataReq2.setWeight(detectionInfo.getWgt().toString());
				}
				measurementDataReqs.add(measurementDataReq2);
			}
			CheckinReq checkinReq = new CheckinReq();
			checkinReq.setInput(measurementDataReqs);
			try {
				logger.debug("Publish smart gate app request: " + new Gson().toJson(checkinReq));
				mqttService.publish(MqttConsts.SMART_GATE_RES_TOPIC.replace("+", detectionInfo.getGateId()), new MqttMessage(new Gson().toJson(checkinReq).getBytes()));
			} catch (MqttException e) {
				logger.error("Error send detection info: " + e);
			}
		}
		return success();
	}
	
	@Transactional
	private void updateData(DetectionInfo detectionInfo) {
		PickupHistory pickupHistory = new PickupHistory();
		pickupHistory.setTruckNo(detectionInfo.getTruckNo());
		pickupHistory.setChassisNo(detectionInfo.getChassisNo());
		pickupHistory.setStatus(0);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistory);
		if (CollectionUtils.isNotEmpty(pickupHistories)) {
			for (PickupHistory pickupHistory2 : pickupHistories) {
				if (detectionInfo.getLoadableWgt() != null) {
					if (pickupHistory2.getShipment().getServiceType() == EportConstants.SERVICE_PICKUP_FULL) {
						pickupHistory2.setLoadableWgt(detectionInfo.getLoadableWgt());
					}
				}
				if (StringUtils.isNotEmpty(detectionInfo.getYardPosition1())) {
					if (detectionInfo.getContainerNo1().equalsIgnoreCase(pickupHistory2.getContainerNo())) {
						String[] yardPositionArr1 = detectionInfo.getYardPosition1().split("-");
						pickupHistory2.setBlock(yardPositionArr1[0]);
						pickupHistory2.setBay(yardPositionArr1[1]);
						pickupHistory2.setLine(yardPositionArr1[2]);
						pickupHistory2.setTier(yardPositionArr1[3]);
					}
				}
				if (StringUtils.isNotEmpty(detectionInfo.getYardPosition2())) {
					if (detectionInfo.getContainerNo2().equalsIgnoreCase(pickupHistory2.getContainerNo())) {
						String[] yardPositionArr2 = detectionInfo.getYardPosition2().split("-");
						pickupHistory2.setBlock(yardPositionArr2[0]);
						pickupHistory2.setBay(yardPositionArr2[1]);
						pickupHistory2.setLine(yardPositionArr2[2]);
						pickupHistory2.setTier(yardPositionArr2[3]);
					}
				}
				pickupHistoryService.updatePickupHistory(pickupHistory2);
			}
		}
	}
	
	public void sendCheckinReq(String truckNo) {
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setTruckNo(truckNo);;
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
 