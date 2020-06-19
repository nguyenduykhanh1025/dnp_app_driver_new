package vn.com.irtech.eport.framework.mqtt.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.logistic.dto.ServiceRobotReq;
import vn.com.irtech.eport.system.domain.SysRobot;

/**
 * 
 * @author Nguyen Trong Hieu 2020 July 11 MqttPushClient.java
 *
 */
@Component
public class MqttService {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	public static final String ROBOT_TOPIC_REQ = "eport/robot/+/request";
	
	public static final String ROBOT_STATUS_TOPIC = "eport/robot";

	public static final String ROBOT_TOPIC_RES = "eport/robot/+/response";

	public static final String RECEIVE_CONT_FULL_TOPIC = "receive_cont_full_order";

	public static final String RECEIVE_CONT_EMPTY_TOPIC = "receive_cont_empty_order";

	public static final String SEND_CONT_FULL_TOPIC = "send_cont_full_order";

	public static final String SEND_CONT_EMPTY_TOPIC = "send_cont_empty_order";

	public static final Integer DEFAULT_QOS = 1;

	public static final Boolean DEFAULT_RETAINED = true;

	@Autowired
	private CustomMqttCallback customMqttCallback;

	private static MqttClient client;

	private static MqttClient getClient() {
		return client;
	}

	private static void setClient(MqttClient client) {
		MqttService.client = client;
	}
	
	public enum EServiceRobot{
		RECEIVE_CONT_FULL,
		RECEIVE_CONT_EMPTY,
		SEND_CONT_FULL,
		SEND_CONT_EMPTY
	}

	/**
	 * Client connection
	 *
	 * @param host      ip+port
	 * @param clientID  Client Id
	 * @param username  User name
	 * @param password  Password
	 * @param timeout   Timeout time
	 * @param keepalive Retention number
	 */
	public void connect(String host, String clientID, String username, String password, int timeout, int keepalive)
			throws Exception {
		MqttClient client = new MqttClient(host, clientID, new MemoryPersistence());

		MqttService.setClient(client);
		client.setCallback(customMqttCallback);

		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setConnectionTimeout(timeout);
		options.setKeepAliveInterval(keepalive);

		client.connect(options);
	}

	/**
	 * publish a MQTT message
	 *
	 * @param qos         Connection mode
	 * @param retained    Whether to retain
	 * @param topic       theme
	 * @param pushMessage Message body
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	public void publish(String topic, int qos, boolean retained, String pushMessage)
			throws MqttPersistenceException, MqttException {

		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		message.setPayload(pushMessage.getBytes());
		MqttTopic mTopic = MqttService.getClient().getTopic(topic);
		if (null == mTopic) {
			logger.warn("topic not exist");
		}
		MqttDeliveryToken token;
		token = mTopic.publish(message);
		token.waitForCompletion();
	}

	/**
	 * publish a MQTT message
	 *
	 * @param topic       theme
	 * @param pushMessage Message body
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	public void publish(String topic, String pushMessage) throws MqttPersistenceException, MqttException {
		this.publish(topic, DEFAULT_QOS, DEFAULT_RETAINED, pushMessage);
	}
	
	/**
	 * publish message call robot execute service
	 * @param contentMessage
	 */
	public void publishMessageToRobot(ServiceRobotReq serviceRobotReq, EServiceRobot serviceRobot) {
		SysRobot sysRobot = this.getAvailableRobot(serviceRobot);
		if (sysRobot != null) {
			// TODO: get uuid, publish message
		}
	}

	/**
	 * Get a robot already to execute service
	 * @param service
	 * @return
	 */
	public SysRobot getAvailableRobot(EServiceRobot serviceRobot) {
		// TODO
		return null;
	}
	
	/**
	 * Subscribe to a topic
	 *
	 * @param topic theme
	 * @param qos   Connection mode
	 * @throws MqttException
	 */
	public void subscribeTopic(String topic, Integer qos) throws MqttException {
		logger.info("Start subscribing to topics" + topic);
		MqttService.getClient().subscribe(topic, qos);
	}

	/**
	 * Subscribe to a topic
	 *
	 * @param topic theme
	 * @throws MqttException
	 */
	public void subscribeTopic(String topic) throws MqttException {
		logger.info("Start subscribing to topics" + topic);
		MqttService.getClient().subscribe(topic, DEFAULT_QOS);
	}

	/**
	 * Subscribe default topics
	 * 
	 */
	public void subscribeDefaultTopics() {
		try {
			this.subscribeTopic(ROBOT_TOPIC_RES);
			this.subscribeTopic(ROBOT_STATUS_TOPIC);
		} catch (MqttException e) {
			logger.warn("Bad thing happen when subscribe default topics: " + e.getMessage());
		}
	}

}
