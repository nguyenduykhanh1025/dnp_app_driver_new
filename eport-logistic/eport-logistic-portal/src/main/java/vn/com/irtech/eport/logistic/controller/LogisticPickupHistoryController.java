package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/logistic/truck/history")
public class LogisticPickupHistoryController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/logisticTruck/history";
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo list() {
		startPage();
		return getDataTable(null);
	}
}
