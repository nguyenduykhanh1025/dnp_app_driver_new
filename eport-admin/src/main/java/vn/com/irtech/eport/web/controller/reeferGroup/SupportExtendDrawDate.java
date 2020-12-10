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
@RequestMapping("/reefer-gruop/extend-draw-date")
public class SupportExtendDrawDate extends AdminBaseController {
	private static final Logger logger = LoggerFactory.getLogger(SupportDateSetupTemperature.class);

	private String PREFIX = "/reeferGruop/extendDrawDate";

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

	@GetMapping("/shipment/{shipmentId}/shipmentDetails")
	@ResponseBody
	public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setSztp(KEY_ICE);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	@GetMapping("/reject")
	public String openRejectModel() {
		return PREFIX + "/reject";
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

		mmap.put("shipmentDetail", this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId));

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
		mmap.put("billPowers", dictService.getType("bill_power"));

		return PREFIX + "/detail";
	}

	@PostMapping("/shipmentDetail/confirm")
	@ResponseBody
	public AjaxResult confirmExtendDateDrop(String idShipmentDetails) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		for (String id : idShipmentDetails.split(",")) {
			List<ReeferInfo> infos = reeferInfoService.selectReeferInfoListByIdShipmentDetail(Long.parseLong(id));
			ShipmentDetail shipmentDetailFromDB = shipmentDetailService.selectShipmentDetailById(Long.parseLong(id));
			shipmentDetail.setPowerDrawDate(infos.get(0).getDateGetPower());
			shipmentDetail.setId(Long.parseLong(id));
			shipmentDetail.setPowerDrawDateStatus("S");
			shipmentDetail.setDaySetupTemperature(shipmentDetailFromDB.getPowerDrawDate());
			shipmentDetailService.updateShipmentDetail(shipmentDetail);

			for (ReeferInfo info : infos) {
				info.setStatus("S");
				info.setUpdateBy(getUser().getUserName());

				// if no da thanh toan
				boolean isPayment = false;
				List<SysDictData> sysDictDatas = dictService.getType("opr_list_booking_check");
				for (SysDictData data : sysDictDatas) {
					if (data.getDictValue().equals(shipmentDetailFromDB.getOpeCode())) {
						isPayment = true;
					}
				}
				Long logictistId = shipmentDetailFromDB.getLogisticGroupId();
				LogisticGroup groupFromDB = this.logisticGroupService.selectLogisticGroupById(logictistId);
				if (!groupFromDB.getCreditFlag().equals("0")) {
					isPayment = true;
				}

				if (isPayment) {
					info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_SUCCESS);
				} else {
					info.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_PROCESS);
				}

				this.reeferInfoService.updateReeferInfo(info);
			}
		}
//		shipmentDetailService.updateShipmentDetailByIds(idShipmentDetails, shipmentDetail);
		return success();
	}

	@PostMapping("/shipmentDetail/reject")
	@ResponseBody
	public AjaxResult rejectExtendDateDrop(String idShipmentDetails) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setPowerDrawDateStatus("E");
		shipmentDetailService.updateShipmentDetailByIds(idShipmentDetails, shipmentDetail);
		return success();
	}

	@PostMapping("/save-reefer")
	@ResponseBody
	public AjaxResult saveReeferInfo(@RequestBody List<ReeferInfo> reeferInfos) {
		for (ReeferInfo reeferInfo : reeferInfos) {
			reeferInfoService.updateReeferInfo(reeferInfo);
		}
		return AjaxResult.success(
				reeferInfoService.selectReeferInfoListByIdShipmentDetail(reeferInfos.get(0).getShipmentDetailId()));
	}
}
