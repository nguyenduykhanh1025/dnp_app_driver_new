package vn.com.irtech.eport.api.controller.transport;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.MeasurementDataReq;
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
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@RestController
@RequestMapping("/transport/checkin")
public class DriverCheckinController extends BaseController  {
	
	@Autowired
	private IDriverCheckinService driverCheckinService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private MqttService mqttService;
	
	@Log(title = "Tài Xế Check-in", businessType = BusinessType.UPDATE, operatorType = OperatorType.MOBILE)
	@PostMapping("")
	@ResponseBody
	public AjaxResult checkin(@Valid @RequestBody QrCodeReq req) throws Exception{
		
		AjaxResult ajaxResult = AjaxResult.success();
		
		String sessionId = getSession().getId();
		
		ajaxResult.put("sessionId", sessionId);
		
		ajaxResult.put("qrString", driverCheckinService.checkin(req, sessionId));
		
		return ajaxResult;
	}
	
	@PostMapping("/auto")
	@ResponseBody
	public AjaxResult checkInAuto() {
		Long driverId = SecurityUtils.getCurrentUser().getUser().getUserId();
		String sessionId = getSession().getId();
		
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(driverId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		
		if (CollectionUtils.isEmpty(pickupHistories)) {
			throw new BusinessException("Quý khách chưa đăng ký vận chuyển container ra/vào cảng.");
		}
		
		List<MeasurementDataReq> measurementDataReqs = new ArrayList<>();
		for (PickupHistory pickupHistory : pickupHistories) {
			if (pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_DROP_FULL || 
					pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_DROP_EMPTY) {
				MeasurementDataReq measurementDataReq = new MeasurementDataReq();
				measurementDataReq.setTruckNo(pickupHistory.getTruckNo());
				measurementDataReq.setChassisNo(pickupHistory.getChassisNo());
				pickupHistory.setContainerNo(pickupHistory.getContainerNo());
				measurementDataReqs.add(measurementDataReq);
			}
		}
		
		CheckinReq checkinReq = new CheckinReq();
		checkinReq.setInput(measurementDataReqs);
		checkinReq.setSessionId(sessionId);
		
		if (!checkIfDriverIsAtGate(checkinReq)) {
			throw new BusinessException("Quý khách chưa đến cổng chưa thể làm thủ tục vào cổng.");
		}
		try {
			logger.debug("Publish smart gate app request: " + new Gson().toJson(checkinReq));
			mqttService.publish(MqttConsts.SMART_GATE_RES_TOPIC.replace("+", "gate1"), new MqttMessage(new Gson().toJson(checkinReq).getBytes()));
		} catch (MqttException e) {
			logger.error("Error send detection info: " + e);
		}
		return success();
	}
	
	
	private Boolean checkIfDriverIsAtGate(CheckinReq checkinReq) {
		// TODO : Add condition to check if driver is at gate and information about truck is true
		return true;
	}
}
