package vn.com.irtech.eport.web.controller.gate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.web.dto.DetectionInfomation;
import vn.com.irtech.eport.web.dto.GateInTestDataReq;

@Controller
@RequestMapping("/gate/test")
public class GateTestController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(GateTestController.class);

	private final static String PREFIX = "gate/test";
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private ILogisticGroupService logisticGroupService;
	
	@Autowired
	private IDriverAccountService driverAccountService;
	
	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private IPickupAssignService pickupAssignService;
	
	@Autowired
	private ICatosApiService catosApiService;
	
	@GetMapping()
	public String getView() {
		return PREFIX + "/test";
	}
	
	@PostMapping("/detection")
	@ResponseBody
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfomation detectionInfo) {
		
		String detectJson = new Gson().toJson(detectionInfo);
		logger.debug(">>>> Receive detection info:" + detectJson);
		// Save detection info to cache
		CacheUtils.put("detectionInfo_" + detectionInfo.getGateId(), detectionInfo);
		
		// Send to monitor
		webSocketService.sendMessage("/gate/detection/monitor", detectionInfo);
		return success();
	}
	
	@PostMapping("/logistics")
	@ResponseBody
	public List<LogisticGroup> getLogisticGroups() {
		LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    LogisticGroup logisticGroupParam = new LogisticGroup();
	    logisticGroupParam.setDelFlag("0");
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
	    logisticGroups.add(0, logisticGroup);
	    return logisticGroups;
	}
	
	@PostMapping("/logistic/{groupId}/drivers")
	@ResponseBody
	public List<DriverAccount> getDriverAccounts(@PathVariable("groupId") Long groupId) {
		DriverAccount driverAccount = new DriverAccount();
		driverAccount.setLogisticGroupId(groupId);
		return driverAccountService.selectDriverAccountList(driverAccount);
	}
	
	@PostMapping("/gateIn")
	@Transactional
	@ResponseBody
	public AjaxResult gateIn(@Validated @RequestBody GateInTestDataReq gateInTestDataReq) {
		
		// Sample data
		Shipment shipment = new Shipment();
		shipment.setLogisticAccountId(1L);
		shipment.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
		shipment.setContainerAmount(2L);
		
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
		shipmentDetail.setFe("F");
		shipmentDetail.setVslNm("test");
		shipmentDetail.setVoyNo("test");
		shipmentDetail.setOpeCode("test");
		shipmentDetail.setPaymentStatus("Y");
		shipmentDetail.setProcessStatus("Y");
		shipmentDetail.setUserVerifyStatus("Y");
		shipmentDetail.setStatus(4);
		shipmentDetail.setCustomStatus("Y");
		shipmentDetail.setDoReceivedTime(new Date());
		shipmentDetail.setDoStatus("Y");
		shipmentDetail.setSztp("22G0");
		
		if (gateInTestDataReq.getSendOption()) {
			if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerSend1())) {
				logger.debug("Drop container 1: " + gateInTestDataReq.getContainerSend1());
				Shipment shipment1 = new Shipment();
				BeanUtils.copyBeanProp(shipment1, shipment);
				shipment1.setBookingNo("test");
				shipment1.setServiceType(EportConstants.SERVICE_DROP_FULL);
				logger.debug("Create shipment for drop cont 1: " + new Gson().toJson(shipment1));
				shipmentService.insertShipment(shipment1);
				
				ShipmentDetail shipmentDetail1 = new ShipmentDetail();
				BeanUtils.copyBeanProp(shipmentDetail1, shipmentDetail);
				shipmentDetail1.setContainerNo(gateInTestDataReq.getContainerSend1());
				shipmentDetail1.setBookingNo("test");
				shipmentDetail1.setShipmentId(shipment1.getId());
				logger.debug("Create shipment detail for drop cont 1: " + new Gson().toJson(shipmentDetail1));
				shipmentDetailService.insertShipmentDetail(shipmentDetail1);
				
				PickupAssign pickupAssign1 = new PickupAssign();
				pickupAssign1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
				pickupAssign1.setShipmentId(shipment1.getId());
				pickupAssign1.setDriverId(gateInTestDataReq.getDriverId());
				pickupAssign1.setShipmentDetailId(shipmentDetail1.getId());
				pickupAssign1.setTruckNo(gateInTestDataReq.getTruckNo());
				pickupAssign1.setChassisNo(gateInTestDataReq.getChassisNo());
				logger.debug("Create pickup assign for drop cont 1: " + new Gson().toJson(pickupAssign1));
				pickupAssignService.insertPickupAssign(pickupAssign1);
				
				PickupHistory pickupHistory1 = new PickupHistory();
				pickupHistory1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
				pickupHistory1.setShipmentId(shipment1.getId());
				pickupHistory1.setDriverId(gateInTestDataReq.getDriverId());
				pickupHistory1.setPickupAssignId(pickupAssign1.getId());
				pickupHistory1.setTruckNo(gateInTestDataReq.getTruckNo());
				pickupHistory1.setChassisNo(gateInTestDataReq.getChassisNo());
				pickupHistory1.setContainerNo(gateInTestDataReq.getContainerSend1());
				pickupHistory1.setStatus(0);
				pickupHistory1.setShipmentDetailId(shipmentDetail1.getId());
				pickupHistory1.setGatePass(gateInTestDataReq.getGatePass());
				
				if (StringUtils.isNotEmpty(gateInTestDataReq.getYardPosition1())) {
					String[] positionArr = gateInTestDataReq.getYardPosition1().split("-");
					if (positionArr.length == 4) {
						pickupHistory1.setBlock(positionArr[0]);
						pickupHistory1.setBay(positionArr[1]);
						pickupHistory1.setLine(positionArr[2]);
						pickupHistory1.setTier(positionArr[3]);
					}
				}
				
				logger.debug("Create pickup history for drop cont 1: " + new Gson().toJson(pickupHistory1));
				pickupHistoryService.insertPickupHistory(pickupHistory1);
				
				logger.debug("Complete prepare data for drop container 1");
				if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerSend2())) {
					logger.debug("Drop full with container 2: " + gateInTestDataReq.getContainerSend2());
					ShipmentDetail shipmentDetail2 = new ShipmentDetail();
					BeanUtils.copyBeanProp(shipmentDetail2, shipmentDetail);
					shipmentDetail2.setContainerNo(gateInTestDataReq.getContainerSend2());
					shipmentDetail2.setShipmentId(shipment1.getId());
					shipmentDetail2.setBookingNo("test");
					logger.debug("Create shipment detail for drop cont 2: " + new Gson().toJson(shipmentDetail2));
					shipmentDetailService.insertShipmentDetail(shipmentDetail2);
					
					PickupAssign pickupAssign2 = new PickupAssign();
					BeanUtils.copyBeanProp(pickupAssign2, pickupAssign1);
					pickupAssign2.setShipmentDetailId(shipmentDetail2.getId());
					pickupAssign2.setId(null);
					logger.debug("Create pickup assign for drop cont 2: " + new Gson().toJson(pickupAssign2));
					pickupAssignService.insertPickupAssign(pickupAssign2);
					
					PickupHistory pickupHistory2 = new PickupHistory();
					BeanUtils.copyBeanProp(pickupHistory2, pickupHistory1);
					pickupHistory2.setShipmentDetailId(shipmentDetail2.getId());
					pickupHistory2.setId(null);
					pickupHistory2.setContainerNo(gateInTestDataReq.getContainerSend2());
					pickupHistory2.setPickupAssignId(pickupAssign2.getId());
					pickupHistory2.setGatePass(gateInTestDataReq.getGatePass());
					if (StringUtils.isNotEmpty(gateInTestDataReq.getYardPosition2())) {
						String[] positionArr = gateInTestDataReq.getYardPosition2().split("-");
						if (positionArr.length == 4) {
							pickupHistory2.setBlock(positionArr[0]);
							pickupHistory2.setBay(positionArr[1]);
							pickupHistory2.setLine(positionArr[2]);
							pickupHistory2.setTier(positionArr[3]);
						}
					} else {
						pickupHistory2.setBlock("");
						pickupHistory2.setBay("");
						pickupHistory2.setLine("");
						pickupHistory2.setTier("");
					}
					logger.debug("Create pickup history for drop cont 2: " + new Gson().toJson(pickupHistory2));
					pickupHistoryService.insertPickupHistory(pickupHistory2);
					
					logger.debug("Complete prepare data for drop container 2");
				}
			}
		}
		
		if (gateInTestDataReq.getReceiveOption()) {
			
			if (StringUtils.isEmpty(gateInTestDataReq.getBlNo())) {
				String blNo = catosApiService.getBlNoByOrderJobNo(gateInTestDataReq.getOrderJobNo());
				if (StringUtils.isNotEmpty(blNo)) {
					gateInTestDataReq.setBlNo(blNo);
				}
			}
			
			Shipment shipment2 = new Shipment();
			shipment2.setBlNo(gateInTestDataReq.getBlNo());
			shipment2.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
			shipment2.setServiceType(EportConstants.SERVICE_PICKUP_FULL);
			
			
			Long contId1 = 0L;
			Long contId2 = 0L;
			Boolean shipmentFound = false;
			
			// Check shipment exists
			List<Shipment> shipments = shipmentService.selectShipmentList(shipment2);
			if (CollectionUtils.isNotEmpty(shipments)) {
				shipment2 = shipments.get(0);
				ShipmentDetail shipmentDetailParam = new ShipmentDetail();
				shipmentDetailParam.setShipmentId(shipment2.getId());
				List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
				if (CollectionUtils.isNotEmpty(shipmentDetails)) {
					for (ShipmentDetail shipmentDetail1 : shipmentDetails) {
						if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive1())) {
							if (shipmentDetail1.getContainerNo().equals(gateInTestDataReq.getContainerReceive1())) {
								contId1 = shipmentDetail1.getId();
							}
						}
						if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive2())) {
							if (shipmentDetail1.getContainerNo().equals(gateInTestDataReq.getContainerReceive2())) {
								contId2 = shipmentDetail1.getId();
							}
						}
					}
					shipmentFound = true;
				}
			}
			
