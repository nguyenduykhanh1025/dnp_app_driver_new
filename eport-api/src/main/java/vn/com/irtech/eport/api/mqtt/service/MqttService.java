package vn.com.irtech.eport.api.mqtt.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.mqtt.listener.CheckinHandler;

@Component
public class MqttService implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	public static String BASE_TOPIC;

	@Autowired
	private CheckinHandler checkinHandler;

	@Value("${mqtt.root:'eport'}")
	public void setBaseTopic(String baseTopic) {
		BASE_TOPIC = baseTopic;
	}

	private MqttAsyncClient mqttClient;
	private Object connectLock = new Object();

	private Boolean isReconnecting = false;
	private String host;
	private String username;
	private String password;

	public MqttAsyncClient getMqttClient() {
		return mqttClient;
	}

	public boolean isConnected() {
		return mqttClient != null && mqttClient.isConnected();
	}

	public void connect(String host, String username, String password) throws MqttException {
		// save info first
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

		tokens.add(mqttClient.subscribe(MqttConsts.SMART_GATE_REQ_TOPIC, 0, checkinHandler));

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
		String prerix = "API-";
		if (env.containsKey("COMPUTERNAME"))
			return prerix + env.get("COMPUTERNAME");
		else if (env.containsKey("HOSTNAME"))
			return prerix + env.get("HOSTNAME");
		else
			return prerix + "Unknown Computer";
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

	public void sendMessageToMc(String message) throws MqttException {
		this.publish(MqttConsts.MC_REQ_TOPIC, new MqttMessage(message.getBytes()));
	}

}
