package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

/**
 * Shipment DetailsMapper Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface ShipmentDetailMapper 
{
    /**
     * Get Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return Shipment Details
     */
    public ShipmentDetail selectShipmentDetailById(Long id);

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details List
     */
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail);

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return Result
     */
    public int insertShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return Result
     */
    public int updateShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    public int deleteShipmentDetailById(Long id);

    /**
     * Batch Delete Shipment Details
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentDetailByIds(String[] ids);

    public List<ShipmentDetail> selectShipmentDetailByIds(String[] ids);
}
