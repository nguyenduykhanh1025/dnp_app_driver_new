package vn.com.irtech.eport.carrier.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/depo")
@Transactional(rollbackFor = Exception.class)
public class CarrierDepoController extends CarrierBaseController {
	
	private final String PREFIX = "depo";

	@Autowired
	private IEdoService edoService;

	@GetMapping()
	public String EquipmentDo() {
		if (!hasDepoPermission()) {
			return "error/unauth";
		}
		return PREFIX + "/depoManagement";
	}

	@PostMapping("/blNo")
	@ResponseBody
	public TableDataInfo getBillList(@RequestBody PageAble<Edo> param) {
		if (!hasDepoPermission()) {
			return null;
		}
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edoParam = param.getData();
		if (edoParam == null) {
			edoParam = new Edo();
		}
		Map<String, Objecct> params = new HashMap<>();
		params.put("oprs", Convert.toStrArray(getuser().getOperateCode()));
		edoParam.setParams(params);
		List<Edo> edos = edoService.selectEdoListByBillNo(edoParam);
		if (CollectionUtils.isEmpty(edos)) {
			edos = new ArrayList<>();
		}
		return getDataTable(edos);
	}
	
	@PostMapping("/blNo/containers")
	@ResponseBody
	public TableDataInfo getContainerListByBlNo(@RequestBody PageAble<Edo> param) {
		if (!hasDoPermission()) {
			return null;
		}
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edoParam = param.getData();
		if (edoParam == null) {
			edoParam = new Edo();
		}
		Map<String, Objecct> params = new HashMap<>();
		params.put("oprs", Convert.toStrArray(getuser().getOperateCode()));
		edoParam.setParams(params);
		List<Edo> edos = edoService.selectEdoListForReport(edoParam);
		if (CollectionUtils.isEmpty(edos)) {
			edos = new ArrayList<>();
		}
		return getDataTable(edos);
	}
}