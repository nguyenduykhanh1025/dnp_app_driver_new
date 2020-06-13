package vn.com.irtech.eport.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.framework.mqtt.service.MqttPushClient;

/**
 * @Classname MqttConfig
 * @Description mqtt Related configuration information
 * @Date 2020/3/5 11:00
 * @Created by bam
 */
@Component
@ConfigurationProperties("mqtt")
public class MqttConfig {
    @Autowired
    private MqttPushClient mqttPushClient;

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

	public void setMqttPushClient(MqttPushClient mqttPushClient) {
		this.mqttPushClient = mqttPushClient;
	}

    @Bean
    public MqttPushClient getMqttPushClient() {
    	System.out.println("hostUrl: "+ hostUrl);
    	System.out.println("clientID: "+ clientID);
    	System.out.println("username: "+ username);
    	System.out.println("password: "+ password);
    	System.out.println("timeout: "+timeout);
    	System.out.println("keepalive: "+ keepalive);
        mqttPushClient.connect(hostUrl, clientID, username, password, timeout, keepalive);
        // End with / / to subscribe to all topics starting with test
        mqttPushClient.subscribe(defaultTopic, 0);
        return mqttPushClient;
    }
}
