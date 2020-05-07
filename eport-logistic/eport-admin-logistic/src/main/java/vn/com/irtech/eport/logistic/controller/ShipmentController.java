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
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * ShipmentController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/shipment")
public class ShipmentController extends BaseController
{
    private String prefix = "logistic/shipment";

    @Autowired
    private IShipmentService shipmentService;

    @RequiresPermissions("logistic:shipment:view")
    @GetMapping()
    public String shipment()
    {
        return prefix + "/shipment";
    }

    /**
     * Get Shipment List
     */
    @RequiresPermissions("logistic:shipment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Shipment shipment)
    {
        startPage();
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        return getDataTable(list);
    }

    /**
     * Export Shipment List
     */
    @RequiresPermissions("logistic:shipment:export")
    @Log(title = "Shipment", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Shipment shipment)
    {
        List<Shipment> list = shipmentService.selectShipmentList(shipment);
        ExcelUtil<Shipment> util = new ExcelUtil<Shipment>(Shipment.class);
        return util.exportExcel(list, "shipment");
    }

    /**
     * Add Shipment
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Shipment
     */
    @RequiresPermissions("logistic:shipment:add")
    @Log(title = "Shipment", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Shipment shipment)
    {
        return toAjax(shipmentService.insertShipment(shipment));
    }

    /**
     * Update Shipment
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Shipment shipment = shipmentService.selectShipmentById(id);
        mmap.put("shipment", shipment);
        return prefix + "/edit";
    }

    /**
     * Update Save Shipment
     */
    @RequiresPermissions("logistic:shipment:edit")
    @Log(title = "Shipment", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Shipment shipment)
    {
        return toAjax(shipmentService.updateShipment(shipment));
    }

    /**
     * Delete Shipment
     */
    @RequiresPermissions("logistic:shipment:remove")
    @Log(title = "Shipment", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(shipmentService.deleteShipmentByIds(ids));
    }
}
