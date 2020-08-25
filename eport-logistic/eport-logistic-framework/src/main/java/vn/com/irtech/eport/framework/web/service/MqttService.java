package vn.com.irtech.eport.framework.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.framework.mqtt.listener.RobotResponseHandler;
import vn.com.irtech.eport.framework.mqtt.listener.RobotUpdateStatusHandler;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.dto.ServiceRobotReq;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
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

	private static MqttService instance = null;
	private MqttAsyncClient mqttClient;
	private Object connectLock = new Object();
	
	// connection info
	private String host;
	private String username;
	private String password;

	@Autowired
	private RobotUpdateStatusHandler robotUpdateStatusHandler;

	@Autowired
	private RobotResponseHandler robotResponseHandler;

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IProcessOrderService processOrderService;

	private Boolean isReconnecting = false;

	public static MqttService getInstance() {
		if (instance == null) {
			instance = new MqttService();
		}
		return instance;
	}

	public MqttService() {
	}

	public MqttAsyncClient getMqttClient() {
		return mqttClient;
	}

	public boolean isConnected() {
		return mqttClient != null && mqttClient.isConnected();
	}

	/**
	 * Connect to MQTT Server
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @throws MqttException
	 */
	public void connect(String host, String username, String password) throws MqttException {
		this.host = host;
		this.username = username;
		this.password = password;
		
		if (mqttClient == null) {
			mqttClient = newMqttClient(host);
		}
		// set callback
		mqttClient.setCallback(this);

		MqttConnectOptions clientOptions = new MqttConnectOptions();
		clientOptions.setCleanSession(true);

		// checkConnection
		if (!mqttClient.isConnected()) {
			synchronized (connectLock) {
				while (!mqttClient.isConnected()) {
					logger.info("Attent connect mqtt");
					try {
						mqttClient.connect(clientOptions, null, new IMqttActionListener() {
							@Override
							public void onSuccess(IMqttToken iMqttToken) {
								logger.info("MQTT broker connection established!");
								try {
									subscribeToTopics();
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(IMqttToken iMqttToken, Throwable e) {
								logger.info("MQTT broker connection faied!" + e.getMessage());
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
		System.out.println("Subscribe to topic: " + BASE);
		tokens.add(mqttClient.subscribe(BASE, 0, robotUpdateStatusHandler));
		System.out.println("Subscribe to topic: " + RESPONSE_TOPIC);
		tokens.add(mqttClient.subscribe(RESPONSE_TOPIC, 0, robotResponseHandler));
		// Wait for subscribe complete
		for (IMqttToken token : tokens) {
			token.waitForCompletion();
		}
		System.out.println("Subscribe topic completed!");
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

	private MqttAsyncClient newMqttClient(String host) throws MqttException {
		return new MqttAsyncClient(host, this.getComputerName(), new MemoryPersistence());
	}

	public String getComputerName() {
		Map<String, String> env = System.getenv();
		if (env.containsKey("COMPUTERNAME"))
			return "Logistic-" + env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return "Logistic-" + env.get("HOSTNAME");
		else
			return "Logistic-Unknown Computer";
	}

	@Override
	public void connectionLost(Throwable cause) {
		while (true) {
			logger.info("Retry connect to Mqtt");
			try {
				if (!isReconnecting) {
					this.isReconnecting = true;
					reconnect();
				}
				return;
			} catch (MqttException e) {
				e.printStackTrace();
				logger.warn(e.getMessage());
			}
			try {
				Thread.sleep(3000); // wait 3s before reconnect
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.warn(e.getMessage());
			}
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Message arrived: " + topic + "," + message.getPayload().toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("Published message!");

	}

	private void reconnect() throws MqttException {
		if (!mqttClient.isConnected()) {
			this.connect(host, username, password);
//			IMqttToken token = mqttClient.connect();
//			token.waitForCompletion();
//			subscribeToTopics();
			isReconnecting = false;
		}
	}

	public void publish(String topic, MqttMessage msg) throws MqttException {
		logger.info("Send topic: " + topic);
		try {
			mqttClient.publish(topic, msg, null, new IMqttActionListener() {
				@Override
				public void onSuccess(IMqttToken iMqttToken) {
				}

				@Override
				public void onFailure(IMqttToken iMqttToken, Throwable e) {
				}
			});
		} catch (MqttException e) {
			logger.warn(e.getMessage());
			throw e;
		}
	}

	public enum EServiceRobot {
		RECEIVE_CONT_FULL, RECEIVE_CONT_EMPTY, SEND_CONT_FULL, SEND_CONT_EMPTY, SHIFTING_CONT, CHANGE_VESSEL, CREATE_BOOKING, EXTENSION_DATE, GATE_IN
	}

	@Transactional
	public boolean publishMessageToRobot(ServiceSendFullRobotReq payLoad, EServiceRobot serviceRobot) throws MqttException {
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
		if (serviceRobot.equals(EServiceRobot.CHANGE_VESSEL) || serviceRobot.equals(EServiceRobot.EXTENSION_DATE)) {
			processOrder.setRunnable(false);
		}
		processOrderService.updateProcessOrder(processOrder);
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
	public void publicMessageToDemandRobot(ServiceSendFullRobotReq payLoad, EServiceRobot serviceRobot, String uuid) throws MqttException {
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
		}
		return robotService.findFirstRobot(sysRobot);
	}

	public enum NotificationCode {
		NOTIFICATION_OM, NOTIFICATION_OM_CUSTOM, NOTIFICATION_IT, NOTIFICATION_CONT
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
		}
		Map<String, String> data = new HashMap<>();
		data.put("title", title);
		data.put("msg", content);
		data.put("link", url);
		String msg = new Gson().toJson(data);
		publish(topic, new MqttMessage(msg.getBytes()));
	}
}