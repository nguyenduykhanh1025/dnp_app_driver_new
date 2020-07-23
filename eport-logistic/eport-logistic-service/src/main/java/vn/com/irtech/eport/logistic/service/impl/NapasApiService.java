package vn.com.irtech.eport.logistic.service.impl;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.logistic.service.INapasApiService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@Service
public class NapasApiService implements INapasApiService {
	
	@Autowired
	private ISysConfigService configService;

	@Override
	public String getAccessToken() {
		String urlString = configService.selectConfigByKey("napas.access.key");
		urlString = urlString.replace("amp;", "");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		return restTemplate.postForObject(urlString,  requestEntity, JSONObject.class).getString("access_token");
	}

	@Override
	public JSONObject getDataKey(String clienIp, String deviceId, String orderId, Long amount, String token) {
		String urlString = configService.selectConfigByKey("napas.data.key");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		
		Map<String, Object> data = new HashMap<>();
		data.put("apiOperation", "DATA_KEY");
		
		Map<String, String> inputParameters = new HashMap<>();
		inputParameters.put("clientIP", clienIp);
		inputParameters.put("deviceId", deviceId);
		inputParameters.put("environment", "WebApp");
		inputParameters.put("cardScheme", "AtmCard");
		inputParameters.put("enable3DSecure", "false");
		data.put("inputParameters", inputParameters);
		
		Map<String, Object> order = new HashMap<>();
		order.put("id", orderId);
		order.put("amount", amount);
		order.put("currency", "VND");
		data.put("order", order);
		
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<Map<String,Object>>(data, headers);
		RestTemplate restTemplate = new RestTemplate();
		return  restTemplate.postForObject(urlString, requestEntity, JSONObject.class);
	}
	
	

}
