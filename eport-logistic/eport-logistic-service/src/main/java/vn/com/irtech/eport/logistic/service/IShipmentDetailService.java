package vn.com.irtech.eport.logistic.service;

import java.util.LinkedHashMap;
import java.util.List;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;

/**
 * Shipment DetailsService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface IShipmentDetailService 
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
     * @return result
     */
    public int insertShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    public int updateShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Batch Delete Shipment Details
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentDetailByIds(String ids);

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    public int deleteShipmentDetailById(Long id);

    public List<ShipmentDetail> selectShipmentDetailByIds(String ids);

    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno);

    public List<String> selectBlList(String keyString);

    public long countShipmentDetailList(ShipmentDetail shipmentDetail);

    public int updateDoStatusShipmentDetail(ShipmentDetail shipmentDetail);
    
    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * @return result
     */
    public List<ShipmentWaitExec> selectListShipmentWaitExec();

    public List<ShipmentDetail[][]> getContPosition(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> shipmentDetails);
    
    public boolean calculateMovingCont(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails);

    public boolean makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails);

    public String getGroupNameByTaxCode(String taxCode);

    public boolean makeOrderSendContEmpty(List<ShipmentDetail> shipmentDetails);

    public boolean makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails);
}
