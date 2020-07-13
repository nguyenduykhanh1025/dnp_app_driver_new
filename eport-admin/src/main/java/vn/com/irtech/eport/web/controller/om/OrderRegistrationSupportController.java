package vn.com.irtech.eport.web.controller.om;

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
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/order/support")
public class OrderRegistrationSupportController extends AdminBaseController {

  private final String PREFIX = "om/orderRegistrationSupport";

  @Autowired
  private IShipmentService shipmentService;

  @Autowired
  private IProcessOrderService processOrderService;

  @Autowired
  private IProcessBillService processBillService;

  @Autowired
  private IShipmentDetailService shipmentDetailService;

  @Autowired IProcessHistoryService processHistoryService;

  @GetMapping()
  public String getMainView() {
    return PREFIX + "/index";
  }

  @GetMapping("/custom/{shipmentId}")
  public String getCustomSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    ShipmentDetail shipmentDetail = new ShipmentDetail();
    shipmentDetail.setShipmentId(shipmentId);
    mmap.put("shipmentDetails", shipmentDetailService.selectShipmentDetailList(shipmentDetail));
    return PREFIX + "/customSupport";
  }

  @GetMapping("/driver/{shipmentId}")
  public String getdriverSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/driverSupport";
  }

  @GetMapping("/payment/{shipmentId}")
  public String getpaymentSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    ProcessOrder processOrder = new ProcessOrder();
    processOrder.setShipmentId(shipmentId);
    mmap.put("orderList", processOrderService.selectOrderListForOmSupport(processOrder));
    return PREFIX + "/paymentSupport";
  }

  @GetMapping("/do/{shipmentId}")
  public String getReceiveDoSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/receiveDoSupport";
  }

  @GetMapping("/verification/{shipmentId}")
  public String getVerificationsupport(@PathVariable Long shipmentId, ModelMap mmap) {
    ProcessOrder processOrder = new ProcessOrder();
    processOrder.setShipmentId(shipmentId);
    mmap.put("orderList", processOrderService.selectOrderListForOmSupport(processOrder));
    return PREFIX + "/verificationSupport";
  }

  @PostMapping("/shipments")
  @ResponseBody
  public TableDataInfo getShipmentList(@RequestBody PageAble<Shipment> param) {
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    Shipment shipment = param.getData();
    if (shipment == null) {
      shipment = new Shipment();
    }
    List<Shipment> shipments = shipmentService.selectShipmentListForOm(shipment);
    return getDataTable(shipments);
  }

  @GetMapping("/process-order/doing")
  @Transactional
  @ResponseBody
  public AjaxResult updateProcessOrder(String processOrderIds) {
    if (processOrderService.updateDoingProcessOrder(processOrderIds) == 1) {
      String[] processOrderIdArr = processOrderIds.split(",");
      for (String processOrderId : processOrderIdArr) {
        ProcessHistory processHistory = new ProcessHistory();
        processHistory.setProcessOrderId(Long.valueOf(processOrderId));
        processHistory.setSysUserId(getUserId());
        processHistory.setStatus(1); // START
        processHistory.setResult("S"); // RESULT SUCCESS
        processHistory.setCreateTime(new Date());
        processHistoryService.insertProcessHistory(processHistory);
      }
      return success();
    }
    return error("Lỗi hệ thống, vui lòng thử lại sau.");
  }

  @GetMapping("/process-order/canceling")
  @Transactional
  @ResponseBody
  public AjaxResult cancelProcessOrder(String processOrderIds) {
    if (processOrderService.updateCancelingProcessOrder(processOrderIds) == 1) {
      String[] processOrderIdArr = processOrderIds.split(",");
      for (String processOrderId : processOrderIdArr) {
        ProcessHistory processHistory = new ProcessHistory();
        processHistory.setProcessOrderId(Long.valueOf(processOrderId));
        processHistory.setSysUserId(getUserId());
        processHistory.setStatus(3); // CANCEL
        processHistory.setResult("S"); // RESULT SUCCESS
        processHistory.setCreateTime(new Date());
        processHistoryService.insertProcessHistory(processHistory);
      }
      return success();
    }
    return error("Lỗi hệ thống, vui lòng thử lại sau.");
  }

  @PostMapping("/invoice-no")
  @Transactional
  @ResponseBody
  public AjaxResult updateInvoiceNo(@RequestBody List<ProcessOrder> processOrders) {
    if (processOrders != null && processOrders.size() > 0) {
      SysUser user = getUser();
      for (ProcessOrder processOrder : processOrders) {
        if ((processOrder.getReferenceNo() != null && !"".equals(processOrder.getReferenceNo())) || "Credit".equals(processOrder.getPayType())) {
          ShipmentDetail shipmentDetail = new ShipmentDetail();
			    shipmentDetail.setProcessOrderId(processOrder.getId());
			    List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);

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
          shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", processOrder.getReferenceNo(), processOrder);

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
    }
    return error("Thất bại.");
  }

  
}