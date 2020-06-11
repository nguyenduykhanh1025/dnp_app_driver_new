package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vn.com.irtech.api.entity.ShipmentDetailEntity;
@Mapper
public interface ShipmentDetailDao extends BaseMapper<ShipmentDetailEntity> {
    List<ShipmentDetailEntity> selectShipmentDetailsByBLNo(ShipmentDetailEntity shipmentDetailEntity);
    ShipmentDetailEntity selectShipmentDetailByContNo(ShipmentDetailEntity shipmentDetailEntity);
    List<ShipmentDetailEntity> selectCoordinateOfContainers(ShipmentDetailEntity shipmentDetailEntity);
}