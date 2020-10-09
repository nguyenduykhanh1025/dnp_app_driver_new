package vn.com.irtech.eport.logistic.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.common.utils.StringUtils;

/**
 * Logistic login
 * 
 * @author irtech
 */
@Controller
public class LogisticLoginController extends LogisticBaseController {

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

	@GetMapping("/tutorial")
    public String tutorial() {
      return "tutorial";
	}
	
}
