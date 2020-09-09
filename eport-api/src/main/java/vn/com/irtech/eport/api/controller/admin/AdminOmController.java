package vn.com.irtech.eport.api.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@RestController
@RequestMapping("/admin")
public class AdminOmController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminOmController.class);
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@PostMapping("/pickup/yard-position")
	public AjaxResult updateYardPosition(@RequestBody PickupHistory pickupHistory) {
		if (pickupHistory.getId() == null) {
			return error("id không được để trống.");
		}
		PickupHistory pickupHistoryOrigin = pickupHistoryService.selectPickupHistoryById(pickupHistory.getId());
		if (pickupHistoryOrigin == null) {
			return error("id không tồn tại.");
		}
		pickupHistoryOrigin.setBlock(pickupHistory.getBlock());
		pickupHistoryOrigin.setBay(pickupHistory.getBay());
		pickupHistoryOrigin.setLine(pickupHistory.getLine());
		pickupHistoryOrigin.setTier(pickupHistory.getTier());
		pickupHistoryOrigin.setArea(pickupHistory.getArea());
		pickupHistoryService.updatePickupHistory(pickupHistoryOrigin);
		return success();
	}
}
