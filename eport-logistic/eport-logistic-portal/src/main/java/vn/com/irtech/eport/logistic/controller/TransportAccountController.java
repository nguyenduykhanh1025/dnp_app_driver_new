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
import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.service.ITransportAccountService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Driver login infoController
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
@Controller
@RequestMapping("/logistic/transport")
public class TransportAccountController extends BaseController
{
    private String prefix = "logistic/transport";

    @Autowired
    private ITransportAccountService transportAccountService;

    @GetMapping()
    public String account()
    {
        return prefix + "/index";
    }

    /**
     * Get Driver login info List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TransportAccount transportAccount)
    {
        startPage();
        List<TransportAccount> list = transportAccountService.selectTransportAccountList(transportAccount);
        return getDataTable(list);
    }

    /**
     * Export Driver login info List
     */
    @RequiresPermissions("system:account:export")
    @Log(title = "Driver login info", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TransportAccount transportAccount)
    {
        List<TransportAccount> list = transportAccountService.selectTransportAccountList(transportAccount);
        ExcelUtil<TransportAccount> util = new ExcelUtil<TransportAccount>(TransportAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * Add Driver login info
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Driver login info
     */
    @RequiresPermissions("system:account:add")
    @Log(title = "Driver login info", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TransportAccount transportAccount)
    {
        return toAjax(transportAccountService.insertTransportAccount(transportAccount));
    }

    /**
     * Update Driver login info
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TransportAccount transportAccount = transportAccountService.selectTransportAccountById(id);
        mmap.put("transportAccount", transportAccount);
        return prefix + "/edit";
    }

    /**
     * Update Save Driver login info
     */
    @RequiresPermissions("system:account:edit")
    @Log(title = "Driver login info", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TransportAccount transportAccount)
    {
        return toAjax(transportAccountService.updateTransportAccount(transportAccount));
    }

    /**
     * Delete Driver login info
     */
    @RequiresPermissions("system:account:remove")
    @Log(title = "Driver login info", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(transportAccountService.deleteTransportAccountByIds(ids));
    }
}
