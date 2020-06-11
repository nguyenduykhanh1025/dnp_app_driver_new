package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.mapper.ShipmentDetailMapper;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

/**
 * Shipment DetailsService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentDetailServiceImpl implements IShipmentDetailService 
{
    @Autowired
    private ShipmentDetailMapper shipmentDetailMapper;

    /**
     * Get Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return Shipment Details
     */
    @Override
    public ShipmentDetail selectShipmentDetailById(Long id)
    {
        return shipmentDetailMapper.selectShipmentDetailById(id);
    }

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details
     */
    @Override
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail)
    {
        return shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);
    }

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    @Override
    public int insertShipmentDetail(ShipmentDetail shipmentDetail)
    {
        shipmentDetail.setCreateTime(DateUtils.getNowDate());
        return shipmentDetailMapper.insertShipmentDetail(shipmentDetail);
    }

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    @Override
    public int updateShipmentDetail(ShipmentDetail shipmentDetail)
    {
        shipmentDetail.setUpdateTime(DateUtils.getNowDate());
        return shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
    }

    /**
     * Delete Shipment Details By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentDetailByIds(String ids)
    {
        return shipmentDetailMapper.deleteShipmentDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    @Override
    public int deleteShipmentDetailById(Long id)
    {
        return shipmentDetailMapper.deleteShipmentDetailById(id);
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByIds(String ids) {
        return shipmentDetailMapper.selectShipmentDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * @return result
     */
	@Override
	public List<ShipmentWaitExec> selectListShipmentWaitExec() {
		return shipmentDetailMapper.selectListShipmentWaitExec();
	}
}
