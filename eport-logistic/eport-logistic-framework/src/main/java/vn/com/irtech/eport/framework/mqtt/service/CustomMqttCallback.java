package vn.com.irtech.eport.framework.mqtt.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.framework.config.MqttConfig;
import vn.com.irtech.eport.logistic.domain.QueueOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IQueueOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

/**
 * @Classname PushCallback
 * @Description Consumer monitoring
 * @Date 2020/6/11 23:59
 * @Created by Nguyen Trong Hieu
 */
@Component
public class CustomMqttCallback implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	@Autowired
	private MqttConfig mqttConfig;

	@Autowired
	private ISysRobotService robotService;
	
	@Autowired
	private IQueueOrderService queueOrderService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Override
	public void connectionLost(Throwable throwable) {
		// After the connection is lost, it is usually reconnected here
		// logger.info("Disconnected, can be reconnected");
		// try {
		// 	mqttConfig.getMqttPushClient();
		// } catch (Exception e) {
		// 	logger.error("Bad thing happen when reconnected Mqtt server: " + e.getMessage());
		// }
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		// The message you get after you subscribe will be executed here
		logger.info("Receive message subject : " + topic);
		logger.info("receive messages Qos : " + mqttMessage.getQos());
		String messageContent = new String(mqttMessage.getPayload());
		logger.info("Receive message content : " + messageContent);

		switch (topic) {
		case MqttService.ROBOT_STATUS_TOPIC:
			handleWhenRobotSendStatus(messageContent);
			break;
		default:
			handleWhenRobotResponse(topic, messageContent);
			break;
		}
	}

	/**
	 * Handle when robot public status
	 * @param messageContent
	 */
	private void handleWhenRobotSendStatus(String messageContent) {
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
			} catch (Exception ex) {}
		}
	}
	
	/**
	 * update receiptId
	 * @param receiptId
	 */
	private void updateReceiptId(Long receiptId) {
		// TODO: update receiptId
	}

	/**
	 * Hande when robot executed task done and public response
	 * @param topic
	 * @param messageContent
	 */
	private void handleWhenRobotResponse(String topic, String messageContent) {

		Map<String, Object> map = null;
		try {
			messageContent = messageContent.replace("\'", "\"");
			ObjectMapper mapper = new ObjectMapper();
			map = mapper.readValue(messageContent, Map.class);
		} catch (Exception e) {
			return;
		}

		String uuId = map.get("id") == null ? null : map.get("id").toString();
		if (uuId == null) {
			return;
		}

		SysRobot sysRobot = robotService.selectRobotByUuId(uuId);

		if (sysRobot == null) {
			return;
		}

		String status = map.get("status") == null ? null : map.get("status").toString();
		String result = map.get("result") == null ? null : map.get("result").toString();
		String receiptId = map.get("receiptId") == null ? null : map.get("receiptId").toString();

		robotService.updateRobotStatusByUuId(uuId, status);

		this.updateShipmentDetail(result, receiptId);

	}

	/**
	 * update shipment detail
	 * @param result: "success/error"
	 * @param receiptId
	 */
	private void updateShipmentDetail(String result, String receiptId) {
		// TODO: update shipment detail
		Long id = Long.parseLong(receiptId);
		QueueOrder queueOrder = queueOrderService.selectQueueOrderById(id);
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setRegisterNo(receiptId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (queueOrder != null) {
			if ("success".equalsIgnoreCase(result)) {
				 shipmentDetailService.updateProcessStatus(shipmentDetails, "Y");
			} else {
				shipmentDetailService.updateProcessStatus(shipmentDetails, "E");
			}
		}	
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		logger.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
	}
}
