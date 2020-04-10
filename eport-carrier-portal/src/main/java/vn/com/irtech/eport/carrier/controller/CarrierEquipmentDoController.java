package vn.com.irtech.eport.carrier.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.AppToolUtils;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * Exchange Delivery OrderController
 * 
 * @author ruoyi
 * @date 2020-04-06
 */
@Controller
@RequestMapping("/carrier/do")
public class CarrierEquipmentDoController extends BaseController {
  private String prefix = "carrier/do";

  @Autowired
  private IEquipmentDoService equipmentDoService;

  private CarrierAccount currentUser;

  @Autowired
  private ICarrierGroupService carrierGroupService;

  @GetMapping()
  public String EquipmentDo() {
    return prefix + "/do";
  }

  private JSONArray equipmentDoList;
  private JSONArray containerIdList;

  /**
   * Get Exchange Delivery Order List
   */

  @PostMapping("/list2")
	@ResponseBody
	public TableDataInfo list(EquipmentDoPaging EquipmentDo) {
		int page = EquipmentDo.getPage();
		page = page * 10;
		EquipmentDo.setPage(page);
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListPagingCarrier(EquipmentDo);
		return getDataTable(list);
	}

	@RequestMapping("/list")
	@ResponseBody
	public TableDataInfo list(EquipmentDo edo) {
		startPage();
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(edo);
		return getDataTable(list);
	}

  /**
   * Export Exchange Delivery Order List
   */

  @Log(title = "Exchange Delivery Order", businessType = BusinessType.EXPORT)
  @PostMapping("/export")
  @ResponseBody
  public AjaxResult export(EquipmentDo equipmentDo) {
    List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(equipmentDo);
    ExcelUtil<EquipmentDo> util = new ExcelUtil<EquipmentDo>(EquipmentDo.class);
    return util.exportExcel(list, "do");
  }

  /**
   * Add Exchange Delivery Order
   */
  @GetMapping("/add")
  public String add() {
    return prefix + "/addDo";
  }

  /**
   * Add or Update Exchange Delivery Order
   * 
   * @throws ParseException
   */

