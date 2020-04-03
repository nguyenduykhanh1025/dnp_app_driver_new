package vn.com.irtech.eport.edo.controller;

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
import vn.com.irtech.eport.edo.domain.EquipmentDocument;
import vn.com.irtech.eport.edo.service.IEquipmentDocumentService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Equipment attached documentController
 * 
 * @author irtech
 * @date 2020-04-03
 */
@Controller
@RequestMapping("/edo/document")
public class EquipmentDocumentController extends BaseController
{
    private String prefix = "edo/document";

    @Autowired
    private IEquipmentDocumentService equipmentDocumentService;

    @RequiresPermissions("edo:document:view")
    @GetMapping()
    public String document()
    {
        return prefix + "/document";
    }

    /**
     * Get Equipment attached document List
     */
    @RequiresPermissions("edo:document:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EquipmentDocument equipmentDocument)
    {
        startPage();
        List<EquipmentDocument> list = equipmentDocumentService.selectEquipmentDocumentList(equipmentDocument);
        return getDataTable(list);
    }

    /**
     * Export Equipment attached document List
     */
    @RequiresPermissions("edo:document:export")
    @Log(title = "Equipment attached document", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EquipmentDocument equipmentDocument)
    {
        List<EquipmentDocument> list = equipmentDocumentService.selectEquipmentDocumentList(equipmentDocument);
        ExcelUtil<EquipmentDocument> util = new ExcelUtil<EquipmentDocument>(EquipmentDocument.class);
        return util.exportExcel(list, "document");
    }

    /**
     * Add Equipment attached document
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Equipment attached document
     */
    @RequiresPermissions("edo:document:add")
    @Log(title = "Equipment attached document", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EquipmentDocument equipmentDocument)
    {
        return toAjax(equipmentDocumentService.insertEquipmentDocument(equipmentDocument));
    }

    /**
     * Update Equipment attached document
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EquipmentDocument equipmentDocument = equipmentDocumentService.selectEquipmentDocumentById(id);
        mmap.put("equipmentDocument", equipmentDocument);
        return prefix + "/edit";
    }

    /**
     * Update Save Equipment attached document
     */
    @RequiresPermissions("edo:document:edit")
    @Log(title = "Equipment attached document", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EquipmentDocument equipmentDocument)
    {
        return toAjax(equipmentDocumentService.updateEquipmentDocument(equipmentDocument));
    }

    /**
     * Delete Equipment attached document
     */
    @RequiresPermissions("edo:document:remove")
    @Log(title = "Equipment attached document", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(equipmentDocumentService.deleteEquipmentDocumentByIds(ids));
    }
}
