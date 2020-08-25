package vn.com.irtech.eport.api.controller.transport;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.dto.NotificationRes;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;

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
	private IShipmentService shipmentService;

	@Autowired
	private ISysConfigService configService;

	@Autowired 
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private ISysNotificationReceiverService sysNotificationReceiverService;
	
	/**
	 * Get pickup history
	 * 
	 * @return AjaxResult
	 */
    @GetMapping("/pickup/history")
	@ResponseBody
	public AjaxResult getPickupHistory() {
		startPage();
		AjaxResult ajaxResult = AjaxResult.success();
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		ajaxResult.put("data",  pickupHistoryService.selectPickupHistoryForDriver(userId));
		return ajaxResult;
    }
	
	/**
	 * Get pickup that waiting for deliver or finish
	 * 
	 * @return AjaxResult
	 */
    @GetMapping("/pickup")
	@ResponseBody
	public AjaxResult getPickupList() {
		AjaxResult ajaxResult = AjaxResult.success();
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		ajaxResult.put("data", pickupHistoryService.selectPickupListByDriverId(userId));
		return ajaxResult;
    }
	
	/**
	 * Get truck list (truck no list and chassis no lsit)
	 * 
	 * @return AjaxResult
	 */
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
	
	/**
	 * Get Shipment List
	 * 
	 * @param serviceType
	 * @return AjaxResult
	 */
	@GetMapping("/shipments/service-type/{serviceType}")
	@ResponseBody
	public AjaxResult getShipmentList(@PathVariable Integer serviceType) {
		startPage();
		if (serviceType == null || serviceType > 4 || serviceType < 1) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0005));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentList", shipmentService.selectShipmentListForDriver(serviceType, SecurityUtils.getCurrentUser().getUser().getUserId()));
		return ajaxResult;
	}
	
	/**
	 * Get pick up assign list for driver by shipment id
	 * 
	 * @param shipmentId
	 * @return AjaxResult
	 */
    @GetMapping("/shipment/{shipmentId}/pickup")
	@ResponseBody
	public AjaxResult getContainerList(@PathVariable Long shipmentId) {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (shipmentId == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0015));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		List<PickupAssignForm> pickupAssigns = shipmentDetailService.selectShipmentDetailForDriverShipmentAssign(shipmentId, userId);
		if (CollectionUtils.isEmpty(pickupAssigns)) {
			pickupAssigns = pickupAssignService.selectPickupAssignListByDriverId(userId, shipmentId);
		}
		if (shipment.getServiceType() % 2 == 1 && CollectionUtils.isNotEmpty(pickupAssigns)) {
			List<PickupAssignForm> pickupAssign = pickupAssignService.selectPickupAssignListByDriverId(userId, shipmentId);
			if (CollectionUtils.isNotEmpty(pickupAssign)) {
				for (PickupAssignForm pickupAssignForm1 : pickupAssigns) {
					pickupAssignForm1.setClickable(false);
					for (PickupAssignForm pickupAssignForm2 : pickupAssign) {
						if (pickupAssignForm1.getShipmentDetailId().equals(pickupAssignForm2.getShipmentDetailId())) {
							pickupAssignForm1.setClickable(true);
						}
					}
				}
			} else {
				for (PickupAssignForm assignForm : pickupAssigns) {
					assignForm.setClickable(false);
				}
			}
		
		}
		Collections.sort(pickupAssigns, Collections.reverseOrder(new AssignContComparator()));
		ajaxResult.put("data", pickupAssigns);
		return ajaxResult;
    }
	
	/**
	 * Make a pickup to deliver
	 * 
	 * @param pickupHistoryTemp
	 * @return AjaxResult
	 */
	@Log(title = "Tài xế nhận cuốc", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/pickup")
	@ResponseBody
	public AjaxResult pickup(@RequestBody PickupHistory pickupHistoryTemp) {
		PickupAssign pickupAssign = pickupAssignService.selectPickupAssignById(pickupHistoryTemp.getPickupAssignId());
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		if (pickupAssign == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
		}
		if (!userId.equals(pickupAssign.getDriverId()) ||
				(pickupHistoryTemp.getShipmentDetailId() != null && pickupHistoryService.checkPickupHistoryExists(pickupAssign.getShipmentId(), pickupHistoryTemp.getShipmentDetailId()) > 0)) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0009));
		}
		PickupHistory pickupHistory = new PickupHistory();
		ShipmentDetail shipmentDetail = null;
		List<ShipmentDetail> shipmentDetails = null;
		if (StringUtils.isNotEmpty(pickupHistoryTemp.getContainerNo())) {
			shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(pickupAssign.getShipmentId());
			shipmentDetail.setContainerNo(pickupHistoryTemp.getContainerNo());
			shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				shipmentDetail = shipmentDetails.get(0);
			} else {
				shipmentDetail = null;
			}
		}
		
		// Case shipmentDetailId != null
		if (shipmentDetail != null) {
			pickupHistory.setContainerNo(shipmentDetail.getContainerNo());
			pickupHistory.setShipmentDetailId(shipmentDetail.getId());
		}
		
		// Check max pickup driver can pick
		Shipment shipment = shipmentService.selectShipmentById(pickupAssign.getShipmentId());
		if (!pickupHistoryService.checkPossiblePickup(pickupAssign.getDriverId(), shipment.getServiceType(), shipmentDetail)) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0016));
		}
		
		pickupHistory.setDriverId(pickupAssign.getDriverId());
		pickupHistory.setTruckNo(pickupHistoryTemp.getTruckNo());
		pickupHistory.setChassisNo(pickupHistoryTemp.getChassisNo());
		// Check plate number
		if (pickupHistoryService.checkPlateNumberIsUnavailable(pickupHistory) > 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0027));
		}
		
		pickupHistory.setLogisticGroupId(pickupAssign.getLogisticGroupId());
		pickupHistory.setShipmentId(pickupAssign.getShipmentId());
		pickupHistory.setDriverPhoneNumber(pickupAssign.getPhoneNumber());
		pickupHistory.setPickupAssignId(pickupAssign.getId());
		pickupHistory.setGatePass(pickupHistory.getTruckNo().substring(pickupHistory.getTruckNo().length()-5, pickupHistory.getTruckNo().length()));
		pickupHistory.setStatus(0);
		pickupHistoryService.insertPickupHistory(pickupHistory);
		return success();
    }
	
	/**
	 * Get pickup detail info (from history or current pickup)
	 * 
	 * @param pickupId
	 * @return AjaxResult
	 */
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
	
	/**
	 * Finish pick up (pickup become history)
	 * 
	 * @param pickupHistoryId
	 * @return AjaxResult
	 */
	@Log(title = "Hoàn thành giao nhận", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/pickup/{pickupHistoryId}/complete")
	@ResponseBody
	public AjaxResult finishPickup(@PathVariable Long pickupHistoryId) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupHistoryId);
		if (pickupHistory == null || !pickupHistory.getDriverId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0017));
		}
		pickupHistory.setStatus(2);
		pickupHistoryService.updatePickupHistory(pickupHistory);
		return success();
	}
	
	@Log(title = "Hủy nhận cuốc", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@PostMapping("/pickup/{pickupId}/cancel")
	@ResponseBody
	public AjaxResult cancelPickup(@PathVariable Long pickupId) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupId);
		if (pickupHistory == null || !pickupHistory.getDriverId().equals(SecurityUtils.getCurrentUser().getUser().getUserId()) || pickupHistory.getStatus() != 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0018));
		}
		pickupHistoryService.deletePickupHistoryById(pickupId);
		return success();
	}

	@GetMapping("/shipment/{shipmentId}/auto-pickup")
	@ResponseBody
	public AjaxResult getPickupAssignByShipmentId(@PathVariable Long shipmentId) {
		if (shipmentId == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0015));
		}
		PickupAssign pickupAssignParam = new PickupAssign();
		pickupAssignParam.setShipmentId(shipmentId);
		pickupAssignParam.setDriverId(SecurityUtils.getCurrentUser().getUser().getUserId());
		PickupAssign pickupAssign = pickupAssignService.selectPickupAssignByShipmentId(pickupAssignParam);
		if (pickupAssign == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0008));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("pickupAssignId", pickupAssign.getId());
		return ajaxResult;
	}
	
	/**
	 * Get list notification
	 * 
	 * @return AjaxResult
	 */
    @PostMapping("/notify")
	@ResponseBody
	public AjaxResult getNotifyList() {
		startPage();
		SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
		sysNotificationReceiver.setUserId(SecurityUtils.getCurrentUser().getUser().getUserId());
		sysNotificationReceiver.setUserType(BusinessConsts.DRIVER_USER_TYPE);
		List<NotificationRes> notificationReses = sysNotificationReceiverService.getNotificationList(sysNotificationReceiver);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("notificationList", notificationReses);
		return ajaxResult;
    }
	
	/**
	 * Update location
	 * 
	 * @param location
	 * @return AjaxResult
	 */
	@PostMapping("/location")
	@ResponseBody
	public AjaxResult updateLocation(@RequestBody Map<String, Double> location) {
		if (location == null) {
			return error();
		}

		double distanceRequire = Double.parseDouble(configService.selectConfigByKey("driver.distance.port"));

		double latEport = Double.parseDouble(configService.selectConfigByKey("location.port.lat"));

		double lonEport = Double.parseDouble(configService.selectConfigByKey("location.port.long"));

		CacheUtils.put("driver-" + SecurityUtils.getCurrentUser().getUser().getUserId(), location);
		
		double x = location.get("x");
		
		double y = location.get("y");
		
		if (x != 0 && y != 0) {
			if (distanceRequire > distance(x, latEport, y, lonEport, 0.0, 0.0)) {
				
				List<Pickup> pickups = pickupHistoryService.selectPickupListByDriverId(SecurityUtils.getCurrentUser().getUser().getUserId());
				
				for (Pickup pickup : pickups) {
					
					// Check if service is send cont
					if (pickup.getServiceType()%2 == 0) {
						// Check if cont has position
						if (!checkContHasPosition(pickup)) {
							// Send to mc
							Map<String, Object> map = new HashMap<>();
							map.put("pickupHistoryId", pickup.getPickupId().toString());
							try {
								mqttService.sendMessageToMc(new Gson().toJson(map));
							} catch (MqttException e) {
								logger.error("Api Driver Error Update Location: " + e);
							}
						}					
					}
				}
			}
		}
		return success();
	}

	/**
	 * Get connection info mqtt
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/connection/info")
	@ResponseBody
	public AjaxResult getMqttUrl() {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("domain", configService.selectConfigByKey("driver.connection.domain"));
		ajaxResult.put("port", configService.selectConfigByKey("driver.connection.port"));
		ajaxResult.put("topic", configService.selectConfigByKey("driver.topic").replace("+", getSession().getId()));
		return ajaxResult;
	}
	
	/**
	 * Calculate distance between two points in latitude and longitude taking into
	 * account height difference. If you are not interested in height difference
	 * pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters el2
	 * End altitude in meters
	 * 
	 * @returns Distance in Meters
	 */
	private double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);
	}
	
	/**
	 * Check container has position
	 * 
	 * @param pickup
	 * @return Boolean
	 */
	private Boolean checkContHasPosition(Pickup pickup) {
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickup.getPickupId());
		if (pickupHistory.getArea() != null) {
			return true;
		}

		if (pickupHistory.getBlock() != null && pickupHistory.getBay() != null && pickupHistory.getLine() != null && pickupHistory.getTier() != null) {
			return true;
		}
		return false;
	}
	
	class AssignContComparator implements Comparator<PickupAssignForm> {
        public int compare(PickupAssignForm pickupAssignForm1, PickupAssignForm pickupAssignForm2) {
            // In the following line you set the criterion,
            // which is the name of Contact in my example scenario
            return pickupAssignForm1.getClickable().compareTo(pickupAssignForm2.getClickable());
        }
    }
}