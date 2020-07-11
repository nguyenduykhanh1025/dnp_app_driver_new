package vn.com.irtech.eport.framework.config;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.framework.web.service.MqttService;

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

	@Bean
	public MqttAsyncClient getMqttPushClient() throws Exception {
		mqttService.connect(hostUrl, username, password);
		return mqttService.getMqttClient();
	}
}
