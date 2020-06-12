package vn.com.irtech.eport.logistic.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;

/**
 * @author GiapHD
 *
 */
public abstract class LogisticBaseController extends BaseController {

	@Autowired
	ILogisticGroupService logisticGroupService;

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
		
}
