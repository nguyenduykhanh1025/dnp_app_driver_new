package vn.com.irtech.eport.logistic.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/logistic/transportMonitor")
public class TransportMonitorController extends LogisticBaseController{
  
  private final String PREFIX = "logistic/transportMonitor";
  
  @Autowired
  private IPickupHistoryService pickupHistoryService;

  @GetMapping("/")
  public String index() {
    return PREFIX + "/index";
  }

  @GetMapping("/searchTruck")
  @ResponseBody
  public AjaxResult getTruckInfo(String keyWord) {
    return AjaxResult.success("Thành công");
  }
  
  @SuppressWarnings("unchecked")
@GetMapping("/list")
  @ResponseBody
  public AjaxResult getListInfo() {
	  AjaxResult ajaxResult = AjaxResult.success();
	  PickupHistory pickupHistory = new PickupHistory();
	  pickupHistory.setLogisticGroupId(getUser().getGroupId());
	  List<PickupHistory> pickupHistories = pickupHistoryService.selectDeliveringDriverInfo(pickupHistory);
	  for (PickupHistory pickupHistory2 : pickupHistories) {
		  Map<String, Double> locationMap = (Map<String, Double>) CacheUtils.get("driver-"+pickupHistory2.getDriverId());
		  if (locationMap != null) {
			  pickupHistory2.setX(locationMap.get("x"));
			  pickupHistory2.setY(locationMap.get("y"));
		  }
	  }
	  ajaxResult.put("pickupHistories", pickupHistories);
	  return ajaxResult;
  }

  @SuppressWarnings("unchecked")
@PostMapping("/tableList")
	@ResponseBody
  public TableDataInfo tableList(@RequestBody PageAble<PickupHistory> param) {
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    PickupHistory pickupHistory = param.getData();
    if (pickupHistory == null) {
			  pickupHistory = new PickupHistory();
    }
    pickupHistory.setLogisticGroupId(getUser().getGroupId());
    List<PickupHistory> pickupHistories = pickupHistoryService.selectDeliveringDriverInfoTable(pickupHistory);
    for (PickupHistory pickupHistory2 : pickupHistories) {
		  Map<String, Double> locationMap = (Map<String, Double>) CacheUtils.get("driver-"+pickupHistory2.getDriverId());
		  if (locationMap != null) {
			  pickupHistory2.setX(locationMap.get("x"));
			  pickupHistory2.setY(locationMap.get("y"));
		  }
	  }
    TableDataInfo dataList = getDataTable(pickupHistories);
    return dataList;
  }
}
