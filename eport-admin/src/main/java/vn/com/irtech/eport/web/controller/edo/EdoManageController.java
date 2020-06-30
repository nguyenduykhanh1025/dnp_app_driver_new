package vn.com.irtech.eport.web.controller.edo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/history")
    public String history()
    {
        return PREFIX + "/history";
    }

    @GetMapping("/getHistory")
    @ResponseBody
    public TableDataInfo getHistory()
    {
        startPage();
        EdoHistory edoHistory = new EdoHistory();
        List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
        TableDataInfo dataInfo = getDataTable(edoHistories);
        return dataInfo;
    }

}