package vn.com.irtech.eport.web.controller.om;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import org.springframework.web.bind.annotation.ResponseBody;


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


    @GetMapping("/getShipmentDetail")
    @ResponseBody
    public AjaxResult getShipmentDetail() {
        Map<String, Object> pageInfo = new HashMap<>();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setDoStatus("N");
       // shipmentDetail.setParams(pageInfo);
        return AjaxResult.success(shipmentDetailService.selectShipmentDetailList(shipmentDetail));
    }
    

    @GetMapping("/getOptionSearch")
    @ResponseBody
    public AjaxResult getOptionSearch(String keyString) {
        List<String> blNo = shipmentDetailService.selectBlList(keyString);
        return AjaxResult.success(blNo);
    }
    
}