package vn.com.irtech.eport.web.controller.om;

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
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/generalSupport")
public class GeneralOmSupportController extends AdminBaseController {
	private final String PREFIX = "om/generalSupport";

	@Autowired
	private IProcessOrderService processOrderService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private ILogisticGroupService logisticGroupService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private ICatosApiService catosService;
	
	@GetMapping()
	public String getViewPaymentHistory(ModelMap mmap) {
		LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(new LogisticGroup());
	    logisticGroups.add(0, logisticGroup);
	    mmap.put("logisticsGroups", logisticGroups);
		return PREFIX + "/index";
	}
	
	@GetMapping("/processOrderId/{processOrderId}/payment")
	public String getPaymentView(@PathVariable("processOrderId") String processOrderId, ModelMap mmap) {
		List<ProcessBill> processBills = processBillService.selectProcessBillListByProcessOrderIds(processOrderId);
		mmap.put("processBills", processBills);
		return PREFIX + "/paymentSupport";
	}
	
	@PostMapping("/orders")
	@ResponseBody
	public AjaxResult getListOrder(@RequestBody ProcessOrder processOrder) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ProcessOrder> processOrders = processOrderService.selectOrdersByShipmentId(processOrder);
		ProcessOrder processOrder2 = new ProcessOrder();
		processOrder2.setShipmentId(processOrder.getShipmentId());
		processOrder2.setId(0L);
		processOrder2.setRemark("Danh sách chưa xác nhận làm lệnh");
		processOrders.add(processOrder2);
		ajaxResult.put("processOrders", processOrders);
		return ajaxResult;
	}
	
	@GetMapping("/shipment/{shipmentId}/processOrder/{processOrderId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId, @PathVariable("processOrderId") Long processOrderId) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ShipmentDetail> shipmentDetails = null;
		if (processOrderId != 0) {
			shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId.toString());
		} else {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setUserVerifyStatus("N");
			shipmentDetail.setShipmentId(shipmentId);
			shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		}
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}
	
	@PostMapping("/doStatus")
	@ResponseBody
	public AjaxResult updateDoStatus(String processOrderId) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setDoReceivedTime(new Date());
				shipmentDetail.setDoStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
		}
		return success();
	}
	
	@PostMapping("/customStatus")
	@ResponseBody
	public AjaxResult updateCustomStatus(String processOrderId) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (catosService.checkCustomStatus(shipmentDetail.getContainerNo(), shipmentDetail.getVoyNo())) {
			        shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
			        shipmentDetail.setCustomStatus("R");
			        shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
			}
		}
		return success();
	}
	
	@PostMapping("/customStatus/shipmentDetail/{shipmentDetailId}")
	@ResponseBody
	public AjaxResult updateCustomStatus(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (catosService.checkCustomStatus(shipmentDetail.getContainerNo(), shipmentDetail.getVoyNo())) {
	        shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
	        shipmentDetail.setCustomStatus("R");
	        shipmentDetailService.updateShipmentDetail(shipmentDetail);
		}
		return success();
	}
}
