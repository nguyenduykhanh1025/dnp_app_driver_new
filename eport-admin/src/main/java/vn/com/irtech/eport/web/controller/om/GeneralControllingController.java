package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/controlling")
public class GeneralControllingController extends AdminBaseController  {

	private String prefix = "om/controlling";
	
	@GetMapping()
	public String getViewControlling() {
		return prefix + "/index";
	}
}
