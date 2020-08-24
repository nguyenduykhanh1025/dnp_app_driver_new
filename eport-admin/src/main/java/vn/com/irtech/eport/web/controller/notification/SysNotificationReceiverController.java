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
//import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
//import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
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
//public class SysNotificationReceiverController extends BaseController
//{
//    private String prefix = "system/notify";
//
//    @Autowired
//    private ISysNotificationReceiverService sysNotificationReceiverService;
//
//    @RequiresPermissions("system:notify:view")
//    @GetMapping()
//    public String receiver()
//    {
//        return prefix + "/notify";
//    }
//
//    /**
//     * Get Notification List
//     */
//    @RequiresPermissions("system:notify:view")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(SysNotificationReceiver sysNotificationReceiver)
//    {
//        startPage();
//        List<SysNotificationReceiver> list = sysNotificationReceiverService.selectSysNotificationReceiverList(sysNotificationReceiver);
//        return getDataTable(list);
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
//    public AjaxResult addSave(SysNotificationReceiver sysNotificationReceiver)
//    {
//        return toAjax(sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver));
//    }
//
//    /**
//     * Update Notification
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        SysNotificationReceiver sysNotificationReceiver = sysNotificationReceiverService.selectSysNotificationReceiverById(id);
//        mmap.put("sysNotificationReceiver", sysNotificationReceiver);
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
//    public AjaxResult editSave(SysNotificationReceiver sysNotificationReceiver)
//    {
//        return toAjax(sysNotificationReceiverService.updateSysNotificationReceiver(sysNotificationReceiver));
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
//        return toAjax(sysNotificationReceiverService.deleteSysNotificationReceiverByIds(ids));
//    }
//}
