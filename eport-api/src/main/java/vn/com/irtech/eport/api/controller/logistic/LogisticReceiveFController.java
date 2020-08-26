package vn.com.irtech.eport.api.controller.logistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.api.form.ProcessBillForm;
import vn.com.irtech.eport.api.form.ShipmentDetailForm;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.SystemConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.domain.EdoHouseBill;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IEdoHouseBillService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@RestController
@RequestMapping("/logistic/receive-f")
public class LogisticReceiveFController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticReceiveFController.class);
	
	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired 
	private IEdoService edoService;
	
	@Autowired
	private IEdoHouseBillService edoHouseBillService;
	
	@Autowired
	private ICarrierGroupService carrierGroupService;
	
	@Autowired
	private ICatosApiService catosApiService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private ISysConfigService configService;
	
	@Autowired
	private CustomQueueService customQueueService;
	
	class StatusComparator implements Comparator<ShipmentDetailForm> {
        public int compare(ShipmentDetailForm shipmentDetail1, ShipmentDetailForm shipmentDetail2) {
            return shipmentDetail1.getStatus().compareTo(shipmentDetail2.getStatus());
        }
    }
	
	/**
	 * Get information by bill of lading
	 * 
	 * @param blNo
	 * @return AjaxResult
	 */
	@GetMapping("/blNo/{blNo}/info")
	public AjaxResult getInfoShipmentByBlNo(@PathVariable("blNo") String blNo) {
		AjaxResult ajaxResult = new AjaxResult();
		Shipment shipment = new Shipment();
		//check bill unique
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setLogisticGroupId(getGroupLogisticId());
		shipment.setBlNo(blNo);
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}
		//check opeCode
		String opeCode = edoService.getOpeCodeByBlNo(blNo);
		
		// Check edo with master bill
		if(opeCode != null) {
			ajaxResult = AjaxResult.success();
			ajaxResult.put("edoFlg", "1");
			ajaxResult.put("opeCode", opeCode);
			return ajaxResult;
		} else {
			// Check edo with house bill
			EdoHouseBill edoHouseBill = edoHouseBillService.getEdoHouseBillByBlNo(blNo);
			if (edoHouseBill != null) {
				ajaxResult = AjaxResult.success();
				ajaxResult.put("edoFlg", "1");
				ajaxResult.put("opeCode", edoHouseBill.getCarrierCode());
				ajaxResult.put("houseBill", blNo);
				ajaxResult.put("blNo", edoService.getBlNoByHouseBillId(edoHouseBill.getId()));
				return ajaxResult;
			} else {
				// check do
				Shipment shipCatos = shipmentService.getOpeCodeCatosByBlNo(blNo);
				if (shipCatos != null) {
					String edoFlg = carrierGroupService.getDoTypeByOpeCode(shipCatos.getOpeCode());
					if(edoFlg == null){
						return error("Mã hãng tàu:"+ shipCatos.getOpeCode() +" không có trong hệ thống. Vui lòng liên hệ Cảng!");
					}
					ajaxResult = AjaxResult.success();
					ajaxResult.put("edoFlg", edoFlg);
					ajaxResult.put("opeCode", shipCatos.getOpeCode());
					ajaxResult.put("containerAmount", shipCatos.getContainerAmount());
					return ajaxResult;
				}
			}
		} 
		ajaxResult = AjaxResult.error("Số bill không tồn tại!");
		return ajaxResult;
	}
	
	/**
	 * Check order number match with bl no for edo (master bill, house bill)
	 * 
	 * @param shipment
	 * @return AjaxResult
	 */
	@PostMapping("/orderNumber/check")
	public AjaxResult checkOrderNumber(@RequestBody Shipment shipment) {
		int containerAmount = 0;
		// Check for house bill case
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			containerAmount = edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber());
		} else {
			// Check for master bill case
			containerAmount = edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber());
		}
		if (containerAmount != 0) {
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("containerAmount", containerAmount);
			return ajaxResult;
		}
		return error("Mã nhận container không chính xác.");
	}
	
	/**
	 * Add shipment
	 * 
	 * @param shipment
	 * @return AjaxResult
	 */
	@Log(title = "Thêm Lô Bốc Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/shipment")
	public AjaxResult addShipment(@RequestBody Shipment shipment) {
		
		if (StringUtils.isNotEmpty(shipment.getBlNo())) {
			return error("Quý khách chưa nhập số bill.");
		}
		
		// Check tax code 
		if (StringUtils.isEmpty(shipment.getTaxCode())) {
			return error("Quý khách chưa nhập mã số thuế.");
		} else {
			String groupName = catosApiService.getGroupNameByTaxCode(shipment.getTaxCode()).getGroupName();
			if(groupName == null){
				error("Mã số thuế không tồn tại");
			}
		}
		
		// check order number
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			if (edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber()) == 0) {
				return error("Mã nhận container không chính xác");
			} else if (edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber()) == 0) {
				return error("Mã nhận container không chính xác");
			}
		} 
		
		shipment.setLogisticAccountId(getUserId());
		shipment.setLogisticGroupId(getGroupLogisticId());
		shipment.setCreateTime(new Date());
		shipment.setCreateBy(getUser().getLoginName());
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setStatus("1");
		
		// check bill of lading
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}
		
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}
	
	/**
	 * Get shipment detail by shipment id
	 * 
	 * @param shipmentId
	 * @return AjaxResult
	 */
	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	public AjaxResult getShipmentDetail(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setLogisticGroupId(getGroupLogisticId());
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		List<ShipmentDetailForm> shipmentDetailsReturn = new ArrayList<>();
		boolean customStatus = true;
		boolean processStatus = true;
		boolean paymentStatus = true;
		boolean finishStatus = true;
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			// Step 1: register
			ajaxResult.put("shipmentStep", 1);
			return ajaxResult;
		} else {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (!"R".equals(shipmentDetail.getCustomStatus())) {
					customStatus = false;
				} else if (!"Y".equals(shipmentDetail.getProcessStatus())) {
					processStatus = false;
				} else if (!"Y".equals(shipmentDetail.getPaymentStatus())) {
					paymentStatus = false;
				} else if (!"Y".equals(shipmentDetail.getFinishStatus())) {
					finishStatus = false;
				}
				ShipmentDetailForm shipmentDetailReturn = new ShipmentDetailForm();
				BeanUtils.copyBeanProp(shipmentDetailReturn, shipmentDetail);
				shipmentDetailsReturn.add(shipmentDetailReturn);
			}
		}
		
		Collections.sort(shipmentDetailsReturn, new StatusComparator());
		ajaxResult.put("shipmentDetails", shipmentDetailsReturn);
		
		// Step 2: custom 
		if (!customStatus) {
			ajaxResult.put("shipmentStep", 2);
			return ajaxResult;
		}
		
		// Step 3: process
		if (!processStatus) {
			ajaxResult.put("shipmentStep", 3);
			return ajaxResult;
		}
		
		// Step 4: payment
		if (!paymentStatus) {
			ajaxResult.put("shipmentStep", 4);
			
			// Get bill
			ProcessBill processBillParam = new ProcessBill();
			processBillParam.setShipmentId(shipmentId);
			processBillParam.setPaymentStatus("N");
			List<ProcessBill> processBills = processBillService.selectProcessBillList(processBillParam);
			List<ProcessBillForm> processBillsReturn = new ArrayList<>();
			
			Long total = 0L;
			if (CollectionUtils.isNotEmpty(processBills)) {
				for (ProcessBill processBill : processBills) {
					total += processBill.getVatAfterFee();
					ProcessBillForm processBillReturn = new ProcessBillForm();
					BeanUtils.copyBeanProp(processBillReturn, processBill);
					processBillsReturn.add(processBillReturn);
				}
			}
			
			Map<String, Object> paymentInfo = new HashMap<>();
			paymentInfo.put("total", total);
			paymentInfo.put("paymentContAmount", processBillsReturn.size());
			ajaxResult.put("paymentInfo", paymentInfo);
			ajaxResult.put("paymentDetail", processBillsReturn);
			return ajaxResult;
		}
		
		// Step 5: finish
		if (!finishStatus) {
			ajaxResult.put("shipmentStep", 5);
			return ajaxResult;
		}
		
		return error("Lỗi dữ liệu.");
	}
	
	@GetMapping("/shipment/{shipmentId}/contNo/{contNo}/shipment-detail")
	public AjaxResult getShipmentDetailByBlNo(@PathVariable("shipmentId") Long shipmentId, @PathVariable("contNo") String contNo) {
		
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		ShipmentDetail shipmentDetail = null;
		if ("1".equals(shipment.getEdoFlg())) {
			// TODO : eDO
		} else {
			// DO
			shipmentDetail = catosApiService.selectShipmentDetailByContNo(shipment.getBlNo(), contNo);
		}
		if (shipmentDetail == null) {
			return error("Không tìm thấy thông tin container.");
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentDetail", shipmentDetail);
		return ajaxResult;
	}
	
	/**
	 * Add shipment detail
	 * 
	 * @param shipmentDetail
	 * @return AjaxResult
	 */
	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/shipment-detail")
	public AjaxResult addShipmentDetail(@RequestBody ShipmentDetail shipmentDetail) {
		LogisticAccount user = getLogisticAccount();
		String taxCode = catosApiService.getTaxCodeBySnmGroupName(shipmentDetail.getConsignee());
		
		// TODO : validate form insert;
		
		// Update shipment detail
		if (shipmentDetail.getId() == null) {
			shipmentDetail.setLogisticGroupId(user.getGroupId());
			shipmentDetail.setCreateBy(user.getFullName());
			shipmentDetail.setCreateTime(new Date());
			shipmentDetail.setFe("F");
			shipmentDetail.setPaymentStatus("N");
			shipmentDetail.setUserVerifyStatus("N");
			shipmentDetail.setProcessStatus("N");
			shipmentDetail.setDoStatus("N");
			shipmentDetail.setPreorderPickup("N");
			shipmentDetail.setFinishStatus("N");
			shipmentDetail.setTaxCode(taxCode);
			shipmentDetail.setConsigneeByTaxCode(shipmentDetail.getConsignee());
			if ("VN".equalsIgnoreCase(shipmentDetail.getLoadingPort().substring(0, 2))) {
				shipmentDetail.setCustomStatus("R");
				shipmentDetail.setStatus(2);
			} else {
				shipmentDetail.setCustomStatus("N");
				shipmentDetail.setStatus(1);
			}
			if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
				return error("Lưu khai báo thất bại.");
			}
			
			// Update shipment status 
			Shipment shipment = new Shipment();
			shipment.setId(shipmentDetail.getShipmentId());
			shipment.setUpdateTime(new Date());
			shipment.setUpdateBy(getUser().getLoginName());
			shipment.setStatus("2");
			shipmentService.updateShipment(shipment);
		} 
		
		return success("Lưu khai báo thành công");
	}
	
	@Log(title = "Check Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.MOBILE)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList, @RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getGroupLogisticId());
			boolean customsNoMappingFlg = "1".equals(configService.selectConfigByKey(SystemConstants.ACCIS_CUSTOM_MAPPING_FLG_KEY));
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declareNoList to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// Neu bat buoc check to khai thi phai goi lai acciss
					if (!customsNoMappingFlg && catosApiService.checkCustomStatus(shipmentDetail.getContainerNo(), shipmentDetail.getVoyNo())) {
						shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						shipmentDetail.setCustomStatus("R");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
						AjaxResult ajaxResult = AjaxResult.success();
						ajaxResult.put("shipmentDetail", shipmentDetail);
					} else {
						customQueueService.offerShipmentDetail(shipmentDetail);
					}
				}
				return success();
			}
		}
		return error();
	}
	
	
}
