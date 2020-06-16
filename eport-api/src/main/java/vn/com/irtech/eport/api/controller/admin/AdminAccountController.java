package vn.com.irtech.eport.api.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.service.ISysUserService;

@RestController
@RequestMapping("/admin/user")
public class AdminAccountController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(AdminAccountController.class);

	@Autowired()
	private ISysUserService sysUserService;

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		SysUser user = sysUserService.selectUserById(userId);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("user", user);
		return ajaxResult;
	}
}
