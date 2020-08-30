package vn.com.irtech.eport.api.controller.logistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
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
	private ISysNotificationReceiverService sysNotificationReceiverService;

	
	/**
	 * Get shipment list by service type
	 * 
	 * @param serviceType
	 * @return AjaxResult
	 */
	@GetMapping("/serviceType/{serviceType}/shipments")
	public AjaxResult getShipmentList(@PathVariable("serviceType") Integer serviceType) {
		startPage();
		if (serviceType == null || serviceType > 4 || serviceType < 1) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0005));
		}
		Shipment shipment = new Shipment();
		AjaxResult ajaxResult = AjaxResult.success();
		shipment.setServiceType(serviceType);
		shipment.setLogisticAccountId(getUserId());
		ajaxResult.put("shipmentList", shipmentService.selectShipmentList(shipment));
		// TODO
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
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setLogisticGroupId(getGroupLogisticId());
		shipmentDetail.setProcessStatus("Y");
		shipmentDetail.setFinishStatus("N");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailListForSendFReceiveE(shipmentDetail);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}
	
	/**
	 * Get driver list to assign
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/drivers")
	public AjaxResult getDriverList() {
		DriverAccount driverAccount = new DriverAccount();
    	driverAccount.setLogisticGroupId(getGroupLogisticId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");
		AjaxResult ajaxResult = AjaxResult.success();
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccount);
		ajaxResult.put("driversList", driverList);
		return ajaxResult;
	}
	
	/**
	 * Assign list driver by shipment id
	 * 
	 * @param shipmentId
	 * @return AjaxResult
	 */
	@PostMapping("/shipment/{shipmentId}/assign")
	public AjaxResult assignByShipment(@PathVariable("shipmentId") Long shipmentId) {
		// TODO
		AjaxResult ajaxResult = AjaxResult.success();
		List<DriverAccount> driverList = new ArrayList<DriverAccount>();
		DriverAccount driverAccount = new DriverAccount();
        driverAccount.setLogisticGroupId(getGroupLogisticId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");
        PickupAssign pickupAssign = new PickupAssign();
        pickupAssign.setShipmentId(shipmentId);
        List<PickupAssign> assignList = pickupAssignService.selectPickupAssignList(pickupAssign);
        if(assignList.size() != 0) {
        	for(PickupAssign i : assignList) {
				//TH: custom theo batch
				if(i.getShipmentDetailId() == null && i.getDriverId() != null){
					driverList.add(driverAccountService.selectDriverAccountById(i.getDriverId()));
				}
			}
		}
        ajaxResult.put("assignList", driverList);
		return ajaxResult;
	}

	
	
	/**
	 * Assign driver for single container ?? pickupAssigns by Shipment
	 * 
	 * @param pickupAssigns
	 * @return AjaxResult
	 */
	@PostMapping("/assign")
	public AjaxResult assign(@RequestBody List<PickupAssign> pickupAssigns) {
		AjaxResult ajaxResult = AjaxResult.success();
		if(pickupAssigns.size() == 0){
			return AjaxResult.error();
		}
		int accountNumber = 0;
		for(PickupAssign i :pickupAssigns){
			if (i.getDriverId() != null) {
				accountNumber++;
			} else {
				if (i.getPhoneNumber() == null || "".equals(i.getPhoneNumber().trim())) {
					return AjaxResult.error("Số điện thoại không được trống");
				}
				if (i.getFullName() == null || "".equals(i.getFullName().trim())) {
					return AjaxResult.error("Họ tên không được trống");
				}
				if (i.getTruckNo() == null || "".equals(i.getTruckNo().trim())) {
					return AjaxResult.error("Biển số xe đầu kéo không được trống");
				}
				if (i.getChassisNo() == null || "".equals(i.getChassisNo().trim())) {
					return AjaxResult.error("Biển số xe đầu rơ mooc không được trống");
				}
			}
			i.setLogisticGroupId(getGroupLogisticId());
		}
		//check driverId of current logistic
		if(accountNumber > 0){// co driver trong cty moi check
			int check = driverAccountService.checkDriverOfLogisticGroup(pickupAssigns);
			if(check != accountNumber){
				return AjaxResult.error("Có tài xế không tồn tại.");
			}
		}
		//check shipmentId of current logistic
		Shipment shipment = shipmentService.selectShipmentById(pickupAssigns.get(0).getShipmentId());
		if(shipment != null && shipment.getLogisticGroupId().equals(getGroupLogisticId())){
			//delete last assign follow batch
			PickupAssign assignBatch = new PickupAssign();
			assignBatch.setShipmentId(shipment.getId());
			List<PickupAssign> assignlist = pickupAssignService.selectPickupAssignList(assignBatch);
			if(assignlist.size() != 0 ){
				for(PickupAssign i : assignlist){
					if(i.getShipmentDetailId() == null){
						pickupAssignService.deletePickupAssignById(i.getId());
					}
				}
			}
			
			// Create info notification
			SysNotification sysNotification = new SysNotification();
			sysNotification.setTitle("Thông báo điều xe.");
			sysNotification.setNotifyLevel(2L);
			sysNotification.setContent("Bạn đã được chỉ định điều xe cho lô " + shipment.getId());
			sysNotification.setNotifyLink("");
			sysNotification.setStatus(1L);
			sysNotificationService.insertSysNotification(sysNotification);
			
			// add custom assign follow batch
			for(int i = 0 ; i< pickupAssigns.size(); i ++){
				pickupAssigns.get(i).setCreateBy(getUser().getLoginName());
				pickupAssigns.get(i).setCreateTime(new Date());
				pickupAssignService.insertPickupAssign(pickupAssigns.get(i));
				
				// Notification receiver
				SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
				sysNotificationReceiver.setUserId(pickupAssigns.get(i).getDriverId());
				sysNotificationReceiver.setNotificationId(sysNotification.getId());
				sysNotificationReceiver.setUserType(2L);
				sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);
			}
			return ajaxResult;
		}
		return AjaxResult.error("Bạn không có quyền điều xe cho lô này.");
	}
}
