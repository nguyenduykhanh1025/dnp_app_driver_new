package vn.com.irtech.eport.web.controller.edo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/edo/manage")
public class EdoManageController extends BaseController {
    
    final String PREFIX = "/edo/manage";
    @Autowired
    private IEdoService edoService;
    @Autowired
    private IEdoHistoryService edoHistoryService;

    @GetMapping("/index")
    public String index()
    {
        return PREFIX + "/index";
    }

    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo getShipmentDetail( ) {
        startPage();
        Edo edo = new Edo();
        List<Edo> Edo = edoService.selectEdoList(edo);
        TableDataInfo dataInfo = getDataTable(Edo);
        return dataInfo;
    }
    
    @GetMapping("/edo")
    public String history()
    {
        return PREFIX + "/edo";
    }

    @GetMapping("/getEdo")
	@ResponseBody
	public TableDataInfo edo(Edo edo,String fromDate,String toDate)
	{
		startPage();
		Map<String, Object> searchDate = new HashMap<>();
		searchDate.put("fromDate", fromDate);
		searchDate.put("toDate", toDate);
		edo.setParams(searchDate);
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}

    @GetMapping("/history/{containerNumber}")
	public String getHistory(@PathVariable("containerNumber") String containerNumber,ModelMap map) {
		map.put("containerNumber",containerNumber);
		return PREFIX + "/history";
	}

    @GetMapping("/getHistory")
    @ResponseBody
    public TableDataInfo getHistory(EdoHistory edoHistory,String containerNumber)
    {
        startPage();
        edoHistory.setContainerNumber(containerNumber);
        List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
        return getDataTable(edoHistories);
    }

}