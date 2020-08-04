package vn.com.irtech.eport.web.controller.mc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Controller
@RequestMapping("/mc/plan")
public class PlanController extends BaseController {

	private static final String PREFIX = "mc/plan";

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@GetMapping("/request/index")
	public String getRequestView() {
		return PREFIX + "/request";
	}

	@GetMapping("/edit/{pickupHistoryId}")
	public String edit(@PathVariable("pickupHistoryId") Long pickupHistoryId, ModelMap mmap) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupHistoryId);
		mmap.put("pickupHistory", pickupHistory);
		return PREFIX + "/edit";
	}

	@GetMapping("/detail/{pickupHistoryId}")
	public String detail(@PathVariable("pickupHistoryId") Long pickupHistoryId, ModelMap mmap) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupHistoryId);
		mmap.put("pickupHistory", pickupHistory);
		mmap.put("viewOnly", true);
		return PREFIX + "/edit";
	}

	@GetMapping("/request/list")
	@ResponseBody
	public TableDataInfo listRequestPlan() {
		startPage();
		List<PickupHistory> result = pickupHistoryService.selectPickupHistoryWithoutYardPostion(getSearchParams());
		return getDataTable(result);
	}

	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editYardPosition(PickupHistory data) {
		
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(data.getId());
		if (pickupHistory == null) {
			return AjaxResult.error("Lệnh không tồn tại!");
		}
		
		if (pickupHistory.getStatus() > 1) {
			return AjaxResult.error("Không thể sửa lệnh đã được gate in!");
		}
		
		pickupHistory.setStatus(1);
		if (StringUtils.isNotEmpty(data.getArea())) {
			pickupHistory.setArea(data.getArea());
		} else {
			pickupHistory.setBay(data.getBay());
			pickupHistory.setBlock(data.getBlock());
			pickupHistory.setLine(data.getLine());
			pickupHistory.setTier(data.getTier());
		}
		pickupHistory.setUpdateBy(ShiroUtils.getLoginName());
		pickupHistory.setPlanningDate(new Date());
		pickupHistoryService.updatePickupHistory(pickupHistory);
		return AjaxResult.success();
	}

	@GetMapping("/history/index")
	public String getHistoryView() {
		return PREFIX + "/history";
	}

	@GetMapping("/history/list")
	@ResponseBody
	public TableDataInfo listPlanned() {
		startPage();
		List<PickupHistory> result = pickupHistoryService.selectPickupHistoryHasYardPostion(getSearchParams());
		return getDataTable(result);
	}

	private Map<String, String> getSearchParams() {
		Map<String, String> searchParams = new HashMap<String, String>();
		searchParams.put("beginTime", ServletUtils.getParameter("beginTime"));
		searchParams.put("endTime", ServletUtils.getParameter("endTime"));
		searchParams.put("serviceType", ServletUtils.getParameter("serviceType"));
		searchParams.put("contNo", ServletUtils.getParameter("contNo"));
		searchParams.put("sztp", ServletUtils.getParameter("sztp"));
		searchParams.put("vslNm", ServletUtils.getParameter("vslNm"));
		searchParams.put("voyNo", ServletUtils.getParameter("voyNo"));
		searchParams.put("dischargePort", ServletUtils.getParameter("dischargePort"));
		searchParams.put("consignee", ServletUtils.getParameter("consignee"));
		searchParams.put("opeCode", ServletUtils.getParameter("opeCode"));
		return searchParams;
	}
}