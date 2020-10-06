package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.ShipmentDetailHist;

/**
 * Shipment Detail History Mapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-10-06
 */
public interface ShipmentDetailHistMapper 
{
    /**
     * Get Shipment Detail History
     * 
     * @param id 
     * @return ShipmentDetailHist
     */
    public ShipmentDetailHist selectShipmentDetailHistById(Long id);

    /**
     * Get ShipmentDetailHist List
     * 
     * @param shipmentDetailHist shipmentDetailHist
     * @return shipmentDetailHist List
     */
    public List<ShipmentDetailHist> selectShipmentDetailHistList(ShipmentDetailHist shipmentDetailHist);

    /**
     * Add shipmentDetailHist
     * 
     * @param shipmentDetailHist shipmentDetailHist
     * @return Result
     */
    public int insertShipmentDetailHist(ShipmentDetailHist shipmentDetailHist);

    /**
     * Update shipmentDetailHist
     * 
     * @param shipmentDetailHist shipmentDetailHist
     * @return Result
     */
    public int updateShipmentDetailHist(ShipmentDetailHist shipmentDetailHist);

    /**
     * Delete shipmentDetailHist
     * 
     * @param id shipmentDetailHistID
     * @return result
     */
    public int deleteShipmentDetailHistById(Long id);

    /**
     * Batch Delete shipmentDetailHist
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentDetailHistByIds(String[] ids);
}
