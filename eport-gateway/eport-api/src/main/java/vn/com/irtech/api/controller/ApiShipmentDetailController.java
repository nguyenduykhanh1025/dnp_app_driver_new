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
import vn.com.irtech.api.common.utils.StringUtils;
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
	
	@GetMapping("/shipmentDetail/getConsigneeListWithoutTaxCode")
	public List<String> getConsigneeListWithoutTaxCode() {
		return shipmentDetailDao.selectConsigneeListWithoutTaxCode();
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
	
	@PostMapping("/shipmentDetail/booking/index")
	public Integer getIndexBooking(@RequestBody ShipmentDetailEntity shipmentDetailEntity) {
		List<ShipmentDetailEntity> list = shipmentDetailDao.getIndexBooking(shipmentDetailEntity);
		if(list.size() > 0) {
			for(ShipmentDetailEntity i : list) {
				if(i.getBookingNo().equals(shipmentDetailEntity.getBookingNo()) && i.getSztp().equals(shipmentDetailEntity.getSztp())) {
					return list.indexOf(i) + 1;
				}
			}
		}
		return null;
	}
	
	@GetMapping("/shipmentDetail/location/bl-no/{blNo}/container-no/{containerNo}")
	public ShipmentDetailEntity getLocationForReceiveF(@PathVariable String blNo, @PathVariable String containerNo) {
		return shipmentDetailDao.getLocationForReceiveF(blNo, containerNo);
	}
	
	@PostMapping("/shipmentDetail/container-status")
	public String checkContainerStatus(@RequestBody ShipmentDetailEntity shipmentDetailEntity) {
		return shipmentDetailDao.checkContainerStatus(shipmentDetailEntity);
	}
	
	@GetMapping("/shipmentDetail/block/list/keyword/{keyword}")
	public List<String> getBlockList(@PathVariable("keyword") String keyword){
		if(keyword.equals("empty")) {
			keyword = "";
		}
		return shipmentDetailDao.getBlockList(keyword.toUpperCase());
	}
	
	@GetMapping("/shipmentDetail/area/list/keyword/{keyword}")
	public List<String> getAreaList(@PathVariable("keyword") String keyword){
		if(keyword.equals("empty")) {
			keyword = "";
		}
		return shipmentDetailDao.getAreaList(keyword.toUpperCase());
	}
	
	@PostMapping("/shipmentDetail/check/reserved")
	public Boolean checkContReserved(@RequestBody ShipmentDetailEntity shipmentDetailEntity) {
		ShipmentDetailEntity ship = shipmentDetailDao.checkContReserved(shipmentDetailEntity);
		if(ship != null) {
			return true;
		}
		return false;
	}
	
	@GetMapping("/shipmentDetail/receive-cont-empty/container-amount/booking-no/{bookingNo}/sztp/{sztp}/check/not-ordered")
	public Integer checkTheNumberOfContainersNotOrderedForReceiveContEmpty(@PathVariable String bookingNo, @PathVariable String sztp) {
		Integer result = shipmentDetailDao.checkTheNumberOfContainersNotOrderedForReceiveContEmpty(bookingNo, sztp);
		if(result == null) {
			return 0;
		}
		return result;
	}
	
	@GetMapping("/shipmentDetail/receive-cont-empty/container-amount/booking-no/{bookingNo}/sztp/{sztp}/check/ordered")
	public Integer checkTheNumberOfContainersOrderedForReceiveContEmpty(@PathVariable String bookingNo, @PathVariable String sztp) {
		Integer result = shipmentDetailDao.checkTheNumberOfContainersOrderedForReceiveContEmpty(bookingNo, sztp);
		if(result == null) {
			return 0;
		}
		return result;
	}
	
	@GetMapping("/shipmentDetail/send-cont-full/container-amount/booking-no/{bookingNo}/sztp/{sztp}/check/not-ordered")
	public Integer checkTheNumberOfContainersNotOrderedForSendContFull(@PathVariable String bookingNo, @PathVariable String sztp) {
		Integer result = shipmentDetailDao.checkTheNumberOfContainersNotOrderedForSendContFull(bookingNo, sztp);
		if(result == null) {
			return 0;
		}
		return result;
	}
	
	@PostMapping("/consignee/list")
	public List<ShipmentEntity> getconsigneeList(@RequestBody ShipmentEntity shipmentEntity) {
		return shipmentDetailDao.selectConsigneeTaxCode(shipmentEntity);
	}
	
	@GetMapping("/containerNo/{containerNo}/sztp")
	public String getSztpByContainerNo(@PathVariable("containerNo") String containerNo) {
		String sztp = shipmentDetailDao.getSztpByContainerNoMaster(containerNo);
		if (StringUtils.isEmpty(sztp)) {
			sztp = shipmentDetailDao.getSztpByContainerNoInventory(containerNo);
		}
		return sztp;
	}
	
	@GetMapping("/consignee/{consignee}/taxCode")
	public String getTaxCodeBySnmGroupName(@PathVariable("consignee") String consignee) {
		return shipmentDetailDao.getTaxCodeBySnmGroupName(consignee);
	}
	
	@GetMapping("/jobOrder/{jobOrder}/blNo")
	public String getblNoByJobOrderNo(@PathVariable("jobOrder") String jobOrder) {
		return shipmentDetailDao.getblNoByJobOrderNo(jobOrder);
	}
	
	@GetMapping("/shipmentDetail/getConsigneeNameByTaxCode/{taxCode}")
	public ShipmentEntity getConsigneeByTaxCode(@PathVariable String taxCode) {
		return shipmentDetailDao.getConsigneeByTaxCode(taxCode);
	}
}