  @Log(title = "Exchange Delivery Order", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  @ResponseBody
  public AjaxResult addSave(@RequestParam(value = "equipmentDo") Optional<JSONArray> equipmentDo) {
    equipmentDo.ifPresent(value -> equipmentDoList = value);
    currentUser = ShiroUtils.getSysUser();
    if (equipmentDoList != null) {
      String[] strList = new String[9];
      for (int index = 0; index < equipmentDoList.size(); index++) {
        // Resolve " mark in array
        String st = equipmentDoList.get(index++).toString();
        strList[0] = st.substring(st.indexOf("[") + 1, st.length()).replace('"', ' ').trim();
        if (index == 1) {
          strList[0] = strList[0].substring(strList[0].indexOf("[")+2, strList[0].length());
        }
        for (int i = 1; i < 8; i++) {
          strList[i] = equipmentDoList.get(index++).toString().replace('"', ' ').trim();
        }
        String a = equipmentDoList.get(index).toString();
        // Resolve ]} mark in last element
        int listSize = equipmentDoList.size();
        if (index == listSize-1) {
          strList[8] = a.substring(0, a.indexOf("]"));
          strList[8] = a.substring(0, a.length() - 2).replace('"', ' ').trim();
        } else {
          strList[8] = a.substring(0, a.length() - 1).replace('"', ' ').trim();
        }
        // Resolve null string
        for (int i = 0; i <= 8; i++) {
          if (strList[i].trim().equals("null")) {
            strList[i] = null;
          }
        }
        EquipmentDo equipment = new EquipmentDo();
        equipment.setCarrierId(currentUser.getId());
        equipment.setCarrierCode(carrierGroupService.selectCarrierGroupById(ShiroUtils.getUserId()).getGroupName());
        if (strList[0] != null) {
          equipment.setBillOfLading(strList[0]);
        }
        if (strList[1] != null) {
          equipment.setContainerNumber(strList[1]);
        }
        if (strList[2] != null) {
          equipment.setConsignee(strList[2]);
        }
        if (strList[3] != null) {
          equipment.setExpiredDem(AppToolUtils.formatStringToDate(strList[3], "dd/MM/yyyy"));
        }
        if (strList[4] != null) {
          equipment.setEmptyContainerDepot(strList[4]);
        }
        if (strList[5] != null) {
          equipment.setDetFreeTime(Integer.parseInt(strList[5]));
        }
        if (strList[6] != null) {
          equipment.setVessel(strList[6]);
        }
        if (strList[7] != null) {
          equipment.setVoyNo(strList[7]);
        } 
        if (strList[8] != null) {
          equipment.setRemark(strList[8]);
        }
        // set who and time created this record
        equipment.setCreateBy(currentUser.getFullName());
        equipment.setCreateTime(new Date());
        try {
          equipmentDoService.insertEquipmentDo(equipment);
        } catch (Exception e) {
          return AjaxResult.error();
        }
      }
    }
    return AjaxResult.success();
  }


  //update
  @Log(title = "Update Delivery Order", businessType = BusinessType.UPDATE)
  @PostMapping("/update")
  @ResponseBody
  public AjaxResult update(@RequestParam(value = "equipmentDo") Optional<JSONArray> equipmentDo) {
    equipmentDo.ifPresent(value -> equipmentDoList = value);
    currentUser = ShiroUtils.getSysUser();
    if (equipmentDoList != null) {
      String[] strList = new String[13];
      for (int index = 0; index < equipmentDoList.size(); index++) {
        // Resolve " mark in array
        String st = equipmentDoList.get(index++).toString();
        System.out.println("strList   "+ st);
        strList[0] = st.substring(st.indexOf("[") + 1, st.length()).replace('"', ' ').trim();
        if (index == 1) {
          strList[0] = strList[0].substring(strList[0].indexOf("[")+2, strList[0].length());
        }
        for (int i = 1; i < 12; i++) {
          strList[i] = equipmentDoList.get(index++).toString().replace('"', ' ').trim();
        }
        // String a = equipmentDoList.get(index).toString();
        // Resolve ]} mark in last element
        // int listSize = equipmentDoList.size();
        // if (index == listSize-1) {
        //   strList[11] = strList[11].substring(0, strList[11].indexOf("]"));
        //   strList[11] = a.substring(0, a.length() - 2).replace('"', ' ').trim();
        // } else {
        //   strList[11] = a.substring(0, a.length() - 1).replace('"', ' ').trim();
        // }
       
        // Insert new DO
        EquipmentDo equipment = new EquipmentDo();
        equipment.setStatus(strList[11]);
        equipment.setId(Long.parseLong(strList[0]));
        equipmentDoService.updateEquipmentDo(equipment);
      }
    }
    return toAjax(1);
  }
  /**
   * Update Exchange Delivery Order
   */
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, ModelMap mmap) {
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
  public AjaxResult editSave(EquipmentDo equipmentDo) {
    return toAjax(equipmentDoService.updateEquipmentDo(equipmentDo));
  }

  /**
   * Delete Exchange Delivery Order
   */

  @Log(title = "Exchange Delivery Order", businessType = BusinessType.DELETE)
  @PostMapping("/remove")
  @ResponseBody
  public AjaxResult remove(String ids) {
    return toAjax(equipmentDoService.deleteEquipmentDoByIds(ids));
  }

  @GetMapping("/getContainer")
  @ResponseBody
  public List<String> getContainerCode(@RequestParam(value = "containerId[]") String containerId) {
    return equipmentDoService.getContainerNumberListByIds(containerId);
  }

  @PostMapping("/updateExpire")
  @ResponseBody
  public AjaxResult updateExprireDem(@RequestParam(value = "containerId[]") String containerId, @RequestParam(value = "newDate") String newDate) {
    String[] doList = containerId.split(",");
    for (String i : doList) {
      EquipmentDo edo = new EquipmentDo();
      edo.setId(Long.parseLong(i));
      edo.setExpiredDem(AppToolUtils.formatStringToDate(newDate, "yyyy-MM-dd"));
      equipmentDoService.updateEquipmentDo(edo);
    }
    return AjaxResult.success();
  }

}