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
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Logistic accountController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/account")
public class LogisticAccountController extends BaseController
{
    private String prefix = "logistic/account";

    @Autowired
    private ILogisticAccountService logisticAccountService;

    @RequiresPermissions("logistic:account:view")
    @GetMapping()
    public String account()
    {
        return prefix + "/account";
    }

    /**
     * Get Logistic account List
     */
    @RequiresPermissions("logistic:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogisticAccount logisticAccount)
    {
        startPage();
        List<LogisticAccount> list = logisticAccountService.selectLogisticAccountList(logisticAccount);
        return getDataTable(list);
    }

    /**
     * Export Logistic account List
     */
    @RequiresPermissions("logistic:account:export")
    @Log(title = "Logistic account", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LogisticAccount logisticAccount)
    {
        List<LogisticAccount> list = logisticAccountService.selectLogisticAccountList(logisticAccount);
        ExcelUtil<LogisticAccount> util = new ExcelUtil<LogisticAccount>(LogisticAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * Add Logistic account
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Logistic account
     */
    @RequiresPermissions("logistic:account:add")
    @Log(title = "Logistic account", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LogisticAccount logisticAccount)
    {
        return toAjax(logisticAccountService.insertLogisticAccount(logisticAccount));
    }

    /**
     * Update Logistic account
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticAccount logisticAccount = logisticAccountService.selectLogisticAccountById(id);
        mmap.put("logisticAccount", logisticAccount);
        return prefix + "/edit";
    }

    /**
     * Update Save Logistic account
     */
    @RequiresPermissions("logistic:account:edit")
    @Log(title = "Logistic account", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LogisticAccount logisticAccount)
    {
        return toAjax(logisticAccountService.updateLogisticAccount(logisticAccount));
    }

    /**
     * Delete Logistic account
     */
    @RequiresPermissions("logistic:account:remove")
    @Log(title = "Logistic account", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(logisticAccountService.deleteLogisticAccountByIds(ids));
    }
}