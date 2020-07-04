package vn.com.irtech.eport.web.controller.history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/history/truck")
public class TruckHistoryController extends BaseController {
	private final static String PREFIX = "history/truck";

	@Autowired
	private IPickupHistoryService pickupHistoryService; 
	
	@GetMapping("/")
	public String getTruckHistory() {
		return PREFIX + "/index";
	}
 
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo getListTruckHistor(@RequestBody PickupHistory pickupHistory) {
		startPage();
		List<PickupHistory> pickupHistorys = pickupHistoryService.selectPickupHistoryListForHistory(pickupHistory);
		return getDataTable(pickupHistorys);
	}
}
