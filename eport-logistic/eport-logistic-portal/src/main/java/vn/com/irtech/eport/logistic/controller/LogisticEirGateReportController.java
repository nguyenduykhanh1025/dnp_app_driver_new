package vn.com.irtech.eport.logistic.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.logistic.dto.EirGateDto;
import vn.com.irtech.eport.logistic.service.ICatosApiService;

@Controller
@RequiresPermissions("logistic:order")
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
		TableDataInfo tableDataInfo = new TableDataInfo();
		EirGateDto eirGateParam = param.getData();
		if (eirGateParam == null) {
			eirGateParam = new EirGateDto();
		}
		Map<String, Object> params = eirGateParam.getParams();
		if (params == null) {
			params = new HashMap<>();
		}

		if (params.get("variableYear") == null) {
			params.put("variableYear", Calendar.getInstance().get(Calendar.YEAR));
		}

		Integer pageNum = param.getPageNum();
		Integer pageSize = param.getPageSize();
		if (pageNum == null || pageSize == null) {
			return tableDataInfo;
		}

		params.put("rowNum", pageNum * pageSize);
		params.put("rowId", (pageNum - 1) * pageSize);
		params.put("variableTax", getGroup().getMst());
		eirGateParam.setParams(params);

		tableDataInfo.setRows(catosApiService.getEirGateReport(eirGateParam));
		tableDataInfo.setTotal(catosApiService.getEirGateReportTotal(eirGateParam));
		tableDataInfo.setCode(0);
		tableDataInfo.setMsg("Thành công");
		return tableDataInfo;
	}

	@PostMapping("/export")
	@ResponseBody
	public AjaxResult exportExcel(@RequestBody EirGateDto eirGate) {
		if (eirGate == null) {
			eirGate = new EirGateDto();
		}
		Map<String, Object> params = eirGate.getParams();
		if (params == null) {
			params = new HashMap<>();
		}

		String fromDate = params.get("variableStart").toString();
		String toDate = params.get("variableEnd").toString();
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(fromDate);
			dateEnd = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(toDate);
		} catch (ParseException e) {
			logger.error("Failed to parse string to date: " + e);
		}

		if (dateStart == null || dateEnd == null) {
			return error("Từ ngày và đến ngày không được để trống.");
		}

		long fortyDays = 40l * 24l * 60l * 60l * 1000l;
		if (Math.abs(dateEnd.getTime() - dateStart.getTime()) > fortyDays) {
			return error("Không thể xuất báo cáo trong khoảng lớn hơn 40 ngày!");
		}

		if (params.get("variableYear") == null) {
			params.put("variableYear", Calendar.getInstance().get(Calendar.YEAR));
		}

		params.put("variableTax", getGroup().getMst());
		eirGate.setParams(params);
		List<EirGateDto> eirGateDtos = catosApiService.getEirGateReport(eirGate);
		ExcelUtil<EirGateDto> util = new ExcelUtil<EirGateDto>(EirGateDto.class);
		String fileName = "Eir_Gate_Report";
		Calendar c = Calendar.getInstance();
		return util.exportExcel(eirGateDtos,
				fileName + "_" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH));
	}
}
