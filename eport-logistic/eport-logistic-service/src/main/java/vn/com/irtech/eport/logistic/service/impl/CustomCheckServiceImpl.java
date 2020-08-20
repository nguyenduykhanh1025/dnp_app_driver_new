/**
 * 
 */
package vn.com.irtech.eport.logistic.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.constant.SystemConstants;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.common.json.JSONObject.JSONArray;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.dto.CustomsCheckResultDto;
import vn.com.irtech.eport.logistic.service.ICustomCheckService;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * @author GiapHD
 *
 */
@Service
public class CustomCheckServiceImpl implements ICustomCheckService {

	private static final Logger logger = LoggerFactory.getLogger(CustomCheckServiceImpl.class);

	@Autowired
	private ISysConfigService configService;

	@Override
	public CustomsCheckResultDto checkCustomStatus(String userVoy, String cntrNo) {
		try {
			String uri = configService.selectConfigByKey(SystemConstants.ACCIS_API_URL_KEY);
			// String header = configService.selectConfigByKey("acciss.api.header");
			String requestJson = "{\"RequestCntrStatus\": {\"UserVoy\": \"" + userVoy + "\",\"CntrNo\": \"" + cntrNo + "\"}}";
			// Header setting
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Authorization", "Basic RVBPUlQ6MTEx"); // FIXME Get author from SysConfig
			// Using rest template
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			// Post to check
			ResponseEntity<JSONObject> result = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
			JSONObject json = result.getBody();
			// Log debug
			logger.debug("Call ACISS, CntrNo:{}, Voy:{}, Response: {}", cntrNo, userVoy, json);
//			JsonObject convertedObject = new Gson().fromJson(stringJson, JsonObject.class);
			JSONObject response = json.getObj("response");
			JSONArray jarray = response.getArr("data");
			// check return result
			if (jarray.size() > 0) {
				JSONObject data = (JSONObject) jarray.get(0);
				CustomsCheckResultDto resultDto = new CustomsCheckResultDto();
				BeanUtils.copyBeanProp(resultDto, data);
				// String rs = data.get("customsStatus").toString();
				return resultDto;
			}
			return null;
		} catch (Exception e) {
			logger.error("Call ACISS Error", e);
			return null;
		}
	}

}
