package vn.com.irtech.eport.carrier.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.ContainerInfo;
import vn.com.irtech.eport.carrier.service.IContainerInfoService;
import vn.com.irtech.eport.common.annotation.Log;
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
public class ContainerInfoController extends CarrierBaseController
{
    private String prefix = "carrier/cont";

    @Autowired
    private IContainerInfoService containerInfoService;

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
    public TableDataInfo list(ContainerInfo containerInfo,Date toDate,Date  fromDate,String contFE,String carrierCode)
    {
        startPage();
        // SEARCH CONT 
        containerInfo.setPtnrCode(carrierCode);
        if (contFE.equals("F"))
        {
            containerInfo.setFe("F");
            containerInfo.setCntrState("D");
        }
        if (contFE.equals("E"))
        {
            containerInfo.setFe("E");
            containerInfo.setCntrState("D");
        }

        if (toDate != null) {
			containerInfo.setToDate(toDate);
        }
        if (fromDate != null) {
			containerInfo.setFromDate(fromDate);
		}
        
        List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);
        return getDataTable(list);
    }

    /**
     * Export Container Infomation List
     */
    @Log(title = "Container Infomation", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ContainerInfo containerInfo,Date toDate,Date  fromDate,String contFE,String carrierCode)
    {
        //Cont FE
        containerInfo.setPtnrCode(carrierCode);
        if (contFE.equals("F"))
        {
            containerInfo.setFe("F");
            containerInfo.setCntrState("D");
        }
        if (contFE.equals("E"))
        {
            containerInfo.setFe("E");
            containerInfo.setCntrState("D");
        }
        // SEARCH CONT 
        if (fromDate != null) {
            containerInfo.setFromDate(fromDate);
        }
        if (toDate != null) {
            containerInfo.setToDate(toDate);
        }
        List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);
        
        
        ExcelUtil<ContainerInfo> util = new ExcelUtil<ContainerInfo>(ContainerInfo.class);
        return util.exportExcel(list, "cont");
    }

}
