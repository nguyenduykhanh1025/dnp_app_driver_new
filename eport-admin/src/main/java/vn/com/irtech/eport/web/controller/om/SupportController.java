
package vn.com.irtech.eport.web.controller.om;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;


@Controller
@RequestMapping("/om/support")
public class SupportController extends BaseController{

    private final String PREFIX = "om/support"; 
    
    @Autowired
    private IProcessOrderService processOrderService;

    @Autowired
    private IShipmentDetailService shipmentDetailService;
    
    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private ICatosApiService catosService;
    
    @GetMapping("/receiveFull")
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
        return PREFIX + "/receiveFull";
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
		List<ProcessOrder> processOrders = processOrderService.selectProcessOrderListWithLogisticName(processOrder);
        TableDataInfo dataList = getDataTable(processOrders);
		return dataList;
    }
    
    @PostMapping("/shipmentDetails")
	@ResponseBody
	public TableDataInfo getshipmentDetails(@RequestBody PageAble<ShipmentDetail> param) {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        ShipmentDetail shipmentDetail = param.getData();
        if (shipmentDetail == null) {
            shipmentDetail = new ShipmentDetail();
        }
        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        TableDataInfo dataList = getDataTable(shipmentDetails);
		return dataList;
    }
	
	@Log(title = "Đồng bộ trạng thái Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
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
    
    @GetMapping("/receiveContEmpty")
    public String getViewSupportReceiveContEmpy(ModelMap mmap)
    {
        LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    LogisticGroup logisticGroupParam = new LogisticGroup();
	    logisticGroupParam.setDelFlag("0");
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
	    logisticGroups.add(0, logisticGroup);
	    mmap.put("logisticsGroups", logisticGroups);
        return PREFIX + "/receiveContEmpty";
    }

    @GetMapping("/receiveDo")
    public String getViewSupportReceiveDo(ModelMap mmap)
    {
        LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    LogisticGroup logisticGroupParam = new LogisticGroup();
	    logisticGroupParam.setDelFlag("0");
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
	    logisticGroups.add(0, logisticGroup);
	    mmap.put("logisticsGroups", logisticGroups);
        return PREFIX + "/receiveDo";
    }

	@Log(title = "Đồng bộ trạng thái nộp DO gốc", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
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

	@Log(title = "Đồng bộ trạng thái Container", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
    @PostMapping("/containerStatus")
	@ResponseBody
	public AjaxResult updateContainerStatus(String processOrderId) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
			    shipmentDetail.setContainerStatus(catosService.checkContainerStatus(shipmentDetail));
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
		}
		return success();
	}
	

    

}