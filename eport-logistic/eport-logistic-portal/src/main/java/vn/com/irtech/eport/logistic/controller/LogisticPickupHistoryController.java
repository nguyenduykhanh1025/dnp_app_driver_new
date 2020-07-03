package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/logistic/truck/history")
public class LogisticPickupHistoryController extends LogisticBaseController {
	
  private final String PREFIX = "logistic/logisticTruck/history";
  
  @Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list(PickupHistory pickupHistory) {
    startPage();
    Long groupId = super.getUser().getLogisticGroup().getId();
    pickupHistory.setLogisticGroupId(groupId);
    List<PickupHistory> pickupHistorys = pickupHistoryService.selectPickupHistoryList(pickupHistory);
		return getDataTable(pickupHistorys);
	}
}
