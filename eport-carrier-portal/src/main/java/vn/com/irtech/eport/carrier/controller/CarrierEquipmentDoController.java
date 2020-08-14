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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.domain.EquipmentDoAuditLog;
import vn.com.irtech.eport.carrier.service.IEquipmentDoAuditLogService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.AppToolUtils;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;
import vn.com.irtech.eport.framework.mail.service.MailService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;

/**
 * Exchange Delivery OrderController
 * 
 * @author admin
 * @date 2020-04-06
 */
@Controller
@RequestMapping("/carrier/do")
@Transactional
public class CarrierEquipmentDoController extends CarrierBaseController {

	private final String PREFIX = "carrier/do";

	private static final Pattern VALID_CONTAINER_NO_REGEX = Pattern.compile("^[A-Za-z]{4}[0-9]{7}$",
			Pattern.CASE_INSENSITIVE);

	@Autowired
	private IEquipmentDoService equipmentDoService;

	@Autowired
	private MailService mailService;

	@Autowired
	private IEquipmentDoAuditLogService equipmentDoAuditLogService;

	@Autowired
	private ISysDictDataService dictDataService;

	@GetMapping()
	public String EquipmentDo() {
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/do";
	}

	@RequestMapping("/list")
	@ResponseBody
	public TableDataInfo list(EquipmentDo edo, Date fromDate, Date toDate, String vessel, String contNo, String blNo) {
		startPage();
		edo.setCarrierId(ShiroUtils.getUserId());
		if (vessel != null) {
			edo.setVessel(vessel.toLowerCase());
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
		if (!hasDoPermission()) {
			return "error/404";
		}
		EquipmentDo equipmentDos = equipmentDoService.getBillOfLadingInfo(billOfLading);
		mmap.addAttribute("bl", equipmentDos);
		return PREFIX + "/billInfo";
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
	@GetMapping("/changeExpiredDate/{billOfLading}")
	public String changeExpiredDate(@PathVariable("billOfLading") String billOfLading, ModelMap mmap) {
		if (!hasDoPermission()) {
			return "error/404";
		}
		mmap.addAttribute("billOfLading", billOfLading);
		return PREFIX + "/changeExpriedDate";
	}

	// update
	@Log(title = "Gia Hạn DO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/updateExpiredDate")
	@ResponseBody
	@Transactional
	public AjaxResult updateExpiredDate(String billOfLading, String expiredDem) {
		Date newExpiredDem = AppToolUtils.formatStringToDate(expiredDem, "dd/MM/yyyy");
		Date now = new Date();
		newExpiredDem.setHours(23);
		newExpiredDem.setMinutes(59);
		newExpiredDem.setSeconds(59);
		if (newExpiredDem.getTime() >= now.getTime()) {
			EquipmentDo equipmentDo = new EquipmentDo();
			equipmentDo.setBillOfLading(billOfLading);
			equipmentDo.setNewExpiredDem(newExpiredDem);
			return toAjax(equipmentDoService.updateEquipmentDoExpiredDem(equipmentDo));
		} else {
			return error("Ngày gia hạn lệnh không được trong quá khứ");
		}

	}

	/**
	 * Export Exchange Delivery Order List
	 */

	@Log(title = "Export DO", businessType = BusinessType.EXPORT)
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
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/addDo";
	}

	/**
	 * Add or Update Exchange Delivery Order
	 * 
	 * @throws ParseException
	 */

	@Log(title = "Phát Hành DO", businessType = BusinessType.INSERT, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/add")
	@ResponseBody
	@Transactional
	public AjaxResult addSave(@RequestBody List<EquipmentDo> equipmentDos) {
		// String message = userService.importUser(userList, updateSupport, operName);
		// return AjaxResult.success(message);
		if (!hasDoPermission()) {
			return error("Tài khoản này không có quyền phát hành DO");
		}
		if (equipmentDos != null) {
			String consignee = equipmentDos.get(0).getConsignee();
			String containerNumber = "";
			for (EquipmentDo e : equipmentDos) {
				e.setCarrierId(super.getUser().getGroupId());
				e.setCreateBy(getUser().getFullName());
				e.setUpdateTime(new Date());
				e.setUpdateBy(getUser().getFullName());
				if (StringUtils.isBlank(e.getCarrierCode()) || StringUtils.isBlank(e.getBillOfLading())
						|| StringUtils.isBlank(e.getContainerNumber()) || StringUtils.isBlank(e.getConsignee())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Hãy nhập đầy đủ các trường bắt buộc.");
				}
				// Check if carrier group code is valid
				if (!getGroupCodes().contains(e.getCarrierCode())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã hãng tàu '" + e.getCarrierCode() + "' không đúng.");
				}
				if (equipmentDoService.getBillOfLadingInfo(e.getBillOfLading()) != null) {
					// exist B/L
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã vận đơn (B/L No.) " + e.getBillOfLading() + " đã tồn tại.");
				}
				if (!isContainerNumber(e.getContainerNumber())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã container không đúng tiêu chuẩn.");
				}
				// trong 1 bill ton tai 2 container

				// Check expiredDem is future
				Date expiredDem = e.getExpiredDem();
				expiredDem.setHours(23);
				expiredDem.setMinutes(59);
				expiredDem.setSeconds(59);
				e.setExpiredDem(expiredDem);
				if (expiredDem.before(new Date())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Hạn lệnh không được phép là ngày quá khứ");
				}
				// DEM Free date la so
				if (e.getDetFreeTime() != null && e.getDetFreeTime() >= 10000) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Ngày miễn lưu không được lớn hơn 9999");
				}
				// Consignee is the same
				if (!e.getConsignee().equals(consignee)) {
					return AjaxResult.error("Tên khách hàng không được khác nhau");
				}
				consignee = e.getConsignee();
				// Container number is unique
				if (e.getContainerNumber().equals(containerNumber)) {
					return AjaxResult.error("Số container " + containerNumber + " bị trùng");
				}
				containerNumber = e.getContainerNumber();

			}
			// Do the insert to DB
			for (EquipmentDo edo : equipmentDos) {
				edo.setStatus("1");
				equipmentDoService.insertEquipmentDo(edo);
				edo.setCreateBy(super.getUser().getEmail());
				equipmentDoAuditLogService.addAuditLogFirst(edo);
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
							conStr += eTemp.getContainerNumber() + ";";
						} else {
							Map<String, Object> variables = new HashMap<>();
							variables.put("updateTime",
									AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
							variables.put("carrierCode", eTemp.getCarrierCode());
							variables.put("billOfLading", eTemp.getBillOfLading());
							variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
							variables.put("consignee", eTemp.getConsignee());
							variables.put("expiredDem",
									AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
							variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
							variables.put("detFreeTime", eTemp.getDetFreeTime());
							variables.put("vessel", eTemp.getVessel());
							variables.put("voyNo", eTemp.getVoyNo());
							variables.put("remark", eTemp.getRemark());
							eTemp = equipmentDos.get(e);
							conStr = "" + eTemp.getContainerNumber() + ";";
							// send email
							try {
								mailService.prepareAndSend(
										"Bill " + variables.get("billOfLading") + ": thêm thành công",
										getUser().getEmail(), "", variables, "dnpEmail");
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					}
					Map<String, Object> variables = new HashMap<>();
					variables.put("updateTime",
							AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
					variables.put("carrierCode", eTemp.getCarrierCode());
					variables.put("billOfLading", eTemp.getBillOfLading());
					variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
					variables.put("consignee", eTemp.getConsignee());
					variables.put("expiredDem",
							AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
					variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
					variables.put("detFreeTime", eTemp.getDetFreeTime());
					variables.put("vessel", eTemp.getVessel());
					variables.put("voyNo", eTemp.getVoyNo());
					variables.put("remark", eTemp.getRemark());
					// send email
					try {
						mailService.prepareAndSend("Bill " + variables.get("billOfLading") + ": thêm thành công", "",
								getUser().getEmail(), variables, "dnpEmail");
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
	@Log(title = "Cập Nhật DO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/update/{billOfLading}")
	@ResponseBody
	@Transactional
	public AjaxResult update(@RequestBody List<EquipmentDo> equipmentDos, @PathVariable("billOfLading") String bill) {
		if (getUserId() != equipmentDoService.getBillOfLadingInfo(bill).getCarrierId()) {
			return AjaxResult.error("Bạn không có quyền cập nhật DO này");
		}
		if (equipmentDos != null) {
			String containerNumber = "";
			String consignee = equipmentDos.get(0).getConsignee();
			String billOfLading = equipmentDos.get(0).getBillOfLading();
			String carrierCode = equipmentDos.get(0).getCarrierCode();
			for (EquipmentDo e : equipmentDos) {
				if (e.getStatus() == null) {
					e.setStatus("0");
				}
				if (e.getStatus().equals("1")) {
					return AjaxResult.error("Bill này đã được làm lệnh, cập nhật thất bại");
				}
				if (StringUtils.isBlank(e.getCarrierCode()) || StringUtils.isBlank(e.getContainerNumber())
						|| StringUtils.isBlank(e.getConsignee())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Hãy nhập đầy đủ các trường bắt buộc.");
				}
				// Check if carrier group code is valid
				if (!getGroupCodes().contains(e.getCarrierCode())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã hãng tàu '" + e.getCarrierCode() + "' không đúng.");
				}
				if (!isContainerNumber(e.getContainerNumber())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã container không đúng tiêu chuẩn.");
				}
				// Check expiredDem is future
				Date expiredDem = e.getExpiredDem();
				expiredDem.setHours(23);
				expiredDem.setMinutes(59);
				expiredDem.setSeconds(59);
				e.setExpiredDem(expiredDem);
				if (expiredDem.before(new Date())) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Hạn lệnh không được phép là ngày quá khứ");
				}
				// DEM Free date la so
				if (e.getDetFreeTime() != null && e.getDetFreeTime() >= 10000) {
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Ngày miễn lưu không được lớn hơn 9999");
				}
				// Container number is unique
				if (e.getContainerNumber().equals(containerNumber)) {
					return AjaxResult.error("Số container " + containerNumber + " bị trùng");
				}
				containerNumber = e.getContainerNumber();
				// Consignee is the same
				if (!e.getConsignee().equals(consignee)) {
					return AjaxResult.error("Tên khách hàng không được khác nhau");
				}
				consignee = e.getConsignee();

				e.setUpdateBy(ShiroUtils.getSysUser().getFullName());
				e.setUpdateTime(new Date());
			}
			for (EquipmentDo edo : equipmentDos) {
				if (edo.getId() != null) {
					equipmentDoService.updateEquipmentDo(edo);
					edo.setCreateBy(super.getUser().getEmail());
					equipmentDoAuditLogService.updateAuditLog(edo);
				} else {
					edo.setCarrierId(getUserId());
					edo.setBillOfLading(billOfLading);
					edo.setCarrierCode(carrierCode);
					equipmentDoService.insertEquipmentDo(edo);
					edo.setCreateBy(super.getUser().getEmail());
					equipmentDoAuditLogService.addAuditLogFirst(edo);
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
							conStr += eTemp.getContainerNumber() + ";";
						} else {
							Map<String, Object> variables = new HashMap<>();
							variables.put("updateTime",
									AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
							variables.put("carrierCode", eTemp.getCarrierCode());
							variables.put("billOfLading", eTemp.getBillOfLading());
							variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
							variables.put("consignee", eTemp.getConsignee());
							variables.put("expiredDem",
									AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
							variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
							variables.put("detFreeTime", eTemp.getDetFreeTime());
							variables.put("vessel", eTemp.getVessel());
							variables.put("voyNo", eTemp.getVoyNo());
							variables.put("remark", eTemp.getRemark());
							eTemp = equipmentDos.get(e);
							conStr = "" + eTemp.getContainerNumber() + ";";
							// send email
							try {
								mailService.prepareAndSend(
										"Bill " + variables.get("billOfLading") + ": cập nhật thành công", "",
										getUser().getEmail(), variables, "dnpEmail");
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					}
					Map<String, Object> variables = new HashMap<>();
					variables.put("updateTime",
							AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
					variables.put("carrierCode", eTemp.getCarrierCode());
					variables.put("billOfLading", eTemp.getBillOfLading());
					variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
					variables.put("consignee", eTemp.getConsignee());
					variables.put("expiredDem",
							AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
					variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
					variables.put("detFreeTime", eTemp.getDetFreeTime());
					variables.put("vessel", eTemp.getVessel());
					variables.put("voyNo", eTemp.getVoyNo());
					variables.put("remark", eTemp.getRemark());
					// send email
					try {
						mailService.prepareAndSend("Bill " + variables.get("billOfLading") + ": cập nhật thành công",
								"", getUser().getEmail(), variables, "dnpEmail");
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}.start();
			// END SEND EMAIL
			return AjaxResult.success("Đã cập nhật thành công " + equipmentDos.size() + " DO lên Web Portal.");
		}
		return AjaxResult.error("Không có dữ liệu để cập nhật DO, hãy kiểm tra lại");
	}

	/**
	 * Update Exchange Delivery Order
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		if (!hasDoPermission()) {
			return "error/404";
		}
		EquipmentDo equipmentDo = equipmentDoService.selectEquipmentDoById(id);
		mmap.put("equipmentDo", equipmentDo);
		return PREFIX + "/edit";
	}

	/**
	 * Update Save Exchange Delivery Order
	 */

	@Log(title = "Cập Nhật DO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(EquipmentDo equipmentDo) {
		return toAjax(equipmentDoService.updateEquipmentDo(equipmentDo));
	}

	/**
	 * Delete Exchange Delivery Order
	 */

	@Log(title = "Xóa DO", businessType = BusinessType.DELETE, operatorType = OperatorType.SHIPPINGLINE)
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

	@Log(title = "Gia Hạn DO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/updateExpire")
	@ResponseBody
	public AjaxResult updateExprireDem(@RequestParam(value = "containerId[]") String containerId,
			@RequestParam(value = "newDate") String newDate) {
		String[] doList = containerId.split(",");
		for (String i : doList) {
			EquipmentDo edo = new EquipmentDo();
			edo.setId(Long.parseLong(i));
			edo.setExpiredDem(AppToolUtils.formatStringToDate(newDate, "yyyy-MM-dd"));
			edo.setUpdateTime(new Date());
			equipmentDoService.updateEquipmentDo(edo);
		}
		List<EquipmentDo> equipmentDos = equipmentDoService.getContainerListByIds(containerId);
		Collections.sort(equipmentDos, new BillNoComparator());
		EquipmentDo eTemp = equipmentDos.get(0);
		String conStr = "" + eTemp.getContainerNumber() + ";";
		for (int e = 1; e < equipmentDos.size(); e++) {
			if (eTemp.getBillOfLading().equals(equipmentDos.get(e).getBillOfLading())) {
				eTemp = equipmentDos.get(e);
				conStr += eTemp.getContainerNumber() + ";";
			} else {
				Map<String, Object> variables = new HashMap<>();
				variables.put("updateTime",
						AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
				variables.put("carrierCode", eTemp.getCarrierCode());
				variables.put("billOfLading", eTemp.getBillOfLading());
				variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
				variables.put("consignee", eTemp.getConsignee());
				variables.put("expiredDem",
						AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
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
							mailService.prepareAndSend(
									"Bill " + variables.get("billOfLading") + ": cập nhật thành công", "",
									getUser().getEmail(), variables, "dnpEmail");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
		Map<String, Object> variables = new HashMap<>();
		variables.put("updateTime", AppToolUtils.formatDateToString(eTemp.getUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
		variables.put("carrierCode", eTemp.getCarrierCode());
		variables.put("billOfLading", eTemp.getBillOfLading());
		variables.put("containerNumber", conStr.substring(0, conStr.length() - 1));
		variables.put("consignee", eTemp.getConsignee());
		variables.put("expiredDem", AppToolUtils.formatDateToString(eTemp.getExpiredDem(), "dd/MM/yyyy HH:mm:ss"));
		variables.put("emptyContainerDepot", eTemp.getEmptyContainerDepot());
		variables.put("detFreeTime", eTemp.getDetFreeTime());
		variables.put("vessel", eTemp.getVessel());
		variables.put("voyNo", eTemp.getVoyNo());
		variables.put("remark", eTemp.getRemark());
		// send email
		new Thread() {
			public void run() {
				try {
					mailService.prepareAndSend("Bill " + variables.get("billOfLading") + ": cập nhật thành công", "",
							getUser().getEmail(), variables, "dnpEmail");
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

	@GetMapping("/getListOptions")
	@ResponseBody
	public Map<String, List<String>> getListOptionsGrid() {
		// Get distinct of consignee, distinct of empty container depot, distinct vessel
		// Only of current login user
		// Return data Map<consigneeList: ListConsignee, emptyDepotList: ListEmptyDepot,
		// vesselList: ListVessel>
		Map<String, List<String>> optionsList = new HashMap<>();
		optionsList.put("consigneeList", equipmentDoService.getConsignee(getUserId()));
		optionsList.put("emptyDepotList", equipmentDoService.getEmptyContainerDepot(getUserId()));
		optionsList.put("vesselList", equipmentDoService.getVessel(getUserId()));
		return optionsList;
	}

	@GetMapping("/getInfoBl")
	@ResponseBody
	public List<EquipmentDo> getInfoBl(String blNo) {
		List<EquipmentDo> doList = equipmentDoService.selectEquipmentDoVoByBillNo(blNo);
		if (doList.size() != 0) {
			if (doList.get(0).getCarrierId() == getUserId()) {
				return doList;
			}
		}
		return null;
	}

	private boolean isContainerNumber(String input) {
		return VALID_CONTAINER_NO_REGEX.matcher(input).find();
	}

	@GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id, ModelMap map) {
		map.put("edoId", id);
		return PREFIX + "/history";
	}

	@GetMapping("/auditLog/{edoId}")
	@ResponseBody
	public TableDataInfo edoAuditLog(@PathVariable("edoId") Long edoId, EquipmentDoAuditLog edoAuditLog) {
		edoAuditLog.setDoId(edoId);
		List<EquipmentDoAuditLog> edoAuditLogsList = equipmentDoAuditLogService
				.selectEquipmentDoAuditLogList(edoAuditLog);
		return getDataTable(edoAuditLogsList);
	}

	// --------newStyle-----------

	@GetMapping("/index")
	public String EquipmentDo2() {
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/equipmentDo";
	}

	@PostMapping("/billNo")
	@ResponseBody
	public TableDataInfo billNo(@RequestBody PageAble<EquipmentDo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		EquipmentDo edo = param.getData();
		if (edo == null) {
			edo = new EquipmentDo();
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		edo.setCarrierCode(null);
		List<EquipmentDo> dataList = equipmentDoService.selectEdoListByBillNo(edo);
		return getDataTable(dataList);
	}

	@PostMapping("/equipmentDo")
	@ResponseBody
	public TableDataInfo edo(@RequestBody PageAble<EquipmentDo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		EquipmentDo edo = param.getData();
		if (edo == null) {
			edo = new EquipmentDo();
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setCarrierCode(null);
		edo.setParams(groupCodes);
		List<EquipmentDo> dataList = equipmentDoService.selectEquipmentDoList(edo);
		return getDataTable(dataList);
	}

	@GetMapping("/carrierCode")
	@ResponseBody
	public List<String> carrierCode() {
		return super.getGroupCodes();
	}

	@GetMapping("/getVoyNo")
	@ResponseBody
	public List<String> listVoyNo(String keyString, String vessel) {
		EquipmentDo edo = new EquipmentDo();
		edo.setVessel(vessel);
		edo.setVoyNo(keyString);
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		return equipmentDoService.selectVoyNos(edo);
	}

	@GetMapping("/getVessel")
	@ResponseBody
	public List<String> listVessel(String keyString) {
		EquipmentDo edo = new EquipmentDo();
		edo.setVessel(keyString);
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		return equipmentDoService.selectVessels(edo);
	}

	@GetMapping("/update")
	public String update() {
		return PREFIX + "/update";
	}

	@GetMapping("/update/{id}")
	public String getUpdate(@PathVariable("id") Long id, ModelMap map) {
		map.put("id", id);
		EquipmentDo edo = equipmentDoService.selectEquipmentDoById(id);
		map.put("edo", edo);
		return PREFIX + "/update";
	}
	@GetMapping("/multiUpdate/{ids}")
	public String multiUpdate(@PathVariable("ids") String ids, ModelMap map) {
		map.put("ids", ids);
		String[] idMap = ids.split(",");
		Long id = Long.parseLong(idMap[0]);
		EquipmentDo edo = equipmentDoService.selectEquipmentDoById(id);
		map.put("edo", edo);
		return PREFIX + "/multiUpdate";
	}

	@PostMapping("/update")
	@ResponseBody
	@Log(title = "Cập Nhật Equipment DO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	public AjaxResult multiUpdate(String ids, EquipmentDo edo) {
		if (ids == null) {
			ids = edo.getId().toString();
		}
		try {
			String[] idsList = ids.split(",");
			edo.setCarrierCode(super.getUserGroup().getGroupCode());
			edo.setCarrierId(super.getUser().getGroupId());
			for (String id : idsList) {
				EquipmentDo edoCheck = new EquipmentDo();
				edoCheck.setId(Long.parseLong(id));
				Map<String, Object> groupCodes = new HashMap<>();
				groupCodes.put("groupCode", super.getGroupCodes());
				edoCheck.setParams(groupCodes);
				if (equipmentDoService.selectFirstEdo(edoCheck) == null) {
					return AjaxResult.error(
							"Bạn đã chọn container mà bạn không <br> có quyền cập nhật, vui lòng kiếm tra lại dữ liệu");
				} else if (equipmentDoService.selectFirstEdo(edoCheck).getStatus().equals("3")) {
					return AjaxResult.error(
							"Bạn đã chọn container đã GATE-IN ra khỏi <br> cảng, vui lòng kiểm tra lại dữ liệu!");
				}
			}
			for (String id : idsList) {
				edo.setId(Long.parseLong(id));
				equipmentDoService.updateEquipmentDo2(edo);
				edo.setCreateBy(super.getUser().getEmail());
				equipmentDoAuditLogService.updateAuditLog(edo);
			}
			return AjaxResult.success("Update thành công");
		} catch (Exception e) {
			return AjaxResult.error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		}

	}
	@GetMapping("/getEmptyContainerDeport")
	@ResponseBody
	public AjaxResult listEmptyContainerDeport() {
		SysDictData dictData = new SysDictData();
		dictData.setDictType("edo_empty_container_deport");
		return AjaxResult.success(dictDataService.selectDictDataList(dictData));
	}



}