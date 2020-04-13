package vn.com.irtech.eport.carrier.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
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
import vn.com.irtech.eport.framework.mail.service.MailService;
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
	@Autowired
	private MailService mailService;

	@GetMapping()
	public String EquipmentDo() {
		return prefix + "/do";
	}

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
	public TableDataInfo list(EquipmentDo edo, Date fromDate, Date toDate, String voyageNo, String contNo,
			String blNo) {
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
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListExclusiveBill(edo);
		return getDataTable(list);
	}

	/**
	 * Update Exchange Delivery Order
	 */
	@GetMapping("/viewbl/{blNo}")
	public String billInfo(@PathVariable("blNo") String billOfLading, ModelMap mmap) {
		EquipmentDo equipmentDos = equipmentDoService.getBillOfLadingInfo(billOfLading);
		mmap.addAttribute("bl", equipmentDos);
		return prefix + "/billInfo";
	}
	
	@GetMapping("/searchCon")
	@ResponseBody
	public List<EquipmentDo> searchCon(String billOfLading, String contNo) {
		EquipmentDo equipmentDo = new EquipmentDo();
		equipmentDo.setBillOfLading(billOfLading);
		equipmentDo.setContainerNumber(contNo.toLowerCase());
		return equipmentDoService.selectEquipmentDoDetails(equipmentDo);
	}

	/**
	 * Update Exchange Delivery Order
	 */
	@GetMapping("/changeExpiredDate/{billOfLading}/{status}")
	public String changeExpiredDate(@PathVariable("billOfLading") String billOfLading,
			@PathVariable("status") String status, ModelMap mmap) {
		mmap.addAttribute("billOfLading", billOfLading.substring(1, billOfLading.length() - 1));
		mmap.addAttribute("status", status.substring(1, status.length() - 1));
		return prefix + "/changeExpriedDate";
	}

	// update
	@Log(title = "Update Expired Date", businessType = BusinessType.UPDATE)
	@PostMapping("/updateExpiredDate")
	@ResponseBody
	public AjaxResult updateExpiredDate(String billOfLading, Date expiredDem, boolean status) {
		Date now = new Date();
		expiredDem.setHours(23);
		expiredDem.setMinutes(59);
		expiredDem.setSeconds(59);
		if (expiredDem.getTime() >= now.getTime()) {
			if (!status) {
				EquipmentDo equipmentDo = new EquipmentDo();
				equipmentDo.setBillOfLading(billOfLading);
				equipmentDo.setExpiredDem(expiredDem);
				return toAjax(equipmentDoService.updateEquipmentDoExpiredDem(equipmentDo));
			} else
				return error("Cập nhật thất bại vì hóa đơn đã làm lệnh");
		} else {
			return error("Ngày gia hạn lệnh không được trong quá khứ");
		}

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
	public AjaxResult addSave(@RequestBody List<EquipmentDo> equipmentDos) {
		if (equipmentDos != null) {
			for (EquipmentDo e : equipmentDos) {
				e.setCarrierId(ShiroUtils.getUserId());
			}
			HashMap<String, Object> doList = new HashMap<>();
			doList.put("doList", equipmentDos);
			return toAjax(equipmentDoService.insertEquipmentDoList(doList));
    	}
    	return AjaxResult.success();
	}

	// update
	@Log(title = "Update Delivery Order", businessType = BusinessType.UPDATE)
	@PostMapping("/update")
	@ResponseBody
	public AjaxResult update(@RequestParam(value = "equipmentDo") Optional<JSONArray> equipmentDo) {
		JSONArray equipmentDoList = null;
		if (equipmentDo.isPresent()) {
			equipmentDoList = equipmentDo.get();
		}
//		equipmentDo.ifPresent(value -> equipmentDoList = value);
//		CarrierAccount currentUser = ShiroUtils.getSysUser();
		if (equipmentDoList != null) {
			String[] strList = new String[13];
			for (int index = 0; index < equipmentDoList.size(); index++) {
				// Resolve " mark in array
				String st = equipmentDoList.get(index++).toString();
				System.out.println("strList   " + st);
				strList[0] = st.substring(st.indexOf("[") + 1, st.length()).replace('"', ' ').trim();
				if (index == 1) {
					strList[0] = strList[0].substring(strList[0].indexOf("[") + 2, strList[0].length());
				}
				for (int i = 1; i < 12; i++) {
					strList[i] = equipmentDoList.get(index++).toString().replace('"', ' ').trim();
				}
				// String a = equipmentDoList.get(index).toString();
				// Resolve ]} mark in last element
				// int listSize = equipmentDoList.size();
				// if (index == listSize-1) {
				// strList[11] = strList[11].substring(0, strList[11].indexOf("]"));
				// strList[11] = a.substring(0, a.length() - 2).replace('"', ' ').trim();
				// } else {
				// strList[11] = a.substring(0, a.length() - 1).replace('"', ' ').trim();
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
	public List<EquipmentDo> getContainerCode(@RequestParam(value = "containerId[]") String containerId) {
		return equipmentDoService.getContainerListByIds(containerId);
	}

	@PostMapping("/updateExpire")
	@ResponseBody
	public AjaxResult updateExprireDem(@RequestParam(value = "containerId[]") String containerId,
			@RequestParam(value = "newDate") String newDate) {
		String[] doList = containerId.split(",");
		for (String i : doList) {
			EquipmentDo edo = new EquipmentDo();
			edo.setId(Long.parseLong(i));
			edo.setExpiredDem(AppToolUtils.formatStringToDate(newDate, "yyyy-MM-dd"));
			equipmentDoService.updateEquipmentDo(edo);
		}
		CarrierAccount currentUser = ShiroUtils.getSysUser();
		List<EquipmentDo> equipmentDos = equipmentDoService.getContainerListByIds(containerId);
		Collections.sort(equipmentDos, new BillNoComparator());
		EquipmentDo eTemp = equipmentDos.get(0);
		String conStr = "" + eTemp.getContainerNumber() + ";";
		for (int e = 1; e < equipmentDos.size(); e++) {
			if (eTemp.getBillOfLading().equals(equipmentDos.get(e).getBillOfLading())) {
				eTemp = equipmentDos.get(e);
				conStr += eTemp.getBillOfLading() + ";";
			} else {
				Map<String, Object> variables = new HashMap<>();
				variables.put("updateTime", eTemp.getUpdateTime());
				variables.put("carrierCode", eTemp.getCarrierCode());
				variables.put("billOfLading", eTemp.getBillOfLading());
				variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
				variables.put("consignee", eTemp.getConsignee());
				variables.put("expiredDem", eTemp.getExpiredDem());
				variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
				variables.put("detFreeTime", eTemp.getDetFreeTime());
				variables.put("vessel", eTemp.getVessel());
				variables.put("voyNo", eTemp.getVoyNo());
				variables.put("remark", eTemp.getRemark());
				eTemp = equipmentDos.get(e);
				conStr = "" + eTemp.getContainerNumber() + ";";
				// send email
				new Thread() {
					public void run() {
						try {
							mailService.prepareAndSend("Thông tin cập nhật DO", currentUser.getEmail(), variables,
									"dnpEmail");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
		Map<String, Object> variables = new HashMap<>();
		variables.put("updateTime", eTemp.getUpdateTime());
		variables.put("carrierCode", eTemp.getCarrierCode());
		variables.put("billOfLading", eTemp.getBillOfLading());
		variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
		variables.put("consignee", eTemp.getConsignee());
		variables.put("expiredDem", eTemp.getExpiredDem());
		variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
		variables.put("detFreeTime", eTemp.getDetFreeTime());
		variables.put("vessel", eTemp.getVessel());
		variables.put("voyNo", eTemp.getVoyNo());
		variables.put("remark", eTemp.getRemark());
		// send email
		new Thread() {
			public void run() {
				try {
					mailService.prepareAndSend("Thông tin cập nhật DO", currentUser.getEmail(), variables, "dnpEmail");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return AjaxResult.success();
	}

	class BillNoComparator implements Comparator<EquipmentDo> {
		public int compare(EquipmentDo equipmentDo1, EquipmentDo equipmentDo2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return equipmentDo1.getBillOfLading().compareTo(equipmentDo2.getBillOfLading());
		}
  }
  
  @GetMapping("/getOperateCode")
  @ResponseBody
  public String[] getOperateCode() {
    CarrierAccount carrierAccount = ShiroUtils.getSysUser();
    String[] operateCodes = carrierAccount.getCarrierCode().split(",");
    return operateCodes;
  }

  @GetMapping("/getInfoBl")
  @ResponseBody
  public List<EquipmentDo> getInfoBl(String blNo) {
    List<EquipmentDo> doList = equipmentDoService.selectEquipmentDoVoByBillNo(blNo);
    return doList;
  }
}