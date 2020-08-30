package vn.com.irtech.eport.framework.web.service;

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

import com.google.gson.Gson;

import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.config.MqttConfig;
import vn.com.irtech.eport.framework.mqtt.listener.MCRequestHandler;
import vn.com.irtech.eport.framework.mqtt.listener.RobotResponseHandler;

@Component
public class MqttService implements MqttCallback {

	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	private static final String BASE = "eport";
	private static final String ROBOT_BASE = BASE + "/robot";
	private static final String NOTIFICATION_OM_TOPIC = BASE + "/notification/om";
	private static final String NOTIFICATION_IT_TOPIC = BASE + "/notification/it";
	private static final String NOTIFICATION_CONT_TOPIC = BASE + "/notification/cont";
	private static final String ROBOT_CONNECTION_REQUEST = ROBOT_BASE + "/connection/+/request";
	private static final String ROBOT_CONNECTION_RESPONSE = ROBOT_BASE + "/connection/+/response";

	@Autowired
	private MqttAsyncClient mqttClient;
	@Autowired
	private MqttConfig config;
	private Object connectLock = new Object();

	@Autowired
	private MCRequestHandler mcRequestHandler;

	@Autowired
	private RobotResponseHandler robotResponseHandler;

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
		tokens.add(mqttClient.subscribe(BASE + "/mc/plan/request", 0, mcRequestHandler));

		tokens.add(mqttClient.subscribe(ROBOT_CONNECTION_RESPONSE, 0, robotResponseHandler));
		// subscribe default topics when connect
//		tokens.add(mqttClient.subscribe(BASE, 0, robotUpdateStatusHandler));
		// Wait for subscribe complete
		for (IMqttToken token : tokens) {
			token.waitForCompletion();
			System.out.println("Subscribed: " + token.getTopics()[0]);
		}
	}

	@PreDestroy
	public void disconnect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				IMqttToken token = mqttClient.disconnect();
				token.waitForCompletion(5000); // 5s: wait 5s timeout
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
		logger.info("Message arrived: " + topic + "," + message.getPayload().toString());
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

	public enum NotificationCode {
		NOTIFICATION_OM, NOTIFICATION_IT, NOTIFICATION_CONT
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

	/**
	 * 
	 * @param uuid
	 * @throws MqttException
	 */
	public void pingRobot(String uuid) throws MqttException {
		Map<String, String> data = new HashMap<>();
		data.put("msg", "ping");
		String msg = new Gson().toJson(data);
		String topic = ROBOT_CONNECTION_REQUEST.replace("+", uuid);
		publish(topic, new MqttMessage(msg.getBytes()));
	}
}
