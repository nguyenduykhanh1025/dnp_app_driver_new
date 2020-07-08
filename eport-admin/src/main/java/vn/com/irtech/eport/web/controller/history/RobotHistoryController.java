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
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;

@Controller
@RequestMapping("/history/robot")
public class RobotHistoryController extends BaseController {
    private final static String PREFIX = "history/robot";

    @Autowired
    private IProcessHistoryService processHistoryService;
    
    @GetMapping("/")
	public String getTruckHistory() {
		return PREFIX + "/index";
    }
    
    @PostMapping("/list")
	@ResponseBody
	public TableDataInfo getListTruckRobot(@RequestBody PageAble<ProcessHistory> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		ProcessHistory processHistory = param.getData();
		if (processHistory == null) {
			processHistory = new ProcessHistory();
		}
		List<ProcessHistory> pickupHistorys = processHistoryService.selectRobotHistory(processHistory);
		return getDataTable(pickupHistorys);
	}
}