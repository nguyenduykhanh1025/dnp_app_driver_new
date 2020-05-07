package vn.com.irtech.eport.logistic.controller;

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
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Logistic GroupController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/group")
public class LogisticGroupController extends BaseController
{
    private String prefix = "logistic/group";

    @Autowired
    private ILogisticGroupService logisticGroupService;

    @RequiresPermissions("logistic:group:view")
    @GetMapping()
    public String group()
    {
        return prefix + "/group";
    }

    /**
     * Get Logistic Group List
     */
    @RequiresPermissions("logistic:group:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogisticGroup logisticGroup)
    {
        startPage();
        List<LogisticGroup> list = logisticGroupService.selectLogisticGroupList(logisticGroup);
        return getDataTable(list);
    }

    /**
     * Export Logistic Group List
     */
    @RequiresPermissions("logistic:group:export")
    @Log(title = "Logistic Group", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LogisticGroup logisticGroup)
    {
        List<LogisticGroup> list = logisticGroupService.selectLogisticGroupList(logisticGroup);
        ExcelUtil<LogisticGroup> util = new ExcelUtil<LogisticGroup>(LogisticGroup.class);
        return util.exportExcel(list, "group");
    }

    /**
     * Add Logistic Group
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Logistic Group
     */
    @RequiresPermissions("logistic:group:add")
    @Log(title = "Logistic Group", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LogisticGroup logisticGroup)
    {
        return toAjax(logisticGroupService.insertLogisticGroup(logisticGroup));
    }

    /**
     * Update Logistic Group
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticGroup logisticGroup = logisticGroupService.selectLogisticGroupById(id);
        mmap.put("logisticGroup", logisticGroup);
        return prefix + "/edit";
    }

    /**
     * Update Save Logistic Group
     */
    @RequiresPermissions("logistic:group:edit")
    @Log(title = "Logistic Group", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LogisticGroup logisticGroup)
    {
        return toAjax(logisticGroupService.updateLogisticGroup(logisticGroup));
    }

    /**
     * Delete Logistic Group
     */
    @RequiresPermissions("logistic:group:remove")
    @Log(title = "Logistic Group", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(logisticGroupService.deleteLogisticGroupByIds(ids));
    }
}
