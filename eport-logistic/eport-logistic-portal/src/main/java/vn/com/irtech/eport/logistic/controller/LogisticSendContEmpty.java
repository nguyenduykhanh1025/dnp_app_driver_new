package vn.com.irtech.eport.logistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logistic/sendContEmpty")
public class LogisticSendContEmpty {

    private final String prefix = "logistic/sendContEmpty";

    @GetMapping()
	public String sentContEmpty() {
		return prefix + "/index";
	}
    
}