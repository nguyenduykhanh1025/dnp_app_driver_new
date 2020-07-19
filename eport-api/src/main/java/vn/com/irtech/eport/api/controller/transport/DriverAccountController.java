package vn.com.irtech.eport.api.controller.transport;

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

import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.form.DriverInfo;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.system.service.IUserDevicesService;

@RestController
@RequestMapping("/transport/user")
public class DriverAccountController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DriverAccountController.class);

	@Autowired()
	private IDriverAccountService driverAccountService;

	@Autowired
	private IUserDevicesService userDevicesService;

	@Autowired
	private ILogisticTruckService logisticTruckService;

	@PostMapping("/logout")
	public AjaxResult logout(@RequestHeader("Authorization") String token) {
		userDevicesService.deleteUserDevicesByUserToken(token);
		return success();
	}

	@GetMapping("/info")
	public AjaxResult getUserInfo() {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		DriverInfo driverInfo = driverAccountService.selectDriverAccountInfoById(userId);
		driverInfo.setTruckNoList(logisticTruckService.selectListTruckNoByDriverId(userId));
		driverInfo.setChassisNoList(logisticTruckService.selectListChassisNoByDriverId(userId));
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", driverInfo);
		return ajaxResult;
	}

	@PostMapping("/register")
	@ResponseBody
	public AjaxResult register(@RequestBody DriverAccount driverAccountTemp) {
		DriverAccount driverAccount = new DriverAccount();
		driverAccount.setFullName(driverAccountTemp.getFullName());
		driverAccount.setMobileNumber(driverAccountTemp.getMobileNumber());
		return success();
    }
    
    @PostMapping("/resetpwd")
	@ResponseBody
	public AjaxResult resetPassword() {
		return success();
    }
}
