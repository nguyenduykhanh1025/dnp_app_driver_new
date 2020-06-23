package vn.com.irtech.eport.framework.mqtt.listener;

import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.logistic.domain.QueueOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IQueueOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotResponseHandler implements IMqttMessageListener{
	
	private static final Logger logger = LoggerFactory.getLogger(RobotResponseHandler.class);
	
	@Autowired
	private ISysRobotService robotService;
	
	@Autowired
	private IQueueOrderService queueOrderService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

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
	 * 
	 * @param result:   "success/error"
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

}
