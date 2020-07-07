package vn.com.irtech.eport.web.controller.edo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/edo/manager")
public class EdoManageController extends BaseController {

  final String PREFIX = "/edo/manager";
  @Autowired
  private IEdoService edoService;

  @GetMapping()
  public String index() {
    return PREFIX + "/index";
  }

  @GetMapping("/billNo")
  @ResponseBody
  public TableDataInfo billNo(Edo edo, String fromDate, String toDate) {
    startPage();
    Map<String, Object> searchDate = new HashMap<>();
    searchDate.put("fromDate", fromDate);
    searchDate.put("toDate", toDate);
    edo.setParams(searchDate);
    List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
    return getDataTable(dataList);
  }

  @GetMapping("/edo")
  @ResponseBody
  public TableDataInfo edo(Edo edo, String fromDate, String toDate) {
    startPage();
    Map<String, Object> searchDate = new HashMap<>();
    searchDate.put("fromDate", fromDate);
    searchDate.put("toDate", toDate);
    edo.setParams(searchDate);
    List<Edo> dataList = edoService.selectEdoList(edo);
    return getDataTable(dataList);
  }
}