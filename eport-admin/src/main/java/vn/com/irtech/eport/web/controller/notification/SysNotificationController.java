//package vn.com.irtech.eport.web.controller.notification;
//
//import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import vn.com.irtech.eport.common.annotation.Log;
//import vn.com.irtech.eport.common.enums.BusinessType;
//import vn.com.irtech.eport.system.domain.SysNotification;
//import vn.com.irtech.eport.system.service.ISysNotificationService;
//import vn.com.irtech.eport.common.core.controller.BaseController;
//import vn.com.irtech.eport.common.core.domain.AjaxResult;
//import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
//import vn.com.irtech.eport.common.core.page.TableDataInfo;
//
///**
// * NotificationController
// * 
// * @author Irtech
// * @date 2020-08-22
// */
//@Controller
//@RequestMapping("/system/notify")
//public class SysNotificationController extends BaseController
//{
//    private String prefix = "system/notify";
//
//    @Autowired
//    private ISysNotificationService sysNotificationService;
//
//    @RequiresPermissions("system:notify:view")
//    @GetMapping()
//    public String notification()
//    {
//        return prefix + "/notify";
//    }
//
//    /**
//     * Get Notification List
//     */
//    @RequiresPermissions("system:notify:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(SysNotification sysNotification)
//    {
//        startPage();
//        List<SysNotification> list = sysNotificationService.selectSysNotificationList(sysNotification);
//        return getDataTable(list);
//    }
//
//    /**
//     * Export Notification List
//     */
//    @RequiresPermissions("system:notify:export")
//    @Log(title = "Notification", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(SysNotification sysNotification)
//    {
//        List<SysNotification> list = sysNotificationService.selectSysNotificationList(sysNotification);
//        ExcelUtil<SysNotification> util = new ExcelUtil<SysNotification>(SysNotification.class);
//        return util.exportExcel(list, "notify");
//    }
//
//    /**
//     * Add Notification
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存Notification
//     */
//    @RequiresPermissions("system:notify:add")
//    @Log(title = "Notification", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(SysNotification sysNotification)
//    {
//        return toAjax(sysNotificationService.insertSysNotification(sysNotification));
//    }
//
//    /**
//     * Update Notification
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        SysNotification sysNotification = sysNotificationService.selectSysNotificationById(id);
//        mmap.put("sysNotification", sysNotification);
//        return prefix + "/edit";
//    }
//
//    /**
//     * Update Save Notification
//     */
//    @RequiresPermissions("system:notify:edit")
//    @Log(title = "Notification", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(SysNotification sysNotification)
//    {
//        return toAjax(sysNotificationService.updateSysNotification(sysNotification));
//    }
//
//    /**
//     * Delete Notification
//     */
//    @RequiresPermissions("system:notify:remove")
//    @Log(title = "Notification", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(sysNotificationService.deleteSysNotificationByIds(ids));
//    }
//}
