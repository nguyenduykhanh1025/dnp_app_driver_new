package vn.com.irtech.eport.web.controller.om;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/executeCatos")
public class ExecuteCatosController extends AdminBaseController {
	private String prefix = "om/executeCatos";

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IProcessBillService processBillService;

	@Autowired
	private IProcessHistoryService processHistoryService;

	@GetMapping("/index")
	public String getViewexEcuteCatos() {
		return prefix + "/executeCatos";
	}

	/**
	 * Select list shipment detail wait robot execute or robot can't be execute,
	 * group by shipment id
	 * 
	 * @return
	 */
	@PostMapping("/listWaitExec")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody PageAble<ProcessOrder> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		ProcessOrder processOrder = param.getData();
		if (processOrder == null) {
			processOrder = new ProcessOrder();
		}
		List<ProcessOrder> result = processOrderService.selectProcessOrderListForOmManagement(processOrder);
		return getDataTable(result);
	}

	/**
	 * Update list shipment detail
	 * 
	 * @param shipmentId
	 * @param processStatus
	 * @param mmap
	 * @return
	 */
	@GetMapping("/detail/{processOrderId}")
	public String edit(@PathVariable("processOrderId") Long processOrderId, ModelMap mmap) {
		ProcessOrder processOrder = new ProcessOrder();
    	processOrder.setId(processOrderId);
    	mmap.put("orderList", processOrderService.selectOrderListForOmSupport(processOrder));
		return prefix + "/edit";
	}

	@GetMapping("/process-order/doing")
  	@Transactional
	@ResponseBody
	public AjaxResult updateProcessOrder(String processOrderId) {
		String[] processOrderIds = {processOrderId};
		if (processOrderService.countProcessOrderDoing(processOrderIds) != 1) {
			return error("Xác nhận làm lệnh của bạn bị gián đoạn, vui lòng thử lại sau.");
		}
		if (processOrderService.updateDoingProcessOrder(processOrderIds) == 1) {
			ProcessHistory processHistory = new ProcessHistory();
			processHistory.setProcessOrderId(Long.valueOf(processOrderId));
			processHistory.setSysUserId(getUserId());
			processHistory.setStatus(1); // START
			processHistory.setResult("S"); // RESULT SUCCESS
			processHistory.setCreateTime(new Date());
			processHistoryService.insertProcessHistory(processHistory);
			return success();
		}
		return error("Lỗi hệ thống, vui lòng thử lại sau.");
	}

	@GetMapping("/process-order/canceling")
	@Transactional
	@ResponseBody
	public AjaxResult cancelProcessOrder(String processOrderId) {
		String[] processOrderIds = {processOrderId};
		if (processOrderService.updateCancelingProcessOrder(processOrderIds) == 1) {
			ProcessHistory processHistory = new ProcessHistory();
				processHistory.setProcessOrderId(Long.valueOf(processOrderId));
				processHistory.setSysUserId(getUserId());
				processHistory.setStatus(0); // CANCEL
				processHistory.setResult("S"); // RESULT SUCCESS
				processHistory.setCreateTime(new Date());
				processHistoryService.insertProcessHistory(processHistory);
			return success();
		}
		return error("Lỗi hệ thống, vui lòng thử lại sau.");
	}

	@PostMapping("/invoice-no")
	@Transactional
	@ResponseBody
	public AjaxResult updateInvoiceNo(@RequestBody ProcessOrder processOrder) {
		if (processOrder != null) {
			SysUser user = getUser();
			if ((processOrder.getReferenceNo() != null && !"".equals(processOrder.getReferenceNo())) || "Credit".equals(processOrder.getPayType())) {
				ShipmentDetail shipmentDetail = new ShipmentDetail();
				shipmentDetail.setProcessOrderId(processOrder.getId());
				List<ShipmentDetail> shipmentDetails = shipmentDetailService
						.selectShipmentDetailList(shipmentDetail);

				// SAVE BILL
				if ("Cash".equals(processOrder.getPayType())) {
					processBillService.saveProcessBillByInvoiceNo(processOrder);
				} else {
					processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
				}

				// UPDATE PROCESS ORDER
				processOrder.setStatus(2); // FINISH
				processOrder.setResult("S"); // RESULT SUCESS
				processOrder.setUpdateBy(user.getUserName());
				processOrder.setUpdateTime(new Date());
				processOrderService.updateProcessOrder(processOrder);

				// UPDATE SHIPMENT DETAIL
				shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", processOrder.getReferenceNo(),
						processOrder);

				// SAVE HISTORY
				ProcessHistory processHistory = new ProcessHistory();
				processHistory.setProcessOrderId(processOrder.getId());
				processHistory.setStatus(2); // FINISH
				processHistory.setSysUserId(getUserId());
				processHistory.setResult("S");
				processHistory.setCreateTime(new Date());
				processHistory.setCreateBy(user.getUserName());
				processHistoryService.insertProcessHistory(processHistory);

				return success("Thành công.");
			}
		}
		return error("Thất bại.");
	}
}
