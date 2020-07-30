package vn.com.irtech.eport.logistic.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;

/**
 * Pickup AssignService Interface
 * 
 * @author admin
 * @date 2020-06-27
 */
public interface IPickupAssignService 
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
     * @return result
     */
    public int insertPickupAssign(PickupAssign pickupAssign);

    /**
     * Update Pickup Assign
     * 
     * @param pickupAssign Pickup Assign
     * @return result
     */
    public int updatePickupAssign(PickupAssign pickupAssign);

    /**
     * Batch Delete Pickup Assign
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deletePickupAssignByIds(String ids);

    /**
     * Delete Pickup Assign
     * 
     * @param id Pickup AssignID
     * @return result
     */
    public int deletePickupAssignById(Long id);

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
}
