package vn.com.irtech.eport.logistic.controller;

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
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.RfidTruck;
import vn.com.irtech.eport.logistic.service.IRfidTruckService;

/**
 * RFID TruckController
 * 
 * @author Trong Hieu
 * @date 2020-11-14
 */
@Controller
@RequestMapping("/logistic/rfid")
public class RfidTruckController extends BaseController
{
    private String prefix = "logistic/rfid";

    @Autowired
    private IRfidTruckService rfidTruckService;

    @GetMapping()
    public String rfid()
    {
        return prefix + "/rfid";
    }

    /**
     * Get RFID Truck List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RfidTruck rfidTruck)
    {
        startPage();
        List<RfidTruck> list = rfidTruckService.selectRfidTruckList(rfidTruck);
        return getDataTable(list);
    }

    /**
     * Export RFID Truck List
     */
    @Log(title = "RFID Truck", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RfidTruck rfidTruck)
    {
        List<RfidTruck> list = rfidTruckService.selectRfidTruckList(rfidTruck);
        ExcelUtil<RfidTruck> util = new ExcelUtil<RfidTruck>(RfidTruck.class);
        return util.exportExcel(list, "rfid");
    }

    /**
     * Add RFID Truck
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
	 * Insert RFID Truck
	 */
    @Log(title = "RFID Truck", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RfidTruck rfidTruck)
    {
		rfidTruck.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(rfidTruckService.insertRfidTruck(rfidTruck));
    }

    /**
     * Update RFID Truck
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        RfidTruck rfidTruck = rfidTruckService.selectRfidTruckById(id);
        mmap.put("rfidTruck", rfidTruck);
        return prefix + "/edit";
    }

    /**
     * Update Save RFID Truck
     */
    @Log(title = "RFID Truck", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RfidTruck rfidTruck)
    {
        return toAjax(rfidTruckService.updateRfidTruck(rfidTruck));
    }

    /**
     * Delete RFID Truck
     */
    @Log(title = "RFID Truck", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(rfidTruckService.deleteRfidTruckByIds(ids));
    }

	@PostMapping("/disabled/change")
	@ResponseBody
	public AjaxResult changeDisabledStatus(RfidTruck rfidTruck) {
		rfidTruck.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(rfidTruckService.updateRfidTruck(rfidTruck));
	}
}
