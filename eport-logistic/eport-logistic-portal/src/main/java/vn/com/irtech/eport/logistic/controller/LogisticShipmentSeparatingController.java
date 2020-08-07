package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistic/shipmentSeparating")
public class LogisticShipmentSeparatingController extends LogisticBaseController {

	private final String PREFIX = "logistic/shipmentSeparating";
	
	@GetMapping()
	public String getShipmentSeparatingView() {
		return PREFIX + "/addCont";
	}
}
