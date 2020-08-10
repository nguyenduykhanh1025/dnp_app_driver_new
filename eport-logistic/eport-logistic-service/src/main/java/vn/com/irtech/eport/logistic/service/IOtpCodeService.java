package vn.com.irtech.eport.logistic.service;

import java.io.IOException;
import java.util.List;

import vn.com.irtech.eport.logistic.domain.OtpCode;

/**
 * otp CodeService Interface
 * 
 * @author irtech
 * @date 2020-06-05
 */
public interface IOtpCodeService 
{
   /**
     * Get otp Code
     * 
     * @param id otp CodeID
     * @return otp Code
     */
    public OtpCode selectSysOtpById(Long id);

    /**
     * Get otp Code List
     * 
     * @param sysOtp otp Code
     * @return otp Code List
     */
    public List<OtpCode> selectSysOtpList(OtpCode sysOtp);

    /**
     * Add otp Code
     * 
     * @param otpCode otp Code
     * @return result
     */
    public int insertSysOtp(OtpCode otpCode);

    /**
     * Update otp Code
     * 
     * @param sysOtp otp Code
     * @return result
     */
    public int updateSysOtp(OtpCode sysOtp);

    /**
     * Batch Delete otp Code
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysOtpByIds(String ids);

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    public int deleteSysOtpById(Long id);

    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId);

    public int verifyOtpCodeAvailable(OtpCode otpCode);

    public int deleteOtpCodeByShipmentDetailIds(String shipmentDetailIds);

    public String postOtpMessage(String mobilePhone,String content) throws IOException;

}
