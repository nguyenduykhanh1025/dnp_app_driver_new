package vn.com.irtech.eport.logistic.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.OtpCodeMapper;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * otp CodeService Business Processing
 * 
 * @author ruoyi
 * @date 2020-06-05
 */
@Service
public class OtpCodeServiceImpl implements IOtpCodeService 
{
    @Autowired
    private OtpCodeMapper sysOtpMapper;

     /**
     * Get otp Code
     * 
     * @param id otp CodeID
     * @return otp Code
     */
    @Override
    public OtpCode selectSysOtpById(Long id)
    {
        return sysOtpMapper.selectSysOtpById(id);
    }

    /**
     * Get otp Code List
     * 
     * @param sysOtp otp Code
     * @return otp Code
     */
    @Override
    public List<OtpCode> selectSysOtpList(OtpCode sysOtp)
    {
        return sysOtpMapper.selectSysOtpList(sysOtp);
    }

    /**
     * Add otp Code
     * 
     * @param sysOtp otp Code
     * @return result
     */
    @Override
    public int insertSysOtp(OtpCode sysOtp)
    {
        sysOtp.setCreateTime(DateUtils.getNowDate());
        return sysOtpMapper.insertSysOtp(sysOtp);
    }

    /**
     * Update otp Code
     * 
     * @param sysOtp otp Code
     * @return result
     */
    @Override
    public int updateSysOtp(OtpCode sysOtp)
    {
        sysOtp.setUpdateTime(DateUtils.getNowDate());
        return sysOtpMapper.updateSysOtp(sysOtp);
    }

    /**
     * Delete otp Code By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysOtpByIds(String ids)
    {
        return sysOtpMapper.deleteSysOtpByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    @Override
    public int deleteSysOtpById(Long id)
    {
        return sysOtpMapper.deleteSysOtpById(id);
    }

    @Override
    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId)
    {
        return sysOtpMapper.selectOtpCodeByshipmentDetailId(shipmentDetailId);
    }

    @Override
    public int verifyOtpCodeAvailable(OtpCode otpCode) {
        return sysOtpMapper.verifyOtpCodeAvailable(otpCode);
    }

    @Override
    public int deleteOtpCodeByShipmentDetailIds(String shipmentDetailIds) {
        return sysOtpMapper.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);
    }

    @Override
    public String postOtpMessage(String mobilePhone,String content) throws IOException {
        mobilePhone = "84"+mobilePhone.substring(mobilePhone.length()-9,mobilePhone.length());
		String url = "http://svc.netplus.vn/WSSendSMS.asmx";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		//String countryCode="Canada";
		String xml = "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"><soap12:Body><SendSMS xmlns=\"http://tempuri.org/\"><aSMS_Input><SmsType>1</SmsType><IdCustomerSent>140273</IdCustomerSent><CompanyCode>DANANGPORT</CompanyCode><Mobile>"+mobilePhone+"</Mobile><SMSContent>"+content+"</SMSContent></aSMS_Input><userName>danangportguitin</userName><password>568926</password></SendSMS></soap12:Body></soap12:Envelope>";
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
