package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.PickupAssignMapper;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
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
    public List<String> getDriverOwner(PickupAssign pickupAssign) {
        return pickupAssignMapper.getDriverOwner(pickupAssign);
    }
}
