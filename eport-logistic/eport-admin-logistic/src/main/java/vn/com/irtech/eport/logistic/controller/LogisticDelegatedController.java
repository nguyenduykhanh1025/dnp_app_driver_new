package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticDelegated;
import vn.com.irtech.eport.logistic.service.ILogisticDelegatedService;

/**
 * DelegateController
 * 
 * @author Irtech
 * @date 2020-08-14
 */
@Controller
@RequestMapping("/logistic/delegate")
public class LogisticDelegatedController extends BaseController
{
    private String prefix = "logistic/delegate";

    @Autowired
    private ILogisticDelegatedService logisticDelegatedService;

    @Autowired
    private DictService dictService;

    @RequiresPermissions("logistic:delegate:view")
    @GetMapping()
    public String delegate()
    {
        return prefix + "/delegate";
    }

    /**
     * Get Delegate List
     */
    @RequiresPermissions("logistic:group:add")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody PageAble<LogisticDelegated> param)
    {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        LogisticDelegated logisticDelegated = param.getData();
        if (logisticDelegated == null) {
            logisticDelegated = new LogisticDelegated();
        }
        List<LogisticDelegated> list = logisticDelegatedService.selectLogisticDelegatedList(logisticDelegated);
        return getDataTable(list);
    }

    /**
     * Add Delegate
     */
    @GetMapping("/addDelegated")
    public String add(ModelMap mmap)
    {
        mmap.put("delegateTypes", dictService.getType("delegate_type_list"));
        return prefix + "/addDelegated";
    }

    @RequiresPermissions("logistic:group:add")
    @Log(title = "Delegate", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LogisticDelegated logisticDelegated)
    {
        return toAjax(logisticDelegatedService.insertLogisticDelegated(logisticDelegated));
    }

    /**
     * Update Delegate
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticDelegated logisticDelegated = logisticDelegatedService.selectLogisticDelegatedById(id);
        mmap.put("logisticDelegated", logisticDelegated);
        return prefix + "/edit";
    }

    /**
     * Update Save Delegate
     */
    @RequiresPermissions("logistic:group:add")
    @Log(title = "Delegate", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LogisticDelegated logisticDelegated)
    {
        return toAjax(logisticDelegatedService.updateLogisticDelegated(logisticDelegated));
    }

    /**
     * Delete Delegate
     */
    @RequiresPermissions("logistic:group:add")
    @Log(title = "Delegate", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(logisticDelegatedService.deleteLogisticDelegatedByIds(ids));
    }
}
