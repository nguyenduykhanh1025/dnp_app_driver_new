package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.ShipmentDetailHist;

/**
 * ShipmentDetailHist Service Interface
 * 
 * @author Trong Hieu
 * @date 2020-10-06
 */
public interface IShipmentDetailHistService 
{
    /**
     * Get ShipmentDetailHist
     * 
     * @param id ShipmentDetailHist ID
     * @return ShipmentDetailHist
     */
    public ShipmentDetailHist selectShipmentDetailHistById(Long id);

    /**
     * Get ShipmentDetailHist List
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return ShipmentDetailHist List
     */
    public List<ShipmentDetailHist> selectShipmentDetailHistList(ShipmentDetailHist shipmentDetailHist);

    /**
     * Add ShipmentDetailHist
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return result
     */
    public int insertShipmentDetailHist(ShipmentDetailHist shipmentDetailHist);

    /**
     * Update ShipmentDetailHist
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return result
     */
    public int updateShipmentDetailHist(ShipmentDetailHist shipmentDetailHist);

    /**
     * Batch Delete ShipmentDetailHist
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentDetailHistByIds(String ids);

    /**
     * Delete ShipmentDetailHist
     * 
     * @param id ShipmentDetailHistID
     * @return result
     */
    public int deleteShipmentDetailHistById(Long id);
}
