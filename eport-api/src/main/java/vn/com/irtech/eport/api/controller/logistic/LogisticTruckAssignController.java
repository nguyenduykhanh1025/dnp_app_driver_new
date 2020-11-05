package vn.com.irtech.eport.api.controller.logistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;

import vn.com.irtech.eport.api.firebase.service.FirebaseService;
import vn.com.irtech.eport.api.form.DriverAccountForm;
import vn.com.irtech.eport.api.form.PickupAssignReq;
import vn.com.irtech.eport.api.form.ShipmentDetailForm;
import vn.com.irtech.eport.api.form.ShipmentForm;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysNotification;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.system.service.ISysNotificationService;
import vn.com.irtech.eport.system.service.ISysUserTokenService;

@RestController
@RequestMapping("/logistic/assign")
public class LogisticTruckAssignController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticTruckAssignController.class);
	

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IDriverAccountService driverAccountService;

	@Autowired
	private IPickupAssignService pickupAssignService;

	@Autowired
	private ISysNotificationService sysNotificationService;

	@Autowired
	private ISysUserTokenService sysUserTokenService;

	@Autowired
	private FirebaseService firebaseService;

	@Autowired
	private ISysNotificationReceiverService sysNotificationReceiverService;

	
	/**
	 * Get shipment list by service type
	 * 
	 * @param serviceType
	 * @return AjaxResult
	 */
	@PostMapping("/serviceType/{serviceType}/shipments")
	public AjaxResult getShipmentList(@PathVariable("serviceType") Integer serviceType,
			@RequestBody PageAble<Shipment> params) {
		// Pagination
		startPage(params.getPageNum(), params.getPageSize(), params.getOrderBy());

		// Only check if service is drop or pickup
		// Only service with 1: pickup full, 2: drop empty, 3: pickup empty, 4: drop
		// full
		if (serviceType == null || serviceType > 4 || serviceType < 1) {
			throw new BusinessException("Không tìm thấy loại dịch vụ bạn yêu cầu, vui lòng kiểm tra lại.");
		}
		Shipment shipmentInput = params.getData();
		if (shipmentInput == null) {
			shipmentInput = new Shipment();
		}
		shipmentInput.setServiceType(serviceType);
		shipmentInput.setLogisticGroupId(getLogisticAccount().getGroupId());

		List<Shipment> shipments = shipmentService.getShipmentListForAssign(shipmentInput);
		List<ShipmentForm> shipmentsResult = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shipments)) {
			for (Shipment shipment : shipments) {
				ShipmentForm shipmentForm = new ShipmentForm();
				BeanUtils.copyBeanProp(shipmentForm, shipment);
				shipmentsResult.add(shipmentForm);
			}
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipments", getDataTable(shipmentsResult));
		return ajaxResult;
	}
	
	/**
	 * Get list container by shipment id to assign
	 * 
	 * @param shipmentId
	 * @return
	 */
	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	public AjaxResult getShipmentDetailList(@PathVariable("shipmentId") Long shipmentId) {
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setLogisticGroupId(getGroupLogisticId());
		shipmentDetailParam.setPaymentStatus("Y");
		shipmentDetailParam.setFinishStatus("N");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService
				.getShipmentDetailListForAssign(shipmentDetailParam);
		List<ShipmentDetailForm> shipmentDetailsResult = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				ShipmentDetailForm shipmentDetailForm = new ShipmentDetailForm();
				BeanUtils.copyBeanProp(shipmentDetailForm, shipmentDetail);
				shipmentDetailsResult.add(shipmentDetailForm);
			}
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentDetails", shipmentDetailsResult);
		return ajaxResult;
	}
	
	/**
	 * Get driver list to assign shipment detail
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/shipment-detail/{shipmentDetailId}/assign-info")
	public AjaxResult getDriverListWhenAssignForShipmentDetail(
			@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		if (shipmentDetailId == null) {
			throw new BusinessException("Không tìm thấy container bạn yêu cầu, vui lòng kiểm tra lại.");
		}
		Long logisticGroupId = getGroupLogisticId();

		// Get shipment detail to get remark address destination if exist
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetail == null) {
			throw new BusinessException("Không tìm thấy container bạn yêu cầu, vui lòng kiểm tra lại.");
		}

		if (!logisticGroupId.equals(shipmentDetail.getLogisticGroupId())) {
			throw new BusinessException("Bạn không có quyền điều xe cho container này, vui lòng kiểm tra lại.");
		}

		if (!"Y".equalsIgnoreCase(shipmentDetail.getPaymentStatus())) {
			throw new BusinessException("Container này chưa được thanh toán, vui lòng kiểm tra lại.");
		}

		// Get all driver
		DriverAccount driverAccountParam = new DriverAccount();
		driverAccountParam.setLogisticGroupId(logisticGroupId);
		driverAccountParam.setDelFlag(false);
		driverAccountParam.setStatus("0");
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccountParam);
		if (CollectionUtils.isEmpty(driverList)) {
			throw new BusinessException(
					"Không tìm thấy tài xế nào để điều xe, vui lòng thêm tài xế trước khi điều xe.");
		}

		// Get assigned driver list
		PickupAssign pickupAssignParam = new PickupAssign();
		pickupAssignParam.setShipmentDetailId(shipmentDetailId);
		pickupAssignParam.setLogisticGroupId(logisticGroupId);
		List<DriverAccount> driverAssignedList = driverAccountService.selectAssignedDriverList(pickupAssignParam);

		// List driver from return to client
		List<DriverAccountForm> driversResult = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(driverList)) {
			String assignedDriverIds = "";
			for (DriverAccount driverAccount : driverAssignedList) {
				assignedDriverIds += driverAccount.getId() + ",";
			}
			// Set flg for driver has be
			for (DriverAccount driverAccount : driverList) {
				DriverAccountForm driverAccountForm = new DriverAccountForm();
				BeanUtils.copyBeanProp(driverAccountForm, driverAccount);
				driverAccountForm.setAssignedFlg(false);
				if (assignedDriverIds.contains(driverAccount.getId() + ",")) {
					driverAccountForm.setAssignedFlg(true);
					driversResult.add(0, driverAccountForm);
				} else {
					driversResult.add(driverAccountForm);
				}
			}
		}

		String address = shipmentDetail.getDeliveryAddress(); // Address destination for container
		String mobileNumber = shipmentDetail.getDeliveryMobile(); // Mobile phone contact for consignee
		String remark = shipmentDetail.getDeliveryRemark(); // Remark for driver when delivery container

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("drivers", driversResult);
		ajaxResult.put("address", address);
		ajaxResult.put("mobileNumber", mobileNumber);
		ajaxResult.put("remark", remark);
		return ajaxResult;
	}

	/**
	 * Get driver list to assign
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/shipment/{shipmentId}/assign-info")
	public AjaxResult getDriverList(@PathVariable("shipmentId") Long shipmentId) {
		if (shipmentId == null) {
			throw new BusinessException("Không tìm thấy mã lô bạn yêu cầu, vui lòng kiểm tra lại.");
		}
		Long logisticGroupId = getGroupLogisticId();
		
		// Get list shipment detail to get remark address destination if exist
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setLogisticGroupId(logisticGroupId);
		shipmentDetailParam.setPaymentStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			throw new BusinessException("Không tìm thấy thông tin lô bạn yêu cầu, vui lòng kiểm tra lại.");
		}
		
		// Get all driver
		DriverAccount driverAccountParam = new DriverAccount();
		driverAccountParam.setLogisticGroupId(logisticGroupId);
		driverAccountParam.setDelFlag(false);
		driverAccountParam.setStatus("0");
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccountParam);
		if (CollectionUtils.isEmpty(driverList)) {
			throw new BusinessException("Không tìm thấy tài xế nào để điều xe, vui lòng thêm tài xế trước khi điều xe.");
		}
		
		// Get assigned driver list
		PickupAssign pickupAssignParam = new PickupAssign();
		pickupAssignParam.setShipmentId(shipmentId);
		pickupAssignParam.setLogisticGroupId(logisticGroupId);
		Map<String, Object> params = new HashMap<>();
		// Set null to shipment detail -> get driver assign by shipment (exclude driver
		// assign by container)
		params.put("nullShipmentDetail", true);
		List<DriverAccount> driverAssignedList = driverAccountService.selectAssignedDriverList(pickupAssignParam);

		// List driver from return to client
		List<DriverAccountForm> driversResult = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(driverList)) {
			String assignedDriverIds = "";
			for (DriverAccount driverAccount : driverAssignedList) {
				assignedDriverIds += driverAccount.getId() + ",";
			}
			// Set flg for driver has be
			for (DriverAccount driverAccount : driverList) {
				DriverAccountForm driverAccountForm = new DriverAccountForm();
				BeanUtils.copyBeanProp(driverAccountForm, driverAccount);
				driverAccountForm.setAssignedFlg(false);
				if (assignedDriverIds.contains(driverAccount.getId() + ",")) {
					driverAccountForm.setAssignedFlg(true);
					driversResult.add(0, driverAccountForm);
				} else {
					driversResult.add(driverAccountForm);
				}
			}
		}

		String address = ""; // Address destination for container
		String mobileNumber = ""; // Mobile phone contact for consignee
		String remark = ""; // Remark for driver when delivery container

		// Get deliver info in first element has info
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (StringUtils.isNotEmpty(shipmentDetail.getDeliveryAddress())
					|| StringUtils.isNotEmpty(shipmentDetail.getDeliveryMobile())
					|| StringUtils.isNotEmpty(shipmentDetail.getDeliveryRemark())) {
				address = shipmentDetail.getDeliveryAddress();
				mobileNumber = shipmentDetail.getDeliveryMobile();
				remark = shipmentDetail.getDeliveryRemark();
				break;
			}
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("drivers", driversResult);
		ajaxResult.put("address", address);
		ajaxResult.put("mobileNumber", mobileNumber);
		ajaxResult.put("remark", remark);
		return ajaxResult;
	}
	
	@PostMapping("/shipment/{shipmentId}/assign")
	@Transactional
	public AjaxResult assignDriverForShipment(@PathVariable("shipmentId") Long shipmentId,
			@Validated @RequestBody PickupAssignReq pickupAssignReq) {
		if (shipmentId == null) {
			throw new BusinessException("Không tìm thấy lô bạn yêu cầu, vui lòng kiểm tra lại.");
		}

		Long logisticGroupId = getGroupLogisticId();

		// Get shipment info check if exist
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (shipment == null || !logisticGroupId.equals(shipment.getLogisticGroupId())) {
			throw new BusinessException("Không tìm thấy lô bạn yêu cầu, vui lòng kiểm tra lại.");
		}
		
		DriverAccount driverAccountParam = new DriverAccount();
		driverAccountParam.setLogisticGroupId(logisticGroupId);
		Map<String, Object> driverAccountParams = new HashMap<>();
		driverAccountParams.put("driverAccountIds", Convert.toStrArray(pickupAssignReq.getDriverIds()));
		driverAccountParam.setParams(driverAccountParams);
		// Get list driver assign request
		List<DriverAccount> driverNeedAssignList = driverAccountService.selectDriverListByIds(driverAccountParam);
		if (CollectionUtils.isEmpty(driverNeedAssignList)) {
			throw new BusinessException("Không tìm thấy tài xế nào được chỉ định vận chuyển container.");
		}

		// Get list assigned driver
		PickupAssign pickupAssignParam = new PickupAssign();
		pickupAssignParam.setShipmentId(shipmentId);
		pickupAssignParam.setLogisticGroupId(logisticGroupId);
		Map<String, Object> params = new HashMap<>();
		// Set null to shipment detail -> get driver assign by shipment (exclude driver
		// assign by container)
		params.put("nullShipmentDetail", true);
		List<DriverAccount> assignedDriverList = driverAccountService.selectAssignedDriverList(pickupAssignParam);
		if (CollectionUtils.isEmpty(assignedDriverList)) {
			assignedDriverList = new ArrayList<>();
		}

		Iterator<DriverAccount> needAssignIterator = driverNeedAssignList.iterator();
		Iterator<DriverAccount> assignedIterator = assignedDriverList.iterator();
		while (needAssignIterator.hasNext()) {
			DriverAccount needAssignDriver = needAssignIterator.next();
			while (assignedIterator.hasNext()) {
				DriverAccount assignedDriver = assignedIterator.next();
				if (needAssignDriver.getId().equals(assignedDriver.getId())) {
					needAssignIterator.remove();
					assignedIterator.remove();
				}
			}
		}

		if (CollectionUtils.isNotEmpty(assignedDriverList)) {
			// List pickup assign need to delete
			String delPickupAssignIds = "";
			for (DriverAccount driverAccount : assignedDriverList) {
				delPickupAssignIds += driverAccount.getPickupAssignId() + ",";
			}
			pickupAssignService
					.deletePickupAssignByIds(delPickupAssignIds.substring(0, delPickupAssignIds.length() - 1));
		}

		// Insert new pickup assign
		if (CollectionUtils.isNotEmpty(driverNeedAssignList)) {
			String serviceName = "";
			switch (shipment.getServiceType()) {
			case EportConstants.SERVICE_PICKUP_FULL:
				serviceName = "nhận container hàng";
				break;
			case EportConstants.SERVICE_PICKUP_EMPTY:
				serviceName = "nhận container rỗng";
				break;
			case EportConstants.SERVICE_DROP_FULL:
				serviceName = "giao container hàng";
				break;
			case EportConstants.SERVICE_DROP_EMPTY:
				serviceName = "giao container rỗng";
				break;
			default:
				break;
			}

			// Create info notification
			SysNotification sysNotification = new SysNotification();
			sysNotification.setTitle("Thông báo điều xe.");
			sysNotification.setNotifyLevel(2L);
			sysNotification
					.setContent("Bạn đã được chỉ định điều xe cho dịch vụ " + serviceName + " lô " + shipment.getId());
			sysNotification.setNotifyLink("");
			sysNotification.setStatus(1L);
			sysNotificationService.insertSysNotification(sysNotification);
			for (DriverAccount driverAccount : driverNeedAssignList) {
				PickupAssign pickupAssign = new PickupAssign();
				pickupAssign.setShipmentId(shipmentId);
				pickupAssign.setDriverId(driverAccount.getId());
				pickupAssign.setLogisticGroupId(logisticGroupId);
				pickupAssign.setFullName(driverAccount.getFullName());
				pickupAssign.setPhoneNumber(driverAccount.getMobileNumber());
				pickupAssign.setRemark(pickupAssignReq.getDeliveryRemark());
				pickupAssign.setDeliveryAddress(pickupAssignReq.getDeliveryAddress());
				pickupAssign.setDeliveryPhoneNumber(pickupAssignReq.getDeliveryMobileNumber());
				pickupAssign.setCreateBy(getUser().getLoginName());
				pickupAssignService.insertPickupAssign(pickupAssign);

				SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
				sysNotificationReceiver.setUserId(driverAccount.getId());
				sysNotificationReceiver.setNotificationId(sysNotification.getId());
				sysNotificationReceiver.setUserType(2L);
				sysNotificationReceiver.setSentFlg(true);
				sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);

				List<String> sysUserTokens = sysUserTokenService.getListDeviceTokenByUserId(driverAccount.getId());
				if (CollectionUtils.isNotEmpty(sysUserTokens)) {
					try {
						firebaseService.sendNotification(sysNotification.getTitle(), sysNotification.getContent(),
								sysUserTokens);
					} catch (FirebaseMessagingException e) {
						logger.warn("Error send notification: " + e);
					}
				}
			}
		}

		return success();
	}
	
	@PostMapping("/shipment-detail/{shipmentDetailId}/assign")
	public AjaxResult assignDriverForShipmentDetail(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@Validated @RequestBody PickupAssignReq pickupAssignReq) {
		if (shipmentDetailId == null) {
			throw new BusinessException("Không tìm thấy container bạn yêu cầu, vui lòng kiểm tra lại.");
		}

		Long logisticGroupId = getGroupLogisticId();

		// Get shipment detail info check if exist
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetailId == null || !logisticGroupId.equals(shipmentDetail.getLogisticGroupId())) {
			throw new BusinessException("Không tìm thấy container bạn yêu cầu, vui lòng kiểm tra lại.");
		}

		Shipment shipment = shipmentService.selectShipmentById(shipmentDetail.getShipmentId());
		if (shipment == null || !logisticGroupId.equals(shipment.getLogisticGroupId())) {
			throw new BusinessException("Không tìm thấy lô bạn yêu cầu, vui lòng kiểm tra lại.");
		}

		// Check if container can be assign
		if (shipment.getServiceType() == EportConstants.SERVICE_PICKUP_FULL
				|| shipment.getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY) {
			if (!"Y".equalsIgnoreCase(shipmentDetail.getPreorderPickup())
					|| !"Y".equalsIgnoreCase(shipmentDetail.getPrePickupPaymentStatus())) {
				throw new BusinessException(
						"Container này không thể được chỉ định vì chưa làm dịch vụ dịch chuyển nâng hạ, vui lòng kiểm tra lại.");
			}
		}

		DriverAccount driverAccountParam = new DriverAccount();
		driverAccountParam.setLogisticGroupId(logisticGroupId);
		Map<String, Object> driverAccountParams = new HashMap<>();
		driverAccountParams.put("driverAccountIds", Convert.toStrArray(pickupAssignReq.getDriverIds()));
		driverAccountParam.setParams(driverAccountParams);
		// Get list driver assign request
		List<DriverAccount> driverNeedAssignList = driverAccountService.selectDriverListByIds(driverAccountParam);
		if (CollectionUtils.isEmpty(driverNeedAssignList)) {
			throw new BusinessException("Không tìm thấy tài xế nào được chỉ định vận chuyển container.");
		}

		// Get list assigned driver
		PickupAssign pickupAssignParam = new PickupAssign();
		pickupAssignParam.setShipmentDetailId(shipmentDetailId);
		pickupAssignParam.setLogisticGroupId(logisticGroupId);
		List<DriverAccount> assignedDriverList = driverAccountService.selectAssignedDriverList(pickupAssignParam);
		if (CollectionUtils.isEmpty(assignedDriverList)) {
			assignedDriverList = new ArrayList<>();
		}

		Iterator<DriverAccount> needAssignIterator = driverNeedAssignList.iterator();
		Iterator<DriverAccount> assignedIterator = assignedDriverList.iterator();
		while (needAssignIterator.hasNext()) {
			DriverAccount needAssignDriver = needAssignIterator.next();
			while (assignedIterator.hasNext()) {
				DriverAccount assignedDriver = assignedIterator.next();
				if (needAssignDriver.getId().equals(assignedDriver.getId())) {
					needAssignIterator.remove();
					assignedIterator.remove();
				}
			}
		}

		if (CollectionUtils.isNotEmpty(assignedDriverList)) {
			// List pickup assign need to delete
			String delPickupAssignIds = "";
			for (DriverAccount driverAccount : assignedDriverList) {
				delPickupAssignIds += driverAccount.getPickupAssignId() + ",";
			}
			pickupAssignService
					.deletePickupAssignByIds(delPickupAssignIds.substring(0, delPickupAssignIds.length() - 1));
		}

		// Insert new pickup assign
		if (CollectionUtils.isNotEmpty(driverNeedAssignList)) {
			String serviceName = "";
			switch (shipment.getServiceType()) {
			case EportConstants.SERVICE_PICKUP_FULL:
				serviceName = "nhận container hàng";
				break;
			case EportConstants.SERVICE_PICKUP_EMPTY:
				serviceName = "nhận container rỗng";
				break;
			case EportConstants.SERVICE_DROP_FULL:
				serviceName = "giao container hàng";
				break;
			case EportConstants.SERVICE_DROP_EMPTY:
				serviceName = "giao container rỗng";
				break;
			default:
				break;
			}

			// Create info notification
			SysNotification sysNotification = new SysNotification();
			sysNotification.setTitle("Thông báo điều xe.");
			sysNotification.setNotifyLevel(2L);
			sysNotification
					.setContent("Bạn đã được chỉ định điều xe cho dịch vụ " + serviceName + " lô " + shipment.getId()
							+ " container: " + shipmentDetail.getContainerNo());
			sysNotification.setNotifyLink("");
			sysNotification.setStatus(1L);
			sysNotificationService.insertSysNotification(sysNotification);
			for (DriverAccount driverAccount : driverNeedAssignList) {
				PickupAssign pickupAssign = new PickupAssign();
				pickupAssign.setShipmentId(shipmentDetail.getShipmentId());
				pickupAssign.setShipmentDetailId(shipmentDetailId);
				pickupAssign.setLogisticGroupId(logisticGroupId);
				pickupAssign.setDriverId(driverAccount.getId());
				pickupAssign.setFullName(driverAccount.getFullName());
				pickupAssign.setPhoneNumber(driverAccount.getMobileNumber());
				pickupAssign.setRemark(pickupAssignReq.getDeliveryRemark());
				pickupAssign.setDeliveryAddress(pickupAssignReq.getDeliveryAddress());
				pickupAssign.setDeliveryPhoneNumber(pickupAssignReq.getDeliveryMobileNumber());
				pickupAssign.setCreateBy(getUser().getLoginName());
				pickupAssignService.insertPickupAssign(pickupAssign);

				SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
				sysNotificationReceiver.setUserId(driverAccount.getId());
				sysNotificationReceiver.setNotificationId(sysNotification.getId());
				sysNotificationReceiver.setUserType(2L);
				sysNotificationReceiver.setSentFlg(true);
				sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);

				List<String> sysUserTokens = sysUserTokenService.getListDeviceTokenByUserId(driverAccount.getId());
				if (CollectionUtils.isNotEmpty(sysUserTokens)) {
					try {
						firebaseService.sendNotification(sysNotification.getTitle(), sysNotification.getContent(),
								sysUserTokens);
					} catch (FirebaseMessagingException e) {
						logger.warn("Error send notification: " + e);
					}
				}
			}
		}
		return success();
	}
}
