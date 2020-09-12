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
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.system.dto.NotificationReq;

@RestController
@RequestMapping("/gate")
public class GateDetectionController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(GateDetectionController.class);
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@PostMapping("/detection")
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfo detectionInfo) {
		
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
	
	
}
 