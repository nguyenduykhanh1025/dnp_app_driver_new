package vn.com.irtech.eport.logistic.service.impl;

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
    private OtpCodeMapper otpCodeMapper;

    /**
     * Get otp Code
     * 
     * @param id otp CodeID
     * @return otp Code
     */
    @Override
    public OtpCode selectOtpCodeById(Long id)
    {
        return otpCodeMapper.selectOtpCodeById(id);
    }

    /**
     * Get otp Code List
     * 
     * @param otpCode otp Code
     * @return otp Code
     */
    @Override
    public List<OtpCode> selectOtpCodeList(OtpCode otpCode)
    {
        return otpCodeMapper.selectOtpCodeList(otpCode);
    }

    /**
     * Add otp Code
     * 
     * @param otpCode otp Code
     * @return result
     */
    @Override
    public int insertOtpCode(OtpCode otpCode)
    {
        otpCode.setCreateTime(DateUtils.getNowDate());
        return otpCodeMapper.insertOtpCode(otpCode);
    }

    /**
     * Update otp Code
     * 
     * @param otpCode otp Code
     * @return result
     */
    @Override
    public int updateOtpCode(OtpCode otpCode)
    {
        return otpCodeMapper.updateOtpCode(otpCode);
    }

    /**
     * Delete otp Code By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteOtpCodeByIds(String ids)
    {
        return otpCodeMapper.deleteOtpCodeByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete otp Code
     * 
     * @param id otp CodeID
     * @return result
     */
    @Override
    public int deleteOtpCodeById(Long id)
    {
        return otpCodeMapper.deleteOtpCodeById(id);
    }

    @Override
    public OtpCode selectOtpCodeByshipmentDetailId(String shipmentDetailId)
    {
        return otpCodeMapper.selectOtpCodeByshipmentDetailId(shipmentDetailId);
    }

    @Override
    public int verifyOtpCodeAvailable(OtpCode otpCode) {
        return otpCodeMapper.verifyOtpCodeAvailable(otpCode);
    }

    @Override
    public int deleteOtpCodeByShipmentDetailIds(String shipmentDetailIds) {
        return otpCodeMapper.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);
    }
}
