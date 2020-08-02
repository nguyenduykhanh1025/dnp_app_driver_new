package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.PickupAssignMapper;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Pickup AssignService Business Processing
 * 
 * @author admin
 * @date 2020-06-27
 */
@Service
public class PickupAssignServiceImpl implements IPickupAssignService {
    @Autowired
    private PickupAssignMapper pickupAssignMapper;

    /**
     * Get Pickup Assign
     * 
     * @param id Pickup AssignID
     * @return Pickup Assign
     */
    @Override
    public PickupAssign selectPickupAssignById(Long id) {
        return pickupAssignMapper.selectPickupAssignById(id);
    }

    /**
     * Get Pickup Assign List
     * 
     * @param pickupAssign Pickup Assign
     * @return Pickup Assign
     */
    @Override
    public List<PickupAssign> selectPickupAssignList(PickupAssign pickupAssign) {
        return pickupAssignMapper.selectPickupAssignList(pickupAssign);
    }

    /**
     * Add Pickup Assign
     * 
     * @param pickupAssign Pickup Assign
     * @return result
     */
    @Override
    public int insertPickupAssign(PickupAssign pickupAssign) {
        pickupAssign.setCreateTime(DateUtils.getNowDate());
        return pickupAssignMapper.insertPickupAssign(pickupAssign);
    }

    /**
     * Update Pickup Assign
     * 
     * @param pickupAssign Pickup Assign
     * @return result
     */
    @Override
    public int updatePickupAssign(PickupAssign pickupAssign) {
        pickupAssign.setUpdateTime(DateUtils.getNowDate());
        return pickupAssignMapper.updatePickupAssign(pickupAssign);
    }

    /**
     * Delete Pickup Assign By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deletePickupAssignByIds(String ids) {
        return pickupAssignMapper.deletePickupAssignByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Pickup Assign
     * 
     * @param id Pickup AssignID
     * @return result
     */
    @Override
    public int deletePickupAssignById(Long id) {
        return pickupAssignMapper.deletePickupAssignById(id);
    }

    @Override
    public List<String> getDriverOwners(PickupAssign pickupAssign) {
        return pickupAssignMapper.getDriverOwners(pickupAssign);
    }

    @Override
    public List<String> getPhoneNumbersByDriverOwner(PickupAssign pickupAssign) {
        return pickupAssignMapper.getPhoneNumbersByDriverOwner(pickupAssign);
    }

    @Override
    public PickupAssign getInforOutSourceByPhoneNumber(PickupAssign pickupAssign) {
        return pickupAssignMapper.getInforOutSourceByPhoneNumber(pickupAssign);
    }

    @Override
    public List<PickupAssignForm> selectPickupAssignListByDriverId(Long driverId, Long shipmentId) {
        return pickupAssignMapper.selectPickupAssignListByDriverId(driverId, shipmentId);
    }

    @Override
    public String getRemarkFollowBatchByShipmentId(PickupAssign pickupAssign) {
        return pickupAssignMapper.getRemarkFollowBatchByShipmentId(pickupAssign);
    }

    @Override
    public String getRemarkFollowContainerByShipmentDetailId(PickupAssign pickupAssign) {
        return pickupAssignMapper.getRemarkFollowContainerByShipmentDetailId(pickupAssign);
    }

    /**
     * Select Pickup Assign By Shipment Id
     * 
     * @param pickupAssign
     * @return PickupAssign
     */
    @Override
    public PickupAssign selectPickupAssignByShipmentId(PickupAssign pickupAssign) {
        return pickupAssignMapper.selectPickupAssignByShipmentId(pickupAssign);
    }
    
}
