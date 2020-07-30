package vn.com.irtech.eport.web.controller.edo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.beanutils.BeanUtils;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;

@Controller
@RequestMapping("/edo/manage")
public class EdoManageController extends BaseController {

  final String PREFIX = "edo/manage";
  @Autowired
  private IEdoService edoService;

  private static final String EXPORT_SHEET_NAME = "EDI_INFO";

  @Autowired
  private ICarrierGroupService carrierGroupService;

  @Autowired
	private IEdoAuditLogService edoAuditLogService;

  @GetMapping("/index")
  public String index() {
    return PREFIX + "/edo";
  }

  @PostMapping("/billNo")
  @ResponseBody
  public TableDataInfo billNo(@RequestBody PageAble<Edo> param) {
    startPage();
    Edo edo = param.getData();
    if (edo == null) {
      edo = new Edo();
    }
    List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
    return getDataTable(dataList);
  }

  @PostMapping("/edo")
  @ResponseBody
  public TableDataInfo edo(@RequestBody PageAble<Edo> param) {
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    Edo edo = param.getData();
    if (edo == null) {
      edo = new Edo();
    }
    List<Edo> dataList = edoService.selectEdoList(edo);
    return getDataTable(dataList);
  }

  @GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id,ModelMap map) {
		map.put("edoId", id);
		return PREFIX + "/history";
  }
  
  
  @GetMapping("/auditLog/{edoId}")
	@ResponseBody
	public TableDataInfo edoAuditLog(@PathVariable("edoId") Long edoId, EdoAuditLog edoAuditLog)
	{
		edoAuditLog.setEdoId(edoId);
		List<EdoAuditLog> edoAuditLogsList = edoAuditLogService.selectEdoAuditLogList(edoAuditLog);
		return getDataTable(edoAuditLogsList);
  }
  
  @GetMapping("/viewFileEdi")
	public String viewFileEdi()
	{
		return PREFIX + "/viewFileEdi";
	}

  @PostMapping("/readEdiOnly")
	@ResponseBody
	public Object readEdi(String fileContent)
	{
    List<Edo> edo = new ArrayList<>();
    fileContent = fileContent.replace("\n", "");
    fileContent = fileContent.replace("\r", "");
		String[] text = fileContent.split("'");
		edo = edoService.readEdi(text);
		return edo;
  }

  @GetMapping("/getOprCode")
  @ResponseBody
  public List<String> lisOprCode(String keyString)
  {
      return edoService.selectOprCode(keyString);
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
  
  @Log(title = "Export Excel Infomation File EDI", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(@RequestBody List<Edo> dataObj) throws Exception
  {
    // List list = new ArrayList();
    // Edo ctnr = null;
    // convert to list entity before export
    // for(Map<String, JSONObject> item : requestParams) {
    //   ctnr = new Edo();
    //   BeanUtils.copyProperties(ctnr, item);
    //     list.add(ctnr);
    // }
    // ExcelUtil<Edo> util = new ExcelUtil<Edo>(Edo.class);
    // return util.exportExcel(list, EXPORT_SHEET_NAME);
    System.out.print(dataObj);
    return null;
  }
}