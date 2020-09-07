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

import com.google.firebase.auth.GetUsersResult;
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
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/support/custom-receive-full")
public class SupportCustomReiceiveFullController  extends OmBaseController{
	protected final Logger logger = LoggerFactory.getLogger(SupportCustomReiceiveFullController.class);
    private final String PREFIX = "om/support/customReceiveFull"; 
    
    @Autowired
    private IShipmentDetailService shipmentDetailService;
    
    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private IShipmentService shipmentService;
    
    @Autowired IShipmentCommentService shipmentCommentService;
    
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
        return PREFIX + "/customReceiveFull";
    }
    
    @PostMapping("/shipments")
	@ResponseBody
	public TableDataInfo getListOrder(@RequestBody PageAble<Shipment> param) {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        Shipment shipment = param.getData();
        if (shipment == null) {
        	shipment = new Shipment();
        }
        shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		List<Shipment> shipments = shipmentService.getShipmentsForSupportCustom(shipment);
        TableDataInfo dataList = getDataTable(shipments);
		return dataList;
    }
    
    @GetMapping("/shipmentId/{shipmentId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getshipmentDetails(@PathVariable Long shipmentId) {
    	AjaxResult ajaxResult = success();
    	ShipmentDetail shipmentDetail = new ShipmentDetail();
    	shipmentDetail.setShipmentId(shipmentId);
        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        if (shipmentDetails.size() > 0) {
        	ajaxResult.put("shipmentDetails", shipmentDetails);
        	return ajaxResult;
        }
		return error();
    }
    @GetMapping("/confirm-result-notification/shipmentId/{shipmentId}")
    public String confirmResultNotification(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {
    	mmap.put("shipmentId", shipmentId);
    	return PREFIX + "/confirmResultNotification";
    }
    @Log(title = "Gửi thông báo Hỗ trợ Hải Quan Bốc Hàng(OM)", businessType = BusinessType.INSERT, operatorType = OperatorType.MANAGE)
    @PostMapping("/confirm-result-notification")
    @ResponseBody
    public AjaxResult sendNotification(@RequestBody ShipmentComment shipmentComment) {
    	if(shipmentComment == null || shipmentComment.getContent() == null ||
    			shipmentComment.getContent() == "") {
    		return error();
    	}
    	Shipment shipment = shipmentService.selectShipmentById(shipmentComment.getShipmentId());
    	shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
    	shipmentComment.setUserId(getUserId());
    	shipmentComment.setUserType("S");// S: DNP Staff
    	shipmentComment.setUserName(getUser().getUserName());
    	shipmentComment.setUserAlias(getUser().getUserName());//TODO get tạm username
    	shipmentComment.setCommentTime(new Date());
    	shipmentComment.setCreateTime(new Date());
    	shipmentComment.setCreateBy(getUser().getUserName());
    	//TODO
    	//setTopic
    	if(shipmentCommentService.insertShipmentComment(shipmentComment) == 1) {
    		return success();
    	}
    	return error();
    }
}
