package vn.com.irtech.eport.web.controller.om;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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

import com.google.gson.Gson;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/controlling")
public class GeneralControllingController extends AdminBaseController {

	private String PREFIX = "om/controlling";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private DictService dictDataService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IPickupAssignService pickupAssignService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private IProcessBillService processBillService;

	@Autowired
	private IEdoService edoService;

	@GetMapping()
	public String getViewDocument(@RequestParam(required = false) Long sId, ModelMap mmap) {

		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());
		// Get list logistic group
		LogisticGroup logisticGroup = new LogisticGroup();
		logisticGroup.setGroupName("Chọn đơn vị Logistics");
		logisticGroup.setId(0L);
		LogisticGroup logisticGroupParam = new LogisticGroup();
		logisticGroupParam.setDelFlag("0");
		List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
		logisticGroups.add(0, logisticGroup);
		mmap.put("logisticGroups", logisticGroups);

		// Get list vslNm : vslNmae : voyNo
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlanWithoutOpe();
		if (berthplanList == null) {
			berthplanList = new ArrayList<>();
		}
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setVslAndVoy("Chọn tàu chuyến");
		berthplanList.add(0, shipmentDetail);
		mmap.put("vesselAndVoyages", berthplanList);
		return PREFIX + "/index";
	}

	@GetMapping("/container/history/{callSeq}/{vslCd}/{cntrNo}")
	public String getHistoryCatosForm(ModelMap mmap, @PathVariable("callSeq") String callSeq,
			@PathVariable("vslCd") String vslCd, @PathVariable("cntrNo") String cntrNo) {
		mmap.put("callSeq", callSeq);
		mmap.put("vslCd", vslCd);
		mmap.put("cntrNo", cntrNo);
		return PREFIX + "/catosHistory";
	}
	
	@GetMapping("/container/history/{shipmentDetailId}")
	public String getHistoryEportForm(ModelMap mmap, @PathVariable("shipmentDetailId") Long shipmentDetailId) {
		mmap.put("shipmentDetailId", shipmentDetailId);
		return PREFIX + "/eportHistory";
	}

	@PostMapping("/shipments")
	@ResponseBody
	public AjaxResult getShipments(@RequestBody PageAble<Shipment> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = param.getData();
		if (shipment == null) {
			shipment = new Shipment();
		}
		List<Shipment> shipments = shipmentService.selectShipmentListByWithShipmentDetailFilter(shipment);
		ajaxResult.put("shipments", getDataTable(shipments));
		return ajaxResult;
	}

	@GetMapping("/shipment/{shipmentId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	@PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		SysUser user = getUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@GetMapping("/data-source")
	@ResponseBody
	public AjaxResult getDataSource() {
		AjaxResult ajaxResult = success();
		// Consignee list for receive cont full case
		List<String> listConsigneeWithTaxCode = (List<String>) CacheUtils.get("listConsigneeWithTaxCode");
		if (listConsigneeWithTaxCode == null) {
			listConsigneeWithTaxCode = shipmentDetailService.getConsigneeList();
			CacheUtils.put("listConsigneeWithTaxCode", listConsigneeWithTaxCode);
		}
		ajaxResult.put("listConsigneeWithTaxCode", listConsigneeWithTaxCode);

		// Consignee list for other case
		List<String> listConsignee = (List<String>) CacheUtils.get("consigneeList");
		if (listConsignee == null) {
			listConsignee = shipmentDetailService.getConsigneeListWithoutTaxCode();
			CacheUtils.put("consigneeList", listConsignee);
		}
		ajaxResult.put("consigneeList", listConsignee);

		// Vessel, voyage
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlanWithoutOpe();
		if (berthplanList != null && berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for (ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			ajaxResult.put("berthplanList", berthplanList);
			ajaxResult.put("vesselAndVoyages", vesselAndVoyages);
		}

		// sztp list
		ajaxResult.put("sizeList", dictDataService.getType("sys_size_container_eport"));

		// empty depot location list
		String dnPortName = configService.selectConfigByKey("danang.port.name");
		List<String> emptyDepotList = new ArrayList<>();
		emptyDepotList.add(dnPortName);
		emptyDepotList.add("Cảng khác");
		ajaxResult.put("emptyDepotList", emptyDepotList);

		// Opr
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprList");
		if (oprCodeList == null) {
			oprCodeList = catosApiService.getOprCodeList();
			CacheUtils.put("oprList", oprCodeList);
		}
		ajaxResult.put("oprList", oprCodeList);

		return ajaxResult;
	}

	@PostMapping("/vessel/pods")
	@ResponseBody
	public AjaxResult getPODs(@RequestBody ShipmentDetail shipmentDetail) {
		AjaxResult ajaxResult = success();
		List<String> listPOD = new ArrayList<String>();
		if (shipmentDetail != null) {
			listPOD = catosApiService.getPODList(shipmentDetail);
			ajaxResult.put("dischargePorts", listPOD);
		}
		return ajaxResult;
	}

	@Log(title = "Chỉnh sửa Cont", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
	@PostMapping("/shipment-detail")
	@ResponseBody
	@Transactional
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
				return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
			}
		}
		return success();
	}

	@Log(title = "Xóa container", businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE)
	@PostMapping("/shipment-detail/cancel")
	@ResponseBody
	public AjaxResult deleteShipmentDetail(String shipmentDetailIds, Long shipmentId) {

		logger.debug("Delete all shipment detail om want to delete");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		Long logisticGroupId = shipmentDetails != null && shipmentDetails.size() > 0
				? shipmentDetails.get(0).getLogisticGroupId()
				: null;
		shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds, logisticGroupId);

		// check to delete process order when empty
		logger.debug("Check and delete all process order mapping with shipment detail has been deleted");
		List<Long> processOrderId = new ArrayList<>();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (!processOrderId.contains(shipmentDetail.getProcessOrderId())) {
				processOrderId.add(shipmentDetail.getProcessOrderId());
				ShipmentDetail shipmentDetailParam = new ShipmentDetail();
				shipmentDetailParam.setProcessOrderId(shipmentDetail.getProcessOrderId());
				if (shipmentDetailService.countShipmentDetailList(shipmentDetailParam) == 0) {
					processOrderService.deleteProcessOrderById(shipmentDetail.getProcessOrderId());
				}
			}
		}

		// Delete all pick up assign
		logger.debug("Check shipment is empty");
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);

		// Check if shipment has shipment detail after delete
		// if it is then delete both pickup by shipment and shipment detail else only
		// shipment detail
		if (shipmentDetailService.countShipmentDetailList(shipmentDetailParam) == 0) {
			// delete all pickup assign and history include pickup by shipment
			PickupAssign pickupAssignParam = new PickupAssign();
			pickupAssignParam.setShipmentId(shipmentId);
			logger.debug("delete pickup assign list by shipment id");
			pickupAssignService.deletePickupAssignByCondition(pickupAssignParam);

			PickupHistory pickupHistoryParam = new PickupHistory();
			pickupHistoryParam.setShipmentId(shipmentId);
			logger.debug("delete pickup history list by shipment id");
			pickupHistoryService.deletePickupHistoryByCondition(pickupHistoryParam);

			// Set status shipment to init
			Shipment shipment = new Shipment();
			shipment.setId(shipmentId);
			shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
			shipmentService.updateShipment(shipment);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("shipmentDetailIds", Convert.toStrArray(shipmentDetailIds));

			// delete all pickup assign and history by shipment detail id
			PickupAssign pickupAssignParam = new PickupAssign();
			pickupAssignParam.setShipmentId(shipmentId);
			pickupAssignParam.setParams(map);
			logger.debug("delete pickup assign list by shipment ids");
			pickupAssignService.deletePickupAssignByCondition(pickupAssignParam);

			PickupHistory pickupHistoryParam = new PickupHistory();
			pickupHistoryParam.setShipmentId(shipmentId);
			pickupHistoryParam.setParams(map);
			logger.debug("delete pickup history list by shipment id");
			pickupHistoryService.deletePickupHistoryByCondition(pickupHistoryParam);
		}

		// TODO: Send notification

		return success();
	}

	@GetMapping("/shipments/{shipmentId}/shipment-images")
	@ResponseBody
	public AjaxResult getShipmentImages(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentId(shipmentId);
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		ajaxResult.put("shipmentFiles", shipmentImages);
		return ajaxResult;
	}

	@GetMapping("/verify-executed-command-success")
	public String verifyExecutedCommandSuccess() {
		return PREFIX + "/verifyExecutedCommandSuccess";
	}

	@GetMapping("/reset-process-status")
	public String resetProcessStatus() {
		return PREFIX + "/verifyResetProcessStatus";
	}

	@Log(title = "Xác nhận làm lệnh OK(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
	@PostMapping("/sync-catos")
	@ResponseBody
	public AjaxResult executedTheCommandCatosSuccess(String processOrderIds, String content) {
		String[] processOrderIdArr = processOrderIds.split(",");
		for (int i = 0; i < processOrderIdArr.length; i++) {
			Long processOrderId = Long.parseLong(processOrderIdArr[i]);
			ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
			if (processOrder == null) {
				// Co loi bat thuong xay ra. order khong ton tai
				throw new IllegalArgumentException("Process order not exist");
			}
			// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(processOrderId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			// get orderNo from catos
			String orderNo = null, invoiceNo = null;
			if(CollectionUtils.isNotEmpty(shipmentDetails)) {
				List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoDtoByContNos(shipmentDetails.get(0).getContainerNo());
				if (CollectionUtils.isNotEmpty(cntrInfos)) {
					switch (processOrder.getServiceType()) {
						case EportConstants.SERVICE_PICKUP_FULL:
							for (ContainerInfoDto cntrInfo : cntrInfos) {
								if ("F".equals(cntrInfo.getFe())) {
									orderNo = cntrInfo.getJobOdrNo2();
								}
							}
							break;
						case EportConstants.SERVICE_PICKUP_EMPTY:
							for (ContainerInfoDto cntrInfo : cntrInfos) {
								if ("E".equals(cntrInfo.getFe())) {
									orderNo = cntrInfo.getJobOdrNo2();
								}
							}		
							break;
						case EportConstants.SERVICE_DROP_FULL:
							for (ContainerInfoDto cntrInfo : cntrInfos) {
								if ("F".equals(cntrInfo.getFe())) {
									orderNo = cntrInfo.getJobOdrNo();
								}
							}
							break;
						case EportConstants.SERVICE_DROP_EMPTY:
							for (ContainerInfoDto cntrInfo : cntrInfos) {
								if ("E".equals(cntrInfo.getFe())) {
									orderNo = cntrInfo.getJobOdrNo();
								}
							}
							break;
						default:
							break;
					}
				}
	    	}
			if (orderNo == null || orderNo.equals("")) {
				return error();
			}
			// get Invoice
			if (processOrder.getPayType().equals("Cash") && orderNo != null) {
				invoiceNo = catosApiService.getInvoiceNoByOrderNo(orderNo);
			}
			// update processOrder
			processOrder.setOrderNo(orderNo);
			processOrder.setInvoiceNo(invoiceNo);
			processOrder.setStatus(2); // FINISH
			processOrder.setResult("S"); // RESULT SUCESS
			processOrderService.updateProcessOrder(processOrder);
			// SAVE BILL TO PROCESS BILL BY INVOICE NO
			if (invoiceNo != null && !invoiceNo.equals("")) {
				processBillService.saveProcessBillByInvoiceNo(processOrder);
			} else if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
				processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
			} else if (processOrder.getProcessData() != null) {
				ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(),
						ProcessJsonData.class);
				processBillService.saveShiftingBillWithCredit(processJsonData.getShipmentDetailIds(), processOrder);
				for (Long shipmentDetailId : processJsonData.getPrePickupContIds()) {
					ShipmentDetail prePickupShipmentDetail = new ShipmentDetail();
					prePickupShipmentDetail.setId(shipmentDetailId);
					prePickupShipmentDetail.setPrePickupPaymentStatus("Y");
					shipmentDetailService.updateShipmentDetail(prePickupShipmentDetail);
				}
			}
			// UPDATE STATUS OF SHIPMENT DETAIL AFTER MAKE ORDER SUCCESS
			if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
				shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", invoiceNo, processOrder);
				Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
				if (processOrder.getServiceType() == EportConstants.SERVICE_PICKUP_FULL
						&& "1".equals(shipment.getEdoFlg())) {
					for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
						Edo edo = new Edo();
						edo.setBillOfLading(shipment.getBlNo());
						edo.setContainerNumber(shipmentDetail2.getContainerNo());
						edo.setStatus("2"); // status process order has been made for this edo
						edoService.updateEdoByBlCont(edo);
					}
				}
			}
			// notify msg to Logistic
			if (content != null && content != "") {
				ShipmentComment shipmentComment = new ShipmentComment();
				Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
				shipmentComment.setShipmentId(shipment.getId());
				shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
				shipmentComment.setUserId(getUserId());
				shipmentComment.setUserType("S");// S: DNP Staff
				shipmentComment.setUserName(getUser().getUserName());
				shipmentComment.setUserAlias(getUser().getUserName());// TODO get tạm username
				shipmentComment.setCommentTime(new Date());
				shipmentComment.setContent(content);
				shipmentComment.setCreateTime(new Date());
				shipmentComment.setCreateBy(getUser().getUserName());
				switch (shipment.getServiceType()) {
				case EportConstants.SERVICE_PICKUP_FULL:
					shipmentComment.setTopic(Constants.RECEIVE_CONT_FULL_SUPPORT);
					break;
				case EportConstants.SERVICE_PICKUP_EMPTY:
					shipmentComment.setTopic(Constants.RECEIVE_CONT_EMPTY_SUPPORT);
					break;
				case EportConstants.SERVICE_DROP_FULL:
					shipmentComment.setTopic(Constants.SEND_CONT_FULL_SUPPORT);
					break;
				case EportConstants.SERVICE_DROP_EMPTY:
					shipmentComment.setTopic(Constants.SEND_CONT_EMPTY_SUPPORT);
					break;
				default:
					break;
				}
				shipmentComment.setServiceType(shipment.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}
		}
		return success();
	}

	@Log(title = "Reset Proccess Status(OM)", businessType = BusinessType.UPDATE, operatorType = OperatorType.MANAGE)
	@PostMapping("/order/reset")
	@Transactional
	@ResponseBody
	public AjaxResult resetProcessStatus(String shipmentDetailIds, Long shipmentId, String content) {
		// GET LIST SHIPMENT DETAIL BY shipmentDetailIds (id seperated by comma)
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		// update shipment detail 2 truong processOrderId, registerNo processStatus,
		// status
		String processOrderIds = "";
		Long currentProcessId = 0L;
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		try {
			if (shipmentDetails.size() > 0) {
				for (ShipmentDetail i : shipmentDetails) {
					if (!currentProcessId.equals(i.getProcessOrderId())) {
						currentProcessId = i.getProcessOrderId();
						processOrderIds += currentProcessId + ",";
					}
					i.setProcessOrderId(null);
					i.setRegisterNo(null);
					i.setProcessStatus("N");
					if (shipment.getServiceType() == EportConstants.SERVICE_PICKUP_FULL
							|| shipment.getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY) {
						i.setStatus(2);
					} else {
						i.setStatus(1);
					}
					i.setPaymentStatus("N");
					i.setUserVerifyStatus("N");
					i.setUpdateBy(getUser().getLoginName());
					shipmentDetailService.resetShipmentDetailProcessStatus(i);
				}
			}
			// delete record table process_order
			if (processOrderIds.length() > 0) {
				processOrderIds = processOrderIds.substring(0, processOrderIds.length() - 1);
				processOrderService.deleteProcessOrderByIds(processOrderIds);
			}

			// notify msg to Logistic
			if (content != null && content != "") {
				SysUser user = getUser();
				ShipmentComment shipmentComment = new ShipmentComment();
				shipmentComment.setShipmentId(shipment.getId());
				shipmentComment.setLogisticGroupId(shipment.getLogisticGroupId());
				shipmentComment.setUserId(getUserId());
				shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);// S: DNP Staff
				shipmentComment.setUserName(user.getUserName());
				shipmentComment.setUserAlias(user.getDept().getDeptName());
				shipmentComment.setCommentTime(new Date());
				shipmentComment.setContent(content);
				shipmentComment.setCreateTime(new Date());
				shipmentComment.setCreateBy(getUser().getUserName());
				switch (shipment.getServiceType()) {
				case EportConstants.SERVICE_PICKUP_FULL:
					shipmentComment.setTopic(Constants.RECEIVE_CONT_FULL_SUPPORT);
					break;
				case EportConstants.SERVICE_PICKUP_EMPTY:
					shipmentComment.setTopic(Constants.RECEIVE_CONT_EMPTY_SUPPORT);
					break;
				case EportConstants.SERVICE_DROP_FULL:
					shipmentComment.setTopic(Constants.SEND_CONT_FULL_SUPPORT);
					break;
				case EportConstants.SERVICE_DROP_EMPTY:
					shipmentComment.setTopic(Constants.SEND_CONT_EMPTY_SUPPORT);
					break;
				default:
					break;
				}
				shipmentComment.setServiceType(shipment.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}
			return success();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.getStackTrace();
			return error();
		}
	}

	/**
	 * Print Packing List
	 */
	@GetMapping("/shipment/{id}/packing-list")
	public String packingList(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.put("shipmentId", id);
		return PREFIX + "/packingList";
	}

	@GetMapping("create-packing-list/{shipmentId}")
	public void packingListReport(@PathVariable("shipmentId") Long shipmentId, HttpServletResponse response) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.isEmpty()) {
			logger.error("Error when print packing list: " + shipmentId);
			return;
		}
		try {
			response.setContentType("application/pdf");
			createPackingListReport(shipmentDetails, response.getOutputStream());
		} catch (final Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
	}

	private void createPackingListReport(final List<ShipmentDetail> shipmentDetailList, OutputStream out)
			throws JRException {
		// Fetching the report file from the resources folder.
		final JasperReport report = (JasperReport) JRLoader
				.loadObject(this.getClass().getResourceAsStream("/report/packinglist.jasper"));

		// Fetching the shipmentDetails from the data source.
		// final JRBeanCollectionDataSource params = new
		// JRBeanCollectionDataSource(shipmentDetails);

		// Adding the additional parameters to the pdf.
		ShipmentDetail shipmentDetail = shipmentDetailList.get(0);
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("bookingNo", shipmentDetail.getBookingNo());
		parameters.put("consignee", shipmentDetail.getConsignee());
		parameters.put("etd", shipmentDetail.getEtd());
		parameters.put("feederName", shipmentDetail.getVslName() + " - " + shipmentDetail.getVoyCarrier());
		parameters.put("voy", shipmentDetail.getVoyCarrier());
		parameters.put("pol", "VNDAD");
		parameters.put("pod", shipmentDetail.getDischargePort());
		parameters.put("logisticGroup",
				logisticGroupService.selectLogisticGroupById(shipmentDetail.getLogisticGroupId()).getGroupName());

		parameters.put("table", shipmentDetailList);
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
//		final JasperPrint print = JasperFillManager.fillReport(report, parameters, params);
		// Export DPF to output stream
		JasperExportManager.exportReportToPdfStream(print, out);
	}
}
