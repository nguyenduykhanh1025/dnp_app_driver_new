package vn.com.irtech.eport.framework.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.framework.mqtt.listener.RobotResponseHandler;
import vn.com.irtech.eport.framework.mqtt.listener.RobotUpdateStatusHandler;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class MqttService implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	private static final String BASE = "eport/robot";
	private static final String RESPONSE_TOPIC = BASE + "/+/response";

	private static final String REQUEST_TOPIC = BASE + "/+/request";

	private static MqttService instance = null;
	private MqttAsyncClient mqttClient;
	private Object connectLock = new Object();

	@Autowired
	private RobotUpdateStatusHandler robotUpdateStatusHandler;

	@Autowired
	private RobotResponseHandler robotResponseHandler;

	@Autowired
	private ISysRobotService robotService;

	private Boolean isReconnecting = false;

	public static MqttService getInstance() {
		if (instance == null) {
			instance = new MqttService();
		}
		return instance;
	}

	private MqttService() {
	}

	public MqttAsyncClient getMqttClient() {
		return mqttClient;
	}

	public boolean isConnected() {
		return mqttClient != null && mqttClient.isConnected();
	}

	public void connect(String host, String username, String password) throws MqttException {
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
							}

							@Override
							public void onFailure(IMqttToken iMqttToken, Throwable e) {
								logger.info("MQTT broker connection faied!" + e.getMessage());
							}
						}).waitForCompletion();
						subscribeToTopics();
					} catch (MqttException e) {
						logger.info("MQTT broker connection failed!" + e.getMessage());
						if (!mqttClient.isConnected()) {
							try {
								Thread.sleep(3000); // 3s
							} catch (InterruptedException e1) {
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
		tokens.add(mqttClient.subscribe(BASE, 0, robotUpdateStatusHandler));
		tokens.add(mqttClient.subscribe(RESPONSE_TOPIC, 0, robotResponseHandler));
		// Wait for subscribe complete
		for (IMqttToken token : tokens) {
			token.waitForCompletion();
		}
	}

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
			return env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return env.get("HOSTNAME");
		else
			return "Unknown Computer";
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
			}
			try {
				Thread.sleep(3000); // wait 3s before reconnect
			} catch (InterruptedException e) {
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
			IMqttToken token = mqttClient.connect();
			token.waitForCompletion();
			subscribeToTopics();
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
		RECEIVE_CONT_FULL, RECEIVE_CONT_EMPTY, SEND_CONT_FULL, SEND_CONT_EMPTY
	}

	public boolean publishMessageToRobot(Object payLoad, EServiceRobot serviceRobot) throws MqttException {
		SysRobot sysRobot = this.getAvailableRobot(serviceRobot);
		if (sysRobot == null) {
			return false;
		}
		String msg = new Gson().toJson(payLoad);
		String topic = REQUEST_TOPIC.replace("+", sysRobot.getUuId());
		publish(topic, new MqttMessage(msg.getBytes()));
		return true;
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
		}
		return robotService.findFirstRobot(sysRobot);
	}
}