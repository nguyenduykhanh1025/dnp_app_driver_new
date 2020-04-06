package vn.com.irtech.eport.system.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.system.domain.SysEdi;
import vn.com.irtech.eport.system.service.ISysEdiService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
@Controller
@RequestMapping("/system/edi")
public class SysEdiController extends BaseController
{
    private String prefix = "system/edi";

    @Autowired
    private ISysEdiService sysEdiService;

    @RequiresPermissions("system:edi:view")
    @GetMapping()
    public String edi()
    {
        return prefix + "/edi";
    }

    /**
     * Get 【请填写功能名称】 List
     */
    @RequiresPermissions("system:edi:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysEdi sysEdi)
    {
        startPage();
        List<SysEdi> list = sysEdiService.selectSysEdiList(sysEdi);
        return getDataTable(list);
    }

    /**
     * Export 【请填写功能名称】 List
     */
    @RequiresPermissions("system:edi:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysEdi sysEdi)
    {
        List<SysEdi> list = sysEdiService.selectSysEdiList(sysEdi);
        ExcelUtil<SysEdi> util = new ExcelUtil<SysEdi>(SysEdi.class);
        return util.exportExcel(list, "edi");
    }

    /**
     * Add 【请填写功能名称】
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update 【请填写功能名称】
     */
    @RequiresPermissions("system:edi:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysEdi sysEdi)
    {
        return toAjax(sysEdiService.insertSysEdi(sysEdi));
    }

    /**
     * Update 【请填写功能名称】
     */
    @GetMapping("/edit/{ediId}")
    public String edit(@PathVariable("ediId") Long ediId, ModelMap mmap)
    {
        SysEdi sysEdi = sysEdiService.selectSysEdiById(ediId);
        mmap.put("sysEdi", sysEdi);
        return prefix + "/edit";
    }

    /**
     * Update Save 【请填写功能名称】
     */
    @RequiresPermissions("system:edi:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysEdi sysEdi)
    {
        return toAjax(sysEdiService.updateSysEdi(sysEdi));
    }

    /**
     * Delete 【请填写功能名称】
     */
    @RequiresPermissions("system:edi:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysEdiService.deleteSysEdiByIds(ids));
    }
}
