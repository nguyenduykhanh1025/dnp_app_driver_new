package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/paymentHistory")
public class NapasPaymentHistoryController extends AdminBaseController {
	private String prefix = "om/paymentHistory";

	@GetMapping("/")
	public String getViewexEcuteCatos() {
		return prefix + "/paymentHistory";
	}
}
