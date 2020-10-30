package vn.com.irtech.eport.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.domain.CacheEntity;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.CacheUtils;

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

    @GetMapping()
    public String config()
    {
		return prefix + "/cache";
    }

	@PostMapping("/list")
    @ResponseBody
	public TableDataInfo getCacheList(CacheEntity cacheEntity) {
		return getDataTable(CacheUtils.getAllCacheData(cacheEntity));
    }

	@DeleteMapping("/remove-all")
	@ResponseBody
	public AjaxResult removeAllCache() {
		CacheUtils.removeAll("sys-cache");
		CacheUtils.removeAll(Constants.SYS_CONFIG_CACHE);
		CacheUtils.removeAll(Constants.SYS_DICT_CACHE);
		return success();
	}

	@DeleteMapping("/{keyName}/{key}/remove")
	@ResponseBody
	public AjaxResult removeAllCache(@PathVariable("key") String key, @PathVariable("keyName") String keyName) {
		CacheUtils.remove(keyName, key);
		return success();
	}
}
