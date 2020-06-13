package vn.com.irtech.eport.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.dto.request.LoginForm;
import vn.com.irtech.eport.api.service.ILoginService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;

@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Autowired
	private ILoginService loginService;

	@PostMapping("/transport")
	@ResponseBody
	public AjaxResult loginAdmin(@RequestBody LoginForm loginForm) {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("token", loginService.login(loginForm));
		return ajaxResult;
	}
}
