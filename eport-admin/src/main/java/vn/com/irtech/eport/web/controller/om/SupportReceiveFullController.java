
package vn.com.irtech.eport.web.controller.om;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import vn.com.irtech.eport.common.core.controller.BaseController;


@Controller
@RequestMapping("/om/support/receiveFull")
public class SupportReceiveFullController extends BaseController{

    private final String PREFIX = "om/SupportReceiveFull"; 

    @GetMapping()
    public String getViewSupportReceiveFull()
    {
        return PREFIX + "/index";
    }

}