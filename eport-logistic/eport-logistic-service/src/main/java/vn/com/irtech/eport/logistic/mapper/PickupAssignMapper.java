package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;

/**
 * Pickup AssignMapper Interface
 * 
 * @author admin
 * @date 2020-06-27
 */
public interface PickupAssignMapper 
{
    /**
     * Get Pickup Assign
     * 
     * @param id Pickup AssignID
     * @return Pickup Assign
     */
    public PickupAssign selectPickupAssignById(Long id);

    /**
     * Get Pickup Assign List
     * 
     * @param pickupAssign Pickup Assign
     * @return Pickup Assign List
     */
    public List<PickupAssign> selectPickupAssignList(PickupAssign pickupAssign);

    /**
     * Add Pickup Assign
     * 
     * @param pickupAssign Pickup Assign
     * @return Result
     */
    public int insertPickupAssign(PickupAssign pickupAssign);

    /**
     * Update Pickup Assign
     * 
     * @param pickupAssign Pickup Assign
     * @return Result
     */
    public int updatePickupAssign(PickupAssign pickupAssign);

    /**
     * Delete Pickup Assign
     * 
     * @param id Pickup AssignID
     * @return result
     */
    public int deletePickupAssignById(Long id);

    /**
     * Batch Delete Pickup Assign
     * 
     * @param ids IDs
     * @return result
     */
    public int deletePickupAssignByIds(String[] ids);

    public List<String> getDriverOwners(PickupAssign pickupAssign);

    public List<String> getPhoneNumbersByDriverOwner(PickupAssign pickupAssign);

    public PickupAssign getInforOutSourceByPhoneNumber(PickupAssign pickupAssign);

    /**
     * 
     * @param driverId
     * @param serviceType
     * @return
     */
    public List<PickupAssignForm> selectPickupAssignListByDriverId(@Param("driverId") Long driverId, @Param("shipmentId") Long shipmentId); 

    /**
     * Get remark pickup_assgin follow batch
     */
    public String getRemarkFollowBatchByShipmentId(PickupAssign pickupAssign);

    /**
     * Get remark pickup_assgin follow container
     */
    public String getRemarkFollowContainerByShipmentDetailId(PickupAssign pickupAssign);

    /**
     * Select Pickup Assign By Shipment Id
     * 
     * @param pickupAssign
     * @return PickupAssign
     */
    public PickupAssign selectPickupAssignByShipmentId(PickupAssign pickupAssign);
}
