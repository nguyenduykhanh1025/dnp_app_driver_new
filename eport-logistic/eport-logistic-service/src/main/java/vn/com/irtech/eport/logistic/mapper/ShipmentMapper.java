package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.Shipment;

/**
 * ShipmentMapper Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface ShipmentMapper 
{
    /**
     * Get Shipment
     * 
     * @param id ShipmentID
     * @return Shipment
     */
    public Shipment selectShipmentById(Long id);

    /**
     * Get Shipment List
     * 
     * @param shipment Shipment
     * @return Shipment List
     */
    public List<Shipment> selectShipmentList(Shipment shipment);

    /**
     * Add Shipment
     * 
     * @param shipment Shipment
     * @return Result
     */
    public int insertShipment(Shipment shipment);

    /**
     * Update Shipment
     * 
     * @param shipment Shipment
     * @return Result
     */
    public int updateShipment(Shipment shipment);

    /**
     * Delete Shipment
     * 
     * @param id ShipmentID
     * @return result
     */
    public int deleteShipmentById(Long id);

    /**
     * Batch Delete Shipment
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentByIds(String[] ids);

    public int checkBillBookingNoUnique(Shipment shipment);

    public List<Shipment> selectShipmentListForOm(Shipment shipment);
}
