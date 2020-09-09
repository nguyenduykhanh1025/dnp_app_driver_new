package vn.com.irtech.eport.web.controller.om;

import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
@Controller
@RequestMapping("/om/support/send-empty")
public class SupportSendEmptyController extends OmBaseController{
	protected final Logger logger = LoggerFactory.getLogger(SupportSendEmptyController.class);
    private final String PREFIX = "om/support/sendEmpty"; 
    
    @Autowired
    private IProcessOrderService processOrderService;
    
    @Autowired
    private IProcessBillService processBillService;

    @Autowired
    private IShipmentDetailService shipmentDetailService;
    
    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private ICatosApiService catosService;
    
    @Autowired
    private IShipmentService shipmentService;
    
    @Autowired
    private IEdoService edoService;
    
    @Autowired
    private IShipmentCommentService shipmentCommentService;
    
    @GetMapping("/view")
    public String getViewSupportReceiveFull(ModelMap mmap)
    {
		// ProcessOrder processOrder = new ProcessOrder();
	    // List<String> logisticsGroups = processOrderService.selectProcessOrderOnlyLogisticName(processOrder);
		// mmap.put("logisticsGroups", logisticsGroups);
		LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    LogisticGroup logisticGroupParam = new LogisticGroup();
	    logisticGroupParam.setDelFlag("0");
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
	    logisticGroups.add(0, logisticGroup);
	    mmap.put("logisticsGroups", logisticGroups);
        return PREFIX + "/sendEmpty";
    }
    
    @GetMapping("/verify-executed-command-success/process-order/{processOrderId}")
    public String verifyExecutedCommandSuccess(@PathVariable Long processOrderId, ModelMap mmap) {
  	  mmap.put("processOrderId", processOrderId);
  	  return PREFIX + "/verifyExecutedCommandSuccess";
    }
    
    @GetMapping("/reset-process-status/process-order/{processOrderId}")
    public String resetProcessStatus(@PathVariable Long processOrderId, ModelMap mmap) {
  	  mmap.put("processOrderId", processOrderId);
  	  return PREFIX + "/verifyResetProcessStatus";
    }
    
    @PostMapping("/orders")
	@ResponseBody
	public TableDataInfo getListOrder(@RequestBody PageAble<ProcessOrder> param) {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        ProcessOrder processOrder = param.getData();
        if (processOrder == null) {
            processOrder = new ProcessOrder();
        }
		processOrder.setResult("F");
		processOrder.setServiceType(Constants.SEND_CONT_EMPTY);
		List<ProcessOrder> processOrders = processOrderService.selectProcessOrderListWithLogisticName(processOrder);
        TableDataInfo dataList = getDataTable(processOrders);
		return dataList;
    }
    
