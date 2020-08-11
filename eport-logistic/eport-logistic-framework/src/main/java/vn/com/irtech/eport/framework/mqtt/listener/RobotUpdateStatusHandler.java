package vn.com.irtech.eport.framework.mqtt.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.framework.web.service.MqttService.EServiceRobot;
import vn.com.irtech.eport.framework.web.service.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotUpdateStatusHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotUpdateStatusHandler.class);

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

	@Override
	@Transactional
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

		String serviceTypes = "";

		if (isReceiveContFullOrder) {
			serviceTypes += 1 + ",";
		}
		if (isReceiveContEmptyOrder) {
			serviceTypes += 3 + ",";
		}
		if (isSendContFullOrder) {
			serviceTypes += 4 + ",";
		}
		if (isSendContEmptyOrder) {
			serviceTypes += 2 + ",";
		}
		if (isShiftingContOrder) {
			serviceTypes += 5 + ",";
		}
		if (isChangeVesselOrder) {
			serviceTypes += 6 + ",";
		}
		if (isCreateBookingOrder) {
			serviceTypes += 7 + ",";
		}
		if (isGateInOrder) {
			serviceTypes += 8 + ",";
		}
		if (isExtensionDateOrder) {
			serviceTypes += 9 + ",";
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
		if ("1".equals(status)) {
			try {
				String receiptIdStr = map.get("receiptId") == null ? null : map.get("receiptId").toString();
				Long receiptId = Long.parseLong(receiptIdStr);
				this.updateHistory(receiptId.toString(), uuId);
			} catch (Exception ex) {
				return;
			}
		} else if ("0".equals(status)) {
			List<ProcessOrder> processOrders = processOrderService.getProcessOrderByUuid(uuId);

			if (processOrders != null && !processOrders.isEmpty()) {
				for (ProcessOrder processOrder : processOrders) {
					if (processOrder != null) {
						processOrder.setStatus(0);
						processOrderService.updateProcessOrder(processOrder);
						try {
							List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrder.getId().toString());
							updateErrorShipmentDetail(shipmentDetails);
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
						

						// Send notification to logistics
						AjaxResult ajaxResult= null;
						ajaxResult = AjaxResult.error("Làm lệnh thất bại, quý khách vui lòng liên hệ với bộ phận OM để được hỗ trợ thêm.");
						webSocketService.sendMessage("/" + processOrder.getId() + "/response", ajaxResult);
		
						// Send notification for OM
						try {
							mqttService.sendNotification(NotificationCode.NOTIFICATION_OM, "Lỗi lệnh số " + processOrder.getId(), configService.getKey("domain.admin.name") + "/om/executeCatos/detail/" + processOrder.getId());
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
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

			// Find process order for robot
			ProcessOrder reqProcessOrder = processOrderService.findProcessOrderForRobot(serviceTypes);
			if (reqProcessOrder != null) {
				reqProcessOrder.setStatus(1);
				reqProcessOrder.setRobotUuid(sysRobot.getUuId());
				if (processOrderService.updateProcessOrder(reqProcessOrder) == 1) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setProcessOrderId(reqProcessOrder.getId());
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
					ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(reqProcessOrder, shipmentDetails);

					// Send order to robot
					status = "1";

					// check robot exists in db
					if (robotService.selectRobotByUuId(uuId) == null) {
						// insert robot to db
						robotService.insertRobot(sysRobot);
					} else {
						// update status of robot
						sysRobot.setStatus("1");
						robotService.updateRobotByUuId(sysRobot);
					}

					switch (reqProcessOrder.getServiceType()) {
						case 1:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_FULL, uuId);
							break;
						case 2:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_EMPTY, uuId);
							break;
						case 3:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_EMPTY, uuId);
							break;
						case 4:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_FULL, uuId);
							break;
						case 5:
							mqttService.publicMessageToDemandRobot(req, EServiceRobot.SHIFTING_CONT, uuId);
							break;
					}
				}
			}

		} else if ("2".equals(status)) {
			List<ProcessOrder> processOrders = processOrderService.getProcessOrderByUuid(uuId);
			if (processOrders != null && !processOrders.isEmpty()) {
				for (ProcessOrder processOrder : processOrders) {
					if (processOrder != null) {
						processOrder.setRobotUuid(null);
						processOrderService.updateProcessOrder(processOrder);
						try {
							List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrder.getId().toString());
							updateErrorShipmentDetail(shipmentDetails);
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
						// Send notification to logistics
						AjaxResult ajaxResult= null;
						ajaxResult = AjaxResult.error("Làm lệnh thất bại, quý khách vui lòng liên hệ với bộ phận OM để được hỗ trợ thêm.");
						webSocketService.sendMessage("/" + processOrder.getId() + "/response", ajaxResult);
		
						// Send notification for om
						try {
							mqttService.sendNotification(NotificationCode.NOTIFICATION_OM, "Lỗi lệnh số " + processOrder.getId(), configService.getKey("domain.admin.name") + "/om/executeCatos/detail/" + processOrder.getId());
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
					}
				}
			}

			// Send notification for IT
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

	private void updateHistory(String receiptId, String uuId) {
		Long id = Long.parseLong(receiptId);
		ProcessHistory processHistory = new ProcessHistory();
		processHistory.setProcessOrderId(id);
		processHistory.setRobotUuid(uuId);
		processHistory.setStatus(1); // START
		processHistory.setResult("S"); // RESULT SUCCESS
		processHistory.setCreateTime(new Date());
		processHistoryService.insertProcessHistory(processHistory);
	}

	@Transactional
	private void updateErrorShipmentDetail(List<ShipmentDetail> shipmentDetails) {
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetail.setProcessStatus("E");
			shipmentDetailService.updateShipmentDetail(shipmentDetail);
		}
	}
}
