package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistic/test")
public class TestLayoutController extends LogisticBaseController {

	private final String PREFIX = "logistic/test";

	@GetMapping("")
	public String testing() {
		return PREFIX + "/testing";
	}
	
	@GetMapping("/layout2")
	public String layout2() {
		return PREFIX + "/layout2LeftRight";
	}

	@GetMapping("/layout3")
	public String layout3() {
		return PREFIX + "/layout3LeftRightWithTab";
	}
}
