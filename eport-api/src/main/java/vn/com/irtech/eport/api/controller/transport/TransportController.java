package vn.com.irtech.eport.api.controller.transport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.PickupHistoryForm;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@RestController
@RequestMapping("/transport")
public class TransportController extends BaseController {

    @Autowired
    private IShipmentService shipmentService;

    @Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired IPickupHistoryService pickupHistoryService;
	
    @PostMapping("/register")
	@ResponseBody
	public AjaxResult register() {
		return success();
    }
    
    @PostMapping("/resetpwd")
	@ResponseBody
	public AjaxResult resetPassword() {
		return success();
    }
    
    @GetMapping("/pickup/history")
	@ResponseBody
	public AjaxResult getPickupHistory(@RequestHeader("Authorization") String token) {
		startPage();
		AjaxResult ajaxResult = AjaxResult.success();
		String userPhoneNumber = TokenUtils.getSubjectFromToken(token);
		ajaxResult.put("data",  pickupHistoryService.selectPickupHistoryForDriver(userPhoneNumber));
		return success();
    }
    
    @GetMapping("/pickup")
	@ResponseBody
	public AjaxResult getPickupList() {
		return success();
    }
    
    @GetMapping("/truck")
	@ResponseBody
	public AjaxResult getTruckList() {
		return success();
    }
    
    @PostMapping("/shipment")
	@ResponseBody
	public AjaxResult getShipmentList() {
		return success();
    }
    
    @GetMapping("/shipment/{shipmentId}")
	@ResponseBody
	public AjaxResult getContainerList() {
		return success();
    }
    
    @PostMapping("/pickup")
	@ResponseBody
	public AjaxResult getPickup() {
		return success();
    }
    
    @GetMapping("/pickup/{pickupId}")
	@ResponseBody
	public AjaxResult getPickupDetailInfo(@PathVariable Long pickupId) {
		return success();
    }
    
    @PostMapping("/checkin")
	@ResponseBody
	public AjaxResult checkIn() {
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