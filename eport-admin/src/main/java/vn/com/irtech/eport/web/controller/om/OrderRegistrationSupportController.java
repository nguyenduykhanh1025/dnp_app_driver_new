package vn.com.irtech.eport.web.controller.om;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/om/order/support")
public class OrderRegistrationSupportController extends BaseController {

  private final String PREFIX = "om/orderRegistrationSupport";

  @Autowired
  private IShipmentService shipmentService;

  @Autowired
  private IProcessOrderService processOrderService;

  @GetMapping()
  public String getMainView() {
    return PREFIX + "/index";
  }

  @GetMapping("/custom/{shipmentId}")
  public String getCustomSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/customSupport";
  }

  @GetMapping("/driver/{shipmentId}")
  public String getdriverSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/driverSupport";
  }

  @GetMapping("/payment/{shipmentId}")
  public String getpaymentSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/paymentSupport";
  }

  @GetMapping("/do/{shipmentId}")
  public String getReceiveDoSupport(@PathVariable Long shipmentId, ModelMap mmap) {
    return PREFIX + "/receiveDoSupport";
  }

  @GetMapping("/verification/{shipmentId}")
  public String getVerificationsupport(@PathVariable Long shipmentId, ModelMap mmap) {
    ProcessOrder processOrder = new ProcessOrder();
    processOrder.setResult("F");
    processOrder.setStatus(0);
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
  @ResponseBody
  public AjaxResult updateProcessOrder(String processOrderIds) {
    if (processOrderService.updateProcessOrderStatusForOm(processOrderIds) == 1) {
      return success();
    }
    return error("Lỗi hệ thống, vui lòng thử lại sau.");
  }
}