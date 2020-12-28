package vn.com.irtech.eport.web.controller.reeferGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

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
import vn.com.irtech.eport.web.controller.AdminBaseController;
import vn.com.irtech.eport.web.mqtt.MqttService;
import vn.com.irtech.eport.web.mqtt.MqttService.NotificationCode;

@Controller
@RequestMapping("/reefer-group/receive-cont-empty")
public class ReceiveContReeferEmpty extends AdminBaseController {
	private final static String PREFIX = "reeferGroup/receiveContEmpty";
	private final static String keyReefer = "R";
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

	@PostMapping("/shipment-details")
	@ResponseBody
	public AjaxResult getShipmentDetails(@RequestBody ShipmentDetail shipmentDetail) {
		AjaxResult ajaxResult = AjaxResult.success();

		List<ShipmentDetail> shipmentDetails = shipmentDetailService
				.getShipmentDetailListInnerJoinToShipment(shipmentDetail);
		if (shipmentDetails != null) {
			ajaxResult.put("shipmentDetails", shipmentDetails);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
	}

	@PostMapping("/shipment-details-ver2")
	@ResponseBody
	public AjaxResult getShipmentDetailsVer2(@RequestBody PageAble<ShipmentDetail> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		
		AjaxResult ajaxResult = AjaxResult.success();

		ShipmentDetail shipmentDetail = param.getData();
		List<ShipmentDetail> shipmentDetails = shipmentDetailService
				.getShipmentDetailListInnerJoinToShipment(shipmentDetail);
		if (shipmentDetails != null) {
			ajaxResult.put("shipmentDetails", shipmentDetails);
		} else {
			ajaxResult = AjaxResult.error();
		}
		ajaxResult.put("shipmentDetails", getDataTable(shipmentDetails));
		return ajaxResult;
	}

	
	@GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
	public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {

		mmap.put("containerNo", containerNo);
		mmap.put("sztp", sztp);
		mmap.put("shipmentDetailId", shipmentDetailId);
		mmap.put("shipmentDetail", this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId));

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		mmap.put("shipmentFiles", shipmentImages);
		return PREFIX + "/detail";
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
	
	@GetMapping("/shipments/{id}")
	@ResponseBody
	public AjaxResult getShipmentById(@PathVariable("id") Long id) {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipment", this.shipmentService.selectShipmentById(id));
		return ajaxResult;
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setSztp(keyReefer);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
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

	@PostMapping("/delete")
	@ResponseBody
	public AjaxResult deleteSupply(String content, String shipmentDetailIds) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setProcessStatus(EportConstants.PROCESS_STATUS_SHIPMENT_DETAIL_DELETE);
		shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_HOLD);
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

		for (String i : shipmentDetailIds.split(",")) {
			ShipmentDetail detail = this.shipmentDetailService.selectShipmentDetailById(Long.parseLong(i));
			Long shipmentId = detail.getShipmentId();
			Long logisticGroupId = detail.getLogisticGroupId();
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

	@PostMapping("/confirm")
	@Transactional
	@ResponseBody
	public AjaxResult confirm(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			boolean allUpdate = true;
			SysUser user = ShiroUtils.getSysUser();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (StringUtils.isEmpty(shipmentDetail.getContainerNo()) || EportConstants.CONTAINER_SUPPLY_STATUS_REQ
						.equalsIgnoreCase(shipmentDetail.getContSupplyStatus())) {
					allUpdate = false;
				} 
//				else if (!EportConstants.CONTAINER_SUPPLY_STATUS_HOLD
//						.equalsIgnoreCase(shipmentDetail.getContSupplyStatus())) {
					shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_FINISH);
					// Set container is qualified to verify otp to make order with status = 2
					shipmentDetail.setStatus(2);
					shipmentDetail.setContSupplierName(user.getLoginName());
//				}
				shipmentDetail.setUpdateBy(user.getLoginName());
				if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
					return error("Cấp container thất bại từ container: " + shipmentDetail.getContainerNo());
				}
			}
//			if (allUpdate) {
//				Shipment shipment = new Shipment();
//				shipment.setId(shipmentDetails.get(0).getShipmentId());
//				shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
//				shipment.setUpdateTime(new Date());
//				shipment.setUpdateBy(user.getLoginName());
//				shipmentService.updateShipment(shipment);
//			}
			return success("Cấp container thành công");
		}
		return error("Cấp container thất bại");
	}

	@PostMapping("/reject")
	@ResponseBody
	public AjaxResult rejectSupply(@RequestBody List<ShipmentDetail> shipmentDetails) {
		StringBuilder shipmentDetailIds = new StringBuilder();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds.append(shipmentDetail.getId());
			shipmentDetailIds.append(",");
		}
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_HOLD);
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds.toString(), shipmentDetail);
		return success();
	}

	@PostMapping("/reject-information")
	@ResponseBody
	public AjaxResult rejectSupplyInformation(String content, String shipmentDetailIds) {

		for (String i : shipmentDetailIds.split(",")) {
			ShipmentDetail shipmentDetail = this.shipmentDetailService.selectShipmentDetailById(Long.parseLong(i));
			Long shipmentId = shipmentDetail.getShipmentId();
			Long logisticGroupId = shipmentDetail.getLogisticGroupId();
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
		}
		return success();
	}

}
