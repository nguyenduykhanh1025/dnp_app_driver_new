package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.logistic.utils.R;
import vn.com.irtech.eport.system.service.ISysConfigService;



@Controller
@RequestMapping("/logistic/receive-cont-full")
public class LogisticReceiveContFullController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/receiveContFull";

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
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentWithGroupById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
		}
		return PREFIX + "/edit";
	}

	@GetMapping("/custom-status/{shipmentDetailIds}")
	public String checkCustomStatus(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0) {
			if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
				mmap.put("contList", shipmentDetails);
			}
		}
		return PREFIX + "/checkCustomStatus";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		mmap.put("creditFlag", getGroup().getCreditFlag());
		if (shipmentDetails != null && shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			if (("Cảng Tiên Sa").equals(shipmentDetails.get(0).getEmptyDepot())) {
				mmap.put("sendContEmpty", true);
			}
			mmap.put("shipmentDetails", shipmentDetails);
		} else {
			mmap.put("sendContEmpty", false);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{isSendContEmpty}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("creditFlag") boolean creditFlag, @PathVariable("isSendContEmpty") boolean isSendContEmpty, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		mmap.put("creditFlag", creditFlag);
		mmap.put("isSendContEmpty", isSendContEmpty);
		return PREFIX + "/verifyOtp";
	}

	@GetMapping("/cont-list/yard-position/{blNo}")
	public String pickContOnDemand(@PathVariable("blNo") String blNo, ModelMap mmap) {
		ShipmentDetail shipmentDt = new ShipmentDetail();
		shipmentDt.setBlNo(blNo);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);
		//Get coordinate from catos test
		String url = Global.getApiUrl() + "/shipmentDetail/getCoordinateOfContainers";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.postForObject(url,shipmentDt , R.class);
		List<LinkedHashMap> coordinateOfList = (List<LinkedHashMap>) r.get("data");
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("bayList", shipmentDetailService.getContPosition(coordinateOfList, shipmentDetails));
			mmap.put("unitCosts", 20000);
		}
		return PREFIX + "/pickContOnDemand";
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

	@GetMapping("/payment/napas")
	public String napasPaymentForm() {
		return PREFIX + "/napasPaymentForm";
	}

	@GetMapping("/unique/bl-no/{blNo}")
	@ResponseBody
	public AjaxResult checkBlNoUnique(@PathVariable String blNo) {
		Shipment shipment = new Shipment();
		shipment.setServiceType(1);
		shipment.setLogisticGroupId(getUser().getGroupId());
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error();
	}

	@PostMapping("/shipment")
	@ResponseBody
	public AjaxResult addShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateTime(new Date());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(1);
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	@PutMapping("/shipment/{shipmentId}")
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

	@SuppressWarnings("unchecked")
	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		AjaxResult ajaxResult = AjaxResult.success();
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (shipment.getEdoFlg().equals("1") && shipmentDetails.size() == 0) {
				shipmentDetails = new ArrayList<>();
				//get infor from edi
				shipmentDetails = shipmentDetailService.getShipmentDetailsFromEDIByBlNo(shipment.getBlNo());
				//get infor from catos
				if (shipmentDetails.size() == 0 ){
					String url = Global.getApiUrl() + "/shipmentDetail/list";
					ShipmentDetail shipDetail = new ShipmentDetail();
					shipDetail.setBlNo(shipment.getBlNo());
					RestTemplate restTemplate = new RestTemplate();
					R r = restTemplate.postForObject( url, shipDetail, R.class);
					shipmentDetails = (List<ShipmentDetail>) r.get("data");
				}
			}
			ajaxResult.put("shipmentDetails", shipmentDetails);
		}
		return ajaxResult;
	}

	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null && shipmentDetails.size() > 0){
			LogisticAccount user = getUser();
			ShipmentDetail shipmentDt = shipmentDetails.get(0);
			Shipment shipment = new Shipment();
			boolean isCreated = true;
			if ("Cảng Tiên Sa".equals(shipmentDt.getEmptyDepot()) && shipmentDt.getVgmChk()) {
				shipment.setBlNo(shipmentDt.getBlNo());
				shipment.setServiceType(2);
				List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
				if (shipments == null || shipments.size() == 0) {
					shipment.setContainerAmount(Long.valueOf(shipmentDt.getTier()));
					shipment.setTaxCode(shipmentDt.getProcessStatus());
					shipment.setLogisticAccountId(user.getId());
					shipment.setLogisticGroupId(user.getGroupId());
					shipment.setCreateTime(new Date());
					shipmentService.insertShipment(shipment);
					isCreated = false;
				}
			}
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setProcessStatus(null);
				if (shipmentDetail.getId() != null && "N".equals(shipmentDetail.getUserVerifyStatus())) {
					shipmentDetail.setUpdateBy(user.getFullName());
					shipmentDetail.setUpdateTime(new Date());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				} else {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setFe("F");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setDoStatus("N");
					shipmentDetail.setPreorderPickup("N");
					if ("VN".equalsIgnoreCase(shipmentDetail.getDischargePort().substring(0, 2))) {
						shipmentDetail.setCustomStatus("R");
						shipmentDetail.setStatus(2);
					} else {
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setStatus(1);
					}
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
					if ("Cảng Tiên Sa".equals(shipmentDt.getEmptyDepot()) && !isCreated && shipmentDt.getVgmChk()) {
						shipmentDetail.setShipmentId(shipment.getId());
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setStatus(1);
						shipmentDetailService.insertShipmentDetail(shipmentDetail);
					}
				}
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@DeleteMapping("/shipment-detail/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentDetailIds);
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@GetMapping("/shipment-detail/bl-no/{blNo}/cont/{containerNo}")
	@ResponseBody
	public ShipmentDetail getContInfo(@PathVariable String blNo, @PathVariable String containerNo) {
		if (blNo != null && containerNo != null) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setBlNo(blNo);
			shipmentDetail.setContainerNo(containerNo);
			String url = Global.getApiUrl() + "/shipmentDetail/containerInfor";
			RestTemplate restTemplate = new RestTemplate();
			R r = restTemplate.postForObject(url, shipmentDetail, R.class);
			// List<ShipmentDetail> l = (List<ShipmentDetail>) r.get("data");
			ObjectMapper mapper = new ObjectMapper();
			ShipmentDetail aShipmentDetail = mapper.convertValue(r.get("data"), ShipmentDetail.class);
			return aShipmentDetail;
		} else {
			return null;
		}
	}

	@PostMapping("/custom-status/shipment-detail/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNoList[]") String[] declareNoList, @PathVariable String shipmentDetailIds) {
		if (declareNoList != null) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails != null && shipmentDetails.size() > 0) {
				if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						customQueueService.offerShipmentDetail(shipmentDetail);
					}
					return success();
				}
			}
		}
		return error();
	}

	@PostMapping("/otp/{otp}/verification/shipment-detail/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable String shipmentDetailIds, @PathVariable String otp, boolean creditFlag, boolean isSendContEmpty) {
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContFull(shipmentDetails, shipment, creditFlag);
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

	@PostMapping("/shipment-detail/pickup-cont")
	@ResponseBody
	public AjaxResult pickContOnDemand(@RequestBody List<ShipmentDetail> preorderPickupConts) {
		if (preorderPickupConts.size() > 0) {
			ShipmentDetail shipmentDt = new ShipmentDetail();
			shipmentDt.setBlNo(preorderPickupConts.get(0).getBlNo());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);
			String url = Global.getApiUrl() + "/shipmentDetail/getCoordinateOfContainers";
			RestTemplate restTemplate = new RestTemplate();
			R r = restTemplate.postForObject(url, shipmentDt, R.class);
			List<LinkedHashMap> coordinateOfList = (List<LinkedHashMap>) r.get("data");
			if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				if (shipmentDetailService.calculateMovingCont(coordinateOfList, preorderPickupConts, shipmentDetails)) {
					return success("Bốc container chỉ định thành công.");
				}
			}
		}
		return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
	}

	@PostMapping("/payment/{shipmentDetailIds}")
	@Transactional
	@ResponseBody
	public AjaxResult payment(@PathVariable String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(4);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

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
		shipment.setBlNo(blNo);
		String opeCode = shipmentService.getOpeCodeByBlNo(blNo);
		Long containerAmount = shipmentService.getCountContainerAmountByBlNo(blNo);
		if(opeCode != null) {
			// String edoFlg = shipmentService.getEdoFlgByOpeCode(opeCode);
			// if(edoFlg == null){
			// 	return error("Mã hãng tàu:"+ opeCode +" không có trong hệ thống. Vui lòng liên hệ Cảng!");
			// }
			shipment.setEdoFlg("1");
			ajaxResult = success();
			shipment.setOpeCode(opeCode);
			shipment.setContainerAmount(containerAmount);
			ajaxResult.put("shipment", shipment);
			return ajaxResult;
		} else {
			Shipment shipCatos = shipmentService.getOpeCodeCatosByBlNo(blNo);
			if (shipCatos != null) {
				String edoFlg = shipmentService.getEdoFlgByOpeCode(shipCatos.getOpeCode());
				if(edoFlg == null){
					return error("Mã hãng tàu:"+ shipCatos.getOpeCode() +" không có trong hệ thống. Vui lòng liên hệ Cảng!");
				}
				shipment.setEdoFlg(edoFlg);
				ajaxResult = success();
				shipment.setOpeCode(shipCatos.getOpeCode());
				shipment.setContainerAmount(shipCatos.getContainerAmount());
				ajaxResult.put("shipment", shipment);
				return ajaxResult;
			}
		}
		ajaxResult = error("Billing No không tồn tại!");
		return ajaxResult;
	}
}

