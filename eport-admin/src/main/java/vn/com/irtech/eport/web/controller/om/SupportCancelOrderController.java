package vn.com.irtech.eport.web.controller.om;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/support/cancel")
public class SupportCancelOrderController extends AdminBaseController  {

	private String PREFIX = "om/support/cancel";
	
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
	private IProcessOrderService processOrderService;
	
	@Autowired
	private IPickupAssignService pickupAssignService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private ServerConfig serverConfig;
	
	@Autowired
	private IShipmentImageService shipmentImageService;
	
	@GetMapping()
	public String getView(@RequestParam(required = false) Long sId, ModelMap mmap) {
		
		if (sId != null) {
			mmap.put("sId", sId);
		}
		
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
	    if(berthplanList == null) {
	    	berthplanList = new ArrayList<>();
	    }
	    ShipmentDetail shipmentDetail = new ShipmentDetail();
	    shipmentDetail.setVslAndVoy("Chọn tàu chuyến");
	    berthplanList.add(0, shipmentDetail);
	    mmap.put("vesselAndVoyages", berthplanList);
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
		Map<String, Object> params = new HashMap<>();
		params.put("processStatus", EportConstants.PROCESS_STATUS_SHIPMENT_DETAIL_DELETE);
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
	
	@Log(title = "Xóa container", businessType = BusinessType.DELETE, operatorType = OperatorType.MANAGE)
	@PostMapping("/shipment-detail/cancel")
	@ResponseBody
	public AjaxResult deleteShipmentDetail(String shipmentDetailIds, Long shipmentId) {
		
		logger.debug("Delete all shipment detail om want to delete");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds);
		
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
		// if it is then delete both pickup by shipment and shipment detail else only shipment detail
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
}
