package vn.com.irtech.eport.carrier.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;

@Controller
@RequestMapping("/edo")
@Transactional(rollbackFor = Exception.class)
public class CarrierEdoController extends CarrierBaseController {

    private final String PREFIX = "edo";
	

	@Autowired
	private IEdoService edoService;

	@Autowired
    private ISysDictDataService dictDataService;
	
	@Autowired
	private IEdoAuditLogService edoAuditLogService;

    @GetMapping("/index")
	public String EquipmentDo() {
		if (!hasEdoPermission()) {
			return "error/404";
		}
		return PREFIX + "/edo";
	}


	@PostMapping("/billNo")
	@ResponseBody
	public TableDataInfo billNo(@RequestBody PageAble<Edo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
	  if (edo == null) {
		edo = new Edo();
	  }
	  edo.setCarrierCode(super.getUserGroup().getGroupCode());
	  List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
	  return getDataTable(dataList);
	}

	//List
	@PostMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(@RequestBody PageAble<Edo> param)
	{
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
		if (edo == null) {
			edo = new Edo();
		}
		edo.setCarrierCode(super.getUserGroup().getGroupCode());
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}
 
	@GetMapping("/carrierCode")
	@ResponseBody
    public List<String> carrierCode()
    {
        return super.getGroupCodes();
	}
	
	@GetMapping("/update")
	public String update()
	{
		return PREFIX + "/update";
	}

	@GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id,ModelMap map) {
		map.put("edoId", id);
		return PREFIX + "/history";
	}

	@GetMapping("/update/{id}")
	public String getUpdate(@PathVariable("id") Long id,ModelMap map) {
		map.put("id", id);
		Edo edo = edoService.selectEdoById(id);
		map.put("edo", edo);
		return PREFIX + "/update";
	}

	@GetMapping("/multiUpdate/{ids}")
	public String multiUpdate(@PathVariable("ids") String ids,ModelMap map)
	{
		map.put("ids", ids);
		String [] idMap = ids.split(",");
		Long id = Long.parseLong(idMap[0]);
		Edo edo = edoService.selectEdoById(id);
		map.put("edo", edo);
		return PREFIX + "/multiUpdate";
	}

	@PostMapping("/update")
	@ResponseBody 
	public AjaxResult multiUpdate(String ids,Edo edo)
	{
		if(ids == null)
		{
			ids = edo.getId().toString();
		}
		try {
			EdoAuditLog edoAuditLog = new EdoAuditLog();
			Date timeNow = new Date();
			edoAuditLog.setCarrierId(super.getUser().getGroupId());
			edoAuditLog.setCarrierCode(super.getUserGroup().getGroupCode());
			edoAuditLog.setCreateTime(timeNow);
			String[] idsList = ids.split(",");
			edo.setCarrierCode(super.getUserGroup().getGroupCode());
			edo.setCarrierId(super.getUser().getGroupId());
			for(String id : idsList)
			{
				Edo edoCheck = new Edo();
				edoCheck.setId(Long.parseLong(id));
				// edoCheck.setCarrierId(super.getUser().getGroupId());
				Edo seR = new Edo();
				seR = edoService.selectFirstEdo(edoCheck);
				if(edoService.selectFirstEdo(edoCheck) == null)
				{
					return AjaxResult.error("Bạn đã chọn container mà bạn không có quyền cập nhật, vui lòng kiếm tra lại dữ liệu");
				}else if (edoService.selectFirstEdo(edoCheck).getStatus().equals("3")) {
					return AjaxResult.error("Bạn đã chọn container đã GATE-IN ra khỏi cảng, vui lòng kiểm tra lại dữ liệu!");
				}
			}
			for(String id : idsList)
			{	
				edo.setId(Long.parseLong(id));
				edoService.updateEdo(edo);
				edo.setCreateBy(super.getUser().getEmail());
				edoAuditLogService.updateAuditLog(edo);	
			}
		return AjaxResult.success("Update thành công");
		}catch(Exception e) {
			return AjaxResult.error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		} 
		
	}
	@PostMapping("/readEdiOnly")
	@ResponseBody
	public Object readEdi(String fileContent)
	{
		List<Edo> edo = new ArrayList<>();
		String[] text = fileContent.split("'");
		edo = edoService.readEdi(text);
		return edo;
	}

	@GetMapping("/auditLog/{edoId}")
	@ResponseBody
	public TableDataInfo edoAuditLog(@PathVariable("edoId") Long edoId, EdoAuditLog edoAuditLog)
	{
		edoAuditLog.setEdoId(edoId);
		List<EdoAuditLog> edoAuditLogsList = edoAuditLogService.selectEdoAuditLogList(edoAuditLog);
		return getDataTable(edoAuditLogsList);
	}

	@GetMapping("/getVesselCode")
	@ResponseBody
	public List<String> lisVesselNo(String keyString)
	{
		return edoService.selectVesselNo(keyString);
	}

	@GetMapping("/getVoyNo")
	@ResponseBody
	public List<String> listVoyNo(String keyString)
	{
		return edoService.selectVoyNo(keyString);
	}

	@GetMapping("/getVessel")
	@ResponseBody
	public List<String> listVessel(String keyString)
	{
		return edoService.selectVesselList(keyString);
	}

	@GetMapping("/getEmptyContainerDeport")
	@ResponseBody
	public AjaxResult listEmptyContainerDeport()
	{
		SysDictData dictData = new SysDictData();
		dictData.setDictType("edo_empty_container_deport");
		return AjaxResult.success(dictDataService.selectDictDataList(dictData));
	}


	// Report
	@GetMapping("/report")
	public String report() {
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/report";
	}

	//List
	@PostMapping("/edoReport")
	@ResponseBody
	public TableDataInfo edoReport(@RequestBody PageAble<Edo> param)
	{
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
		  if (edo == null) {
			edo = new Edo();
		  }
		edo.setCarrierId(super.getUser().getGroupId());
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}



}