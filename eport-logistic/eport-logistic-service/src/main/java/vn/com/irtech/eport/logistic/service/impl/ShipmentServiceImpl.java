package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.ShipmentMapper;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * ShipmentService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentServiceImpl implements IShipmentService 
{
    @Autowired
    private ShipmentMapper shipmentMapper;

    /**
     * Get Shipment
     * 
     * @param id ShipmentID
     * @return Shipment
     */
    @Override
    public Shipment selectShipmentById(Long id)
    {
        return shipmentMapper.selectShipmentById(id);
    }

    /**
     * Get Shipment List
     * 
     * @param shipment Shipment
     * @return Shipment
     */
    @Override
    public List<Shipment> selectShipmentList(Shipment shipment)
    {
        return shipmentMapper.selectShipmentList(shipment);
    }

    /**
     * Add Shipment
     * 
     * @param shipment Shipment
     * @return result
     */
    @Override
    public int insertShipment(Shipment shipment)
    {
        shipment.setCreateTime(DateUtils.getNowDate());
        return shipmentMapper.insertShipment(shipment);
    }

    /**
     * Update Shipment
     * 
     * @param shipment Shipment
     * @return result
     */
    @Override
    public int updateShipment(Shipment shipment)
    {
        shipment.setUpdateTime(DateUtils.getNowDate());
        return shipmentMapper.updateShipment(shipment);
    }

    /**
     * Delete Shipment By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentByIds(String ids)
    {
        return shipmentMapper.deleteShipmentByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment
     * 
     * @param id ShipmentID
     * @return result
     */
    @Override
    public int deleteShipmentById(Long id)
    {
        return shipmentMapper.deleteShipmentById(id);
    }

    @Override
    public Shipment selectShipmentWithGroupById(Long id) {
        return shipmentMapper.selectShipmentWithGroupById(id);
    }

    @Override
    public int checkBillNoIsUnique(Shipment shipment) {
        return shipmentMapper.checkBillNoIsUnique(shipment);
    }
}
