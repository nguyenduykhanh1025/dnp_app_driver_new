package vn.com.irtech.eport.logistic.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.mqtt.service.MqttPushClient;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.utils.R;

/**
 * @author GiapHD
 *
 */
public abstract class LogisticBaseController extends BaseController {

	@Autowired
	ILogisticGroupService logisticGroupService;
	
	@Autowired
    private MqttPushClient mqttPushClient;

	public LogisticAccount getUser() {
		return ShiroUtils.getSysUser();
	}
	
	public Long getUserId() {
		return ShiroUtils.getUserId();
	}
	
	public LogisticGroup getGroup() {
		LogisticAccount user = getUser();
		LogisticGroup group = logisticGroupService.selectLogisticGroupById(user.getGroupId());
		return group;
	}

	public boolean verifyPermission(Long groupId) {
		LogisticAccount user = getUser();
		if (user.getGroupId().equals(groupId)) {
			return true;
		}
		return false;
	}
	
	public void sendDataToTopic(String data, String topic) {
    	mqttPushClient.publish(1, true, topic, data);
	}
	
	public String postOtpMessage(String contentabc) throws IOException {
		String url = "http://svc.netplus.vn/WSSendSMS.asmx";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		//String countryCode="Canada";
		String xml = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><SendSMS xmlns=\"http://tempuri.org/\"><aSMS_Input><SmsType>1</SmsType><IdCustomerSent>140273</IdCustomerSent><CompanyCode>DANANGPORT</CompanyCode><Mobile>84983960445</Mobile><SMSContent>"+contentabc+"</SMSContent></aSMS_Input><userName>danangportguitin</userName><password>568926</password></SendSMS></soap12:Body></soap12:Envelope>";
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(xml);
		wr.flush();
		wr.close();
		String responseStatus = con.getResponseMessage();
		System.out.println(responseStatus);
		BufferedReader in = new BufferedReader(new InputStreamReader(
		con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();
			System.out.println("response:" + response.toString());
			return response.toString();
	}

	public List<String> getVesselCodeList(){
		String url = Global.getApiUrl() + "/selectField/selectVesselCode";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class);
		List<String> list = (List<String>) r.get("data");
		return list;
	}
	public List<String> getConsigneeList(){
		List<String> list = new ArrayList<>();
		list.add("HAHPHU");
		list.add("VINAL");
		list.add("MAVI");
		list.add("BACHYSOLY");
		list.add("SUNPAPER");
		list.add("DIENXANH");
		return list;
	}
	public List<String> getTruckCoList(){
		List<String> list = new ArrayList<>();
		list.add("0120314052601 : CTY TNHH HAHPHU");
		list.add("013105020130 : CTY CP VINAL");
		list.add("010201011023: MAVI");
		return list;
	}
	public List<String> getVoyageList(){
		List<String> list = new ArrayList<>();
		list.add("0101");
		list.add("0102");
		list.add("0103");
		list.add("0120");
		list.add("0130");
		list.add("0210");
		return list;
	}
	public List<String> getOperatorCodeList(){
		List<String> list = new ArrayList<>();
		list.add("SIT");
		list.add("COS");
		list.add("MSC");
		list.add("OWN");
		list.add("MSL");
		list.add("CMA");
		return list;
	}
	public List<String> getFeList(){
		List<String> list = new ArrayList<>();
		list.add("F");
		list.add("E");
		return list;
	}
	public List<String> getCargoTypeList(){
		List<String> list = new ArrayList<>();
		list.add("MT");
		list.add("DR");
		list.add("RF");
		list.add("GP");
		return list;
	}
	public List<String> getPODList(){
		List<String> list = new ArrayList<>();
		list.add("VNDAD");
		list.add("CMTVN");
		list.add("HKHKG");
		list.add("TCCVN");
		list.add("TWTXG");
		list.add("CNTAO");
		return list;
	}
}
