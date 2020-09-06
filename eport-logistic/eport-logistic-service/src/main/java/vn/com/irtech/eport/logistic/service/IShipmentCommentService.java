package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;

/**
 * Shipment CommentService Interface
 * 
 * @author IRTech
 * @date 2020-09-06
 */
public interface IShipmentCommentService 
{
    /**
     * Get Shipment Comment
     * 
     * @param id Shipment CommentID
     * @return Shipment Comment
     */
    public ShipmentComment selectShipmentCommentById(Long id);

    /**
     * Get Shipment Comment List
     * 
     * @param shipmentComment Shipment Comment
     * @return Shipment Comment List
     */
    public List<ShipmentComment> selectShipmentCommentList(ShipmentComment shipmentComment);

    /**
     * Add Shipment Comment
     * 
     * @param shipmentComment Shipment Comment
     * @return result
     */
    public int insertShipmentComment(ShipmentComment shipmentComment);

    /**
     * Update Shipment Comment
     * 
     * @param shipmentComment Shipment Comment
     * @return result
     */
    public int updateShipmentComment(ShipmentComment shipmentComment);

    /**
     * Batch Delete Shipment Comment
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentCommentByIds(String ids);

    /**
     * Delete Shipment Comment
     * 
     * @param id Shipment CommentID
     * @return result
     */
    public int deleteShipmentCommentById(Long id);
}
