package vn.com.irtech.eport.web.mqtt.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

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
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.web.dto.GateInFormData;
import vn.com.irtech.eport.web.mqtt.BusinessConsts;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Component
public class AutoGatePassHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(AutoGatePassHandler.class);

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IGateDetectionService gateDetectionService;
	
	@Autowired
	private IProcessHistoryService processHistoryService;
	
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

		GateInFormData gateInFormData = new Gson().fromJson(dataString, GateInFormData.class);
		if (gateInFormData != null && gateInFormData.getReceiptId() != null) {
			robotService.updateRobotStatusByUuId(uuId, status);
		}
		this.updateHistory(gateInFormData, result, uuId, status);
	}
	
	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	private void updateHistory(GateInFormData gateInFormData, String result, String uuId, String status) {
		// Declare process order to update status
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(gateInFormData.getReceiptId());
		processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);
		
		// Declare process history to save history
		ProcessHistory history = new ProcessHistory();
		history.setProcessOrderId(gateInFormData.getReceiptId());
		history.setRobotUuid(uuId);
		history.setServiceType(EportConstants.SERVICE_GATE_IN);
		
		// Find history record when start time of this process order is set to update finish time
		List<ProcessHistory> processHistories = processHistoryService.selectProcessHistoryList(history);
		if (CollectionUtils.isNotEmpty(processHistories)) {
			history.setId(processHistories.get(0).getId());
			history.setFinishTime(new Date());
		}
		history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_FINISHED);
		
		// If Robot return success
		if ("success".equalsIgnoreCase(result)) {
			try {
				// Send result to gate app
				String message = "Làm lệnh vào cổng thành công.";
				mqttService.sendProgressToGate(BusinessConsts.FINISH, BusinessConsts.SUCCESS, message,
						gateInFormData.getGateId());
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}
			
			history.setResult("S");
			processOrder.setResult("S");
		
		} else if("position_failed".equalsIgnoreCase(result)) {
			if (gateInFormData.getId() != 0) {
				GateDetection gateDetection = gateDetectionService.selectGateDetectionById(gateInFormData.getId());

				String message = "Chưa có tọa độ, đang thực hiện yêu cầu mc cấp tọa độ.";
				mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
						gateInFormData.getGateId());

				sendRequestToMc(gateDetection);

				for (int i = 1; i <= RETRY_WAIT_MC; i++) {
					logger.debug("Wait " + TIME_OUT_WAIT_MC + " miliseconds");
					try {
						Thread.sleep(TIME_OUT_WAIT_MC);
					} catch (InterruptedException e) {
						logger.error("Error sleep to wait mc: " + e);
					}
					logger.debug("Check db for position");
					gateDetection = gateDetectionService.selectGateDetectionById(gateInFormData.getId());
					if (hasLocation(gateDetection)) {
						break;
					}
				}
				if (hasLocation(gateDetection)) {
					message = "Đã có tọa độ đầy đủ, tiếp tục làm lệnh gate in";
					mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
							gateInFormData.getGateId());
					sendGateInOrderToRobot(gateDetection);
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
		// Insert when history doesn't have id (that's mean something went wrong and couldn't get the id
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
	private void sendGateInOrderToRobot(GateDetection gateDetection) {
		try {
			GateInFormData gateInFormData = new GateInFormData();
			List<PickupHistory> pickupIn = new ArrayList<>();

			// Container 1
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setContainerNo(gateDetection.getContainerNo1());

				// Check location
				if (StringUtils.isNotEmpty(gateDetection.getLocation1())) {
					String[] location1 = gateDetection.getLocation1().split("-");
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
				}
				pickupIn.add(pickupHistory);
			}

			// Container 2
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setContainerNo(gateDetection.getContainerNo2());

				// Check location
				if (StringUtils.isNotEmpty(gateDetection.getLocation2())) {
					String[] location2 = gateDetection.getLocation2().split("-");
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

	private void sendRequestToMc(GateDetection gateDetection) {

	}
}