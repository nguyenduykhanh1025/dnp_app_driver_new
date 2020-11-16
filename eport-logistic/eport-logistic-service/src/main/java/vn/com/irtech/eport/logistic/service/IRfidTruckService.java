package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.RfidTruck;

/**
 * RFID TruckService Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-14
 */
public interface IRfidTruckService 
{
    /**
     * Get RFID Truck
     * 
     * @param id RFID TruckID
     * @return RFID Truck
     */
    public RfidTruck selectRfidTruckById(Long id);

    /**
     * Get RFID Truck List
     * 
     * @param rfidTruck RFID Truck
     * @return RFID Truck List
     */
    public List<RfidTruck> selectRfidTruckList(RfidTruck rfidTruck);

    /**
     * Add RFID Truck
     * 
     * @param rfidTruck RFID Truck
     * @return result
     */
    public int insertRfidTruck(RfidTruck rfidTruck);

    /**
     * Update RFID Truck
     * 
     * @param rfidTruck RFID Truck
     * @return result
     */
    public int updateRfidTruck(RfidTruck rfidTruck);

    /**
     * Batch Delete RFID Truck
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteRfidTruckByIds(String ids);

    /**
     * Delete RFID Truck
     * 
     * @param id RFID TruckID
     * @return result
     */
    public int deleteRfidTruckById(Long id);
}
