package vn.com.irtech.api.controller;

import java.util.Collections;
import java.util.Date;
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
import vn.com.irtech.api.entity.ShipmentEntity;

@RestController
@RequestMapping("/api")
public class ApiShipmentDetailController {

	@Autowired
	private ShipmentDetailDao shipmentDetailDao;
	
	@GetMapping("/shipmentDetail/list/{blNo}")
	public List<ShipmentDetailEntity> listShipmentDetail(@PathVariable String blNo) {
		List<ShipmentDetailEntity> list = shipmentDetailDao.selectShipmentDetailsByBLNo(blNo);
		return list;
	}
	
	@GetMapping("/shipmentDetail/containerInfor/{blNo}/{containerNo}")
	public ShipmentDetailEntity getShipmentDetail(@PathVariable String blNo, @PathVariable String containerNo) {
		ShipmentDetailEntity ship = shipmentDetailDao.selectShipmentDetailByContNo(blNo, containerNo);
		if(ship == null) {
			return new ShipmentDetailEntity();
		}
		return ship;
	}
	
	@GetMapping("/shipmentDetail/getCoordinateOfContainers/{blNo}")
	public List<ShipmentDetailEntity> getCoordinateOfContainers(@PathVariable String blNo) {
		List<ShipmentDetailEntity> list = shipmentDetailDao.selectCoordinateOfContainers(blNo);
		return list;
	}
	
	@PostMapping("/shipmentDetail/getPODList")
	public List<String> getPODList(@RequestBody ShipmentDetailEntity shipmentDetailEntity) {
		List<String> pods = shipmentDetailDao.selectPODList(shipmentDetailEntity);
		return pods;
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
	public ShipmentEntity getGroupNameByTaxCode(@PathVariable String taxCode) {
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
	
	@GetMapping("shipmentDetail/getOpeCodeCatosByBlNo/{blNo}")
	public ShipmentEntity getOpeCodeCatosByBlNo(@PathVariable String blNo) {
		return shipmentDetailDao.getOpeCodeCatosByBlNo(blNo);
	}
	
	@GetMapping("shipmentDetail/check/custom/{containerNo}/{voyNo}")
	public Boolean checkCustomStatus(@PathVariable String containerNo, @PathVariable String voyNo) {
		Boolean rs = shipmentDetailDao.checkCustomStatus(containerNo, voyNo);
		if(rs != null) {
			return rs;
		}
		return false;
	}
	
	@GetMapping("/shipmentDetail/berthplan/vessel-code/list/ope-code/{opeCode}")
	public List<String> selectVesselCodeBerthPlan(@PathVariable String opeCode){
		return shipmentDetailDao.selectVesselCodeBerthPlan(opeCode);
	}
	
	@GetMapping("/shipmentDetail/vessel-code/{vesselCode}/voyage/{voyNo}/year")
	public String getYearByVslCodeAndVoyNo(@PathVariable String vesselCode, @PathVariable String voyNo) {
		return shipmentDetailDao.getYearByVslCodeAndVoyNo(vesselCode, voyNo);
	}
	
	@GetMapping("/shipmentDetail/berthplan/ope-code/list")
	public List<String> selectOpeCodeListInBerthPlan(){
		return shipmentDetailDao.selectOpeCodeListInBerthPlan();
	}
	
	@GetMapping("/shipmentDetail/berthplan/ope-code/{opeCode}/vessel-voyage/list")
	public List<ShipmentDetailEntity> selectVesselVoyageBerthPlan(@PathVariable String opeCode){
		return shipmentDetailDao.selectVesselVoyageBerthPlan(opeCode);
	}
	
	@GetMapping("/shipmentDetail/check/booking-no/{bookingNo}/fe/{fe}")
	public Integer checkBookingNoForSendFReceiveE(@PathVariable String bookingNo, @PathVariable String fe) {
		return shipmentDetailDao.checkBookingNoForSendFReceiveE(bookingNo, fe);
	}
	
	@PostMapping("/shipmentDetail/infor/send-full-receive-e")
	public ShipmentDetailEntity getInforSendFReceiveE(@RequestBody ShipmentDetailEntity shipmentDetailEntity) {
		return shipmentDetailDao.getInforSendFReceiveE(shipmentDetailEntity);
	}
	
	@GetMapping("/shipmentDetail/index/container-master-ssr/{containerNo}")
	public Integer getIndexContMasterForSSRByContainerNo(@PathVariable String containerNo) {
		List<Date> list = shipmentDetailDao.getIndexContMasterForSSRByContainerNo(containerNo);
		if(list.size() > 0) {
			return list.indexOf(Collections.max(list)) + 1;
		}
		return null;
	}
}
