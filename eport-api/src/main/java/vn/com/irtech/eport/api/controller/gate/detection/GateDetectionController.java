package vn.com.irtech.eport.api.controller.gate.detection;

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
import vn.com.irtech.eport.api.form.DetectionInfo;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;

@RestController
@RequestMapping("/gate")
public class GateDetectionController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(GateDetectionController.class);
	
	@Autowired
	private MqttService mqttService;
	
	@PostMapping("/detection")
	@ResponseBody
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfo detectionInfo) {
		CacheUtils.put("detectionInfo_" + detectionInfo.getGateId(), detectionInfo);
		try {
			mqttService.publish(MqttConsts.SMART_GATE_RES_TOPIC.replace("+", detectionInfo.getGateId()), new MqttMessage(new Gson().toJson(detectionInfo).getBytes()));
		} catch (MqttException e) {
			logger.error("Error send detection info: " + e);
		}
		return success();
	}
}
 