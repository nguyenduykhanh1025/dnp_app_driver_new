package vn.com.irtech.eport.logistic.service;

import java.util.List;
import java.util.Map;

import vn.com.irtech.eport.logistic.domain.PickupHistory;

/**
 * Pickup history Service Interface
 * 
 * @author baohv
 * @date 2020-06-27
 */
public interface IPickupHistoryService 
{
    /**
     * Get Pickup history
     * 
     * @param id Pickup historyID
     * @return Pickup history
     */
    public PickupHistory selectPickupHistoryById(Long id);

    /**
     * Get Pickup history without yard position List
     * 
     * @return Pickup history List
     */
    public List<PickupHistory> selectPickupHistoryWithoutYardPostion(Map<String, String> searchParams);
    
    /**
     * Get Pickup history has yard position
     * 
     * @return Pickup history List
     */
    public List<PickupHistory> selectPickupHistoryHasYardPostion(Map<String, String> searchParams);

    /**
     * Add Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return result
     */
    public int insertPickupHistory(PickupHistory pickupHistory);

    /**
     * Update Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return result
     */
    public int updatePickupHistory(PickupHistory pickupHistory);

    /**
     * Batch Delete Pickup history
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deletePickupHistoryByIds(String ids);

    /**
     * Delete Pickup history
     * 
     * @param id Pickup historyID
     * @return result
     */
    public int deletePickupHistoryById(Long id);

    public List<PickupHistory> selectPickupHistoryListForReport(PickupHistory pickupHistory);

    public List<PickupHistory> selectPickupHistoryListForHistory(PickupHistory pickupHistory);
}