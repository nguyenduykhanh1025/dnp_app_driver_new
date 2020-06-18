package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.DriverTruck;

/**
 * driver_truckService Interface
 * 
 * @author admin
 * @date 2020-06-18
 */
public interface IDriverTruckService 
{
    /**
     * Get driver_truck
     * 
     * @param id driver_truckID
     * @return driver_truck
     */
    public DriverTruck selectDriverTruckById(Long id);

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
     * @return result
     */
    public int insertDriverTruck(DriverTruck driverTruck);

    /**
     * Update driver_truck
     * 
     * @param driverTruck driver_truck
     * @return result
     */
    public int updateDriverTruck(DriverTruck driverTruck);

    /**
     * Batch Delete driver_truck
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteDriverTruckByIds(String ids);

    /**
     * Delete driver_truck
     * 
     * @param id driver_truckID
     * @return result
     */
    public int deleteDriverTruckById(Long id);
}
