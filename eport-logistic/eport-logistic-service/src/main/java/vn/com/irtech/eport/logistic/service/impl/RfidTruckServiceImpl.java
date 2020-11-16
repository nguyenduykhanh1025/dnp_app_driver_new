package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.RfidTruck;
import vn.com.irtech.eport.logistic.mapper.RfidTruckMapper;
import vn.com.irtech.eport.logistic.service.IRfidTruckService;

/**
 * RFID TruckService Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-11-14
 */
@Service
public class RfidTruckServiceImpl implements IRfidTruckService 
{
    @Autowired
    private RfidTruckMapper rfidTruckMapper;

    /**
     * Get RFID Truck
     * 
     * @param id RFID TruckID
     * @return RFID Truck
     */
    @Override
    public RfidTruck selectRfidTruckById(Long id)
    {
        return rfidTruckMapper.selectRfidTruckById(id);
    }

    /**
     * Get RFID Truck List
     * 
     * @param rfidTruck RFID Truck
     * @return RFID Truck
     */
    @Override
    public List<RfidTruck> selectRfidTruckList(RfidTruck rfidTruck)
    {
        return rfidTruckMapper.selectRfidTruckList(rfidTruck);
    }

    /**
     * Add RFID Truck
     * 
     * @param rfidTruck RFID Truck
     * @return result
     */
    @Override
    public int insertRfidTruck(RfidTruck rfidTruck)
    {
        rfidTruck.setCreateTime(DateUtils.getNowDate());
        return rfidTruckMapper.insertRfidTruck(rfidTruck);
    }

    /**
     * Update RFID Truck
     * 
     * @param rfidTruck RFID Truck
     * @return result
     */
    @Override
    public int updateRfidTruck(RfidTruck rfidTruck)
    {
        rfidTruck.setUpdateTime(DateUtils.getNowDate());
        return rfidTruckMapper.updateRfidTruck(rfidTruck);
    }

    /**
     * Delete RFID Truck By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteRfidTruckByIds(String ids)
    {
        return rfidTruckMapper.deleteRfidTruckByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete RFID Truck
     * 
     * @param id RFID TruckID
     * @return result
     */
    @Override
    public int deleteRfidTruckById(Long id)
    {
        return rfidTruckMapper.deleteRfidTruckById(id);
    }
}
