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
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Shipment DetailsController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/shipmentdetail")
public class ShipmentDetailController extends BaseController
{
    private String prefix = "logistic/shipmentdetail";

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @RequiresPermissions("logistic:shipmentdetail:view")
    @GetMapping()
    public String shipmentdetail()
    {
        return prefix + "/shipmentdetail";
    }

    /**
     * Get Shipment Details List
     */
    @RequiresPermissions("logistic:shipmentdetail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ShipmentDetail shipmentDetail)
    {
        startPage();
        List<ShipmentDetail> list = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        return getDataTable(list);
    }

    /**
     * Export Shipment Details List
     */
    @RequiresPermissions("logistic:shipmentdetail:export")
    @Log(title = "Shipment Details", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ShipmentDetail shipmentDetail)
    {
        List<ShipmentDetail> list = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        ExcelUtil<ShipmentDetail> util = new ExcelUtil<ShipmentDetail>(ShipmentDetail.class);
        return util.exportExcel(list, "shipmentdetail");
    }

    /**
     * Add Shipment Details
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Shipment Details
     */
    @RequiresPermissions("logistic:shipmentdetail:add")
    @Log(title = "Shipment Details", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ShipmentDetail shipmentDetail)
    {
        return toAjax(shipmentDetailService.insertShipmentDetail(shipmentDetail));
    }

    /**
     * Update Shipment Details
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(id);
        mmap.put("shipmentDetail", shipmentDetail);
        return prefix + "/edit";
    }

    /**
     * Update Save Shipment Details
     */
    @RequiresPermissions("logistic:shipmentdetail:edit")
    @Log(title = "Shipment Details", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ShipmentDetail shipmentDetail)
    {
        return toAjax(shipmentDetailService.updateShipmentDetail(shipmentDetail));
    }

    /**
     * Delete Shipment Details
     */
    @RequiresPermissions("logistic:shipmentdetail:remove")
    @Log(title = "Shipment Details", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(shipmentDetailService.deleteShipmentDetailByIds(ids));
    }
}
