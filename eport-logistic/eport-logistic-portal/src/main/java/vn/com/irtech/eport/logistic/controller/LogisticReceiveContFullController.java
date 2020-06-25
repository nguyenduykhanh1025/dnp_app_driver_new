package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceRobotReq;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.logistic.utils.R;



@Controller
@RequestMapping("/logistic/receiveContFull")
public class LogisticReceiveContFullController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/receiveContFull";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired 
	private IOtpCodeService otpCodeService;

	@Autowired
	private IProcessOrderService processOrderService;

	@GetMapping()
	public String receiveContFull() {
		return PREFIX + "/index";
	}

	@Autowired
	private MqttService mqttService;
	
	@GetMapping("/getGroupNameByTaxCode")
	@ResponseBody
	public AjaxResult getGroupNameByTaxCode(String taxCode) throws Exception {
		AjaxResult ajaxResult = AjaxResult.success();
		if (taxCode == null || "".equals(taxCode)) {
			return error();
		}
		String groupName = shipmentDetailService.getGroupNameByTaxCode(taxCode);
		if (groupName != null) {
			ajaxResult.put("groupName", groupName);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
	
	@RequestMapping("/listShipment")
	@ResponseBody
	public TableDataInfo listShipment(Shipment shipment) {
		startPage();
		LogisticAccount user = getUser();
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setServiceType(1);
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}

	@GetMapping("/listShipmentDetail")
	@ResponseBody
	public AjaxResult listShipmentDetail(Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		AjaxResult ajaxResult = AjaxResult.success();
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (shipment.getEdoFlg().equals("1") && shipmentDetails.size() == 0) {
				shipmentDetails = new ArrayList<>();
				String url = Global.getApiUrl() + "/shipmentDetail/list";
				ShipmentDetail shipDetail = new ShipmentDetail();
				shipDetail.setBlNo(shipment.getBlNo());
				RestTemplate restTemplate = new RestTemplate();
				R r = restTemplate.postForObject( url, shipDetail, R.class);
				shipmentDetails = (List<ShipmentDetail>) r.get("data");
			}
			ajaxResult.put("shipmentDetails", shipmentDetails);
		}
		return ajaxResult;
	}

	@GetMapping("/addShipmentForm")
	public String add(ModelMap mmap) {
		return PREFIX + "/add";
	}

	@PostMapping("/checkBlNoUnique")
	@ResponseBody
	public AjaxResult checkBlNoUnique(Shipment shipment) {
		shipment.setServiceType(1);
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error();
	}

	@PostMapping("/addShipment")
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

	@GetMapping("/editShipmentForm/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentWithGroupById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
		}
		return PREFIX + "/edit";
	}

	@PostMapping("/editShipment")
	@ResponseBody
	public AjaxResult editShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			shipment.setUpdateTime(new Date());
			shipment.setUpdateBy(user.getFullName());
			if (shipmentService.updateShipment(shipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@PostMapping("/saveShipmentDetail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (shipmentDetail.getId() != null) {
					if (shipmentDetail.getContainerNo() == null || shipmentDetail.getContainerNo().equals("")) {
						shipmentDetailService.deleteShipmentDetailById(shipmentDetail.getId());
					} else if ("N".equals(shipmentDetail.getUserVerifyStatus())) {
						shipmentDetail.setUpdateBy(user.getFullName());
						shipmentDetail.setUpdateTime(new Date());
						if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
							return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
						}
					}
				} else {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setFe("F");
					shipmentDetail.setPaymentStatus("N");
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
				}
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@PostMapping("/deleteShipmentDetail")
	@ResponseBody
	public AjaxResult deleteShipmentDetail(String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentDetailIds);
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@PostMapping("/getContInfo")
	@ResponseBody
	public ShipmentDetail getContInfo(ShipmentDetail shipmentDetail) {
		if (shipmentDetail.getBlNo() != null && shipmentDetail.getContainerNo() != null) {
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

	@GetMapping("checkCustomStatusForm/{shipmentDetailIds}")
	@Transactional
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

	@PostMapping("/checkCustomStatus")
	@ResponseBody
	public List<ShipmentDetail> checkCustomStatus(@RequestParam(value = "declareNoList[]") String[] declareNoList,
			String shipmentDetailIds) throws IOException {
		if (declareNoList != null) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService
					.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0) {
				if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						try {
							Thread.sleep(500);
							if(shipmentDetailService.checkCustomStatus(shipmentDetail.getVoyNo(),shipmentDetail.getContainerNo()) == true)
							{
								shipmentDetail.setStatus(4);
								shipmentDetail.setCustomStatus("R");
								shipmentDetailService.updateShipmentDetail(shipmentDetail);
								// push notification with socketIO 
							}else {
								// push notification with socketIO 
							};
						
						} catch(Exception e) {
							e.printStackTrace(); 
						}
					}
					return shipmentDetails;
				}
			}
		}
		return null;
	}

	@GetMapping("checkContListBeforeVerify/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		mmap.put("creditFlag", getGroup().getCreditFlag());
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("verifyOtpForm/{shipmentDetailIds}/{creditFlag}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("creditFlag") boolean creditFlag, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		mmap.put("creditFlag", creditFlag);
		return PREFIX + "/verifyOtp";
	}

	@PostMapping("sendOTP")
	@ResponseBody
	public AjaxResult sendOTP(String shipmentDetailIds) {
		LogisticGroup lGroup = getGroup();

		OtpCode otpCode = new OtpCode();
		Random rd = new Random();
		long rD = rd.nextInt(900000)+100000;
		String tDCode = Long.toString(rD);
		otpCodeService.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);

		otpCode.setTransactionId(shipmentDetailIds);
		otpCode.setPhoneNumber(lGroup.getMobilePhone());
		otpCode.setOtpCode(tDCode);
		otpCode.setOtpType("1");

		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, +5);
		otpCode.setExpiredTime(cal.getTime());
		otpCodeService.insertSysOtp(otpCode);

		String content = "TEST SMS   " + rD;
		String response = "";
		try {
			response = otpCodeService.postOtpMessage(lGroup.getMobilePhone(),content);
			System.out.println(response);
		} catch (IOException ex) {
			// process the exception
		}
		return AjaxResult.success("response.toString()");
	}

	@PostMapping("/verifyOtp")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds, String otp, boolean creditFlag) {
		OtpCode otpCode = new OtpCode();
		otpCode.setTransactionId(shipmentDetailIds);
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -5);
		otpCode.setCreateTime(cal.getTime());
		otpCode.setOtpCode(otp);
		if (otpCodeService.verifyOtpCodeAvailable(otpCode) == 1) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				AjaxResult ajaxResult = null;
				Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
				List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService
						.makeOrderReceiveContFull(shipmentDetails, shipment, creditFlag);
				if (serviceRobotReqs != null) {
					processOrderService.insertProcessOrderReceiveContFull(serviceRobotReqs);
					//
					List<Long> processIds = new ArrayList<>();
					for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
						processIds.add(serviceRobotReq.processOrder.getId());
					}
					try {
						if (!mqttService.publishMessageToRobot(serviceRobotReqs.get(0),
								EServiceRobot.RECEIVE_CONT_FULL)) {
							ajaxResult = AjaxResult
									.warn("Yêu cầu đang được xử lý. Hệ thống sẽ thông báo khi có kết quả!");
							return ajaxResult;
						}
					} catch (Exception e) {
						return error("Có lỗi xảy ra trong quá trình xác thực!");
					}

					ajaxResult = AjaxResult.success("Xác thực OTP thành công");
					ajaxResult.put("processIds", processIds);
					ajaxResult.put("orderNumber", serviceRobotReqs.size());
					return ajaxResult;
				} else {
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}
			}
		}
		return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
	}

	@GetMapping("pickContOnDemandForm/{blNo}")
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

	@PostMapping("/pickContOnDemand")
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

	@GetMapping("paymentForm/{shipmentDetailIds}")
	public String paymentForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return PREFIX + "/paymentForm";
	}

	@PostMapping("/payment")
	@Transactional
	@ResponseBody
	public AjaxResult payment( String shipmentDetailIds) {
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

	@GetMapping("/getField")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class);
		List<String> listConsignee = (List<String>) r.get("data");
		ajaxResult.put("consigneeList", listConsignee);
		return ajaxResult;
	}
	
}

