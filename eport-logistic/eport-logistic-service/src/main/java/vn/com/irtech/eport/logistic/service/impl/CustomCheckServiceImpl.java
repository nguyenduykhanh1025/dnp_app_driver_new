/**
 * 
 */
package vn.com.irtech.eport.logistic.service.impl;

import org.apache.commons.lang3.StringUtils;
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
import vn.com.irtech.eport.logistic.dto.CustomsCheckResultDto;
import vn.com.irtech.eport.logistic.service.ICustomCheckService;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * @author GiapHOD
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
				// BeanUtils.copyBeanProp(resultDto, data);
				// Copy to result
				resultDto.setUserVoy(data.getStr("userVoy"));
				resultDto.setCntrNo(data.getStr("cntrNo"));
				resultDto.setCustomsStatus(data.getStr("customsStatus"));
				resultDto.setCustomsAppNo(data.getStr("customsAppNo"));
				resultDto.setCustomsRemark(data.getStr("customsRemark"));
				resultDto.setMsgRecvContent(data.getStr("msgRecvContent"));
				resultDto.setTaxCode(data.getStr("enterpriseIdentity"));
				resultDto.setCompanyName(data.getStr("enterpriseName"));
				// String rs = data.get("customsStatus").toString();
				// convert xml to object
				String msgRecvContent = data.getStr("msgRecvContent");
				if(msgRecvContent != null) {
					//search customsStatus&gt;
					if(StringUtils.isBlank(resultDto.getCustomsStatus())) {
						int statusIdx = msgRecvContent.indexOf("customsStatus&gt;");
						// Search status again
						if(statusIdx > 0) {
							String statusStr = msgRecvContent.substring(statusIdx + "customsStatus&gt;".length());
							String status = statusStr.substring(0, statusStr.indexOf("&lt;/customsStatus&gt;"));
							resultDto.setCustomsStatus(status);
						}
					}
					if(StringUtils.isBlank(resultDto.getTaxCode())) {
						int mstIdx = msgRecvContent.indexOf("enterpriseIdentity&gt;");
						// Search status again
						if(mstIdx > 0) {
							String mstStr = msgRecvContent.substring(mstIdx + "enterpriseIdentity&gt;".length());
							String mst = mstStr.substring(0, mstStr.indexOf("&lt;/enterpriseIdentity&gt;"));
							resultDto.setTaxCode(mst);
						}
						int companyIdx = msgRecvContent.indexOf("enterpriseName&gt;");
						// Search status again
						if(companyIdx > 0) {
							String companyStr = msgRecvContent.substring(companyIdx + "enterpriseName&gt;".length());
							String company = companyStr.substring(0, companyStr.indexOf("&lt;/enterpriseName&gt;"));
							resultDto.setCompanyName(company);
						}
					}
//					String xml = HtmlUtils.htmlUnescape(msgRecvContent);
//					JAXBContext jaxbContext = JAXBContext.newInstance(CustomDeclareResult.class);
//					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//					CustomDeclareResult customDeclareResult = (CustomDeclareResult) unmarshaller.unmarshal(new StringReader(xml));
//					resultDto.setCustomDeclareResult(customDeclareResult);
				}
				return resultDto;
			}
			return null;
		} catch (Exception e) {
			logger.error("Call ACISS Error", e);
			return null;
		}
	}

}
