package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.json.JSONObject;
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
@RequestMapping("/logistic/sendContFull")
public class LogisticSendContFullController extends LogisticBaseController {
    
    private final String PREFIX = "logistic/sendContFull";
	
	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private MqttService mqttService;

    @GetMapping()
	public String sendContEmpty() {
		return PREFIX + "/index";
	}

	@GetMapping("/getGroupNameByTaxCode")
	@ResponseBody
	public AjaxResult getGroupNameByTaxCode(String taxCode){
		AjaxResult ajaxResult = AjaxResult.success();
		if (taxCode == null || "".equals(taxCode)) {
			return error();
		}
		// String groupName = shipmentDetailService.getNameCompany(taxCode);
		String groupName = "Công ty abc";
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
		shipment.setServiceType(4);
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}

	@GetMapping("/addShipmentForm")
	public String add(ModelMap mmap) {
		return PREFIX + "/add";
	}

	@PostMapping("/checkBookingNoUnique")
	@ResponseBody
	public AjaxResult checkBookingNoUnique(Shipment shipment) {
		shipment.setServiceType(4);
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
		shipment.setServiceType(4);
		shipment.setBlNo("null");
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

	@GetMapping("/listShipmentDetail")
	@ResponseBody
	public AjaxResult listShipmentDetail(Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (shipmentDetails != null) {
				ajaxResult.put("shipmentDetails", shipmentDetails);
			} else {
				ajaxResult = AjaxResult.error();
			}
		}
		return ajaxResult;
	}

	@PostMapping("/saveShipmentDetail")
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
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
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setFe("F");
					if (shipmentDetail.getLoadingPort() == null || shipmentDetail.getLoadingPort().equals("")) {
						shipmentDetail.setLoadingPort(" ");
					}
					if (shipmentDetail.getDischargePort() == null || shipmentDetail.getDischargePort().equals("")) {
						shipmentDetail.setDischargePort(" ");
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

	@GetMapping("checkCustomStatusForm/{shipmentId}")
	@Transactional
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
			String shipmentDetailIds) throws IOException {
		if (declareNoList != null) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService
					.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0) {
				if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						try {
							Thread.sleep(5000);
							if(shipmentDetailService.checkCustomStatus(shipmentDetail.getVoyNo(),shipmentDetail.getContainerNo()) == true)
							{
								shipmentDetail.setStatus(2);
								shipmentDetail.setCustomStatus("R");
								shipmentDetailService.updateShipmentDetail(shipmentDetail);
								// push notification with socketIO 
							}else {
								// push notification with socketIO 
							};
						
						} catch(Exception e) {
							//Exception 
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
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("verifyOtpForm/{shipmentDetailIds}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		return PREFIX + "/verifyOtp";
	}

	@PostMapping("sendOTP")
	@ResponseBody
	public AjaxResult sendOTP(String shipmentDetailIds) {
		// LogisticGroup lGroup = getGroup();

		// OtpCode otpCode = new OtpCode();
		// Random rd = new Random();
		// long rD = rd.nextInt(900000)+100000;

		// otpCodeService.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);

		// otpCode.setShipmentDetailids(shipmentDetailIds);
		// otpCode.setPhoneNumber(lGroup.getMobilePhone());
		// otpCode.setOptCode(rD);
		// otpCodeService.insertOtpCode(otpCode);

		// String content = "Lam lenh giaoa cont la  " + rD;
		// String response = "";
//		try {
//			response = otpCodeService.postOtpMessage(content);
//			System.out.println(response);
//		} catch (IOException ex) {
//			// process the exception
//		}
		return AjaxResult.success("response.toString()");
	}

	@PostMapping("/verifyOtp")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds,Long otp) {
		
		// OtpCode otpCode = new OtpCode();
		// otpCode.setShipmentDetailids(shipmentDetailIds);
		// Date now = new Date();
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(now);
		// cal.add(Calendar.MINUTE, -5);
		// otpCode.setCreateTime(cal.getTime());
		// otpCode.setOptCode(otp);
		// if (otpCodeService.verifyOtpCodeAvailable(otpCode) == 1) {
		// 	List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		// 	if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
		// 		Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
		// 		ProcessOrder processOrder = shipmentDetailService.makeOrderSendContFull(shipmentDetails, shipment, getGroup().getCreditFlag());
		// 		if (processOrder != null) {
		// 			processOrderService.insertProcessOrder(processOrder);
		// 			//
		// 			ServiceRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		// 			try {
		// 				mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_FULL);
		// 			} catch (Exception e1) {
		// 				e1.printStackTrace();
		// 			}
					
		// 			return success("Xác thực OTP thành công");
		// 		} else {
		// 			return error("Có lỗi xảy ra trong quá trình xác thực!");
		// 		}
		// 	}
		// }
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			ProcessOrder processOrder = shipmentDetailService.makeOrderSendContFull(shipmentDetails, shipment, getGroup().getCreditFlag());
			if (processOrder != null) {
				processOrderService.insertProcessOrder(processOrder);
				//
				ServiceRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
				try {
					mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_FULL);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				return success("Xác thực OTP thành công");
			} else {
				return error("Có lỗi xảy ra trong quá trình xác thực!");
			}
		}
		return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
	}

