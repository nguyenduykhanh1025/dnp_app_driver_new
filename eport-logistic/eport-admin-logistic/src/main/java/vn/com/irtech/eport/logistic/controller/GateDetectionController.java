package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;

/**
 * Gate DetectionController
 * 
 * @author Irtech
 * @date 2020-10-10
 */
@Controller
@RequestMapping("/gate/detection")
@RequiresPermissions("gate:detection")
public class GateDetectionController extends BaseController {
	private String prefix = "logistic/gate";

	@Autowired
	private IGateDetectionService gateDetectionService;

	@GetMapping()
	public String detection() {
		return prefix + "/detection";
	}

	/**
	 * Get Gate Detection List
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(GateDetection gateDetection) {
		startPage();
		List<GateDetection> list = gateDetectionService.selectGateDetectionList(gateDetection);
		return getDataTable(list);
	}

	/**
	 * Delete Gate Detection
	 */
	@Log(title = "Gate Detection", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(gateDetectionService.deleteGateDetectionByIds(ids));
	}
}
