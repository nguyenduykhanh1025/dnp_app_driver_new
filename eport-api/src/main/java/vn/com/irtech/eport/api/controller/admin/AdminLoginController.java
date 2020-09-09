package vn.com.irtech.eport.api.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.domain.EportUserType;
import vn.com.irtech.eport.api.form.LoginReq;
import vn.com.irtech.eport.api.security.service.LoginService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;

@RestController
@RequestMapping("/admin")
public class AdminLoginController extends BaseController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ICatosApiService catosApiService;
	
	@Autowired
	private ISysDictDataService sysDictDataService;

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult login(@RequestBody LoginReq loginForm) {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("token", loginService.login(loginForm, EportUserType.ADMIN));
		
		// Get block from catos
		List<String> blocks = null;
		try {
			blocks = catosApiService.getBlockList("empty");
		} catch (Exception e) {
			logger.error("Failed to get list block from catos: " + e);
			blocks = new ArrayList<>();
		}
		ajaxResult.put("blocks", blocks);
		
		// Get list object dictionary from eport db bay 2x for container sztp 2x
		List<SysDictData> bay2xFull = null;
		try {
			bay2xFull = sysDictDataService.selectDictDataByType("cont_plan_bay_2x");
		} catch (Exception e) {
			logger.error("Failed to get list cont 2x: " + e);
		}
		List<String> bay2x = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(bay2xFull)) {
			for (SysDictData sysDictData2 : bay2xFull) {
				bay2x.add(sysDictData2.getDictValue());
			}
		}
		ajaxResult.put("bays2x", bay2x);
		
		// Get list object dictionary from eport db bay 4x for container sztp 4x 
		List<SysDictData> bay4xFull = null;
		try {
			bay4xFull = sysDictDataService.selectDictDataByType("cont_plan_bay_4x");
		} catch (Exception e) {
			logger.error("Failed to get list cont 4x: " + e);
		}
		List<String> bay4x = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(bay4xFull)) {
			for (SysDictData sysDictData2 : bay4xFull) {
				bay4x.add(sysDictData2.getDictValue());
			}
		}
		ajaxResult.put("bays4x", bay4x);
		
		
		List<SysDictData> rowsFull = null;
		try {
			rowsFull = sysDictDataService.selectDictDataByType("cont_plan_row");
		} catch (Exception e) {
			logger.error("Failed to get list row: " + e);
		}
		List<String> rows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(rowsFull)) {
			for (SysDictData sysDictData2 : rowsFull) {
				rows.add(sysDictData2.getDictValue());
			}
		}
		ajaxResult.put("rows", rows);
		
		List<SysDictData> tiersFull = null;
		try {
			tiersFull = sysDictDataService.selectDictDataByType("cont_plan_tier");
		} catch (Exception e) {
			logger.error("Failed to get list tier: " + e);
		}
		List<String> tiers = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(tiersFull)) {
			for (SysDictData sysDictData2 : tiersFull) {
				tiers.add(sysDictData2.getDictValue());
			}
		}
		ajaxResult.put("tiers", tiers);
		
		List<String> areasFull = null;
		try {
			areasFull = catosApiService.getAreaList("empty");
		} catch (Exception e) {
			logger.error("Failed to get list area from catos: " + e);
			areasFull = new ArrayList<>();
		}
		ajaxResult.put("areas", areasFull);
		return ajaxResult;
	}
}
