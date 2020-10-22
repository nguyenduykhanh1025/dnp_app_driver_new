package vn.com.irtech.eport.api.controller.transport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.form.PickupHistoryDetail;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
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

	protected final Logger logger = LoggerFactory.getLogger(TransportController.class);

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
	private IDriverAccountService driverAccountService;

//	@Autowired
//	private MqttService mqttService;
//	
//	@Autowired
//	private IProcessOrderService processOrderService;

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
		ajaxResult.put("data", pickupHistoryService.selectPickupHistoryForDriver(userId));
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
		List<Pickup> pickups = pickupHistoryService.selectPickupListByDriverId(userId);
		for (Pickup pickup : pickups) {
			if (StringUtils.isEmpty(pickup.getContainerNo())) {
				pickup.setContainerNo("Chưa có");
				ShipmentDetail shipmentDetail = new ShipmentDetail();
				shipmentDetail.setShipmentId(pickup.getBatchCode());
				shipmentDetail.setSztp(pickup.getSztp());
				startPage(1, 1, null);
				List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
				if (CollectionUtils.isNotEmpty(shipmentDetails)) {
					shipmentDetail = shipmentDetails.get(0);
				}
			}
		}
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
		ajaxResult.put("shipmentList", shipmentService.selectShipmentListForDriver(serviceType,
				SecurityUtils.getCurrentUser().getUser().getUserId()));
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
		if (shipmentId == null) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0015));
		}
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		AjaxResult ajaxResult = AjaxResult.success();
		// get shipment
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		// get assign list for current user
		List<PickupAssignForm> pickupAssigns = shipmentDetailService
				.selectShipmentDetailForDriverShipmentAssign(shipmentId, userId);
		// ??
		if (CollectionUtils.isEmpty(pickupAssigns)) {
			pickupAssigns = pickupAssignService.selectPickupAssignListByDriverId(userId, shipmentId);
		}

		// cho t/h boc hang hoac boc rong
		if ((shipment.getServiceType() == EportConstants.SERVICE_PICKUP_FULL
				|| shipment.getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY)
				&& CollectionUtils.isNotEmpty(pickupAssigns)) {

			List<PickupAssignForm> pickupAssign = pickupAssignService.selectPickupAssignListByDriverId(userId,
					shipmentId);
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
		logger.debug("Get list by container by shipment id " + shipmentId + ": " + new Gson().toJson(pickupAssigns));
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

		logger.debug("Receive request pickup from driver: " + new Gson().toJson(pickupHistoryTemp));

		Long idSecret = pickupHistoryTemp.getPickupAssignId();

		if (idSecret == null) {
			throw new BusinessException("Không tìm thấy dữ liệu, vui lòng kiểm tra lại.");
		}

		ShipmentDetail shipmentDetail = null;
		Shipment shipment = null;
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		DriverAccount driverAccount = driverAccountService.selectDriverAccountById(userId);
		PickupHistory pickupHistory = new PickupHistory();

		// Check if id secret is shipment detail id = 0 or pickup assign id = 1
		if (idSecret % 10 == 0) {
			// shipment detail id
			Long shipmentDetailId = idSecret / 10;
			logger.debug("Request pickup with pickup assign id: " + shipmentDetailId);
			shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);

			// Check in case shipment detail id not null (pickup container)
			// then this container need to be has not pickup yet
			if (shipmentDetailId != null && pickupHistoryService
					.checkPickupHistoryExists(shipmentDetail.getShipmentId(), shipmentDetailId) > 0) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0009));
			}
			// Check max pickup driver can pick
			shipment = shipmentService.selectShipmentById(shipmentDetail.getShipmentId());
			if (!pickupHistoryService.checkPossiblePickup(userId, shipment.getServiceType(), shipmentDetail)) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0016));
			}
			pickupHistory.setContainerNo(shipmentDetail.getContainerNo());
			pickupHistory.setSztp(shipmentDetail.getSztp());
			pickupHistory.setShipmentDetailId(shipmentDetailId);
			pickupHistory.setLogisticGroupId(shipmentDetail.getLogisticGroupId());
			pickupHistory.setShipmentId(shipmentDetail.getShipmentId());
			pickupHistory.setDriverPhoneNumber(driverAccount.getMobileNumber());
		} else {
			// pickup assign id
			Long pickupAssignId = idSecret / 10;
			logger.debug("Request pickup with pickup assign id: " + pickupAssignId);
			// Get pickup assign to make pickup history
			PickupAssign pickupAssign = pickupAssignService.selectPickupAssignById(pickupAssignId);

			if (pickupAssign == null) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
			}

			// Check if pickup is assign to this driver
			// or in case shipment detail id not null (pickup container)
			// then this container need to be has not pickup yet
			if (!userId.equals(pickupAssign.getDriverId()) || (pickupHistoryTemp.getShipmentDetailId() != null
					&& pickupHistoryService.checkPickupHistoryExists(pickupAssign.getShipmentId(),
							pickupHistoryTemp.getShipmentDetailId()) > 0)) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0009));
			}

			// Begin make pickup history base on pickup assign has been check and validate
			// carefully
			List<ShipmentDetail> shipmentDetails = null;

			// Check if this pickup is make with container no
			if (pickupHistoryTemp.getShipmentDetailId() != null) {
				shipmentDetail = shipmentDetailService
						.selectShipmentDetailById(pickupHistoryTemp.getShipmentDetailId());
				if (shipmentDetail != null) {
					pickupHistory.setContainerNo(shipmentDetail.getContainerNo());
					pickupHistory.setShipmentDetailId(shipmentDetail.getId());
					pickupHistory.setSztp(shipmentDetail.getSztp());
				} else {
					shipmentDetail = null;
					throw new BusinessException("Container này không tồn tại hoặc đã được nhận bởi tài xế khác.");
				}
			} else {
				pickupHistory.setJobOrderFlg(true);
				shipmentDetail = new ShipmentDetail();
				shipmentDetail.setSztp(pickupHistoryTemp.getSztp());
				shipmentDetail.setShipmentId(pickupAssign.getShipmentId());
				shipmentDetail.setFinishStatus("N");
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
				shipmentDetail = shipmentDetails.get(0);
				if (CollectionUtils.isNotEmpty(shipmentDetails)) {
					pickupHistory.setJobOrderNo(shipmentDetail.getOrderNo());
					pickupHistory.setSztp(shipmentDetail.getSztp());
				} else {
					throw new BusinessException(
							"Không tìm thấy container đủ điều kiện đăng ký vận chuyển, quý khách vui lòng thử lại sau.");
				}
			}

			// Check max pickup driver can pick
			shipment = shipmentService.selectShipmentById(pickupAssign.getShipmentId());
			if (!pickupHistoryService.checkPossiblePickup(pickupAssign.getDriverId(), shipment.getServiceType(),
					shipmentDetail)) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0016));
			}
			pickupHistory.setLogisticGroupId(pickupAssign.getLogisticGroupId());
			pickupHistory.setShipmentId(pickupAssign.getShipmentId());
			pickupHistory.setDriverPhoneNumber(pickupAssign.getPhoneNumber());
			pickupHistory.setPickupAssignId(pickupAssign.getId());
		}

		pickupHistory.setDriverId(userId);
		pickupHistory.setTruckNo(pickupHistoryTemp.getTruckNo());
		pickupHistory.setChassisNo(pickupHistoryTemp.getChassisNo());
		// Check plate number
		if (pickupHistoryService.checkPlateNumberIsUnavailable(pickupHistory) > 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0027));
		}

		LogisticTruck logisticTruck = new LogisticTruck();
		logisticTruck.setPlateNumber(pickupHistoryTemp.getTruckNo());
		List<LogisticTruck> logisticTrucks = logisticTruckService.selectLogisticTruckList(logisticTruck);
		if (CollectionUtils.isNotEmpty(logisticTrucks)) {
			pickupHistory.setGatePass(logisticTrucks.get(0).getGatepass());
			pickupHistory.setLoadableWgt(logisticTrucks.get(0).getSelfWgt());
		}
		pickupHistory.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
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
		PickupHistoryDetail pickupHistoryDetail = pickupHistoryService.selectPickupHistoryDetailById(userId, pickupId);
		if (StringUtils.isEmpty(pickupHistoryDetail.getContainerNo())) {
			pickupHistoryDetail.setContainerNo("Chưa có");
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setSztp(pickupHistoryDetail.getSztp());
			shipmentDetail.setShipmentId(pickupHistoryDetail.getBatchId());
			shipmentDetail.setPaymentStatus("Y");
			shipmentDetail.setFinishStatus("N");
			startPage(1, 1, null);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				shipmentDetail = shipmentDetails.get(0);
				pickupHistoryDetail.setAddress(shipmentDetail.getDeliveryAddress());
				pickupHistoryDetail.setMobileNumber(shipmentDetail.getDeliveryMobile());
				pickupHistoryDetail.setRemark(shipmentDetail.getDeliveryRemark());
				pickupHistoryDetail.setConsignee(shipmentDetail.getConsignee());
				pickupHistoryDetail.setCargoType(shipmentDetail.getCargoType());
				pickupHistoryDetail.setWgt(shipmentDetail.getWgt());
			}
		}
		ajaxResult.put("data", pickupHistoryDetail);
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
		if (pickupHistory == null
				|| !pickupHistory.getDriverId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())) {
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
		if (pickupHistory == null
				|| !pickupHistory.getDriverId().equals(SecurityUtils.getCurrentUser().getUser().getUserId())
				|| pickupHistory.getStatus() != 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0018));
		}
		pickupHistoryService.deletePickupHistoryById(pickupId);
		return success();
	}

	@GetMapping("/shipment/{shipmentId}/sztp/{sztp}/pickup-info")
	@ResponseBody
	public AjaxResult getPickupAssignByShipmentId(@PathVariable("shipmentId") Long shipmentId,
			@PathVariable("sztp") String sztp) {
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
		// Push pickup assign for driver to make confirm and make a pick up history
		ajaxResult.put("pickupAssignId", (pickupAssign.getId() * 10) + 1);
		startPage(1, 1, null);
		// Get information of pickup before confirm get pickup
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setSztp(sztp);
		shipmentDetailParam.setFinishStatus("N");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			ShipmentDetail shipmentDetail = shipmentDetails.get(0);
			PickupHistoryDetail pickupHistoryDetail = new PickupHistoryDetail();
			pickupHistoryDetail.setContainerNo("Chưa có");
			pickupHistoryDetail.setSztp(shipmentDetail.getSztp());
			pickupHistoryDetail.setConsignee(shipmentDetail.getConsignee());
			pickupHistoryDetail.setAddress(shipmentDetail.getDeliveryAddress());
			pickupHistoryDetail.setMobileNumber(shipmentDetail.getDeliveryMobile());
			pickupHistoryDetail.setRemark(shipmentDetail.getDeliveryRemark());
			ajaxResult.put("data", pickupHistoryDetail);
		}
		return ajaxResult;
	}

	/**
	 * Get list notification
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/notify")
	@ResponseBody
	@Transactional
	public AjaxResult getNotifyList() {
		startPage();
		SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
		sysNotificationReceiver.setUserId(SecurityUtils.getCurrentUser().getUser().getUserId());
		sysNotificationReceiver.setUserType(BusinessConsts.DRIVER_USER_TYPE);
		List<NotificationRes> notificationReses = sysNotificationReceiverService
				.getNotificationList(sysNotificationReceiver);
		if (CollectionUtils.isNotEmpty(notificationReses)) {
			for (NotificationRes notificationRes : notificationReses) {
				SysNotificationReceiver sysNotificationRecei = new SysNotificationReceiver();
				sysNotificationRecei.setId(notificationRes.getId());
				sysNotificationRecei.setSeenFlg(true);
				sysNotificationReceiverService.updateSysNotificationReceiver(sysNotificationRecei);
			}
		}
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
	@Transactional
	public AjaxResult updateLocation(@RequestBody Map<String, Double> location) {
		if (location == null) {
			return error();
		}
		logger.debug(">>>>>>>>>>>>>>>>>>.. Receive GPS location: {} : {} , {}", location, location.get("x"),
				location.get("y"));

//		double distanceRequire = Double.parseDouble(configService.selectConfigByKey("driver.distance.port"));
		double latEport = Double.parseDouble(configService.selectConfigByKey("location.port.lat"));
		double lonEport = Double.parseDouble(configService.selectConfigByKey("location.port.long"));

		Double x = location.get("x");
		Double y = location.get("y");

		if (x != null && y != null && x != 0 && y != 0) {
			// Update location to cache
			CacheUtils.put("driver-" + SecurityUtils.getCurrentUser().getUser().getUserId(), location);
			double distance = distance(x, latEport, y, lonEport, 0.0, 0.0);
			logger.debug(">>>>>>>>>>>>>>>>>>..Distance from DNP: {}", distance);
			// Check distance to request MC request yard position
			// TODO Cache list
			List<Pickup> pickups = pickupHistoryService
					.selectPickupListByDriverId(SecurityUtils.getCurrentUser().getUser().getUserId());

			// Update location and update location time for pick up history
			PickupHistory pickupHistory = null;
			for (Pickup pickup : pickups) {
				// Update location for driver DROP F/E
//				if (pickup.getServiceType().intValue() == EportConstants.SERVICE_DROP_EMPTY
//						|| pickup.getServiceType().intValue() == EportConstants.SERVICE_DROP_FULL) {
//					
//				}
				// FIXME fix lai perfomance
				pickupHistory = new PickupHistory();
				pickupHistory.setId(pickup.getPickupId());
				pickupHistory.setDistance(distance);
				pickupHistory.setUpdateLocationTime(new Date());
				pickupHistoryService.updatePickupHistory(pickupHistory);
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
		ajaxResult.put("locationUpdatePeriod", configService.selectConfigByKey("driver.location.period"));
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

//	/**
//	 * Check container has position
//	 * 
//	 * @param pickup
//	 * @return Boolean
//	 */
//	private Boolean checkContHasPosition(Pickup pickup) {
//		// PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickup.getPickupId());
//
//		if (StringUtils.isNotBlank(pickup.getArea()) || StringUtils.isNotBlank(pickup.getBlock())
//				&& StringUtils.isNotBlank(pickup.getBay()) && StringUtils.isNotBlank(pickup.getLine())
//				&& StringUtils.isNotBlank(pickup.getTier())) {
//			return true;
//		}
//		return false;
//	}

	class AssignContComparator implements Comparator<PickupAssignForm> {
		public int compare(PickupAssignForm pickupAssignForm1, PickupAssignForm pickupAssignForm2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return pickupAssignForm1.getClickable().compareTo(pickupAssignForm2.getClickable());
		}
	}

	@PostMapping("/pickup/truck")
	public AjaxResult updateTruckNo(@RequestBody PickupHistory pickupHistoryReq) {
		Long userId = SecurityUtils.getCurrentUser().getUser().getUserId();
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(userId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		if (CollectionUtils.isNotEmpty(pickupHistories)) {
			for (PickupHistory pickupHistory : pickupHistories) {
				if (StringUtils.isNotEmpty(pickupHistoryReq.getTruckNo())) {
					pickupHistory.setTruckNo(pickupHistoryReq.getTruckNo());
				}
				if (StringUtils.isNotEmpty(pickupHistoryReq.getChassisNo())) {
					pickupHistory.setTruckNo(pickupHistoryReq.getChassisNo());
				}
				pickupHistoryService.updatePickupHistory(pickupHistory);
			}
		} else {
			throw new BusinessException(
					"Bạn chưa nhận đơn hoặc đơn của bạn đang ở trạng thái gate in, vui lòng kiểm tra và thử lại sau.");
		}
		return success();
	}

	@GetMapping("/shipment-detail/{shipmentDetailId}/pickup-info")
	public AjaxResult getInfoWithShipmentDetailId(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetail == null) {
			throw new BusinessException("Không tìm thấy thông tin container.");
		}
		AjaxResult ajaxResult = AjaxResult.success();
		PickupHistoryDetail pickupHistoryDetail = new PickupHistoryDetail();
		pickupHistoryDetail.setContainerNo(shipmentDetail.getContainerNo());
		pickupHistoryDetail.setSztp(shipmentDetail.getSztp());
		pickupHistoryDetail.setCargoType(shipmentDetail.getCargoType());
		pickupHistoryDetail.setWgt(shipmentDetail.getWgt());
		pickupHistoryDetail.setConsignee(shipmentDetail.getConsignee());
		pickupHistoryDetail.setAddress(shipmentDetail.getDeliveryAddress());
		pickupHistoryDetail.setMobileNumber(shipmentDetail.getDeliveryMobile());
		pickupHistoryDetail.setRemark(shipmentDetail.getDeliveryRemark());
		ajaxResult.put("data", pickupHistoryDetail);
		return ajaxResult;
	}

	@PostMapping("/pickup-assign/serviceType/{serviceType}/list")
	public AjaxResult getPickupAssingListSend(@PathVariable("serviceType") Integer serviceType,
			@RequestBody PickupAssignForm pickupHistoryDetail) {
		// Get driver Account info
		DriverAccount driverAccount = driverAccountService
				.selectDriverAccountById(SecurityUtils.getCurrentUser().getUser().getUserId());
		// Get list shipment detail for driver pickup
		List<PickupAssignForm> pickupAssignForms = shipmentDetailService.selectShipmentDetailForDriverSendCont(
				driverAccount.getLogisticGroupId(), pickupHistoryDetail, serviceType);
		if (CollectionUtils.isEmpty(pickupAssignForms)) {
			pickupAssignForms = new ArrayList<>();
		}
		for (PickupAssignForm pickupAssignForm : pickupAssignForms) {
			pickupAssignForm.setPickupAssignId(pickupAssignForm.getPickupAssignId() * 10);
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", pickupAssignForms);
		return ajaxResult;
	}
}