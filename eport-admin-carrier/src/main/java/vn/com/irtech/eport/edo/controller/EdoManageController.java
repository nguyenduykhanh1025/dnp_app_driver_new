package vn.com.irtech.eport.edo.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;


@Controller
@RequestMapping("/edo/manage")
public class EdoManageController extends BaseController {

  final String PREFIX = "edo/manage";
  @Autowired
  private IEdoService edoService;

  @Autowired
	private IEdoAuditLogService edoAuditLogService;

  @GetMapping("/index")
  public String index() {
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
    if(edo.getBillOfLading() == null)
		{
			return null;
		}
    List<Edo> dataList = edoService.selectEdoListForReport(edo);
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

  @PostMapping("/getOprCode")
  @ResponseBody
  public List<Edo> lisOprCode()
  {
      Edo edo = new Edo();
      return edoService.selectOprCode(edo);
  }


  @PostMapping("/getVoyNo/{carrierCode}/{vessel}")
	@ResponseBody
	public List<Edo> listVoyNos(@PathVariable("carrierCode") String carrierCode,@PathVariable("vessel") String vessel) {
    Edo edo = new Edo();
    edo.setVessel(vessel);
    edo.setCarrierCode(carrierCode);
		return edoService.selectVoyNos(edo);
	}

	@PostMapping("/getVessel/{carrierCode}")
	@ResponseBody
	public List<Edo> listVessels(@PathVariable("carrierCode") String carrierCode) {
    Edo edo = new Edo();
    edo.setCarrierCode(carrierCode);
		return edoService.selectVessels(edo);
	}
}