package vn.com.irtech.eport.api.controller.logistic;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.domain.EportUserType;
import vn.com.irtech.eport.api.form.LoginReq;
import vn.com.irtech.eport.api.security.service.LoginService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.system.domain.SysUserToken;
import vn.com.irtech.eport.system.service.ISysUserTokenService;

@RestController
@RequestMapping("/logistic")
public class LogisticLoginController extends BaseController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ISysUserTokenService sysUserTokenService;

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult login(@RequestBody LoginReq loginForm) {
		AjaxResult ajaxResult = AjaxResult.success();
		String token = loginService.login(loginForm, EportUserType.LOGISTIC);
		ajaxResult.put("token", token);
		SysUserToken sysUserToken = new SysUserToken();
		sysUserToken.setUserLoginToken(token);		
		// Set firebase device token for notification
		sysUserToken.setDeviceToken(loginForm.getDeviceToken());		
		// Set login ip user
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
		sysUserToken.setLoginIp(request.getRemoteAddr());		
		sysUserToken.setUserType(BusinessConsts.LOGISTIC_USER_TYPE);
		sysUserToken.setUserId(SecurityUtils.getCurrentUser().getUser().getUserId());
		sysUserToken.setExpireTime(TokenUtils.getExpirationDate());
		sysUserTokenService.insertSysUserToken(sysUserToken);
		return ajaxResult;
	}
}
