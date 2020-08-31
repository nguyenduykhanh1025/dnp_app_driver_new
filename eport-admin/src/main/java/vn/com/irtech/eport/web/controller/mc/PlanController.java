package vn.com.irtech.eport.web.controller.mc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;

@Controller
@RequestMapping("/mc/plan")
public class PlanController extends BaseController {

	private static final String PREFIX = "mc/plan";

	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private ICatosApiService catosApiService;
	
	@Autowired
	private ISysDictDataService sysDictDataService;

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
		
		if (pickupHistory.getStatus() > 0) {
			return AjaxResult.error("Không thể sửa lệnh đã được gate in!");
		}
		
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
	@GetMapping("/block/list")
	@ResponseBody
	public List<JSONObject> blockList(String keyword){
		if(keyword == null || keyword == "") {
			keyword = "empty";
		}
		List<String> blocks = catosApiService.getBlockList(keyword);
		List<JSONObject> rs = new ArrayList<JSONObject>();
		for(String i : blocks) {
			JSONObject json = new JSONObject();
			json.put("id", i);
			json.put("text", i);
			rs.add(json);
		}
		return rs;
	}
	@RequestMapping("/bay/list")
	@ResponseBody
	public List<JSONObject> bayList(String keyword, String sztp){
		List<JSONObject> rs = new ArrayList<JSONObject>();
		if(sztp != null && sztp.substring(0, 1).equals("2")) {
			SysDictData sysDictData = new SysDictData();
			sysDictData.setDictLabel(keyword);
			sysDictData.setDictType("cont_plan_bay_2x");
			List<SysDictData> list = sysDictDataService.selectDictDataList(sysDictData);
			for(SysDictData i : list) {
				JSONObject json = new JSONObject();
				json.put("id", i.getDictValue());
				json.put("text", i.getDictValue());
				rs.add(json);
			}
		}
		if(sztp != null && sztp.substring(0, 1).equals("4")) {
			SysDictData sysDictData = new SysDictData();
			sysDictData.setDictLabel(keyword);
			sysDictData.setDictType("cont_plan_bay_4x");
			List<SysDictData> list = sysDictDataService.selectDictDataList(sysDictData);
			for(SysDictData i : list) {
				JSONObject json = new JSONObject();
				json.put("id", i.getDictValue());
				json.put("text", i.getDictValue());
				rs.add(json);
			}
		}
		return rs;
	}
	
	@GetMapping("/row/list")
	@ResponseBody
	public List<JSONObject> rowList(String keyword){
		List<JSONObject> rs = new ArrayList<JSONObject>();
		List<SysDictData> list = sysDictDataService.selectDictDataByType("cont_plan_row");
		for(SysDictData i : list) {
			JSONObject json = new JSONObject();
			json.put("id", i.getDictValue());
			json.put("text", i.getDictValue());
			rs.add(json);
		}
		return rs;
	}
	
	@GetMapping("/tier/list")
	@ResponseBody
	public List<JSONObject> tierList(String keyword){
		List<JSONObject> rs = new ArrayList<JSONObject>();
		List<SysDictData> list = sysDictDataService.selectDictDataByType("cont_plan_tier");
		for(SysDictData i : list) {
			JSONObject json = new JSONObject();
			json.put("id", i.getDictValue());
			json.put("text", i.getDictValue());
			rs.add(json);
		}
		return rs;
	}
	
	@GetMapping("/area/list")
	@ResponseBody
	public List<JSONObject> areaList(String keyword){
		if(keyword == null || keyword == "") {
			keyword = "empty";
		}
		List<String> blocks = catosApiService.getAreaList(keyword);
		List<JSONObject> rs = new ArrayList<JSONObject>();
		for(String i : blocks) {
			JSONObject json = new JSONObject();
			json.put("id", i);
			json.put("text", i);
			rs.add(json);
		}
		return rs;
	}
}