    @GetMapping("/processOrderId/{processOrderId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getshipmentDetails(@PathVariable Long processOrderId) {
    	AjaxResult ajaxResult = success();
    	ShipmentDetail shipmentDetail = new ShipmentDetail();
    	shipmentDetail.setProcessOrderId(processOrderId);
        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        if (shipmentDetails.size() > 0) {
        	ajaxResult.put("shipmentDetails", shipmentDetails);
        	return ajaxResult;
        }
		return error();
    }
    @Log(title = "Xác nhận làm lệnh OK(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
    @PostMapping("/executed-the-command-catos-success")
    @ResponseBody
    public AjaxResult executedTheCommandCatosSuccess(Long processOrderId , String content) {
    	ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if(processOrder == null) {
			// Co loi bat thuong xay ra. order khong ton tai
			throw new IllegalArgumentException("Process order not exist");
		}
		// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setProcessOrderId(processOrderId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
    	//get orderNo from catos
		String orderNo = null, invoiceNo = null;
    	if(shipmentDetails.size() >0) {
			if (processOrder.getServiceType().equals(Constants.RECEIVE_CONT_FULL) ||
    				processOrder.getServiceType().equals(Constants.RECEIVE_CONT_EMPTY))
				orderNo = catosService.getOrderNoInInventoryByShipmentDetail(shipmentDetails.get(0));
			if(processOrder.getServiceType().equals(Constants.SEND_CONT_FULL) ||
    				processOrder.getServiceType().equals(Constants.SEND_CONT_EMPTY))
				orderNo = catosService.getOrderNoInReserveByShipmentDetail(shipmentDetails.get(0));
    	}
    	if(orderNo == null || orderNo.equals("")) {
    		return error();
    	}
    	//get Invoice
    	if(processOrder.getPayType().equals("Cash") && orderNo != null) {
    		invoiceNo = catosService.getInvoiceNoByOrderNo(orderNo);
    	}
    	//update processOrder
    	processOrder.setOrderNo(orderNo);
		processOrder.setInvoiceNo(invoiceNo);
		processOrder.setStatus(2); // FINISH		
		processOrder.setResult("S"); // RESULT SUCESS	
		processOrderService.updateProcessOrder(processOrder);
		// SAVE BILL TO PROCESS BILL BY INVOICE NO
		if (invoiceNo != null && !invoiceNo.equals("")) {
			processBillService.saveProcessBillByInvoiceNo(processOrder);
		} else if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
			processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
		} else if (processOrder.getProcessData() != null) {
			ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
			processBillService.saveShiftingBillWithCredit(processJsonData.getShipmentDetailIds(), processOrder);
			for (Long shipmentDetailId : processJsonData.getPrePickupContIds()) {
				ShipmentDetail prePickupShipmentDetail = new ShipmentDetail();
				prePickupShipmentDetail.setId(shipmentDetailId);
				prePickupShipmentDetail.setPrePickupPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(prePickupShipmentDetail);
			}
		}
		// UPDATE STATUS OF SHIPMENT DETAIL AFTER MAKE ORDER SUCCESS
		if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
			shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", invoiceNo, processOrder);
			Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
			if (processOrder.getServiceType() == EportConstants.SERVICE_PICKUP_FULL && "1".equals(shipment.getEdoFlg())) {
				for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
					Edo edo = new Edo();
					edo.setBillOfLading(shipment.getBlNo());
					edo.setContainerNumber(shipmentDetail2.getContainerNo());
					edo.setStatus("2"); // status process order has been made for this edo
					edoService.updateEdoByBlCont(edo);
				}
			}
		}
		//notify msg to Logistic
		if(content != null && content != "") {
			ShipmentComment shipmentComment = new ShipmentComment();
	    	Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
	    	shipmentComment.setShipmentId(shipment.getId());
	    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
	    	shipmentComment.setUserId(getUserId());
	    	shipmentComment.setUserType("S");// S: DNP Staff
	    	shipmentComment.setUserName(getUser().getUserName());
	    	shipmentComment.setUserAlias(getUser().getUserName());//TODO get tạm username
	    	shipmentComment.setCommentTime(new Date());
	    	shipmentComment.setContent(content);
	    	shipmentComment.setCreateTime(new Date());
	    	shipmentComment.setCreateBy(getUser().getUserName());
	    	shipmentComment.setTopic(Constants.SEND_CONT_EMPTY_SUPPORT);
	    	shipmentCommentService.insertShipmentComment(shipmentComment);
		}
    	return success();
    }
    @Log(title = "Reset Proccess Status(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
    @PostMapping("/reset-process-status")
    @Transactional
    @ResponseBody
    public AjaxResult resetProcessStatus(Long processOrderId, String content) {
    	ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if(processOrder == null) {
			// Co loi bat thuong xay ra. order khong ton tai
			throw new IllegalArgumentException("Process order not exist");
		}
		// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setProcessOrderId(processOrderId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		//update shipment detail 2 truong processOrderId, registerNo processStatus, status
		try {
			if(shipmentDetails.size() > 0) {
				for(ShipmentDetail i: shipmentDetails) {
					i.setProcessOrderId(null);
					i.setRegisterNo(null);
					i.setProcessStatus("N");
					i.setStatus(1);
					i.setUserVerifyStatus("N");
					shipmentDetailService.updateShipmentDetailForOMSupport(i);
				}
			}
			//delete record table process_order
			processOrderService.deleteProcessOrderById(processOrderId);
			//notify msg to Logistic
			if(content != null && content != "") {
				ShipmentComment shipmentComment = new ShipmentComment();
		    	Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
		    	shipmentComment.setShipmentId(shipment.getId());
		    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
		    	shipmentComment.setUserId(getUserId());
		    	shipmentComment.setUserType("S");// S: DNP Staff
		    	shipmentComment.setUserName(getUser().getUserName());
		    	shipmentComment.setUserAlias(getUser().getUserName());//TODO get tạm username
		    	shipmentComment.setCommentTime(new Date());
		    	shipmentComment.setContent(content);
		    	shipmentComment.setCreateTime(new Date());
		    	shipmentComment.setCreateBy(getUser().getUserName());
		    	shipmentComment.setTopic(Constants.SEND_CONT_EMPTY_SUPPORT);
		    	shipmentCommentService.insertShipmentComment(shipmentComment);
			}
	    	return success();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.getStackTrace();
			return error();
		}
    }
}
