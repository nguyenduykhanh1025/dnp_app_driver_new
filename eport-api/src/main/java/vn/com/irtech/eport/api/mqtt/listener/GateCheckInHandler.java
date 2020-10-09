package vn.com.irtech.eport.api.mqtt.listener;

import java.util.ArrayList;
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

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.DriverDataRes;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateInFormData;
import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class GateCheckInHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(GateCheckInHandler.class);

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
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
					logger.error("Error while process CheckIn message", e);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Begin process handle the message
	 * 
	 * @param topic
	 * @param message
	 * @throws Exception
	 */
	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload());
		logger.info(String.format("Receive message topic: [%s], content: %s", topic, messageContent));
		
		Map<String, Object> map = new Gson().fromJson(messageContent, Map.class);
		
		String result = map.get("result") == null ? null : map.get("result").toString();
		
		String gateInData = map.get("gateInData") == null ? null : map.get("gateInData").toString();
		
		GateNotificationCheckInReq gateNotificationCheckInReq = new Gson().fromJson(gateInData, GateNotificationCheckInReq.class);
		if (gateNotificationCheckInReq == null) {
			return;
		}
		
		if ("reject".equals(result)) {
			String msg = "Thông tin xe/container không trùng khớp với thực tế. <br/>Vui lòng kiểm tra và thực hiện Check In lại hoặc gặp bàn cân để xử lý.";
			if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getSessionId())) {
				mqttService.sendNotificationOfProcessForDriver(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, gateNotificationCheckInReq.getSessionId(), msg);
			}
			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, gateNotificationCheckInReq.getSessionId(), msg);
			return;
		} 
		
		if ("accept".equals(result)) {
			String msg = "Chấp nhận yêu cầu gate in, chuẩn bị làm lệnh gate in.";
			if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getSessionId())) {
				mqttService.sendNotificationOfProcessForDriver(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, gateNotificationCheckInReq.getSessionId(), msg);
			}
			sendGateInOrderToRobot(gateNotificationCheckInReq);
		}
		
		
	}
	
	/**
	 * Prepare data to make gate in order, find robot gate in available, send order to robot
	 * 
	 * @param gateNotificationCheckInReq
	 */
	private void sendGateInOrderToRobot(GateNotificationCheckInReq gateNotificationCheckInReq) {
		try {
			PickupHistory pickupHistoryParam = new PickupHistory();
			pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
			pickupHistoryParam.setTruckNo(gateNotificationCheckInReq.getTruckNo());
			pickupHistoryParam.setChassisNo(pickupHistoryParam.getChassisNo());
			pickupHistoryParam.setGatePass(pickupHistoryParam.getGatePass());
			List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
			
			if (CollectionUtils.isNotEmpty(pickupHistories)) {
				
				PickupHistory pickupHistoryGeneral = pickupHistories.get(0);
				GateInFormData gateInFormData = new GateInFormData();
				List<PickupHistory> pickupIn = new ArrayList<>();
				List<PickupHistory> pickupOut = new ArrayList<>();

				ProcessOrder processOrder = new ProcessOrder();
				processOrder.setShipmentId(pickupHistoryGeneral.getShipmentId());
				processOrder.setServiceType(EportConstants.SERVICE_GATE_IN);
				processOrder.setLogisticGroupId(pickupHistoryGeneral.getLogisticGroupId());
				processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
				processOrderService.insertProcessOrder(processOrder);
				
				// Begin interate pickup history list get by driver id
				for (PickupHistory pickupHistory : pickupHistories) {
					
					// Get service type of pickup history
					Integer serviceType = pickupHistory.getShipment().getServiceType();
					
					if (EportConstants.SERVICE_PICKUP_FULL == serviceType || EportConstants.SERVICE_PICKUP_EMPTY == serviceType) {
						pickupOut.add(pickupHistory);
					} else {
						if (pickupHistory.getBlock() == null) {
							pickupHistory.setBlock("");
						}
						if (pickupHistory.getArea() == null) {
							pickupHistory.setArea("");
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
						
						pickupIn.add(pickupHistory);
					}
					
					pickupHistory.setProcessOrderId(processOrder.getId());
					pickupHistoryService.updatePickupHistory(pickupHistory);
				}
				
				
				if (pickupIn.size() == 0) {
					gateInFormData.setPickupOut(pickupOut);
					gateInFormData.setModule("OUT");
					gateInFormData.setContNumberOut(pickupOut.size());
				} else if (pickupOut.size() == 0) {
					gateInFormData.setPickupIn(pickupIn);
					gateInFormData.setModule("IN");
					gateInFormData.setContNumberIn(pickupIn.size());
				} else {
					gateInFormData.setPickupIn(pickupIn);
					gateInFormData.setPickupOut(pickupOut);
					gateInFormData.setModule("INOUT");
					gateInFormData.setContNumberIn(pickupIn.size());
					gateInFormData.setContNumberOut(pickupOut.size());
				}	
				gateInFormData.setGatePass(gateNotificationCheckInReq.getGatePass());
				gateInFormData.setTruckNo(gateNotificationCheckInReq.getTruckNo());
				gateInFormData.setChassisNo(gateNotificationCheckInReq.getChassisNo());
				Integer gross = gateNotificationCheckInReq.getWeight() - gateNotificationCheckInReq.getDeduct();
				gateInFormData.setWgt(gross.toString());
				gateInFormData.setSessionId(gateNotificationCheckInReq.getSessionId());
				gateInFormData.setGateId(gateInFormData.getGateId());
				gateInFormData.setReceiptId(processOrder.getId());
				
				String msg = new Gson().toJson(gateInFormData);
				SysRobot robot = new SysRobot();
				robot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
				robot.setIsGateInOrder(true);
				robot.setDisabled(false);
				SysRobot sysRobot = robotService.findFirstRobot(robot);
				if (sysRobot != null) {
					processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_PROCESSING);
					processOrder.setRobotUuid(sysRobot.getUuId());
					processOrder.setProcessData(msg);
					processOrderService.updateProcessOrder(processOrder);
					robotService.updateRobotStatusByUuId(sysRobot.getUuId(), EportConstants.ROBOT_STATUS_BUSY);
					logger.debug("Send request to robot: " + sysRobot.getUuId() + ", content: " + msg);
					mqttService.sendMessageToRobot(msg, sysRobot.getUuId());
					
					String message = "Đang thực hiện làm lệnh gate in";
					if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getSessionId())) {
						mqttService.sendNotificationOfProcessForDriver(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, gateNotificationCheckInReq.getSessionId(), message);
					}
					mqttService.sendNotificationToGate(gateNotificationCheckInReq.getTruckNo(), message);
				} else {
					logger.debug("No GateRobot is available: " + msg);
				}
			}
		} catch (Exception e) {
			logger.error("Error when send order gate in: " + e);
		}
	}
}
