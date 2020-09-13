package vn.com.irtech.eport.logistic.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/order/extension")
public class LogisticExtensionOrderController extends LogisticBaseController {

	private final String PREFIX = "logistic/extension";
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticExtensionOrderController.class);
	
	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;
	
	@Autowired
	private MqttService mqttService;
	
	/**
	 * Get main view for extend expired dem
	 * 
	 * @return
	 */
	@GetMapping()
	public String getExtensionView() {
		return PREFIX + "/index";
	}

	/**
	 * Get view for input new expired dem to change
	 * 
	 * @param shipmentDetailIds
	 * @param mmap
	 * @return
	 */
	@GetMapping("/shipment-detail-ids/{shipmentDetailIds}/form")
	public String getExtensionForm(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			mmap.put("expiredDem", format.format(shipmentDetails.get(0).getExpiredDem()));
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return PREFIX + "/extensionForm";	
	}

	/**
	 * Receive new expired dem and get view to verify otp
	 * 
	 * @param shipmentDetailIds
	 * @param vessel
	 * @param mmap
	 * @return
	 */
	@GetMapping("/otp/shipment-detail-ids/{shipmentDetailIds}/expiredDem/{expiredDem}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("expiredDem") String expiredDem , ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("expiredDem", expiredDem);
		mmap.put("numberPhone", getUser().getMobile());
		return PREFIX + "/otp";
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
		shipment.setEdoFlg("0");
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
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, Long expiredDem) {
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

		// Check shipment detail ids can be change expired dem
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			return error("Không tìm thấy danh sách container cần đổi tàu, quý khách vui lòng thử lại sau.");
		}

		// Check valid date
		if (expiredDem == null) {
			return error("Quý khách chưa nhập hạn lệnh.");
		}
		Date expiredDemDate = new Date(expiredDem);
		
		// Make order send to robot
		List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeExtensionDateOrder(shipmentDetails, expiredDemDate, getUser().getGroupId());
		if (CollectionUtils.isEmpty(serviceRobotReqs)) {
			return error("Có lỗi xảy ra trong quá trình chuẩn bị dữ liệu làm lệnh.");
		}
		AjaxResult ajaxResult = null;
		List<Long> processIds = new ArrayList<>();
		boolean robotBusy = false;
		try {
			for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
				processIds.add(serviceRobotReq.processOrder.getId());
				if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.EXTENSION_DATE)) {
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
