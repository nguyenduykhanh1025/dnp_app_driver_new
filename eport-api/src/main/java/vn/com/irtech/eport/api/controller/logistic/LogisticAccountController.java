package vn.com.irtech.eport.api.controller.logistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.form.LogisticUser;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.system.service.ISysUserTokenService;

@RestController
@RequestMapping("/logistic/user")
public class LogisticAccountController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticAccountController.class);
	
	@Autowired
	private ISysUserTokenService userTokenService;

	@PostMapping("/logout")
	public AjaxResult logout(@RequestHeader("Authorization") String token) {
		userTokenService.deleteUserTokenByUserToken(token);
		return success();
	}
	
	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		LogisticAccount user = getLogisticAccount();
		LogisticUser logisticUser = new LogisticUser();
		BeanUtils.copyBeanProp(logisticUser, user);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("user", logisticUser);
		return ajaxResult;
	}
	
	@Log(title = "Đăng ký logistic", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/register")
	@ResponseBody
	public AjaxResult register(@RequestBody DriverAccount driverAccountTemp) {
		logger.info("Đăng ký thành công.");
		return success();
    }
    
	@Log(title = "Reset mật khẩu logistic", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/resetpwd")
	@ResponseBody
	public AjaxResult resetPassword(String phoneNumber) {
		logger.info("Thiết lập mật khẩu thành công.");
		return success();
    }
	
}
