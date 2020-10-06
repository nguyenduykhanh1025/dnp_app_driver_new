package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.ShipmentDetailHistMapper;
import vn.com.irtech.eport.system.domain.ShipmentDetailHist;
import vn.com.irtech.eport.system.service.IShipmentDetailHistService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * ShipmentDetailHistService Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-10-06
 */
@Service
public class ShipmentDetailHistServiceImpl implements IShipmentDetailHistService 
{
    @Autowired
    private ShipmentDetailHistMapper shipmentDetailHistMapper;

    /**
     * Get ShipmentDetailHist
     * 
     * @param id ShipmentDetailHistID
     * @return ShipmentDetailHist
     */
    @Override
    public ShipmentDetailHist selectShipmentDetailHistById(Long id)
    {
        return shipmentDetailHistMapper.selectShipmentDetailHistById(id);
    }

    /**
     * Get ShipmentDetailHist List
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return ShipmentDetailHist
     */
    @Override
    public List<ShipmentDetailHist> selectShipmentDetailHistList(ShipmentDetailHist shipmentDetailHist)
    {
        return shipmentDetailHistMapper.selectShipmentDetailHistList(shipmentDetailHist);
    }

    /**
     * Add ShipmentDetailHist
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return result
     */
    @Override
    public int insertShipmentDetailHist(ShipmentDetailHist shipmentDetailHist)
    {
        shipmentDetailHist.setCreateTime(DateUtils.getNowDate());
        return shipmentDetailHistMapper.insertShipmentDetailHist(shipmentDetailHist);
    }

    /**
     * Update ShipmentDetailHist
     * 
     * @param shipmentDetailHist ShipmentDetailHist
     * @return result
     */
    @Override
    public int updateShipmentDetailHist(ShipmentDetailHist shipmentDetailHist)
    {
        shipmentDetailHist.setUpdateTime(DateUtils.getNowDate());
        return shipmentDetailHistMapper.updateShipmentDetailHist(shipmentDetailHist);
    }

    /**
     * Delete ShipmentDetailHist By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentDetailHistByIds(String ids)
    {
        return shipmentDetailHistMapper.deleteShipmentDetailHistByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete ShipmentDetailHist
     * 
     * @param id ShipmentDetailHistID
     * @return result
     */
    @Override
    public int deleteShipmentDetailHistById(Long id)
    {
        return shipmentDetailHistMapper.deleteShipmentDetailHistById(id);
    }
}
