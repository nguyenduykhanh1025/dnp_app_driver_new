package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}
