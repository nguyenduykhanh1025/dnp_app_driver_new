package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ReeferInfo;

/**
 * ReeferMapper Interface
 * 
 * @author Khanh
 * @date 2020-12-07
 */
public interface ReeferInfoMapper 
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
     * Get Reefer List Follow Shipment Deatail ID
     * 
     * @param reeferInfo Reefer
     * @return Reefer List
     */
    public List<ReeferInfo> selectReeferInfoListByIdShipmentDetail(Long shipmentDetailId);

    /**
     * Add Reefer
     * 
     * @param reeferInfo Reefer
     * @return Result
     */
    public int insertReeferInfo(ReeferInfo reeferInfo);

    /**
     * Update Reefer
     * 
     * @param reeferInfo Reefer
     * @return Result
     */
    public int updateReeferInfo(ReeferInfo reeferInfo);

    /**
     * Delete Reefer
     * 
     * @param id ReeferID
     * @return result
     */
    public int deleteReeferInfoById(Long id);

    /**
     * Batch Delete Reefer
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteReeferInfoByIds(String[] ids);
}
