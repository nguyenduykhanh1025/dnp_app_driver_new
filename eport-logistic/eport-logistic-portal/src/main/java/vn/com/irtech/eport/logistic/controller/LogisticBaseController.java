package vn.com.irtech.eport.logistic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;


/**
 * @author GiapHD
 *
 */
public abstract class LogisticBaseController extends BaseController {
	
	protected static final Logger logger = LoggerFactory.getLogger(LogisticBaseController.class);

	public static final String PHONE_PATTERN = "^[0-9]{10,11}$";

	@Autowired
	ILogisticGroupService logisticGroupService;

	public LogisticAccount getUser() {
		return ShiroUtils.getSysUser();
	}
	
	public Long getUserId() {
		return ShiroUtils.getUserId();
	}
	
	public LogisticGroup getGroup() {
		LogisticAccount user = getUser();
		LogisticGroup group = logisticGroupService.selectLogisticGroupById(user.getGroupId());
		return group;
	}

	public boolean verifyPermission(Long groupId) {
		return getUser().getGroupId().equals(groupId);
	}

	public String getUserIp() {
		return ShiroUtils.getIp();
	}
	
}
