package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.ShipmentTransportMapper;
import vn.com.irtech.eport.logistic.domain.ShipmentTransport;
import vn.com.irtech.eport.logistic.service.IShipmentTransportService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Thong tin dieu xeService Business Processing
 * 
 * @author ruoyi
 * @date 2020-05-26
 */
@Service
public class ShipmentTransportServiceImpl implements IShipmentTransportService 
{
    @Autowired
    private ShipmentTransportMapper shipmentTransportMapper;

    /**
     * Get Thong tin dieu xe
     * 
     * @param id Thong tin dieu xeID
     * @return Thong tin dieu xe
     */
    @Override
    public ShipmentTransport selectShipmentTransportById(Long id)
    {
        return shipmentTransportMapper.selectShipmentTransportById(id);
    }

    /**
     * Get Thong tin dieu xe List
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return Thong tin dieu xe
     */
    @Override
    public List<ShipmentTransport> selectShipmentTransportList(ShipmentTransport shipmentTransport)
    {
        return shipmentTransportMapper.selectShipmentTransportList(shipmentTransport);
    }

    /**
     * Add Thong tin dieu xe
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return result
     */
    @Override
    public int insertShipmentTransport(ShipmentTransport shipmentTransport)
    {
        return shipmentTransportMapper.insertShipmentTransport(shipmentTransport);
    }

    /**
     * Update Thong tin dieu xe
     * 
     * @param shipmentTransport Thong tin dieu xe
     * @return result
     */
    @Override
    public int updateShipmentTransport(ShipmentTransport shipmentTransport)
    {
        return shipmentTransportMapper.updateShipmentTransport(shipmentTransport);
    }

    /**
     * Delete Thong tin dieu xe By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentTransportByIds(String ids)
    {
        return shipmentTransportMapper.deleteShipmentTransportByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Thong tin dieu xe
     * 
     * @param id Thong tin dieu xeID
     * @return result
     */
    @Override
    public int deleteShipmentTransportById(Long id)
    {
        return shipmentTransportMapper.deleteShipmentTransportById(id);
    }
}
