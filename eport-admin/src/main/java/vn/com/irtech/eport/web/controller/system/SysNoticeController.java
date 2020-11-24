package vn.com.irtech.eport.web.controller.system;

import java.util.List;

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
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysNotice;
import vn.com.irtech.eport.system.service.ISysNoticeService;

/**
 * Notification formController
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    private String prefix = "system/notice";

    @Autowired
    private ISysNoticeService sysNoticeService;

    @GetMapping()
    public String notice()
    {
        return prefix + "/notice";
    }

    /**
     * Get Notification form List
     */
    @PostMapping("/list")
    @ResponseBody
	public TableDataInfo list(@RequestBody PageAble<SysNotice> param)
    {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		SysNotice sysNotice = param.getData();
		if (sysNotice == null) {
			sysNotice = new SysNotice();
		}
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        return getDataTable(list);
    }

    /**
     * Export Notification form List
     */
    @Log(title = "Notification form", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysNotice sysNotice)
    {
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        return util.exportExcel(list, "notice");
    }

    /**
     * Add Notification form
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
	 * Save Notification form
	 */
    @Log(title = "Notification form", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
	public AjaxResult addSave(@RequestBody SysNotice sysNotice)
    {
		sysNotice.setCreateBy(ShiroUtils.getSysUser().getLoginName());
        return toAjax(sysNoticeService.insertSysNotice(sysNotice));
    }

    /**
     * Update Notification form
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap)
    {
        SysNotice sysNotice = sysNoticeService.selectSysNoticeById(noticeId);
        mmap.put("sysNotice", sysNotice);
        return prefix + "/edit";
    }

    /**
     * Update Save Notification form
     */
    @Log(title = "Notification form", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
	public AjaxResult editSave(@RequestBody SysNotice sysNotice)
    {
		sysNotice.setUpdateBy(ShiroUtils.getSysUser().getLoginName());
        return toAjax(sysNoticeService.updateSysNotice(sysNotice));
    }

    /**
     * Delete Notification form
     */
    @Log(title = "Notification form", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysNoticeService.deleteSysNoticeByIds(ids));
    }
}
