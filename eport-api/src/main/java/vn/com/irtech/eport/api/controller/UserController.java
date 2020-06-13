package vn.com.irtech.eport.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.service.impl.UserService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.TransportAccount;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		TransportAccount user = userService.getUserInfor(SecurityUtils.getCurrentUser().getUser().getId());
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("user", user);
		return ajaxResult;
	}

}
