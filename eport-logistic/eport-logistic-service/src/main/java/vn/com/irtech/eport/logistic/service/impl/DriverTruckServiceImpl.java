package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.DriverTruckMapper;
import vn.com.irtech.eport.logistic.domain.DriverTruck;
import vn.com.irtech.eport.logistic.service.IDriverTruckService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * driver_truckService Business Processing
 * 
 * @author admin
 * @date 2020-06-18
 */
@Service
public class DriverTruckServiceImpl implements IDriverTruckService 
{
    @Autowired
    private DriverTruckMapper driverTruckMapper;

    /**
     * Get driver_truck
     * 
     * @param driverId driver_truckID
     * @return driver_truck
     */
    @Override
    public DriverTruck selectDriverTruckById(Long driverId)
    {
        return driverTruckMapper.selectDriverTruckById(driverId);
    }

    /**
     * Get driver_truck List
     * 
     * @param driverTruck driver_truck
     * @return driver_truck
     */
    @Override
    public List<DriverTruck> selectDriverTruckList(DriverTruck driverTruck)
    {
        return driverTruckMapper.selectDriverTruckList(driverTruck);
    }

    /**
     * Add driver_truck
     * 
     * @param driverTruck driver_truck
     * @return result
     */
    @Override
    public int insertDriverTruck(DriverTruck driverTruck)
    {
        return driverTruckMapper.insertDriverTruck(driverTruck);
    }

    /**
     * Update driver_truck
     * 
     * @param driverTruck driver_truck
     * @return result
     */
    @Override
    public int updateDriverTruck(DriverTruck driverTruck)
    {
        return driverTruckMapper.updateDriverTruck(driverTruck);
    }

    /**
     * Delete driver_truck By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteDriverTruckByIds(String ids)
    {
        return driverTruckMapper.deleteDriverTruckByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete driver_truck
     * 
     * @param driverId driver_truckID
     * @return result
     */
    @Override
    public int deleteDriverTruckById(Long driverId)
    {
        return driverTruckMapper.deleteDriverTruckById(driverId);
    }
}
