package vn.com.irtech.eport.web.controller.om;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/executeCatos")
public class ExecuteCatosController extends BaseController{
    private String prefix = "/executeCatos";
    @GetMapping("/index")
    public String getViewexEcuteCatos()
    {
        return(prefix + "/index");
    }
}