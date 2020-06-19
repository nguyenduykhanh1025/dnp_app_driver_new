package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.Truck;

/**
 * TruckMapper Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface TruckMapper 
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
     * @return Result
     */
    public int insertTruck(Truck truck);

    /**
     * Update Truck
     * 
     * @param truck Truck
     * @return Result
     */
    public int updateTruck(Truck truck);

    /**
     * Delete Truck
     * 
     * @param id TruckID
     * @return result
     */
    public int deleteTruckById(Long id);

    /**
     * Batch Delete Truck
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteTruckByIds(String[] ids);
    public int checkPlateNumberUnique(String plateNumber);
    public int updateDelFlagByIds(String[] ids);
}
