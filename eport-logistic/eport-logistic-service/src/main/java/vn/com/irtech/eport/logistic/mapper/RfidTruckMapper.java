package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.RfidTruck;

/**
 * RFID TruckMapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-14
 */
public interface RfidTruckMapper 
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
     * @return Result
     */
    public int insertRfidTruck(RfidTruck rfidTruck);

    /**
     * Update RFID Truck
     * 
     * @param rfidTruck RFID Truck
     * @return Result
     */
    public int updateRfidTruck(RfidTruck rfidTruck);

    /**
     * Delete RFID Truck
     * 
     * @param id RFID TruckID
     * @return result
     */
    public int deleteRfidTruckById(Long id);

    /**
     * Batch Delete RFID Truck
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteRfidTruckByIds(String[] ids);
}
