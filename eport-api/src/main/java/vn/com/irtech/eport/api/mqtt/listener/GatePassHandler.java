package vn.com.irtech.eport.api.mqtt.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.DriverDataRes;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateInFormData;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class GatePassHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(GatePassHandler.class);
	
	@Autowired
	private ISysRobotService robotService;
	
	@Autowired
	private IProcessOrderService processOrderService;
	
	@Autowired
	private IProcessHistoryService processHistoryService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private MqttService mqttService;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message content : " + messageContent);
		Map<String, Object> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
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
		String dataString = map.get("data") == null ? null : new Gson().toJson(map.get("data"));
		
		GateInFormData gateInFormData = new Gson().fromJson(dataString, GateInFormData.class);
		if (gateInFormData != null && gateInFormData.getReceiptId() != null) {
			this.updatePickupHistory(gateInFormData, result);
		}
		
		robotService.updateRobotStatusByUuId(uuId, status);
	}
	
	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	@Transactional
	private void updatePickupHistory(GateInFormData gateInFormData, String result) {
		// List data response for driver
		List<DriverDataRes> driverDataRes = new ArrayList<>();
		DriverRes driverRes = new DriverRes();
		driverRes.setStatus(BusinessConsts.FINISH);
		
		// Declare process order to update status
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setId(gateInFormData.getReceiptId());
		processOrder.setStatus(2);
		
		// Declare process history to save history
		ProcessHistory processHistory = new ProcessHistory();
		processHistory.setProcessOrderId(gateInFormData.getReceiptId());
		processHistory.setRobotUuid(gateInFormData.getGateId());
		processHistory.setStatus(2); // FINISH
		
		// If Robot return success
		if ("success".equalsIgnoreCase(result)) {
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupIn())) {
				
				// Iterate pickup in list
				for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
					pickupHistory.setStatus(1);
					pickupHistoryService.updatePickupHistory(pickupHistory);
					DriverDataRes driverData = new DriverDataRes();
					driverData.setPickupHistoryId(pickupHistory.getId());
					driverData.setContNo(pickupHistory.getContainerNo());
					driverData.setYardPosition(pickupHistory.getBlock()+"-"+pickupHistory.getBay()
					+"-"+pickupHistory.getLine()+"-"+pickupHistory.getTier());
					if (pickupHistory.getShipment().getServiceType() == 2) {
						driverData.setFe("E");
					} else {
						driverData.setFe("F");
					}
					driverData.setSztp(pickupHistory.getSztp());
					driverData.setTruckNo(pickupHistory.getTruckNo());
					driverData.setChassisNo(pickupHistory.getChassisNo());
					driverData.setWgt(gateInFormData.getWgt());
					driverDataRes.add(driverData);
				}
			}
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupOut())) {
				
				// Iterate pickup out list
				for (PickupHistory pickupHistory : gateInFormData.getPickupOut()) {
					pickupHistory.setStatus(1);
					pickupHistoryService.updatePickupHistory(pickupHistory);
					DriverDataRes driverData = new DriverDataRes();
					driverData.setPickupHistoryId(pickupHistory.getId());
					driverData.setContNo(pickupHistory.getContainerNo());
					driverData.setYardPosition(pickupHistory.getBlock()+"-"+pickupHistory.getBay()
					+"-"+pickupHistory.getLine()+"-"+pickupHistory.getTier());
					if (pickupHistory.getShipment().getServiceType() == 1) {
						driverData.setFe("F");
					} else {
						driverData.setFe("E");
					}
					driverData.setSztp(pickupHistory.getSztp());
					driverData.setTruckNo(pickupHistory.getTruckNo());
					driverData.setChassisNo(pickupHistory.getChassisNo());
					driverData.setWgt(gateInFormData.getWgt());
					driverDataRes.add(driverData);
				}
			}
			
			// Set data for response driver
			driverRes.setData(driverDataRes);
			driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
			driverRes.setResult(BusinessConsts.PASS);;
			processOrder.setResult("S");
			processHistory.setResult("S");
			try {
				responseDriver(driverRes, gateInFormData.getSessionId());
				responseSmartGate(gateInFormData.getGateId(), BusinessConsts.SUCCESS);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			
		} else {
			driverRes.setResult(BusinessConsts.FAIL);
			driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
			try {
				responseDriver(driverRes, gateInFormData.getSessionId());
				responseSmartGate(gateInFormData.getGateId(), BusinessConsts.FAIL);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			processOrder.setResult("F");
			processHistory.setResult("F");
		}
		
		// Update process order (S or F)
		processOrderService.updateProcessOrder(processOrder);
		
		// Update History robot
		processHistoryService.insertProcessHistory(processHistory);
	}
	
	
	/**
	 * Response checkin result to smart gate
	 * 
	 * @param result
	 * @throws Exception
	 */
	private void responseSmartGate(String gateId, String result) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		String msg = new Gson().toJson(map);
		mqttService.publish(MqttConsts.SMART_GATE_RES_TOPIC.replace("+", gateId), new MqttMessage(msg.getBytes()));
	}
	
	/**
	 * Response checkin result to driver
	 * 
	 * @param driverRes
	 * @param sessionId
	 * @throws Exception
	 */
	private void responseDriver(DriverRes driverRes, String sessionId) throws Exception {
		String msg = new Gson().toJson(driverRes);
		mqttService.publish(MqttConsts.DRIVER_RES_TOPIC.replace("+", sessionId), new MqttMessage(msg.getBytes()));
	}
}
