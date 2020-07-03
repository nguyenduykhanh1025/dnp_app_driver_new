package vn.com.irtech.eport.equipment.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
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
public class EquipmentDoController extends BaseController {
	private String prefix = "carrier/do";

	@Autowired
	private IEquipmentDoService equipmentDoService;

	@GetMapping("/getViewDo")
	public String getDoView() {
		return prefix + "/do";
	}

	/**
	 * GET Delivery Order
	 */
	@RequiresPermissions("equipment:do:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo listBuild(EquipmentDo edo, Date fromDate, Date toDate, String voyageNo, String vessel,
			String consignee, String blNo, String carrierCode, String status, String documentStatus) {
		startPage();
		
		if(fromDate != null)
		{
			edo.setFromDate(fromDate);
		}
		if(toDate != null)
		{
			edo.setToDate(toDate);
		}
		edo.setVoyNo(voyageNo);
		if (vessel != null) {
			edo.setVessel(vessel.toLowerCase());
		}
		if (consignee != null) {
			edo.setConsignee(consignee.toLowerCase());
		}
		edo.setBillOfLading(blNo);
		if (carrierCode != null) {
			edo.setCarrierCode(carrierCode.toLowerCase());
		}
		edo.setDocumentStatus(documentStatus);
		edo.setStatus(status);
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListExclusiveBill(edo);
		return getDataTable(list);
	}

	@GetMapping("/getViewCont/{getBillOfLading}")
	public String getViewCont(@PathVariable("getBillOfLading") String billOfLading, ModelMap mmap) {
		EquipmentDo equipmentDos = equipmentDoService.getBillOfLadingInfo(billOfLading);
		mmap.addAttribute("billOfLading", equipmentDos);
		return prefix + "/listContainer";
	}

	@RequiresPermissions("equipment:do:list")
	@PostMapping("/listCont/{blNo}")
	@ResponseBody
	public TableDataInfo list(@PathVariable("blNo") String blNo, EquipmentDoPaging EquipmentDo) {
		EquipmentDo.setBillOfLading(blNo);
		List<EquipmentDo> list = equipmentDoService.selectEquipmentDoListPagingAdmin(EquipmentDo);
		return getDataTable(list);
	}

	@RequiresPermissions("equipment:do:list")
	@PostMapping("/getCountPages")
	@ResponseBody
	public Long getCountPages(String billOfLading) {
		return equipmentDoService.getTotalPagesCont(billOfLading);
	}

	@RequiresPermissions("equipment:do:list")
	@GetMapping("/checkStatus/{billOfLading}")
	public String checkStatus(@PathVariable("billOfLading") String blNo, ModelMap mmap) {
		EquipmentDo equipmentDos = equipmentDoService.getBillOfLadingInfo(blNo);
		mmap.addAttribute("equipmentDos", equipmentDos);
		return prefix + "/checkStatus";
	}

	@RequiresPermissions("equipment:do:edit")
	@PostMapping("/updateDoStatus/{blNo}")
	@ResponseBody
	public AjaxResult updateDoStatus(@PathVariable("blNo") String blNo,EquipmentDo edo) {
		// check B/L for authentication
		EquipmentDo blItem = equipmentDoService.getBillOfLadingInfo(blNo);
		if(blItem == null) {
			return error("Số vận đơn (B/L No) không tồn tại");
		}
		if(StringUtils.isAllBlank(edo.getStatus(), edo.getDocumentStatus(), edo.getProcessRemark())) {
			return error("Không phát sinh thay đổi");
		}
		if(edo.getStatus() != null && "1".equals(edo.getStatus())) {
			blItem.setStatus(edo.getStatus());
			blItem.setProcessStatus(edo.getStatus());
		}
		if(edo.getDocumentStatus() != null && "1".equals(edo.getDocumentStatus())) {
			blItem.setDocumentStatus(edo.getDocumentStatus());
			blItem.setDocumentReceiptDate(new Date());
		}
		blItem.setprocessRemark(edo.getProcessRemark());

		blItem.setUpdateBy(ShiroUtils.getSysUser().getLoginName());
		
		if(equipmentDoService.updateBillOfLading(blItem) > 0) {
			return success("Xác nhận thành công");
		}
		
		return error("Có lỗi xảy ra khi cập nhật dữ liệu, hãy thử lại.");
	}

}
