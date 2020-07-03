package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/logistic/report/container")
public class LogisticReportContController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/report/container";

	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}
	
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody PickupHistory pickupHistory) {
		startPage();
		pickupHistory.setLogisticGroupId(getUser().getGroupId());
		pickupHistory.setStatus(2);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryListForReport(pickupHistory);
		return getDataTable(pickupHistories);
	}
	
	
	
}
