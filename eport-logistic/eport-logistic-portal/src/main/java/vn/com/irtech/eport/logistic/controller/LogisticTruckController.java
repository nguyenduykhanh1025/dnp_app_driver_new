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
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;

/**
 *LogisticTruckController
 * 
 * @author admin
 * @date 2020-06-16
 */
@Controller
@RequestMapping("/logistic/logisticTruck")
public class LogisticTruckController extends LogisticBaseController
{
    private String prefix = "logistic/logisticTruck";

    @Autowired
    private ILogisticTruckService logisticTruckService;

    @GetMapping()
    public String logisticTruck()
    {
        return prefix + "/logisticTruck";
    }

    /**
     * GetLogisticTruck List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogisticTruck logisticTruck)
    {
        startPage();
        logisticTruck.setDelFlag(false);
        logisticTruck.setLogisticGroupId(getUser().getGroupId());
        List<LogisticTruck> list = logisticTruckService.selectLogisticTruckList(logisticTruck);
        return getDataTable(list);
    }

    /**
     * AddLogisticTruck
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        
        String groupName = getGroup().getGroupName();
        mmap.put("groupName", groupName);
        return prefix + "/add";
    }

    /**
     * Add or UpdateLogisticTruck
     */
    @Log(title = "Thêm Xe", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LogisticTruck logisticTruck)
    {
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
    	logisticTruck.setPlateNumber(logisticTruck.getPlateNumber().trim().toUpperCase());
    	logisticTruck.setCreateBy(getUser().getFullName());
        return toAjax(logisticTruckService.insertLogisticTruck(logisticTruck));
    }

    /**
     * UpdateLogisticTruck
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticTruck logisticTruck = logisticTruckService.selectLogisticTruckById(id);
        mmap.put("logisticTruck", logisticTruck);
        String groupName = getGroup().getGroupName();
        mmap.put("groupName", groupName);
        return prefix + "/edit";
    }

    /**
     * Update SaveLogisticTruck
     */
    @Log(title = "Chỉnh Sữa Xe", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LogisticTruck logisticTruck)
    {
    	logisticTruck.setPlateNumber(logisticTruck.getPlateNumber().trim().toUpperCase());
    	if(logisticTruckService.checkPlateNumberUnique(logisticTruck.getPlateNumber()) > 1) {
    		return error("Biển số xe này đã tồn tại!");
    	}
        return toAjax(logisticTruckService.updateLogisticTruck(logisticTruck));
    }
    /**
     * DeleteLogisticTruck
     */
    @Log(title = "Xóa Xe", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(logisticTruckService.updateDelFlagByIds(ids));
    }
    
    @GetMapping("/searchTractorByKeyword")
    @ResponseBody
    public List<JSONObject> searchTractorByKeyword(String keyword){
    	LogisticTruck logisticTruck = new LogisticTruck();
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
        logisticTruck.setPlateNumber(keyword);
    	logisticTruck.setDelFlag(false);
        logisticTruck.setType("0");
    	List<LogisticTruck> list = logisticTruckService.selectLogisticTruckList(logisticTruck);
    	List<JSONObject> result = new ArrayList<>();
    	if(list.size() > 0) {
        	for(LogisticTruck i : list) {
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
    public List<JSONObject> searchTrailerByKeyword(String keyword){
    	LogisticTruck logisticTruck = new LogisticTruck();
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
        logisticTruck.setPlateNumber(keyword);
    	logisticTruck.setDelFlag(false);
        logisticTruck.setType("1");
    	List<LogisticTruck> list = logisticTruckService.selectLogisticTruckList(logisticTruck);
    	List<JSONObject> result = new ArrayList<>();
    	if(list.size() > 0) {
        	for(LogisticTruck i : list) {
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
    
    @GetMapping("/getTractorList")
    @ResponseBody
    public List<LogisticTruck> getTractorList(){
    	LogisticTruck logisticTruck = new  LogisticTruck();
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
    	logisticTruck.setDelFlag(false);
    	logisticTruck.setType("0");
    	return logisticTruckService.selectLogisticTruckList(logisticTruck);
    }
    
    @GetMapping("/getTrailerList")
    @ResponseBody
    public List<LogisticTruck> getTrailerList(){
    	LogisticTruck logisticTruck = new  LogisticTruck();
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
    	logisticTruck.setDelFlag(false);
    	logisticTruck.setType("1");
    	return logisticTruckService.selectLogisticTruckList(logisticTruck);
    }
    
    @GetMapping("/unique/plate/{plate}")
    @ResponseBody
    public AjaxResult checkPlateUnique(@PathVariable("plate") String plate) {
    	if(logisticTruckService.checkPlateNumberUnique(plate.trim().toUpperCase()) > 0) {
    		return error("Biển số xe này đã tồn tại!");
    	}
    	return success();
    }
}
