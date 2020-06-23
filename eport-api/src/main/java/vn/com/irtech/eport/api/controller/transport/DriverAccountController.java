package vn.com.irtech.eport.api.controller.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;

@RestController
@RequestMapping("/transport/user")
public class DriverAccountController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DriverAccountController.class);

	@Autowired()
	private IDriverAccountService driverAccountService;

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		DriverAccount user = driverAccountService.selectDriverAccountById(userId);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", user);
		return ajaxResult;
	}
}
