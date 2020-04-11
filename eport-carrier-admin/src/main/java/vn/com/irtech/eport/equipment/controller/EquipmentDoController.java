package vn.com.irtech.eport.equipment.controller;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * Exchange Delivery OrderController
 * 
 * @author irtech
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/carrier/admin/do")
public class EquipmentDoController extends BaseController
{
    private String prefix = "carrier/do";
	
	@Autowired
 	private IEquipmentDoService equipmentDoService;

  	@GetMapping("/getListView")
	public String getDoView()
	{
		return  prefix + "/do";
    }
    
    // @GetMapping("/getViewDo/{billOfLading}")
    // public String getContView(@PathVariable("billOfLading") String billOfLading,Model model)
    // {
    //     model.addAttribute("name","Fire dragon");
    //     return prefix + "/listContainer";
    // }
    @GetMapping("/getViewDo")
    public String getContView()
    {
        return prefix + "/listContainer";
    }
	/**
     * GET Delivery Order
     */
	
    @RequestMapping("/list")
	@ResponseBody
	public TableDataInfo list(EquipmentDo edo, Date fromDate, Date toDate, String voyageNo, String contNo, String blNo) {
    startPage();
    edo.setCarrierId(ShiroUtils.getUserId());
    if (voyageNo != null) {
      edo.setVoyNo(voyageNo);
    }
    if (contNo != null) {
      edo.setContainerNumber(contNo);
    }
    if (blNo != null) {
      edo.setBillOfLading(blNo);
    }
    if (fromDate != null) {
      edo.setFromDate(fromDate);
    }
    if (toDate != null) {
      edo.setToDate(toDate);
    }
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(edo);
		return getDataTable(list);
	}

    @PostMapping("/listCont")
	@ResponseBody
	public TableDataInfo list(EquipmentDoPaging EquipmentDo) {
		int page = EquipmentDo.getPage();
		page = page * 15;
		EquipmentDo.setPage(page);
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListPagingAdmin(EquipmentDo);
		return getDataTable(list);
	}
	// Return panination
	@PostMapping("/getCountPages")
	@ResponseBody
	public Long getCountPages()
	{
		return equipmentDoService.getTotalPages();
	}
    /**
     * Update Exchange Delivery Order
     */
    @RequiresPermissions("equipment:do:add")
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EquipmentDo equipmentDo)
    {
        return toAjax(equipmentDoService.insertEquipmentDo(equipmentDo));
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
    @RequiresPermissions("equipment:do:edit")
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
    @RequiresPermissions("equipment:do:remove")
    @Log(title = "Exchange Delivery Order", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(equipmentDoService.deleteEquipmentDoByIds(ids));
    }

	
}   
