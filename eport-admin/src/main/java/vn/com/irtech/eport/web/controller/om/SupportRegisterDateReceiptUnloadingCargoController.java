package vn.com.irtech.eport.web.controller.om;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ICfsHouseBillService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.domain.SysUser;

@Controller
@RequestMapping("/om/register-date-receipt-unloading-cargo")
public class SupportRegisterDateReceiptUnloadingCargoController extends OmBaseController {
	protected final Logger logger = LoggerFactory.getLogger(SupportRegisterDateReceiptLoadingCargoController.class);

	private final String PREFIX = "om/registerDateReceiptUnloadingCargo";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private DictService dictService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private ICfsHouseBillService cfsHouseBillService;

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

		List<SysDictData> dictDatas = dictService.getType("opr_list_booking_check");
		if (CollectionUtils.isEmpty(dictDatas)) {
			dictDatas = new ArrayList<>();
		}
		SysDictData sysDictData = new SysDictData();
		sysDictData.setDictLabel("Chọn OPR");
		sysDictData.setDictValue("Chọn OPR");
		dictDatas.add(0, sysDictData);
		mmap.put("oprList", dictDatas);
		return PREFIX + "/index";
	}

	@GetMapping("/confirmation")
	public String getConfirmationForm() {
		return PREFIX + "/confirmation";
	}

	@GetMapping("/reject")
	public String openRejectModel() {
		return PREFIX + "/reject";
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
		Map<String, Object> params = shipment.getParams();
		if (params == null) {
			params = new HashMap<>();
		}
		params.put("processStatus", "Y");
		Integer[] serviceArray = { EportConstants.SERVICE_UNLOADING_CARGO };
		params.put("serviceArray", serviceArray);
//		params.put("dateReceiptStatus", EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_PROGRESS);
		shipment.setParams(params);
		List<Shipment> shipments = shipmentService.selectShipmentListByWithShipmentDetailFilter(shipment);
		ajaxResult.put("shipments", getDataTable(shipments));
		return ajaxResult;
	}

	@GetMapping("/shipment/{shipmentId}/shipmentDetails/dateReceiptStatus/{dateReceiptStatus}")
	@ResponseBody
	public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId,
			@PathVariable("dateReceiptStatus") String dateReceiptStatus) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setDateReceiptStatus(dateReceiptStatus);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	@PostMapping("/confirmation")
	@ResponseBody
	@Transactional
	public AjaxResult submitConfirmation(String shipmentDetailIds, Long logisticGroupId) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				logisticGroupId);
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setDateReceiptStatus(EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_SUCCESS);
			shipmentDetail.setUpdateBy(getUser().getLoginName());
			shipmentDetailService.updateShipmentDetail(shipmentDetail);
		}
		return success("Xác nhận thành công.");
	}

	@PostMapping("/reject")
	@ResponseBody
	@Transactional
	public AjaxResult rejectRequestContDangerous(String shipmentDetailIds) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();

		shipmentDetail.setDateReceiptStatus(EportConstants.DATE_RECEIPT_STATUS_SHIPMENT_DETAIL_ERROR);
		shipmentDetail.setUpdateBy(getUser().getLoginName());

		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

		return success("Đã từ chối yêu cầu.");
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

	@GetMapping("/shipment-detail/{shipmentDetailId}/house-bill")
	public String getCfsHouseBill(@PathVariable("shipmentDetailId") Long shipmentDetailId, ModelMap mmap) {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetail != null) {
			mmap.put("masterBill", shipmentDetail.getBookingNo());
			mmap.put("containerNo", shipmentDetail.getContainerNo());
			mmap.put("shipmentDetailId", shipmentDetailId);
		}
		return PREFIX + "/houseBill";
	}

	@GetMapping("shipment-detail/{shipmentDetailId}/house-bills")
	@ResponseBody
	public AjaxResult getHouseBillList(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		CfsHouseBill cfsHouseBillParam = new CfsHouseBill();
		cfsHouseBillParam.setShipmentDetailId(shipmentDetailId);
//		cfsHouseBillParam.setLogisticGroupId(getUser().getGroupId());
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("cfsHouseBills", cfsHouseBillService.selectCfsHouseBillList(cfsHouseBillParam));
		return ajaxResult;
	}

	// SAVE OR EDIT SHIPMENT DETAIL
	@Log(title = "Lưu Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			ShipmentDetail firstDetail = shipmentDetails.get(0);
			Long shipmentId = firstDetail.getShipmentId();
			if (shipmentId == null) {
				return error("Không tìm thấy lô cần thêm chi tiết.");
			}
			Shipment shipment = shipmentService.selectShipmentById(shipmentId);

			// container no list string separated by comma
			String containerNos = "";
			for (ShipmentDetail inputDetail : shipmentDetails) {
				containerNos += inputDetail.getContainerNo() + ",";
			}
			if (StringUtils.isNotEmpty(containerNos)) {
				containerNos = containerNos.substring(0, containerNos.length() - 1);
			}

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {
				if (inputDetail.getId() != null) {
					updateShipment = false;
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());
					// Check permission
					shipmentDetailReference.setDateReceipt(inputDetail.getDateReceipt());
					shipmentDetailReference.setUpdateBy(getUser().getUserName());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetailReference) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetailReference.getContainerNo());
					}
				}
				if (updateShipment) {
					shipment.setUpdateBy(getUser().getUserName());
					shipment.setStatus(EportConstants.SHIPMENT_STATUS_SAVE);
					shipmentService.updateShipment(shipment);
				}
				return success("Lưu khai báo thành công");
			}
			return error("Lưu khai báo thất bại");
		}
		return error("Lưu khai báo thất bại");
	}

	@Log(title = "Mở khóa house bill", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/house-bill/unlock")
	@Transactional
	@ResponseBody
	public AjaxResult unlockHouseBill(@RequestBody List<CfsHouseBill> cfsHouseBills) {
		if (CollectionUtils.isNotEmpty(cfsHouseBills)) {
			for (CfsHouseBill inputHouseBill : cfsHouseBills) {
				CfsHouseBill bill = this.cfsHouseBillService.selectCfsHouseBillById(inputHouseBill.getId());
				bill.setStatus("O");
				bill.setUpdateBy(getUser().getUserName());
				cfsHouseBillService.updateCfsHouseBill(bill);

			}
			return success("Lưu house bill thành công");
		}
		return error("Lưu house bill thất bại");

	}

	@Log(title = "Khóa house bill", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/house-bill/lock")
	@Transactional
	@ResponseBody
	public AjaxResult lockHouseBill(@RequestBody List<CfsHouseBill> cfsHouseBills) {
		if (CollectionUtils.isNotEmpty(cfsHouseBills)) {
			for (CfsHouseBill inputHouseBill : cfsHouseBills) {
				CfsHouseBill bill = this.cfsHouseBillService.selectCfsHouseBillById(inputHouseBill.getId());
				bill.setStatus("L");
				bill.setUpdateBy(getUser().getUserName());
				cfsHouseBillService.updateCfsHouseBill(bill);

			}
			return success("Lưu house bill thành công");
		}
		return error("Lưu house bill thất bại");

	}
}
