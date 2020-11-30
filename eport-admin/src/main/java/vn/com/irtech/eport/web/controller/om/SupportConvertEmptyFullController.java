package vn.com.irtech.eport.web.controller.om;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICfsHouseBillService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysUser;

@Controller
@RequestMapping("/om/support/convert/emty-full")
public class SupportConvertEmptyFullController extends OmBaseController {
	protected final Logger logger = LoggerFactory.getLogger(SupportConvertEmptyFullController.class);
	private final String PREFIX = "om/support/convertEmptyFull";

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private ICfsHouseBillService cfsHouseBillService;

	/**
	 * @param sId
	 * @param mmap
	 * @return
	 */
	@GetMapping("/view")
	public String getViewSupportEmptyFull(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());

		LogisticGroup logisticGroup = new LogisticGroup();
		logisticGroup.setGroupName("Chọn đơn vị Logistics");
		logisticGroup.setId(0L);
		LogisticGroup logisticGroupParam = new LogisticGroup();
		logisticGroupParam.setDelFlag("0");
		List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
		logisticGroups.add(0, logisticGroup);
		mmap.put("logisticsGroups", logisticGroups);
		return PREFIX + "/emptyFull";
	}

	/**
	 * @param param
	 * @return
	 */
	@PostMapping("/orders")
	@ResponseBody
	public TableDataInfo getListOrder(@RequestBody PageAble<ProcessOrder> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		ProcessOrder processOrder = param.getData();
		if (processOrder == null) {
			processOrder = new ProcessOrder();
		}
		processOrder.setServiceType(EportConstants.SERVICE_LOADING_CARGO);// 15
		processOrder.setResult(EportConstants.PROCESS_ORDER_RESULT_SUCCESS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateReceiptStatus", EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_DONE);
		params.put("fe", "E");
		processOrder.setParams(params);
		List<ProcessOrder> processOrders = processOrderService.selectProcessOrderListWithLogisticName(processOrder);
		TableDataInfo dataList = getDataTable(processOrders);
		return dataList;
	}

	/**
	 * @param processOrderId
	 * @return
	 */
	@GetMapping("/processOrderId/{processOrderId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getshipmentDetails(@PathVariable Long processOrderId) {
		AjaxResult ajaxResult = success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setProcessOrderId(processOrderId);
		shipmentDetail.setDateReceiptStatus(EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_DONE);
		shipmentDetail.setFe("E");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.size() > 0) {
			ajaxResult.put("shipmentDetails", shipmentDetails);
			return ajaxResult;
		}
		return error();
	}

	/**
	 * @param shipmentComment
	 * @return
	 */
	@PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		SysUser user = getUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF); // S
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);// 3
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@Log(title = "Yêu cầu xác nhận", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail/request-confirm")
	@ResponseBody
	public AjaxResult CheckShipmentDetail(String shipmentDetailIds) {
		ShipmentDetail shipmentDetailUpdate = new ShipmentDetail();
		shipmentDetailUpdate.setFe("F");
		shipmentDetailUpdate.setFinishStatus("Y");
		shipmentDetailService.updateShipmentDetailByProcessOderIds(shipmentDetailIds, shipmentDetailUpdate);
		return success("Xác nhận chuyển full thành công");
	}

	// house bill
	@GetMapping("/shipment-detail/{shipmentDetailId}/house-bill")
	public String getCfsHouseBill(@PathVariable("shipmentDetailId") Long shipmentDetailId, ModelMap mmap) {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		mmap.put("masterBill", shipmentDetail.getBookingNo());
		mmap.put("containerNo", shipmentDetail.getContainerNo());
		mmap.put("shipmentDetailId", shipmentDetailId);
		return PREFIX + "/houseBill";
	}

	@GetMapping("shipment-detail/{shipmentDetailId}/house-bills")
	@ResponseBody
	public AjaxResult getHouseBillList(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		CfsHouseBill cfsHouseBillParam = new CfsHouseBill();
		cfsHouseBillParam.setShipmentDetailId(shipmentDetailId);
		// cfsHouseBillParam.setLogisticGroupId(getUser().getGroupId());
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("cfsHouseBills", cfsHouseBillService.selectCfsHouseBillList(cfsHouseBillParam));
		return ajaxResult;
	}

}
