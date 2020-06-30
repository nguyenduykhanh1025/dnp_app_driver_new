package vn.com.irtech.eport.web.controller.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/history/truck")
public class TruckHistoryController extends BaseController {
	private final static String PREFIX = "history/truck";
	
	@GetMapping("/")
	public String getTruckHistory() {
		return PREFIX + "/index";
	}
}
