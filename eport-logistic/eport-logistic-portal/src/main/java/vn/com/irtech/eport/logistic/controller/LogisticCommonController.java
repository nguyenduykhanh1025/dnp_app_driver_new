package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.INapasApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic")
public class LogisticCommonController extends LogisticBaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticCommonController.class);
    
    private final String PREFIX = "logistic";
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired ICatosApiService catosApiService;
	
	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired 
	private IOtpCodeService otpCodeService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired 
	private INapasApiService napasApiService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IPaymentHistoryService paymentHistoryService;
	
	@Autowired
	private DictService dictDataService;

	@GetMapping("/company/{taxCode}")
	@ResponseBody
	public AjaxResult getGroupNameByTaxCode(@PathVariable String taxCode) throws Exception {
		AjaxResult ajaxResult = AjaxResult.success();
		if (taxCode == null || "".equals(taxCode)) {
			return error();
		}
		Shipment shipment = catosApiService.getGroupNameByTaxCode(taxCode);
		String groupName = shipment.getGroupName();
		String address = shipment.getAddress();
		if (address != null) {
			ajaxResult.put("address", address);
		}
		if (groupName != null) {
			ajaxResult.put("groupName", groupName);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}
	
	@PostMapping("/shipments")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody PageAble<Shipment> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		LogisticAccount user = getUser();
		Shipment shipment = param.getData();
		shipment.setLogisticGroupId(user.getGroupId());
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}
	
	@GetMapping("/otp/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult sendOTP(@PathVariable String shipmentDetailIds) {
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
		return AjaxResult.success();
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/source/option")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		List<String> listConsignee = (List<String>) CacheUtils.get("consigneeList");
		if (listConsignee == null) {
			listConsignee = shipmentDetailService.getConsigneeList();
			CacheUtils.put("consigneeList", listConsignee);
		}
		ajaxResult.put("consigneeList", listConsignee);
		
		List<String> listVessel = (List<String>) CacheUtils.get("vslNmList");
		if (listVessel == null) {
			listVessel = shipmentDetailService.getVesselCodeList();
			CacheUtils.put("vslNmList", listVessel);
		}
		ajaxResult.put("vslNmList", listVessel);
		
		List<String> opeCodeList = (List<String>) CacheUtils.get("opeCodeList");
		if (opeCodeList == null) {
			opeCodeList = shipmentDetailService.getOpeCodeList();
			CacheUtils.put("opeCodeList", opeCodeList);
		}
		ajaxResult.put("opeCodeList", opeCodeList);
		
		return ajaxResult;
	}

	@GetMapping("/vessel/{vslNm}/voyages")
	@ResponseBody
	public AjaxResult getVoyages(@PathVariable String vslNm) {
		AjaxResult ajaxResult = success();
		List<String> voyages = shipmentDetailService.getVoyageNoList(vslNm);
		ajaxResult.put("voyages", voyages);
		return ajaxResult;
	}
	
	@GetMapping("/payment/napas/{processOrderIds}")
	public String napasPaymentForm(@PathVariable String processOrderIds, ModelMap mmap) {
		String[] processOrderIdsArr = processOrderIds.split(",");

		// return error when logistic didn't own process order
		if (processOrderIdsArr.length != processOrderService.checkLogisticOwnedProcessOrder(getUser().getGroupId(), processOrderIdsArr)) {
			return "error/unauth";
		}

		String orderId = "Order";
		for (String id : processOrderIdsArr) {
			orderId += "-" + id;
		}
		orderId = orderId + "-" + DateUtils.dateTimeNow();

		// return error when get total bill null
		Long total = processBillService.getSumOfTotalBillList(processOrderIdsArr);
		if (total == null) {
			return "error/500";
		}

		// check if process order is on payment transaction
		PaymentHistory paymentHistoryParam = new PaymentHistory();
		paymentHistoryParam.setProccessOrderIds(processOrderIds);
		paymentHistoryParam.setStatus("1");
		List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
		PaymentHistory paymentHistory;
		if (paymentHistories.isEmpty()) {
			paymentHistory = new PaymentHistory();
			paymentHistory.setUserId(getUserId());
			paymentHistory.setProccessOrderIds(processOrderIds);
			paymentHistory.setShipmentId(processOrderService.getShipmentIdByProcessOrderId(Long.valueOf(processOrderIdsArr[0])));
			paymentHistory.setAmount(total);
			paymentHistory.setStatus("0");
			paymentHistory.setOrderId(orderId);
			paymentHistory.setCreateBy(getUser().getFullName());
			paymentHistoryService.insertPaymentHistory(paymentHistory);
		} else {
			// paymentHistory = paymentHistories.get(0);
			// paymentHistory.setOrderId(orderId);
			// paymentHistoryService.updatePaymentHistory(paymentHistory);
			return "error/unauth";
		}

		mmap.put("resultUrl", configService.getKey("napas.payment.result"));
		mmap.put("referenceOrder", "Thanh toan " + orderId);
		mmap.put("clientIp", getUserIp());
		mmap.put("data", napasApiService.getDataKey(getUserIp(), "deviceId", orderId, total, napasApiService.getAccessToken()));

		return PREFIX + "/napas/napasPaymentForm";
	}

	@RequestMapping(value="/payment/result", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	@Transactional
	public String getPaymentResult(@RequestParam("napasResult") String result, ModelMap mmap) {
		JSONObject json = JSONObject.parseObject(result);
		String dataBase64 = json.getString("data");

		boolean isError = true;
		
		//checksum
		String  checksumString = json.getString("checksum");
		if (checksumString.equalsIgnoreCase(DigestUtils.sha256Hex(dataBase64+configService.getKey("napas.client.secret")))) {
			
			//decode base
			JSONObject decodeData = JSONObject.parseObject(new String(Base64.getDecoder().decode(dataBase64)));
			
			// result (Success or Failed)
			String resultStatus = decodeData.getJSONObject("paymentResult").getString("result");
			
			if ("SUCCESS".equalsIgnoreCase(resultStatus)) {
				// order id
				String orderId = decodeData.getJSONObject("paymentResult").getJSONObject("order").getString("id");
				PaymentHistory paymentHistoryParam = new PaymentHistory();
				paymentHistoryParam.setOrderId(orderId);
				List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
				if (!paymentHistories.isEmpty()) {
					PaymentHistory paymentHistory = paymentHistories.get(0);

					// Update payment history
					if ("1".equals(paymentHistory.getStatus())) {
						paymentHistory.setId(null);
						paymentHistoryService.insertPaymentHistory(paymentHistory);
					} else {
						paymentHistory.setUpdateBy(getUser().getFullName());
						paymentHistory.setStatus("1");
						paymentHistoryService.updatePaymentHistory(paymentHistory);
					}

					// Update shipment detail
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(paymentHistory.getProcessOrderIds());
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDetail.setPaymentStatus("Y");
						shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						if (shipmentDetail.getCustomStatus() != null && "N".equals(shipmentDetail.getCustomStatus()) && 
						shipmentDetail.getDischargePort() != null && shipmentDetail.getDischargePort().length() > 2 && 
						"VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
							shipmentDetail.setCustomStatus("R");
							shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						}
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}

					// Update bill
					processBillService.updateBillListByProcessOrderIds(paymentHistory.getProcessOrderIds());

					isError = false;
				}
			}
		}
		if (isError) {
			mmap.put("result", "ERROR");
		} else {
			mmap.put("result", "SUCCESS");
		}
		return PREFIX + "/napas/resultForm";
	}

	@PostMapping("/pods")
	@ResponseBody
	public AjaxResult getPODs(@RequestBody ShipmentDetail shipmentDetail){
		AjaxResult ajaxResult = success();
		List<String> listPOD = new ArrayList<String>();
		if(shipmentDetail != null){
//			String year = catosApiService.getYearByVslCodeAndVoyNo(shipmentDetail.getVslNm(), shipmentDetail.getVoyNo());
//			if(year != null) {
//				shipmentDetail.setYear(year);
//			}
			listPOD = catosApiService.getPODList(shipmentDetail);
			ajaxResult.put("dischargePorts", listPOD);
			return ajaxResult;
		}
		return error();
	}
	
	@GetMapping("/size/container/list")
	@ResponseBody
	public AjaxResult getSztps()
	{
		return AjaxResult.success(dictDataService.getType("sys_size_container_eport"));
	}

	@GetMapping("/shipment/{shipmentId}/napas")
	public String napasShiftingPaymentForm(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {

		List<ProcessBill> processBills = processBillService.getBillShiftingContByShipmentId(shipmentId, getUser().getGroupId());

		if (processBills.isEmpty()) {
			return "error/500";
		}

		Long total = 0L;
		Long idTemp = processBills.get(0).getProcessOrderId();
		String orderId = "Order-" + idTemp;
		String processOrderIds = idTemp + ",";
		for (ProcessBill processBill : processBills) {
			total += processBill.getVatAfterFee();
			if (!idTemp.equals(processBill.getProcessOrderId())) {
				idTemp = processBill.getProcessOrderId();
				orderId += "-" + idTemp;
				processOrderIds += idTemp + ",";
			}
		}
		orderId = orderId + "-" + DateUtils.dateTimeNow();
		processOrderIds.substring(0, processOrderIds.length()-1);

		// check if process order is on payment transaction
		PaymentHistory paymentHistoryParam = new PaymentHistory();
		paymentHistoryParam.setProccessOrderIds(processOrderIds);
		paymentHistoryParam.setStatus("0");
		List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
		PaymentHistory paymentHistory;
		if (paymentHistories.isEmpty()) {
			paymentHistory = new PaymentHistory();
			paymentHistory.setUserId(getUserId());
			paymentHistory.setProccessOrderIds(processOrderIds);
			paymentHistory.setShipmentId(shipmentId);
			paymentHistory.setAmount(total);
			paymentHistory.setStatus("0");
			paymentHistory.setOrderId(orderId);
			paymentHistory.setCreateBy(getUser().getFullName());
			paymentHistoryService.insertPaymentHistory(paymentHistory);
		} else {
			paymentHistory = paymentHistories.get(0);
			paymentHistory.setOrderId(orderId);
			paymentHistoryService.updatePaymentHistory(paymentHistory);
		}

		mmap.put("resultUrl", configService.getKey("napas.payment.shifting.result"));
		mmap.put("referenceOrder", "Thanh toan " + orderId);
		mmap.put("clientIp", getUserIp());
		mmap.put("data", napasApiService.getDataKey(getUserIp(), "deviceId", orderId, total, napasApiService.getAccessToken()));

		return PREFIX + "/napas/napasPaymentForm";
	}

	@RequestMapping(value="/payment/shifting/result", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	@Transactional
	public String getPaymentShiftingResult(@RequestParam("napasResult") String result, ModelMap mmap) {
		JSONObject json = JSONObject.parseObject(result);
		String dataBase64 = json.getString("data");

		boolean isError = true;
		
		//checksum
		String  checksumString = json.getString("checksum");
		if (checksumString.equalsIgnoreCase(DigestUtils.sha256Hex(dataBase64+configService.getKey("napas.client.secret")))) {
			
			//decode base
			JSONObject decodeData = JSONObject.parseObject(new String(Base64.getDecoder().decode(dataBase64)));
			
			// result (Success or Failed)
			String resultStatus = decodeData.getJSONObject("paymentResult").getString("result");
			
			if ("SUCCESS".equalsIgnoreCase(resultStatus)) {
				// order id
				String orderId = decodeData.getJSONObject("paymentResult").getJSONObject("order").getString("id");
				PaymentHistory paymentHistoryParam = new PaymentHistory();
				paymentHistoryParam.setOrderId(orderId);
				List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
				if (!paymentHistories.isEmpty()) {
					PaymentHistory paymentHistory = paymentHistories.get(0);

					// Update payment history
					if ("1".equals(paymentHistory.getStatus())) {
						paymentHistory.setId(null);
						paymentHistoryService.insertPaymentHistory(paymentHistory);
					} else {
						paymentHistory.setUpdateBy(getUser().getFullName());
						paymentHistory.setStatus("1");
						paymentHistoryService.updatePaymentHistory(paymentHistory);
					}

					// Update shipment detail
					ShipmentDetail shipmentDetailParam = new ShipmentDetail();
					shipmentDetailParam.setShipmentId(paymentHistory.getShipmentId());
					shipmentDetailParam.setPreorderPickup("Y");
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDetail.setPrePickupPaymentStatus("Y");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}

					// Update bill
					processBillService.updateBillListByProcessOrderIds(paymentHistory.getProcessOrderIds());

					isError = false;
				}
			}
		}
		if (isError) {
			mmap.put("result", "ERROR");
		} else {
			mmap.put("result", "SUCCESS");
		}
		return PREFIX + "/napas/resultForm";
	}
	
	@GetMapping("/ope-code/{opeCode}/vessel-code/list")
	@ResponseBody
	public AjaxResult getVesselBerthPlanByOpeCode(@PathVariable String opeCode) {
		AjaxResult ajaxResult = success();
		List<String> vesselList = catosApiService.selectVesselCodeBerthPlan(opeCode);
		if(vesselList.size() > 0) {
			ajaxResult.put("vessels", vesselList);
			return ajaxResult;
		}
		return error();
	}
	
}
