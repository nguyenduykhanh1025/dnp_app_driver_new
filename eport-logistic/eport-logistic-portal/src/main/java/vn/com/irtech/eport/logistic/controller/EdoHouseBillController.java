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

import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;

/**
 * Master BillController
 * 
 * @author irtech
 * @date 2020-08-10
 */
@Controller
@RequestMapping("/logistic/masterbill")
public class EdoHouseBillController extends BaseController
{
    private String prefix = "logistic/masterbill";

    @Autowired
    private IEdoHouseBillService edoHouseBillService;

    @RequiresPermissions("logistic:masterbill:view")
    @GetMapping()
    public String masterbill()
    {
        return prefix + "/masterbill";
    }

    /**
     * Get Master Bill List
     */
    @RequiresPermissions("logistic:masterbill:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EdoHouseBill edoHouseBill)
    {
        startPage();
        List<EdoHouseBill> list = edoHouseBillService.selectEdoHouseBillList(edoHouseBill);
        return getDataTable(list);
    }

    /**
     * Export Master Bill List
     */
    @RequiresPermissions("logistic:masterbill:export")
    @Log(title = "Master Bill", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EdoHouseBill edoHouseBill)
    {
        List<EdoHouseBill> list = edoHouseBillService.selectEdoHouseBillList(edoHouseBill);
        ExcelUtil<EdoHouseBill> util = new ExcelUtil<EdoHouseBill>(EdoHouseBill.class);
        return util.exportExcel(list, "masterbill");
    }

    /**
     * Add Master Bill
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存Master Bill
     */
    @RequiresPermissions("logistic:masterbill:add")
    @Log(title = "Master Bill", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EdoHouseBill edoHouseBill)
    {
        return toAjax(edoHouseBillService.insertEdoHouseBill(edoHouseBill));
    }

    /**
     * Update Master Bill
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EdoHouseBill edoHouseBill = edoHouseBillService.selectEdoHouseBillById(id);
        mmap.put("edoHouseBill", edoHouseBill);
        return prefix + "/edit";
    }

    /**
     * Update Save Master Bill
     */
    @RequiresPermissions("logistic:masterbill:edit")
    @Log(title = "Master Bill", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EdoHouseBill edoHouseBill)
    {
        return toAjax(edoHouseBillService.updateEdoHouseBill(edoHouseBill));
    }

    /**
     * Delete Master Bill
     */
    @RequiresPermissions("logistic:masterbill:remove")
    @Log(title = "Master Bill", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(edoHouseBillService.deleteEdoHouseBillByIds(ids));
    }
}
