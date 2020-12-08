package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.ReeferInfoMapper;
import vn.com.irtech.eport.logistic.domain.ReeferInfo;
import vn.com.irtech.eport.logistic.service.IReeferInfoService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * ReeferService Business Processing
 * 
 * @author Khanh
 * @date 2020-12-07
 */
@Service
public class ReeferInfoServiceImpl implements IReeferInfoService 
{
    @Autowired
    private ReeferInfoMapper reeferInfoMapper;

    /**
     * Get Reefer
     * 
     * @param id ReeferID
     * @return Reefer
     */
    @Override
    public ReeferInfo selectReeferInfoById(Long id)
    {
        return reeferInfoMapper.selectReeferInfoById(id);
    }

    /**
     * Get Reefer List
     * 
     * @param reeferInfo Reefer
     * @return Reefer
     */
    @Override
    public List<ReeferInfo> selectReeferInfoList(ReeferInfo reeferInfo)
    {
        return reeferInfoMapper.selectReeferInfoList(reeferInfo);
    }

    @Override
    /**
     * Get Reefer List
     * 
     * @param reeferInfo Reefer
     * @return Reefer List
     */
    public List<ReeferInfo> selectReeferInfoListByIdShipmentDetail(Long shipmentDetailId) {
    	return reeferInfoMapper.selectReeferInfoListByIdShipmentDetail(shipmentDetailId);
    }

    /**
     * Add Reefer
     * 
     * @param reeferInfo Reefer
     * @return result
     */
    @Override
    public int insertReeferInfo(ReeferInfo reeferInfo)
    {
        reeferInfo.setCreateTime(DateUtils.getNowDate());
        return reeferInfoMapper.insertReeferInfo(reeferInfo);
    }

    /**
     * Update Reefer
     * 
     * @param reeferInfo Reefer
     * @return result
     */
    @Override
    public int updateReeferInfo(ReeferInfo reeferInfo)
    {
        reeferInfo.setUpdateTime(DateUtils.getNowDate());
        return reeferInfoMapper.updateReeferInfo(reeferInfo);
    }

    /**
     * Delete Reefer By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteReeferInfoByIds(String ids)
    {
        return reeferInfoMapper.deleteReeferInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Reefer
     * 
     * @param id ReeferID
     * @return result
     */
    @Override
    public int deleteReeferInfoById(Long id)
    {
        return reeferInfoMapper.deleteReeferInfoById(id);
    }
}
