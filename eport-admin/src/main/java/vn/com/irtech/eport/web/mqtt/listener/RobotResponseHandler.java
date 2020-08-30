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

import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotResponseHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotResponseHandler.class);
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

		String messageContent = new String(message.getPayload());
		logger.info("Receive message topic: {}, content {}", topic, messageContent);
		Map<String, Object> map = null;
		try {
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return;
		}

		String status = map.get("status") == null ? null : map.get("status").toString();

		String uuid = null;
		String[] dataStrings = topic.split("/");
		if (dataStrings.length > 3) {
			uuid = dataStrings[3];
		}

		if (uuid == null) {
			return;
		}
		// get robot status, update db when status was updated only
		SysRobot robot = robotService.selectRobotByUuId(uuid);
		if (StringUtils.isNotEmpty(status) && robot != null && !robot.getStatus().equals(status)) {
			robot.setStatus(status);
			robotService.updateRobot(robot);
			logger.debug("Update robot " + uuid + " status to " + status);
		}
	}
}