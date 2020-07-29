package vn.com.irtech.eport.logistic.mapper;

import vn.com.irtech.eport.logistic.domain.ShipmentImage;

import java.util.List;

/**
 * @author ThanhD
 * @date 2020-07-28
 */
public interface ShipmentImageMapper {

    int insertShipmentImage(ShipmentImage shipmentImage);

    int countShipmentImagesByShipmentId(Long shipmentId);

    List<ShipmentImage> selectShipmentImagesByShipmentId(Long shipmentId);
}
