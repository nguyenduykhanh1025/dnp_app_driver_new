package vn.com.irtech.eport.api.controller.gate.detection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.DetectionInfo;
import vn.com.irtech.eport.api.form.MeasurementDataReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;

@RestController
@RequestMapping("/gate")
public class GateDetectionController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(GateDetectionController.class);
	
	@Autowired
	private MqttService mqttService;
	
	@PostMapping("/detection")
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfo detectionInfo) {
		
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
}
 