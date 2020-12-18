package vn.com.irtech.eport.web.controller.yard.management;

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
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ICfsHouseBillService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/yard/monitor/loading-cargo")
public class YardMonitorLoadingCargoController extends AdminBaseController {
	protected final Logger logger = LoggerFactory.getLogger(YardMonitorLoadingCargoController.class);
	private final String PREFIX = "yard/loadingCargo";

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private ICfsHouseBillService cfsHouseBillService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private DictService dictDataService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ServerConfig serverConfig;

	@GetMapping("/view")
	public String getViewSupportReceiveFull(@RequestParam(required = false) Long sId, ModelMap mmap) {
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
		return PREFIX + "/loadingCargo";
	}

	@GetMapping("/shipment-detail/{shipmentDetailId}/house-bill")
	public String getCfsHouseBill(@PathVariable("shipmentDetailId") Long shipmentDetailId, ModelMap mmap) {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetail != null) {
			mmap.put("masterBill", shipmentDetail.getBookingNo());
			mmap.put("containerNo", shipmentDetail.getContainerNo());
			mmap.put("shipmentDetailId", shipmentDetailId);
			// Get image file from shipment detail id
			ShipmentImage shipmentImage = new ShipmentImage();
			shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
			List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
			if (CollectionUtils.isNotEmpty(shipmentImages)) {
				for (ShipmentImage shipmentImage2 : shipmentImages) {
					shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
				}
			}
			mmap.put("shipmentImages", shipmentImages);
		}
		return PREFIX + "/cargo";
	}

	@GetMapping("/reject")
	public String getConfirmationForm() {
		return PREFIX + "/confirmation";
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
			params = new HashMap<String, Object>();
		}
		params.put("dateReceiptStatus", "Y");
		shipment.setParams(params);
		shipment.setServiceType(EportConstants.SERVICE_LOADING_CARGO_YARD);
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
		shipmentDetail.setDateReceiptStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}

	@PostMapping("/finish/confirm")
	@ResponseBody
	@Transactional
	public AjaxResult confirmOrder(String shipmentDetailIds) {
		ShipmentDetail shipmentDetailUpdate = new ShipmentDetail();
		shipmentDetailUpdate.setFinishStatus("Y");
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetailUpdate);
		return success();
	}

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

	@PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		SysUser user = getUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_LOADING_CARGO_YARD);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@GetMapping("shipment-detail/{shipmentDetailId}/house-bills")
	@ResponseBody
	public AjaxResult getHouseBillList(@PathVariable("shipmentDetailId") Long shipmentDetailId) {
		CfsHouseBill cfsHouseBillParam = new CfsHouseBill();
		cfsHouseBillParam.setShipmentDetailId(shipmentDetailId);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("cfsHouseBills", cfsHouseBillService.selectCfsHouseBillList(cfsHouseBillParam));
		return ajaxResult;
	}

	@GetMapping("/shipments/{shipmentId}/shipment-images")
	@ResponseBody
	public AjaxResult getShipmentImages(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentId(shipmentId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nullShipmentDetailId", true);
		shipmentImage.setParams(params);
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		ajaxResult.put("shipmentFiles", shipmentImages);
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
}
