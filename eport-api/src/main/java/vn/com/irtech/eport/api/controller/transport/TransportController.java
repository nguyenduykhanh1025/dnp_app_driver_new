package vn.com.irtech.eport.api.controller.transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.form.NotificationForm;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@RestController
@RequestMapping("/transport")
public class TransportController extends BaseController {

	@Autowired 
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private ILogisticTruckService logisticTruckService;

	@Autowired
	private IPickupAssignService pickupAssignService;

	@Autowired
	private IDriverAccountService driverAccountService;

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private ISysConfigService configService;
    
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
	
	@GetMapping("/shipments/service-type/{serviceType}")
	@ResponseBody
	public AjaxResult getShipmentList(@PathVariable Integer serviceType) {
		Long logisticGroupId = driverAccountService.selectDriverAccountById(SecurityUtils.getCurrentUser().getUser().getUserId()).getLogisticGroupId();
		startPage();
		if (serviceType == null || serviceType > 4 || serviceType < 1) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0005));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentList", shipmentService.selectShipmentListForDriver(serviceType, logisticGroupId));
		return ajaxResult;
	}
    
    @GetMapping("/shipment/{shipmentId}/pickup")
	@ResponseBody
	public AjaxResult getContainerList(@PathVariable Long shipmentId) {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (shipmentId == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0015));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", pickupAssignService.selectPickupAssignListByDriverId(userId, shipmentId));
		return ajaxResult;
    }
    
    @PostMapping("/pickup")
	@ResponseBody
	public AjaxResult pickup(@RequestBody PickupHistory pickupHistoryTemp) {
		PickupAssign pickupAssign = pickupAssignService.selectPickupAssignById(pickupHistoryTemp.getPickupAssignId());
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (pickupAssign == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
		}
		if (userId != pickupAssign.getDriverId() || pickupHistoryService.checkPickupHistoryExists(pickupAssign.getShipmentId(), pickupHistoryTemp.getContainerNo()) > 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0009));
		}
		Shipment shipment = shipmentService.selectShipmentById(pickupAssign.getShipmentId());
		
		if (!pickupHistoryService.checkPossiblePickup(pickupAssign.getDriverId(), shipment.getServiceType())) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0016));
		}
		PickupHistory pickupHistory = new PickupHistory();
		pickupHistory.setLogisticGroupId(pickupAssign.getLogisticGroupId());
		pickupHistory.setShipmentId(pickupAssign.getShipmentId());
		pickupHistory.setShipmentDetailId(pickupAssign.getShipmentDetailId());
		pickupHistory.setDriverPhoneNumber(pickupAssign.getPhoneNumber());
		pickupHistory.setDriverId(pickupAssign.getDriverId());
		pickupHistory.setPickupAssignId(pickupAssign.getId());
		pickupHistory.setTruckNo(pickupHistoryTemp.getTruckNo());
		pickupHistory.setChassisNo(pickupHistoryTemp.getChassisNo());
		pickupHistory.setContainerNo(pickupHistoryTemp.getContainerNo());
		pickupHistory.setStatus(0);
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
		return ajaxResult;
    }
    
    @PostMapping("/pickup/{pickupHistoryId}/complete")
	@ResponseBody
	public AjaxResult finishPickup(@PathVariable Long pickupHistoryId) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupHistoryId);
		if (pickupHistory == null || !pickupHistory.getDriverId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0017));
		}
		pickupHistory.setStatus(3);
		pickupHistoryService.updatePickupHistory(pickupHistory);
		return success();
    }
    
    @PostMapping("/notify")
	@ResponseBody
	public AjaxResult getNotifyList() {
		startPage();
		List<NotificationForm> notificationForms = new ArrayList<>();
		for (int i=0; i<10; i++) {
			NotificationForm notificationForm = new NotificationForm();
			notificationForm.setNotificationId(Long.valueOf(i));
			notificationForm.setTitle("Title " + i);
			notificationForm.setContent("Content " + i);
			notificationForm.setCreateTime(new Date());
			notificationForms.add(notificationForm);
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("notificationList", notificationForms);
		return ajaxResult;
    }
	
	@PostMapping("/location")
	@ResponseBody
	public AjaxResult updateLocation(@RequestBody Map<String, Double> location) {

		if (location == null) {
			return error();
		}
		CacheUtils.put("driver-" + SecurityUtils.getCurrentUser().getUser().getUserId(), location);
		return success();
	}

	@GetMapping("/mqtt/url")
	@ResponseBody
	public AjaxResult getMqttUrl() {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("mqttUrl", configService.selectConfigByKey("mobile.mqtt.url"));
		return ajaxResult;
	}
}