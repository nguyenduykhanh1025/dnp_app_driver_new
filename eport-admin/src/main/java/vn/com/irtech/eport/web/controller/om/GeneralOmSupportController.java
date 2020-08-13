package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/generalSupport")
public class GeneralOmSupportController extends AdminBaseController {
	private final String PREFIX = "om/generalSupport";

	
	@GetMapping()
	public String getViewPaymentHistory() {
		return PREFIX + "/index";
	}
}
