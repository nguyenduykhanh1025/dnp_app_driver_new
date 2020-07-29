package vn.com.irtech.eport.logistic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
