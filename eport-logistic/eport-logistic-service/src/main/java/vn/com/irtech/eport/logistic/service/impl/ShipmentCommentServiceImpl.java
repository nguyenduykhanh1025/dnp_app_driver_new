package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.ShipmentCommentMapper;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Shipment CommentService Business Processing
 * 
 * @author IRTech
 * @date 2020-09-06
 */
@Service
public class ShipmentCommentServiceImpl implements IShipmentCommentService 
{
    @Autowired
    private ShipmentCommentMapper shipmentCommentMapper;

    /**
     * Get Shipment Comment
     * 
     * @param id Shipment CommentID
     * @return Shipment Comment
     */
    @Override
    public ShipmentComment selectShipmentCommentById(Long id)
    {
        return shipmentCommentMapper.selectShipmentCommentById(id);
    }

    /**
     * Get Shipment Comment List
     * 
     * @param shipmentComment Shipment Comment
     * @return Shipment Comment
     */
    @Override
    public List<ShipmentComment> selectShipmentCommentList(ShipmentComment shipmentComment)
    {
        return shipmentCommentMapper.selectShipmentCommentList(shipmentComment);
    }

    /**
     * Add Shipment Comment
     * 
     * @param shipmentComment Shipment Comment
     * @return result
     */
    @Override
    public int insertShipmentComment(ShipmentComment shipmentComment)
    {
        shipmentComment.setCreateTime(DateUtils.getNowDate());
        return shipmentCommentMapper.insertShipmentComment(shipmentComment);
    }

    /**
     * Update Shipment Comment
     * 
     * @param shipmentComment Shipment Comment
     * @return result
     */
    @Override
    public int updateShipmentComment(ShipmentComment shipmentComment)
    {
        shipmentComment.setUpdateTime(DateUtils.getNowDate());
        return shipmentCommentMapper.updateShipmentComment(shipmentComment);
    }

    /**
     * Delete Shipment Comment By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentCommentByIds(String ids)
    {
        return shipmentCommentMapper.deleteShipmentCommentByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment Comment
     * 
     * @param id Shipment CommentID
     * @return result
     */
    @Override
    public int deleteShipmentCommentById(Long id)
    {
        return shipmentCommentMapper.deleteShipmentCommentById(id);
    }
    
    /**
     * Get Shipment Comment List
     * 
     * @param shipmentComment Shipment Comment
     * @return Shipment Comment List
     */
    @Override
    public List<ShipmentComment> selectShipmentCommentListCustom(ShipmentComment shipmentComment) {
    	return shipmentCommentMapper.selectShipmentCommentListCustom(shipmentComment);
    }
    
    /**
     * Update Shipment Comment Flag include seen flag and resolved flag
     * 
     * @param shipmentComment Shipment Comment
     * @return Result
     */
    @Override
    public int updateFlgShipmentComment(ShipmentComment shipmentComment) {
    	return shipmentCommentMapper.updateFlgShipmentComment(shipmentComment);
    }
    
    /**
     * Select count comment list un-seen
     * 
     * @param shipmentComment
     * @return int
     */
    @Override
    public int selectCountCommentListUnSeen(ShipmentComment shipmentComment) {
    	return shipmentCommentMapper.selectCountCommentListUnSeen(shipmentComment);
    }
    
    /**
     * Get Shipment Comment List
     * 
     * @param shipmentComment Shipment Comment
     * @return Shipment Comment List
     */
    @Override
    public List<ShipmentComment> selectShipmentCommentListForNotification(ShipmentComment shipmentComment) {
    	return shipmentCommentMapper.selectShipmentCommentListForNotification(shipmentComment);
    }
}
