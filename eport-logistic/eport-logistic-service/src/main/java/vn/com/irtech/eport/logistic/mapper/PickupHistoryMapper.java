package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.PickupHistory;

/**
 * Pickup history Mapper Interface
 * 
 * @author baohv
 * @date 2020-06-27
 */
public interface PickupHistoryMapper 
{
    /**
     * Get Pickup history
     * 
     * @param id Pickup historyID
     * @return Pickup history
     */
    public PickupHistory selectPickupHistoryById(Long id);

    /**
     * Get Pickup history List
     * 
     * @param pickupHistory Pickup history
     * @return Pickup history List
     */
    public List<PickupHistory> selectPickupHistoryList(PickupHistory pickupHistory);
    
    /**
     * Get Pickup history without yard position List
     * 
     * @return Pickup history List
     */
    public List<PickupHistory> selectPickupHistoryWithoutYardPostion();

    /**
     * Add Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return Result
     */
    public int insertPickupHistory(PickupHistory pickupHistory);

    /**
     * Update Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return Result
     */
    public int updatePickupHistory(PickupHistory pickupHistory);

    /**
     * Delete Pickup history
     * 
     * @param id Pickup historyID
     * @return result
     */
    public int deletePickupHistoryById(Long id);

    /**
     * Batch Delete Pickup history
     * 
     * @param ids IDs
     * @return result
     */
    public int deletePickupHistoryByIds(String[] ids);
}