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
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
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
	
	@Autowired
	private IProcessOrderService processOrderService;
	
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
			vslNm = shipmentDetails.get(0).getVslNm();
			voyNo = shipmentDetails.get(0).getVoyNo();
			bookingNo = shipmentDetails.get(0).getBookingNo();
			vslName = shipmentDetails.get(0).getVslName();
		}
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (!vslNm.equals(shipmentDetail.getVslNm()) || !voyNo.equals(shipmentDetail.getVoyNo())) {
				return "Tàu/chuyến khônng được khác nhau!";
			}
		}
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlanWithoutOpe();
		if(CollectionUtils.isNotEmpty(berthplanList)) {
			List<String> vesselAndVoyages = new ArrayList<>();
			for(ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			mmap.put("berthplanList", berthplanList);
			vesselAndVoyages.add(0, "Chọn tàu/chuyến mới");
			mmap.put("vesselAndVoyages", vesselAndVoyages);
			
		}
		mmap.put("vessel", vslNm + " - " + vslName + " - " + voyNo);
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
	@GetMapping("/otp/shipment-detail-ids/{shipmentDetailIds}/vslNm/{vslNm}/{voyNo}/{vslName}/{voyCarrier}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("vslNm") String vslNm, @PathVariable("voyNo") String voyNo, @PathVariable("vslName") String vslName, @PathVariable("voyCarrier") String voyCarrier, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("vslNm", vslNm);
		mmap.put("voyNo", voyNo);
		mmap.put("vslName", vslName);
		mmap.put("voyCarrier", voyCarrier);
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailListForSendFReceiveE(shipmentDetail);
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
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, String vslNm, String voyNo, String vslName, String voyCarrier) {
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

		// Make order send to robot
		ServiceSendFullRobotReq serviceRobotReq = shipmentDetailService.makeChangeVesselOrder(shipmentDetails, vslNm, voyNo, vslName, voyCarrier, getUser().getGroupId());
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
	
	@GetMapping("/process-order/{processOrderId}/containers/failed")
	@ResponseBody
	public AjaxResult getListContainerFailed(@PathVariable Long processOrderId) {
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId.toString());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			String containers = "";
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (!shipmentDetail.getVoyNo().equals(processOrder.getVoyage())) {
					containers += shipmentDetail.getContainerNo() + ",";
				}
			}
			containers.substring(0, containers.length()-1);
			return success("Yêu cầu đổi tàu thực hiện đổi tàu của quý khách bị lỗi ở các container " + containers + ". Quý khách vui lòng thử lại hoặc liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
		}
		return error("Không tìm thấy dữ liệu.");
	}
}
