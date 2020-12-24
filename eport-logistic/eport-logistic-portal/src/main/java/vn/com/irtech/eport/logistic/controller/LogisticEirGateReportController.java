package vn.com.irtech.eport.logistic.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.dto.EirGateDto;
import vn.com.irtech.eport.logistic.service.ICatosApiService;

@Controller
@RequiresPermissions("logistic:transport")
@RequestMapping("/logistic/eir-gate")
public class LogisticEirGateReportController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/eirGate";

	@Autowired
	private ICatosApiService catosApiService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/report";
	}
	
	@PostMapping("/report")
	@ResponseBody
	public TableDataInfo list(@RequestBody PageAble<EirGateDto> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		EirGateDto eirGateParam = param.getData();
		if (eirGateParam == null) {
			eirGateParam = new EirGateDto();
		}
		Map<String, Object> params = eirGateParam.getParams();
		if (params == null) {
			params = new HashMap<>();
		}
		params.put("pageNum", param.getPageNum());
		params.put("pageSize", param.getPageSize());
		params.put("orderBy", param.getOrderBy());
		eirGateParam.setParams(params);
		return catosApiService.getEirGateReport(eirGateParam);
	}
}
