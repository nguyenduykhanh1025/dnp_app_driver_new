package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ShipmentTransport;

/**
 * Thong tin dieu xeService Interface
 * 
 * @author ruoyi
 * @date 2020-05-26
 */
public interface IShipmentTransportService 
{
    /**
     * Get Thong tin dieu xe
     * 
     * @param id Thong tin dieu xeID
     * @return Thong tin dieu xe
     */
    public ShipmentTransport selectShipmentTransportById(Long id);

    /**
     * Get Thong tin dieu xe List
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return Thong tin dieu xe List
     */
    public List<ShipmentTransport> selectShipmentTransportList(ShipmentTransport shipmentTransport);

    /**
     * Add Thong tin dieu xe
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return result
     */
    public int insertShipmentTransport(ShipmentTransport shipmentTransport);

    /**
     * Update Thong tin dieu xe
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return result
     */
    public int updateShipmentTransport(ShipmentTransport shipmentTransport);

    /**
     * Batch Delete Thong tin dieu xe
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentTransportByIds(String ids);

    /**
     * Delete Thong tin dieu xe
     * 
     * @param id Thong tin dieu xeID
     * @return result
     */
    public int deleteShipmentTransportById(Long id);
}
