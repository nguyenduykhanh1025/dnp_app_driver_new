package vn.com.irtech.eport.carrier.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * @author GiapHD
 *
 */
public abstract class CarrierBaseController extends BaseController {

	@Autowired
	private ICarrierGroupService groupService;
	
	public CarrierAccount getUser() {
		return ShiroUtils.getSysUser();
	}
	
	public Long getUserId() {
		return ShiroUtils.getUserId();
	}
	
	public List<String> getGroupCodes() {
		return Arrays.asList(getUser().getCarrierCode().split(","));
	}
	
	public CarrierGroup getUserGroup() {
		CarrierAccount user = getUser();
		user.setCarrierGroup(groupService.selectCarrierGroupById(user.getGroupId()));
		return user.getCarrierGroup();
	}

	public boolean hasDoPermission() {
		return getUser().getDoFlg();
	}

	public boolean hasEdoPermission() {
		return getUser().getEdoFlg();
	}

	public boolean hasBookingPermission() {
		return getUser().getBookingFlg();
	}

	public boolean hasConsigneeUpdatePermission() {
		CarrierGroup userGroup = getUserGroup();
		return (userGroup != null && "1".equals(userGroup.getConsigneeFlag()));
	}
	
	public boolean hasDepoPermission() {
		return getUser().getDepoFlg();
	}
}