	@GetMapping("paymentForm/{shipmentDetailIds}")
	public String paymentForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return PREFIX + "/paymentForm";
	}

	@GetMapping("/napasPaymentForm")
	public String napasPaymentForm() {
		return PREFIX + "/napasPaymentForm";
	}

	@PostMapping("/payment")
	@ResponseBody
	public AjaxResult payment(String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(3);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}
	
	@GetMapping("pickTruckForm/{shipmentId}")
	public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, ModelMap mmap) {
//		mmap.put("shipmentId", shipmentId);
//		ShipmentDetail shipmentDetail = new ShipmentDetail();
//		shipmentDetail.setShipmentId(shipmentId);
//		shipmentDetail.setLogisticGroupId(getUser().getGroupId());
//		String transportId = "";
//		String shipmentIds = "";
//		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
//		for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
//			if (shipmentDetail2.getTransportIds() != null && transportId.length() == 0) {
//				transportId = shipmentDetail2.getTransportIds();
//			}
//			shipmentIds += shipmentDetail2.getId() + ",";
//		}
//		mmap.put("transportIds", transportId);
//		mmap.put("shipmentDetailIds", shipmentIds);
		return PREFIX + "/pickTruckForm";
	}

	@PostMapping("/pickTruck")
	@ResponseBody
	public AjaxResult pickTruck(String shipmentDetailIds, String driverIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				//shipmentDetail.setTransportIds(driverIds);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Điều xe thành công");
		}
		return error("Xảy ra lỗi trong quá trình điều xe.");
	}
	@GetMapping("/getVesselCodeList")
	@ResponseBody
	public List<String> getVesselCodeList(){
		String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class);
		List<String> listVessel =(List<String>) r.get("data");
		return listVessel;
	}
	
	@GetMapping("/getConsigneeList")
	@ResponseBody
	public List<String> getConsigneeList(){
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class);
		List<String> listVessel =(List<String>) r.get("data");
		return listVessel;
	}
	
	@GetMapping("/getPODList")
	@ResponseBody
	public List<String> getPODList(){
		String url = Global.getApiUrl() + "/shipmentDetail/getPODList";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class);
		List<String> listPOD =(List<String>) r.get("data");
		return listPOD;
	}
	
	@GetMapping("/getVoyageNoList")
	@ResponseBody
	public List<String> getVoyageNoList(String vesselCode){
		String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList?vesselCode={q}";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.getForObject(url, R.class, vesselCode);
		List<String> listVoyageNo =(List<String>) r.get("data");
		return listVoyageNo;
	}
	
	@GetMapping("/getYear")
	@ResponseBody
	public String getYear(String vesselCode, String voyageNo){
		String url = Global.getApiUrl() + "/shipmentDetail/getYear/"+vesselCode+"/"+voyageNo;
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> vars = new HashMap<>();
		vars.put("vesselCode", vesselCode);
		vars.put("voyageNo", voyageNo);
		R r = restTemplate.getForObject(url, R.class, vars);
		String year =(String) r.get("data");
		return year;
	}
	
	@GetMapping("/getBeforeAfterDeparture")
	@ResponseBody
	public String getBeforeAfterDeparture(String vesselCode, String voyageNo){
		String url = Global.getApiUrl() + "/shipmentDetail/getBeforeAfterDeparture/"+vesselCode+"/"+voyageNo;
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> vars = new HashMap<>();
		vars.put("vesselCode", vesselCode);
		vars.put("voyageNo", voyageNo);
		R r = restTemplate.getForObject(url, R.class, vars);
		String beforeAfter =(String) r.get("data");
		return beforeAfter;
	}
}