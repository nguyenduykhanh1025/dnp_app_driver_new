package vn.com.irtech.eport.web.controller.system;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysConfig;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * SysConfig Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    private String prefix = "system/config";

    @Autowired
    private ISysConfigService configService;

    @RequiresPermissions("system:config:view")
    @GetMapping()
    public String config()
    {
        return prefix + "/config";
    }

    /**
     * Query parameter configuration list
     */
    @RequiresPermissions("system:config:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "Parameter management", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:config:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "Parameter data");
    }

    /**
     * New parameter configuration
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Added save parameter configuration
     */
    @RequiresPermissions("system:config:add")
    @Log(title = "Parameter management", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysConfig config)
    {
        if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return error("Thêm tham số mới " + config.getConfigName() + " không thành công，tên tham số đã tồn tại!");
        }
        config.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * Modify parameter configuration
     */
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap)
    {
        mmap.put("config", configService.selectConfigById(configId));
        return prefix + "/edit";
    }

    /**
     * Modify save parameter configuration
     */
    @RequiresPermissions("system:config:edit")
    @Log(title = "Parameter management", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysConfig config)
    {
        if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return error("Thay đổi tham số " + config.getConfigName() + " không thành công，tên tham số đã tồn tại!");
        }
        config.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * Delete parameter configuration
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "Parameter management", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(configService.deleteConfigByIds(ids));
    }

    /**
     * Clear cache
     */
    @RequiresPermissions("system:config:remove")
    @Log(title = "Parameter management", businessType = BusinessType.CLEAN)
    @GetMapping("/clearCache")
    @ResponseBody
    public AjaxResult clearCache()
    {
        configService.clearCache();
        return success();
    }

    /**
     * Verify parameter key name
     */
    @PostMapping("/checkConfigKeyUnique")
    @ResponseBody
    public String checkConfigKeyUnique(SysConfig config)
    {
        return configService.checkConfigKeyUnique(config);
    }
}
