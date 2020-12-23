package vn.com.irtech.eport.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysMenu;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysMenuService;

/**
 * Index controller
 * 
 * @author admin
 */
@Controller
public class SysIndexController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private IEquipmentDoService doService;
    
    @Autowired
    private IProcessOrderService processOrderService;

    // System Home
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        // Get identity information
        SysUser user = ShiroUtils.getSysUser();
        // Take out the menu based on the user id
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("appName", Global.getName());
        mmap.put("appVersion", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled", Global.isDemoEnabled());
        
        Map<String, Long> report = processOrderService.getSupportNumberReportForOm();
        mmap.put("report", report);
        
        Map<String, Long> reportReefer = processOrderService.getSupportNumberReportForContReefer();
        mmap.put("reportReefer", reportReefer);
        
        return "index";
    }
    
    // Get report number for om
    @GetMapping("/report/count")
    @ResponseBody
    public AjaxResult getReportNumber() {
    	AjaxResult ajaxResult = AjaxResult.success();
    	Map<String, Long> report = processOrderService.getSupportNumberReportForOm();
    	ajaxResult.put("report", report);
    	return ajaxResult;
    }
    
    // Get report number for cont reefer
    @GetMapping("/report-reefer/count")
    @ResponseBody
    public AjaxResult getReportNumberForContReefer() {
    	AjaxResult ajaxResult = AjaxResult.success();
    	Map<String, Long> reportReefer = processOrderService.getSupportNumberReportForContReefer();
    	ajaxResult.put("reportReefer", reportReefer);
    	return ajaxResult;
    }

    // switch theme
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }

    // system introduction
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
    	SysUser user = ShiroUtils.getSysUser();
        Map<String, String> report = doService.getReportForAdmin();
        if (report == null || report.isEmpty()) {
            report = new HashMap<>();
            report.put("TOTALBL", "0");
            report.put("TOTALCONT", "0");
            report.put("COMPLETEDBL", "0");
            report.put("WAITINGBL", "0");
        }
	    mmap.put("report", report);
	    mmap.put("user", user);
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
