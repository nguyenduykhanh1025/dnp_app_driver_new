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
import vn.com.irtech.api.dao.UnitBillDao;
import vn.com.irtech.api.entity.ShipmentDetailEntity;
import vn.com.irtech.api.entity.UnitBillEntity;

@RestController
@RequestMapping("/api")
public class ApiUnitBillController {
	@Autowired
	public UnitBillDao unitBillDao;
	
	@GetMapping("/unitBill/list/{invNo}")
	public List<UnitBillEntity> getUnitBillList(@PathVariable String invNo) {
		return unitBillDao.selectUnitBillByInvNo(invNo);
	}
	@PostMapping("/unit-bill/list/send-cont")
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForReserve(@RequestBody ShipmentDetailEntity shipmentDetailEntity){
		return unitBillDao.getUnitBillByShipmentDetailsForReserve(shipmentDetailEntity);
	}
	
	@PostMapping("/unit-bill/list/receive-cont")
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForInventory(@RequestBody ShipmentDetailEntity shipmentDetailEntity){
		return unitBillDao.getUnitBillByShipmentDetailsForInventory(shipmentDetailEntity);
	}
	
	@PostMapping("/unit-bill/list/send-cont/ssr")
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForSendSSR(@RequestBody ShipmentDetailEntity shipmentDetailEntity){
		return unitBillDao.getUnitBillByShipmentDetailsForSendSSR(shipmentDetailEntity);
	}
	
	@PostMapping("/unit-bill/list/receive-cont/ssr")
	public List<UnitBillEntity> getUnitBillByShipmentDetailsForReceiveSSR(@RequestBody ShipmentDetailEntity shipmentDetailEntity){
		return unitBillDao.getUnitBillByShipmentDetailsForReceiveSSR(shipmentDetailEntity);
	}

	@GetMapping("/order-no/{orderNo}/get/invoice-no")
	public String getInvoiceNoByOrderNo(@PathVariable("orderNo") String orderNo) {
		return unitBillDao.getInvoiceNoByOrderNo(orderNo);
	}
}
