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
import vn.com.irtech.eport.system.domain.SysEdiHistory;
import vn.com.irtech.eport.system.service.ISysEdiHistoryService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * SysEdiHistoryController
 */
@Controller
@RequestMapping("/system/history")
public class SysEdiHistoryController extends BaseController
{
    private String prefix = "system/history";

    @Autowired
    private ISysEdiHistoryService sysEdiHistoryService;

    @RequiresPermissions("system:history:view")
    @GetMapping()
    public String history()
    {
        return prefix + "/history";
    }

    /**
     * Get data table List
     */
    @RequiresPermissions("system:history:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysEdiHistory sysEdiHistory)
    {
        startPage();
        List<SysEdiHistory> list = sysEdiHistoryService.selectSysEdiHistoryList(sysEdiHistory);
        return getDataTable(list);
    }

    /**
     * Export SysEdiHistoryList
     */
    @RequiresPermissions("system:history:export")
    @Log(title = "Export SysEdiHistoryList", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysEdiHistory sysEdiHistory)
    {
        List<SysEdiHistory> list = sysEdiHistoryService.selectSysEdiHistoryList(sysEdiHistory);
        ExcelUtil<SysEdiHistory> util = new ExcelUtil<SysEdiHistory>(SysEdiHistory.class);
        return util.exportExcel(list, "history");
    }

    /**
     * Return prefix + /add
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add SysEdiHistory
     */
    @RequiresPermissions("system:history:add")
    @Log(title = "Add SysEdiHistory", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysEdiHistory sysEdiHistory)
    {
        return toAjax(sysEdiHistoryService.insertSysEdiHistory(sysEdiHistory));
    }

    /**
     * Update SysEdiHistory
     */
    @GetMapping("/edit/{ediId}")
    public String edit(@PathVariable("ediId") Long ediId, ModelMap mmap)
    {
        SysEdiHistory sysEdiHistory = sysEdiHistoryService.selectSysEdiHistoryById(ediId);
        mmap.put("sysEdiHistory", sysEdiHistory);
        return prefix + "/edit";
    }

    /**
     * Update Save SysEdiHistory
     */
    @RequiresPermissions("system:history:edit")
    @Log(title = "Update Save SysEdiHistory", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysEdiHistory sysEdiHistory)
    {
        return toAjax(sysEdiHistoryService.updateSysEdiHistory(sysEdiHistory));
    }

    /**
     * Delete SysEdiHistory
     */
    @RequiresPermissions("system:history:remove")
    @Log(title = "Delete SysEdiHistory", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysEdiHistoryService.deleteSysEdiHistoryByIds(ids));
    }
}
