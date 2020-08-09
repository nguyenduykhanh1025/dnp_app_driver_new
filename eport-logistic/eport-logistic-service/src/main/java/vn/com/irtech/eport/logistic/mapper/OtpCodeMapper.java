package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.OtpCode;


/**
 * otp CodeMapper Interface
 * 
 * @author irtech
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
     * @param sysOtp otp Code
     * @return Result
     */
    public int insertSysOtp(OtpCode sysOtp);

    /**
     * Update otp Code
     * 
     * @param sysOtp otp Code
     * @return Result
     */
    public int updateSysOtp(OtpCode sysOtp);

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    public int deleteSysOtpById(Long id);

    /**
     * Batch Delete otp Code
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteSysOtpByIds(String[] ids);

    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId);

    public int verifyOtpCodeAvailable(OtpCode otpCode);

    public int deleteOtpCodeByShipmentDetailIds(String shipmentDetailIds);
}
