//package vn.com.irtech.eport.web.controller.om;
//
//import java.util.Date;
//import java.util.List;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import vn.com.irtech.eport.carrier.service.IEdoService;
//import vn.com.irtech.eport.common.annotation.Log;
//import vn.com.irtech.eport.common.config.ServerConfig;
//import vn.com.irtech.eport.common.constant.Constants;
//import vn.com.irtech.eport.common.constant.EportConstants;
//import vn.com.irtech.eport.common.core.domain.AjaxResult;
//import vn.com.irtech.eport.common.core.page.PageAble;
//import vn.com.irtech.eport.common.core.page.TableDataInfo;
//import vn.com.irtech.eport.common.enums.BusinessType;
//import vn.com.irtech.eport.common.enums.OperatorType;
//import vn.com.irtech.eport.logistic.domain.CfsHouseBill;
//import vn.com.irtech.eport.logistic.domain.LogisticGroup;
//import vn.com.irtech.eport.logistic.domain.ProcessOrder;
//import vn.com.irtech.eport.logistic.domain.Shipment;
//import vn.com.irtech.eport.logistic.domain.ShipmentComment;
//import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
//import vn.com.irtech.eport.logistic.service.ICatosApiService;
//import vn.com.irtech.eport.logistic.service.ICfsHouseBillService;
//import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
//import vn.com.irtech.eport.logistic.service.IProcessBillService;
//import vn.com.irtech.eport.logistic.service.IProcessOrderService;
//import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
//import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
//import vn.com.irtech.eport.logistic.service.IShipmentService;
//import vn.com.irtech.eport.system.domain.SysUser;
//import vn.com.irtech.eport.system.dto.ContainerInfoDto;
//
//@Controller
//
////  /om/support/convert/emty-full
//
////  /om/support/loading-cargo/view
//@RequestMapping("/om/support/convert/full-emty")
//public class SupportConvertFullToEmptyController extends OmBaseController{
//	protected final Logger logger = LoggerFactory.getLogger(SupportConvertFullToEmptyController.class);
//	private final String PREFIX = "om/support/convertFullEmpty";
//    
//    @Autowired
//    private IProcessOrderService processOrderService;
//    
//    @Autowired
//    private IProcessBillService processBillService;
//
//    @Autowired
//    private IShipmentDetailService shipmentDetailService;
//    
//    @Autowired
//    private ILogisticGroupService logisticGroupService;
//    
//    @Autowired
//    private ICatosApiService catosService;
//    
//    @Autowired
//    private IShipmentService shipmentService;
//    
//    @Autowired
//    private IEdoService edoService;
//    
//    @Autowired
//    private IShipmentCommentService shipmentCommentService;
//    
//    @Autowired
//    private ServerConfig serverConfig;
//    
//    @Autowired
//	private ICfsHouseBillService cfsHouseBillService;
//    /**
//     * @param sId
//     * @param mmap
//     * @return
//     */
//    @GetMapping("/view")
//    public String getViewSupportFullEmpty(@RequestParam(required = false) Long sId, ModelMap mmap)
//    {
//    	if (sId != null) {
//			mmap.put("sId", sId);
//		}
//    	mmap.put("domain", serverConfig.getUrl());
//    	
//		LogisticGroup logisticGroup = new LogisticGroup();
//	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
//	    logisticGroup.setId(0L);
//	    LogisticGroup logisticGroupParam = new LogisticGroup();
//	    logisticGroupParam.setDelFlag("0");
//	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
//	    logisticGroups.add(0, logisticGroup);
//	    mmap.put("logisticsGroups", logisticGroups);
//		return PREFIX + "/fullEmpty";
//    }
//    
//    /**
//     * @param processOrderId
//     * @param mmap
//     * @return
//     */
//    @GetMapping("/verify-executed-command-success/process-order/{processOrderId}")
//    public String verifyExecutedCommandSuccess(@PathVariable Long processOrderId, ModelMap mmap) {
//  	  mmap.put("processOrderId", processOrderId);
//  	  return PREFIX + "/verifyExecutedCommandSuccess";
//    }
//    
//    /**
//     * @param processOrderId
//     * @param mmap
//     * @return
//     */
//    @GetMapping("/reset-process-status/process-order/{processOrderId}")
//    public String resetProcessStatus(@PathVariable Long processOrderId, ModelMap mmap) {
//  	  mmap.put("processOrderId", processOrderId);
//  	  return PREFIX + "/verifyResetProcessStatus";
//    }
//    
//    /**
//     * @param param
//     * @return
//     */
//    @PostMapping("/orders")
//	@ResponseBody
//	public TableDataInfo getListOrder(@RequestBody PageAble<ProcessOrder> param) {
//        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
//        ProcessOrder processOrder = param.getData();
//        if (processOrder == null) {
//            processOrder = new ProcessOrder();
//        }
//		processOrder.setServiceType(EportConstants.SERVICE_UNLOADING_CARGO);// 16
//		List<ProcessOrder> processOrders = processOrderService.selectProcessOrderListWithLogisticName(processOrder);
//        TableDataInfo dataList = getDataTable(processOrders);
//		return dataList;
//    }
//    
//    /**
//     * @param processOrderId
//     * @return
//     */
//    @GetMapping("/processOrderId/{processOrderId}/shipmentDetails")
//	@ResponseBody
//	public AjaxResult getshipmentDetails(@PathVariable Long processOrderId) {
//    	AjaxResult ajaxResult = success();
//    	ShipmentDetail shipmentDetail = new ShipmentDetail();
//    	shipmentDetail.setProcessOrderId(processOrderId);
//        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
//        if (shipmentDetails.size() > 0) {
//        	ajaxResult.put("shipmentDetails", shipmentDetails);
//        	return ajaxResult;
//        }
//		return error();
//    }
//    /**
//     * @param processOrderId
//     * @param content
//     * @return
//     */
//    @Log(title = "Xác nhận làm lệnh OK(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
//    @PostMapping("/executed-the-command-catos-success")
//    @ResponseBody
//    public AjaxResult executedTheCommandCatosSuccess(Long processOrderId, String content ) {
//    	ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
//		if(processOrder == null) {
//			// Co loi bat thuong xay ra. order khong ton tai
//			throw new IllegalArgumentException("Process order not exist");
//		}
//		// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
//		ShipmentDetail shipmentDetail = new ShipmentDetail();
//		shipmentDetail.setProcessOrderId(processOrderId);
//		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
//    	//get orderNo from catos
//		String orderNo = null, invoiceNo = null;
//    	if(CollectionUtils.isNotEmpty(shipmentDetails)) {
//			List<ContainerInfoDto> cntrInfos = catosService.getContainerInfoDtoByContNos(shipmentDetails.get(0).getContainerNo());
//			if (CollectionUtils.isNotEmpty(cntrInfos)) {
//				for (ContainerInfoDto cntrInfo : cntrInfos) {
//					if ("E".equals(cntrInfo.getFe())) {
//						orderNo = cntrInfo.getJobOdrNo2();
//					}
//				}
//			}
//    	}
//    	if(orderNo == null || orderNo.equals("")) {
//    		return error();
//    	}
//    	//get Invoice
//    	if(processOrder.getPayType().equals("Cash") && orderNo != null) {
//    		invoiceNo = catosService.getInvoiceNoByOrderNo(orderNo);
//    	}
//    	//update processOrder
//    	processOrder.setOrderNo(orderNo);
//		processOrder.setInvoiceNo(invoiceNo);
//		processOrder.setStatus(2); // FINISH		
//		processOrder.setResult("S"); // RESULT SUCESS	
//		processOrderService.updateProcessOrder(processOrder);
//		// SAVE BILL TO PROCESS BILL BY INVOICE NO
//		if (invoiceNo != null && !invoiceNo.equals("")) {
//			processBillService.saveProcessBillByInvoiceNo(processOrder);
//		} else {
//			processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
//		}
//
//		//notify msg to Logistic
//		if(content != null && content != "") {
//			ShipmentComment shipmentComment = new ShipmentComment();
//	    	Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
//	    	SysUser user = getUser();
//	    	shipmentComment.setShipmentId(shipment.getId());
//	    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
//	    	shipmentComment.setUserId(user.getUserId());
//	    	shipmentComment.setUserType("S");// S: DNP Staff
//	    	shipmentComment.setUserName(getUser().getUserName());
//	    	shipmentComment.setUserAlias(user.getDept().getDeptName());//TODO get tạm username
//	    	shipmentComment.setCommentTime(new Date());
//	    	shipmentComment.setContent(content);
//	    	shipmentComment.setCreateTime(new Date());
//	    	shipmentComment.setCreateBy(getUser().getUserName());
//			shipmentComment.setTopic(Constants.LOADING_CARGO_SUPPORT);// Đóng Hàng Tại Cảng
//			shipmentComment.setServiceType(shipment.getServiceType());
//	    	shipmentCommentService.insertShipmentComment(shipmentComment);
//		}
//    	return success();
//    }
//    
//    /**
//     * @param processOrderId
//     * @param content
//     * @return
//     */
//    @Log(title = "Reset Proccess Status(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
//    @PostMapping("/reset-process-status")
//    @Transactional
//    @ResponseBody
//    public AjaxResult resetProcessStatus(Long processOrderId, String content) {
//    	ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
//		if(processOrder == null) {
//			// Co loi bat thuong xay ra. order khong ton tai
//			throw new IllegalArgumentException("Process order not exist");
//		}
//
//		if (!EportConstants.PROCESS_HISTORY_RESULT_FAILED.equals(processOrder.getResult())) {// F
//			throw new IllegalArgumentException("Lệnh này không bị lỗi, không thể thực hiện reset lại.");
//		}
//
//		// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
//		ShipmentDetail shipmentDetail = new ShipmentDetail();
//		shipmentDetail.setProcessOrderId(processOrderId);
//		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
//		//update shipment detail 2 truong processOrderId, registerNo processStatus, status
//		try {
//			if(shipmentDetails.size() > 0) {
//				for(ShipmentDetail i: shipmentDetails) {
//					i.setProcessOrderId(null);
//					i.setRegisterNo(null);
//					i.setProcessStatus("N");
//					i.setStatus(2);
//					i.setPaymentStatus("N");
//					i.setUserVerifyStatus("N");
//					i.setUpdateBy(getUser().getLoginName());
//					shipmentDetailService.resetShipmentDetailProcessStatus(i);
//				}
//			}
//			//delete record table process_order
//			processOrderService.deleteProcessOrderById(processOrderId);
//			//notify msg to Logistic
//			if(content != null && content != "") {
//				ShipmentComment shipmentComment = new ShipmentComment();
//		    	Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
//		    	shipmentComment.setShipmentId(shipment.getId());
//		    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
//		    	shipmentComment.setUserId(getUserId());
//		    	shipmentComment.setUserType("S");// S: DNP Staff
//		    	shipmentComment.setUserName(getUser().getUserName());
//		    	shipmentComment.setUserAlias(getUser().getUserName());//TODO get tạm username
//		    	shipmentComment.setCommentTime(new Date());
//		    	shipmentComment.setContent(content);
//		    	shipmentComment.setCreateTime(new Date());
//		    	shipmentComment.setCreateBy(getUser().getUserName());
//				shipmentComment.setTopic(Constants.LOADING_CARGO_SUPPORT);// Đóng Hàng Tại Cảng
//				shipmentComment.setServiceType(shipment.getServiceType());
//		    	shipmentCommentService.insertShipmentComment(shipmentComment);
//			}
//	    	return success();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			e.getStackTrace();
//			return error();
//		}
//    }
//    
//    
//    /**
//     * @param shipmentComment
//     * @return
//     */
//    @PostMapping("/shipment/comment")
//	@ResponseBody
//	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
//		SysUser user = getUser();
//		shipmentComment.setCreateBy(user.getUserName());
//		shipmentComment.setUserId(user.getUserId());
//		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF); // S
//		shipmentComment.setUserAlias(user.getDept().getDeptName());
//		shipmentComment.setUserName(user.getUserName());
//		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);// 3
//		shipmentComment.setCommentTime(new Date());
//		shipmentComment.setResolvedFlg(true);
//		shipmentCommentService.insertShipmentComment(shipmentComment);
//		
//		// Add id to make background grey (different from other comment)
//		AjaxResult ajaxResult = AjaxResult.success();
//		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
//		return ajaxResult;
//	}
//    
//    @Log(title = "Yêu cầu xác nhận", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC) 
//	@PostMapping("/shipment-detail/request-confirm") 
//	@ResponseBody  
//	public AjaxResult CheckShipmentDetail(String shipmentDetailIds ) {   
//		ShipmentDetail shipmentDetailUpdate = new ShipmentDetail();  
//		shipmentDetailUpdate.setFe("E");
//		shipmentDetailUpdate.setFinishStatus("Y"); 
//		//shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds,shipmentDetailUpdate); 
//		shipmentDetailService.updateShipmentDetailByProcessOderIds(shipmentDetailIds,shipmentDetailUpdate);
//		return success("Xác nhận chuyển container full thành rỗng thành công");
//		}
//    
//    
//    
// // house bill
// 	@GetMapping("/shipment-detail/{shipmentDetailId}/house-bill")
// 	public String getCfsHouseBill(@PathVariable("shipmentDetailId") Long shipmentDetailId, ModelMap mmap) {
// 		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
// 		//if (shipmentDetail != null && getUser().getGroupId().equals(shipmentDetail.getLogisticGroupId())) {
// 			mmap.put("masterBill", shipmentDetail.getBookingNo());
// 			mmap.put("containerNo", shipmentDetail.getContainerNo());
// 			mmap.put("shipmentDetailId", shipmentDetailId);
// 		//}
// 		return PREFIX + "/houseBill";
// 	}
// 	
// 	@GetMapping("shipment-detail/{shipmentDetailId}/house-bills")
// 	@ResponseBody
// 	public AjaxResult getHouseBillList(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
// 		CfsHouseBill cfsHouseBillParam = new CfsHouseBill();
// 		cfsHouseBillParam.setShipmentDetailId(shipmentDetailId);
// 		//cfsHouseBillParam.setLogisticGroupId(getUser().getGroupId());
// 		AjaxResult ajaxResult = AjaxResult.success();
// 		ajaxResult.put("cfsHouseBills", cfsHouseBillService.selectCfsHouseBillList(cfsHouseBillParam));
// 		return ajaxResult;
// 	}
// 	
// 	
// 	// SAVE OR EDIT SHIPMENT DETAIL
// 	@Log(title = "Lưu Khai Báo House Bill", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
// 	@PostMapping("/shipment-detail/{shipmentDetailId}/house-bills")
// 	@Transactional
// 	@ResponseBody
// 	public AjaxResult saveHouseBill(@RequestBody List<CfsHouseBill> cfsHouseBills,
// 			@PathVariable("shipmentDetailId") Long shipmentDetailId) {
// 		if (CollectionUtils.isNotEmpty(cfsHouseBills)) {
// 			//LogisticAccount user = getUser();
// 			
// 			
// 			for (CfsHouseBill inputHouseBill : cfsHouseBills) {
// 				if (inputHouseBill.getId() != null) {
// 					inputHouseBill.setShipmentDetailId(shipmentDetailId);
// 					//inputHouseBill.setUpdateBy(user.getUserName());
// 					cfsHouseBillService.updateCfsHouseBill(inputHouseBill);
// 				} else {
// 					//inputHouseBill.setLogisticGroupId(user.getGroupId());
// 					inputHouseBill.setShipmentDetailId(shipmentDetailId);
// 					//inputHouseBill.setCreateBy(user.getUserName());
// 					cfsHouseBillService.insertCfsHouseBill(inputHouseBill);
// 				}
// 			}
// 			return success("Lưu khai báo thành công");
// 		}
// 		return error("Lưu khai báo thất bại");
// 	}
//
// 	// DELETE SHIPMENT DETAIL
// 	@Log(title = "Xóa Khai Báo House Bill", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
// 	@DeleteMapping("/house-bills")
// 	@Transactional
// 	@ResponseBody
// 	public AjaxResult deleteHouseBills(String houseBillIds) {
// 		if (houseBillIds != null) {
// 			cfsHouseBillService.deleteCfsHouseBillByIds(houseBillIds);
// 			return success("Xóa house bill thành công");
// 		}
// 		return error("Xóa house bill thất bại");
// 	}
//
// 	@PostMapping("/shipment-detail/register-date-receipt")
// 	@ResponseBody
// 	public AjaxResult registerDateReceiptShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
// 		for (ShipmentDetail detail : shipmentDetails) {
// 			if (detail.getDateReceipt() == null) {
// 				return error("Bạn chưa nhập ngày rút hàng.");
// 			} else {
// 				ShipmentDetail shipmentDetailSave = shipmentDetailService.selectShipmentDetailById(detail.getId());
// 				shipmentDetailSave.setDateReceipt(detail.getDateReceipt());
// 				shipmentDetailSave.setDateReceiptStatus(EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_PROGRESS);
// 				shipmentDetailService.updateShipmentDetail(shipmentDetailSave);
// 			}
// 		}
//
// 		return success("Đăng kí ngày rút hàng thành công.");
// 	}
//     
//}
//
