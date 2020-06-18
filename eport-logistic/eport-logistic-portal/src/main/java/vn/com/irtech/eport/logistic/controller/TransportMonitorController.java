package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;

@Controller
@RequestMapping("/logistic/transportMonitor")
public class TransportMonitorController extends LogisticBaseController{
  @GetMapping("/")
  public String index() {
    return "/logistic/transportMonitor/index";
  }

  @GetMapping("/searchTruck")
  @ResponseBody
  public AjaxResult getTruckInfo(String keyWord) {
    return AjaxResult.success("Thành công");
  }
}
