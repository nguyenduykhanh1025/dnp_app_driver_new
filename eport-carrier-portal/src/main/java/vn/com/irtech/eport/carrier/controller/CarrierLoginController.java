package vn.com.irtech.eport.carrier.controller;

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
 * Carrier login
 * 
 * @author irtech
 */
@Controller
public class CarrierLoginController extends CarrierBaseController {

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response,
					"{\"code\":\"1\",\"msg\":\"Chưa đăng nhập hoặc hết thời gian, vui lòng đăng nhâp lại để tiếp tục.\"}");
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
			String msg = "Tên đăng nhập hoặc mật khẩu không đúng.";
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

//	@GetMapping("/resetPwd/{userId}")
//	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
//		mmap.put("user", carrierService.selectCarrierAccountById(userId));
//		return prefix + "/profile/resetPwd";
//	}
//
//	@Log(title = "Reset Mật Khẩu", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
//	@RequiresPermissions("system:user:resetPwd")
//	@PostMapping("/resetPwd")
//	@ResponseBody
//	public AjaxResult resetPwdSave(CarrierAccount user) {
//		user.setSalt(ShiroUtils.randomSalt());
//		user.setPassword(passwordService.encryptPassword(user.getEmail(), user.getPassword(), user.getSalt()));
//		if (carrierService.resetUserPwd(user) > 0) {
//			if (ShiroUtils.getUserId() == user.getId()) {
//				ShiroUtils.setSysUser(carrierService.selectCarrierAccountById(user.getId()));
//			}
//			return success();
//		}
//		return error();
//	}

	@GetMapping("/tutorial")
    public String tutorial() {
      return "tutorial";
    }
}
