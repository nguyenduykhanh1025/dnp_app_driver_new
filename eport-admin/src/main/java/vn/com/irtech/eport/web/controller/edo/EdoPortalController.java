package vn.com.irtech.eport.web.controller.edo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/edi/portal")
public class EdoPortalController extends BaseController {
    
    final String PREFIX = "/edi/portal";
    
    public String index()
    {
        return "/index";
    }
    
}