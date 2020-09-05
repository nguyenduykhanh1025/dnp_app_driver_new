package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.ShipmentDetailEntity;
import vn.com.irtech.api.entity.UnitBillEntity;

@Mapper
public interface UnitBillDao extends BaseMapper<UnitBillDao>{

	public List<UnitBillEntity> selectUnitBillByInvNo(String invoiceNo);
	
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForReserve(ShipmentDetailEntity shipmentDetailEntity);
	
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForInventory(ShipmentDetailEntity shipmentDetailEntity);
	
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForReceiveSSR(ShipmentDetailEntity shipmentDetailEntity);
	
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForSendSSR(ShipmentDetailEntity shipmentDetailEntity);

	public String getInvoiceNoByOrderNo(String orderNo);
}
