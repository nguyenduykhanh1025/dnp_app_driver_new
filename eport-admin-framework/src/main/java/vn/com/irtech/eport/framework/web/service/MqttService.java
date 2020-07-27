package vn.com.irtech.eport.framework.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

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

	private static final String ROBOT_CONNECTION_REQUEST = ROBOT_BASE + "/connection/request";

	private static final String ROBOT_CONNECTION_RESPONSE = ROBOT_BASE + "/connection/response";

	private static MqttService instance = null;
	private MqttAsyncClient mqttClient;
	private Object connectLock = new Object();

	private Boolean isReconnecting = false;

	public static MqttService getInstance() {
		if (instance == null) {
			instance = new MqttService();
		}
		return instance;
	}
	
	@Autowired
	private MCRequestHandler mcRequestHandler;

	@Autowired
	private RobotResponseHandler robotResponseHandler;

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
		tokens.add(mqttClient.subscribe(BASE + "/mc/plan/request", 0, mcRequestHandler));

		tokens.add(mqttClient.subscribe(ROBOT_CONNECTION_RESPONSE, 0, robotResponseHandler));
		// subscribe default topics when connect
//		tokens.add(mqttClient.subscribe(BASE, 0, robotUpdateStatusHandler));
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
			return "Admin-" + env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return "Admin-"+ env.get("HOSTNAME");
		else
			return "Admin-Unknown Computer";
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
		String topic = ROBOT_CONNECTION_REQUEST + "/" + uuid;
		publish(topic, new MqttMessage(msg.getBytes()));
	}
}
