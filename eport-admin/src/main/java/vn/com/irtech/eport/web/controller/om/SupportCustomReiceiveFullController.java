package vn.com.irtech.eport.web.controller.om;

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
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/om/support/custom-receive-full")
public class SupportCustomReiceiveFullController  extends BaseController{
	protected final Logger logger = LoggerFactory.getLogger(SupportCustomReiceiveFullController.class);
    private final String PREFIX = "om/support/customReceiveFull"; 
    
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
    
    @PostMapping("/shipments")
	@ResponseBody
	public TableDataInfo getListOrder(@RequestBody PageAble<Shipment> param) {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        Shipment shipment = param.getData();
        if (shipment == null) {
        	shipment = new Shipment();
        }
        shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
        shipment.setStatus("1");
		List<Shipment> shipments = shipmentService.getShipmentListForContSupply(shipment);
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
}
