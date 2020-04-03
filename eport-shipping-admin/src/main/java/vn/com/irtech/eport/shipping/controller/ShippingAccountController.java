package vn.com.irtech.eport.shipping.controller;

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
import vn.com.irtech.eport.shipping.domain.ShippingAccount;
import vn.com.irtech.eport.shipping.service.IShippingAccountService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Shipping line accountController
 * 
 * @author Irtech
 * @date 2020-04-03
 */
@Controller
@RequestMapping("/shipping/account")
public class ShippingAccountController extends BaseController
{
    private String prefix = "shipping/account";

    @Autowired
    private IShippingAccountService shippingAccountService;

    @RequiresPermissions("shipping:account:view")
    @GetMapping()
    public String account()
    {
        return prefix + "/account";
    }

    /**
     * Get Shipping line account List
     */
    @RequiresPermissions("shipping:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ShippingAccount shippingAccount)
    {
        startPage();
        List<ShippingAccount> list = shippingAccountService.selectShippingAccountList(shippingAccount);
        return getDataTable(list);
    }

    /**
     * Export Shipping line account List
     */
    @RequiresPermissions("shipping:account:export")
    @Log(title = "Shipping line account", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ShippingAccount shippingAccount)
    {
        List<ShippingAccount> list = shippingAccountService.selectShippingAccountList(shippingAccount);
        ExcelUtil<ShippingAccount> util = new ExcelUtil<ShippingAccount>(ShippingAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * Add Shipping line account
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Shipping line account
     */
    @RequiresPermissions("shipping:account:add")
    @Log(title = "Shipping line account", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ShippingAccount shippingAccount)
    {
        return toAjax(shippingAccountService.insertShippingAccount(shippingAccount));
    }

    /**
     * Update Shipping line account
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ShippingAccount shippingAccount = shippingAccountService.selectShippingAccountById(id);
        mmap.put("shippingAccount", shippingAccount);
        return prefix + "/edit";
    }

    /**
     * Update Save Shipping line account
     */
    @RequiresPermissions("shipping:account:edit")
    @Log(title = "Shipping line account", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ShippingAccount shippingAccount)
    {
        return toAjax(shippingAccountService.updateShippingAccount(shippingAccount));
    }

    /**
     * Delete Shipping line account
     */
    @RequiresPermissions("shipping:account:remove")
    @Log(title = "Shipping line account", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(shippingAccountService.deleteShippingAccountByIds(ids));
    }
}
