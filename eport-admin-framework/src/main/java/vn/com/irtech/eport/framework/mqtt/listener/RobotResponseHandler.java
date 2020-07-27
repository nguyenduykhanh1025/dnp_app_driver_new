package vn.com.irtech.eport.framework.mqtt.listener;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotResponseHandler implements IMqttMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RobotResponseHandler.class);

    @Autowired
    private ISysRobotService robotService;
    
    @Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message content : " + messageContent);
		Map<String, Object> map = null;
		try {
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
            logger.warn(e.getMessage());
            return;
		}
		
        String uuid = String.valueOf(map.get("uuid"));
        
        if (uuid == null) {
            return;
        }

        SysRobot robot = new SysRobot();
        robotService.updateRobot(robot);
	}
}