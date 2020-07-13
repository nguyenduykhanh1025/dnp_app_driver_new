package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.PickupAssign;

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
}
