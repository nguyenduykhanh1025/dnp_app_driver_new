package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/vessel-changing")
public class LogisticChangeVesselController extends LogisticBaseController {

	private final String PREFIX = "logistic/vesselChanging";
	
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
	
	/**
	 * Get main view for change vessel
	 * 
	 * @return
	 */
	@GetMapping()
	public String getVesselChangingView() {
		return PREFIX + "/index";
	}

	/**
	 * Get view for input new vessel to change
	 * 
	 * @param shipmentDetailIds
	 * @param mmap
	 * @return
	 */
	@GetMapping("/shipment-detail-ids/{shipmentDetailIds}/form")
	public String getVesselChangingForm(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		String opeCode = "" , vslNm = "", voyNo = "", carrierName = "", bookingNo = "", vslName = "";
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			opeCode = shipmentDetails.get(0).getOpeCode();
			vslNm = shipmentDetails.get(0).getVslNm();
			voyNo = shipmentDetails.get(0).getVoyNo();
			carrierName = shipmentDetails.get(0).getCarrierName();
			bookingNo = shipmentDetails.get(0).getBookingNo();
			vslName = shipmentDetails.get(0).getVslName();
		}
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (!opeCode.equals(shipmentDetail.getOpeCode()) || !vslNm.equals(shipmentDetail.getVslNm()) || !voyNo.equals(shipmentDetail.getVoyNo())) {
				return "Tàu/chuyến khônng được khác nhau!";
			}
		}
		List<String> opeCodeList = catosApiService.selectOpeCodeListInBerthPlan();
		if (CollectionUtils.isNotEmpty(opeCodeList)) {
			opeCodeList.add(0, "Chọn hãng tàu");
		}
		mmap.put("opeCode", opeCode + ": " + carrierName);
		mmap.put("vessel", vslNm + " - " + vslName + " - " + voyNo);
		mmap.put("opeCodeList", opeCodeList);
		mmap.put("bookingNo", bookingNo);
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return PREFIX + "/changingForm";	
	}

	/**
	 * Receive new vessel and get view to verify otp
	 * 
	 * @param shipmentDetailIds
	 * @param vessel
	 * @param mmap
	 * @return
	 */
	@GetMapping("/otp/shipment-detail-ids/{shipmentDetailIds}/vessel/{vessel}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("vessel") String vessel , ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("vessel", vessel);
		mmap.put("numberPhone", getGroup().getMobilePhone());
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
		shipment.setServiceType(4);
		shipment.setStatus("3");
		shipment.setLogisticGroupId(user.getGroupId());
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
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
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, String vessel) {
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			return error("Không tìm thấy danh sách container cần đổi tàu, quý khách vui lòng thử lại sau.");
		}

		// Check vessel exist
		if (vessel == null || vessel.length() == 0) {
			return error("Quý khách chưa chọn tàu/chuyến.");
		}

		String[] vesselArr = vessel.split(" - ");
		if (vesselArr.length < 3) {
			return error("Thông tin tàu/chuyến không hợp lệ.");
		}

		String vslNm = vesselArr[0];
		String voyAge = vesselArr[2];

		// Make order send to robot
		ServiceSendFullRobotReq serviceRobotReq = shipmentDetailService.makeChangeVesselOrder(shipmentDetails, vslNm, voyAge, getUser().getGroupId());
		if (serviceRobotReq == null) {
			return error("Có lỗi xảy ra trong quá trình tạo lệnh để thực thi!");
		}
		AjaxResult ajaxResult = null;
		try {
			if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.CHANGE_VESSEL)) {
				ajaxResult = AjaxResult.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processId", serviceRobotReq.processOrder.getId());
				return ajaxResult;
			}
		} catch (Exception e) {
			return error("Có lỗi xảy ra trong quá trình xác thực!");
		}
		
		ajaxResult =  AjaxResult.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
		ajaxResult.put("processId", serviceRobotReq.processOrder.getId());
		return ajaxResult;
	}

	@GetMapping("/berthplan/ope-code/{opeCode}/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageList(@PathVariable String opeCode) {
		AjaxResult ajaxResult = success();
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlan(opeCode);
		if(CollectionUtils.isNotEmpty(berthplanList)) {
			List<String> vesselAndVoyages = new ArrayList<>();
			for(ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			ajaxResult.put("vesselAndVoyages", vesselAndVoyages);
			return ajaxResult;
		}
		return AjaxResult.warn("Không tìm thấy tàu/chuyến nào cho hãng tàu này.");
	}
}
