package vn.com.irtech.eport.web.controller.gate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.web.dto.DetectionInfomation;
import vn.com.irtech.eport.web.dto.GateInDataReq;
import vn.com.irtech.eport.web.dto.GateInTestDataReq;
import vn.com.irtech.eport.web.dto.GateNotificationCheckInReq;
import vn.com.irtech.eport.web.dto.SensorResult;
import vn.com.irtech.eport.web.mqtt.BusinessConsts;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Controller
@RequestMapping("/gate/support")
public class GateSupportController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GateSupportController.class);

	private final static String PREFIX = "gate/support";

	private static final String GATE_DETECTION_REQUEST = "eport/detection/gate/+/request";

	private static final Integer DEFAULT_DEDUCT_TRUCK = 13000;

	private static final Long TIME_OUT_WAIT_SEAL = 3000L;

	private static final Long TIME_OUT_WAIT_WGT = 3000L;

	private static final Integer RETRY_WAIT_SEAL = 100;

	private static final Integer RETRY_WAIT_WGT = 3;

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

		// Check sensor result
		// 0 0 0, 1 0 1 => no truck at gate
		// no need to save gate detection

		Object sensorObj = CacheUtils.get("sensorInfo_" + detection.getGateId());
		@SuppressWarnings("unchecked")
		List<Integer> sensorRes = (List<Integer>) sensorObj;
		if (CollectionUtils.isNotEmpty(sensorRes) && sensorRes.size() >= 3) {
			if ((sensorRes.get(0) == 0 && sensorRes.get(1) == 0 && sensorRes.get(2) == 0)
					|| (sensorRes.get(0) == 1 && sensorRes.get(1) == 0 && sensorRes.get(2) == 1)) {
				return success();
			}
		}

		senDetectionInfoToGate(dt);

		CacheUtils.put(dt.getGateNo() + "_" + EportConstants.CACHE_GATE_DETECTION_KEY, dt);

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
			if ((sensorValue.get(0) == 0 && sensorValue.get(1) == 0 && sensorValue.get(2) == 0)
					|| (sensorValue.get(0) == 1 && sensorValue.get(1) == 0 && sensorValue.get(2) == 1)) {
				// Clear gate detection data
				CacheUtils.remove(sensorResult.getGateId() + "_" + EportConstants.CACHE_GATE_DETECTION_KEY);

				// Clear scale result at gate
				CacheUtils.remove("wgt_" + sensorResult.getGateId());
			}

			// Indicate truck move in right position
			String indicateMsg = "";
			if (sensorValue.get(0) == 0 && sensorValue.get(1) == 1 && sensorValue.get(2) == 1) {
				indicateMsg = "Vui lòng di chuyển xe về phía trước.";
			} else if (sensorValue.get(0) == 1 && sensorValue.get(1) == 1 && sensorValue.get(2) == 0) {
				indicateMsg = "Vui lòng di chuyển xe về phía sau.";
			} else if (sensorValue.get(0) == 0 && sensorValue.get(1) == 1 && sensorValue.get(2) == 0) {
				indicateMsg = "Xe đã vào đúng vị trí.";
			}

			// TODO : Send msg to web socket
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

		new Thread() {
			public void run() {
				// Container is qualified to send gate order request
				boolean cont1 = false; // Determine container 1 is qualified for directly make gate order
				boolean cont2 = false; // Determine container 2 is qualified for directly make gate order

				boolean cont1F = false; // Determine container 1 is container full => need check seal no
				boolean cont2F = false; // Determine container 2 is container full => need check seal no

				boolean wgtQualified = false; // Determine weight is accurate

				// Rule check container qualified
				// - Have info in catos reserve and job order no
				// - Is container empty, if full then need to have export seal no
				// - Except container 2 if empty => default qualified (Case detect 1 container)

				Map<String, ContainerInfoDto> cntrMap = getCntrMapFromCatos(detectionInfo);

				// check container 1 qualified
				if (StringUtils.isNotEmpty(detectionInfo.getContainerNo1())) {
					ContainerInfoDto container1 = cntrMap.get(detectionInfo.getContainerNo1());
					if (container1 != null) {
						if ("E".equalsIgnoreCase(container1.getFe())
								&& StringUtils.isNotEmpty(container1.getJobOdrNo())) {
							// Container 1 qualified
							cont1 = true;
						} else {
							// Still not qualified, case container 1 is full => check seal no
							cont1F = true;
						}
					}
				}

				// check container 2 qualified
				if (StringUtils.isNotEmpty(detectionInfo.getContainerNo2())) {
					ContainerInfoDto container2 = cntrMap.get(detectionInfo.getContainerNo2());
					if (container2 != null) {
						if ("E".equalsIgnoreCase(container2.getFe())
								&& StringUtils.isNotEmpty(container2.getJobOdrNo())) {
							// Container 2 qualified
							cont2 = true;
						} else {
							// Still not qualified, case container 2 is full => check seal no
							cont2F = true;
						}
					}
				} else {
					// Container 2 empty => default qualified
					cont2 = true;
				}

				// Check if weight is qualified
				Integer oldWgt = getCurrentWgt(detectionInfo.getGateNo());
				for (int i = 1; i <= RETRY_WAIT_WGT; i++) {

					logger.debug("Wait " + TIME_OUT_WAIT_WGT + " miliseconds");
					try {
						Thread.sleep(TIME_OUT_WAIT_WGT);
					} catch (InterruptedException e) {
						logger.error("Error sleep to wait weight info: " + e);
					}
					Integer wgt = getCurrentWgt(detectionInfo.getGateNo());
					if (oldWgt == wgt) {
						wgtQualified = true;
						break;
					} else {
						oldWgt = wgt;
					}
				}

				// Repeat check catos database in 2 minute
				// Stop when time exceed 2 minutes or seal no is input
				// Check when container 1 or container 2 is full
				if (cont1F || cont2F) {
					for (int i = 1; i <= RETRY_WAIT_SEAL; i++) {
						// Get sensor result from cache
						// 0 0 0, 1 0 1 => no truck at gate
						// Clear detection data in cache

						// Get info from catos
						cntrMap = getCntrMapFromCatos(detectionInfo);

						// If container 1 is full
						if (cont1F) {
							// Get info container 1
							ContainerInfoDto container1 = cntrMap.get(detectionInfo.getContainerNo1());
							// check if container 1 has job and seal no 3 (export seal no)
							// => qualified
							if (container1 != null) {
								if (StringUtils.isNotEmpty(container1.getJobOdrNo())
										&& StringUtils.isNotEmpty(container1.getSealNo3())) {
									cont1 = true;
								}
							}
						}

						// If container is full
						if (cont2F) {
							// Get info container 2
							ContainerInfoDto container2 = cntrMap.get(detectionInfo.getContainerNo2());
							// check if container 2 has job and seal no 3 (export seal no)
							// => qualified
							if (container2 != null) {
								if (StringUtils.isNotEmpty(container2.getJobOdrNo())
										&& StringUtils.isNotEmpty(container2.getSealNo3())) {
									cont2 = true;
								}
							}
						}

						if (cont1 && cont2) {
							break;
						}

						logger.debug("Wait " + TIME_OUT_WAIT_SEAL + " miliseconds");
						try {
							Thread.sleep(TIME_OUT_WAIT_SEAL);
						} catch (InterruptedException e) {
							logger.error("Error sleep to wait SEAL info: " + e);
						}

					}
				}

				if (cont1 && cont2 && wgtQualified) {
					// Send request make gate order directly
					mqttService.sendProgressToGate(BusinessConsts.DATA_QUALIFIED, BusinessConsts.BLANK,
							"Xác nhận data chính xác, trực tiếp làm lênh.", detectionInfo.getGateNo());
				}
			}
		}.start();
	}

	/**
	 * Get current weight in cache mapping with gate id
	 * 
	 * @param gateId
	 * @return
	 */
	private Integer getCurrentWgt(String gateId) {
		Object wgtObj = CacheUtils.get("wgt_" + gateId);
		if (wgtObj == null) {
			return 0;
		}
		Integer wgt = (Integer) wgtObj;
		return wgt;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Get list container info from catos by string containers (container no
	 * separated by comma)
	 * 
	 * @param gateDetection
	 * @return
	 */
	private Map<String, ContainerInfoDto> getCntrMapFromCatos(GateDetection gateDetection) {
		String containers = "";
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
			containers += gateDetection.getContainerNo1();
		}
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
			containers += "," + gateDetection.getContainerNo2();
		}
		Map<String, ContainerInfoDto> cntrMap = new HashMap<>();
		if (StringUtils.isNotEmpty(containers)) {
			List<ContainerInfoDto> cntrInfoDtos = catosApiService.getContainerInfoReserve(containers);
			if (CollectionUtils.isNotEmpty(cntrInfoDtos)) {
				for (ContainerInfoDto cntr : cntrInfoDtos) {
					if (!EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntr.getCntrState())
							&& !EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(cntr.getCntrState())) {
						cntrMap.put(cntr.getCntrNo(), cntr);
					}
				}
			}
		}
		return cntrMap;
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
