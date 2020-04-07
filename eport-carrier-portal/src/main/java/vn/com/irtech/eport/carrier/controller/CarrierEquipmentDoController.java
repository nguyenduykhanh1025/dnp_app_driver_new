package vn.com.irtech.eport.carrier.controller;

import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONArray;

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
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;

/**
 * Exchange Delivery OrderController
 * 
 * @author ruoyi
 * @date 2020-04-06
 */
@Controller
@RequestMapping("/carrier/do")
public class CarrierEquipmentDoController extends BaseController
{
    private String prefix = "carrier/do";

    @Autowired
    private IEquipmentDoService equipmentDoService;

    @GetMapping()
    public String EquipmentDo()
    {
        return prefix + "/do";
    }

    private JSONArray equipmentDoList;

    /**
     * Get Exchange Delivery Order List
     */

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EquipmentDo equipmentDo)
    {
        List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(equipmentDo);
        return getDataTable(list);
    }
     

    @PostMapping("/listDo")
    @ResponseBody
    public Object listDo(int page)
    {
        page = page * 10;
        List<EquipmentDo> List = equipmentDoService.selectEquipmentDoListHome(page);
        return List;
    }
    /**
     * Export Exchange Delivery Order List
     */
   
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EquipmentDo equipmentDo)
    {
        List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(equipmentDo);
        ExcelUtil<EquipmentDo> util = new ExcelUtil<EquipmentDo>(EquipmentDo.class);
        return util.exportExcel(list, "do");
    }

    /**
     * Add Exchange Delivery Order
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/addDo";
    }

    /**
     * Add or Update Exchange Delivery Order
     */
 
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam(value = "equipmentDo") Optional<JSONArray> equipmentDo)
    {   
        equipmentDo.ifPresent(value -> equipmentDoList = value);
        if (equipmentDoList != null) {
            for (Object obj : equipmentDoList) {
                System.out.println(obj);
            }
        }
        return toAjax(1);
    }

    /**
     * Update Exchange Delivery Order
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EquipmentDo equipmentDo = equipmentDoService.selectEquipmentDoById(id);
        mmap.put("equipmentDo", equipmentDo);
        return prefix + "/edit";
    }

    /**
     * Update Save Exchange Delivery Order
     */
    
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EquipmentDo equipmentDo)
    {
      return toAjax(equipmentDoService.updateEquipmentDo(equipmentDo));
    }

    /**
     * Delete Exchange Delivery Order
     */
  
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
      return toAjax(equipmentDoService.deleteEquipmentDoByIds(ids));
    }
}