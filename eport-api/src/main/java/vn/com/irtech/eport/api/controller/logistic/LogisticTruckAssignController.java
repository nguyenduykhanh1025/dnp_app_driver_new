package vn.com.irtech.eport.api.controller.logistic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.PickupAssign;

@RestController
@RequestMapping("/logistic/assign")
public class LogisticTruckAssignController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticTruckAssignController.class);
	
	/**
	 * Get shipment list by service type
	 * 
	 * @param serviceType
	 * @return AjaxResult
	 */
	@GetMapping("/serviceType/{serviceType}/shipments")
	public AjaxResult getShipmentList(@PathVariable("serviceType") Integer serviceType) {
		startPage();
		
		// TODO
		return success();
	}
	
	/**
	 * Get list container by shipment id to assign
	 * 
	 * @param shipmentId
	 * @return
	 */
	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	public AjaxResult getShipmentDetailList(@PathVariable("shipmentId") Long shipmentId) {
		// TODO
		return success();
	}
	
	/**
	 * Get driver list to assign
	 * 
	 * @return AjaxResult
	 */
	@GetMapping("/drivers")
	public AjaxResult getDriverList() {
		// TODO
		return success();
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
		return success();
	}
	
	/**
	 * Assign driver for single container
	 * 
	 * @param pickupAssigns
	 * @return AjaxResult
	 */
	@PostMapping("/assign")
	public AjaxResult assign(@RequestBody List<PickupAssign> pickupAssigns) {
		// TODO
		return success();
	}
}
