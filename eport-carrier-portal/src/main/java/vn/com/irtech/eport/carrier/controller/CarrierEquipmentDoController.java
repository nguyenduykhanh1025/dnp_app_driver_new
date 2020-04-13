package vn.com.irtech.eport.carrier.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.AppToolUtils;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.mail.service.MailService;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * Exchange Delivery OrderController
 * 
 * @author admin
 * @date 2020-04-06
 */
@Controller
@RequestMapping("/carrier/do")
public class CarrierEquipmentDoController extends CarrierBaseController {
	
    private final String prefix = "carrier/do";
    
    private static final Pattern VALID_CONTAINER_NO_REGEX = Pattern.compile("^[A-Za-z]{4}[0-9]{7}$", Pattern.CASE_INSENSITIVE);

  
	@Autowired
	private IEquipmentDoService equipmentDoService;
	@Autowired
	private MailService mailService;

	@GetMapping()
	public String EquipmentDo() {
		return prefix + "/do";
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

//	/**
//	 * Update Exchange Delivery Order
//	 */
//	@GetMapping("/changeExpiredDate/{billOfLading}/{status}")
//	public String changeExpiredDate(@PathVariable("billOfLading") String billOfLading,
//			@PathVariable("status") String status, ModelMap mmap) {
//		mmap.addAttribute("billOfLading", billOfLading.substring(1, billOfLading.length() - 1));
//		mmap.addAttribute("status", status.substring(1, status.length() - 1));
//		return prefix + "/changeExpriedDate";
//	}
//
//	// update
//	@Log(title = "Update Expired Date", businessType = BusinessType.UPDATE)
//	@PostMapping("/updateExpiredDate")
//	@ResponseBody
//	public AjaxResult updateExpiredDate(String billOfLading, Date expiredDem, boolean status) {
//		Date now = new Date();
//		expiredDem.setHours(23);
//		expiredDem.setMinutes(59);
//		expiredDem.setSeconds(59);
//		if (expiredDem.getTime() >= now.getTime()) {
//			if (!status) {
//				EquipmentDo equipmentDo = new EquipmentDo();
//				equipmentDo.setBillOfLading(billOfLading);
//				equipmentDo.setExpiredDem(expiredDem);
//				return toAjax(equipmentDoService.updateEquipmentDoExpiredDem(equipmentDo));
//			} else
//				return error("Cập nhật thất bại vì hóa đơn đã làm lệnh");
//		} else {
//			return error("Ngày gia hạn lệnh không được trong quá khứ");
//		}
//
//	}

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
		//String message = userService.importUser(userList, updateSupport, operName);
        //return AjaxResult.success(message);
		if (equipmentDos != null) {
			for (EquipmentDo e : equipmentDos) {
				e.setCarrierId(getUserId());
				if (StringUtils.isBlank(e.getCarrierCode()) || StringUtils.isBlank(e.getBillOfLading())
						|| StringUtils.isBlank(e.getContainerNumber()) || StringUtils.isBlank(e.getConsignee())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '"+e.getContainerNumber()+"'.<br/>Lỗi: Hãy nhập đầy đủ các trường bắt buộc.");
				}
				// Check if carrier group code is valid
				if(!getGroupCodes().contains(e.getCarrierCode())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '"+e.getContainerNumber()+"'.<br/>Lỗi: Mã hãng tàu '"+e.getCarrierCode()+"' không đúng.");
				}
				if(equipmentDoService.getBillOfLadingInfo(e.getBillOfLading()) != null) {
					// exist B/L
					return AjaxResult.error("Có lỗi xảy ra ở container '"+e.getContainerNumber()+"'.<br/>Lỗi: Mã vận đơn (B/L No.) " + e.getBillOfLading() +" đã tồn tại.");
				}
				if(!isContainerNumber(e.getContainerNumber())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '"+e.getContainerNumber()+"'.<br/>Lỗi: Mã container không đúng tiêu chuẩn.");
				}
				// trong 1 bill ton tai 2 container
				
				// Check expiredDem is future
				Date expiredDem = e.getExpiredDem();
				expiredDem.setHours(23);
				expiredDem.setMinutes(59);
				expiredDem.setSeconds(59);
				e.setExpiredDem(expiredDem);
				if(expiredDem.before(new Date())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '"+e.getContainerNumber()+"'.<br/>Lỗi: Hạn lệnh không được phép là ngày quá khứ");
				}
				// DEM Free date la so
				
			}
			// Do the insert to DB
			for(EquipmentDo edo : equipmentDos) {
				equipmentDoService.insertEquipmentDo(edo);
			}
			// return toAjax(equipmentDoService.insertEquipmentDoList(doList));

			// SEND EMAIL WHEN ADD SUCCESSFULLY
			new Thread() {
				public void run() {
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
							try {
								mailService.prepareAndSend("Bill "+variables.get("billOfLading")+": thêm thành công", getUser().getEmail(), variables,
										"dnpEmail");
							} catch (Exception exception) {
								exception.printStackTrace();
							}
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
					try {
						mailService.prepareAndSend("Bill "+variables.get("billOfLading")+": cập nhật thành công", getUser().getEmail(), variables,
								"dnpEmail");
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}.start();
			// END SEND EMAIL
			return AjaxResult.success("Đã lưu thành công " + equipmentDos.size() + " DO lên Web Portal.");
    	}
    	return AjaxResult.error("Không có dữ liệu để tạo DO, hãy kiểm tra lại");
	}

	// update
	@Log(title = "Update Delivery Order", businessType = BusinessType.UPDATE)
	@PostMapping("/update")
	@ResponseBody
	public AjaxResult update(@RequestBody List<EquipmentDo> equipmentDos) {
		if (equipmentDos != null) {
			for (EquipmentDo e : equipmentDos) {
				e.setUpdateBy(ShiroUtils.getSysUser().getFullName());
				e.setUpdateTime(new Date());
			}
			for(EquipmentDo edo : equipmentDos) {
				if (edo.getId() != null) {
					equipmentDoService.updateEquipmentDo(edo);
				} else {
					equipmentDoService.insertEquipmentDo(edo);
				}				
			}
			// SEND EMAIL WHEN ADD SUCCESSFULLY
			new Thread() {
				public void run() {
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
							try {
								mailService.prepareAndSend("Bill "+variables.get("billOfLading")+": cập nhật thành công", getUser().getEmail(), variables,
										"dnpEmail");
							} catch (Exception exception) {
								exception.printStackTrace();
							}
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
					try {
						mailService.prepareAndSend("Bill "+variables.get("billOfLading")+": cập nhật thành công", getUser().getEmail(), variables,
								"dnpEmail");
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}.start();
			// END SEND EMAIL
			return AjaxResult.success("Đã cập nhật thành công " + equipmentDos.size() + " DO lên Web Portal.");
    	}
    	return AjaxResult.success();
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
	if (doList.size() !=0) {
		if (doList.get(0).getCarrierId() == getUserId()) {
			return doList;
		}
	}
    return null;
  }
  
  private boolean isContainerNumber(String input) {
	  return VALID_CONTAINER_NO_REGEX.matcher(input).find();
  }
}