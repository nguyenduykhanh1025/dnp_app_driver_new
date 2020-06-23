package vn.com.irtech.eport.framework.mqtt.listener;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotUpdateStatusHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotUpdateStatusHandler.class);

	@Autowired
	private ISysRobotService robotService;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message content : " + messageContent);
		Map<String, Object> map = null;
		try {
			messageContent = messageContent.replace("\'", "\"");
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(messageContent, Map.class);
		} catch (Exception e) {
			return;
		}

		// Get uuid
		String uuId = map.get("id") == null ? null : map.get("id").toString();
		if (uuId == null) {
			return;
		}

		// Get status
		String status = map.get("status") == null ? null : map.get("status").toString();

		// Get ip
		String ipAddress = map.get("ip") == null ? null : map.get("ip").toString();

		// Get services robot support
		Boolean isReceiveContFullOrder = "1".equals(
				map.get("isReceiveContFullOrder") == null ? null : map.get("isReceiveContFullOrder").toString());

		Boolean isReceiveContEmptyOrder = "1".equals(
				map.get("isReceiveContEmptyOrder") == null ? null : map.get("isReceiveContEmptyOrder").toString());

		Boolean isSendContFullOrder = "1"
				.equals(map.get("isSendContFullOrder") == null ? null : map.get("isSendContFullOrder").toString());
		Boolean isSendContEmptyOrder = "1"
				.equals(map.get("isSendContEmptyOrder") == null ? null : map.get("isSendContEmptyOrder").toString());

		SysRobot sysRobot = new SysRobot();
		sysRobot.setUuId(uuId);
		sysRobot.setStatus(status);
		sysRobot.setIpAddress(ipAddress);
		sysRobot.setIpAddress(ipAddress);
		sysRobot.setIsReceiveContFullOrder(isReceiveContFullOrder);
		sysRobot.setIsReceiveContEmptyOrder(isReceiveContEmptyOrder);
		sysRobot.setIsSendContFullOrder(isSendContFullOrder);
		sysRobot.setIsSendContEmptyOrder(isSendContEmptyOrder);

		// check robot exists in db
		if (robotService.selectRobotByUuId(uuId) == null) {
			// insert robot to db
			robotService.insertRobot(sysRobot);
		} else {
			// update status of robot
			robotService.updateRobotStatusByUuId(uuId, status);
		}

		// if robot is busing
		if ("1".equals(status)) {
			try {
				String receiptIdStr = map.get("receiptId") == null ? null : map.get("receiptId").toString();
				Long receiptId = Long.parseLong(receiptIdStr);
				this.updateReceiptId(receiptId);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * update receiptId
	 * 
	 * @param receiptId
	 */
	private void updateReceiptId(Long receiptId) {
		// TODO: update receiptId
	}

}
