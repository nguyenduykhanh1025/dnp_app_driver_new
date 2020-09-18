package vn.com.irtech.eport.carrier.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysDictDataService;

@Controller
@RequestMapping("/edo")
@Transactional(rollbackFor = Exception.class)
public class CarrierEdoController extends CarrierBaseController {

	private static final Pattern VALID_CONTAINER_NO_REGEX = Pattern.compile("^[A-Za-z]{4}[0-9]{7}$",
	Pattern.CASE_INSENSITIVE);
	
	private final String PREFIX = "edo";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private ISysDictDataService dictDataService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@GetMapping("/index")
	public String EquipmentDo() {
		if (!hasEdoPermission()) {
			return "error/404";
		}
		return PREFIX + "/edo";
	}

	@PostMapping("/billNo")
	@ResponseBody
	public TableDataInfo billNo(@RequestBody PageAble<Edo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
		if (edo == null) {
			edo = new Edo();
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		edo.setCarrierCode(null);
		List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
		return getDataTable(dataList);
	}

	@PostMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(@RequestBody PageAble<Edo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
		if (edo == null) {
			edo = new Edo();
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setCarrierCode(null);
		edo.setParams(groupCodes);
		edo.setDelFlg(0);
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}

	@GetMapping("/carrierCode")
	@ResponseBody
	public List<String> carrierCode() {
		return super.getGroupCodes();
	}

	@GetMapping("/update")
	public String update() {
		return PREFIX + "/update";
	}

	@GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id, ModelMap map) {
		map.put("edoId", id);
		return PREFIX + "/history";
	}

	@GetMapping("/update/{id}")
	public String getUpdate(@PathVariable("id") Long id, ModelMap map) {
		map.put("id", id);
		Edo edo = edoService.selectEdoById(id);
		map.put("edo", edo);
		map.put("hasConsigneeUpdatePermission",hasConsigneeUpdatePermission());
		return PREFIX + "/update";
	}

	@GetMapping("/multiUpdate/{ids}")
	public String multiUpdate(@PathVariable("ids") String ids, ModelMap map) {
		map.put("ids", ids);
		String[] idMap = ids.split(",");
		Long id = Long.parseLong(idMap[0]);
		Edo edo = edoService.selectEdoById(id);
		map.put("edo", edo);
		map.put("hasConsigneeUpdatePermission",hasConsigneeUpdatePermission());
		return PREFIX + "/multiUpdate";
	}

	@PostMapping("/delContainer")
	@ResponseBody
	@Transactional
	@Log(title = "Xóa container", businessType = BusinessType.DELETE, operatorType = OperatorType.SHIPPINGLINE)
	public AjaxResult delContainer(String ids) {
		Edo edo = new Edo();
		if (ids == null) {
			ids = edo.getId().toString();
		}
		try {
			String[] idsList = ids.split(",");
			edo.setCarrierCode(super.getUserGroup().getGroupCode());
			edo.setCarrierId(super.getUser().getGroupId());
			for (String id : idsList) {
				Edo edoCheck = new Edo();
				edoCheck.setId(Long.parseLong(id));
				Map<String, Object> groupCodes = new HashMap<>();
				groupCodes.put("groupCode", super.getGroupCodes());
				edoCheck.setParams(groupCodes);
				if (edoService.selectFirstEdo(edoCheck) == null) {
					return AjaxResult.error(
							"Bạn đã chọn container mà bạn không <br> có quyền cập nhật, vui lòng kiếm tra lại dữ liệu");
				} else if (edoService.selectFirstEdo(edoCheck).getStatus().equals("3")) {
					return AjaxResult.error(
							"Bạn đã chọn container đã GATE-IN ra khỏi <br> cảng, vui lòng kiểm tra lại dữ liệu!");
				}
			}
			for (String id : idsList) {
				edoService.deleteEdoById(Long.parseLong(id));
			}
			return AjaxResult.success("Xóa eDO thành công");
		} catch (Exception e) {
			return AjaxResult.error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		}
	
	}

	@PostMapping("/update")
	@ResponseBody
	@Log(title = "Cập Nhật eDO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	public AjaxResult multiUpdate(String ids, Edo edo) {
		if (ids == null) {
			ids = edo.getId().toString();
		}
		try {
			String[] idsList = ids.split(",");
			edo.setCarrierCode(super.getUserGroup().getGroupCode());
			edo.setCarrierId(super.getUser().getGroupId());
			for (String id : idsList) {
				Edo edoCheck = new Edo();
				edoCheck.setId(Long.parseLong(id));
				Map<String, Object> groupCodes = new HashMap<>();
				groupCodes.put("groupCode", super.getGroupCodes());
				edoCheck.setParams(groupCodes);
				if(hasConsigneeUpdatePermission() == false && edo.getConsignee() != "")
				{
					return AjaxResult.error(
							"Bạn đã chọn container mà bạn không <br> có quyền cập nhật Consignee, vui lòng kiếm tra lại dữ liệu");
				}
				if (edoService.selectFirstEdo(edoCheck) == null) {
					return AjaxResult.error(
							"Bạn đã chọn container mà bạn không <br> có quyền cập nhật, vui lòng kiếm tra lại dữ liệu");
				} else if (edoService.selectFirstEdo(edoCheck).getStatus().equals("3")) {
					return AjaxResult.error(
							"Bạn đã chọn container đã GATE-IN ra khỏi <br> cảng, vui lòng kiểm tra lại dữ liệu!");
				}
			}
			for (String id : idsList) {
				edo.setId(Long.parseLong(id));
				edoService.updateEdo(edo);
				edo.setCreateBy(super.getUser().getEmail());
				edoAuditLogService.updateAuditLog(edo);
			}
			return AjaxResult.success("Update thành công");
		} catch (Exception e) {
			return AjaxResult.error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		}

	}

	@PostMapping("/readEdiOnly")
	@ResponseBody
	public Object readEdi(String fileContent) {
		List<Edo> edo = new ArrayList<>();
		String[] text = fileContent.split("'");
		edo = edoService.readEdi(text);
		return edo;
	}

	@GetMapping("/auditLog/{edoId}")
	@ResponseBody
	public TableDataInfo edoAuditLog(@PathVariable("edoId") Long edoId, EdoAuditLog edoAuditLog) {
		edoAuditLog.setEdoId(edoId);
		List<EdoAuditLog> edoAuditLogsList = edoAuditLogService.selectEdoAuditLogList(edoAuditLog);
		return getDataTable(edoAuditLogsList);
	}


	@GetMapping("/getVoyNo")
	@ResponseBody
	public List<String> listVoyNos(String keyString, String vessel) {
		Edo edo = new Edo();
		edo.setVessel(vessel);
		edo.setVoyNo(keyString);
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		return edoService.selectVoyNos(edo);
	}

	@GetMapping("/getVessel")
	@ResponseBody
	public List<String> listVessels(String keyString) {
		Edo edo = new Edo();
		edo.setVessel(keyString);
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		return edoService.selectVessels(edo);
	}

	@GetMapping("/getEmptyContainerDeport")
	@ResponseBody
	public AjaxResult listEmptyContainerDeport() {
		SysDictData dictData = new SysDictData();
		dictData.setDictType("edo_empty_container_deport");
		return AjaxResult.success(dictDataService.selectDictDataList(dictData));
	}

	@GetMapping("/report")
	public String report() {
		if (!hasEdoPermission()) {
			return "error/404";
		}
		return PREFIX + "/report";
	}

	@PostMapping("/edoReport")
	@ResponseBody
	public TableDataInfo edoReport(@RequestBody PageAble<Edo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edo = param.getData();
		if (edo == null) {
			edo = new Edo();
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		edo.setParams(groupCodes);
		List<Edo> dataList = edoService.selectEdoListForReport(edo);
		return getDataTable(dataList);
	}

	@GetMapping("/releaseEdo")
	public String releaseEdo() {
		if (!hasEdoPermission()) {
			return "error/404";
		}
		return PREFIX + "/releaseEdo";
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

		Map<String, List<String>> optionsList = new HashMap<>();
		// optionsList.put("consigneeList", equipmentDoService.getConsignee(getUserId()));
		// optionsList.put("emptyDepotList", equipmentDoService.getEmptyContainerDepot(getUserId()));
		// optionsList.put("vesselList", equipmentDoService.getVessel(getUserId()));
		return optionsList;
	}

	@Log(title = "Phát Hành eDO", businessType = BusinessType.INSERT, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/add")
	@ResponseBody
	@Transactional
	public AjaxResult addSave(@RequestBody List<Edo> edos) {
		// String message = userService.importUser(userList, updateSupport, operName);
		// return AjaxResult.success(message);
		if (!hasDoPermission()) {
			return error("Tài khoản này không có quyền phát hành eDO");
		}
		if (edos != null) {
			String consignee = edos.get(0).getConsignee();
			String containerNumber = "";
			for (Edo e : edos) {
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
				if (edoService.getBillOfLadingInfo(e.getBillOfLading()) != null) {
					// exist B/L
					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
							+ "'.<br/>Lỗi: Mã vận đơn (B/L No.) " + e.getBillOfLading() + " đã tồn tại.");
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
//				if (e.getDetFreeTime() != null) {
//					return AjaxResult.error("Có lỗi xảy ra ở container '" + e.getContainerNumber()
//							+ "'.<br/>Lỗi: Ngày miễn lưu không được lớn hơn 9999");
//				}
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
			for (Edo edo : edos) {
				edo.setStatus("1");
				edoService.insertEdo(edo);
				edo.setCreateBy(super.getUser().getEmail());
				edoAuditLogService.addAuditLogFirst(edo);
			}
			// return toAjax(equipmentDoService.insertEquipmentDoList(doList));


			return AjaxResult.success("Đã lưu thành công " + edos.size() + " DO lên Web Portal.");
		}
		return AjaxResult.error("Không có dữ liệu để tạo DO, hãy kiểm tra lại");
	}

	private boolean isContainerNumber(String input) {
		return VALID_CONTAINER_NO_REGEX.matcher(input).find();
	}


}