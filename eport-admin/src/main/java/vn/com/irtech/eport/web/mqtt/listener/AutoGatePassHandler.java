package vn.com.irtech.eport.web.mqtt.listener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.web.dto.GateInFormData;
import vn.com.irtech.eport.web.dto.PickupRobotResult;
import vn.com.irtech.eport.web.mqtt.BusinessConsts;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Component
public class AutoGatePassHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(AutoGatePassHandler.class);

	public static final String NOTIFICATION_MC_TOPIC = "eport/notification/mc";

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IGateDetectionService gateDetectionService;

	@Autowired
	private IProcessHistoryService processHistoryService;

	@Autowired
	private ISysConfigService sysConfigService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	// time wait mc input postion
	private static final Long TIME_OUT_WAIT_MC = 2000L;
	private static final Integer RETRY_WAIT_MC = 60;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}

	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload());
		logger.info("Receive message topic: {}, content : {}", topic, messageContent);
		Map<String, Object> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		String uuId = map.get("id") == null ? null : map.get("id").toString();
		if (uuId == null) {
			return;
		}

		SysRobot sysRobot = robotService.selectRobotByUuId(uuId);

		if (sysRobot == null) {
			return;
		}

		String status = map.get("status") == null ? null : map.get("status").toString();
		String result = map.get("result") == null ? null : map.get("result").toString();
		String dataString = map.get("data") == null ? null : new Gson().toJson(map.get("data"));
		String pickupInResult = map.get("pickupInResult") == null ? null : new Gson().toJson(map.get("pickupInResult"));

		GateInFormData gateInFormData = new Gson().fromJson(dataString, GateInFormData.class);
		if (gateInFormData != null && gateInFormData.getReceiptId() != null) {
			robotService.updateRobotStatusByUuId(uuId, status);
		}
		this.updateHistory(gateInFormData, pickupInResult, result, uuId, status);
	}

	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	private void updateHistory(GateInFormData gateInFormData, String pickupInResult, String result, String uuId,
			String status) {
		// Declare process order to update status
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(gateInFormData.getReceiptId());
		processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);

		// Declare process history to save history
		ProcessHistory history = new ProcessHistory();
		history.setProcessOrderId(gateInFormData.getReceiptId());
		history.setRobotUuid(uuId);
		history.setServiceType(EportConstants.SERVICE_GATE_IN);

		// Find history record when start time of this process order is set to update
		// finish time
		List<ProcessHistory> processHistories = processHistoryService.selectProcessHistoryList(history);
		if (CollectionUtils.isNotEmpty(processHistories)) {
			history.setId(processHistories.get(0).getId());
			history.setFinishTime(new Date());
		}
		history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_FINISHED);

		GateDetection gateDetection = gateDetectionService.selectGateDetectionById(gateInFormData.getId());
		if (gateDetection != null && StringUtils.isNotEmpty(pickupInResult)) {
			List<PickupRobotResult> pickupRobotResults = getListRobotResultPickupFromString(pickupInResult);
			gateDetection = updateYardPositionOfGateDetection(pickupRobotResults, gateDetection);
			gateDetectionService.updateGateDetection(gateDetection);
		}
		// If Robot return success
		if ("success".equalsIgnoreCase(result)) {
			try {
				gateDetection.setStatus("S");
				gateDetectionService.updateGateDetection(gateDetection);

				// Send result to gate app
				String message = "Làm lệnh vào cổng thành công.";
				mqttService.sendProgressToGate(BusinessConsts.FINISH, BusinessConsts.SUCCESS, message,
						gateInFormData.getGateId());
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}

			history.setResult("S");
			processOrder.setResult("S");

		} else if ("position_failed".equalsIgnoreCase(result)) {
			if (gateInFormData.getId() != 0) {
				// The order in catos after register job gate in is reversed
				// => need to reverse data between 2 container to mapping with catos
				gateDetection = reverseData(gateDetection);

				String message = "Chưa có tọa độ, đang thực hiện yêu cầu mc cấp tọa độ.";
				mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
						gateInFormData.getGateId());

				sendRequestToMc(gateDetection);

				// Gate detection reference to check if location is updated by mc
				GateDetection gateDetectionRef = null;

				for (int i = 1; i <= RETRY_WAIT_MC; i++) {
					logger.debug("Wait " + TIME_OUT_WAIT_MC + " miliseconds");
					try {
						Thread.sleep(TIME_OUT_WAIT_MC);
					} catch (InterruptedException e) {
						logger.error("Error sleep to wait mc: " + e);
					}
					logger.debug("Check db for position");
					gateDetectionRef = gateDetectionService.selectGateDetectionById(gateInFormData.getId());
					if (hasLocation(gateDetectionRef)) {
						break;
					}
				}
				if (hasLocation(gateDetectionRef)) {
					message = "Đã có tọa độ đầy đủ, tiếp tục làm lệnh gate in";
					mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
							gateInFormData.getGateId());
					sendGateInOrderToRobot(gateDetection, gateDetectionRef);
				} else {
					try {
						// Send result to gate app
						message = "Làm lệnh thất bại, không lấy được tọa độ.";
						mqttService.sendProgressToGate(BusinessConsts.FINISH, BusinessConsts.FAIL, message,
								gateInFormData.getGateId());
					} catch (Exception e) {
						logger.error("Error send result gate in for smart app: " + e);
					}
				}
			}

			// Set current process order to failed
			processOrder.setResult("M");
			history.setResult("M");
		} else {
			try {
				gateDetection.setStatus("E");
				gateDetectionService.updateGateDetection(gateDetection);

				// Send result to gate app
				String message = "Có lỗi xảy ra khi làm lệnh gate in.";
				mqttService.sendProgressToGate(BusinessConsts.FINISH, BusinessConsts.FAIL, message,
						gateInFormData.getGateId());
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}

			processOrder.setResult("F");
			history.setResult("F");
		}

		// Update process order (S or F)
		processOrderService.updateProcessOrder(processOrder);

		// Update if history has id (possible to update finish time) History robot
		// Insert when history doesn't have id (that's mean something went wrong and
		// couldn't get the id
		if (history.getId() == null) {
			processHistoryService.insertProcessHistory(history);
		} else {
			processHistoryService.updateProcessHistory(history);
		}

	}

	/**
	 * Prepare data to make gate in order, find robot gate in available, send order
	 * to robot
	 * 
	 * @param gateNotificationCheckInReq
	 */
	private void sendGateInOrderToRobot(GateDetection gateDetection, GateDetection gateDetectionRef) {
		try {
			GateInFormData gateInFormData = new GateInFormData();
			// Send gate in req case continue after mc update position
			gateInFormData.setType(EportConstants.GATE_REQ_TYPE_CONTINUE);
			List<PickupHistory> pickupIn = new ArrayList<>();

			// Container 1
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setId(gateDetection.getId() * 10 + 1);
				pickupHistory.setContainerNo(gateDetection.getContainerNo1());

				// Check location
				if (StringUtils.isNotEmpty(gateDetectionRef.getLocation1())) {
					String[] location1 = gateDetectionRef.getLocation1().split("-");
					if (location1.length >= 4) {
						pickupHistory.setBlock(location1[0]);
						pickupHistory.setLine(location1[2]);
						pickupHistory.setTier(location1[3]);
						Integer bay = Integer.parseInt(location1[1]);
						// Check bay is even then need to add odd number before current bay (02 -> even
						// -> 01/02)
						if (bay % 2 == 0) {
							Integer oddNumber = bay - 1;
							String oddString = "";
							if (oddNumber < 10) {
								oddString = "0" + oddNumber.toString();
							} else {
								oddString = oddNumber.toString();
							}
							pickupHistory.setBay(oddString + "/" + bay);
						} else {
							Integer evenNumber = bay + 1;
							String evenString = "";
							if (evenNumber < 10) {
								evenString = "0" + evenNumber.toString();
							} else {
								evenString = evenNumber.toString();
							}
							pickupHistory.setBay(bay + "/" + evenString);
						}
					} else {
						pickupHistory.setArea(gateDetection.getLocation1());
					}
					// Check if location is updated or already had position
					// If already had => location update is false else true
					if (gateDetectionRef.getLocation1().equalsIgnoreCase(gateDetection.getLocation1())) {
						pickupHistory.setLocationUpdate(false);
					} else {
						pickupHistory.setLocationUpdate(true);
					}
				}
				if (StringUtils.isEmpty(pickupHistory.getBlock())) {
					pickupHistory.setBlock("");
				}
				if (StringUtils.isEmpty(pickupHistory.getArea())) {
					pickupHistory.setArea("");
				}
				pickupIn.add(pickupHistory);
			}

			// Container 2
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setId(gateDetection.getId() * 10 + 2);
				pickupHistory.setContainerNo(gateDetection.getContainerNo2());

				// Check location
				if (StringUtils.isNotEmpty(gateDetectionRef.getLocation2())) {
					String[] location2 = gateDetectionRef.getLocation2().split("-");
					if (location2.length >= 4) {
						pickupHistory.setBlock(location2[0]);
						pickupHistory.setLine(location2[2]);
						pickupHistory.setTier(location2[3]);
						Integer bay = Integer.parseInt(location2[1]);
						// Check bay is even then need to add odd number before current bay (02 -> even
						// -> 01/02)
						if (bay % 2 == 0) {
							Integer oddNumber = bay - 1;
							String oddString = "";
							if (oddNumber < 10) {
								oddString = "0" + oddNumber.toString();
							} else {
								oddString = oddNumber.toString();
							}
							pickupHistory.setBay(oddString + "/" + bay);
						} else {
							Integer evenNumber = bay + 1;
							String evenString = "";
							if (evenNumber < 10) {
								evenString = "0" + evenNumber.toString();
							} else {
								evenString = evenNumber.toString();
							}
							pickupHistory.setBay(bay + "/" + evenString);
						}
					} else {
						pickupHistory.setArea(gateDetection.getLocation1());
					}
					// Check if location is updated or already had position
					// If already had => location update is false else true
					if (gateDetectionRef.getLocation2().equalsIgnoreCase(gateDetection.getLocation2())) {
						pickupHistory.setLocationUpdate(false);
					} else {
						pickupHistory.setLocationUpdate(true);
					}
				}
				if (StringUtils.isEmpty(pickupHistory.getBlock())) {
					pickupHistory.setBlock("");
				}
				if (StringUtils.isEmpty(pickupHistory.getArea())) {
					pickupHistory.setArea("");
				}
				pickupIn.add(pickupHistory);
			}

			if (CollectionUtils.isNotEmpty(pickupIn)) {
				ProcessOrder processOrder = new ProcessOrder();
				processOrder.setServiceType(EportConstants.SERVICE_GATE_IN);
				processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
				processOrderService.insertProcessOrder(processOrder);

				gateInFormData.setId(gateDetection.getId());
				gateInFormData.setPickupIn(pickupIn);
				gateInFormData.setModule("IN");
				gateInFormData.setContNumberIn(pickupIn.size());

				gateInFormData.setGatePass(gateDetection.getGatepass());
				gateInFormData.setTruckNo(gateDetection.getTruckNo());
				gateInFormData.setChassisNo(gateDetection.getChassisNo());
				if (StringUtils.isEmpty(gateInFormData.getChassisNo())) {
					gateInFormData.setChassisNo("");
				}
				Long gross = gateDetection.getTotalWgt() - gateDetection.getDeduct();
				gateInFormData.setWgt(gross.toString());
				gateInFormData.setGateId(gateDetection.getGateNo());
				gateInFormData.setReceiptId(processOrder.getId());

				String msg = new Gson().toJson(gateInFormData);
				SysRobot robot = new SysRobot();
				robot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
				robot.setIsGateInOrder(true);
				robot.setDisabled(false);
				SysRobot sysRobot = robotService.findFirstRobot(robot);
				if (sysRobot != null) {
					processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_PROCESSING);
					processOrder.setRobotUuid(sysRobot.getUuId());
					processOrder.setProcessData(msg);
					processOrderService.updateProcessOrder(processOrder);
					robotService.updateRobotStatusByUuId(sysRobot.getUuId(), EportConstants.ROBOT_STATUS_BUSY);
					logger.debug("Send request to robot: " + sysRobot.getUuId() + ", content: " + msg);
					mqttService.sendMessageToRobot(msg, sysRobot.getUuId());

					String message = "Đang thực hiện làm lệnh gate in";
					mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
							gateInFormData.getGateId());
				} else {
					logger.debug("No GateRobot is available: " + msg);
				}
			}
		} catch (Exception e) {
			logger.error("Error when send order gate in: " + e);
		}
	}

	/**
	 * Check GateDetection obj has location for both container
	 * 
	 * @param gateDetection
	 * @return
	 */
	private Boolean hasLocation(GateDetection gateDetection) {
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())
				&& StringUtils.isEmpty(gateDetection.getLocation1())) {
			return false;
		}
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())
				&& StringUtils.isEmpty(gateDetection.getLocation2())) {
			return false;
		}
		return true;
	}

	/**
	 * Send data to mc
	 * 
	 * @param gateDetection
	 */
	private void sendRequestToMc(GateDetection gateDetection) {
		// Check case 2 container in same vessel and voyage
		boolean sameVessel = false;
		String container1 = gateDetection.getContainerNo1();
		String container2 = gateDetection.getContainerNo2();
		if (StringUtils.isNotEmpty(container1) && StringUtils.isNotEmpty(gateDetection.getLocation1())) {
			// Container 1 has position => no need to send request position to mc
			// set blank to exclude
			container1 = "";
		}
		if (StringUtils.isNotEmpty(container2) && StringUtils.isNotEmpty(gateDetection.getLocation2())) {
			// Container 1 has position => no need to send request position to mc
			// set blank to exclude
			container2 = "";
		}

		if (StringUtils.isNotEmpty(container1) && StringUtils.isNotEmpty(container2)) {
			if (gateDetection.getVslCd1().equalsIgnoreCase(gateDetection.getVslCd2())
					&& gateDetection.getCallSeq1().equalsIgnoreCase(gateDetection.getCallSeq2())) {
				sameVessel = true;
			}
		}
		if (sameVessel) {
			// Send 2 container to mc
			NotificationReq notificationReq = new NotificationReq();
			List<Map<String, Object>> list = new ArrayList<>();
			notificationReq.setTitle("ePort: Yêu cầu cấp tọa độ.");
			notificationReq.setMsg("Có yêu cầu tọa độ cho container: " + gateDetection.getOpeCode1() + " - "
					+ container1 + " - " + container2 + " - " + gateDetection.getSztp1() + " - "
					+ gateDetection.getVslCd1() + gateDetection.getCallSeq1() + " - " + gateDetection.getPod1());
			notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
			notificationReq
					.setLink(sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
			notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
			// Container 1
			Map<String, Object> data = new HashMap<>();
			// To differentiate with another pickup history
			// => Add post fix 1 with container 1
			data.put("id", gateDetection.getId() + "1");
			data.put("opeCode", gateDetection.getOpeCode1());
			data.put("containerNo", container1);
			data.put("wgt", gateDetection.getWgt1());
			data.put("sztp", gateDetection.getSztp2());
			data.put("userVoy", gateDetection.getVslCd1() + gateDetection.getCallSeq1());
			data.put("dischargePort", gateDetection.getPod1());
			data.put("cargoType", gateDetection.getCargoType1());
			data.put("fe", gateDetection.getFe1());
			list.add(data);
			// Container 2
			data = new HashMap<>();
			// To differentiate with another pickup history
			// => Add post fix 1 with container 2
			data.put("id", gateDetection.getId() + "2");
			data.put("opeCode", gateDetection.getOpeCode2());
			data.put("containerNo", container2);
			data.put("wgt", gateDetection.getWgt2());
			data.put("sztp", gateDetection.getWgt2());
			data.put("userVoy", gateDetection.getVslCd2() + gateDetection.getCallSeq2());
			data.put("dischargePort", gateDetection.getPod2());
			data.put("cargoType", gateDetection.getCargoType2());
			data.put("fe", gateDetection.getFe2());
			list.add(data);
			notificationReq.setData(list);
			String req = new Gson().toJson(notificationReq);
			try {
				mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
			} catch (MqttException e) {
				logger.error("Error when send req yard position for mc: " + req);
			}
		} else {
			// Send 1 request each time if exist
			if (StringUtils.isNotEmpty(container1)) {
				NotificationReq notificationReq = new NotificationReq();
				List<Map<String, Object>> list = new ArrayList<>();
				notificationReq.setTitle("ePort: Yêu cầu cấp tọa độ.");
				notificationReq.setMsg("Có yêu cầu tọa độ cho container: " + gateDetection.getOpeCode1() + " - "
						+ container1 + " - " + gateDetection.getSztp1() + " - " + gateDetection.getVslCd1()
						+ gateDetection.getCallSeq1() + " - " + gateDetection.getPod1());
				notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
				notificationReq.setLink(
						sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
				notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
				// Container 1
				Map<String, Object> data = new HashMap<>();
				// To differentiate with another pickup history
				// => Add post fix 1 with container 1
				data.put("id", gateDetection.getId() + "1");
				data.put("opeCode", gateDetection.getOpeCode1());
				data.put("containerNo", container1);
				data.put("wgt", gateDetection.getWgt1());
				data.put("sztp", gateDetection.getSztp1());
				data.put("userVoy", gateDetection.getVslCd1() + gateDetection.getCallSeq1());
				data.put("dischargePort", gateDetection.getPod1());
				data.put("cargoType", gateDetection.getCargoType1());
				data.put("fe", gateDetection.getFe1());
				list.add(data);
				notificationReq.setData(list);
				String req = new Gson().toJson(notificationReq);
				try {
					mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
				} catch (MqttException e) {
					logger.error("Error when send req yard position for mc: " + req);
				}
			}
			if (StringUtils.isNotEmpty(container2)) {
				NotificationReq notificationReq = new NotificationReq();
				List<Map<String, Object>> list = new ArrayList<>();
				notificationReq.setTitle("ePort: Yêu cầu cấp tọa độ.");
				notificationReq.setMsg("Có yêu cầu tọa độ cho container: " + gateDetection.getOpeCode2() + " - "
						+ container2 + " - " + gateDetection.getSztp2() + " - " + gateDetection.getVslCd2()
						+ gateDetection.getCallSeq2() + " - " + gateDetection.getPod2());
				notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
				notificationReq.setLink(
						sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
				notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
				// Container 1
				Map<String, Object> data = new HashMap<>();
				// To differentiate with another pickup history
				// => Add post fix 2 with container 2
				data.put("id", gateDetection.getId() + "2");
				data.put("opeCode", gateDetection.getOpeCode2());
				data.put("containerNo", container2);
				data.put("wgt", gateDetection.getWgt2());
				data.put("sztp", gateDetection.getSztp2());
				data.put("userVoy", gateDetection.getVslCd2() + gateDetection.getCallSeq2());
				data.put("dischargePort", gateDetection.getPod2());
				data.put("cargoType", gateDetection.getCargoType2());
				data.put("fe", gateDetection.getFe2());
				list.add(data);
				notificationReq.setData(list);
				String req = new Gson().toJson(notificationReq);
				try {
					mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
				} catch (MqttException e) {
					logger.error("Error when send req yard position for mc: " + req);
				}
			}
		}
	}

	/**
	 * Parsing json string of robot result data to List robot result object
	 * 
	 * @param pickupResult
	 * @return List robot result object
	 */
	private List<PickupRobotResult> getListRobotResultPickupFromString(String pickupResult) {
		Type listType = new TypeToken<ArrayList<PickupRobotResult>>() {
		}.getType();
		ArrayList<PickupRobotResult> pickupRobotResults = new Gson().fromJson(pickupResult, listType);
		return pickupRobotResults;
	}

	/**
	 * Set yard position (block-bay-row-tier)
	 * 
	 * @param pickupRobotResults
	 * @param gateDetection
	 * @return
	 */
	private GateDetection updateYardPositionOfGateDetection(List<PickupRobotResult> pickupRobotResults,
			GateDetection gateDetection) {
		for (PickupRobotResult pickupRobotResult : pickupRobotResults) {
			// Check if container need update yard position is container 1
			if (pickupRobotResult.getContainerNo().equalsIgnoreCase(gateDetection.getContainerNo1())) {
				gateDetection.setLocation1(pickupRobotResult.getYardPosition());
			} else if (pickupRobotResult.getContainerNo().equalsIgnoreCase(gateDetection.getContainerNo2())) {
				gateDetection.setLocation2(pickupRobotResult.getYardPosition());
			}
		}
		return gateDetection;
	}

	private GateDetection reverseData(GateDetection gateDetection) {
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
			String container1 = gateDetection.getContainerNo1();
			String opeCode1 = gateDetection.getOpeCode1();
			String sztp1 = gateDetection.getSztp1();
			String vslCd1 = gateDetection.getVslCd1();
			String callSeq1 = gateDetection.getCallSeq1();
			String cargoType1 = gateDetection.getCargoType1();
			String pod1 = gateDetection.getPod1();
			Long wgt1 = gateDetection.getWgt1();
			String location1 = gateDetection.getLocation1();
			String location2 = gateDetection.getLocation2();
			String fe1 = gateDetection.getFe1();
			Map<String, Object> params = new HashMap<>();

			gateDetection.setContainerNo1(gateDetection.getContainerNo2());
			gateDetection.setOpeCode1(gateDetection.getOpeCode2());
			gateDetection.setSztp1(gateDetection.getSztp2());
			gateDetection.setVslCd1(gateDetection.getVslCd2());
			gateDetection.setCallSeq1(gateDetection.getCallSeq2());
			gateDetection.setCargoType1(gateDetection.getCargoType2());
			gateDetection.setPod1(gateDetection.getPod2());
			gateDetection.setWgt1(gateDetection.getWgt2());
			if (StringUtils.isEmpty(location2)) {
				params.put("nullLocation1", true);
			}
			gateDetection.setLocation1(gateDetection.getLocation2());
			gateDetection.setFe1(gateDetection.getFe2());

			gateDetection.setContainerNo2(container1);
			gateDetection.setOpeCode2(opeCode1);
			gateDetection.setSztp2(sztp1);
			gateDetection.setVslCd2(vslCd1);
			gateDetection.setCallSeq2(callSeq1);
			gateDetection.setCargoType2(cargoType1);
			gateDetection.setPod2(pod1);
			gateDetection.setWgt2(wgt1);
			if (StringUtils.isEmpty(location1)) {
				params.put("nullLocation2", true);
			}
			gateDetection.setLocation2(location1);
			gateDetection.setFe2(fe1);

			gateDetection.setParams(params);
			gateDetectionService.updateGateDetection(gateDetection);
		}
		return gateDetection;
	}
}