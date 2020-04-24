package vn.com.irtech.eport.carrier.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.carrier.domain.ContainerInfo;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.utils.R;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;


/**
 * Container InfomationController
 * 
 * @author Admin
 * @date 2020-04-16
 */
@Controller
@RequestMapping("/carrier/cont")
public class ContainerInfoController extends CarrierBaseController {
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
     * Get Container Infomation List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ContainerInfo containerInfo, String toDate, String fromDate, String contFE,
            String carrierCode, int pageNum, int pageSize,String orderByColumn,String isAsc, String cntrNo) {
        startPage();
        // SEARCH CONT
        Map<String, Object> pageInfo = new HashMap<>();
        if (carrierCode.equals("") || carrierCode == null) {
            pageInfo.put("prntCodes", super.getGroupCodes());
        }else {
            for (String carrierStr : super.getGroupCodes()) {
                if(!carrierCode.equals(carrierStr)){
                    pageInfo.put("prntCodes", super.getGroupCodes());
                }
            }
        }
        if(orderByColumn != null && isAsc != null)
        {
            pageInfo.put("orderByColumn",orderByColumn);
            pageInfo.put("isAsc",isAsc);
        }
        pageInfo.put("pageNum", (pageNum - 1) * pageSize);
        pageInfo.put("pageSize", pageSize);
        containerInfo.setParams(pageInfo);
        containerInfo.setPtnrCode(carrierCode);
        containerInfo.setCntrState("D");
        if(cntrNo!= null)
        {
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
        }else {
            toDate = "";
            containerInfo.setToDate(toDate);
        }
        
        if (contFE.equals("F")) {
            containerInfo.setFe("F");
            containerInfo.setCntrState("Y");
            containerInfo.setToDate("");
            containerInfo.setFromDate("");
        }
        if (contFE.equals("E")) {
            containerInfo.setFe("E");
            containerInfo.setCntrState("Y");
            containerInfo.setToDate("");
            containerInfo.setFromDate("");
        }
        final String uri = Global.getApiUrl() + "/container/list";
        RestTemplate restTemplate = new RestTemplate();
        R r = restTemplate.postForObject( uri, containerInfo, R.class);
        int total = (int) r.get("total");
        TableDataInfo dataList = getDataTable((List) r.get("data"));
        dataList.setTotal(total);
        return dataList;
    }

    /**
     * Export Container Infomation List
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @Log(title = "Container Infomation", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ContainerInfo containerInfo,String toDate,String  fromDate,String contFE,String carrierCode,String orderByColumn,String isAsc, String cntrNo) throws IllegalAccessException, InvocationTargetException
    {
        //Cont FE
        
        Map<String, Object> pageInfo = new HashMap<>();
        if (carrierCode.equals("") || carrierCode == null) {
            pageInfo.put("prntCodes", super.getGroupCodes());
        }else {
            for (String carrierStr : super.getGroupCodes()) {
                if(!carrierCode.equals(carrierStr)){
                    pageInfo.put("prntCodes", super.getGroupCodes());
                }
            }
        }
        containerInfo.setParams(pageInfo);
        containerInfo.setPtnrCode(carrierCode);
        containerInfo.setCntrState("D");
        if(cntrNo!= null)
        {
            containerInfo.setCntrNo(cntrNo.toLowerCase());
        }
        
        // SEARCH CONT 
        if (fromDate != null) {
            containerInfo.setFromDate(fromDate);
        }else {
            fromDate = "";
            containerInfo.setFromDate(fromDate);
        }
        if (toDate != null) {
            containerInfo.setToDate(toDate);
        }else {
            toDate = "";
            containerInfo.setToDate(toDate);
        }
        if (contFE.equals("F")) {
            containerInfo.setFe("F");
            containerInfo.setCntrState("Y");
            containerInfo.setToDate("");
            containerInfo.setFromDate("");
        }
        if (contFE.equals("E")) {
            containerInfo.setFe("E");
            containerInfo.setCntrState("Y");
            containerInfo.setToDate("");
            containerInfo.setFromDate("");
        }
        if(orderByColumn != null && isAsc != null)
        {
            pageInfo.put("orderByColumn",orderByColumn);
            pageInfo.put("isAsc",isAsc);
        }
        final String uri = Global.getApiUrl() + "/container/export";
     
        RestTemplate restTemplate = new RestTemplate();
        R r = restTemplate.postForObject( uri, containerInfo, R.class);
        List<Map<String, Object>> listJson = (List) r.get("data");
        List<ContainerInfo> list = new ArrayList<ContainerInfo>();
        
       
        ContainerInfo ctnr = null;
        // convert to list entity before export
        for(Map<String, Object> item : listJson) {
        	ctnr = new ContainerInfo();
        	BeanUtils.copyProperties(ctnr, item);
            list.add(ctnr);
            
        }
        ExcelUtil<ContainerInfo> util = new ExcelUtil<ContainerInfo>(ContainerInfo.class);
        return util.exportExcel(list, "cont");
    }
    
    @GetMapping("/listCarrierCode")
    @ResponseBody
    public List<String> lisCarrierCode()
    {
        return super.getGroupCodes();
    }

   
}
