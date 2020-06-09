package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/updateDO")
public class UpdateDoController extends BaseController{

    private String prefix = "/updateDO";
    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return(prefix + "/index");
    }

    
}