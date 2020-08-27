package vn.com.irtech.eport.web.controller.gate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.web.dto.DetectionInfomation;

@Controller
@RequestMapping("/gate/test")
public class GateTestController extends BaseController {

	private final static String PREFIX = "gate/test";
	
	@Autowired
	private WebSocketService webSocketService;
	
	@GetMapping()
	public String getView() {
		return PREFIX + "/test";
	}
	
	@PostMapping("/detection")
	@ResponseBody
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfomation detectionInfo) {
		
		String detectJson = new Gson().toJson(detectionInfo);
		logger.debug(">>>> Receive detection info:" + detectJson);
		// Save detection info to cache
		CacheUtils.put("detectionInfo_" + detectionInfo.getGateId(), detectionInfo);
		
		// Send to monitor
		webSocketService.sendMessage("/gate/detection/monitor", detectionInfo);
		return success();
	}
}
