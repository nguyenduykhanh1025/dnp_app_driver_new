package vn.com.irtech.eport.api.controller.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@RestController
@RequestMapping("/transport")
public class TransportController extends BaseController{
    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private IShipmentDetailService shipmentDetailService;
    
    /**
     * Get list of shipments by serviceId
     * @param serviceId
     * @return list
     */
    @GetMapping("/shipmentList/{serviceId}")
    @ResponseBody
    public AjaxResult getShipmentList(@PathVariable("serviceId") Integer serviceId){
        AjaxResult ajaxResult = AjaxResult.success();
        Shipment shipment = new Shipment();
        shipment.setServiceId(serviceId);
        ajaxResult.put("data", shipmentService.selectShipmentList(shipment));
        return ajaxResult;
    }

    /**
     * Get list of shipment details by shipmentId
     * @param shipmentId (batchCode)
     * @return list of shipment details
     */
    @GetMapping("/shipmentDetail/{shipmentId}")
    @ResponseBody
    public AjaxResult getShipmentDetail(@PathVariable("shipmentId") Long shipmentId){
        AjaxResult ajaxResult = AjaxResult.success();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setShipmentId(shipmentId);
        ajaxResult.put("data", shipmentDetailService.selectShipmentDetailList(shipmentDetail));
        return ajaxResult;
    }
}