package vn.com.irtech.eport.web.controller.mc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.form.VesselVoyageMc;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/mc/plan/management")
public class PlanManagementController extends BaseController {

	private static final String PREFIX = "mc/plan";

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@GetMapping()
	public String getRequestView() {
		return PREFIX + "/management";
	}

	@PostMapping("/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageList(@RequestBody PageAble<PickupHistory> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		PickupHistory pickupHistoryParam = param.getData();
		if (pickupHistoryParam == null) {
			pickupHistoryParam = new PickupHistory();
		}
		List<VesselVoyageMc> vesselVoyageMcs = pickupHistoryService.selectVesselVoyageList(pickupHistoryParam);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("vesselVoyageMcs", getDataTable(vesselVoyageMcs));
		return ajaxResult;
	}
}