package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ReeferInfo;

/**
 * ReeferService Interface
 * 
 * @author Khanh
 * @date 2020-12-07
 */
public interface IReeferInfoService 
{
    /**
     * Get Reefer
     * 
     * @param id ReeferID
     * @return Reefer
     */
    public ReeferInfo selectReeferInfoById(Long id);

    /**
     * Get Reefer List
     * 
     * @param reeferInfo Reefer
     * @return Reefer List
     */
    public List<ReeferInfo> selectReeferInfoList(ReeferInfo reeferInfo);
    
    /**
     * Get Reefer List
     * 
     * @param reeferInfo Reefer
     * @return Reefer List
     */
    public List<ReeferInfo> selectReeferInfoListByIdShipmentDetail(Long shipmentDetailId);

    /**
     * Add Reefer
     * 
     * @param reeferInfo Reefer
     * @return result
     */
    public int insertReeferInfo(ReeferInfo reeferInfo);

    /**
     * Update Reefer
     * 
     * @param reeferInfo Reefer
     * @return result
     */
    public int updateReeferInfo(ReeferInfo reeferInfo);

    /**
     * Batch Delete Reefer
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteReeferInfoByIds(String ids);

    /**
     * Delete Reefer
     * 
     * @param id ReeferID
     * @return result
     */
    public int deleteReeferInfoById(Long id);
}
