package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;

/**
 * Truck EntranceService Interface
 * 
 * @author Trong Hieu
 * @date 2020-12-31
 */
public interface ITruckEntranceService 
{
    /**
     * Get Truck Entrance
     * 
     * @param id Truck EntranceID
     * @return Truck Entrance
     */
    public TruckEntrance selectTruckEntranceById(Long id);

    /**
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance List
     */
    public List<TruckEntrance> selectTruckEntranceList(TruckEntrance truckEntrance);

    /**
     * Add Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return result
     */
    public int insertTruckEntrance(TruckEntrance truckEntrance);

    /**
     * Update Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return result
     */
    public int updateTruckEntrance(TruckEntrance truckEntrance);

    /**
     * Batch Delete Truck Entrance
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteTruckEntranceByIds(String ids);

    /**
     * Delete Truck Entrance
     * 
     * @param id Truck EntranceID
     * @return result
     */
    public int deleteTruckEntranceById(Long id);
}
