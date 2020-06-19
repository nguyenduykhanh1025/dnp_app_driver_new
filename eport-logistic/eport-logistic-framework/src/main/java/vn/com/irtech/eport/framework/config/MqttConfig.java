package vn.com.irtech.eport.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.framework.mqtt.service.MqttService;

/**
 * @Classname MqttConfig
 * @Description mqtt Related configuration information
 * @Date 2020/3/5 11:00
 * @Created by bam
 */
@Component
@ConfigurationProperties("mqtt")
public class MqttConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
	
    @Autowired
    private MqttService mqttService;

    /**
     * User name
     */
    private String username;
	/**
     * Password
     */
    private String password;
    /**
     * Connection address
     */
    private String hostUrl;
    /**
     * Customer Id
     */
    private String clientID;
    /**
     * Default connection topic
     */
    private String defaultTopic;
    /**
     * Timeout time
     */
    private int timeout;
    /**
     * Keep connected
     */
    private int keepalive;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getDefaultTopic() {
		return defaultTopic;
	}

	public void setDefaultTopic(String defaultTopic) {
		this.defaultTopic = defaultTopic;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getKeepalive() {
		return keepalive;
	}

	public void setKeepalive(int keepalive) {
		this.keepalive = keepalive;
	}

	public void setMqttPushClient(MqttService mqttPushClient) {
		this.mqttService = mqttPushClient;
	}

    @Bean
    public MqttService getMqttPushClient() throws Exception {
        try {
        	mqttService.connect(hostUrl, clientID, username, password, timeout, keepalive);
        	mqttService.subscribeDefaultTopics();
	        return mqttService;
		} catch (Exception e) {
			logger.error("Bad thing happen when connect Mqtt server: " + e.getMessage());
			throw e;
		}
    }
}
