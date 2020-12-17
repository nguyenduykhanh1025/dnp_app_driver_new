package vn.com.irtech.eport.logistic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.mapper.ShipmentImageMapper;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;

import java.util.List;

/**
 * @author ThanhD
 * @date 2020-07-28
 */
@Service
public class ShipmentImageServiceImpl implements IShipmentImageService {

    @Autowired
    private ShipmentImageMapper shipmentImageMapper;

    @Override
    public int insertShipmentImage(final ShipmentImage shipmentImage) {
        return shipmentImageMapper.insertShipmentImage(shipmentImage);
    }

    @Override
    public int countShipmentImagesByShipmentId(final Long shipmentId) {
        return shipmentImageMapper.countShipmentImagesByShipmentId(shipmentId);
    }

    @Override
    public List<ShipmentImage> selectShipmentImagesByShipmentId(final Long shipmentId) {
        return shipmentImageMapper.selectShipmentImagesByShipmentId(shipmentId);
    }
    
    @Override
    public List<ShipmentImage> selectShipmentImagesByShipmentDetailId(final Long shipmentId) {
        return shipmentImageMapper.selectShipmentImagesByShipmentDetailId(shipmentId);
    }
     
    /**
     * Update shipment image that in ids array 
     * 
     * @param shipmentImage
     * @return int
     */
    @Override
    public int updateShipmentImageByIds(ShipmentImage shipmentImage) {
    	return shipmentImageMapper.updateShipmentImageByIds(shipmentImage);
    }
    
    @Override
    public int updateShipmentImageByIdsReceive(ShipmentImage shipmentImage) {
    	return shipmentImageMapper.updateShipmentImageByIdsReceive(shipmentImage);
    }
    
    
    
    /**
     * Select shipment image list  
     * 
     * @param shipmentImage
     * @return List<ShipmentImage
     */
    @Override
    public List<ShipmentImage> selectShipmentImageList(ShipmentImage shipmentImage) {
    	return shipmentImageMapper.selectShipmentImageList(shipmentImage);
    }
    
    /**
     * Select shipment image list not file type
     * 
     * @param shipmentImage
     * @return List<ShipmentImage
     */
    @Override
    public List<ShipmentImage> selectShipmentImageListNotFileType(ShipmentImage shipmentImage) {
    	return shipmentImageMapper.selectShipmentImageListNotFileType(shipmentImage);
    }

    /**
     * Select shipment image by id
     * 
     * @param shipmentImage
     * @return ShipmentImage
     */
    @Override
    public ShipmentImage selectShipmentImageById(ShipmentImage shipmentImage) {
    	return shipmentImageMapper.selectShipmentImageById(shipmentImage);
    }
    
    
    
    
    @Override
	public List<ShipmentImage> selectShipmentImagesByshipmentDetailIds(String shipmentDetailIds) {
		return shipmentImageMapper.selectShipmentImagesByshipmentDetailIds(Convert.toStrArray(shipmentDetailIds));
	}
    
    /**
     * Delete shipment image by id
     * 
     * @param id
     * @return int
     */
    @Override
    public int deleteShipmentImageById(Long id) {
    	return shipmentImageMapper.deleteShipmentImageById(id);
    }
    
    /**
     * Delete shipment image by ids
     * 
     * @param ids
     * @return int
     */
    @Override
    public int deleteShipmentImageByIds(String ids) {
    	return shipmentImageMapper.deleteShipmentImageByIds(Convert.toStrArray(ids));
    }
}
