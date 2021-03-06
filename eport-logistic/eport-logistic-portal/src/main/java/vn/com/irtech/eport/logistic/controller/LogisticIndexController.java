package vn.com.irtech.eport.logistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * 
 * @author admin
 */
@Controller
public class LogisticIndexController extends LogisticBaseController
{

    @Autowired
    private ISysConfigService configService;

    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
        LogisticAccount user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("groupName", getGroup().getGroupName());
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        mmap.put("appName", Global.getName());
        mmap.put("appVersion", Global.getVersion());
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("fwdFlg", getGroup().getFwdFlg());
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
        LogisticAccount user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("version", Global.getVersion());
        return "main";
    }

}
