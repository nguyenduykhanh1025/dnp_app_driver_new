package vn.com.irtech.eport.logistic.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class MqttService implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	private static final String BASE = "eport/robot";
	private static final String RESPONSE_TOPIC = BASE + "/+/response";
	private static final String REQUEST_TOPIC = BASE + "/+/request";
	private static final String NOTIFICATION_OM_TOPIC = "eport/notification/om";
	private static final String NOTIFICATION_IT_TOPIC = "eport/notification/it";
	private static final String NOTIFICATION_CONT_TOPIC = "eport/notification/cont";
	private static final String NOTIFICATION_MC_TOPIC = "eport/notification/mc";
	private static final String NOTIFICATION_CFS_TOPIC = "eport/notification/cfs";
	private static final String NOTIFICATION_GATE_TOPIC = "eport/notification/gate";

	@Autowired
	private MqttAsyncClient mqttClient;
	@Autowired
	private MqttConfig config;
	private Object connectLock = new Object();

	@Autowired
	private ISysRobotService robotService;
	@Autowired
	private IProcessOrderService processOrderService;
	@Autowired
	private RobotResponseHandler robotResponseHandler;
	@Autowired
	private RobotUpdateStatusHandler robotUpdateStatusHandler;

	@PostConstruct
	public void connect() throws MqttException {
		// set callback
		mqttClient.setCallback(this);
		MqttConnectOptions clientOptions = new MqttConnectOptions();
		clientOptions.setCleanSession(true);
		clientOptions.setMaxInflight(config.getMaxInFlight()); // default is 10
		if (StringUtils.isNotBlank(config.getUsername())) {
			clientOptions.setUserName(config.getUsername());
		}
		if (StringUtils.isNotBlank(config.getPassword())) {
			clientOptions.setPassword(config.getPassword().toCharArray());
		}
		// checkConnection
		if (!mqttClient.isConnected()) {
			synchronized (connectLock) {
				while (!mqttClient.isConnected()) {
					logger.debug("[{}] MQTT broker connection attempt!", mqttClient.getServerURI());
					try {
						mqttClient.connect(clientOptions, null, new IMqttActionListener() {
							@Override
							public void onSuccess(IMqttToken iMqttToken) {
								logger.info("[{}] MQTT broker connection established!", mqttClient.getServerURI());
								try {
									subscribeToTopics();
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(IMqttToken iMqttToken, Throwable e) {
								logger.warn("[{}] MQTT broker connection faied! {}", mqttClient.getServerURI(),
										e.getMessage(), e);
							}
						}).waitForCompletion();
					} catch (MqttException e) {
						e.printStackTrace();
						logger.info("MQTT broker connection failed!" + e.getMessage());
						if (!mqttClient.isConnected()) {
							try {
								Thread.sleep(3000); // 3s
							} catch (InterruptedException e1) {
								logger.warn(e.getMessage());
							}
						}
					}
				}
			}

		}
	}

	private void subscribeToTopics() throws MqttException {
		List<IMqttToken> tokens = new ArrayList<>();
		// subscribe default topics when connect
		logger.debug("Subsribe Topic: " + BASE);
		tokens.add(mqttClient.subscribe(BASE, 1, robotUpdateStatusHandler));
		logger.debug("Subsribe Topic: " + RESPONSE_TOPIC);
		tokens.add(mqttClient.subscribe(RESPONSE_TOPIC, 1, robotResponseHandler));
		// Wait for subscribe complete
		for (IMqttToken token : tokens) {
			token.waitForCompletion();
		}
	}

	@PreDestroy
	public void disconnect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				IMqttToken token = mqttClient.disconnect();
				token.waitForCompletion(5000); // 5s
				mqttClient.close();
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.warn("Lost connection to MQTT server", cause);
		while (true) {
			try {
				logger.info("Attempting to reconnect to MQTT server");
				reconnect();
				logger.info("Reconnected to MQTT server, resuming");
				return;
			} catch (MqttException e) {
				logger.warn("Reconnect failed, retrying in 3 seconds", e);
			}
			try {
				Thread.sleep(3000); // wait 3s before reconnect
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Logistic Message arrived: " + topic + "," + message.getPayload().toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("Published message!");

	}

	private void reconnect() throws MqttException {
		if (!mqttClient.isConnected()) {
			connect();
		}
	}

	public void publish(String topic, MqttMessage msg) throws MqttException {
		logger.debug("Send message to MQ, topic: " + topic);
		try {
			mqttClient.publish(topic, msg, null, new IMqttActionListener() {
				@Override
				public void onSuccess(IMqttToken iMqttToken) {
				}

				@Override
				public void onFailure(IMqttToken iMqttToken, Throwable e) {
					logger.debug("Failed to publish message to MQ", e);
				}
			});
		} catch (MqttException e) {
			logger.error("Error while publish message to MQ", e);
			throw e;
		}
	}

	public enum EServiceRobot {
		RECEIVE_CONT_FULL, // Boc hang
		RECEIVE_CONT_EMPTY, // Boc rong
		SEND_CONT_FULL, // Ha hang
		SEND_CONT_EMPTY, // Ha rong
		SHIFTING_CONT, // Dich chuyen
		CHANGE_VESSEL, // Doi tau chuyen
		CREATE_BOOKING, // Tao booking
		EXTENSION_DATE, // Gia han lenh
		GATE_IN, // Gate in
		TERMINAL_CUSTOM_HOLD, // Terminal custom hold
		CANCEL_DROP_FULL, // Cancel drop full
		CANCEL_PICKUP_EMPTY, // Cancel pickup empty
		EXPORT_RECEIPT, // Export receipt
		EXTENSION_DET, // update detention days
		OVERSIZE_REMARK // update remark for oversize container
	}

	@Transactional
	public boolean publishMessageToRobot(ServiceSendFullRobotReq payLoad, EServiceRobot serviceRobot)
			throws MqttException {
		SysRobot sysRobot = this.getAvailableRobot(serviceRobot);
		if (sysRobot == null) {
			return false;
		}
		String msg = new Gson().toJson(payLoad);
		String topic = REQUEST_TOPIC.replace("+", sysRobot.getUuId());
		publish(topic, new MqttMessage(msg.getBytes()));
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setId(payLoad.processOrder.getId());
		processOrder.setRobotUuid(sysRobot.getUuId()); // robot uuid in charge of process order
		processOrder.setStatus(1); // on progress
		// TODO
		if (serviceRobot.equals(EServiceRobot.CHANGE_VESSEL) || serviceRobot.equals(EServiceRobot.EXTENSION_DATE)) {
			processOrder.setRunnable(false);
		}
		processOrderService.updateProcessOrder(processOrder);
		robotService.updateRobotStatusByUuId(sysRobot.getUuId(), "1");
		return true;
	}

	@Transactional
	public boolean publishBookingOrderToRobot(ProcessOrder payLoad, EServiceRobot serviceRobot) throws MqttException {
		SysRobot sysRobot = this.getAvailableRobot(serviceRobot);
		if (sysRobot == null) {
			return false;
		}
		String msg = new Gson().toJson(payLoad);
		String topic = REQUEST_TOPIC.replace("+", sysRobot.getUuId());
		publish(topic, new MqttMessage(msg.getBytes()));
		payLoad.setRobotUuid(sysRobot.getUuId()); // robot uuid in charge of process order
		payLoad.setStatus(1); // on progress
		processOrderService.updateProcessOrder(payLoad);
		robotService.updateRobotStatusByUuId(sysRobot.getUuId(), "1");
		return true;
	}

	/**
	 * 
	 * @param payLoad
	 * @param serviceRobot
	 * @param uuid
	 * @throws MqttException
	 */
	public void publicMessageToDemandRobot(ServiceSendFullRobotReq payLoad, EServiceRobot serviceRobot, String uuid)
			throws MqttException {
		String msg = new Gson().toJson(payLoad);
		String topic = REQUEST_TOPIC.replace("+", uuid);
		publish(topic, new MqttMessage(msg.getBytes()));
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setId(payLoad.processOrder.getId());
		processOrder.setRobotUuid(uuid); // robot uuid in charge of process order
		processOrder.setStatus(1); // on progress
		if (serviceRobot.equals(EServiceRobot.CHANGE_VESSEL) || serviceRobot.equals(EServiceRobot.EXTENSION_DATE)) {
			processOrder.setRunnable(false);
		}
		processOrderService.updateProcessOrder(processOrder);
	}

	public void publicOrderToDemandRobot(ProcessOrder payLoad, EServiceRobot serviceRobot, String uuid)
			throws MqttException {
		SysRobot sysRobot = new SysRobot();
		sysRobot.setUuId(uuid);
		sysRobot.setStatus(EportConstants.ROBOT_STATUS_BUSY);
		robotService.updateRobotByUuId(sysRobot);
		String msg = new Gson().toJson(payLoad);
		String topic = REQUEST_TOPIC.replace("+", uuid);
		publish(topic, new MqttMessage(msg.getBytes()));
		payLoad.setRobotUuid(uuid); // robot uuid in charge of process order
		payLoad.setStatus(1); // on progress
		processOrderService.updateProcessOrder(payLoad);
	}

	/**
	 * Get a robot already to execute service
	 * 
	 * @param service
	 * @return
	 */
	public SysRobot getAvailableRobot(EServiceRobot serviceRobot) {
		SysRobot sysRobot = new SysRobot();
		sysRobot.setStatus("0");
		switch (serviceRobot) {
		case RECEIVE_CONT_FULL:
			sysRobot.setIsReceiveContFullOrder(true);
			break;
		case RECEIVE_CONT_EMPTY:
			sysRobot.setIsReceiveContEmptyOrder(true);
			break;
		case SEND_CONT_FULL:
			sysRobot.setIsSendContFullOrder(true);
			break;
		case SEND_CONT_EMPTY:
			sysRobot.setIsSendContEmptyOrder(true);
			break;
		case SHIFTING_CONT:
			sysRobot.setIsShiftingContOrder(true);
			break;
		case CHANGE_VESSEL:
			sysRobot.setIsChangeVesselOrder(true);
			break;
		case CREATE_BOOKING:
			sysRobot.setIsCreateBookingOrder(true);
			break;
		case EXTENSION_DATE:
			sysRobot.setIsExtensionDateOrder(true);
			break;
		case TERMINAL_CUSTOM_HOLD:
			sysRobot.setIsChangeTerminalCustomHold(true);
			break;
		case CANCEL_DROP_FULL:
			sysRobot.setIsCancelSendContFullOrder(true);
			break;
		case CANCEL_PICKUP_EMPTY:
			sysRobot.setIsCancelReceiveContEmptyOrder(true);
			break;
		case EXPORT_RECEIPT:
			sysRobot.setIsExportReceipt(true);
			break;
		case EXTENSION_DET:
			sysRobot.setIsExtensionDetOrder(true);
			break;
		case OVERSIZE_REMARK:
			sysRobot.setIsOverSizeRemarkOrder(true);
		}
		sysRobot.setDisabled(false);
		return robotService.findFirstRobot(sysRobot);
	}

	public enum NotificationCode {
		NOTIFICATION_OM, // OM ho tro
		NOTIFICATION_OM_CUSTOM, // OM ho tro khach hang
		NOTIFICATION_IT, // IT ho tro robot
		NOTIFICATION_CONT, // Notify boc cont
		NOTIFICATION_CFS // CFS
	}

	/**
	 * 
	 * @param code
	 * @param content
	 * @param url
	 * @throws MqttException
	 */
	public void sendNotification(NotificationCode code, String content, String url) throws MqttException {
		String title = "", topic = "";
		switch (code) {
		case NOTIFICATION_OM:
			topic = NOTIFICATION_OM_TOPIC;
			title = "Lỗi làm lệnh!";
			break;
		case NOTIFICATION_OM_CUSTOM:
			topic = NOTIFICATION_OM_TOPIC;
			title = "Lỗi hải quan!";
			break;
		case NOTIFICATION_IT:
			topic = NOTIFICATION_IT_TOPIC;
			title = "Lỗi robot!";
			break;
		case NOTIFICATION_CONT:
			topic = NOTIFICATION_CONT_TOPIC;
			title = "Cần cấp cont!";
			break;
		case NOTIFICATION_CFS:
			topic = NOTIFICATION_CFS_TOPIC;
			title = "Hỗ trợ CFS!";
			break;
		}
		Map<String, String> data = new HashMap<>();
		data.put("title", title);
		data.put("msg", content);
		data.put("link", url);
		String msg = new Gson().toJson(data);
		publish(topic, new MqttMessage(msg.getBytes()));
	}

	public void sendNotificationApp(NotificationCode code, String title, String content, String url, Integer priority)
			throws MqttException {
		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setTitle(title);
		notificationReq.setMsg(content);
		notificationReq.setLink(url);
		notificationReq.setPriority(priority);
		String topic = "";
		switch (code) {
		case NOTIFICATION_OM:
			notificationReq.setType(EportConstants.APP_USER_TYPE_OM);
			topic = NOTIFICATION_OM_TOPIC;
			break;
		case NOTIFICATION_OM_CUSTOM:
			notificationReq.setType(EportConstants.APP_USER_TYPE_OM);
			topic = NOTIFICATION_OM_TOPIC;
			break;
		case NOTIFICATION_IT:
			notificationReq.setType(EportConstants.APP_USER_TYPE_IT);
			topic = NOTIFICATION_IT_TOPIC;
			break;
		case NOTIFICATION_CONT:
			notificationReq.setType(EportConstants.APP_USER_TYPE_CONT);
			topic = NOTIFICATION_CONT_TOPIC;
			break;
		case NOTIFICATION_CFS:
			notificationReq.setType(EportConstants.APP_USER_TYPE_CFS);
			topic = NOTIFICATION_CFS_TOPIC;
			break;
		}
		String msg = new Gson().toJson(notificationReq);
		publish(topic, new MqttMessage(msg.getBytes()));
	}
}