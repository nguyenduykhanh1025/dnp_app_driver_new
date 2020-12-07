package vn.com.irtech.eport.web.controller.container.supplier;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
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
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.web.mqtt.MqttService;
import vn.com.irtech.eport.web.mqtt.MqttService.NotificationCode;

@Controller
@RequestMapping("/container/supplier")
public class ContainerSupplierController extends BaseController {

	private final static String PREFIX = "container/supplier";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ICatosApiService catosApiService;

	@GetMapping()
	public String getContSupplier(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());
		return PREFIX + "/index";
	}

	@GetMapping("/confirmation")
	public String getConfirmationForm() {
		return PREFIX + "/confirmation";
	}

	@PostMapping("/shipments")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody PageAble<Shipment> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Shipment shipment = param.getData();
		if (shipment == null) {
			shipment = new Shipment();
		}
		shipment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		List<Shipment> shipments = shipmentService.getShipmentListForContSupply(shipment);
		return getDataTable(shipments);
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		// not have cont sztp Reefer
		shipmentDetail.setSztp("R");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailListNotHaveContReefer(shipmentDetail);
		if (shipmentDetails != null) {
			ajaxResult.put("shipmentDetails", shipmentDetails);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	@PostMapping("/shipment/{shipmentId}/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@PathVariable Long shipmentId,
			@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			boolean allUpdate = true;
			SysUser user = ShiroUtils.getSysUser();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (StringUtils.isEmpty(shipmentDetail.getContainerNo()) || EportConstants.CONTAINER_SUPPLY_STATUS_REQ
						.equalsIgnoreCase(shipmentDetail.getContSupplyStatus())) {
					allUpdate = false;
				} else if (!EportConstants.CONTAINER_SUPPLY_STATUS_HOLD
						.equalsIgnoreCase(shipmentDetail.getContSupplyStatus())) {
					shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_FINISH);
					// Set container is qualified to verify otp to make order with status = 2
					shipmentDetail.setStatus(2);
					shipmentDetail.setContSupplierName(user.getLoginName());
				}
				shipmentDetail.setUpdateBy(user.getLoginName());
				if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
					return error("Cấp container thất bại từ container: " + shipmentDetail.getContainerNo());
				}
			}
			if (allUpdate) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(user.getLoginName());
				shipmentService.updateShipment(shipment);
			}
			return success("Cấp container thành công");
		}
		return error("Cấp container thất bại");
	}

	@GetMapping("/report")
	public String getReport() {
		return PREFIX + "/report";
	}

	@PostMapping("/supplierReport")
	@ResponseBody
	public TableDataInfo supplierReport(@RequestBody PageAble<ShipmentDetail> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		ShipmentDetail shipmentDetail = param.getData();
		if (shipmentDetail == null) {
			shipmentDetail = new ShipmentDetail();
		}
		shipmentDetail.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		List<ShipmentDetail> dataList = shipmentDetailService.selectShipmentDetailListReport(shipmentDetail);
		return getDataTable(dataList);
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

	// COUNT SHIPMENT IMAGE BY SHIPMENT ID
	@GetMapping("/shipments/{shipmentId}/shipment-images/count")
	@ResponseBody
	public AjaxResult countShipmentImage(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		int numberOfShipmentImage = shipmentImageService.countShipmentImagesByShipmentId(shipment.getId());
		ajaxResult.put("numberOfShipmentImage", numberOfShipmentImage);
		return ajaxResult;
	}

	@PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		SysUser user = ShiroUtils.getSysUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@PostMapping("/reject")
	@ResponseBody
	public AjaxResult rejectSupply(String content, String shipmentDetailIds, Long shipmentId, Long logisticGroupId) {
		if (StringUtils.isEmpty(shipmentDetailIds) || shipmentId == null || logisticGroupId == null) {
			return error("Invalid input!");
		}

		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_HOLD);
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

		SysUser user = ShiroUtils.getSysUser();
		ShipmentComment shipmentComment = new ShipmentComment();
		shipmentComment.setShipmentId(shipmentId);
		shipmentComment.setLogisticGroupId(logisticGroupId);
		shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CONT_SUPPLIER_REJECT);
		shipmentComment.setContent(content);
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Send notification to om
		try {
			mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, shipmentComment.getTopic(),
					shipmentComment.getContent(), "", EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
		} catch (MqttException e) {
			logger.error("Fail to send message om notification app: " + e);
		}

		// Check update shipment if all container doesn't need container for supply
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_REQ);
		if (CollectionUtils.isEmpty(shipmentDetailService.selectShipmentDetailList(shipmentDetailParam))) {
			Shipment shipment = new Shipment();
			shipment.setId(shipmentId);
			shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
			shipmentService.updateShipment(shipment);
		}

		return success();
	}

