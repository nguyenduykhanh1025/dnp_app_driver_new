package vn.com.irtech.eport.api.controller.logistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.domain.EportUser;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;

@RestController
@RequestMapping("/logistic/send-e")
public class LogisticBaseController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticBaseController.class);
	
	@Autowired
	private ILogisticAccountService logisticAccountService;
	
	public LogisticAccount getLogisticAccount() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		return logisticAccountService.selectLogisticAccountById(userId);
	}
	
	public Long getGroupLogisticId() {
		return getLogisticAccount().getGroupId();
	}
	
	public Long getUserId() {
		return SecurityUtils.getCurrentUser().getUser().getUserId();
	}
	
	public EportUser getUser() {
		return SecurityUtils.getCurrentUser().getUser();
	}
}
