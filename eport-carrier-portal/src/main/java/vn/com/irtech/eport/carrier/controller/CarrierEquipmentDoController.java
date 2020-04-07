package vn.com.irtech.eport.carrier.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

  /**
   * Get Exchange Delivery Order List
   */

  @PostMapping("/list")
  @ResponseBody
  public TableDataInfo list(EquipmentDo equipmentDo) {
    List<EquipmentDo> list = equipmentDoService.selectEquipmentDoList(equipmentDo);
    return getDataTable(list);
  }

  @PostMapping("/listDo")
  @ResponseBody
  public Object listDo(int page) {
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
      String[] strList = new String[10];
      for (int index = 0; index < equipmentDoList.size(); index++) {
        // Resolve " mark in array
        String st = equipmentDoList.get(index++).toString();
        strList[0] = st.substring(st.indexOf("[") + 1, st.length());
        for (int i = 1; i < 9; i++) {
          strList[i] = equipmentDoList.get(index++).toString().replace('"', ' ').trim();
        }
        String a = equipmentDoList.get(index).toString();
        // Resolve ] mark in last element
        if (index == equipmentDoList.size()) {
          strList[9] = a.substring(0, a.length() - 2);
        } else {
          strList[9] = a.substring(0, a.length() - 1);
        }
        // Resolve null string
        for (int i = 0; i <= 9; i++) {
          if (strList[i].trim().equals("null")) {
            strList[i] = null;
          }
        }
        // Insert new DO
        EquipmentDo equipment = new EquipmentDo();
        equipment.setCarrierId(currentUser.getId());
        equipment.setCarrierCode("1234");
        equipment.setBillOfLading(strList[1]);
        equipment.setContainerNumber(strList[2]);
        equipment.setConsignee(strList[3]);
        equipment.setExpiredDem(AppToolUtils.formatStringToDate(strList[4], "dd/MM/yyyy"));
        equipment.setEmptyContainerDepot(strList[5]);
        if (strList[6] != null) {
          equipment.setDetFreeTime(Integer.parseInt(strList[6]));
        }
        equipment.setVessel(strList[7]);
        equipment.setVoyNo(strList[8]);
        equipment.setRemark(strList[9]);
        // set who created this record
        equipment.setCreateBy(currentUser.getFullName());
        equipment.setCreateTime(new Date());
        // Insert to database
        equipmentDoService.insertEquipmentDo(equipment);
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

  @GetMapping("/getCarrierCode")
  @ResponseBody
  public String getCarrierCode() {
    return carrierGroupService.selectCarrierGroupById(ShiroUtils.getUserId()).getGroupName();
  }

}