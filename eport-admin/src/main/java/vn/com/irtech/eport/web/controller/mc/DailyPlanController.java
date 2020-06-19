package vn.com.irtech.eport.web.controller.mc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/dailyPlan")
public class DailyPlanController extends BaseController{

    final String prefix = "/mc/dailyPlan";
    @GetMapping("/index")
    public String getView() 
    {
        return prefix + "/dallyPlan";
    }
    
}