package vn.com.irtech.eport.logistic.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.Truck;
import vn.com.irtech.eport.logistic.service.ITruckService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * TruckController
 * 
 * @author admin
 * @date 2020-06-16
 */
@Controller
@RequestMapping("/logistic/truck")
public class TruckController extends BaseController
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
        List<Truck> list = truckService.selectTruckList(truck);
        return getDataTable(list);
    }

    /**
     * Export Truck List
     */
    @Log(title = "Truck", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Truck truck)
    {
        List<Truck> list = truckService.selectTruckList(truck);
        ExcelUtil<Truck> util = new ExcelUtil<Truck>(Truck.class);
        return util.exportExcel(list, "truck");
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
        return toAjax(truckService.deleteTruckByIds(ids));
    }
}
