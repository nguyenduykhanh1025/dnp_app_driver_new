package vn.com.irtech.eport.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;

/**
 * SysConfig Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/cache")
public class SysCacheDataController extends BaseController
{
	private String prefix = "system/cache";

	@Autowired
	private ConfigService configService;

    @GetMapping()
    public String config()
    {
		return prefix + "/cache";
    }

	@GetMapping("/list")
    @ResponseBody
	public AjaxResult getCacheList() {
		AjaxResult ajaxResult = AjaxResult.success();
		configService.getKey("otp.format");
		ajaxResult.put("sysCache", CacheUtils.getAllCacheData());
		return ajaxResult;
    }
}
