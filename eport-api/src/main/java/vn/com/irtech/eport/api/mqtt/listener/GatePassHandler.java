																																																																																																																																																																					package vn.com.irtech.eport.api.mqtt.listener;

import java.lang.reflect.Type;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.DriverDataRes;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateInFormData;
import vn.com.irtech.eport.api.form.PickupRobotResult;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.constant.EportConstants;
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
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.system.service.ISysNotificationService;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class GatePassHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(GatePassHandler.class);
	
	@Autowired
	private ISysConfigService configService;
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
		String pickupInResult = map.get("pickupInResult") == null ? null : new Gson().toJson(map.get("pickupInResult"));
		String pickupOutResult = map.get("pickupOutResult") == null ? null : new Gson().toJson(map.get("pickupOutResult"));
		
		GateInFormData gateInFormData = new Gson().fromJson(dataString, GateInFormData.class);
		if (gateInFormData != null && gateInFormData.getReceiptId() != null) {
			robotService.updateRobotStatusByUuId(uuId, status);
			this.updatePickupHistory(gateInFormData, result, uuId, status, pickupInResult, pickupOutResult);
		}
	}
	
	/**
	 * Update pickup history
	 * 
	 * @param gateInFormData
	 * @param result
	 */
	private void updatePickupHistory(GateInFormData gateInFormData, String result, String uuId, String status, String pickupInResult, String pickupOutResult) {
		// List data response for driver
		List<DriverDataRes> driverDataRes = new ArrayList<>();
		DriverRes driverRes = new DriverRes();
		driverRes.setStatus(BusinessConsts.FINISH);
		
		// Declare process order to update status
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(gateInFormData.getReceiptId());
		processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);
		
		// Declare process history to save history
		ProcessHistory history = new ProcessHistory();
		history.setProcessOrderId(gateInFormData.getReceiptId());
		history.setRobotUuid(uuId);
		history.setServiceType(EportConstants.SERVICE_GATE_IN);
		
		// Find history record when start time of this process order is set to update finish time
		List<ProcessHistory> processHistories = processHistoryService.selectProcessHistoryList(history);
		if (CollectionUtils.isNotEmpty(processHistories)) {
			history.setId(processHistories.get(0).getId());
		}
		history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_FINISHED);
		
		// If Robot return success
		if ("success".equalsIgnoreCase(result)) {
			
			if (CollectionUtils.isNotEmpty(gateInFormData.getPickupIn())) {
				
				// Iterate pickup in list
				for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
					if (pickupInResult != null) {
						try {
							List<PickupRobotResult> pickupRobotResults = getListRobotResultPickupFromString(pickupInResult);
							pickupHistory = updatePickupHistoryData(pickupRobotResults, pickupHistory);
						} catch (Exception e) {
							logger.error("Error when parsing pickup result from robot: " + pickupInResult);;
						}
					}
					
					// Data to send response to driver app when having session id
					DriverDataRes driverData = new DriverDataRes();
					driverData.setPickupHistoryId(pickupHistory.getId());
					driverData.setContainerNo(pickupHistory.getContainerNo());
					driverData.setYardPosition(pickupHistory.getBlock()+"-"+pickupHistory.getBay()
					+"-"+pickupHistory.getLine()+"-"+pickupHistory.getTier());
					if (pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY ||
							pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_DROP_EMPTY) {
						driverData.setFe("E");
					} else {
						driverData.setFe("F");
					}
					if (pickupHistory.getStatus().equals(EportConstants.PICKUP_HISTORY_STATUS_GATE_IN)) {
						driverData.setResult(BusinessConsts.PASS);
					} else {
						driverData.setResult(BusinessConsts.FAIL);
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
					if (pickupOutResult != null) {
						try {
							List<PickupRobotResult> pickupRobotResults = getListRobotResultPickupFromString(pickupOutResult);
							PickupHistory pickupUpdated = updatePickupHistoryData(pickupRobotResults, pickupHistory);
							if (pickupUpdated != null) {
								pickupHistory = pickupUpdated;
							}
						} catch (Exception e) {
							logger.error("Error when update pickup result from robot: " + pickupOutResult);;
						}
						
						pickupHistory.setGateinDate(new Date());
						pickupHistory.setStatus(EportConstants.PICKUP_HISTORY_STATUS_GATE_IN);
						pickupHistoryService.updatePickupHistory(pickupHistory);
						
						// Data to send response to driver app when having session id
						DriverDataRes driverData = new DriverDataRes();
						driverData.setPickupHistoryId(pickupHistory.getId());
						driverData.setContainerNo(pickupHistory.getContainerNo());
						driverData.setYardPosition(pickupHistory.getBlock()+"-"+pickupHistory.getBay()
						+"-"+pickupHistory.getLine()+"-"+pickupHistory.getTier());
						if (pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_PICKUP_EMPTY ||
								pickupHistory.getShipment().getServiceType() == EportConstants.SERVICE_DROP_EMPTY) {
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
			}
			
			// Send result gate in to driver
			try {
				driverRes.setData(driverDataRes);
				driverRes.setResult(BusinessConsts.PASS);
				if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
					responseDriver(driverRes, gateInFormData.getSessionId());
				}
			} catch (Exception e) {
				logger.error("Error send result gate in for driver: " + e);
			}
			
			// Send result gate in to smart app
			try {
				responseSmartGate(gateInFormData.getGateId(), BusinessConsts.SUCCESS, MessageHelper.getMessage(MessageConsts.E0028));
			} catch (Exception e) {
				logger.error("Error send result gate in for smart app: " + e);
			}
			
			// Send result gate in to app notification gate
			try {
				sendMessageToNotificationApp(driverRes, processOrder);
			} catch (Exception e) {
				logger.error("Error when send message mqtt to app notification: " + e);
			}
		
		} else if("position_failed".equalsIgnoreCase(result)) {
			//gateInFormData 
			int yardPositionCount = 0;
			int count = 0;
			for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
				if (!checkPickupHistoryHasPosition(pickupHistory)) {
					
					String message = "Container " + pickupHistory.getContainerNo() + " chưa có tọa độ, đang thực hiện yêu cầu mc cấp tọa độ.";
					mqttService.sendNotificationToGate(pickupHistory.getTruckNo(), message);
					
					if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
						mqttService.sendNotificationOfProcessForDriver(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, gateInFormData.getSessionId(), message);
					}
					
					Map<String, Object> map = new HashMap<>();
					map.put("pickupHistoryId", pickupHistory.getId().toString());
					String msg = new Gson().toJson(map);
					try {
						logger.debug("Received Position failed from Robot. Send MC request: " + msg);
						mqttService.sendMessageToMcAppWindow(pickupHistory.getId());
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
						if (checkPickupHistoryHasPosition(pickupHistory)) {
							
							yardPositionCount++;
							logger.debug("Pickup has been updated position. Continue to gate-in");
							break;
						}
					}
					count++;
				} else {
					yardPositionCount++;
					count++;
				}
				// Set bay to fit format of catos (02 -> 01/02)
				if (StringUtils.isNotEmpty(pickupHistory.getBay())) {
					try {
						Integer bay = Integer.parseInt(pickupHistory.getBay());
						
						// Check bay is even then need to add odd number before current bay (02 -> even -> 01/02)
						if (bay%2 == 0) {
							Integer oddNumber = bay - 1;
							String oddString = "";
							if (oddNumber < 10) {
								oddString = "0" + oddNumber.toString();
							} else {
								oddString = oddNumber.toString();
							}
							pickupHistory.setBay(oddString+"/"+pickupHistory.getBay());
						} else {
							Integer evenNumber = bay + 1;
							String evenString = "";
							if (evenNumber < 10) {
								evenString = "0" + evenNumber.toString();
							} else {
								evenString = evenNumber.toString();
							}
							pickupHistory.setBay(pickupHistory.getBay() + "/" + evenString);
						}
					} catch (Exception e) {
						logger.error("Failed to parsing bay string to Integer: " + pickupHistory.getBay());
					}
				}
				gateInFormData.getPickupIn().set(count-1, pickupHistory);
			}
			
			// Get yard position success then send new process order to robot to gate in again
			if (yardPositionCount == gateInFormData.getPickupIn().size()) {
				
				String message = "Đã có tọa độ đầy đủ, tiếp tục làm lệnh gate in";
				if (StringUtils.isNotEmpty(gateInFormData.getSessionId())) {
					mqttService.sendNotificationOfProcessForDriver(BusinessConsts.IN_PROGRESS, pickupInResult, gateInFormData.getSessionId(), message);
				}
				mqttService.sendNotificationToGate(gateInFormData.getTruckNo(), message);
				
				// re-try gate order with yard position
				// Create new process order
				ProcessOrder processOrderNew = new ProcessOrder();
				processOrderNew.setShipmentId(processOrder.getShipmentId());
				processOrderNew.setServiceType(8);
				processOrderNew.setLogisticGroupId(processOrder.getLogisticGroupId());
				processOrderNew.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
				processOrderService.insertProcessOrder(processOrderNew);
				
				// Set new process order just created to update 
				gateInFormData.setReceiptId(processOrder.getId());
				for (PickupHistory pickupHistory : gateInFormData.getPickupIn()) {
					pickupHistory.setProcessOrderId(processOrderNew.getId());
					pickupHistoryService.updatePickupHistory(pickupHistory);
				}
				
				// Parse data to string to send to robot
				String msg = new Gson().toJson(gateInFormData);
				
				// Set up robot param to find robot provide gate in service
				SysRobot robot = new SysRobot();
				robot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
				robot.setIsGateInOrder(true);
				robot.setDisabled(false);
				SysRobot sysRobot = robotService.findFirstRobot(robot);
				
				// If find robot success then send to otherwise put process order on hold
				if (sysRobot != null) {
					processOrderNew.setStatus(EportConstants.PROCESS_ORDER_STATUS_PROCESSING);
					processOrderNew.setRobotUuid(sysRobot.getUuId());
					processOrderNew.setProcessData(msg);
					processOrderService.updateProcessOrder(processOrderNew);
					robotService.updateRobotStatusByUuId(sysRobot.getUuId(), EportConstants.ROBOT_STATUS_BUSY);
					logger.debug("Send request to robot: " + sysRobot.getUuId() + ", content: " + msg);
					try {
						mqttService.sendMessageToRobot(msg, sysRobot.getUuId());
					} catch (MqttException e) {
						logger.error("Error send gate order to robot: " + e);
					}
				} else {
					logger.debug("No GateRobot is available: " + msg);
				}
			}
			
			// Set current process order to failed 
			processOrder.setResult("F");
			
		} else {
			
			// Case gate-in failed 
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
					if (!pickupHistory.getJobOrderFlg()) {
						ShipmentDetail shipmentDetail = new ShipmentDetail();
						shipmentDetail.setId(pickupHistory.getShipmentDetailId());
						shipmentDetail.setFinishStatus("E");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}
				}
			}
			
			driverRes.setStatus(BusinessConsts.FINISH);
			driverRes.setResult(BusinessConsts.FAIL);
			driverRes.setMsg(MessageHelper.getMessage("Có lỗi xảy ra trong quá trình gate in. <br/>Vui lòng thử lại hoặc vào bàn cân để được hỗ trợ."));
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
			
			// Send result gate in to app notification gate
			try {
				sendMessageToNotificationApp(driverRes, processOrder);
			} catch (Exception e) {
				logger.error("Error when send message mqtt to app notification: " + e);
			}
			processOrder.setResult("F");
			history.setResult("F");
		}
		
		// Update process order (S or F)
		processOrderService.updateProcessOrder(processOrder);
		
		// Update if history has id (possible to update finish time) History robot
		// Insert when history doesn't have id (that's mean something went wrong and couldn't get the id
		if (history.getId() == null) {
			processHistoryService.insertProcessHistory(history);
		} else {
			processHistoryService.updateProcessHistory(history);
		}
		 
	}
	
	
	/**
	 * Response check in result to smart gate
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
	 * Response check in result to driver
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
			sysNotification.setNotifyLevel(EportConstants.NOTIIFCATION_LEVEL_INSTANT);
			sysNotification.setContent("Xác thực tại cổng thành công cho container  " + shipmentDetail.getContainerNo());
			sysNotification.setNotifyLink("");
			sysNotification.setStatus(EportConstants.NOTIFICATION_STATUS_ACTIVE);
			sysNotificationService.insertSysNotification(sysNotification);
			
			// Notification receiver
			SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
			sysNotificationReceiver.setUserId(pickupHistory.getDriverId());
			sysNotificationReceiver.setNotificationId(sysNotification.getId());
			sysNotificationReceiver.setUserType(EportConstants.USER_TYPE_DRIVER);
			sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);
			
			// TODO : Send notification by fire-base push notification
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
	
	/**
	 * Set yard position (block-bay-row-tier) 
	 * and update container again for case receive cont with job order no
	 * 
	 * @param pickupRobotResults
	 * @param pickupHistory
	 * @return
	 */
	@Transactional
	private PickupHistory updatePickupHistoryData(List<PickupRobotResult> pickupRobotResults, PickupHistory pickupHistory) {
		// Iterate pickup robot result (list pickup result (include pickup history id, container no, yard position, result: success, fail)
		for (PickupRobotResult pickupRobotResult: pickupRobotResults) {
			
			// Check if Said pickup history is match with result robot return to update 
			if (pickupRobotResult.getId().equals(pickupHistory.getId())) {
				
				// Check if pickup history is pickup by job order no to update container no and shipment detail id
				if (pickupHistory.getJobOrderFlg() && StringUtils.isNotEmpty(pickupRobotResult.getContainerNo())) {
					
					// Update container no 
					pickupHistory.setContainerNo(pickupRobotResult.getContainerNo());
					
					// Get shipment detail list by shipment id from pickup history
					ShipmentDetail shipmentDetailParam = new ShipmentDetail();
					shipmentDetailParam.setShipmentId(pickupHistory.getShipmentId());
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
					
					// Iterate list to find shipment detail id match container no with robot result
					// Set shipment detail id for pickup history 
					for(ShipmentDetail shipmentDetail : shipmentDetails) {
						if (shipmentDetail.getContainerNo().equalsIgnoreCase(pickupRobotResult.getContainerNo())) {
							pickupHistory.setShipmentDetailId(shipmentDetail.getId());
							break;
						}
					}
				}
				
				// Check if robot result data has yard position to update back ward for pick up history
				// Update this information for driver can see it in app mobile
				if (StringUtils.isNotEmpty(pickupRobotResult.getYardPosition())) {
					
					// The yard position data in format (block-bay-row-tier)
					String[] yardPositionArr = pickupRobotResult.getYardPosition().split("-");
					if (yardPositionArr.length > 3) {
						pickupHistory.setBlock(yardPositionArr[0]);
						pickupHistory.setBay(yardPositionArr[1]);
						pickupHistory.setLine(yardPositionArr[2]);
						pickupHistory.setTier(yardPositionArr[3]);
					}
				}
				
				// After update all the information robot return above
				// Update pickup history and corresponding shipment detail
				// Initialize shipment detail with id to update 
				ShipmentDetail shipmentDetail = new ShipmentDetail();
				shipmentDetail.setId(pickupHistory.getShipmentDetailId());
				shipmentDetail.setShipmentId(pickupHistory.getShipmentId());
				
				// Check result up gate in order for said pickup history
				if (EportConstants.GATE_RESULT_SUCCESS.equalsIgnoreCase(pickupRobotResult.getResult())) {
					// Sucess case update status pickup is gate in
					pickupHistory.setStatus(EportConstants.PICKUP_HISTORY_STATUS_GATE_IN);
					// Update status shipment detail finished
					shipmentDetail.setFinishStatus("Y");
				} else {
					
					// Case fail to gate in update shipment detail finish status Error
					shipmentDetail.setFinishStatus("E");
				}
				
				pickupHistoryService.updatePickupHistory(pickupHistory);
				if (pickupHistory.getShipmentDetailId() != null) {
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
				
				// Update shipment status if all shipment detail is finish
				shipmentDetail.setId(null);
				shipmentDetail.setFinishStatus("N");
				if (CollectionUtils.isEmpty(shipmentDetailService.selectShipmentDetailList(shipmentDetail))) {
					Shipment shipment = new Shipment();
					shipment.setStatus(EportConstants.SHIPMENT_STATUS_FINISH);
					shipment.setId(pickupHistory.getShipmentId());
					shipmentService.updateShipment(shipment);
				}
				
				// Send notification to driver after done update pickup history, shipment detail and shipment
				sendNotificationToDriver(pickupHistory, shipmentDetail);
				return pickupHistory;
			}
		}
		return null;
	}
	
	/**
	 * Parsing json string of robot result data to List robot result object
	 * 
	 * @param pickupResult
	 * @return List robot result object
	 */
	private List<PickupRobotResult> getListRobotResultPickupFromString(String pickupResult) {                        
		Type listType = new TypeToken<ArrayList<PickupRobotResult>>() {}.getType();
		ArrayList<PickupRobotResult> pickupRobotResults = new Gson().fromJson(pickupResult , listType);
		return pickupRobotResults;
	}
	
	/**
	 * Send message result of gate in order for gate staff
	 * 
	 * @param driverRes
	 * @throws MqttException
	 */
	public void sendMessageToNotificationApp(DriverRes driverRes, ProcessOrder processOrder) throws MqttException {
		String truckNo = "";
		String chassisNo = "";
		if (CollectionUtils.isNotEmpty(driverRes.getData())) {
			truckNo = driverRes.getData().get(0).getTruckNo();
			chassisNo = driverRes.getData().get(0).getChassisNo();
		}
		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setType(EportConstants.APP_USER_TYPE_GATE);
		notificationReq.setLink(configService.selectConfigByKey("domain.admin.name") + EportConstants.URL_GATE);
		notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
		if (BusinessConsts.PASS.equals(driverRes.getResult())) {
			notificationReq.setTitle("ePort: Thông báo làm lệnh gate in thành công.");
			notificationReq.setMsg("Làm lệnh gate in thành công cho xe: " + truckNo + " - " + chassisNo + ", Số lệnh " + processOrder.getId());
		} else {
			notificationReq.setTitle("ePort: Thông báo làm lệnh gate in thất bại.");
			notificationReq.setMsg("Làm lệnh gate in thất bại cho xe: " + truckNo + " - " + chassisNo + ", Số lệnh " + processOrder.getId());
		}
		String msg = new Gson().toJson(notificationReq);
		mqttService.publish(MqttConsts.NOTIFICATION_GATE_TOPIC, new MqttMessage(msg.getBytes()));
	}
}
