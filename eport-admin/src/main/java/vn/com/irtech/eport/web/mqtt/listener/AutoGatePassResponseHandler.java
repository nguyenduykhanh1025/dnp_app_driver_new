package vn.com.irtech.eport.web.mqtt.listener;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class AutoGatePassResponseHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(AutoGatePassResponseHandler.class);

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}

	private void processMessage(String topic, MqttMessage message) throws Exception {


	}
}