//	@PostMapping("/order/cancel")
//	@ResponseBody
//	public AjaxResult cancelOrderReq(String content, String shipmentDetailIds, Long shipmentId,  Long logisticGroupId) {		
//		// Check container process status Y 
//		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
//		if (CollectionUtils.isEmpty(shipmentDetails)) {
//			return error("Không tìm thấy dữ liệu container trong hệ thống.");
//		}
//		
//		String containerNotOrder = "";
//		for (ShipmentDetail shDetail : shipmentDetails) {
//			if (!"Y".equals(shDetail.getProcessStatus())) {
//				containerNotOrder += shDetail.getContainerNo() + ",";
//			}
//		}
//		if (containerNotOrder.length() > 0) {
//			return error("Các container " + containerNotOrder.substring(0, containerNotOrder.length()-1) + " chưa được làm lệnh trên eport.");
//		}
//		
//		ShipmentDetail shipmentDetail = new ShipmentDetail();
//		shipmentDetail.setProcessStatus(EportConstants.PROCESS_STATUS_SHIPMENT_DETAIL_DELETE);
//		shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_HOLD);
//		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);
//		
//		// Create process order cancel pickup empty
//		// Send req cancel order to robot
//		ProcessOrder processOrder = new ProcessOrder();
//		ProcessJsonData processJsonData = new ProcessJsonData();
//		processJsonData.setShDetailIds(shipmentDetailIds);
//		processOrder.setProcessData(new Gson().toJson(processJsonData));
//		
//		SysUser user = ShiroUtils.getSysUser();
//		ShipmentComment shipmentComment = new ShipmentComment();
//		shipmentComment.setShipmentId(shipmentId);
//		shipmentComment.setLogisticGroupId(logisticGroupId);
//		shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CONT_SUPPLIER_DELETE);
//		shipmentComment.setContent(content);
//		shipmentComment.setCreateBy(user.getUserName());
//		shipmentComment.setUserId(user.getUserId());
//		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
//		shipmentComment.setUserAlias(user.getDept().getDeptName());
//		shipmentComment.setUserName(user.getUserName());
//		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
//		shipmentComment.setCommentTime(new Date());
//		shipmentComment.setResolvedFlg(true);
//		shipmentCommentService.insertShipmentComment(shipmentComment);
//		
//		// Check update shipment if all container doesn't need container for supply
//		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
//		shipmentDetailParam.setShipmentId(shipmentId);
//		shipmentDetailParam.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_REQ);
//		if (CollectionUtils.isEmpty(shipmentDetailService.selectShipmentDetailList(shipmentDetailParam))) {
//			Shipment shipment = new Shipment();
//			shipment.setId(shipmentId);
//			shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
//			shipmentService.updateShipment(shipment);
//		}
//		
//		return success();
//	}

	@PostMapping("/delete")
	@ResponseBody
	public AjaxResult deleteSupply(String content, String shipmentDetailIds, Long shipmentId, Long logisticGroupId) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setProcessStatus(EportConstants.PROCESS_STATUS_SHIPMENT_DETAIL_DELETE);
		shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_HOLD);
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

		SysUser user = ShiroUtils.getSysUser();
		ShipmentComment shipmentComment = new ShipmentComment();
		shipmentComment.setShipmentId(shipmentId);
		shipmentComment.setLogisticGroupId(logisticGroupId);
		shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CONT_SUPPLIER_DELETE);
		shipmentComment.setContent(content);
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setUserId(user.getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
		shipmentComment.setUserAlias(user.getDept().getDeptName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setResolvedFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Send notification to om
		try {
			mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, shipmentComment.getTopic(),
					shipmentComment.getContent(), "", EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
		} catch (MqttException e) {
			logger.error("Fail to send message om notification app: " + e);
		}

		// Check update shipment if all container doesn't need container for supply
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_REQ);
		if (CollectionUtils.isEmpty(shipmentDetailService.selectShipmentDetailList(shipmentDetailParam))) {
			Shipment shipment = new Shipment();
			shipment.setId(shipmentId);
			shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
			shipmentService.updateShipment(shipment);
		}

		return success();
	}

	@PostMapping("/shipment-detail/cont/info")
	@ResponseBody
	public AjaxResult getContInfo(@RequestBody ShipmentDetail shipmentDetail) {
		if (StringUtils.isNotEmpty(shipmentDetail.getContainerNo())) {
			shipmentDetail.setFe("E");
			ShipmentDetail shipmentDetailResult = catosApiService.selectShipmentDetailByContNo(shipmentDetail);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("shipmentDetailResult", shipmentDetailResult);

			// Check container da lam lenh
			if (shipmentDetail.getId() != null) {
				ShipmentDetail shipmentDetailParam = new ShipmentDetail();
				shipmentDetailParam.setContainerNo(shipmentDetail.getContainerNo());
				shipmentDetailParam.setProcessStatus("Y");
				if (shipmentDetailService.countShipmentDetailList(shipmentDetailParam) > 0) {
					ajaxResult.put("isOrder", true);
				}
			}

			return ajaxResult;
		} else {
			return AjaxResult.warn("Không tìm thấy thông tin container");
		}
	}
}
