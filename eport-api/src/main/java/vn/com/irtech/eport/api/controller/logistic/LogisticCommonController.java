package vn.com.irtech.eport.api.controller.logistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.form.ShipmentForm;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.logistic.service.INapasApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.dto.NotificationRes;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;

@RestController
@RequestMapping("/logistic")
public class LogisticCommonController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticCommonController.class);

	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired
	private ILogisticAccountService logisticAccountService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private INapasApiService napasApiService;
	
	@Autowired
	private ISysConfigService configService;
	
	@Autowired
	private ICatosApiService catosApiService;
	
	@Autowired 
	private IOtpCodeService otpCodeService;
	
	@Autowired
	private ISysNotificationReceiverService sysNotificationReceiverService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	class ProcessIdComparator implements Comparator<ProcessBill> {
        public int compare(ProcessBill processBill1, ProcessBill processBill2) {
            return processBill1.getProcessOrderId().compareTo(processBill2.getProcessOrderId());
        }
    }
	
	/**
	 * Get shipment list by service type
	 * 
	 * @param serviceType
	 * @return List<ShipmentForm>
	 */
	@GetMapping("/serviceType/{serviceType}/shipments")
	public List<ShipmentForm> getShipmentList(@PathVariable("serviceType") Integer serviceType) {
		Shipment shipmentParam = new Shipment();
		shipmentParam.setServiceType(serviceType);
		shipmentParam.setLogisticGroupId(getGroupLogisticId());
		startPage();
		List<Shipment> shipments = shipmentService.selectShipmentList(shipmentParam);
		List<ShipmentForm> shipmentForms = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shipments)) {
			for (Shipment shipment : shipments) {
				ShipmentForm shipmentForm = new ShipmentForm();
				BeanUtils.copyBeanProp(shipmentForm, shipment);
				shipmentForms.add(shipmentForm);
			}
		}
		return shipmentForms;
	}
	
	/**
	 * Get company information by tax code
	 * 
	 * @param taxCode
	 * @return AjaxResult
	 */
	@GetMapping("/taxCode/{taxCode}/company")
	public AjaxResult getCompanyInfor(@PathVariable("taxCode") String taxCode) {
		AjaxResult ajaxResult = AjaxResult.success();
		PartnerInfoDto partner = catosApiService.getGroupNameByTaxCode(taxCode);
		String groupName = partner.getGroupName();
		String address = partner.getAddress();
		if (address != null) {
			ajaxResult.put("address", address);
		}
		if (groupName != null) {
			ajaxResult.put("companyName", groupName);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
	
	@GetMapping("/shipment/{shipmentId}/otp")
	@ResponseBody
	public AjaxResult sendOTP(@PathVariable("shipmentId") Long shipmentId) {
		OtpCode otpCode = new OtpCode();
		Random rd = new Random();
		long rD = rd.nextInt(900000)+100000;
		String tDCode = Long.toString(rD);
		
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setProcessStatus("N");
		shipmentDetailParam.setUserVerifyStatus("N");
		shipmentDetailParam.setCustomStatus("R");
		shipmentDetailParam.setShipmentId(shipmentId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		String shipmentDetailIds = "";
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds += shipmentDetail.getId() + ",";
		}
		shipmentDetailIds.substring(0, shipmentDetailIds.length()-1);
		
		otpCodeService.deleteOtpCodeByShipmentDetailIds(shipmentDetailIds);

		otpCode.setTransactionId(shipmentDetailIds);
		otpCode.setPhoneNumber(getUser().getMobile());
		otpCode.setOtpCode(tDCode);
		otpCode.setOtpType("1");

		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, +5);
		otpCode.setExpiredTime(cal.getTime());
		otpCodeService.insertSysOtp(otpCode);
		// FIXME Get message template from SysConfigService, using String.format to replace place holders
		String content = configService.selectConfigByKey("otp.format");
		content = content.replace("{shipmentId}", shipmentId.toString()).replace("{otp}", tDCode);
		String response = "";
		 try {
		 	response = otpCodeService.postOtpMessage(getUser().getMobile(), content);
		 	logger.debug("OTP Send Response: " + response);
		 } catch (IOException ex) {
		 	// process the exception
			 logger.error(ex.getMessage());
		 }
		return AjaxResult.success();
	}

	
	@GetMapping("/shipment/{shipmentId}/payment")
	public AjaxResult paymentForm(@PathVariable Long shipmentId) {
		LogisticAccount logisticAccount = logisticAccountService.selectLogisticAccountById(SecurityUtils.getCurrentUser().getUser().getUserId());
		Shipment shipmentParam = new Shipment();
		shipmentParam.setId(shipmentId);
		shipmentParam.setLogisticGroupId(logisticAccount.getGroupId());
		List<Shipment> shipments = shipmentService.selectShipmentList(shipmentParam);
		if (CollectionUtils.isEmpty(shipments)) {
			// Add messeage text
			return error();
		}
		
		ProcessBill processBillParam = new ProcessBill();
		processBillParam.setShipmentId(shipmentId);
		processBillParam.setLogisticGroupId(logisticAccount.getGroupId());
		processBillParam.setPaymentStatus("N");
		List<ProcessBill> processBills = processBillService.getBillListByShipmentId(shipmentId);
		if (CollectionUtils.isEmpty(processBills)) {
			// Shipment Doesn't have any bill need to pay
			return error();
		}
		
		// Sort processBills by processOrderId
		Collections.sort(processBills, new ProcessIdComparator());
		
		// count Total
		Long total = 0L;	
		
		// Create order id for transaction
		String orderId = "Order-" + processBills.get(0).getProcessOrderId();
		Long currentProcessId = processBills.get(0).getProcessOrderId();
				
		
		for (ProcessBill processBill : processBills) {
			total += processBill.getVatAfterFee();
			if (!currentProcessId.equals(processBill.getProcessOrderId())) {
				currentProcessId = processBill.getProcessOrderId();
				orderId += "-" + currentProcessId;
			}
		}
		orderId += "-" + DateUtils.dateTimeNow();
		
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("resultUrl", configService.selectConfigByKey("napas.payment.mobile.result"));
		ajaxResult.put("referenceOrder", "Thanh toan " + orderId);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String clientIp = request.getRemoteAddr();
		ajaxResult.put("clientIp", clientIp);
		ajaxResult.put("data", napasApiService.getDataKey(clientIp, "deviceId", orderId, total, napasApiService.getAccessToken()));
		
		return ajaxResult;
	}

	/**
	 * Get list notification
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/notify")
	@ResponseBody
	public AjaxResult getNotifyList() {
		startPage();
		SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
		sysNotificationReceiver.setUserId(SecurityUtils.getCurrentUser().getUser().getUserId());
		sysNotificationReceiver.setUserType(BusinessConsts.LOGISTIC_USER_TYPE);
		List<NotificationRes> notificationReses = sysNotificationReceiverService
				.getNotificationList(sysNotificationReceiver);
		if (CollectionUtils.isNotEmpty(notificationReses)) {
			for (NotificationRes notificationRes : notificationReses) {
				SysNotificationReceiver sysNotificationRecei = new SysNotificationReceiver();
				sysNotificationRecei.setId(notificationRes.getId());
				sysNotificationRecei.setSeenFlg(true);
				sysNotificationReceiverService.updateSysNotificationReceiver(sysNotificationRecei);
			}
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("notificationList", getDataTable(notificationReses));
		return ajaxResult;
	}
}
