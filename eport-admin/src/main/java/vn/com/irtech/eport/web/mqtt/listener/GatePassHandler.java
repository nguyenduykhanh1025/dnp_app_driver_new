package vn.com.irtech.eport.web.mqtt.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.SendResponse;
import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.firebase.service.FirebaseService;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.system.service.ISysUserTokenService;
import vn.com.irtech.eport.web.dto.DriverDataRes;
import vn.com.irtech.eport.web.dto.DriverRes;
import vn.com.irtech.eport.web.dto.GateInFormData;
import vn.com.irtech.eport.web.mqtt.BusinessConsts;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Component
public class GatePassHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(GatePassHandler.class);
	
	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IProcessHistoryService processHistoryService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IDriverAccountService driverAccountService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private FirebaseService firebaseService;

	@Autowired
	private ISysUserTokenService sysUserTokenService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;
	
	public static String DRIVER_RES_TOPIC = "eport/driver/+/response";

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				processMessage(topic, message);
			}
		});
	}
	
	private void processMessage(String topic, MqttMessage message) {
//		logger.info("Receive message subject : " + topic);
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
		String pickupOutResult = map.get("pickupOutResult") == null ? null : new Gson().toJson(map.get("pickupOutResult"));
		
		GateInFormData gateInFormData = new Gson().fromJson(dataString, GateInFormData.class);
		if (gateInFormData != null && gateInFormData.getReceiptId() != null) {
			robotService.updateRobotStatusByUuId(uuId, status);
			this.updateHistory(gateInFormData, pickupOutResult, result, uuId, status);
		}
	}
	
	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	private void updateHistory(GateInFormData gateInFormData, String pickupOutResult, String result, String uuId,
			String status) {
		List<DriverDataRes> driverDataRes = new ArrayList<>();
		DriverRes driverRes = new DriverRes();
		// Declare process order to update status
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(gateInFormData.getReceiptId());
		processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);

		// Declare process history to save history
		ProcessHistory history = new ProcessHistory();
		history.setProcessOrderId(gateInFormData.getReceiptId());
		history.setRobotUuid(uuId);
		history.setServiceType(EportConstants.SERVICE_GATE_OUT);

		// Find history record when start time of this process order is set to update
		// finish time
		List<ProcessHistory> processHistories = processHistoryService.selectProcessHistoryList(history);
		if (CollectionUtils.isNotEmpty(processHistories)) {
			history.setId(processHistories.get(0).getId());
			history.setFinishTime(new Date());
		}
		history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_FINISHED);

		// If Robot return success
		if ("success".equalsIgnoreCase(result)) {
			try {
				// Update pickup history and send result to driver if exist
				updateResultForDriver(gateInFormData);

				// Send result to gate app
				if (CollectionUtils.isNotEmpty(gateInFormData.getPickupOut())) {
					// Iterate pickup out list
					for (PickupHistory pickupHistory : gateInFormData.getPickupOut()) {
						if (pickupOutResult != null) {
							// Data to send response to driver app when having session id
							DriverDataRes driverData = new DriverDataRes();
							driverData.setPickupHistoryId(pickupHistory.getId());
							driverData.setContainerNo(pickupHistory.getContainerNo());
							driverData.setYardPosition(pickupHistory.getBlock() + "-" + pickupHistory.getBay() + "-"
									+ pickupHistory.getLine() + "-" + pickupHistory.getTier());
							if (pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY
									|| pickupHistory.getShipment()
											.getServiceType() == EportConstants.SERVICE_DROP_EMPTY) {
								driverData.setFe("E");
							} else {
								driverData.setFe("F");
							}
							driverData.setSztp(pickupHistory.getSztp());
							driverData.setTruckNo(pickupHistory.getTruckNo());
							driverData.setChassisNo(pickupHistory.getChassisNo());
							driverData.setWgt(gateInFormData.getWgt());
							driverDataRes.add(driverData);
						}
					}
				}

				// Send result gate in to driver
				try {
					driverRes.setData(driverDataRes);
					driverRes.setResult(BusinessConsts.PASS);
					if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
						responseDriver(driverRes, gateInFormData.getSessionId());
					}
				} catch (Exception e) {
					logger.error("Error send result gate in for driver: " + e);
				}

			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}

			history.setResult("S");
			processOrder.setResult("S");

		} else {
			try {

			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}

			processOrder.setResult("F");
			history.setResult("F");
		}

		// Update process order (S or F)
		processOrderService.updateProcessOrder(processOrder);

		driverRes.setStatus(BusinessConsts.FINISH);
		driverRes.setResult(BusinessConsts.FAIL);
		driverRes.setMsg("Có lỗi xảy ra trong quá trình gate in. Vui lòng thử lại hoặc vào bàn cân để được hỗ trợ.");
		try {
			if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
				responseDriver(driverRes, gateInFormData.getSessionId());
			}
		} catch (Exception e) {
			logger.error("Error send result gate in for driver: " + e);
		}

		// Update if history has id (possible to update finish time) History robot
		// Insert when history doesn't have id (that's mean something went wrong and
		// couldn't get the id
		if (history.getId() == null) {
			processHistoryService.insertProcessHistory(history);
		} else {
			processHistoryService.updateProcessHistory(history);
		}

	}

	private void updateResultForDriver(GateInFormData gateInFormData) {
		String containerNos = "";
		for (PickupHistory pickupHistory : gateInFormData.getPickupOut()) {
			containerNos += pickupHistory.getContainerNo() + ",";
		}
		if (StringUtils.isNotEmpty(containerNos)) {
			containerNos.substring(0, containerNos.length() - 1);
			PickupHistory pickupHistoryParam = new PickupHistory();
			pickupHistoryParam.setTruckNo(gateInFormData.getTruckNo().replace(".", ""));
			pickupHistoryParam.setChassisNo(gateInFormData.getChassisNo());
			pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
			Map<String, Object> params = new HashMap<>();
			String serviceTypes = EportConstants.SERVICE_PICKUP_EMPTY + "," + EportConstants.SERVICE_PICKUP_FULL;
			params.put("serviceTypes", Convert.toStrArray(serviceTypes));
			params.put("containerNos", Convert.toStrArray(containerNos));
			pickupHistoryParam.setParams(params);
			List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
			Long driverId = null;
			String content = "(Danang port) Toa do boc container: ";
			if (CollectionUtils.isNotEmpty(pickupHistories)) {
				for (PickupHistory pickupHistory : pickupHistories) {
					// Get driver id
					driverId = pickupHistory.getDriverId();
					content += pickupHistory.getContainerNo() + " " + pickupHistory.getBlock() + "-"
							+ pickupHistory.getBay() + "-" + pickupHistory.getLine() + "-" + pickupHistory.getTier()
							+ " ";
					pickupHistory.setStatus(EportConstants.PICKUP_HISTORY_STATUS_GATE_IN);
					pickupHistory.setGateinDate(new Date());
					pickupHistoryService.updatePickupHistory(pickupHistory);
				}
				if (driverId != null) {
					// Send sms yard position result to driver
					DriverAccount driverAccount = driverAccountService.selectDriverAccountById(driverId);
					try {
						otpCodeService.postOtpMessage(driverAccount.getMobileNumber(), content);
					} catch (IOException e) {
						logger.error("Error when send yard position result to driver: " + e);
					}

					// Send firebase
					List<String> sysUserTokens = sysUserTokenService.getListDeviceTokenByUserId(driverId);
					if (CollectionUtils.isNotEmpty(sysUserTokens)) {
						try {
							BatchResponse response = firebaseService.sendNotification(
									"Kết quả tọa độ bốc container xuống bãi cảng", content, sysUserTokens);
							if (response.getFailureCount() > 0) {
								List<SendResponse> responses = response.getResponses();
								List<String> failedTokens = new ArrayList<>();
								for (int i = 0; i < responses.size(); i++) {
									if (!responses.get(i).isSuccessful()) {
										// The order of responses corresponds to the order of the registration
										// tokens.
										failedTokens.add(sysUserTokens.get(i));
									}
								}

								logger.error("List of tokens that caused failures: " + failedTokens);
							}
						} catch (FirebaseMessagingException e) {
							logger.error("Error send notification: " + e);
						}
					}
				}
			}
		}
	}

	/**
	 * Response check in result to driver
	 * 
	 * @param driverRes
	 * @param sessionId
	 * @throws Exception
	 */
	private void responseDriver(DriverRes driverRes, String sessionId) throws Exception {
		String msg = new Gson().toJson(driverRes);
		mqttService.publish(DRIVER_RES_TOPIC.replace("+", sessionId), new MqttMessage(msg.getBytes()));
	}
}
