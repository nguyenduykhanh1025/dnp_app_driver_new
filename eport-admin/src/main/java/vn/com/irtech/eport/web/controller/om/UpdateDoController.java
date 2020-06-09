package vn.com.irtech.eport.web.controller.om;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/updateDO")
public class UpdateDoController extends BaseController{

    private String prefix = "/om/updateDO";

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return prefix + "/updateDO";
    }

    @GetMapping("/getShipmentDetailByBlno/{blNo}")
    public AjaxResult getShipmentDetailByBlno(@RequestParam String param) {
        
        return AjaxResult.success("");
    }
    

    
}