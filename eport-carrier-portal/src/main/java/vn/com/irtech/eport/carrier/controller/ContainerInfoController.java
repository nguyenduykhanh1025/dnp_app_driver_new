package vn.com.irtech.eport.carrier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    /**
     * Get Container Infomation List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ContainerInfo containerInfo)
    {
        startPage();
        List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);
        return getDataTable(list);
    }

    /**
     * Export Container Infomation List
     */
    @Log(title = "Container Infomation", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ContainerInfo containerInfo)
    {
        List<ContainerInfo> list = containerInfoService.selectContainerInfoList(containerInfo);
        ExcelUtil<ContainerInfo> util = new ExcelUtil<ContainerInfo>(ContainerInfo.class);
        return util.exportExcel(list, "cont");
    }

}
