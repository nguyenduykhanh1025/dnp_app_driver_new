package vn.com.irtech.eport.equipment.controller;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysUser;

/**
 * Exchange Delivery OrderController
 * 
 * @author irtech
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/carrier/admin/do")
public class EquipmentDoController extends BaseController {
	private String prefix = "carrier/do";

	@Autowired
	private IEquipmentDoService equipmentDoService;

	private SysUser currentUser;

	@GetMapping("/getViewDo")
	public String getDoView() {
		return prefix + "/do";
	}

	/**
	 * GET Delivery Order
	 */
	@RequiresPermissions("system:do:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo listBuild(EquipmentDo edo, Date fromDate, Date toDate, String voyageNo, String vessel,
			String consignee, String blNo, String carrierCode, String status, String documentStatus) {
		startPage();
		edo.setVoyNo(voyageNo);
		edo.setVessel(vessel);
		edo.setConsignee(consignee);
		edo.setBillOfLading(blNo);
		edo.setCarrierCode(carrierCode);
		edo.setFromDate(fromDate);
		edo.setToDate(toDate);
		edo.setDocumentStatus(documentStatus);
		edo.setStatus(status);
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListExclusiveBill(edo);
		return getDataTable(list);
	}

	@GetMapping("/getViewCont/{getBillOfLading}")
	public String getViewCont(@PathVariable("getBillOfLading") String getBillOfLading, Model model) {
		model.addAttribute("getBillOfLading", getBillOfLading);
		return prefix + "/listContainer";
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
	public Long getCountPages(String billOfLading) {

		return equipmentDoService.getTotalPagesCont(billOfLading);
	}

	/**
	 * Update Exchange Delivery Order
	 */
	@RequiresPermissions("equipment:do:add")
	@Log(title = "Exchange Delivery Order", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(EquipmentDo equipmentDo) {
		return toAjax(equipmentDoService.insertEquipmentDo(equipmentDo));
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

	@RequiresPermissions("equipment:do:edit")
	@Log(title = "Exchange Delivery Order", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(EquipmentDo EquipmentDo) {
		return toAjax(equipmentDoService.updateEquipmentDo(EquipmentDo));
	}

//	/**
//	 * Delete Exchange Delivery Order
//	 */
//	@RequiresPermissions("equipment:do:remove")
//	@Log(title = "Exchange Delivery Order", businessType = BusinessType.DELETE)
//	@PostMapping("/remove")
//	@ResponseBody
//	public AjaxResult remove(String ids) {
//		return toAjax(equipmentDoService.deleteEquipmentDoByIds(ids));
//    }

	@GetMapping("/checkStatus/{billOfLading}")
	public String checkStatus(@PathVariable("billOfLading") String blNo, ModelMap mmap) {
		EquipmentDo equipmentDos = equipmentDoService.getBillOfLadingInfo(blNo);
		mmap.addAttribute("equipmentDos", equipmentDos);
		return prefix + "/checkStatus";
	}

	@PostMapping("/updateDoStatus")
	@ResponseBody
	public AjaxResult updateDoStatus(String billOfLading, String status, String documentStatus, String note) {
		EquipmentDo equipmentDo = new EquipmentDo();
		boolean checkUpdate = false;
		if (documentStatus.equals("1") && equipmentDoService.countDocmentStatusYes(billOfLading) == 0) {
			Date documentReceiptDate = new Date();
			currentUser = ShiroUtils.getSysUser();
			equipmentDo.setDocumentStatus("1");
			equipmentDo.setUpdateBy(currentUser.getLoginName());
			equipmentDo.setDocumentReceiptDate(documentReceiptDate);
			equipmentDo.setBillOfLading(billOfLading);
			equipmentDoService.updateBillOfLading(equipmentDo);
			checkUpdate = true;
		}
		if (status.equals("1") && equipmentDoService.countDOStatusYes(billOfLading) == 0) {
			Date documentReceiptDate = new Date();
			currentUser = ShiroUtils.getSysUser();
			equipmentDo.setStatus("1");
			equipmentDo.setUpdateBy(currentUser.getLoginName());
			equipmentDo.setUpdateTime(documentReceiptDate);
			equipmentDo.setBillOfLading(billOfLading);
			equipmentDoService.updateBillOfLading(equipmentDo);
			checkUpdate = true;
		}
		if(checkUpdate = false)
		{
			return error();
		}
		return success();
	}

}
