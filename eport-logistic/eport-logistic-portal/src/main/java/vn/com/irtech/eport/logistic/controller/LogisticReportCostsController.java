package vn.com.irtech.eport.logistic.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.service.IProcessBillService;

@Controller
@RequestMapping("/logistic/report/costs")
public class LogisticReportCostsController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/report/costs";

	@Autowired
	private IProcessBillService processBillService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}
	
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody ProcessBill processBill) {
		startPage();
		processBill.setLogisticGroupId(getUser().getGroupId());
		List<ProcessBill> processBills = processBillService.selectBillReportList(processBill);
		return getDataTable(processBills);
	}
}
