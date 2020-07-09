package vn.com.irtech.eport.web.controller.om;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


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

    private String prefix = "om/updatePayment";

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return prefix + "/updatePayment";
    }

    @GetMapping("/getOptionSearch")
    @ResponseBody
    public AjaxResult getOptionSearch(String keyString) {
        List<String> blNo = shipmentDetailService.getBlListByPaymentStatus(keyString);
        return AjaxResult.success(blNo);
    }

    @GetMapping("/getShipmentDetail")
    @ResponseBody
    public TableDataInfo getShipmentDetail( int pageNum, int pageSize,String blNo) {
        startPage();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setPaymentStatus("N");
        if(blNo != null)
        {
            shipmentDetail.setBlNo(blNo);
        }
        List<ShipmentDetail> shipmentDetailList = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        TableDataInfo dataList = getDataTable(shipmentDetailList);
        return dataList;
    }

    @GetMapping("/updatePaymentStatus")
    @ResponseBody
    public AjaxResult updateStatusDo(String blNo)
    {
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setPaymentStatus("Y");
        shipmentDetail.setBookingNo(blNo);
        shipmentDetailService.updateShipmentDetail(shipmentDetail);
        return AjaxResult.success("Cập nhật thành công");
    }
    
}