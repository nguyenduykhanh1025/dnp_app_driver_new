package vn.com.irtech.eport.api.mqtt.service;

import java.util.ArrayList;
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

import vn.com.irtech.eport.api.mqtt.listener.NotificationHandler;

@Component
public class MqttService implements MqttCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	private static final String BASE = "eport";
	
	private static final String MC_REQUEST_TOPIC = BASE + "/mc/plan/request";
	
	@Autowired
	private NotificationHandler notificationHandler;

	private static MqttService instance = null;
	private MqttAsyncClient mqttClient;
	private Object connectLock = new Object();
	
	// connection info
	private String host;
	private String username;
	private String password;

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
		// System.out.println("Subscribe to topic: " + BASE);
		// tokens.add(mqttClient.subscribe(BASE, 0, notificationHandler));
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
			return "Api-Mobile-" + env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return "Api-Mobile-" + env.get("HOSTNAME");
		else
			return "Api-Mobile-Unknown Computer";
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

	public void sendMessageToMc(String message) throws MqttException {
		this.publish(MC_REQUEST_TOPIC, new MqttMessage(message.getBytes()));
	}
}
