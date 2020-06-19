package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.Truck;

/**
 * TruckService Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface ITruckService 
{
    /**
     * Get Truck
     * 
     * @param id TruckID
     * @return Truck
     */
    public Truck selectTruckById(Long id);

    /**
     * Get Truck List
     * 
     * @param truck Truck
     * @return Truck List
     */
    public List<Truck> selectTruckList(Truck truck);

    /**
     * Add Truck
     * 
     * @param truck Truck
     * @return result
     */
    public int insertTruck(Truck truck);

    /**
     * Update Truck
     * 
     * @param truck Truck
     * @return result
     */
    public int updateTruck(Truck truck);

    /**
     * Batch Delete Truck
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteTruckByIds(String ids);

    /**
     * Delete Truck
     * 
     * @param id TruckID
     * @return result
     */
    public int deleteTruckById(Long id);
    public int checkPlateNumberUnique(String plateNumber);
    public int updateDelFlagByIds(String ids);
}
