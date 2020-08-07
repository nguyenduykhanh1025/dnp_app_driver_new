package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistic/shifting-cont")
public class LogisticShiftingContController extends LogisticBaseController {

	private final String PREFIX = "logistic/shiftingCont";
	
	@GetMapping()
	public String getShiftingContView() {
		return PREFIX + "/index";
	}
}
