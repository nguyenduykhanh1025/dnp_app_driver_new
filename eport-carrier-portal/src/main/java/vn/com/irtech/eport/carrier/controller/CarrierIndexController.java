package vn.com.irtech.eport.carrier.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.service.ISysConfigService;



/**
 * 
 * @author admin
 */
@Controller
public class CarrierIndexController extends CarrierBaseController
{

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IEquipmentDoService doService;
    @Autowired
    private ICarrierGroupService groupService;

    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("copyrightYear", Global.getCopyrightYear());
        if (hasDoPermission()) {
            mmap.put("doPermission", true);
        } else {
            mmap.put("doPermission", false);
        }
        if (hasEdoPermission()) {
            mmap.put("edoPermission", true);
        } else {
            mmap.put("edoPermission", false);
        }
        return "index";
    }
    @GetMapping("/switchSkin")
    public String switchSkin(ModelMap mmap)
    {
        return "skin";
    }

    @GetMapping("/main")
    public String main(ModelMap mmap)
    {
    	CarrierAccount user = ShiroUtils.getSysUser();
    	String opeCodes = groupService.selectCarrierGroupById(user.getGroupId()).getOperateCode();
        Map<String, String> report = doService.getReportByCarrierGroup(opeCodes.split(","));
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
