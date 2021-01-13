package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;

/**
 * Truck EntranceMapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-12-31
 */
public interface TruckEntranceMapper 
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
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance List
     */
    public List<TruckEntrance> selectTruckEntranceFollowTruckNoList(TruckEntrance truckEntrance);
    
    /**
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance List
     */
    public List<TruckEntrance> selectTruckEntranceListOderByCreateTime(TruckEntrance truckEntrance);
    
    /**
     * Add Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return Result
     */
    public int insertTruckEntrance(TruckEntrance truckEntrance);

    /**
     * Update Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return Result
     */
    public int updateTruckEntrance(TruckEntrance truckEntrance);

    /**
     * Delete Truck Entrance
     * 
     * @param id Truck EntranceID
     * @return result
     */
    public int deleteTruckEntranceById(Long id);

    /**
     * Batch Delete Truck Entrance
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteTruckEntranceByIds(String[] ids);
}
