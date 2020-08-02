package vn.com.irtech.eport.logistic.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.form.PickupHistoryDetail;
import vn.com.irtech.eport.logistic.form.PickupHistoryForm;

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

    public List<PickupHistory> selectPickupHistoryListForOmSupport(PickupHistory pickupHistory);

    public List<PickupHistoryForm> selectPickupHistoryForDriver(@Param("userId") Long userId);

    /**
     * 
     * @param driverId
     * @return
     */
    public List<Pickup> selectPickupListByDriverId(Long driverId);

    public PickupHistoryDetail selectPickupHistoryDetailById(@Param("driverId") Long driverId, @Param("pickupId") Long pickupId);

    /**
     * Check pickup history exists
     * 
     * @param shipmentId
     * @param containerNo
     * @return  int
     */
    public int checkPickupHistoryExists(Long shipmentId, String containerNo);

    /**
     * Check possible pickup
     * 
     * @param driverId
     * @param serviceType
     * @param shipmentDetail
     * @return Boolean
     */
    public Boolean checkPossiblePickup(Long driverId, Integer serviceType, ShipmentDetail shipmentDetail);

    /**
     * Select Delievering Driver Info
     * 
     * @return PickupHistory
     */
    public List<PickupHistory> selectDeliveringDriverInfo(PickupHistory pickupHistory);

}