package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.constant.SystemConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@Controller
@RequestMapping("/logistic/receive-cont-full")
public class LogisticReceiveContFullController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/receiveContFull";

	private static final Logger logger = LoggerFactory.getLogger(LogisticReceiveContFullController.class);

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired 
	private IOtpCodeService otpCodeService;

	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private CustomQueueService customQueueService;

	@Autowired
	private ISysConfigService configService;
	
	@Autowired
	private MqttService mqttService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IEdoService edoService;

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private IEdoHouseBillService edoHouseBillService;
	
	@Autowired
	private DictService dictService;
	
	@GetMapping()
	public String receiveContFull(ModelMap mmap) {
		List<String> emptyDepots = new ArrayList<>();
		String danangPortName = configService.selectConfigByKey("danang.port.name");
		if (danangPortName != null) {
			emptyDepots.add(danangPortName);
		}
		emptyDepots.add("Cảng Khác");
		mmap.put("emptyDepots", emptyDepots);
		return PREFIX + "/index";
	}

	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		mmap.put("taxCode", getGroup().getMst());
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
			mmap.put("taxCode", getGroup().getMst());
		}
		return PREFIX + "/edit";
	}

	@GetMapping("/custom-status/{shipmentDetailIds}")
	public String checkCustomStatus(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
 		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
			mmap.put("contList", shipmentDetails);
		}
		return PREFIX + "/checkCustomStatus";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			if (("Cảng Tiên Sa").equals(shipmentDetails.get(0).getEmptyDepot())) {
				mmap.put("sendContEmpty", true);
			}
			mmap.put("shipmentDetails", shipmentDetails);
		} else {
			mmap.put("sendContEmpty", false);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{isSendContEmpty}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, 
			@PathVariable("creditFlag") boolean creditFlag, @PathVariable("isSendContEmpty") boolean isSendContEmpty, 
			@PathVariable("taxCode") String taxCode, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		mmap.put("creditFlag", creditFlag);
		mmap.put("isSendContEmpty", isSendContEmpty);
		mmap.put("taxCode", taxCode);
		return PREFIX + "/verifyOtp";
	}

	@GetMapping("/payment/{processOrderIds}")
	public String paymentForm(@PathVariable("processOrderIds") String processOrderIds, ModelMap mmap) {
		String shipmentDetailIds = "";
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderIds);
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds += shipmentDetail.getId() + ",";
		}
		if (!"".equalsIgnoreCase(shipmentDetailIds)) {
			shipmentDetailIds.substring(0, shipmentDetailIds.length()-1);
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("processBills", processBillService.selectProcessBillListByProcessOrderIds(processOrderIds));
		return PREFIX + "/paymentForm";
	}

	@GetMapping("/unique/bl-no/{blNo}")
	@ResponseBody
	public AjaxResult checkBlNoUnique(@PathVariable String blNo) {
		Shipment shipment = new Shipment();
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		shipment.setLogisticGroupId(getUser().getGroupId());
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error();
	}

	@Log(title = "Thêm Lô Bốc Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
	@ResponseBody
	public AjaxResult addShipment(Shipment shipment) {
		
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			if (edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber()) == 0) {
				return error("Thêm lô thất bại");
			} else {
				if (edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber()) == 0) {
					return error("Thêm lô thất bại");
				}
			}
		} 
		
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateTime(new Date());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setStatus("1");
		if (shipmentService.insertShipment(shipment) == 1) {
//			// assign driver default
//			PickupAssign pickupAssign = new PickupAssign();
//			pickupAssign.setLogisticGroupId(getUser().getGroupId());
//			pickupAssign.setShipmentId(shipment.getId());
//			//list driver
//			DriverAccount driverAccount = new DriverAccount();
//			driverAccount.setLogisticGroupId(getUser().getGroupId());
//			driverAccount.setDelFlag(false);
//			driverAccount.setStatus("0");
//			List<DriverAccount> driverAccounts = driverAccountService.selectDriverAccountList(driverAccount);
//			if(driverAccounts.size() > 0) {
//				for(DriverAccount i : driverAccounts) {
//					pickupAssign.setDriverId(i.getId());
//					pickupAssign.setFullName(i.getFullName());
//					pickupAssign.setPhoneNumber(i.getMobileNumber());
//					pickupAssign.setCreateBy(getUser().getFullName());
//					pickupAssignService.insertPickupAssign(pickupAssign);
//				}
//			}
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	@Log(title = "Chỉnh Sửa Lô", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment/{shipmentId}")
	@ResponseBody
	public AjaxResult editShipment(Shipment shipment, @PathVariable Long shipmentId) {
		LogisticAccount user = getUser();
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		//check if current user own shipment
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			shipment.setUpdateTime(new Date());
			shipment.setUpdateBy(user.getFullName());
			if (shipmentService.updateShipment(shipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		AjaxResult ajaxResult = AjaxResult.success();
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailList(shipmentDetail);
			if (shipment.getEdoFlg().equals("1") && shipmentDetails.size() == 0) {
				if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
					shipmentDetails = shipmentDetailService.getShipmentDetailFromHouseBill(shipment.getHouseBill());
					
				} else {
					shipmentDetails = new ArrayList<>();
					//get infor from edi
					shipmentDetails = shipmentDetailService.getShipmentDetailsFromEDIByBlNo(shipment.getBlNo());
				}
				//get infor from catos
				List<ShipmentDetail> shipmentDetailsCatos = catosApiService.selectShipmentDetailsByBLNo(shipment.getBlNo());
				//Get opecode, sealNo, wgt, pol,pod
				if(shipmentDetailsCatos != null) {
					for(ShipmentDetail i : shipmentDetails) {
						i.setVoyNo(i.getVoyCarrier());
						for(ShipmentDetail j : shipmentDetailsCatos) {
							if(i.getContainerNo().equals(j.getContainerNo())) {
								// Overwrite information from CATOS
//								i.setOpeCode(j.getOpeCode());
								i.setVslNm(j.getVslNm());					// overwrite VSL_CD:VSL_NM from CATOS
								i.setVoyNo(j.getVoyNo());
								i.setSealNo(j.getSealNo());
								i.setWgt(j.getWgt());
								i.setLoadingPort(j.getLoadingPort());		// overwrite from CATOS
								i.setDischargePort(j.getDischargePort());	// overwrite from CATOS
								i.setYear(j.getYear());
							}
						}
					}
				}
			}
			ajaxResult.put("shipmentDetails", shipmentDetails);
		}
		return ajaxResult;
	}

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null && shipmentDetails.size() > 0){
			String dnPortName = configService.selectConfigByKey("danang.port.name");
			LogisticAccount user = getUser();
			ShipmentDetail shipmentDt = shipmentDetails.get(0);
			Shipment shipmentSendCont = new Shipment();
			boolean isCreated = true;
			Shipment shipment = new Shipment();
			shipment.setId(shipmentDetails.get(0).getShipmentId());
			boolean updateShipment = true;
			boolean isSendEmpty = shipmentDt.getVgmChk();
			if (dnPortName.equals(shipmentDt.getEmptyDepot()) && isSendEmpty) {
				shipmentSendCont.setBlNo(shipmentDt.getBlNo());
				shipmentSendCont.setServiceType(Constants.SEND_CONT_EMPTY);
				List<Shipment> shipments = shipmentService.selectShipmentList(shipmentSendCont);
				if (shipments == null || shipments.size() == 0) {
					shipmentSendCont.setContainerAmount(Long.valueOf(shipmentDt.getTier()));
					shipmentSendCont.setLogisticAccountId(user.getId());
					shipmentSendCont.setLogisticGroupId(user.getGroupId());
					shipmentSendCont.setCreateTime(new Date());
					shipmentSendCont.setStatus("1");
					shipmentService.insertShipment(shipmentSendCont);
					isCreated = false;
				}
			}
			String taxCode = catosApiService.getTaxCodeBySnmGroupName(shipmentDt.getConsignee());
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setProcessStatus(null);
				shipmentDetail.setCustomStatus(null);
				shipmentDetail.setVgmChk(null);
				if (shipmentDetail.getId() == null) {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setFe("F");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setDoStatus("N");
					shipmentDetail.setPreorderPickup("N");
					shipmentDetail.setFinishStatus("N");
					// set consignee Taxcode
					shipmentDetail.setTaxCode(taxCode);
					shipmentDetail.setConsigneeByTaxCode(shipmentDetail.getConsignee());
					if ("VN".equalsIgnoreCase(shipmentDetail.getLoadingPort().substring(0, 2))) {
						shipmentDetail.setCustomStatus("R");
						shipmentDetail.setStatus(2);
					} else {
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setStatus(1);
					}
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
					if (dnPortName.equals(shipmentDt.getEmptyDepot()) && !isCreated && isSendEmpty) {
						shipmentDetail.setShipmentId(shipmentSendCont.getId());
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setFe("E");
						shipmentDetail.setCargoType("MT");
						shipmentDetail.setDischargePort("VNDAD");
						shipmentDetail.setVslNm("EMTY");
						shipmentDetail.setVoyNo("0000");
						shipmentDetail.setEmptyDepotLocation(getEmptyDepotLocation(shipmentDetail.getSztp(), shipmentDetail.getOpeCode()));
						shipmentDetail.setStatus(1);
						shipmentDetailService.insertShipmentDetail(shipmentDetail);
					}
					
				} else {
					updateShipment = false;
					shipmentDetail.setUpdateBy(user.getFullName());
					shipmentDetail.setTaxCode(taxCode);
					shipmentDetail.setConsigneeByTaxCode(shipmentDetail.getConsignee());
					shipmentDetail.setUpdateTime(new Date());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				}
			}
			if (updateShipment) {
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipment.setStatus("2");
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@Log(title = "Xóa Khai Báo Cont", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
	@DeleteMapping("/shipment/{shipmentId}/shipment-detail/{shipmentDetailIds}")
	@Transactional
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId, @PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentDetailIds);
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setStatus("1");
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@GetMapping("/shipment-detail/bl-no/{blNo}/cont/{containerNo}")
	@ResponseBody
	public ShipmentDetail getContInfo(@PathVariable("blNo") String blNo, @PathVariable("containerNo") String containerNo) {
		if (blNo != null && containerNo != null) {
			ShipmentDetail shipmentDetail = catosApiService.selectShipmentDetailByContNo(blNo, containerNo);
			return shipmentDetail;
		} else {
			return null;
		}
	}

	@Log(title = "Check Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList, @RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
			boolean customsNoMappingFlg = "1".equals(configService.selectConfigByKey(SystemConstants.ACCIS_CUSTOM_MAPPING_FLG_KEY));
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declareNoList to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// Neu bat buoc check to khai thi phai goi lai acciss
					if (!customsNoMappingFlg && catosApiService.checkCustomStatus(shipmentDetail.getContainerNo(), shipmentDetail.getVoyNo())) {
						shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						shipmentDetail.setCustomStatus("R");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
						AjaxResult ajaxResult = AjaxResult.success();
						ajaxResult.put("shipmentDetail", shipmentDetail);
						webSocketService.sendMessage("/" + shipmentDetail.getContainerNo() + "/response", ajaxResult);
					} else {
						customQueueService.offerShipmentDetail(shipmentDetail);
					}
				}
				return success();
			}
		}
		return error();
	}

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds, @PathVariable("otp") String otp, String taxCode, boolean creditFlag, boolean isSendContEmpty) {
		try {
			Long.parseLong(otp);
		} catch (Exception e) {
			return error("Mã OTP nhập vào không hợp lệ!");
		}
		OtpCode otpCode = new OtpCode();
		otpCode.setTransactionId(shipmentDetailIds);
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -5);
		otpCode.setCreateTime(cal.getTime());
		otpCode.setOtpCode(otp);
		if (otpCodeService.verifyOtpCodeAvailable(otpCode) != 1) {
			return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
		}
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!"3".equals(shipment.getStatus())) {
				shipment.setStatus("3");
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContFull(shipmentDetails, shipment, taxCode, creditFlag);
			if (serviceRobotReqs != null) {
				List<Long> processIds = new ArrayList<>();
				boolean robotBusy = false;

				// MAKE ORDER SEND CONT EMPTY
				// if (isSendContEmpty) {
				// 	shipment.setId(null);
				// 	List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
				// 	if (shipments != null && shipments.size() > 0) {
				// 		String conts = "";
				// 		for (ShipmentDetail shipmentDt: shipmentDetails) {
				// 			conts += shipmentDt.getContainerNo() + ",";
				// 		}
				// 		conts = conts.substring(0, conts.length()-1);
				// 		List<ShipmentDetail> shipmentDetails2 = shipmentDetailService.selectSendEmptyShipmentDetailByListCont(conts, shipments.get(0).getId());
				// 		ProcessOrder processOrder = shipmentDetailService.makeOrderSendCont(shipmentDetails2, shipments.get(0), creditFlag);
				// 		ServiceRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails2);
				// 		try {
				// 			mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_EMPTY);
				// 		} catch (Exception e) {
				// 			e.printStackTrace();
				// 		}
				// 	}
				// }

				// MAKE ORDER RECEIVE CONT FULL
				try {
					for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
						processIds.add(serviceRobotReq.processOrder.getId());
						if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.RECEIVE_CONT_FULL)) {
							robotBusy = true;
						}
					}
					if (robotBusy) {
						ajaxResult = AjaxResult.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
						ajaxResult.put("processIds", processIds);
						ajaxResult.put("orderNumber", serviceRobotReqs.size());
						return ajaxResult;
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}
				ajaxResult = AjaxResult.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processIds", processIds);
				ajaxResult.put("orderNumber", serviceRobotReqs.size());
				return ajaxResult;
			}
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	// @Log(title = "Bốc Chỉ Định", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	// @PostMapping("/shipment-detail/pickup-cont/{isCredit}")
	// @ResponseBody
	// public AjaxResult pickContOnDemand(@RequestBody List<ShipmentDetail> preorderPickupConts, @PathVariable("isCredit") Boolean isCredit) {
	// 	// Check if logistic can pay by credit
	// 	if (getGroup().getCreditFlag() == "0" && isCredit) {
	// 		return error("Qúy khách không có quyền thanh toán trả sau!");
	// 	}
	// 	if (!preorderPickupConts.isEmpty()) {
	// 		ShipmentDetail shipmentDt = new ShipmentDetail();
	// 		shipmentDt.setBlNo(preorderPickupConts.get(0).getBlNo());
	// 		shipmentDt.setFe("F");
	// 		shipmentDt.setLogisticGroupId(getUser().getGroupId());

	// 		// Check if logistic own preorderPickupConts
	// 		if (preorderPickupConts.size() != shipmentDetailService.countNumberOfLegalCont(preorderPickupConts, getUser().getGroupId())) {
	// 			return error("Bạn không có quyền bốc chỉ định những container này!");
	// 		}

	// 		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);

	// 		Shipment shipment = null;
	// 		if (shipmentDetails.isEmpty()) {
	// 			return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
	// 		} else {
	// 			shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
	// 		}

	// 		//Get coordinate from catos test
	// 		List<ShipmentDetail> coordinateOfList = catosApiService.getCoordinateOfContainers(preorderPickupConts.get(0).getBlNo());
	// 		AjaxResult ajaxResult = AjaxResult.success();
	// 		List<Long> orderIds = new ArrayList<>();
	// 		if (!shipmentDetails.isEmpty()) {
	// 			List<ServiceSendFullRobotReq> reqs = shipmentDetailService.calculateMovingCont(coordinateOfList, preorderPickupConts, shipmentDetails, shipment, isCredit);
	// 			if (reqs == null) {
	// 				return error("Không có container nào cần làm lệnh dịch chuyển!");
	// 			}
	// 			try {
	// 				for (ServiceSendFullRobotReq robotReq : reqs) {
	// 					orderIds.add(robotReq.processOrder.getId());
	// 					mqttService.publishMessageToRobot(robotReq, EServiceRobot.SHIFTING_CONT);
	// 				}
	// 			} catch (MqttException e) {
	// 				logger.warn(e.getMessage());
	// 				return AjaxResult.warn("Lệnh dịch chuyển của quý khách đang được chờ xử lý!");
	// 			}
	// 		}
	// 		ajaxResult.put("orderIds", orderIds);
	// 		return ajaxResult;
	// 	}
	// 	return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
	// }

	@SuppressWarnings("unchecked")
	@GetMapping("/consignees")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		List<String> listConsignee = (List<String>) CacheUtils.get("consigneeList");
		if (listConsignee == null) {
			listConsignee = shipmentDetailService.getConsigneeList();
			CacheUtils.put("consigneeList", listConsignee);
		}
		ajaxResult.put("consigneeList", listConsignee);
		return ajaxResult;
	}
	
	@GetMapping("/shipment/bl-no/{blNo}")
	@ResponseBody
	public AjaxResult checkShipmentInforByBlNo(@PathVariable String blNo) {
		AjaxResult ajaxResult = new AjaxResult();
		Shipment shipment = new Shipment();
		//check bill unique
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}
		//check opeCode
		String opeCode = edoService.getOpeCodeByBlNo(blNo);
		// Long containerAmount = edoService.getCountContainerAmountByBlNo(blNo);
		
		// Check edo with master bill
		if(opeCode != null) {
			shipment.setEdoFlg("1");
			shipment.setOpeCode(opeCode);
			// shipment.setContainerAmount(containerAmount);
			ajaxResult = success();
			ajaxResult.put("shipment", shipment);
			return ajaxResult;
		} else {
			// Check edo with house bill
			EdoHouseBill edoHouseBill = edoHouseBillService.getEdoHouseBillByBlNo(blNo);
			if (edoHouseBill != null) {
				shipment.setEdoFlg("1");
				shipment.setOpeCode(edoHouseBill.getCarrierCode());
				shipment.setHouseBill(blNo);
				shipment.setBlNo(edoService.getBlNoByHouseBillId(edoHouseBill.getId()));
				// shipment.setContainerAmount(containerAmount);
				ajaxResult = success();
				ajaxResult.put("shipment", shipment);
				return ajaxResult;
			} else {
				// check do
				Shipment shipCatos = shipmentService.getOpeCodeCatosByBlNo(blNo);
				if (shipCatos != null) {
					String edoFlg = carrierGroupService.getDoTypeByOpeCode(shipCatos.getOpeCode());
					if(edoFlg == null){
						return error("Mã hãng tàu:"+ shipCatos.getOpeCode() +" không có trong hệ thống. Vui lòng liên hệ Cảng!");
					}
//					if(edoFlg.equals("1")){
//						return error("Bill này là eDO nhưng không có dữ liệu trong eport. Vui lòng liên hệ Cảng!");
//					}
					shipment.setEdoFlg(edoFlg);
					ajaxResult = success();
					shipment.setOpeCode(shipCatos.getOpeCode());
					shipment.setContainerAmount(shipCatos.getContainerAmount());
					ajaxResult.put("shipment", shipment);
					return ajaxResult;
				}
			}
		} 
		ajaxResult = error("Số bill không tồn tại!");
		return ajaxResult;
	}

	/**
	 * Check order number match with bl no for edo (master bill, house bill)
	 * 
	 * @param shipment
	 * @return AjaxResult
	 */
	@PostMapping("/orderNumber/check")
	@ResponseBody
	public AjaxResult checkOrderNumber(@RequestBody Shipment shipment) {
		int containerAmount = 0;
		// Check for house bill case
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			containerAmount = edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber());
		} else {
			// Check for master bill case
			containerAmount = edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber());
		}
		if (containerAmount != 0) {
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("containerAmount", containerAmount);
			return ajaxResult;
		}
		return error("Mã nhận container không chính xác.");
	}
	
	@GetMapping("/shipment/{shipmentId}/delegate/permission")
	@ResponseBody
	public AjaxResult checkDelegatePermission(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		// check DO or eDO (0 is DO no need to validate 
		// Get tax code of consignee own this shipment
		String taxCode = shipmentDetailService.selectConsigneeTaxCodeByShipmentId(shipmentId);
		if (taxCode == null) {
			return error();
		}
		
		// Check if logistic can make order for this shipment
		if (logisticGroupService.checkDelegatePermission(taxCode, getGroup().getMst(), EportConstants.DELEGATE_PERMISSION_PROCESS) > 0) {
			return success();
		}
		return error();
	}
	
	@GetMapping("/shipment/{shipmentId}/custom/notification")
	@ResponseBody
	public AjaxResult sendNotificationCustomError(@PathVariable("shipmentId") String shipmentId) {
		try {
			mqttService.sendNotification(NotificationCode.NOTIFICATION_OM_CUSTOM, "Lỗi hải quan lô " + shipmentId, configService.selectConfigByKey("domain.admin.name") + "/om/support/custom/" + shipmentId);
		} catch (MqttException e) {
			logger.error("Gửi thông báo lỗi hải quan cho om: " + e);
		}
		return success();
	}
	
	/**
	 * Get empty depot location base on sztp and opr in dictionary
	 * 
	 * @param sztp
	 * @param opr
	 * @return empty depot location string (DANALOG 01, DANALOG 02, Tiên Sa)
	 */
	private String getEmptyDepotLocation(String sztp, String opr) {
		String emptyDepotRule = dictService.getLabel("empty_depot_location", opr);
		if (StringUtils.isNotEmpty((emptyDepotRule))) {
			String[] emptyDepotArr = emptyDepotRule.split(",");
			int length = emptyDepotArr.length;
			for (int i = 0; i < length; i++) {
				if (sztp.equalsIgnoreCase(emptyDepotArr[i])) {
					String emptyDepotLocation = emptyDepotArr[length - 1];
					return emptyDepotLocation;
				}
			}
		}
		String danangDepotName = configService.selectConfigByKey("danang.depot.name");
		return danangDepotName;
	}
}

