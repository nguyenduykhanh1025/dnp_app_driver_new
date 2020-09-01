package vn.com.irtech.eport.logistic.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotUpdateStatusHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotUpdateStatusHandler.class);
	
	private static final String GATE_ROBOT_REQ_TOPIC = "eport/robot/gate/+/request";

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IProcessHistoryService processHistoryService;
	
	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired 
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private ConfigService configService;
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
	

	@Transactional
	private void processMessage(String topic, MqttMessage message) throws Exception {
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
		String ipAddress = map.get("ipAddress") == null ? null : map.get("ipAddress").toString();
		// Get services robot support
		Boolean isReceiveContFullOrder = "1".equals(
				map.get("isReceiveContFullOrder") == null ? null : map.get("isReceiveContFullOrder").toString());

		Boolean isReceiveContEmptyOrder = "1".equals(
				map.get("isReceiveContEmptyOrder") == null ? null : map.get("isReceiveContEmptyOrder").toString());

		Boolean isSendContFullOrder = "1"
				.equals(map.get("isSendContFullOrder") == null ? null : map.get("isSendContFullOrder").toString());

		Boolean isSendContEmptyOrder = "1"
				.equals(map.get("isSendContEmptyOrder") == null ? null : map.get("isSendContEmptyOrder").toString());

		Boolean isShiftingContOrder = "1"
				.equals(map.get("isShiftingContOrder") == null ? null : map.get("isShiftingContOrder").toString());
		
		Boolean isChangeVesselOrder = "1"
				.equals(map.get("isChangeVesselOrder") == null ? null : map.get("isChangeVesselOrder").toString());
		
		Boolean isCreateBookingOrder = "1"
				.equals(map.get("isCreateBookingOrder") == null ? null : map.get("isCreateBookingOrder").toString());
		
		Boolean isGateInOrder = "1"
				.equals(map.get("isGateInOrder") == null ? null : map.get("isGateInOrder").toString());
		
		Boolean isExtensionDateOrder = "1"
				.equals(map.get("isExtensionDateOrder") == null ? null : map.get("isExtensionDateOrder").toString());

		// Check Service robot support anh make into a string split by comma for query db
		String serviceTypes = "";
		if (isReceiveContFullOrder) {
			serviceTypes += EportConstants.SERVICE_PICKUP_FULL + ",";
		}
		if (isReceiveContEmptyOrder) {
			serviceTypes += EportConstants.SERVICE_PICKUP_EMPTY + ",";
		}
		if (isSendContFullOrder) {
			serviceTypes += EportConstants.SERVICE_DROP_FULL + ",";
		}
		if (isSendContEmptyOrder) {
			serviceTypes += EportConstants.SERVICE_DROP_EMPTY + ",";
		}
		if (isShiftingContOrder) {
			serviceTypes += EportConstants.SERVICE_SHIFTING + ",";
		}
		if (isChangeVesselOrder) {
			serviceTypes += EportConstants.SERVICE_CHANGE_VESSEL + ",";
		}
		if (isCreateBookingOrder) {
			serviceTypes += EportConstants.SERVICE_CREATE_BOOKING + ",";
		}
		if (isGateInOrder) {
			serviceTypes += EportConstants.SERVICE_GATE_IN + ",";
		}
		if (isExtensionDateOrder) {
			serviceTypes += EportConstants.SERVICE_EXTEND_DATE + ",";
		}
		
		if (serviceTypes.length() > 0) {
			serviceTypes = serviceTypes.substring(0, serviceTypes.length()-1);
		}
				
		SysRobot sysRobot = new SysRobot();
		sysRobot.setUuId(uuId);
		sysRobot.setStatus(status);
		sysRobot.setIpAddress(ipAddress);
		sysRobot.setIsReceiveContFullOrder(isReceiveContFullOrder);
		sysRobot.setIsReceiveContEmptyOrder(isReceiveContEmptyOrder);
		sysRobot.setIsSendContFullOrder(isSendContFullOrder);
		sysRobot.setIsSendContEmptyOrder(isSendContEmptyOrder);
		sysRobot.setIsShiftingContOrder(isShiftingContOrder);
		sysRobot.setIsChangeVesselOrder(isChangeVesselOrder);
		sysRobot.setIsCreateBookingOrder(isCreateBookingOrder);
		sysRobot.setIsGateInOrder(isGateInOrder);
		sysRobot.setIsExtensionDateOrder(isExtensionDateOrder);

		// if robot is busying
		if (EportConstants.ROBOT_STATUS_BUSY.equals(status)) {
			try {
				// Parsing process order id and save history time start of order
				Long receiptId = map.get("receiptId") == null ? 0L : Long.parseLong(map.get("receiptId").toString());
				ProcessOrder processOrder = processOrderService.selectProcessOrderById(receiptId);
				if(processOrder != null) {
					this.addProcessHistory(receiptId.toString(), uuId, processOrder.getServiceType());
				}
			} catch (Exception ex) {
				logger.error("Error when update history for order when start: " + ex);
				return;
			}
			// Robot is available
		} else if (EportConstants.ROBOT_STATUS_AVAILABLE.equals(status)) {
			
			// Get list process order has been assigned to robot to check if process order is failed when robot indicate available
			List<ProcessOrder> processOrders = processOrderService.getProcessingProcessOrderByUuid(uuId);

			// ProcessOrders is not empty -> there is error happen when robot indicate available
			if (processOrders != null && !processOrders.isEmpty()) {
				for (ProcessOrder processOrder : processOrders) {
					// Update status error for process was failed
					processOrder.setStatus(0); // TODO Vi sao update lai status=0? dung de chay lai?
					processOrderService.updateProcessOrder(processOrder);

					// Update error status for shipment detail 
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrder.getId().toString());
					updateErrorShipmentDetail(shipmentDetails);

					// Send notification to logistics
					AjaxResult ajaxResult= null;
					ajaxResult = AjaxResult.error("Làm lệnh thất bại, quý khách vui lòng liên hệ với bộ phận OM để được hỗ trợ thêm.");
					webSocketService.sendMessage("/" + processOrder.getId() + "/response", ajaxResult);

					// Send notification for OM
					try {
						mqttService.sendNotification(NotificationCode.NOTIFICATION_OM, "Lỗi lệnh số " + processOrder.getId(), configService.getKey("domain.admin.name") + "/om/executeCatos/detail/" + processOrder.getId());
					} catch (Exception e) {
						logger.warn("Error when send notification error order for om: " + e);
					}
				}
			}
			
			// check robot exists in db
			if (robotService.selectRobotByUuId(uuId) == null) {
				// insert robot to db
				robotService.insertRobot(sysRobot);
			} else {
				// update status of robot
				robotService.updateRobotByUuId(sysRobot);
			}

			// Find process order for this robot support serviceTypes
			ProcessOrder reqProcessOrder = processOrderService.findProcessOrderForRobot(serviceTypes);
			if (reqProcessOrder != null) {
				reqProcessOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_PROCESSING);
				reqProcessOrder.setRobotUuid(sysRobot.getUuId());
				// Update process order to processing
				if (processOrderService.updateProcessOrder(reqProcessOrder) == 1) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setProcessOrderId(reqProcessOrder.getId());
					// Get list of shipment details for current processOrder
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
					ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(reqProcessOrder, shipmentDetails);

					// update status of robot to BUZY
					sysRobot.setStatus(EportConstants.ROBOT_STATUS_BUSY);
					robotService.updateRobotByUuId(sysRobot);
					// Send message to robot
					switch (reqProcessOrder.getServiceType()) {
						case EportConstants.SERVICE_PICKUP_FULL:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_FULL, uuId);
							break;
						case EportConstants.SERVICE_DROP_EMPTY:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_EMPTY, uuId);
							break;
						case EportConstants.SERVICE_PICKUP_EMPTY:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_EMPTY, uuId);
							break;
						case EportConstants.SERVICE_DROP_FULL:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_FULL, uuId);
							break;
						case EportConstants.SERVICE_SHIFTING:
							sendShiftingOrderToRobot(reqProcessOrder, uuId);
							break;
						case EportConstants.SERVICE_CREATE_BOOKING:
							mqttService.publicBookingOrderToDemandRobot(reqProcessOrder, EServiceRobot.CREATE_BOOKING, uuId);
							break;
						case EportConstants.SERVICE_GATE_IN:
							sendGateInOrderToRobot(reqProcessOrder, sysRobot.getUuId());
							break;
						case EportConstants.SERVICE_CHANGE_VESSEL:
							sendChangeVesselOrderToRobot(reqProcessOrder, uuId);
							break;
						case EportConstants.SERVICE_EXTEND_DATE:
							sendExtendDateOrderToRobot(reqProcessOrder, uuId);
							break;
					}
				}
			}

			// Case robot indicate offline need to check process order has been assigned to robot before
		} else if (EportConstants.ROBOT_STATUS_OFFLINE.equals(status)) {
			List<ProcessOrder> processOrders = processOrderService.getProcessingProcessOrderByUuid(uuId);
			
			// If process order exists then there would be a error before, need to send notification to om
			if (processOrders != null && !processOrders.isEmpty()) {
				for (ProcessOrder processOrder : processOrders) {
					// Update failed process order and shipment detail
					if (processOrder != null) {
						processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);
						processOrder.setResult(EportConstants.PROCESS_ORDER_RESULT_FAILED);
						processOrderService.updateProcessOrder(processOrder);
						List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrder.getId().toString());
						updateErrorShipmentDetail(shipmentDetails);

						// Send notification to logistics
						AjaxResult ajaxResult= null;
						ajaxResult = AjaxResult.error("Làm lệnh thất bại!");
						webSocketService.sendMessage("/" + processOrder.getId() + "/response", ajaxResult);
						// Send notification for om
						try {
							mqttService.sendNotification(NotificationCode.NOTIFICATION_OM, "Lỗi lệnh số " + processOrder.getId(), configService.getKey("domain.admin.name") + "/om/executeCatos/detail/" + processOrder.getId());
						} catch (Exception e) {
							logger.warn("Error when send notification to om: " + e);
						}
					}
				}
			}
			// Something went wrong with robot bc offline Send notification for IT
			try {
				mqttService.sendNotification(NotificationCode.NOTIFICATION_IT, "Lỗi Robot " + sysRobot.getUuId(), configService.getKey("domain.admin.name") + "/system/robot/edit/" + sysRobot.getId());
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			// check robot exists in db
			if (robotService.selectRobotByUuId(uuId) == null) {
				// insert robot to db
				robotService.insertRobot(sysRobot);
			} else {
				// update status of robot
				robotService.updateRobotByUuId(sysRobot);
			}
		}	
	}

	/**
	 * Update history for robot base on process order and id of robot
	 * 
	 * @param receiptId
	 * @param uuId
	 */
	private void addProcessHistory(String receiptId, String uuId, int serviceType) {
		Long id = Long.parseLong(receiptId);
		ProcessHistory processHistory = new ProcessHistory();
		processHistory.setProcessOrderId(id);
		processHistory.setRobotUuid(uuId);
		processHistory.setServiceType(serviceType);
		processHistory.setStatus(EportConstants.PROCESS_HISTORY_STATUS_START);
		processHistory.setStartTime(new Date());
		processHistory.setCreateTime(new Date());
		processHistoryService.insertProcessHistory(processHistory);
	}

	/**
	 * Update Error status for shipment detail list
	 * 
	 * @param shipmentDetails
	 */
	@Transactional
	private void updateErrorShipmentDetail(List<ShipmentDetail> shipmentDetails) {
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessStatus("E");
			shipmentDetailService.updateShipmentDetail(shipmentDetail);
		}
	}
	
	/**
	 * Send gate in roder to robot when robot is available 
	 * The info to send gate in order is stored in process data under json string
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	private void sendGateInOrderToRobot(ProcessOrder processOrder, String uuid) {
		try {
			mqttService.publish(GATE_ROBOT_REQ_TOPIC.replace("+", uuid), new MqttMessage(processOrder.getProcessData().getBytes()));		
		} catch (MqttException e) {
			logger.error("Error when send order gate in: " + e);
		}
	}
	
	/**
	 * Send shifting order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendShiftingOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.SHIFTING_CONT, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting shifting order to robot: " + e);
		}
	}
	
	/**
	 * Send extend date order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendExtendDateOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.EXTENSION_DATE, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting extend date order to robot: " + e);
		}
	}
	
	/**
	 * Send process order change vessel that in queue waiting to execute
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendChangeVesselOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.CHANGE_VESSEL, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting change vessel order to robot: " + e);
		}
	}
}
