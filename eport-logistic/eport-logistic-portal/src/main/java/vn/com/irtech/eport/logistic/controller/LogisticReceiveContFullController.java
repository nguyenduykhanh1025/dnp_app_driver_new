package vn.com.irtech.eport.logistic.controller;

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
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.logistic.utils.R;

import java.io.IOException;


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

	@GetMapping()
	public String receiveContFull() {
		return PREFIX + "/index";
	}

	@GetMapping("/getFieldList")
	@ResponseBody
	public AjaxResult GetField(){
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("consigneeList", shipmentDetailService.getConsigneeList());
		return ajaxResult;
	}
	
	@RequestMapping("/listShipment")
	@ResponseBody
	public TableDataInfo listShipment(Shipment shipment) {
		startPage();
		LogisticAccount user = getUser();
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setServiceId(1);
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
		mmap.put("groupName", getGroup().getGroupName());
		return PREFIX + "/add";
	}

	@PostMapping("/checkBlNoUnique")
	@ResponseBody
	public AjaxResult checkBlNoUnique(Shipment shipment) {
		shipment.setServiceId(1);
		if (shipmentService.checkBillNoIsUnique(shipment) == 0) {
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
		shipment.setServiceId(1);
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
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			int index = 0;
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				index++;
				if (shipmentDetail.getId() != null) {
					if (shipmentDetail.getContainerNo() == null || shipmentDetail.getContainerNo().equals("")) {
						shipmentDetailService.deleteShipmentDetailById(shipmentDetail.getId());
					} else {
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
					shipmentDetail.setRegisterNo(shipmentDetail.getShipmentId().toString() + index);
					shipmentDetail.setStatus(1);
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setDoStatus("N");
					shipmentDetail.setPreorderPickup("N");
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
			// shipmentDetail.setOpeCode("CMC");
			// shipmentDetail.setSztp("22G0");
			// shipmentDetail.setFe("F");
			// shipmentDetail.setConsignee("VINCOSHIP");
			// shipmentDetail.setSealNo("G8331306");
			// shipmentDetail.setExpiredDem(new Date());
			// shipmentDetail.setWgt(11l);
			// shipmentDetail.setVslNm("Vessel");
			// shipmentDetail.setVoyNo("Voyage");
			// shipmentDetail.setLoadingPort("LoadingPort");
			// shipmentDetail.setDischargePort("dischargePort");
			// shipmentDetail.setTransportType("Truck");
			// shipmentDetail.setEmptyDepot("emptyDepot");
			// shipmentDetail.setCustomStatus("N");
			// shipmentDetail.setPaymentStatus("N");
			// shipmentDetail.setProcessStatus("N");
			// shipmentDetail.setDoStatus("N");
			// shipmentDetail.setUserVerifyStatus("N");
			// shipmentDetail.setStatus(1);
			// shipmentDetail.setRemark("Ghi chu");
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

	@GetMapping("checkCustomStatusForm/{shipmentId}")
	public String checkCustomStatus(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {
		mmap.put("shipmentId", shipmentId);
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setStatus(1);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.size() > 0) {
			if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				mmap.put("contList", shipmentDetails);
			}
		}
		return PREFIX + "/checkCustomStatus";
	}

	@PostMapping("/checkCustomStatus")
	@ResponseBody
	public List<ShipmentDetail> checkCustomStatus(@RequestParam(value = "declareNoList[]") String[] declareNoList,
			String shipmentDetailIds) {
		if (declareNoList != null) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService
					.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0) {
				if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDetail.setStatus(2);
						shipmentDetail.setCustomStatus("R");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}
					return shipmentDetails;
				}
			}
		}
		return null;
	}

	@GetMapping("checkContListBeforeVerify/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("verifyOtpForm/{shipmentDetailIds}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		return PREFIX + "/verifyOtp";
	}

	@PostMapping("sendOTP")
	@ResponseBody
	public AjaxResult sendOTP(String shipmentDetailIds) {
		LogisticGroup lGroup = getGroup();

		OtpCode otpCode = new OtpCode();
		Random rd = new Random();
		long rD = rd.nextInt(900000)+100000;

		otpCodeService.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);

		otpCode.setShipmentDetailids(shipmentDetailIds);
		otpCode.setPhoneNumber(lGroup.getMobilePhone());
		otpCode.setOptCode(rD);
		otpCodeService.insertOtpCode(otpCode);

		String contentabc = "Lam lenh lay cont la  " + rD;
		String response = "";
		// try {

		// 	response = postOtpMessage(contentabc);
		// 	System.out.println(response);
		// } catch (IOException ex) {
		// 	// process the exception
		// }

		return AjaxResult.success(response.toString());
	}

	// // Send SMS OTP
	// private int otpGenerator() {
	// 	int opt = 000000;
	// 	// random code;
	// 	return opt;
	// }
	@PostMapping("/verifyOtp")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds,Long otp) {
		OtpCode otpCode = new OtpCode();
		otpCode.setShipmentDetailids(shipmentDetailIds);
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -5);
		otpCode.setCreateTime(cal.getTime());
		otpCode.setOptCode(otp);
		if (otpCodeService.verifyOtpCodeAvailable(otpCode) > 0) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService
					.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					shipmentDetail.setUserVerifyStatus("Y");
					shipmentDetail.setStatus(3);
					shipmentDetail.setProcessStatus("Y");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
				JSONObject data = new JSONObject();
				data.put("something", "something");
				sendDataToTopic(data.toString(), "receive_cont_full_order");
				return success("Xác thực OTP thành công");
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

	@GetMapping("pickTruckForm/{shipmentId}/{pickCont}/{shipmentDetailId}")
	public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, @PathVariable("pickCont") boolean pickCont,@PathVariable("shipmentDetailId") Integer shipmentDetailId, ModelMap mmap) {
		mmap.put("shipmentId", shipmentId);
		mmap.put("pickCont", pickCont);
		mmap.put("shipmentDetailId", shipmentDetailId);
		String transportId = "";
		String shipmentIds = "";
		if (!pickCont) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			shipmentDetail.setLogisticGroupId(getUser().getGroupId());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
				if (shipmentDetail2.getPreorderPickup() == null || !shipmentDetail2.getPreorderPickup().equals("Y")) {
					shipmentIds += shipmentDetail2.getId() + ",";
					if (shipmentDetail2.getTransportIds() != null && transportId.length() == 0) {
						transportId = shipmentDetail2.getTransportIds();
					}
				}
			}
		}
		mmap.put("transportIds", transportId);
		mmap.put("shipmentDetailIds", shipmentIds);
		return PREFIX + "/pickTruckForm";
	}

	@PostMapping("/pickTruck")
	@ResponseBody
	public AjaxResult pickTruck(String shipmentDetailIds, String driverIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setTransportIds(driverIds);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Điều xe thành công");
		}
		return error("Xảy ra lỗi trong quá trình điều xe.");
	}
	
}

