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
@RequestMapping("/updateDO")
public class UpdateDoController extends BaseController{

    private String prefix = "om/updateDO";

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return prefix + "/updateDO";
    }

//    @GetMapping("/getOptionSearchDo")
//    @ResponseBody
//    public AjaxResult getOptionSearch(String keyString) {
//        List<String> blNo = shipmentDetailService.getBlListByDoStatus(keyString);
//        return AjaxResult.success(blNo);
//    }

    @GetMapping("/getShipmentDetail")
    @ResponseBody
    public TableDataInfo getShipmentDetail( int pageNum, int pageSize,String blNo) {
        startPage();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setDoStatus("N");
        if(blNo != null)
        {
            shipmentDetail.setBlNo(blNo);
        }
        List<ShipmentDetail> shipmentDetailList = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        TableDataInfo dataList = getDataTable(shipmentDetailList);
        return dataList;
    }

    @GetMapping("/updateStatusDo")
    @ResponseBody
    public AjaxResult updateStatusDo(String blNo)
    {
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setDoStatus("Y");
        shipmentDetail.setBookingNo(blNo);
        shipmentDetailService.updateShipmentDetail(shipmentDetail);
        return AjaxResult.success("Cập nhật thành công");
    }
}