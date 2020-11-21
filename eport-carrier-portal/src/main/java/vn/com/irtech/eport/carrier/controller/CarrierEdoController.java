package vn.com.irtech.eport.carrier.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.domain.SysSyncQueue;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysDictDataService;
import vn.com.irtech.eport.system.service.ISysSyncQueueService;

@Controller
@RequestMapping("/edo")
@RequiresPermissions("carrier:edo")
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

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ISysSyncQueueService sysSyncQueueService;

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
		edo.getParams().put("groupCode", super.getGroupCodes());
		edo.setCarrierCode(null);
		List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
		return getDataTable(dataList);
	}

	@PostMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(@RequestBody PageAble<Edo> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Edo edoParam = param.getData();
		if (edoParam == null) {
			edoParam = new Edo();
		}
		if (edoParam.getBillOfLading() == null) {
			return null;
		}
		edoParam.setCarrierCode(null);
		edoParam.getParams().put("groupCode", super.getGroupCodes());
		edoParam.setDelFlg(0);
		edoParam.setContainerNumber(null);
		List<Edo> dataList = edoService.selectEdoList(edoParam);
		Map<String, ContainerInfoDto> cntrInfoMap = getContainerInfoMap(edoParam.getBillOfLading());
		for (Edo edo : dataList) {
			// Get container info from catos mapping by container no
			ContainerInfoDto cntrInfo = cntrInfoMap.get(edo.getContainerNumber());
			Map<String, Object> extraData = new HashMap<>();
			if (cntrInfo != null) {
				extraData.put("jobOrderNo", cntrInfo.getJobOdrNo2());
				extraData.put("gateOutDate", cntrInfo.getOutDate());
				extraData.put("status", cntrInfo.getCntrState());
			}
			edo.setParams(extraData);
		}
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
		// TODO set carier
		map.put("edo", edo);
		map.put("hasConsigneeUpdatePermission", hasConsigneeUpdatePermission());
		return PREFIX + "/update";
	}

	@GetMapping("/multiUpdate/{ids}")
	public String multiUpdate(@PathVariable("ids") String ids, ModelMap map) {
		map.put("ids", ids);
		String[] idMap = ids.split(",");
		Long id = Long.parseLong(idMap[0]);
		Edo edo = edoService.selectEdoById(id);
		map.put("edo", edo);
		map.put("hasConsigneeUpdatePermission", hasConsigneeUpdatePermission());
		return PREFIX + "/multiUpdate";
	}

	@PostMapping("/delContainer")
	@ResponseBody
	@Transactional
	@Log(title = "Xóa container", businessType = BusinessType.DELETE, operatorType = OperatorType.SHIPPINGLINE)
	public AjaxResult delContainer(String ids) {
		if (ids == null) {
			return error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		}
//		String[] idsList = ids.split(",");
//		edo.setCarrierCode(super.getUserGroup().getGroupCode());
//		edo.setCarrierId(super.getUser().getGroupId());
//		for (String id : idsList) {
//			Map<String, Object> groupCodes = new HashMap<>();
//			groupCodes.put("groupCode", super.getGroupCodes());
//			Edo edoCheck = new Edo();
//			edoCheck.setId(Long.parseLong(id));
//			edoCheck.setParams(groupCodes);
//			if (edoService.selectFirstEdo(edoCheck) == null) {
//				return AjaxResult.error("Container không tồn tại, vui lòng kiểm tra lại dữ liệu");
//			} else if (edoService.selectEdoById(Long.parseLong(id)).getStatus() != null) {
//				return AjaxResult.error("Bạn không thể xóa container này <br>Thông tin cont đã được khách hàng khai báo trên cảng điện tử!");
//			}
//		}

		// Get all edo need to delete
		Edo edoParam = new Edo();
		Map<String, Object> params = new HashMap<>();
		params.put("groupCode", super.getGroupCodes());
		params.put("ids", Convert.toStrArray(ids));
		edoParam.setParams(params);
		List<Edo> edos = edoService.selectEdoList(edoParam);

		// Check if edo is exist
		if (CollectionUtils.isEmpty(edos)) {
			return error("Container không tồn tại, vui lòng kiểm tra lại dữ liệu.");
		}

		// Get edo info from catos
		Map<String, ContainerInfoDto> cntrInfoMap = getContainerInfoMap(edoParam.getBillOfLading());
		String containerRegister = "";
		String containerOrder = "";
		for (Edo edo : edos) {
			if (StringUtils.isNotEmpty(edo.getStatus())) {
				containerRegister += edo.getContainerNumber() + ",";
			}
			ContainerInfoDto cntrInfo = cntrInfoMap.get(edo.getContainerNumber());
			if (cntrInfo != null) {
				// Check catos has job order no
				if (StringUtils.isNotEmpty(cntrInfo.getJobOdrNo2())) {
					containerOrder += edo.getContainerNumber() + ",";
				}
			}
		}

		if (containerRegister.length() > 0) {
			return error("Các container " + containerRegister.substring(0, containerRegister.length() - 1)
					+ " đã được khai báo trên cảng điện tử.<br>Không thể xóa những container này.");
		}

		if (containerOrder.length() > 0) {
			return error("Các container " + containerOrder.substring(0, containerOrder.length() - 1)
					+ " đã được làm lệnh.<br>Không thể xóa những container này.");
		}

		edoService.deleteEdoByIds(ids);
		return AjaxResult.success("Xóa eDO thành công");

	}

	@PostMapping("/update")
	@ResponseBody
	@Log(title = "Cập Nhật eDO", businessType = BusinessType.UPDATE, operatorType = OperatorType.SHIPPINGLINE)
	public AjaxResult multiUpdate(String ids, Edo edoInput) {
		if (ids == null) {
			return error("Có lỗi xảy ra, vui lòng kiểm tra lại dữ liệu");
		}
		// Validate permission
		if (StringUtils.isNotEmpty(edoInput.getConsignee()) && !hasConsigneeUpdatePermission()) {
			return AjaxResult.error("Không thể cập nhật Consignee, vui lòng kiếm tra lại dữ liệu.");
		}
		try {

			// Get all edo need to delete
			Edo edoParam = new Edo();
			Map<String, Object> params = new HashMap<>();
			params.put("groupCode", super.getGroupCodes());
			params.put("ids", Convert.toStrArray(ids));
			edoParam.setParams(params);
			List<Edo> edos = edoService.selectEdoList(edoParam);

			// Check if edo is exist
			if (CollectionUtils.isEmpty(edos)) {
				return error("Container không tồn tại, vui lòng kiểm tra lại dữ liệu.");
			}

			// Get container no string with container no separated by comma
			// Use to query container info from catos
			String containerNos = "";
			for (Edo edo : edos) {
				containerNos += edo.getContainerNumber() + ",";
			}
			// Remove last comma
			containerNos = containerNos.substring(0, containerNos.length() - 1);

			// Get edo info from catos
			Map<String, ContainerInfoDto> cntrInfoMap = getContainerInfoMapFE(containerNos);
			for (Edo edo : edos) {
				// Container full in catos
				ContainerInfoDto cntrFull = cntrInfoMap.get(edo.getContainerNumber() + "F");
				// Container empty in catos (if exists mean container has already been gate out)
				ContainerInfoDto cntrEmty = cntrInfoMap.get(edo.getContainerNumber() + "E");
				// Check if expired dem has update
				if (edoInput.getExpiredDem() != null && edoInput.getExpiredDem().compareTo(edo.getExpiredDem()) != 0) {
					// has update
					if (cntrFull != null && StringUtils.isNotEmpty(cntrFull.getJobOdrNo2())) {
						// Get old request if exist, update else insert new request
						SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
						sysSyncQueueParam.setBlNo(edo.getBillOfLading());
						sysSyncQueueParam.setCntrNo(edo.getContainerNumber());
						sysSyncQueueParam.setJobOdrNo(cntrFull.getJobOdrNo2());
						sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DEM);
						sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
						List<SysSyncQueue> sysSyncQueues = sysSyncQueueService
								.selectSysSyncQueueList(sysSyncQueueParam);
						if (CollectionUtils.isNotEmpty(sysSyncQueues)) {
							// Case update request in queue
							SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
							sysSyncQueueUpdate.setId(sysSyncQueues.get(0).getId());
							sysSyncQueueUpdate.setExpiredDem(edoInput.getExpiredDem());
							sysSyncQueueService.updateSysSyncQueue(sysSyncQueueUpdate);
						} else {
							// Case insert new request in queue
							SysSyncQueue sysSyncQueue = new SysSyncQueue();
							sysSyncQueue.setSyncType(EportConstants.SYNC_QUEUE_DEM);
							sysSyncQueue.setExpiredDem(edoInput.getExpiredDem());
							sysSyncQueue.setBlNo(edo.getBillOfLading());
							sysSyncQueue.setCntrNo(edo.getContainerNumber());
							sysSyncQueue.setJobOdrNo(cntrFull.getJobOdrNo2());
							sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
							sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
						}
					}
				}

				// Check if det free time has update
				if (StringUtils.isNotEmpty(edoInput.getDetFreeTime())
						&& edoInput.getDetFreeTime().equals(edo.getDetFreeTime())) {
					// has update
					// Check condition drop empty container order has been made
					if (cntrEmty != null && StringUtils.isNotEmpty(cntrEmty.getJobOdrNo())) {
						// Get old request if exist, update else insert new request
						SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
						sysSyncQueueParam.setBlNo(edo.getBillOfLading());
						sysSyncQueueParam.setCntrNo(edo.getContainerNumber());
						sysSyncQueueParam.setJobOdrNo(cntrEmty.getJobOdrNo());
						sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DET);
						sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
						List<SysSyncQueue> sysSyncQueues = sysSyncQueueService
								.selectSysSyncQueueList(sysSyncQueueParam);
						if (CollectionUtils.isNotEmpty(sysSyncQueues)) {
							// Case update request in queue
							SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
							sysSyncQueueUpdate.setId(sysSyncQueues.get(0).getId());
							sysSyncQueueUpdate.setDetFreeTime(edoInput.getDetFreeTime());
							sysSyncQueueParam.setRemark(getRemarkAfterUpdateDet(edoInput.getDetFreeTime(), cntrEmty.getRemark()));
							sysSyncQueueService.updateSysSyncQueue(sysSyncQueueUpdate);
						} else {
							// Case insert new request in queue
							SysSyncQueue sysSyncQueue = new SysSyncQueue();
							sysSyncQueue.setSyncType(EportConstants.SYNC_QUEUE_DET);
							sysSyncQueue.setDetFreeTime(edoInput.getDetFreeTime());
							sysSyncQueue.setRemark(
									getRemarkAfterUpdateDet(edoInput.getDetFreeTime(), cntrEmty.getRemark()));
							sysSyncQueue.setBlNo(edo.getBillOfLading());
							sysSyncQueue.setCntrNo(edo.getContainerNumber());
							sysSyncQueue.setJobOdrNo(cntrEmty.getJobOdrNo());
							sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
							sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
						}
					}
				}

				// Check if empty container depot has update
				if (StringUtils.isNotEmpty(edoInput.getEmptyContainerDepot())
						&& edoInput.getEmptyContainerDepot().equalsIgnoreCase(edo.getEmptyContainerDepot())) {
					// has update
					// Check condition drop empty container order has been made
					if (cntrEmty != null && StringUtils.isNotEmpty(cntrEmty.getJobOdrNo())) {
						return error("Container đã có lệnh trả rỗng tại cảng.<br>Không thể cập nhật nơi trả vỏ.");
					}
				}
			}

			// Update
			String[] idsList = ids.split(",");
			// Set input field permit update
			Edo edoUpdate = new Edo();
			edoUpdate.setExpiredDem(edoInput.getExpiredDem());
			edoUpdate.setDetFreeTime(edoInput.getDetFreeTime());
			edoUpdate.setEmptyContainerDepot(edoInput.getEmptyContainerDepot());
			edoUpdate.setRemark(edoInput.getRemark());
			edoUpdate.setConsignee(edoInput.getConsignee());
			edoUpdate.setCarrierCode(super.getUserGroup().getGroupCode());
			edoUpdate.setCarrierId(super.getUser().getGroupId());
			for (String id : idsList) {
				edoUpdate.setId(Long.parseLong(id));
				edoService.updateEdo(edoUpdate);
				edoUpdate.setCreateBy(super.getUser().getEmail());
				edoAuditLogService.updateAuditLog(edoUpdate);
			}
			return AjaxResult.success("Update thành công");
		} catch (Exception e) {
			logger.error("Error when update edo from web carrier portal: " + e);
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
		// filter theo groupCode cua user (khong duoc xem cont cua group khac)
		edoAuditLog.getParams().put("groupCode", super.getGroupCodes());
		List<EdoAuditLog> edoAuditLogsList = edoAuditLogService.selectEdoAuditLogList(edoAuditLog);
		return getDataTable(edoAuditLogsList);
	}

	@PostMapping("/getVoyNo/{vessel}")
	@ResponseBody
	public List<Edo> listVoyNos(@PathVariable("vessel") String vessel) {
		Edo edo = new Edo();
		edo.setVessel(vessel);
		edo.getParams().put("groupCode", super.getGroupCodes());
		return edoService.selectVoyNos(edo);
	}

	@PostMapping("/getVessel")
	@ResponseBody
	public List<Edo> listVessels() {
		Edo edo = new Edo();
		edo.getParams().put("groupCode", super.getGroupCodes());
		List<Edo> edos = edoService.selectVessels(edo);
		return edos;
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
		edo.getParams().put("groupCode", super.getGroupCodes());
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
	public AjaxResult getListOptionsGrid() {
		AjaxResult ajaxResult = success();
		ajaxResult.put("sizeList", dictDataService.selectDictDataByType("sys_size_container_eport"));
		ajaxResult.put("consigneeList", catosApiService.getConsigneeList());
		return ajaxResult;
	}

	@Log(title = "Phát Hành eDO", businessType = BusinessType.INSERT, operatorType = OperatorType.SHIPPINGLINE)
	@PostMapping("/add")
	@ResponseBody
	@Transactional
	public AjaxResult addSave(@RequestBody List<Edo> edos) {
		// String message = userService.importUser(userList, updateSupport, operName);
		// return AjaxResult.success(message);
		if (!hasEdoPermission()) {
			return error("Tài khoản này không có quyền phát hành eDO");
		}
		Map<String, Object> groupCodes = new HashMap<>();
		groupCodes.put("groupCode", super.getGroupCodes());
		if (edos != null) {
			String consignee = edos.get(0).getConsignee();
			String billOfLading = edos.get(0).getBillOfLading();
			String vessel = edos.get(0).getVessel();
			String voyNo = edos.get(0).getVoyNo();
			Edo edoCheckBilll = new Edo();
			edoCheckBilll.setBillOfLading(edos.get(0).getBillOfLading());
			edoCheckBilll.setParams(groupCodes);
			if (edoService.getBillOfLadingInfo(edoCheckBilll) != null) {
				// exist B/L
				return AjaxResult.error("Có lỗi xảy ra ở container '" + edos.get(0).getBillOfLading()
						+ "'.<br/>Lỗi: Mã vận đơn (B/L No.) " + edos.get(0).getBillOfLading() + " đã tồn tại.");
			}
			String containerNumber = "";
			for (Edo e : edos) {
				e.setCarrierId(super.getUser().getGroupId());
				e.setCreateBy(getUser().getFullName());
				e.setUpdateTime(new Date());
				e.setUpdateBy(getUser().getFullName());
				if (StringUtils.isBlank(e.getCarrierCode()) || StringUtils.isBlank(e.getBillOfLading())
						|| StringUtils.isBlank(e.getContainerNumber()) || StringUtils.isBlank(e.getConsignee())
						|| StringUtils.isBlank(e.getEmptyContainerDepot()) || StringUtils.isBlank(e.getVessel())
						|| StringUtils.isBlank(e.getVoyNo())) {
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
				// // DEM Free date la so
				// if (e.getDetFreeTime() != null && e.getDetFreeTime() >= 10000) {
				// return AjaxResult.error("Có lỗi xảy ra ở container '" +
				// e.getContainerNumber()
				// + "'.<br/>Lỗi: Ngày miễn lưu không được lớn hơn 9999");
				// }

				// Bill is the same
				if (!e.getBillOfLading().equals(billOfLading)) {
					return AjaxResult.error(" Số bill không được khác nhau");
				}
				billOfLading = e.getBillOfLading();
				// Consignee is the same
				if (!e.getConsignee().equals(consignee)) {
					return AjaxResult.error("Tên khách hàng không được khác nhau");
				}
				consignee = e.getConsignee();

				// Vessel is unique
				if (!e.getVessel().equals(vessel)) {
					return AjaxResult.error("Tên tàu không được khác nhau");
				}
				vessel = e.getVessel();

				// voyNo is unique
				if (!e.getVoyNo().equals(voyNo)) {
					return AjaxResult.error("Tên chuyến không được khác nhau");
				}
				voyNo = e.getVoyNo();

				// Container number is unique
				if (e.getContainerNumber().equals(containerNumber)) {
					return AjaxResult.error("Số container " + containerNumber + " bị trùng");
				}
				containerNumber = e.getContainerNumber();

			}
			// Do the insert to DB
			for (Edo edo : edos) {
				edo.setStatus("1");
				edo.setCarrierCode(super.getUserGroup().getGroupCode());
				edo.setCreateBy(super.getUser().getEmail());
				edoService.insertEdo(edo);
				edoAuditLogService.addAuditLogFirst(edo);
			}
			// return toAjax(equipmentDoService.insertEquipmentDoList(doList));
			return AjaxResult.success("Đã lưu thành công " + edos.size() + " eDO lên Web Portal.");
		}
		return AjaxResult.error("Không có dữ liệu để tạo eDO, hãy kiểm tra lại");
	}

	private boolean isContainerNumber(String input) {
		return VALID_CONTAINER_NO_REGEX.matcher(input).find();
	}

	/**
	 * Get list container by bill and convert to map container no - container info
	 * obj
	 * 
	 * @param blNo
	 * @return Map<String, ContainerInfoDto>
	 */
	private Map<String, ContainerInfoDto> getContainerInfoMap(String blNo) {
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoListByBlNo(blNo);
		// List<ContainerInfoDto> cntrInfos =
		// catosApiService.getContainerInfoListByBlNo(blNo);
		// Map oject store container info data by key container no
		Map<String, ContainerInfoDto> cntrInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			for (ContainerInfoDto cntrInfo : cntrInfos) {
				cntrInfoMap.put(cntrInfo.getCntrNo(), cntrInfo);
			}
		}
		return cntrInfoMap;
	}

	/**
	 * Get list container by containerNos (separated by comma) and convert to map
	 * container no - container info obj differentiate by fe
	 * 
	 * @param containerNos
	 * @return Map<String, ContainerInfoDto>
	 */
	private Map<String, ContainerInfoDto> getContainerInfoMapFE(String containerNos) {
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoDtoByContNos(containerNos);
		Map<String, ContainerInfoDto> cntrInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			for (ContainerInfoDto cntrInfo : cntrInfos) {
				if ("E".equalsIgnoreCase(cntrInfo.getFe())
						&& !EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntrInfo.getCntrState())
						&& !EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(cntrInfo.getCntrState())) {
					cntrInfoMap.put(cntrInfo.getCntrNo() + cntrInfo.getFe(), cntrInfo);
				} else if ("F".equalsIgnoreCase(cntrInfo.getFe())
						&& !EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntrInfo.getCntrState())) {
					cntrInfoMap.put(cntrInfo.getCntrNo() + cntrInfo.getFe(), cntrInfo);
				}
			}
		}
		return cntrInfoMap;
	}

	/**
	 * Replace det free time remark in catos if has remark or append new remark
	 * 
	 * @param detFreeTime
	 * @param currentRemark
	 * @return
	 */
	private String getRemarkAfterUpdateDet(String detFreeTime, String currentRemark) {
		boolean isAppend = true;
		if (StringUtils.isNotEmpty(currentRemark)) {
			String[] arrStr = currentRemark.split(" ");
			for (int i = 0; i < arrStr.length; i++) {
				// format remark free xxx days
				// current word is free => next will be det free time
				// change next word
				if (arrStr[i].equalsIgnoreCase("free")) {
					arrStr[i + 1] = detFreeTime;
					isAppend = false;
					currentRemark = String.join(" ", arrStr);
					break;
				}
			}
		} else {
			currentRemark = "";
		}
		if (isAppend) {
			currentRemark += StringUtils.format(", free {} days", detFreeTime);
		}
		return currentRemark;
	}
}