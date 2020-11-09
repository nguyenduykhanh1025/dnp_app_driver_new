package vn.com.irtech.eport.web.controller.gate;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.web.dto.DetectionInfomation;
import vn.com.irtech.eport.web.dto.GateInDataReq;
import vn.com.irtech.eport.web.dto.GateInTestDataReq;
import vn.com.irtech.eport.web.dto.GateNotificationCheckInReq;
import vn.com.irtech.eport.web.dto.SensorResult;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Controller
@RequestMapping("/gate/support")
public class GateSupportController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GateSupportController.class);

	private final static String PREFIX = "gate/support";

	private static final String GATE_DETECTION_REQUEST = "eport/detection/gate/+/request";

	private static final Integer DEFAULT_DEDUCT_TRUCK = 13000;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private IDriverAccountService driverAccountService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IGateDetectionService gateDetectionService;

	@Autowired
	private MqttService mqttService;

	@GetMapping()
	public String getView() {
		return PREFIX + "/index";
	}

	@PostMapping("/detection")
	@ResponseBody
	public AjaxResult submitDectionInfo(@Validated @RequestBody DetectionInfomation detection) {

		String detectJson = new Gson().toJson(detection);
		logger.debug(">>>> Receive detection info:" + detectJson);
		// Save to db
		GateDetection dt = new GateDetection();
		dt.setGateNo(detection.getGateId());
		dt.setTruckNo(detection.getTruckNo());
		dt.setChassisNo(detection.getChassisNo());
		dt.setContainerNo1(detection.getContainerNo1());
		dt.setContainerNo2(detection.getContainerNo2());
		dt.setStatus("W");

		gateDetectionService.insertGateDetection(dt);

		// Put gate detection info into cache to get when has driver request check in
		String truckNo = dt.getTruckNo();
		String chassisNo = dt.getChassisNo();
		if (StringUtils.isNotEmpty(truckNo)) {
			dt.setTruckNo(truckNo.replace("-", ""));
		}
		if (StringUtils.isNotEmpty(chassisNo)) {
			dt.setChassisNo(chassisNo.replace("-", ""));
		}
		CacheUtils.put(dt.getGateNo() + "_" + EportConstants.CACHE_GATE_DETECTION_KEY, dt);

		senDetectionInfoToGate(dt);

		// Send to monitor
		webSocketService.sendMessage("/gate/detection/monitor", detection);
		return success();
	}

	@PostMapping("/sensor")
	@ResponseBody
	public AjaxResult submitSensorResult(@Validated @RequestBody SensorResult sensorResult) {

		logger.debug(">>>>> Receive sensor result info: " + new Gson().toJson(sensorResult));
		CacheUtils.put("sensorInfo_" + sensorResult.getGateId(), sensorResult);

		// Check if truck on gate
		List<Integer> sensorValue = sensorResult.getSensors();
		if (sensorValue.size() >= 3) {
			if (sensorValue.get(0) == 0 && sensorValue.get(1) == 0 && sensorValue.get(2) == 0) {
				CacheUtils.remove(sensorResult.getGateId() + "_" + EportConstants.CACHE_GATE_DETECTION_KEY);
			}
		}
		return success();
	}

	private void senDetectionInfoToGate(GateDetection detectionInfo) {
		GateNotificationCheckInReq gateNotificationCheckInReq = new GateNotificationCheckInReq();
		gateNotificationCheckInReq.setReceiveOption(false);
		gateNotificationCheckInReq.setSendOption(true);
		gateNotificationCheckInReq.setRefFlg1(false);
		gateNotificationCheckInReq.setRefFlg2(false);
		gateNotificationCheckInReq.setId(detectionInfo.getId());
		String truckNo = detectionInfo.getTruckNo();
		if (StringUtils.isNotEmpty(truckNo)) {
			gateNotificationCheckInReq.setTruckNo(truckNo);
			int truckLength = truckNo.length();
			if (truckLength > 5) {
				gateNotificationCheckInReq.setGatePass(truckNo.substring(truckLength - 5, truckLength));
			}
		}
		gateNotificationCheckInReq.setChassisNo(detectionInfo.getChassisNo());
		gateNotificationCheckInReq.setDeduct(DEFAULT_DEDUCT_TRUCK);
		gateNotificationCheckInReq.setContainerSend1(detectionInfo.getContainerNo1());
		gateNotificationCheckInReq.setContainerSend2(detectionInfo.getContainerNo2());

		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setTitle("ePort: Phát hiện xe tại cổng.");
		notificationReq.setMsg("Có xe ở cổng đang đợi làm lệnh " + gateNotificationCheckInReq.getTruckNo());
		notificationReq.setType(EportConstants.APP_USER_TYPE_GATE);
		notificationReq.setLink("");
		notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
		notificationReq.setGateData(gateNotificationCheckInReq);

		String msg = new Gson().toJson(notificationReq);
		try {
			logger.warn(">>>>>>>>>>>>>>>>> Send GATE Dialog: " + msg);
			mqttService.publish(GATE_DETECTION_REQUEST.replace("+", detectionInfo.getGateNo()),
					new MqttMessage(msg.getBytes()));
		} catch (MqttException e) {
			logger.error("Error when try sending notification request check in for gate: " + e);
		}
	}

	@PostMapping("/logistics")
	@ResponseBody
	public List<LogisticGroup> getLogisticGroups() {
		LogisticGroup logisticGroup = new LogisticGroup();
		logisticGroup.setGroupName("Chọn đơn vị Logistics");
		logisticGroup.setId(0L);
		LogisticGroup logisticGroupParam = new LogisticGroup();
		logisticGroupParam.setDelFlag("0");
		List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
		logisticGroups.add(0, logisticGroup);
		return logisticGroups;
	}

	@PostMapping("/logistic/{groupId}/drivers")
	@ResponseBody
	public List<DriverAccount> getDriverAccounts(@PathVariable("groupId") Long groupId) {
		DriverAccount driverAccount = new DriverAccount();
		driverAccount.setLogisticGroupId(groupId);
		return driverAccountService.selectDriverAccountList(driverAccount);
	}

	@PostMapping("/gateIn")
	@ResponseBody
	public AjaxResult gateIn(@RequestBody GateInDataReq gateInDataReq) {
		if (StringUtils.isNotEmpty(gateInDataReq.getTruckNo())) {
			PickupHistory pickupHistory = new PickupHistory();
			pickupHistory.setTruckNo(gateInDataReq.getTruckNo());
			pickupHistory.setChassisNo(gateInDataReq.getChassisNo());
			pickupHistory.setStatus(0);
			logger.debug("Get pickup history info from " + gateInDataReq.getTruckNo() + " and chassisno "
					+ gateInDataReq.getChassisNo());
			;
			List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistory);
			if (CollectionUtils.isNotEmpty(pickupHistories)) {
				// Check if having req option send container
				if (gateInDataReq.getSendOption()) {
					// Index of container to send (max = 1, min = 0)
					for (PickupHistory pickupHistory2 : pickupHistories) {
						if (StringUtils.isNotEmpty(gateInDataReq.getContainerSend1()) && gateInDataReq
								.getContainerSend1().equalsIgnoreCase(pickupHistory2.getContainerNo())) {
							if (StringUtils.isNotEmpty(gateInDataReq.getYardPosition1())) {
								String[] yardPositionArr = gateInDataReq.getYardPosition1().split("-");
								if (yardPositionArr.length == 4) {
									pickupHistory2.setBlock(yardPositionArr[0]);
									pickupHistory2.setBay(yardPositionArr[1]);
									pickupHistory2.setLine(yardPositionArr[2]);
									pickupHistory2.setTier(yardPositionArr[3]);
									pickupHistoryService.updatePickupHistory(pickupHistory2);
								}
							}
						}
						if (StringUtils.isNotEmpty(gateInDataReq.getContainerSend2()) && gateInDataReq
								.getContainerSend2().equalsIgnoreCase(pickupHistory2.getContainerNo())) {
							if (StringUtils.isNotEmpty(gateInDataReq.getYardPosition2())) {
								String[] yardPositionArr = gateInDataReq.getYardPosition1().split("-");
								if (yardPositionArr.length == 4) {
									pickupHistory2.setBlock(yardPositionArr[0]);
									pickupHistory2.setBay(yardPositionArr[1]);
									pickupHistory2.setLine(yardPositionArr[2]);
									pickupHistory2.setTier(yardPositionArr[3]);
									pickupHistoryService.updatePickupHistory(pickupHistory2);
								}
							}
						}
					}
				}

				// Check if have req option receive container
				if (gateInDataReq.getReceiveOption()) {
					// TODO :
				}
			}
		}
		return success();
	}

	@GetMapping("/blNo/{blNo}/yardPosition")
	@ResponseBody
	public AjaxResult getYardPositionByBlNo(@PathVariable String blNo) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ShipmentDetail> shipmentDetails = catosApiService.getCoordinateOfContainers(blNo);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			List<ShipmentDetail[][]> bay = shipmentDetailService.getContPosition(blNo, shipmentDetails);
			ajaxResult.put("bayList", bay);
			return ajaxResult;
		}
		return AjaxResult.warn("Không tìm thấy tọa độ.");
	}

	@GetMapping("/jobOrder/{jobOrder}/yardPosition")
	@ResponseBody
	public AjaxResult getYardPositionByJobOrder(@PathVariable("jobOrder") String jobOrder) {
		AjaxResult ajaxResult = AjaxResult.success();
		List<ShipmentDetail> shipmentDetails = catosApiService.getCoordinateOfContainersByJobOrderNo(jobOrder);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			List<ShipmentDetail[][]> bay = shipmentDetailService.getContPosition(shipmentDetails.get(0).getBlNo(),
					shipmentDetails);
			ajaxResult.put("bayList", bay);
			return ajaxResult;
		}
		return AjaxResult.warn("Không tìm thấy tọa độ.");
	}

	@PostMapping("/pickupList")
	@ResponseBody
	public AjaxResult getPickupList(@RequestBody GateInTestDataReq gateInTestDataReq) {
		if (StringUtils.isNotEmpty(gateInTestDataReq.getTruckNo())) {
			PickupHistory pickupHistory = new PickupHistory();
			pickupHistory.setTruckNo(gateInTestDataReq.getTruckNo());
			pickupHistory.setChassisNo(gateInTestDataReq.getChassisNo());
			pickupHistory.setStatus(0);
			List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistory);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("pickupList", getDataTable(pickupHistories));
			return ajaxResult;
		}
		return AjaxResult.warn("Bạn chưa nhập truck no.");
	}
}
