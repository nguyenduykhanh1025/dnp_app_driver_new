package vn.com.irtech.eport.logistic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.shiro.service.SysPasswordService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;

/**
 * Logistic login
 * 
 * @author irtech
 */
@Controller
public class LogisticLoginController extends LogisticBaseController {

	private String prefix = "logistic";
	
	@Autowired
	private ILogisticAccountService logisticService;
	
	@Autowired
	private SysPasswordService passwordService;

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response,
					"{\"code\":\"1\",\"msg\":\"Not logged in or timed out. please login again\"}");
		}

		return "login";
	}
	
	@PostMapping("/login")
	@ResponseBody
	public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return success();
		} catch (AuthenticationException e) {
			String msg = "User or password is wrong";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
			return error(msg);
		}
	}

	@GetMapping("/unauth")
	public String unauth() {
		return "error/unauth";
	}

	@Log(title = "Logistic Reset password", businessType = BusinessType.UPDATE)
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", logisticService.selectLogisticAccountById(userId));
		return prefix + "/profile/resetPwd";
	}

	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "Reset password", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwdSave(LogisticAccount user) {
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getUserName(), user.getPassword(), user.getSalt()));
//		if (logisticService.resetUserPwd(user) > 0) {
//			if (ShiroUtils.getUserId() == user.getId()) {
//				ShiroUtils.setSysUser(logisticService.selectLogisticAccountById(user.getId()));
//			}
//			return success();
//		}
		return error();
	}
	@GetMapping("/tutorial")
    public String tutorial() {
      return "tutorial";
    }
}
