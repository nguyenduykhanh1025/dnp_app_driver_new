package vn.com.irtech.eport.logistic.controller;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;

/**
 * @author GiapHD
 *
 */
public abstract class LogisticBaseController extends BaseController {

	public LogisticAccount getUser() {
		return ShiroUtils.getSysUser();
	}
	
	public Long getUserId() {
		return ShiroUtils.getUserId();
	}
	
}
