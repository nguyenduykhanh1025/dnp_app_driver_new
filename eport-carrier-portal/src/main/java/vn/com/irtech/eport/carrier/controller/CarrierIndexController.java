package vn.com.irtech.eport.carrier.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        mmap.put("report", report);
        mmap.put("user", user);
        mmap.put("version", Global.getVersion());
        return "main";
    }

    @GetMapping("/lisCarrierCode")
    @ResponseBody
    public String lisCarrierCode()
    {
        Long groupId = ShiroUtils.getSysUser().getGroupId();
        String operateCode = groupService.getCarrierCodeById(groupId);
        return operateCode;
    }

}