//			if (!shipmentFound) {
//				shipment2.setLogisticAccountId(1L);
//				shipment2.setEdoFlg("0");
//				logger.debug("Get container list by B/L No ");
//				List<ContainerInfoDto> shipmentDetails = catosApiService.selectShipmentDetailsByBLNo(gateInTestDataReq.getBlNo());
//				shipment2.setContainerAmount(Long.valueOf(shipmentDetails.size()));
//				shipmentService.insertShipment(shipment2);
//				logger.debug("List container: " + new Gson().toJson(shipmentDetails));
//				for (ContainerInfoDto shipmentDetail1 : shipmentDetails) {
//					shipmentDetail1.setContainerStatus("");
//					shipmentDetail1.setOpeCode(shipmentDetail1.getOpeCode().substring(0, 3));
//					ShipmentDetail shipmentDetail2 = new ShipmentDetail();
//					BeanUtils.copyBeanProp(shipmentDetail2, shipmentDetail1);
//					shipmentDetail2.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
//					shipmentDetail2.setFe("F");
//					if (StringUtils.isEmpty(shipmentDetail2.getVslNm())) {
//						shipmentDetail2.setVslNm("test");
//					}
//					
//					if (StringUtils.isEmpty(shipmentDetail2.getVoyNo())) {
//						shipmentDetail2.setVoyNo("test");
//					}
//					
//					if (StringUtils.isEmpty(shipmentDetail2.getOpeCode())) {
//						shipmentDetail2.setOpeCode("test");
//					}
//					
//					if (StringUtils.isEmpty(shipmentDetail2.getSztp())) {
//						shipmentDetail2.setSztp("22G0");
//					}
//					shipmentDetail2.setPaymentStatus("Y");
//					shipmentDetail2.setProcessStatus("Y");
//					shipmentDetail2.setUserVerifyStatus("Y");
//					shipmentDetail2.setStatus(4);
//					shipmentDetail2.setCustomStatus("Y");
//					shipmentDetail2.setDoReceivedTime(new Date());
//					shipmentDetail2.setPreorderPickup("Y");
//					shipmentDetail2.setPrePickupPaymentStatus("Y");
//					shipmentDetail2.setDoStatus("Y");
//					shipmentDetail2.setBlNo(gateInTestDataReq.getBlNo());
//					shipmentDetail2.setShipmentId(shipment2.getId());
//					shipmentDetailService.insertShipmentDetail(shipmentDetail2);
//					if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive1())) {
//						if (shipmentDetail2.getContainerNo().equals(gateInTestDataReq.getContainerReceive1())) {
//							contId1 = shipmentDetail2.getId();
//						}
//					}
//					if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive2())) {
//						if (shipmentDetail2.getContainerNo().equals(gateInTestDataReq.getContainerReceive2())) {
//							contId2 = shipmentDetail2.getId();
//						}
//					}
//				}
//			}
			
			
			if ("1".equals(gateInTestDataReq.getContainerFlg())) {
				
				if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive1())) {
					logger.debug("Pick container 1: " + gateInTestDataReq.getContainerReceive1());
					
					PickupAssign pickupAssign1 = new PickupAssign();
					pickupAssign1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
					pickupAssign1.setShipmentId(shipment2.getId());
					pickupAssign1.setDriverId(gateInTestDataReq.getDriverId());
					pickupAssign1.setShipmentDetailId(contId1);
					pickupAssign1.setTruckNo(gateInTestDataReq.getTruckNo());
					pickupAssign1.setChassisNo(gateInTestDataReq.getChassisNo());
					
					pickupAssignService.insertPickupAssign(pickupAssign1);
					
					PickupHistory pickupHistory1 = new PickupHistory();
					pickupHistory1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
					pickupHistory1.setShipmentId(shipment2.getId());
					pickupHistory1.setDriverId(gateInTestDataReq.getDriverId());
					pickupHistory1.setPickupAssignId(pickupAssign1.getId());
					pickupHistory1.setTruckNo(gateInTestDataReq.getTruckNo());
					pickupHistory1.setChassisNo(gateInTestDataReq.getChassisNo());
					pickupHistory1.setContainerNo(gateInTestDataReq.getContainerReceive1());
					pickupHistory1.setStatus(0);
					pickupHistory1.setShipmentDetailId(contId1);
					pickupHistory1.setGatePass(gateInTestDataReq.getGatePass());
					pickupHistoryService.insertPickupHistory(pickupHistory1);
					
					logger.debug("Complete prepare data for pick container 1");
					if (StringUtils.isNotEmpty(gateInTestDataReq.getContainerReceive2())) {
						logger.debug("pick full with container 2: " + gateInTestDataReq.getContainerReceive2());
						PickupAssign pickupAssign2 = new PickupAssign();
						BeanUtils.copyBeanProp(pickupAssign2, pickupAssign1);
						pickupAssign2.setShipmentDetailId(contId2);
						pickupAssign2.setId(null);
						pickupAssignService.insertPickupAssign(pickupAssign2);
						
						PickupHistory pickupHistory2 = new PickupHistory();
						BeanUtils.copyBeanProp(pickupHistory2, pickupHistory1);
						pickupHistory2.setShipmentDetailId(contId2);
						pickupHistory2.setId(null);
						pickupHistory2.setContainerNo(gateInTestDataReq.getContainerReceive2());
						pickupHistory2.setPickupAssignId(pickupAssign2.getId());
						pickupHistoryService.insertPickupHistory(pickupHistory2);
						
						logger.debug("Complete prepare data for pick container 2");
					}
				}
			} else {
				for (int i=0; i<gateInTestDataReq.getContainerAmount(); i++) {
					PickupAssign pickupAssign1 = new PickupAssign();
					pickupAssign1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
					pickupAssign1.setShipmentId(shipment2.getId());
					pickupAssign1.setDriverId(gateInTestDataReq.getDriverId());
					pickupAssign1.setTruckNo(gateInTestDataReq.getTruckNo());
					pickupAssign1.setChassisNo(gateInTestDataReq.getChassisNo());
					
					pickupAssignService.insertPickupAssign(pickupAssign1);
					
					PickupHistory pickupHistory1 = new PickupHistory();
					pickupHistory1.setLogisticGroupId(gateInTestDataReq.getLogisticGroupId());
					pickupHistory1.setShipmentId(shipment2.getId());
					pickupHistory1.setDriverId(gateInTestDataReq.getDriverId());
					pickupHistory1.setPickupAssignId(pickupAssign1.getId());
					pickupHistory1.setTruckNo(gateInTestDataReq.getTruckNo());
					pickupHistory1.setChassisNo(gateInTestDataReq.getChassisNo());
					pickupHistory1.setStatus(0);
					pickupHistory1.setGatePass(gateInTestDataReq.getGatePass());
					pickupHistoryService.insertPickupHistory(pickupHistory1);
				}
			}
		}
		return success();
	}
	
	@GetMapping("/blNo/{blNo}/yardPosition")
	@ResponseBody
	public AjaxResult getYardPositionByBlNo(@PathVariable String blNo) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ShipmentDetail> shipmentDetails = catosApiService.getCoordinateOfContainers(blNo);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			List<ShipmentDetail> coordinates = new ArrayList<>(shipmentDetails);
			List<ShipmentDetail[][]> bay = shipmentDetailService.getContPosition(coordinates, shipmentDetails);
			ajaxResult.put("bayList", bay);
			return ajaxResult;
		}
		return AjaxResult.warn("Không tìm thấy tọa độ.");
	}
	
	@GetMapping("/jobOrder/{jobOrder}/yardPosition")
	@ResponseBody
	public AjaxResult getYardPositionByJobOrder(@PathVariable("jobOrder") String jobOrder) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ShipmentDetail> shipmentDetails = catosApiService.getCoordinateOfContainersByJobOrderNo(jobOrder);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			List<ShipmentDetail> coordinates = new ArrayList<>(shipmentDetails);
			List<ShipmentDetail[][]> bay = shipmentDetailService.getContPosition(coordinates, shipmentDetails);
			ajaxResult.put("bayList", bay);
			return ajaxResult;
		}
		return AjaxResult.warn("Không tìm thấy tọa độ.");
	}
	
	@PostMapping("/pickupList")
	@ResponseBody
	public AjaxResult getPickupList(@RequestBody GateInTestDataReq gateInTestDataReq) {
		if (StringUtils.isNotEmpty(gateInTestDataReq.getTruckNo()) && StringUtils.isNotEmpty(gateInTestDataReq.getChassisNo())) {
			PickupHistory pickupHistory = new PickupHistory();
			pickupHistory.setTruckNo(gateInTestDataReq.getTruckNo());
			pickupHistory.setChassisNo(gateInTestDataReq.getChassisNo());
			pickupHistory.setStatus(0);
			List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistory);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("pickupList", getDataTable(pickupHistories));
			return ajaxResult;
 		}
		return AjaxResult.warn("Bạn chưa nhập đủ thông tin tìm kiếm.");
	}
}
