package vn.com.irtech.eport.web.controller.accountant;

import java.util.Date;
import java.util.List;

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

import vn.com.irtech.eport.web.controller.AdminBaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysUser;


@Controller
@RequestMapping("/accountant/support")
public class accountantSupportController extends AdminBaseController{
    private final String PREFIX = "accountant/support"; 
    
    @Autowired
    private IProcessOrderService processOrderService;

    @Autowired
    private IShipmentDetailService shipmentDetailService;
    
    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private IProcessBillService processBillService;
    
    @Autowired
	private IPaymentHistoryService paymentHistoryService;
    
    @GetMapping("/receiveFull")
    public String getViewSupportReceiveFull(ModelMap mmap)
    {
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

    @GetMapping("/processOrderId/{processOrderId}/payment")
	public String getPaymentView(@PathVariable("processOrderId") String processOrderId, ModelMap mmap) {
        List<ProcessBill> processBills = processBillService.selectProcessBillListByProcessOrderIds(processOrderId);
        mmap.put("processBills", processBills);
        mmap.put("processOrderId", processOrderId);
		return PREFIX + "/paymentSupport";
    }
    
    @GetMapping("/payment")
    @Transactional
    @ResponseBody
    public AjaxResult updatePaymentStatus(Long processOrderId)
    {
        SysUser user = getUser();

        // UPDATE PROCESS BILL PAYMENT STATUS Y
        ProcessBill processBill = new ProcessBill();
        processBill.setProcessOrderId(processOrderId);
        processBill.setUpdateBy(user.getUserName());
        processBill.setUpdateTime(new Date());
        processBill.setPaymentStatus("Y");
        processBillService.updateBillList(processBill);
    
        // UPDATE SHIPMENT DETAIL PAYMENT STATUS Y
        ShipmentDetail shipmentDt = new ShipmentDetail();
        shipmentDt.setProcessOrderId(processOrderId);
        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
          if ("N".equals(shipmentDetail.getPaymentStatus())) {
            shipmentDetail.setPaymentStatus("Y");
            shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
            shipmentDetail.setUpdateBy(user.getUserName());
            shipmentDetail.setUpdateTime(new Date());
            shipmentDetailService.updateShipmentDetail(shipmentDetail);
          }
        }
    
        return success("Cập nhật trạng thái thanh toán thành công.");
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
    
    
    
}