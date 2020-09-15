package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.form.ContainerServiceForm;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/send-cont-full")
public class LogisticSendContFullController extends LogisticBaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticSendContFullController.class);
    
    private final String PREFIX = "logistic/sendContFull";
	
	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private CustomQueueService customQueueService;

	@Autowired
	private ICatosApiService catosApiService;
	
    @GetMapping()
	public String sendContFull() {
		return PREFIX + "/index";
	}

	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		mmap.put("taxCode", getGroup().getMst());
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			oprCodeList = catosApiService.getOprCodeList();
			oprCodeList.add(0, "Chọn OPR");
			CacheUtils.put("oprCodeList", oprCodeList);
		}
		
		mmap.put("oprCodeList", oprCodeList);
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
			mmap.put("taxCode", getGroup().getMst());
		}
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			oprCodeList = catosApiService.getOprCodeList();
			oprCodeList.add(0, "Chọn OPR");
			CacheUtils.put("oprCodeList", oprCodeList);
		}
		mmap.put("oprCodeList", oprCodeList);
        return PREFIX + "/edit";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{shipmentId}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("shipmentId") Long shipmentId,
			@PathVariable("creditFlag") boolean creditFlag, @PathVariable("taxCode") String taxCode, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getUser().getMobile());
		mmap.put("shipmentId", "-");
		mmap.put("creditFlag", creditFlag);
		mmap.put("taxCode", taxCode);
		mmap.put("shipmentId", shipmentId);
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

	@GetMapping("/custom-status/{shipmentDetailIds}")
	public String checkCustomStatus(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
				mmap.put("contList", shipmentDetails);
			}
		}
		return PREFIX + "/checkCustomStatus";
	}
	
	@GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
	public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId, @PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {
		mmap.put("containerNo", containerNo);
		mmap.put("sztp", sztp);
		mmap.put("shipmentDetailId", shipmentDetailId);
		return PREFIX + "/detail";
	}

	@Log(title = "Tạo Lô Hạ Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
    @ResponseBody
    public AjaxResult addShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		if (StringUtils.isNotEmpty(shipment.getBookingNo())) {
			shipment.setBookingNo(shipment.getBookingNo().toUpperCase());
		}
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(EportConstants.SERVICE_DROP_FULL);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Có lỗi khi thực hiện thêm lô, vui lòng thử lại");
	}

	@PostMapping("/unique/booking-no")
	@ResponseBody
	public AjaxResult checkBookingNoUnique(@RequestBody ContainerServiceForm inputForm) {
		String bookingNo = inputForm.getBookingNo();
		if(StringUtils.isAllBlank(bookingNo)) {
			return error("Hãy nhập số Booking");
		}
		Shipment shipment = new Shipment();
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBookingNo(bookingNo);
		shipment.setServiceType(EportConstants.SERVICE_DROP_FULL);
//		if(catosApiService.checkBookingNoForSendFReceiveE(bookingNo, "F").intValue() == 0) {
//			return error("Booking No này chưa có trong hệ thống. Vui lòng liên hệ OM để tạo !");
//		}
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error(String.format("Số Booking '%s' đã tồn tại!", bookingNo));
	}
	
	@PostMapping("/shipment/{shipmentId}")
    @ResponseBody
    public AjaxResult editShipment(Shipment input, @PathVariable Long shipmentId) {
		LogisticAccount user = getUser();
		Shipment shipment = shipmentService.selectShipmentById(input.getId());
		if (verifyPermission(shipment.getLogisticGroupId())) {
			// Chi update cac item cho phep
			// Kiem tra co update so luong co giam -> kiem tra xem so luong details 
			if(input.getContainerAmount() < shipment.getContainerAmount()) {
				ShipmentDetail shipmentSearch = new ShipmentDetail();
				shipmentSearch.setShipmentId(shipmentId);
				long contNumber = shipmentDetailService.countShipmentDetailList(shipmentSearch);
				if(contNumber > input.getContainerAmount()) {
					return error("Không thể chỉnh sửa số lượng container nhỏ hơn danh sách khai báo.");
				}
			}
			shipment.setContainerAmount(input.getContainerAmount());
			shipment.setRemark(input.getRemark());
			shipment.setUpdateBy(user.getFullName());
			// Update OPR, Booking when status is 1 or 2
			if(shipment.getStatus().equals(EportConstants.SHIPMENT_STATUS_INIT)) {
				shipment.setOpeCode(input.getOpeCode());
				if (StringUtils.isNotEmpty(input.getBookingNo())) {
					shipment.setBookingNo(input.getBookingNo().toUpperCase());
				}
			} else if (shipment.getStatus().equals(EportConstants.SHIPMENT_STATUS_SAVE)) {
				shipment.setOpeCode(input.getOpeCode());
			}
			
			if (shipmentService.updateShipment(shipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailListForSendFReceiveE(shipmentDetail);
			if (shipmentDetails != null) {
				ajaxResult.put("shipmentDetails", shipmentDetails);
			} else {
				ajaxResult = AjaxResult.error("Không tìm thấy thông tin lô hàng, vui lòng kiểm tra lại.");
			}
		} else {
			ajaxResult = AjaxResult.error("Lô không tồn tại, vui lòng kiểm tra lại.");
		}
		
		return ajaxResult;
	}

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails, @PathVariable("shipmentId") Long shipmentId) {
		// Verify shipment
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (!verifyPermission(shipment.getLogisticGroupId())) {
			return AjaxResult.error("Không tìm thấy lô, xin vui lòng kiểm tra lại");
		}
		// Check if list have one or more items
		if (shipmentDetails != null && shipmentDetails.size() > 0) {
			LogisticAccount user = getUser();
			boolean updateShipment = true;
			// Lay danh sach container da lam lenh booking
			List<String> contReservedList = shipmentDetailService.checkContainerReserved(shipmentDetails.get(0).getProcessStatus());
			if (contReservedList.size() > 0) {
				// Thong bao loi khong the khai bao cho cac conts nay
				AjaxResult ajaxResult = AjaxResult.error();
				ajaxResult.put("conts", contReservedList);
				return ajaxResult;
			}
			// Danh sachs shipment details co 2 truong hop: co container da tao tu truoc (id != null) va container vua tao (id ==null)
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				// ? Vi sao update process status = null ??
				shipmentDetail.setProcessStatus(null);
				// Giu lai cac thong tin khong the overwrite kih update
				shipmentDetail.setShipmentId(shipmentId);
				shipmentDetail.setLogisticGroupId(user.getGroupId());
				shipmentDetail.setServiceType(EportConstants.SERVICE_DROP_FULL);
				// T/H container da ton tai
				if (shipmentDetail.getId() != null) {
					updateShipment = false;
					shipmentDetail.setUpdateBy(user.getFullName());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				} else {
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("F");
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				}
			}
			if (updateShipment) {
				shipment.setUpdateBy(getUser().getFullName());
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_SAVE);
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@Log(title = "Xóa Khai Báo Cont", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail/delete")
	@Transactional
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable("shipmentId") Long shipmentId, @RequestBody ContainerServiceForm inputForm) {
		// check shipment permission
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (!verifyPermission(shipment.getLogisticGroupId())) {
			return AjaxResult.error("Không tìm thấy lô, xin vui lòng kiểm tra lại");
		}
		String shipmentDetailIds = inputForm.getIds();
		// delete shipment details
		if (shipmentDetailIds != null) {
			// just delete shipmentIds for shipment has been verified before
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds);
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			// Reset status shipment to INIT if deleted all shipment details
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
				shipmentService.updateShipment(shipment);
			}
			return success("Xóa khai báo thành công");
		}
		return error("Xóa khai báo thất bại");
	}

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, String taxCode, boolean creditFlag) {
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
			return error("Mã OTP không chính xác hoặc đã hết hạn, xin vui lòng kiểm tra lại.");
		}
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!EportConstants.SHIPMENT_STATUS_PROCESSING.equals(shipment.getStatus())) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			ProcessOrder processOrder = shipmentDetailService.makeOrderSendCont(shipmentDetails, shipment, taxCode, creditFlag);
			if (processOrder != null) {
				ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
				try {
					if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_FULL)) {
						ajaxResult = AjaxResult.warn("Yêu cầu đang được xử lý, xin vui lòng đợi trong giây lát.");
						ajaxResult.put("processId", processOrder.getId());
						return ajaxResult;
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}
				
				ajaxResult =  AjaxResult.success("Yêu cầu đang được xử lý, xin vui lòng đợi trong giây lát.");
				ajaxResult.put("processId", processOrder.getId());
				return ajaxResult;
			}
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	@Log(title = "Thanh Toán Hạ Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/payment/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult payment(@PathVariable String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(3); // TODO Chuyen thanh dung EportConstants
				shipmentDetail.setPaymentStatus("Y");
				if ("VN".equals(shipmentDetail.getDischargePort().substring(0,2))) {
					shipmentDetail.setStatus(4);
					shipmentDetail.setCustomStatus("Y");
				}
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

	@Log(title = "Khai Báo Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList, @RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		// FIXME chuyen params thanh POST REQUEST BODY
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declare no list to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetail.setCustomScanTime(new Date());
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					customQueueService.offerShipmentDetail(shipmentDetail);
				}
				return success();
			}
		}
		return error();
	}
	
	@GetMapping("/berthplan/ope-code/list")
	@ResponseBody
	public AjaxResult getOpeCodeList() {
		AjaxResult ajaxResult = success();
		List<String> opeCodeList = catosApiService.selectOpeCodeListInBerthPlan();
		if(opeCodeList.size() > 0 ) {
			ajaxResult.put("opeCodeList", opeCodeList);
			return ajaxResult;
		}
		return error();
		
	}
	
	@GetMapping("/berthplan/ope-code/{opeCode}/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageList(@PathVariable String opeCode) {
		AjaxResult ajaxResult = success();
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlan(opeCode);
		if(berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for(ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			ajaxResult.put("berthplanList", berthplanList);
			ajaxResult.put("vesselAndVoyages", vesselAndVoyages);
			return ajaxResult;
		}
		return error();
	}
	
	@GetMapping("/berthplan/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageListWithoutOpeCode() {
		AjaxResult ajaxResult = success();
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlanWithoutOpe();
		if(berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for(ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			ajaxResult.put("berthplanList", berthplanList);
			ajaxResult.put("vesselAndVoyages", vesselAndVoyages);
			return ajaxResult;
		}
		return error();
	}
	
	@GetMapping("/containerNo/{containerNo}/sztp")
	@ResponseBody
	public AjaxResult getSztpByContainerNo(@PathVariable("containerNo") String containerNo) {
		String sztp = catosApiService.getSztpByContainerNo(containerNo);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("sztp", sztp);
		return ajaxResult;
	}
}