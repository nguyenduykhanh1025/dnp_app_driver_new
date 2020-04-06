package vn.com.irtech.eport.carrier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * 
 * @author admin
 */
@Controller
public class CarrierIndexController extends BaseController
{

    @Autowired
    private ISysConfigService configService;

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
        mmap.put("version", Global.getVersion());
        return "main";
    }

    @GetMapping("/do/add")
    public String addDO(ModelMap map)
    {
      return "delivery-order/add";
    }
}
