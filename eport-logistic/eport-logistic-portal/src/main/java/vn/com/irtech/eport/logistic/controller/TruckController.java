package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.logistic.domain.Truck;
import vn.com.irtech.eport.logistic.service.ITruckService;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * TruckController
 * 
 * @author admin
 * @date 2020-06-16
 */
@Controller
@RequestMapping("/logistic/truck")
public class TruckController extends LogisticBaseController
{
    private String prefix = "logistic/truck";

    @Autowired
    private ITruckService truckService;

    @GetMapping()
    public String truck()
    {
        return prefix + "/truck";
    }

    /**
     * Get Truck List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Truck truck)
    {
        startPage();
        truck.setDelFlag(false);
        List<Truck> list = truckService.selectTruckList(truck);
        return getDataTable(list);
    }

    /**
     * Add Truck
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Truck
     */
    @Log(title = "Truck", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Truck truck)
    {
    	truck.setLogisticGroupId(getUser().getGroupId());
    	truck.setPlateNumber(truck.getPlateNumber().trim().toUpperCase());
    	if(truckService.checkPlateNumberUnique(truck.getPlateNumber()) > 0) {
    		error("Biển số xe này đã tồn tại!");
    	}
        return toAjax(truckService.insertTruck(truck));
    }

    /**
     * Update Truck
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Truck truck = truckService.selectTruckById(id);
        mmap.put("truck", truck);
        return prefix + "/edit";
    }

    /**
     * Update Save Truck
     */
    @Log(title = "Truck", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Truck truck)
    {
    	truck.setPlateNumber(truck.getPlateNumber().trim().toUpperCase());
    	if(truckService.checkPlateNumberUnique(truck.getPlateNumber()) > 1) {
    		error("Biển số xe này đã tồn tại!");
    	}
        return toAjax(truckService.updateTruck(truck));
    }
    /**
     * Delete Truck
     */
    @Log(title = "Truck", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(truckService.updateDelFlagByIds(ids));
    }
    
    @GetMapping("/searchTractorByKeyword")
    @ResponseBody
    public List<JSONObject> searchTractorByKeyword(String keyword, @RequestParam(value = "tractorIdArray[]") String[] tractorIdArray){
    	Truck truck = new Truck();
    	truck.setLogisticGroupId(getUser().getGroupId());
        truck.setPlateNumber(keyword);
        truck.setType("0");
    	List<Truck> list = truckService.selectTruckList(truck);
    	List<JSONObject> result = new ArrayList<>();
    	if(list.size() > 0) {
        	for(Truck i : list) {
        		JSONObject jsonObject = new JSONObject();
        		jsonObject.put("id", i.getId());
        		jsonObject.put("text", i.getPlateNumber());
        		result.add(jsonObject);
        	}
    	}else {
    		return null;
    	}
    	return result;
    }
    @GetMapping("/searchTrailerByKeyword")
    @ResponseBody
    public List<JSONObject> searchTrailerByKeyword(String keyword, @RequestParam(value = "trailerIdArray[]") String[] trailerIdArray){
    	Truck truck = new Truck();
    	truck.setLogisticGroupId(getUser().getGroupId());
        truck.setPlateNumber(keyword);
        truck.setType("1");
    	List<Truck> list = truckService.selectTruckList(truck);
    	List<JSONObject> result = new ArrayList<>();
    	if(list.size() > 0) {
        	for(Truck i : list) {
        		JSONObject jsonObject = new JSONObject();
        		jsonObject.put("id", i.getId());
        		jsonObject.put("text", i.getPlateNumber());
        		result.add(jsonObject);
        	}
    	}else {
    		return null;
    	}
    	return result;
    }
}
