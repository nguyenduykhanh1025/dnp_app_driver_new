package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.mapper.PickupHistoryMapper;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

/**
 * Pickup historyService Business Processing
 * 
 * @author baohv
 * @date 2020-06-27
 */
@Service
public class PickupHistoryServiceImpl implements IPickupHistoryService 
{
    @Autowired
    private PickupHistoryMapper pickupHistoryMapper;

    /**
     * Get Pickup history
     * 
     * @param id Pickup historyID
     * @return Pickup history
     */
    @Override
    public PickupHistory selectPickupHistoryById(Long id)
    {
        return pickupHistoryMapper.selectPickupHistoryById(id);
    }

    /**
     * Get Pickup history List
     * 
     * @param pickupHistory Pickup history
     * @return Pickup history
     */
    @Override
    public List<PickupHistory> selectPickupHistoryList(PickupHistory pickupHistory)
    {
        return pickupHistoryMapper.selectPickupHistoryList(pickupHistory);
    }
    
    /**
     * Get Pickup history without yard position List
     * 
     * @return Pickup history List
     */
    @Override
    public List<PickupHistory> selectPickupHistoryWithoutYardPostion(){
    	return pickupHistoryMapper.selectPickupHistoryWithoutYardPostion();
    }

    /**
     * Add Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return result
     */
    @Override
    public int insertPickupHistory(PickupHistory pickupHistory)
    {
        return pickupHistoryMapper.insertPickupHistory(pickupHistory);
    }

    /**
     * Update Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return result
     */
    @Override
    public int updatePickupHistory(PickupHistory pickupHistory)
    {
        return pickupHistoryMapper.updatePickupHistory(pickupHistory);
    }

    /**
     * Delete Pickup history By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deletePickupHistoryByIds(String ids)
    {
        return pickupHistoryMapper.deletePickupHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Pickup history
     * 
     * @param id Pickup historyID
     * @return result
     */
    @Override
    public int deletePickupHistoryById(Long id)
    {
        return pickupHistoryMapper.deletePickupHistoryById(id);
    }

    @Override
    public List<PickupHistory> selectPickupHistoryListForReport(PickupHistory pickupHistory) {
        return pickupHistoryMapper.selectPickupHistoryListForReport(pickupHistory);
    }

    @Override
    public List<PickupHistory> selectPickupHistoryListForHistory(PickupHistory pickupHistory) {
        return pickupHistoryMapper.selectPickupHistoryListForHistory(pickupHistory);
    }
}
