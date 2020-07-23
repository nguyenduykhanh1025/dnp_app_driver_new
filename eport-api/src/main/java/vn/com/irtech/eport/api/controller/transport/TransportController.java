package vn.com.irtech.eport.api.controller.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@RestController
@RequestMapping("/transport")
public class TransportController extends BaseController {

	@Autowired 
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private ILogisticTruckService logisticTruckService;

	@Autowired
	private IPickupAssignService pickupAssignService;
    
    @GetMapping("/pickup/history")
	@ResponseBody
	public AjaxResult getPickupHistory() {
		startPage();
		AjaxResult ajaxResult = AjaxResult.success();
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		ajaxResult.put("data",  pickupHistoryService.selectPickupHistoryForDriver(userId));
		return ajaxResult;
    }
    
    @GetMapping("/pickup")
	@ResponseBody
	public AjaxResult getPickupList() {
		AjaxResult ajaxResult = AjaxResult.success();
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		ajaxResult.put("data", pickupHistoryService.selectPickupListByDriverId(userId));
		return ajaxResult;
    }
    
    @GetMapping("/truck")
	@ResponseBody
	public AjaxResult getTruckList() {
		AjaxResult ajaxResult = AjaxResult.success();
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		Map<String, List<String>> data = new HashMap<>();
		data.put("truckNoList", logisticTruckService.selectListTruckNoByDriverId(userId));
		data.put("chassisNoList", logisticTruckService.selectListChassisNoByDriverId(userId));
		ajaxResult.put("data", data);
		return ajaxResult;
    }
    
    @GetMapping("/shipment/{serviceType}")
	@ResponseBody
	public AjaxResult getContainerList(@PathVariable Integer serviceType) {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (serviceType == null || serviceType > 4 || serviceType < 1) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0005));
		}
		if (!pickupHistoryService.checkPossiblePickup(userId, serviceType)) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0006));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", pickupAssignService.selectPickupAssignListByDriverId(userId, serviceType));
		return ajaxResult;
    }
    
    @PostMapping("/pickup")
	@ResponseBody
	public AjaxResult getPickup(@RequestBody PickupHistory pickupHistoryTemp) {
		PickupAssign pickupAssign = pickupAssignService.selectPickupAssignById(pickupHistoryTemp.getPickupAssignId());
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (pickupAssign == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
		}
		if (userId != pickupAssign.getDriverId()) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0009));
		}
		PickupHistory pickupHistory = new PickupHistory();
		pickupHistory.setLogisticGroupId(pickupAssign.getLogisticGroupId());
		pickupHistory.setShipmentId(pickupAssign.getShipmentId());
		pickupHistory.setShipmentDetailId(pickupAssign.getShipmentDetailId());
		pickupHistory.setDriverPhoneNumber(pickupAssign.getPhoneNumber());
		pickupHistory.setDriverId(pickupAssign.getDriverId());
		pickupHistory.setPickupAssignId(pickupAssign.getId());
		pickupHistoryService.insertPickupHistory(pickupHistory);
		return success();
    }
    
    @GetMapping("/pickup/{pickupId}")
	@ResponseBody
	public AjaxResult getPickupDetailInfo(@PathVariable Long pickupId) {
		if (pickupId == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0008));
		}
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", pickupHistoryService.selectPickupHistoryDetailById(userId, pickupId));
		return success();
    }
    
    @PostMapping("/pickup/{pickupId}/complete")
	@ResponseBody
	public AjaxResult finishPickup() {
		return success();
    }
    
    @PostMapping("/notify")
	@ResponseBody
	public AjaxResult getNotifyList() {
		return success();
    }
    
    @PostMapping("/notify{id}")
	@ResponseBody
	public AjaxResult updateNotify(@PathVariable Long id) {
		return success();
    }
    
    @GetMapping("/profile")
	@ResponseBody
	public AjaxResult getProfile() {
		return success();
    }
    
    @PostMapping("/profile")
	@ResponseBody
	public AjaxResult updateProfile() {
		return success();
	}
}