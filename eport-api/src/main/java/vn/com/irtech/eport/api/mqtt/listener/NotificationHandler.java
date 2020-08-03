package vn.com.irtech.eport.api.mqtt.listener;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(NotificationHandler.class);
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		
	}
}
