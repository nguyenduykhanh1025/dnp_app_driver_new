package vn.com.irtech.eport.web.controller.om;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/updatePayment")
public class UpdatePaymentStatusController extends BaseController{

    private String prefix = "/om/updatePayment";

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return prefix + "/updatePayment";
    }

    
}