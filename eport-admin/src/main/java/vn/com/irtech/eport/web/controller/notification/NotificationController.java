package vn.com.irtech.eport.web.controller.notification;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/notification")
public class NotificationController extends BaseController {
	private final static String PREFIX = "notification";
	
	@GetMapping("/")
	private String getNotification() {
		return PREFIX + "/index";
	}

}
