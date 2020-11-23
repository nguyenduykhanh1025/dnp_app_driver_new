package vn.com.irtech.eport.web.mqtt.listener;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.utils.CacheUtils;

@Component
public class ScaleResultHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ScaleResultHandler.class);

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

	/**
	 * Begin process handle the message
	 * 
	 * @param topic
	 * @param message
	 * @throws Exception
	 */
	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload());
		logger.info(String.format("Receive message topic: [%s], content: %s", topic, messageContent));

		Map<String, Object> map = new Gson().fromJson(messageContent, Map.class);

		// Get gate id from topic
		String[] topicArr = topic.split("/");
		String gateId = "";
		if (topicArr.length == 5) {
			gateId = topicArr[2];
		}

		// Get weight
		String weight = map.get("weight") == null ? null : map.get("weight").toString();

		// Put weight result to cache
		CacheUtils.put("wgt_" + gateId, weight);
	}
}