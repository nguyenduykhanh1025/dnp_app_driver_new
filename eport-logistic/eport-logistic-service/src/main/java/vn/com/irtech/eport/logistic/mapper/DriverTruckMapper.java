package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.DriverTruck;

/**
 * driver_truckMapper Interface
 * 
 * @author admin
 * @date 2020-06-18
 */
public interface DriverTruckMapper 
{
    /**
     * Get driver_truck
     * 
     * @param driverId driver_truckID
     * @return driver_truck
     */
    public DriverTruck selectDriverTruckById(Long driverId);

    /**
     * Get driver_truck List
     * 
     * @param driverTruck driver_truck
     * @return driver_truck List
     */
    public List<DriverTruck> selectDriverTruckList(DriverTruck driverTruck);

    /**
     * Add driver_truck
     * 
     * @param driverTruck driver_truck
     * @return Result
     */
    public int insertDriverTruck(DriverTruck driverTruck);

    /**
     * Update driver_truck
     * 
     * @param driverTruck driver_truck
     * @return Result
     */
    public int updateDriverTruck(DriverTruck driverTruck);

    /**
     * Delete driver_truck
     * 
     * @param driverId driver_truckID
     * @return result
     */
    public int deleteDriverTruckById(Long driverId);

    /**
     * Batch Delete driver_truck
     * 
     * @param driverIds IDs
     * @return result
     */
    public int deleteDriverTruckByIds(String[] driverIds);
}
