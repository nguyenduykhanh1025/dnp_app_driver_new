package vn.com.irtech.eport.web.controller.om;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Controller
@RequestMapping("/printQrCode")
public class PrintQrCodeController extends BaseController{
    private String prefix = "/om/printQrCode";
    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @GetMapping("/index")
    public String getViewUpdateDo()
    {
        return prefix + "/printQrCode";
    }

    @GetMapping("/getOptionSearch")
    @ResponseBody
    public AjaxResult getOptionSearch(String keyString) {
        List<String> blNo = shipmentDetailService.getBlLists(keyString);
        return AjaxResult.success(blNo);
    }
    
    @GetMapping("/getShipmentDetail")
    @ResponseBody
    public TableDataInfo getShipmentDetail(String blNo) {
        startPage();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        if(blNo != null)
        {
            shipmentDetail.setBlNo(blNo);
        }
        List<ShipmentDetail> shipmentDetailList = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        TableDataInfo dataList = getDataTable(shipmentDetailList);
        return dataList;
    }
    @GetMapping("/QrCode/{blNo}")
    public String getQrCode(@PathVariable("blNo") String blNo,ModelMap mmap)
    {
        mmap.addAttribute("blNo", blNo);
        return prefix + "/QrCode";
    }
}