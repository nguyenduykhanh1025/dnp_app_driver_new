package vn.com.irtech.eport.framework.mqtt.service;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Nguyen Trong Hieu
 * 2020 July 11
 * MqttPushClient.java
 *
 */
@Component
public class MqttPushClient {
    private static final Logger logger = LoggerFactory.getLogger(MqttPushClient.class);

    @Autowired
    private PushCallback pushCallback;

    private static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttPushClient.client = client;
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
    public void connect(String host, String clientID, String username, String password, int timeout, int keepalive) {
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepalive);
            MqttPushClient.setClient(client);
            try {
                client.setCallback(pushCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Release
     *
     * @param qos         Connection mode
     * @param retained    Whether to retain
     * @param topic       theme
     * @param pushMessage Message body
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            logger.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribe to a topic
     *
     * @param topic theme
     * @param qos   Connection mode
     */
    public void subscribe(String topic, int qos) {
        logger.info("Start subscribing to topics" + topic);
        try {
            MqttPushClient.getClient().subscribe(topic, qos);
            MqttPushClient.getClient().subscribe("receive_cont_full_order", qos);
            MqttPushClient.getClient().subscribe("send_cont_empty_order", qos);
            MqttPushClient.getClient().subscribe("receive_cont_empty_order", qos);
            MqttPushClient.getClient().subscribe("send_cont_full_order", qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
