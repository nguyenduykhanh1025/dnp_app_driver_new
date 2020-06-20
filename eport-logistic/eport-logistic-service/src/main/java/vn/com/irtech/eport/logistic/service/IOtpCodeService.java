package vn.com.irtech.eport.logistic.service;

import java.io.IOException;
import java.util.List;
import vn.com.irtech.eport.logistic.domain.OtpCode;

/**
 * otp CodeService Interface
 * 
 * @author ruoyi
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
    public OtpCode selectOtpCodeById(Long id);

    /**
     * Get otp Code List
     * 
     * @param otpCode otp Code
     * @return otp Code List
     */
    public List<OtpCode> selectOtpCodeList(OtpCode otpCode);

    /**
     * Add otp Code
     * 
     * @param otpCode otp Code
     * @return result
     */
    public int insertOtpCode(OtpCode otpCode);

    /**
     * Update otp Code
     * 
     * @param otpCode otp Code
     * @return result
     */
    public int updateOtpCode(OtpCode otpCode);

    /**
     * Batch Delete otp Code
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteOtpCodeByIds(String ids);

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    public int deleteOtpCodeById(Long id);

    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId);

    public int verifyOtpCodeAvailable(OtpCode otpCode);

    public int deleteOtpCodeByShipmentDetailIds(String shipmentDetailIds);

    public String postOtpMessage(String content) throws IOException;
}
