package vn.com.irtech.eport.carrier.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.carrier.domain.ContainerInfo;
import vn.com.irtech.eport.carrier.domain.ContainerInfoEmpty;
import vn.com.irtech.eport.carrier.utils.R;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;

/**
 * Container Information Controller
 * 
 * @author Admin
 * @date 2020-04-16
 */
@Controller
@RequestMapping("/carrier/cont")
@RequiresPermissions("carrier:inquery")
public class ContainerInfoController extends CarrierBaseController {

	private static final String EXPORT_SHEET_NAME = "Container";
	private String prefix = "carrier/cont";

	@GetMapping()
	public String cont() {
		return prefix + "/cont";
	}

	@GetMapping("/contFull")
	public String contfull(Model map) {
		map.addAttribute("contFE", "F");
		return prefix + "/cont";
	}

	@GetMapping("/contEmpty")
	public String contEmpty(Model map) {
		map.addAttribute("contFE", "E");
		return prefix + "/cont";
	}

	/**
	 * Get Container Information List
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ContainerInfo containerInfo, String toDate, String fromDate, String contFE,
			String carrierCode, int pageNum, int pageSize, String orderByColumn, String isAsc, String cntrNo) {
		startPage();
		// SEARCH CONT
		Map<String, Object> pageInfo = new HashMap<>();
		if (carrierCode.equals("") || carrierCode == null) {
			pageInfo.put("prntCodes", super.getGroupCodes());
		} else {
			for (String carrierStr : super.getGroupCodes()) {
				if (!carrierCode.equals(carrierStr)) {
					pageInfo.put("prntCodes", super.getGroupCodes());
				}
			}
		}

		pageInfo.put("pageNum", (pageNum - 1) * pageSize);
		pageInfo.put("pageSize", pageSize);
		containerInfo.setParams(pageInfo);
		containerInfo.setPtnrCode(carrierCode);
		if (cntrNo != null) {
			containerInfo.setCntrNo(cntrNo.toLowerCase());
		}

		if (fromDate != null) {
			containerInfo.setFromDate(fromDate);
		} else {
			fromDate = "";
			containerInfo.setFromDate(fromDate);
		}
		if (toDate != null) {
			containerInfo.setToDate(toDate);
		} else {
			toDate = "";
			containerInfo.setToDate(toDate);
		}

		if (contFE.equals("F")) {
			containerInfo.setFe("F");
			containerInfo.setCntrState("");
			containerInfo.setToDate("");
			containerInfo.setFromDate("");
			pageInfo.put("orderByColumn", "inDays");
			pageInfo.put("isAsc", "desc");
		}
		if (contFE.equals("E")) {
			containerInfo.setFe("E");
			containerInfo.setCntrState("");
			containerInfo.setToDate("");
			containerInfo.setFromDate("");
			pageInfo.put("orderByColumn", "inDays");
			pageInfo.put("isAsc", "desc");
		}
		if (orderByColumn != null && isAsc != null) {
			pageInfo.put("orderByColumn", orderByColumn);
			pageInfo.put("isAsc", isAsc);
		} else if (contFE.equals("")) {
			pageInfo.put("orderByColumn", "days");
			pageInfo.put("isAsc", "asc");
		}
		final String uri = Global.getApiUrl() + "/container/list";
		RestTemplate restTemplate = new RestTemplate();
		R r = restTemplate.postForObject(uri, containerInfo, R.class);
		int total = (int) r.get("total");
		TableDataInfo dataList = getDataTable((List) r.get("data"));
		dataList.setTotal(total);
		return dataList;
	}

	/**
	 * Export Container Information List
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Log(title = "Xuất DS Container", businessType = BusinessType.EXPORT, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ContainerInfo containerInfo, String toDate, String fromDate, String contFE,
			String carrierCode, String orderByColumn, String isAsc, String cntrNo) throws Exception {
		// Cont FE
		try {
			Map<String, Object> pageInfo = new HashMap<>();
			if (carrierCode.equals("") || carrierCode == null) {
				pageInfo.put("prntCodes", super.getGroupCodes());
			} else {
				for (String carrierStr : super.getGroupCodes()) {
					if (!carrierCode.equals(carrierStr)) {
						pageInfo.put("prntCodes", super.getGroupCodes());
					}
				}
			}
			containerInfo.setParams(pageInfo);
			containerInfo.setPtnrCode(carrierCode);

			if (cntrNo != null) {
				containerInfo.setCntrNo(cntrNo.toLowerCase());
			}

			// SEARCH CONT
			if (fromDate != null) {
				containerInfo.setFromDate(fromDate);
			} else {
				fromDate = "";
				containerInfo.setFromDate(fromDate);
			}
			if (toDate != null) {
				containerInfo.setToDate(toDate);
			} else {
				toDate = "";
				containerInfo.setToDate(toDate);
			}
			if (contFE.equals("F")) {
				containerInfo.setFe("F");
				containerInfo.setCntrState("");
				containerInfo.setToDate("");
				containerInfo.setFromDate("");
				pageInfo.put("orderByColumn", "inDays");
				pageInfo.put("isAsc", "desc");
			}
			if (contFE.equals("E")) {
				containerInfo.setFe("E");
				containerInfo.setCntrState("");
				containerInfo.setToDate("");
				containerInfo.setFromDate("");
				pageInfo.put("orderByColumn", "inDays");
				pageInfo.put("isAsc", "desc");
			}
			if (orderByColumn != null && isAsc != null) {
				pageInfo.put("orderByColumn", orderByColumn);
				pageInfo.put("isAsc", isAsc);
			} else if (contFE.equals("")) {
				pageInfo.put("orderByColumn", "days");
				pageInfo.put("isAsc", "asc");
			}
			final String uri = Global.getApiUrl() + "/container/export";

			RestTemplate restTemplate = new RestTemplate();
			R r = restTemplate.postForObject(uri, containerInfo, R.class);
			List<Map<String, Object>> listJson = (List) r.get("data");
			// Create list to export
			List list = new ArrayList();
			if (contFE.equals("E")) {
				ContainerInfoEmpty ctnr = null;
				// convert to list entity before export
				for (Map<String, Object> item : listJson) {
					ctnr = new ContainerInfoEmpty();
					BeanUtils.copyProperties(ctnr, item);
					list.add(ctnr);
				}
				ExcelUtil<ContainerInfoEmpty> util = new ExcelUtil<ContainerInfoEmpty>(ContainerInfoEmpty.class);
				return util.exportExcel(list, EXPORT_SHEET_NAME);
			}
			// if not Empty
			ContainerInfo ctnr = null;
			// convert to list entity before export
			for (Map<String, Object> item : listJson) {
				ctnr = new ContainerInfo();
				BeanUtils.copyProperties(ctnr, item);
				list.add(ctnr);
			}
			ExcelUtil<ContainerInfo> util = new ExcelUtil<ContainerInfo>(ContainerInfo.class);
			return util.exportExcel(list, EXPORT_SHEET_NAME);
		}
		catch (Exception e)
		{
			return AjaxResult.error("Có lỗi không xác định! vui lòng thử lại sau");
		}
		
	}

	@GetMapping("/listCarrierCode")
	@ResponseBody
	public List<String> lisCarrierCode() {
		return super.getGroupCodes();
	}

}
