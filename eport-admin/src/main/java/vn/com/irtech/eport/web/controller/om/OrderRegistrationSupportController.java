package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/om/order/support")
public class OrderRegistrationSupportController extends BaseController {

    private final String PREFIX = "om/orderRegistrationSupport";

    @GetMapping()
	public String getMainView() {
		return PREFIX + "/index";
	}
    
    @GetMapping("/custom")
	public String getCustomSupport() {
		return PREFIX + "/customSupport";
    }
    
    @GetMapping("/driver")
	public String getdriverSupport() {
		return PREFIX + "/driverSupport";
    }
    
    @GetMapping("/payment")
	public String getpaymentSupport() {
		return PREFIX + "/paymentSupport";
    }

    @GetMapping("/do")
	public String getReceiveDoSupport() {
		return PREFIX + "/receiveDoSupport";
    }

    @GetMapping("/verification")
	public String getVerificationsupport() {
		return PREFIX + "/verificationSupport";
	}
}