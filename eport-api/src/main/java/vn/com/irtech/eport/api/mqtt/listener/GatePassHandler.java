package vn.com.irtech.eport.api.mqtt.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

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
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysNotification;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.system.service.ISysNotificationService;
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
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	@Autowired
	private IShipmentService shipmentService;
	@Autowired
	private ISysNotificationService sysNotificationService;
	@Autowired
	private ISysNotificationReceiverService sysNotificationReceiverService;
	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	// time wait mc input postion
	private static final Long TIME_OUT_WAIT_MC = 2000L;
	private static final Integer RETRY_WAIT_MC = 60;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				processMessage(topic, message);
			}
		});
	}
	
	private void processMessage(String topic, MqttMessage message) {
//		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message topic: {}, content : {}", topic, messageContent);
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
			this.updatePickupHistory(gateInFormData, result, uuId, status);
		}
	}
	
	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	private void updatePickupHistory(GateInFormData gateInFormData, String result, String uuId, String status) {
		// List data response for driver
		List<DriverDataRes> driverDataRes = new ArrayList<>();
		DriverRes driverRes = new DriverRes();
		driverRes.setStatus(BusinessConsts.FINISH);
		
		// Declare process order to update status
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(gateInFormData.getReceiptId());
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
					
					pickupHistory.setGateinDate(new Date());
					pickupHistory.setStatus(1);
					pickupHistoryService.updatePickupHistory(pickupHistory);
					
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setId(pickupHistory.getShipmentDetailId());
					shipmentDetail.setFinishStatus("Y");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// Update shipment status if all shipment detail is finish
					shipmentDetail.setId(null);
					shipmentDetail.setFinishStatus("N");
					if (CollectionUtils.isEmpty(shipmentDetailService.selectShipmentDetailList(shipmentDetail))) {
						Shipment shipment = new Shipment();
						shipment.setStatus("4");
						shipment.setId(pickupHistory.getShipmentId());
						shipmentService.updateShipment(shipment);
					}
					
					
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
					
					sendNotificationToDriver(pickupHistory, shipmentDetail);
				}
			}
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupOut())) {
				
				// Iterate pickup out list
				for (PickupHistory pickupHistory : gateInFormData.getPickupOut()) {
					pickupHistory.setStatus(1);
					pickupHistory.setGateinDate(new Date());
					pickupHistoryService.updatePickupHistory(pickupHistory);
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setId(pickupHistory.getShipmentDetailId());
					shipmentDetail.setFinishStatus("Y");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
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
					

					sendNotificationToDriver(pickupHistory, shipmentDetail);
				}
			}
			
			// Set data for response driver
			driverRes.setData(driverDataRes);
			driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
			driverRes.setResult(BusinessConsts.PASS);;
			processOrder.setResult("S");
			processHistory.setResult("S");
			try {
				if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
					responseDriver(driverRes, gateInFormData.getSessionId());
				}
			} catch (Exception e) {
				logger.error("Error send result gate in for driver: " + e);
			}
			try {
				responseSmartGate(gateInFormData.getGateId(), BusinessConsts.SUCCESS, MessageHelper.getMessage(MessageConsts.E0028));
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}
			
			robotService.updateRobotStatusByUuId(uuId, status);
		} else if("position_failed".equalsIgnoreCase(result)) {
			//gateInFormData 
			int count = 0;
			for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
				if (!checkPickupHistoryHasPosition(pickupHistory)) {
					Map<String, Object> map = new HashMap<>();
					map.put("pickupHistoryId", pickupHistory.getId().toString());
					String msg = new Gson().toJson(map);
					try {
						logger.debug("Received Position failed from Robot. Send MC request: " + msg);
						mqttService.sendMessageToMc(msg);
					} catch (MqttException e) {
						logger.error("Erorr request yard position from mc: " + e);
					}
					
					for (int i = 1; i<= RETRY_WAIT_MC; i++) {
						logger.debug("Wait " + TIME_OUT_WAIT_MC  + " miliseconds");
						try {
							Thread.sleep(TIME_OUT_WAIT_MC);
						} catch (InterruptedException e) {
							logger.error("Error sleep to wait mc: " + e);
						}
						logger.debug("Check db for position");
						pickupHistory = pickupHistoryService .selectPickupHistoryById(pickupHistory.getId());
						if (StringUtils.isEmpty(pickupHistory.getArea())) {
							pickupHistory.setArea("");
						}
						if (StringUtils.isEmpty(pickupHistory.getBlock())) {
							pickupHistory.setBlock("");
						}
						gateInFormData.getPickupIn().set(count, pickupHistory);
						if (checkPickupHistoryHasPosition(pickupHistory)) {
							logger.debug("Pickup has been updated position. Continue to gate-in");
							break;
						}
					}
					count++;
				} else {
					count++;
				}
			}
			
			// re-try gate order with yard position
			ProcessOrder processOrderNew = new ProcessOrder();
			processOrderNew.setShipmentId(processOrder.getShipmentId());
			processOrderNew.setServiceType(8);
			processOrderNew.setLogisticGroupId(processOrder.getLogisticGroupId());
			processOrderNew.setStatus(0);
			processOrderService.insertProcessOrder(processOrderNew);
			
			gateInFormData.setReceiptId(processOrder.getId());
			for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
				pickupHistory.setProcessOrderId(processOrderNew.getId());
				pickupHistoryService.updatePickupHistory(pickupHistory);
			}
			String msg = new Gson().toJson(gateInFormData);
			processOrderNew.setStatus(1);
			processOrderNew.setRobotUuid(uuId);
			processOrderNew.setProcessData(msg);
			processOrderService.updateProcessOrder(processOrderNew);
			logger.debug("Send request to robot after failed location: " + uuId + ", content: " + msg);
			try {
				mqttService.sendMessageToRobot(msg, uuId);
			} catch (MqttException e) {
				logger.error("Error send gate order to robot: " + e);
			}
			processOrder.setResult("F");
			
		} else {
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupIn())) {
				
				// Iterate pickup in list
				for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setId(pickupHistory.getShipmentDetailId());
					shipmentDetail.setFinishStatus("E");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
			}
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupOut())) {
				
				// Iterate pickup out list
				for (PickupHistory pickupHistory : gateInFormData.getPickupOut()) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setId(pickupHistory.getShipmentDetailId());
					shipmentDetail.setFinishStatus("E");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
			}
			
			driverRes.setResult(BusinessConsts.FAIL);
			driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
			try {
				if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
					responseDriver(driverRes, gateInFormData.getSessionId());
				}
			} catch (Exception e) {
				logger.error("Error send result gate in for driver: " + e);
			}
			try {
				responseSmartGate(gateInFormData.getGateId(), BusinessConsts.FAIL, MessageHelper.getMessage(MessageConsts.E0024));
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}
			processOrder.setResult("F");
			processHistory.setResult("F");
			robotService.updateRobotStatusByUuId(uuId, status);
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
	private void responseSmartGate(String gateId, String result, String message) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("result", result);
		map.put("message", message);
		String msg = new Gson().toJson(map);
		logger.debug("Send result to SmartGate App: " + msg);
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
	
	private void sendNotificationToDriver(PickupHistory pickupHistory, ShipmentDetail shipmentDetail) {
		// Create info notification
		if (pickupHistory.getDriverId() != null) {
			SysNotification sysNotification = new SysNotification();
			sysNotification.setTitle("Thông báo vào cổng.");
			sysNotification.setNotifyLevel(2L);
			sysNotification.setContent("Xác thực tại cổng thành công cho container  " + shipmentDetail.getContainerNo());
			sysNotification.setNotifyLink("");
			sysNotification.setStatus(1L);
			sysNotificationService.insertSysNotification(sysNotification);
			
			// Notification receiver
			SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
			sysNotificationReceiver.setUserId(pickupHistory.getDriverId());
			sysNotificationReceiver.setNotificationId(sysNotification.getId());
			sysNotificationReceiver.setUserType(2L);
			sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);
		}
	}
	
	/**
	 * Check pickup history has position
	 * 
	 * @param pickupHistory
	 * @return Boolean
	 */
	private Boolean checkPickupHistoryHasPosition(PickupHistory pickupHistory) {
		if (StringUtils.isNotBlank(pickupHistory.getArea())) {
			return true;
		}
		if (StringUtils.isNotBlank(pickupHistory.getBlock()) && StringUtils.isNotBlank(pickupHistory.getBay())
			&& StringUtils.isNotBlank(pickupHistory.getLine()) && StringUtils.isNotBlank(pickupHistory.getTier())) {
				return true;
			}
		logger.debug("No position for pickup: " + pickupHistory.getContainerNo());
		return false;
	}
}
