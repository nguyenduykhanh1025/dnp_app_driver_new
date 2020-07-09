package vn.com.irtech.eport.web.controller.report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/reports")
public class ReportController extends BaseController {
	private final static String PREFIX = "reports";


	@GetMapping()
	private String getNotification() {
		return PREFIX + "/index";
	}

}
