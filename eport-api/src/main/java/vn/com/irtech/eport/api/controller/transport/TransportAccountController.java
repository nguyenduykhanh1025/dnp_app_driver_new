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
import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.service.ITransportAccountService;

@RestController
@RequestMapping("/transport/user")
public class TransportAccountController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(TransportAccountController.class);

	@Autowired()
	private ITransportAccountService transportAccountService;

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		TransportAccount user = transportAccountService.selectTransportAccountById(userId);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", user);
		return ajaxResult;
	}
}
