package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.ProcessOrderEntity;
import vn.com.irtech.api.entity.ShipmentDetailEntity;
import vn.com.irtech.api.entity.ShipmentEntity;
@Mapper
public interface ShipmentDetailDao extends BaseMapper<ShipmentDetailEntity> {
    public List<ShipmentDetailEntity> selectShipmentDetailsByBLNo(ShipmentDetailEntity shipmentDetailEntity);
    
    public ShipmentDetailEntity selectShipmentDetailByContNo(ShipmentDetailEntity shipmentDetailEntity);
    
    public List<ShipmentDetailEntity> selectCoordinateOfContainers(String blNo);
    
	public List<String> selectVesselCodeList();
	
	public List<String> selectPODList(ShipmentDetailEntity shipmentDetailEntity);
	
	public List<String> selectConsigneeList();
	
	public List<String> selectVoyageNoListByVesselCode(String vesselCode);
	
	public List<String> selectOpeCodeList();
	
	public ShipmentEntity getGroupNameByTaxCode(String taxCode);
	
	public List<String> checkContReservedByContainerNos(String[] containerNos);
	
	public Integer getCountContByBlNo(String blNo);
	
	public ShipmentEntity getOpeCodeCatosByBlNo(String blNo);
	
	public Boolean checkCustomStatus(@Param("containerNo") String containerNo, @Param("voyNo") String voyNo);
}