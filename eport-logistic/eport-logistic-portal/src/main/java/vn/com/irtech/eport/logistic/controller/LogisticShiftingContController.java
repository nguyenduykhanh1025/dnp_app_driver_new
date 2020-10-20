package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Controller
@RequiresPermissions("logistic:order")
@RequestMapping("/logistic/shifting-cont")
public class LogisticShiftingContController extends LogisticBaseController {

	private final String PREFIX = "logistic/shiftingCont";

	private static final Logger logger = LoggerFactory.getLogger(LogisticShiftingContController.class);

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IProcessBillService processBillService;
	
	/**
	 * Get main view for shifting container
	 * 
	 * @return
	 */
	@GetMapping()
	public String getShiftingContView() {
		return PREFIX + "/index";
	}

	/**
	 * Get yard position for bl
	 * 
	 * @param blNo
	 * @param mmap
	 * @return
	 */
	@GetMapping("/cont-list/yard-position/{blNo}")
	public String pickContOnDemand(@PathVariable("blNo") String blNo, ModelMap mmap) {
		ShipmentDetail shipmentDt = new ShipmentDetail();
		shipmentDt.setBlNo(blNo);
		shipmentDt.setFe("F");
		shipmentDt.setLogisticGroupId(getUser().getGroupId());
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);

		//Get coordinate from catos
		List<ShipmentDetail> coordinateOfList = catosApiService.getCoordinateOfContainers(blNo);
		List<ShipmentDetail[][]> bayList = null;
		try {
			bayList = shipmentDetailService.getContPosition(blNo, shipmentDetails);
		} catch (Exception e) {
			logger.warn("Can't get container yard position!");
		}
		if (shipmentDetails.size() > 0) {
			mmap.put("bayList", bayList);
		}
		mmap.put("isCredit", "1".equals(getGroup().getCreditFlag()));
		return PREFIX + "/shiftingCont";
	}
	
	@GetMapping("/otp/shipment-detail-ids/{shipmentDetailIds}/credit/{isCredit}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds,@PathVariable("isCredit") String isCredit ,ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("isCredit", isCredit);
		mmap.put("numberPhone", getUser().getMobile());
		return PREFIX + "/otp";
	}

	@GetMapping("/shipment/{shipmentId}/payment/shifting")
	public String paymentShifting(@PathVariable Long shipmentId, ModelMap mmap) {
		mmap.put("billList", processBillService.getBillShiftingContByShipmentId(shipmentId, getUser().getGroupId()));
		return PREFIX + "/paymentShiftingForm";
	}

	/**
	 * Get list shipment that had been made order but not finish yet
	 * 
	 * @param param
	 * @return
	 */
	@PostMapping("/shipments")
	@ResponseBody
	public TableDataInfo getShipmentList(@RequestBody PageAble<Shipment> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		LogisticAccount user = getUser();
		Shipment shipment = param.getData();
		if (shipment == null) {
			shipment = new Shipment();
		}
		shipment.setServiceType(EportConstants.SERVICE_PICKUP_FULL);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
		shipment.setLogisticGroupId(user.getGroupId());
		List<Shipment> shipments = shipmentService.selectShipmentListForExtensionDate(shipment);
		return getDataTable(shipments);
	}

	/**
	 * Get list shipment detail with process order Y but finish status N by shipment id
	 * 
	 * @param shipmentId
	 * @return
	 */
	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult getShipmentDetailList(@PathVariable Long shipmentId) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setLogisticGroupId(getUser().getGroupId());
		shipmentDetail.setProcessStatus("Y");
		shipmentDetail.setFinishStatus("N");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailList(shipmentDetail);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	/**
	 * Verify otp and make order send to robot
	 * 
	 * @param otp
	 * @param shipmentDetailIds
	 * @return
	 */
	@Log(title = "Bốc Chỉ Định", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, Boolean isCredit) {
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

		// Verify otp
		if (otpCodeService.verifyOtpCodeAvailable(otpCode) != 1) {
			return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
		}

		// Check shipment detail ids can be change vessel
		List<ShipmentDetail> preorderPickupConts = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isEmpty(preorderPickupConts)) {
			return error("Không tìm thấy danh sách container cần bốc chỉ định, quý khách vui lòng thử lại sau.");
		}
		
		// Check if logistic can pay by credit
		if (getGroup().getCreditFlag() == "0" && isCredit) {
			return error("Qúy khách không có quyền thanh toán trả sau!");
		}
		ShipmentDetail shipmentDt = new ShipmentDetail();
		shipmentDt.setBlNo(preorderPickupConts.get(0).getBlNo());
		shipmentDt.setFe("F");
		shipmentDt.setLogisticGroupId(getUser().getGroupId());

		// Check if logistic own preorderPickupConts
		if (preorderPickupConts.size() != shipmentDetailService.countNumberOfLegalCont(preorderPickupConts, getUser().getGroupId())) {
			return error("Bạn không có quyền bốc chỉ định những container này!");
		}

		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);

		Shipment shipment = null;
		if (shipmentDetails.isEmpty()) {
			return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
		} else {
			shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
		}

		//Get coordinate from catos test
		List<ContainerInfoDto> coordinateOfList = catosApiService
				.selectShipmentDetailsByBLNo(preorderPickupConts.get(0).getBlNo());
		AjaxResult ajaxResult = AjaxResult.success();
		List<Long> orderIds = new ArrayList<>();
		List<ServiceSendFullRobotReq> reqs = null;
		if (!shipmentDetails.isEmpty()) {
			reqs = shipmentDetailService.calculateMovingCont(coordinateOfList, preorderPickupConts, shipmentDetails, shipment, isCredit);
			if (reqs == null) {
				return error("Không có container nào cần làm lệnh dịch chuyển!");
			}
			boolean robotBusy = false;
			try {
				for (ServiceSendFullRobotReq robotReq : reqs) {
					orderIds.add(robotReq.processOrder.getId());
					if (!mqttService.publishMessageToRobot(robotReq, EServiceRobot.SHIFTING_CONT)) {
						robotBusy = true;
					}
				}
			} catch (MqttException e) {
				logger.error(e.getMessage());
				return AjaxResult.error("Có sự cố xảy ra trong quá trình xác thức, quý khách vui lòng thử lại sau!");
			}
			if (robotBusy) {
				ajaxResult = AjaxResult.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processIds", orderIds);
				ajaxResult.put("orderNumber", reqs.size());
				return ajaxResult;
			}
		}
		ajaxResult.put("processIds", orderIds);
		ajaxResult.put("orderNumber", reqs.size());
		return ajaxResult;
	}

}
