package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.OtpCode;

/**
 * otp CodeMapper Interface
 * 
 * @author ruoyi
 * @date 2020-06-05
 */
public interface OtpCodeMapper 
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
     * @return Result
     */
    public int insertOtpCode(OtpCode otpCode);

    /**
     * Update otp Code
     * 
     * @param otpCode otp Code
     * @return Result
     */
    public int updateOtpCode(OtpCode otpCode);

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    public int deleteOtpCodeById(Long id);

    /**
     * Batch Delete otp Code
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteOtpCodeByIds(String[] ids);

    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId);

}
