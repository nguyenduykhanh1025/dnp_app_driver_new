package vn.com.irtech.eport.web.controller.mc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;

@Controller
@RequestMapping("/dailyPlan")
public class DailyPlanController extends BaseController{

    final String prefix = "/mc/dailyPlan";
    
    @Autowired
    
    @GetMapping("/index")
    public String getView() 
    {
        return prefix + "/dailyPlan";
    }
    // @GetMapping("/getTruckDetail")
    // @ResponseBody
    // public TableDataInfo getTruckDetail() {
    //     startPage();
    //     //List<Truck> truckDeitailList = truckService.selectTruckList(null);
    //     TableDataInfo dataList = getDataTable(truckDeitailList);
    //     return dataList;
    // }
    
}