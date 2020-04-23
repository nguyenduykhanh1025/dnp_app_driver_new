package vn.com.irtech.eport.carrier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Help pages
 * 
 * @author irtech
 */
@Controller
@RequestMapping("/page")
public class DocumentController extends CarrierBaseController {

	private String prefix = "page";
	
	@GetMapping("/{page}")
    public String showPage(@PathVariable("page") String page) {
      return prefix + "/" + page;
    }
}
