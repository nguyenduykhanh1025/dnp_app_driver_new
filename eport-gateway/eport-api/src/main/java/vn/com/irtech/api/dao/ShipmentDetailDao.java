package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vn.com.irtech.api.entity.ShipmentDetailEntity;
@Mapper
public interface ShipmentDetailDao extends BaseMapper<ShipmentDetailEntity> {
    public List<ShipmentDetailEntity> selectShipmentDetailsByBLNo(ShipmentDetailEntity shipmentDetailEntity);
    public ShipmentDetailEntity selectShipmentDetailByContNo(ShipmentDetailEntity shipmentDetailEntity);
    public List<ShipmentDetailEntity> selectCoordinateOfContainers(ShipmentDetailEntity shipmentDetailEntity);
	public List<String> selectVesselCodeList();
	public List<String> selectPODList();
	public List<String> selectConsigneeList();
}