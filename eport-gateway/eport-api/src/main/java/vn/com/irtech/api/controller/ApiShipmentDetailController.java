package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.common.utils.Convert;
import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.dao.ShipmentDetailDao;
import vn.com.irtech.api.entity.ShipmentDetailEntity;

@RestController
@RequestMapping("/api")
public class ApiShipmentDetailController {

	@Autowired
	private ShipmentDetailDao shipmentDetailDao;
	
	@PostMapping("/shipmentDetail/list")
	public R listShipmentDetail(@RequestBody ShipmentDetailEntity shipmentDetail) {
		List<ShipmentDetailEntity> list = shipmentDetailDao.selectShipmentDetailsByBLNo(shipmentDetail);
		return R.ok().put("data", list);
	}
	
	@PostMapping("/shipmentDetail/containerInfor")
	public R getShipmentDetail(@RequestBody ShipmentDetailEntity shipmentDetail) {
		ShipmentDetailEntity ship = shipmentDetailDao.selectShipmentDetailByContNo(shipmentDetail);
		return R.ok().put("data", ship);
	}
	
	@PostMapping("/shipmentDetail/getCoordinateOfContainers")
	public R getCoordinateOfContainers(@RequestBody ShipmentDetailEntity shipmentDetail) {
		List<ShipmentDetailEntity> list = shipmentDetailDao.selectCoordinateOfContainers(shipmentDetail);
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getPODList")
	public List<String> getPODList() {
		return shipmentDetailDao.selectPODList();
	}
	
	@GetMapping("/shipmentDetail/getVesselCodeList")
	public List<String> getVesselCodeList() {
		return shipmentDetailDao.selectVesselCodeList();
	}
	
	@GetMapping("/shipmentDetail/getConsigneeList")
	public List<String> getConsigneeList() {
		return shipmentDetailDao.selectConsigneeList();
	}
	
	@GetMapping("/shipmentDetail/getVoyageNoList/{vesselCode}")
	public List<String> getVoyageNoList(@PathVariable String vesselCode){
		return shipmentDetailDao.selectVoyageNoListByVesselCode(vesselCode);
	}
	
	@GetMapping("/shipmentDetail/getOpeCodeList")
	public List<String> getOpeCodeList() {
		return shipmentDetailDao.selectOpeCodeList();
	}
	
	@GetMapping("/shipmentDetail/getGroupNameByTaxCode/{taxCode}")
	public String getGroupNameByTaxCode(@PathVariable String taxCode) {
		return shipmentDetailDao.getGroupNameByTaxCode(taxCode);
	}
	
	@GetMapping("/shipmentDetail/checkContReserved/{containerNos}")
	public List<String> checkContReserved(@PathVariable String containerNos){
		return shipmentDetailDao.checkContReservedByContainerNos(Convert.toStrArray(containerNos));
	}
	
	@GetMapping("/shipmentDetail/getCountContByBlNo/{blNo}")
	public Integer getCountContByBlNo(@PathVariable String blNo) {
		return shipmentDetailDao.getCountContByBlNo(blNo);
	}
}
