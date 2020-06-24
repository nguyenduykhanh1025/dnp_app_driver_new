package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public R getPODList() {
		List<String> list = shipmentDetailDao.selectPODList();
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getVesselCodeList")
	public R getVesselCodeList() {
		List<String> list = shipmentDetailDao.selectVesselCodeList();
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getConsigneeList")
	public R getConsigneeList() {
		List<String> list = shipmentDetailDao.selectConsigneeList();
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getVoyageNoList/{vesselCode}")
	public R getVoyageNoList(@PathVariable String vesselCode){
		List<String> list = shipmentDetailDao.selectVoyageNoListByVesselCode(vesselCode);
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getYear/{vesselCode}/{voyageNo}")
	public R getYear(@PathVariable String vesselCode, @PathVariable String voyageNo) {
		String year = shipmentDetailDao.selectYearByVesselCodeAndVoyageNo(vesselCode, voyageNo);
		return R.ok().put("data", year);
	}
	
	@GetMapping("/shipmentDetail/getOpeCodeList")
	public R getOpeCodeList() {
		List<String> list = shipmentDetailDao.selectOpeCodeList();
		return R.ok().put("data", list);
	}
	
	@GetMapping("/shipmentDetail/getGroupNameByTaxCode/{taxCode}")
	public String getGroupNameByTaxCode(@PathVariable String taxCode) {
		return shipmentDetailDao.getGroupNameByTaxCode(taxCode);
	}
}
