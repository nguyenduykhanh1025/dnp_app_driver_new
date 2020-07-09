package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic")
public class LogisticCommonController extends LogisticBaseController {
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired 
	private IOtpCodeService otpCodeService;
	
	@GetMapping("/company/{taxCode}")
	@ResponseBody
	public AjaxResult getGroupNameByTaxCode(@PathVariable String taxCode) throws Exception {
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
	
	@GetMapping("/shipments/{serviceType}")
	@ResponseBody
	public TableDataInfo listShipment(@PathVariable int serviceType) {
		startPage();
		LogisticAccount user = getUser();
		Shipment shipment = new Shipment();
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setServiceType(serviceType);
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
		//  try {
		//  	response = otpCodeService.postOtpMessage(lGroup.getMobilePhone(),content);
		//  	System.out.println(response);
		//  } catch (IOException ex) {
		//  	// process the exception
		//  }
		return AjaxResult.success("response.toString()");
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/source/option")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		List<String> listPOD = (List<String>) CacheUtils.get("dischargePortList");
		if (listPOD == null) {
			listPOD = shipmentDetailService.getPODList();
			CacheUtils.put("dischargePortList", listPOD);
		}
		ajaxResult.put("dischargePortList", listPOD);
		
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
}
