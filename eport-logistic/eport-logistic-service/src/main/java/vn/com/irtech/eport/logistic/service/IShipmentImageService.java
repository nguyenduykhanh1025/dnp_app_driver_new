package vn.com.irtech.eport.logistic.service;

import vn.com.irtech.eport.logistic.domain.ShipmentImage;

import java.util.List;

/**
 * @author ThanhD
 * @date 2020-07-28
 */
public interface IShipmentImageService {
    int insertShipmentImage(final ShipmentImage shipmentImage);

    int countShipmentImagesByShipmentId(Long shipmentId);

    List<ShipmentImage> selectShipmentImagesByShipmentId(Long shipmentId);
    
    List<ShipmentImage> selectShipmentImagesByShipmentDetailId(Long shipmentId);
    
    
    
    /**
     * Update shipment image that in ids array 
     * 
     * @param shipmentImage
     * @return int
     */
    public int updateShipmentImageByIds(ShipmentImage shipmentImage);
    
    /**
     * Select shipment image list  
     * 
     * @param shipmentImage
     * @return List<ShipmentImage
     */
    public List<ShipmentImage> selectShipmentImageList(ShipmentImage shipmentImage);

    /**
     * Select shipment image list not file type
     * 
     * @param shipmentImage
     * @return List<ShipmentImage
     */
    public List<ShipmentImage> selectShipmentImageListNotFileType(ShipmentImage shipmentImage);
    
    /**
     * Select shipment image by id
     * 
     * @param shipmentImage
     * @return ShipmentImage
     */
    public ShipmentImage selectShipmentImageById(ShipmentImage shipmentImage);
    
    /**
     * Delete shipment image by id
     * 
     * @param id
     * @return int
     */
    public int deleteShipmentImageById(Long id);
    
    /**
     * Delete shipment image by ids
     * 
     * @param ids
     * @return int
     */
    public int deleteShipmentImageByIds(String ids);
}
