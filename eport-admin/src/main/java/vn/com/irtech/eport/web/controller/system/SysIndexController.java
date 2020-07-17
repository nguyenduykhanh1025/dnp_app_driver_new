package vn.com.irtech.eport.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
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
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("demoEnabled", Global.isDemoEnabled());
        return "index";
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
        if (report == null) {
            report = new HashMap<>();
            report.put("totalBl", "0");
            report.put("totalCont", "0");
            report.put("completedBl", "0");
            report.put("waitingBl", "0");
        }
	    mmap.put("report", report);
	    mmap.put("user", user);
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
