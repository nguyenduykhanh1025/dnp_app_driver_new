package vn.com.irtech.eport.web.controller.notification;

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
import vn.com.irtech.eport.system.domain.SysUserToken;
import vn.com.irtech.eport.system.service.ISysUserTokenService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Notify TokenController
 * 
 * @author irtehc
 * @date 2020-08-22
 */
@Controller
@RequestMapping("/system/token")
public class SysUserTokenController extends BaseController
{
    private String prefix = "system/token";

    @Autowired
    private ISysUserTokenService sysUserTokenService;

    @RequiresPermissions("system:token:view")
    @GetMapping()
    public String token()
    {
        return prefix + "/token";
    }

    /**
     * Get Notify Token List
     */
    @RequiresPermissions("system:token:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserToken sysUserToken)
    {
        startPage();
        List<SysUserToken> list = sysUserTokenService.selectSysUserTokenList(sysUserToken);
        return getDataTable(list);
    }

    /**
     * Export Notify Token List
     */
    @RequiresPermissions("system:token:export")
    @Log(title = "Notify Token", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUserToken sysUserToken)
    {
        List<SysUserToken> list = sysUserTokenService.selectSysUserTokenList(sysUserToken);
        ExcelUtil<SysUserToken> util = new ExcelUtil<SysUserToken>(SysUserToken.class);
        return util.exportExcel(list, "token");
    }

    /**
     * Add Notify Token
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存Notify Token
     */
    @RequiresPermissions("system:token:add")
    @Log(title = "Notify Token", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUserToken sysUserToken)
    {
        return toAjax(sysUserTokenService.insertSysUserToken(sysUserToken));
    }

    /**
     * Update Notify Token
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUserToken sysUserToken = sysUserTokenService.selectSysUserTokenById(id);
        mmap.put("sysUserToken", sysUserToken);
        return prefix + "/edit";
    }

    /**
     * Update Save Notify Token
     */
    @RequiresPermissions("system:token:edit")
    @Log(title = "Notify Token", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysUserToken sysUserToken)
    {
        return toAjax(sysUserTokenService.updateSysUserToken(sysUserToken));
    }

    /**
     * Delete Notify Token
     */
    @RequiresPermissions("system:token:remove")
    @Log(title = "Notify Token", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysUserTokenService.deleteSysUserTokenByIds(ids));
    }
}
