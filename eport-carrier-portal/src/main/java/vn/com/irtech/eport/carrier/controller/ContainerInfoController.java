package vn.com.irtech.eport.carrier.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.com.irtech.eport.common.core.page.PageDomain;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.core.page.TableSupport;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.util.ShiroUtils;


/**
 * Container InfomationController
 * 
 * @author Admin
 * @date 2020-04-16
 */
@Controller
@RequestMapping("/carrier/cont")
public class ContainerInfoController extends CarrierBaseController
{
    private String prefix = "carrier/cont";

    @Autowired
    private ICarrierGroupService groupService;

    @GetMapping()
    public String cont()
    {
        return prefix + "/cont";
    }

    @GetMapping("/contFull")
    public String contfull(Model map)
    {
        map.addAttribute("contFE","F");
        return prefix + "/cont";
    }

    @GetMapping("/contEmpty")
    public String contEmpty(Model map)
    {
        map.addAttribute("contFE","E");
        return prefix + "/cont";
    }
    /**
     * Get Container Infomation List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ContainerInfo containerInfo,Date toDate,Date  fromDate,String contFE,String carrierCode,int pageNum,int pageSize)
    {
        startPage();
        // SEARCH CONT 
        Map<String, Object> pageInfo = new HashMap<>();
        if(carrierCode.equals(""))
        {
            pageInfo.put("prntCodes", super.getGroupCodes());
        }
        pageInfo.put("pageNum",(pageNum-1)*pageSize);
        pageInfo.put("pageSize", pageSize);
        containerInfo.setParams(pageInfo);
        containerInfo.setPtnrCode(carrierCode);

        //check carrierCode = null or other value then set carrierCode[0] by user
        
        // if(carrierCode == null)
        // {
        //     containerInfo.setPtnrCode(allCarrierCode[0]);
        // }else {
        //     for (String carrierStr : allCarrierCode) {
        //         if(!carrierCode.equals(carrierStr)){
        //             containerInfo.setPtnrCode(allCarrierCode[0]);
        //         }
        //     }
        //     containerInfo.setPtnrCode(carrierCode);
        // }
        if (contFE.equals("F"))
        {
            containerInfo.setFe("F");
            containerInfo.setCntrState("Y");
        }
        if (contFE.equals("E"))
        {
            containerInfo.setFe("E");
            containerInfo.setCntrState("Y");
        }

        if (toDate != null) {
			containerInfo.setToDate(toDate);
        }
        if (fromDate != null) {
			containerInfo.setFromDate(fromDate);
		}
        
        //List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);

        // Call API
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
    public AjaxResult export(ContainerInfo containerInfo,Date toDate,Date  fromDate,String contFE,String carrierCode) throws IllegalAccessException, InvocationTargetException
    {
        //Cont FE
        
        Map<String, Object> pageInfo = new HashMap<>();
        if(carrierCode.equals(""))
        {
            pageInfo.put("prntCodes", super.getGroupCodes());
        }
        containerInfo.setParams(pageInfo);
        containerInfo.setPtnrCode(carrierCode);
        if (contFE.equals("F"))
        {
            containerInfo.setFe("F");
            containerInfo.setCntrState("Y");
        }
        if (contFE.equals("E"))
        {
            containerInfo.setFe("E");
            containerInfo.setCntrState("Y");
        }
        // SEARCH CONT 
        if (fromDate != null) {
            containerInfo.setFromDate(fromDate);
        }
        if (toDate != null) {
            containerInfo.setToDate(toDate);
        }
        // List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);
        // Call API
     // Call API
        final String uri = Global.getApiUrl() + "/container/export";
     
        RestTemplate restTemplate = new RestTemplate();
        R r = restTemplate.postForObject( uri, containerInfo, R.class);
//        int total = (int) r.get("total");
//        TableDataInfo dataList = getDataTable((List) r.get("data"));
//        dataList.setTotal(total);

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
