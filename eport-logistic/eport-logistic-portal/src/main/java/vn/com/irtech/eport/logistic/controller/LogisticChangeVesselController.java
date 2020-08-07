package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistic/vessel-changing")
public class LogisticChangeVesselController extends LogisticBaseController {

	private final String PREFIX = "logistic/vesselChanging";
	
	@GetMapping()
	public String getVesselChangingView() {
		return PREFIX + "/index";
	}
}
