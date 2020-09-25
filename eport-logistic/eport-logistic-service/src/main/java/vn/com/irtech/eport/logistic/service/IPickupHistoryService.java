package vn.com.irtech.eport.logistic.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.form.PickupHistoryDetail;
import vn.com.irtech.eport.logistic.form.PickupHistoryForm;
import vn.com.irtech.eport.logistic.form.PickupPlanForm;
import vn.com.irtech.eport.logistic.form.VesselVoyageMc;

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
     * @param shipmentDetailId
     * @return  int
     */
    public int checkPickupHistoryExists(Long shipmentId, Long shipmentDetailId);

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
     * Select Delivering Driver Info
     * 
     * @return PickupHistory
     */
    public List<PickupHistory> selectDeliveringDriverInfo(PickupHistory pickupHistory);
    
    /**
     * Check plate number is unavailable
     * 
     * @param driverId
     * @return int
     */
    public int checkPlateNumberIsUnavailable(PickupHistory pickupHistory);

    public List<PickupHistory> selectDeliveringDriverInfoTable(PickupHistory pickupHistory);

    /**
     * Select vessel voyage list
     * 
     * @param pickupHistory
     * @return List<VesselVoyageMc>
     */
    public List<VesselVoyageMc> selectVesselVoyageList(PickupHistory pickupHistory); 
    
    /**
     * Select pickup list form mc plan
     * 
     * @param pickupHistory
     * @return List<PickupPlanForm>
     */
    public List<PickupPlanForm> selectPickupListForMcPlan(PickupHistory pickupHistory);
    
    /**
     * 
     * Count pickup history list
     * 
     * @param pickupHistory
     * @return int
     */
    public int countPickupHistoryList(PickupHistory pickupHistory);
    
    /**
     * Delete pickup history by condition
     * 
     * @param pickupHistory
     * @return int
     */
    public int deletePickupHistoryByCondition(PickupHistory pickupHistory);
}