package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;

/**
 * Shipment CommentMapper Interface
 * 
 * @author IRTech
 * @date 2020-09-06
 */
public interface ShipmentCommentMapper 
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
     * @return Result
     */
    public int insertShipmentComment(ShipmentComment shipmentComment);

    /**
     * Update Shipment Comment
     * 
     * @param shipmentComment Shipment Comment
     * @return Result
     */
    public int updateShipmentComment(ShipmentComment shipmentComment);

    /**
     * Delete Shipment Comment
     * 
     * @param id Shipment CommentID
     * @return result
     */
    public int deleteShipmentCommentById(Long id);

    /**
     * Batch Delete Shipment Comment
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentCommentByIds(String[] ids);
}
