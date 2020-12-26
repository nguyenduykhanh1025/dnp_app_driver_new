package vn.com.irtech.eport.web.controller.reeferGroup;

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

import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.ReeferInfo;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IReeferInfoService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.ShipmentDetailHist;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.service.IShipmentDetailHistService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/reefer-group/extend-draw-date")
public class SupportExtendDrawDate extends AdminBaseController {
	private static final Logger logger = LoggerFactory.getLogger(SupportDateSetupTemperature.class);

	private String PREFIX = "reeferGroup/extendDrawDate";

	private final String KEY_ICE = "R";

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
	private ServerConfig serverConfig;

	@Autowired
	private DictService dictDataService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private IShipmentDetailHistService shipmentDetailHistService;

	@Autowired
	private IReeferInfoService reeferInfoService;

	@GetMapping
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
		params.put("sztp", KEY_ICE);
		shipment.setParams(params);

		List<Shipment> shipments = shipmentService.selectShipmentListByWithShipmentDetailFilter(shipment);
		ajaxResult.put("shipments", getDataTable(shipments));
		return ajaxResult;
	}

	@GetMapping("/shipment/{shipmentId}/shipmentDetails/status/{status}")
	@ResponseBody
	public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId,
			@PathVariable("status") String status) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setSztp(KEY_ICE);
		shipmentDetail.setPowerDrawDateStatus(status);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	@PostMapping("/confirmation")
	@ResponseBody
	@Transactional
	public AjaxResult acceptRequestContIce(String shipmentDetailIds, Long logisticGroupId) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_YES);
		shipmentDetail.setUpdateBy(getUser().getLoginName());
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

		return success("Xác nhận thành công.");
	}

	@PostMapping("/reject")
	@ResponseBody
	@Transactional
	public AjaxResult rejectRequestContIce(String shipmentDetailIds) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();

		shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_CANCEL);
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

	@GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
	public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {
		mmap.put("containerNo", containerNo);
		mmap.put("sztp", sztp);
		mmap.put("shipmentDetailId", shipmentDetailId);

		mmap.put("contCargoTypes", dictDataService.getType("cont_cargo_type"));
		mmap.put("contDangerousImos", dictDataService.getType("cont_dangerous_imo"));
		mmap.put("contDangerousUnnos", dictDataService.getType("cont_dangerous_unno"));

		ShipmentDetail shipmentDetailFromDB = this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		mmap.put("shipmentDetail", shipmentDetailFromDB);

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		mmap.put("shipmentFiles", shipmentImages);

		ShipmentDetailHist shipmentDetailHist = new ShipmentDetailHist();
		shipmentDetailHist.setDataField("Power Draw Date");
		shipmentDetailHist.setShipmentDetailId(shipmentDetailId);
		mmap.put("powerDropDate", shipmentDetailHistService.selectShipmentDetailHistList(shipmentDetailHist));

		mmap.put("reeferInfos", reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId));

		mmap.put("reeferInfos", reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId));
		mmap.put("oprlistBookingCheck", dictService.getType("opr_list_booking_check"));
		mmap.put("creditFlag", logisticGroupService.selectLogisticGroupById(shipmentDetailFromDB.getLogisticGroupId())
				.getCreditFlag());
		mmap.put("billPowers", dictService.getType("bill_power"));
		mmap.put("shipment", shipmentService.selectShipmentById(shipmentDetailFromDB.getShipmentId()));

		return PREFIX + "/detail";
	}

	@GetMapping("/shipment-detail/{shipmentDetailId}/extend-power-interrupted")
	public String getExtendInterruptedInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			ModelMap mmap) {
		mmap.put("shipmentDetailId", shipmentDetailId);
		mmap.put("reeferInfos", reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId));
		return PREFIX + "/extendPowerInterrupted";
	}

	@PostMapping("/shipment-detail/{shipmentDetailId}/extend-power-interrupted")
	@ResponseBody
	public AjaxResult getExtendInterruptedInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@RequestBody ReeferInfo reeferInfo) {
		// validate
		ReeferInfo reeferInfoOldFromDB = this.reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId)
				.get(0);
		if (!reeferInfoOldFromDB.getStatus().equals("S") && !reeferInfoOldFromDB.getStatus().equals("E")) {
			return AjaxResult.error("Không thể gia hạn do thời gian gia hạn trước vấn chưa được chấp thuận");
		} else if (reeferInfoOldFromDB.getPayType() != null && reeferInfoOldFromDB.getPayType().equals("Credit")
				&& !reeferInfoOldFromDB.getPaymentStatus().equals("S")) {
			return AjaxResult.error("Không thể gia hạn do thời gian gia hạn trước vấn chưa được thanh toán.");
		} else if (reeferInfo.getDateGetPower().compareTo(reeferInfo.getDateSetPower()) < 0) {
			return AjaxResult.error("Không thể gia hạn do thời gian cắm điện không thể lớn hơn thời gian rút điện.");
		} else if (reeferInfo.getDateSetPower().compareTo(reeferInfoOldFromDB.getDateGetPower()) < 0) {
			return AjaxResult
					.error("Không thể gia hạn do thời gian cắm điện hiện tại nhỏ hơn thời gian rút điện trước đó.");
		}

		reeferInfo.setShipmentDetailId(shipmentDetailId);
		reeferInfo.setStatus("S");
		reeferInfo.setPaymentStatus("P");
		reeferInfo.setUpdateBy(getUser().getUserName());

		ShipmentDetail shipmentDetailFromDB = this.shipmentDetailService
				.selectShipmentDetailByDetailId(shipmentDetailId.toString());
		shipmentDetailFromDB.setDaySetupTemperature(reeferInfo.getDateSetPower());
		shipmentDetailFromDB.setDaySetupTemperature(reeferInfo.getDateGetPower());
		shipmentDetailFromDB.setPowerDrawDateStatus("S");
		shipmentDetailFromDB.setUpdateBy(getUser().getUserName());

		this.shipmentDetailService.updateShipmentDetail(shipmentDetailFromDB);
		this.reeferInfoService.insertReeferInfo(reeferInfo);
		return AjaxResult.success(reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId));
	}

	@PostMapping("/shipmentDetail/confirm")
	@ResponseBody
	public AjaxResult confirmExtendDateDrop(String idShipmentDetails) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		for (String id : idShipmentDetails.split(",")) {
			ReeferInfo info = reeferInfoService.selectReeferInfoListByIdShipmentDetail(Long.parseLong(id)).get(0);
			ShipmentDetail shipmentDetailFromDB = shipmentDetailService.selectShipmentDetailById(Long.parseLong(id));
			shipmentDetail.setPowerDrawDate(info.getDateGetPower());
			shipmentDetail.setId(Long.parseLong(id));
			shipmentDetail.setPowerDrawDateStatus("S");
			shipmentDetail.setDaySetupTemperature(shipmentDetailFromDB.getPowerDrawDate());
			shipmentDetailService.updateShipmentDetail(shipmentDetail);

			info.setStatus("S");
			info.setUpdateBy(getUser().getUserName());

			// if no da thanh toan
//			boolean isPayment = false;
//			List<SysDictData> sysDictDatas = dictService.getType("opr_list_booking_check");
//			for (SysDictData data : sysDictDatas) {
//				if (data.getDictValue().equals(shipmentDetailFromDB.getOpeCode())) {
//					isPayment = true;
//				}
//			}
//			Long logictistId = shipmentDetailFromDB.getLogisticGroupId();
//			LogisticGroup groupFromDB = this.logisticGroupService.selectLogisticGroupById(logictistId);
//			if (!groupFromDB.getCreditFlag().equals("0")) {
//				isPayment = true;
//			}
//
//			if (isPayment) {
//				info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_SUCCESS);
//			} else {
//				info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_PROCESS);
//			}
			// no tra sau
			info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_PROCESS);

			this.reeferInfoService.updateReeferInfo(info);

		}
		return success();
	}

	@PostMapping("/shipmentDetail/reject")
	@ResponseBody
	public AjaxResult rejectExtendDateDrop(String idShipmentDetails) {

		for (String id : idShipmentDetails.split(",")) {
			List<ReeferInfo> infosFromDB = reeferInfoService.selectReeferInfoListByIdShipmentDetail(Long.parseLong(id));
			ReeferInfo info = infosFromDB.get(0);

			info.setStatus("E");
			info.setUpdateBy(getUser().getUserName());
			info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_ERROR);

			this.reeferInfoService.updateReeferInfo(info);

			for (int i = 0; i < infosFromDB.size(); ++i) {
				if (infosFromDB.get(i).getStatus().equals("S")) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setPowerDrawDateStatus("E");
					shipmentDetail.setId(Long.parseLong(id));
					shipmentDetail.setPowerDrawDate(infosFromDB.get(i).getDateGetPower());
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					break;
				}
			}

		}
		// shipmentDetailService.updateShipmentDetailByIds(idShipmentDetails,
		// shipmentDetail);
		return success();
	}

	@PostMapping("/save-reefer-info")
	@ResponseBody
	public AjaxResult saveReeferInfo(@RequestBody List<ReeferInfo> reeferInfos) {
		for (ReeferInfo reeferInfo : reeferInfos) {
			if (reeferInfo != null) {
				ReeferInfo infoNew = new ReeferInfo();
				infoNew.setId(reeferInfo.getId());
				infoNew.setPayerType(reeferInfo.getPayerType());
				infoNew.setPayType(reeferInfo.getPayType());
				infoNew.setUpdateBy(getUser().getUserName());
				reeferInfoService.updateReeferInfo(infoNew);
			}
		}
		return AjaxResult.success("Lưu thành công");
	}

}
