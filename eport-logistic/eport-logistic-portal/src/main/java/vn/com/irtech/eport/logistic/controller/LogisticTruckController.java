package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.RfidTruck;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IRfidTruckService;

/**
 *LogisticTruckController
 * 
 * @author admin
 * @date 2020-06-16
 */
@Controller
@RequiresPermissions("logistic:transport")
@RequestMapping("/logistic/logisticTruck")
public class LogisticTruckController extends LogisticBaseController
{
    private String prefix = "logistic/logisticTruck";

	@Autowired
	private IRfidTruckService rfidTruckService;

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
		List<LogisticTruck> list = logisticTruckService.selectLogisticTruckListWithRfid(logisticTruck);
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
	@Transactional
    public AjaxResult addSave(LogisticTruck logisticTruck)
    {
		LogisticAccount user = getUser();
    	logisticTruck.setLogisticGroupId(getUser().getGroupId());
    	logisticTruck.setPlateNumber(logisticTruck.getPlateNumber().trim().toUpperCase());
		logisticTruck.setCreateBy(user.getFullName());

		// Rfid update
		RfidTruck rfidTruckUpdate = new RfidTruck();
		rfidTruckUpdate.setGatePass(logisticTruck.getGatepass());
		if (logisticTruck.getWgt() != null) {
			rfidTruckUpdate.setLoadableWgt(Long.parseLong(logisticTruck.getWgt().toString()));
		}
		rfidTruckUpdate.setWgt(Long.parseLong(logisticTruck.getSelfWgt().toString()));
		rfidTruckUpdate.setPlateNumber(logisticTruck.getPlateNumber());
		rfidTruckUpdate.setRfid(logisticTruck.getRfid());
		rfidTruckUpdate.setLogisticGroupId(user.getGroupId());
		if (EportConstants.TRUCK_TYPE_TRUCK_NO.equalsIgnoreCase(logisticTruck.getType())) {
			rfidTruckUpdate.setTruckType("T");
		} else {
			rfidTruckUpdate.setTruckType("C");
		}

		// Add or update rfid info
		if (StringUtils.isNotEmpty(logisticTruck.getRfid())) {
			RfidTruck rfidTruckParam = new RfidTruck();
			rfidTruckParam.setPlateNumber(logisticTruck.getPlateNumber());
			rfidTruckParam.setLogisticGroupId(user.getGroupId());
			List<RfidTruck> rfidTrucks = rfidTruckService.selectRfidTruckList(rfidTruckParam);
			if (CollectionUtils.isNotEmpty(rfidTrucks)) {
				for (RfidTruck rfidTruck : rfidTrucks) {
					rfidTruckUpdate.setId(rfidTruck.getId());
					rfidTruckUpdate.setUpdateBy(user.getFullName());
					rfidTruckService.updateRfidTruck(rfidTruckUpdate);
				}
			} else {
				rfidTruckUpdate.setCreateBy(user.getFullName());
				rfidTruckService.insertRfidTruck(rfidTruckUpdate);
			}
		}

        return toAjax(logisticTruckService.insertLogisticTruck(logisticTruck));
    }

    /**
     * UpdateLogisticTruck
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
		LogisticTruck logisticTruck = logisticTruckService.selectLogisticTruckByIdWithRfid(id);
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
	@Transactional
    public AjaxResult editSave(LogisticTruck logisticTruck)
    {
    	logisticTruck.setPlateNumber(logisticTruck.getPlateNumber().trim().toUpperCase());
    	if(logisticTruckService.checkPlateNumberUnique(logisticTruck.getPlateNumber()) > 1) {
    		return error("Biển số xe này đã tồn tại!");
    	}

		LogisticAccount user = getUser();

		// Rfid update
		RfidTruck rfidTruckUpdate = new RfidTruck();
		rfidTruckUpdate.setGatePass(logisticTruck.getGatepass());
		rfidTruckUpdate.setLoadableWgt(Long.parseLong(logisticTruck.getWgt().toString()));
		if (logisticTruck.getWgt() != null) {
			rfidTruckUpdate.setWgt(Long.parseLong(logisticTruck.getSelfWgt().toString()));
		}
		rfidTruckUpdate.setPlateNumber(logisticTruck.getPlateNumber());
		rfidTruckUpdate.setRfid(logisticTruck.getRfid());
		rfidTruckUpdate.setLogisticGroupId(user.getGroupId());
		if (EportConstants.TRUCK_TYPE_TRUCK_NO.equalsIgnoreCase(logisticTruck.getType())) {
			rfidTruckUpdate.setTruckType("T");
		} else {
			rfidTruckUpdate.setTruckType("C");
		}

		// Add or update rfid info
		if (StringUtils.isNotEmpty(logisticTruck.getRfid())) {
			RfidTruck rfidTruckParam = new RfidTruck();
			rfidTruckParam.setPlateNumber(logisticTruck.getPlateNumber());
			rfidTruckParam.setLogisticGroupId(user.getGroupId());
			List<RfidTruck> rfidTrucks = rfidTruckService.selectRfidTruckList(rfidTruckParam);
			if (CollectionUtils.isNotEmpty(rfidTrucks)) {
				for (RfidTruck rfidTruck : rfidTrucks) {
					rfidTruckUpdate.setId(rfidTruck.getId());
					rfidTruckUpdate.setUpdateBy(user.getFullName());
					rfidTruckService.updateRfidTruck(rfidTruckUpdate);
				}
			} else {
				rfidTruckUpdate.setCreateBy(user.getFullName());
				rfidTruckService.insertRfidTruck(rfidTruckUpdate);
			}
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
