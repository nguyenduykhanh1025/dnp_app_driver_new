package vn.com.irtech.eport.web.controller.om;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
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

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
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
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.web.mqtt.MqttService;
import vn.com.irtech.eport.web.mqtt.MqttService.EServiceRobot;
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
    
    @Autowired
    private ServerConfig serverConfig;
    
	@Autowired
	private ISysRobotService sysRobotService;

	@Autowired
	private MqttService mqttService;

    @GetMapping("/view")
    public String getViewSupportReceiveFull(@RequestParam(required = false) Long sId, ModelMap mmap)
    {
    	if (sId != null) {
			mmap.put("sId", sId);
		}
    	mmap.put("domain", serverConfig.getUrl());
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
		if(CollectionUtils.isNotEmpty(shipmentDetails)) {
			List<ContainerInfoDto> cntrInfos = catosService.getContainerInfoDtoByContNos(shipmentDetails.get(0).getContainerNo());
			if (CollectionUtils.isNotEmpty(cntrInfos)) {
				for (ContainerInfoDto cntrInfo : cntrInfos) {
					if ("E".equals(cntrInfo.getFe())) {
						orderNo = cntrInfo.getJobOdrNo();
					}
				}
			}
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
			shipmentComment.setServiceType(shipment.getServiceType());
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

		if (!EportConstants.PROCESS_HISTORY_RESULT_FAILED.equals(processOrder.getResult())) {
			throw new IllegalArgumentException("Lệnh này không bị lỗi, không thể thực hiện reset lại.");
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
					i.setPaymentStatus("N");
					i.setUserVerifyStatus("N");
					i.setUpdateBy(getUser().getLoginName());
					shipmentDetailService.resetShipmentDetailProcessStatus(i);
				}
			}
			//delete record table process_order
			processOrderService.deleteProcessOrderById(processOrderId);
			//notify msg to Logistic
			if(content != null && content != "") {
				ShipmentComment shipmentComment = new ShipmentComment();
		    	Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
		    	SysUser user = getUser();
		    	shipmentComment.setShipmentId(shipment.getId());
		    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
		    	shipmentComment.setUserId(user.getUserId());
		    	shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);// S: DNP Staff
		    	shipmentComment.setUserName(user.getUserName());
		    	shipmentComment.setUserAlias(user.getDept().getDeptName());
		    	shipmentComment.setCommentTime(new Date());
		    	shipmentComment.setContent(content);
		    	shipmentComment.setCreateTime(new Date());
		    	shipmentComment.setCreateBy(user.getUserName());
		    	shipmentComment.setTopic(Constants.SEND_CONT_EMPTY_SUPPORT);
				shipmentComment.setServiceType(shipment.getServiceType());
		    	shipmentCommentService.insertShipmentComment(shipmentComment);
			}
	    	return success();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.getStackTrace();
			return error();
		}
    }
    
    @PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		SysUser user = getUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_DROP_EMPTY);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);
		
		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@PostMapping("/order/retry")
	@ResponseBody
	public AjaxResult retryRobot(Long processOrderId) {
		if (processOrderId == null) {
			return error("Bạn chưa chọn lệnh muốn cho robot chạy lại.");
		}

		// Get process order by id
		ProcessOrder dropEmptyOrder = processOrderService.selectProcessOrderById(processOrderId);

		if (dropEmptyOrder == null) {
			return error("Không tìm thấy lệnh lỗi cần xử lý, vui lòng kiểm tra lại thông tin.");
		}

		if (!EportConstants.PROCESS_ORDER_RESULT_FAILED.equalsIgnoreCase(dropEmptyOrder.getResult())) {
			return error("Lệnh bạn đang chọn không bị lỗi, vui lòng kiểm tra lại dữ liệu.");
		}

		// reset status
		dropEmptyOrder.setRunnable(true);
		dropEmptyOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
		dropEmptyOrder.setResult(EportConstants.PROCESS_ORDER_RESULT_WAITING);
		processOrderService.updateProcessOrder(dropEmptyOrder);

		// Send to robot
		// Find robot booking
		SysRobot robotParam = new SysRobot();
		robotParam.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
		robotParam.setIsSendContEmptyOrder(true);
		robotParam.setDisabled(false);
		SysRobot robotSendEmpty = sysRobotService.findFirstRobot(robotParam);

		// If robot drop empty available => send order to robot
		if (robotSendEmpty != null) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(processOrderId);
			// Get list of shipment details for current processOrder
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(dropEmptyOrder, shipmentDetails);
			try {
				mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_EMPTY, robotSendEmpty.getUuId());
			} catch (MqttException e) {
				logger.error("Failed to send drop empty order to robot " + robotSendEmpty.getUuId() + ":" + e);
			}
		}
		return success();
	}
}
