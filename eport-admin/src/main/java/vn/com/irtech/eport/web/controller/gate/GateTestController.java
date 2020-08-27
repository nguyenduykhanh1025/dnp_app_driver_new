package vn.com.irtech.eport.web.controller.gate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/gate/test")
public class GateTestController extends BaseController {

	private final static String PREFIX = "gate/test";
	
	@GetMapping()
	public String getView() {
		return PREFIX + "/test";
	}
}
