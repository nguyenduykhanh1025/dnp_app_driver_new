package vn.com.irtech.eport.api.controller.logistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;

@RestController
@RequestMapping("/logistic/user")
public class LogisticAccountController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LogisticAccountController.class);

	@Autowired()
	private ILogisticAccountService logisticAccountService;

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		LogisticAccount user = logisticAccountService.selectLogisticAccountById(userId);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("user", user);
		return ajaxResult;
	}

}
