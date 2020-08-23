package vn.com.irtech.eport.api.controller.logistic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.logistic.service.INapasApiService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@RestController
@RequestMapping("/logistic")
public class LogisticController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LogisticController.class);

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
	
	class ProcessIdComparator implements Comparator<ProcessBill> {
        public int compare(ProcessBill processBill1, ProcessBill processBill2) {
            // In the following line you set the criterion,
            // which is the name of Contact in my example scenario
            return processBill1.getProcessOrderId().compareTo(processBill2.getProcessOrderId());
        }
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
			// return "error/unauth";
		}
		
		ProcessBill processBillParam = new ProcessBill();
		processBillParam.setShipmentId(shipmentId);
		processBillParam.setLogisticGroupId(logisticAccount.getGroupId());
		processBillParam.setPaymentStatus("N");
		List<ProcessBill> processBills = processBillService.getBillListByShipmentId(shipmentId);
		if (CollectionUtils.isEmpty(processBills)) {
			// Shipment Doesn't have any bill need to pay
			return error();
			// return "error/404";
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
		
		ajaxResult.put("resultUrl", configService.selectConfigByKey("napas.payment.result"));
		ajaxResult.put("referenceOrder", "Thanh toan " + orderId);
		// TODO : get ip user
		String clientIp = "x.x.x.x";
		ajaxResult.put("clientIp", clientIp);
		ajaxResult.put("data", napasApiService.getDataKey(clientIp, "deviceId", orderId, total, napasApiService.getAccessToken()));
		
		return ajaxResult;
	}

	
	
	
	
}
