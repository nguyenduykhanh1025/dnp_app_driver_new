package vn.com.irtech.eport.api.mqtt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.config.MqttConfig;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.mqtt.listener.CheckinHandler;
import vn.com.irtech.eport.api.mqtt.listener.GateCheckInHandler;
import vn.com.irtech.eport.api.mqtt.listener.GatePassHandler;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class MqttService implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

	public static String BASE_TOPIC;
	public static String ROBOT_TOPIC = "eport/robot/+/request";

	@Autowired
	private CheckinHandler checkinHandler;
	
	@Autowired
	private GatePassHandler gatePassHandler;
	
	@Autowired
	private GateCheckInHandler gateCheckInHandler;
	
	@Autowired
	private IProcessOrderService processOrderService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private ISysRobotService robotService;
	
	@Autowired
	private ISysConfigService sysConfigService;

	@Value("${mqtt.root:'eport'}")
	public void setBaseTopic(String baseTopic) {
		BASE_TOPIC = baseTopic;
	}

	@Autowired
	private MqttAsyncClient mqttClient;
	@Autowired
	private MqttConfig config;
	private Object connectLock = new Object();

	@PostConstruct
	public void connect() throws MqttException {
		// set callback
		mqttClient.setCallback(this);
		MqttConnectOptions clientOptions = new MqttConnectOptions();
		clientOptions.setCleanSession(true);
		clientOptions.setMaxInflight(config.getMaxInFlight()); // default is 10
		if (StringUtils.isNotBlank(config.getUsername())) {
			clientOptions.setUserName(config.getUsername());
		}
		if (StringUtils.isNotBlank(config.getPassword())) {
			clientOptions.setPassword(config.getPassword().toCharArray());
		}
		// checkConnection
		if (!mqttClient.isConnected()) {
			synchronized (connectLock) {
				while (!mqttClient.isConnected()) {
					logger.debug("[{}] MQTT broker connection attempt!", mqttClient.getServerURI());
					try {
						mqttClient.connect(clientOptions, null, new IMqttActionListener() {
							@Override
							public void onSuccess(IMqttToken iMqttToken) {
								logger.info("[{}] MQTT broker connection established!", mqttClient.getServerURI());
								try {
									subscribeToTopics();
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(IMqttToken iMqttToken, Throwable e) {
								logger.warn("[{}] MQTT broker connection faied! {}", mqttClient.getServerURI(),
										e.getMessage(), e);
							}
						}).waitForCompletion();
					} catch (MqttException e) {
						logger.error("MQTT broker connection failed!" + e.getMessage());
						if (!mqttClient.isConnected()) {
							try {
								Thread.sleep(3000); // 3s
							} catch (InterruptedException e1) {
							}
						}
					}
				}
			}

		}
	}

	private void subscribeToTopics() throws MqttException {
		List<IMqttToken> tokens = new ArrayList<>();
		tokens.add(mqttClient.subscribe(MqttConsts.SMART_GATE_REQ_TOPIC, 1, checkinHandler));
		tokens.add(mqttClient.subscribe(MqttConsts.GATE_ROBOT_RES_TOPIC, 1, gatePassHandler));
		tokens.add(mqttClient.subscribe(MqttConsts.NOTIFICATION_GATE_RES_TOPIC, 1, gateCheckInHandler));
		for (IMqttToken token : tokens) {
			token.waitForCompletion();
		}
	}


	@PreDestroy
	public void disconnect() {
		try {
			if (mqttClient != null && mqttClient.isConnected()) {
				IMqttToken token = mqttClient.disconnect();
				token.waitForCompletion(5000); // 5s
				mqttClient.close();
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		logger.warn("Lost connection to MQTT server", cause);
		while (true) {
			try {
				logger.info("Attempting to reconnect to MQTT server");
				reconnect();
				logger.info("Reconnected to MQTT server, resuming");
				return;
			} catch (MqttException e) {
				logger.warn("Reconnect failed, retrying in 3 seconds", e);
			}
			try {
				Thread.sleep(3000); // wait 3s before reconnect
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("Message arrived: " + topic + "," + message.getPayload().toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("Published message!");

	}

	private void reconnect() throws MqttException {
		if (!mqttClient.isConnected()) {
			connect();
		}
	}

	public void publish(String topic, MqttMessage msg) throws MqttException {
		logger.info("Send topic: " + topic);
		try {
			mqttClient.publish(topic, msg, null, new IMqttActionListener() {
				@Override
				public void onSuccess(IMqttToken iMqttToken) {
				}

				@Override
				public void onFailure(IMqttToken iMqttToken, Throwable e) {
				}
			});
		} catch (MqttException e) {
			logger.warn(e.getMessage());
			throw e;
		}
	}

	public void sendMessageToMc(String message) throws MqttException {
		this.publish(MqttConsts.MC_REQ_TOPIC, new MqttMessage(message.getBytes()));
	}

	public void sendMessageToRobot(String message, String gateId) throws MqttException {
		this.publish(MqttConsts.GATE_ROBOT_REQ_TOPIC.replace("+", gateId), new MqttMessage(message.getBytes()));
	}
	
	public enum EServiceRobot {
		RECEIVE_CONT_FULL, RECEIVE_CONT_EMPTY, SEND_CONT_FULL, SEND_CONT_EMPTY
	}
	
	@Transactional
	public boolean publishMessageToRobot(ServiceSendFullRobotReq payLoad, EServiceRobot serviceRobot) throws MqttException {
		SysRobot sysRobot = this.getAvailableRobot(serviceRobot);
		if (sysRobot == null) {
			return false;
		}
		String msg = new Gson().toJson(payLoad);
		String topic = ROBOT_TOPIC.replace("+", sysRobot.getUuId());
		publish(topic, new MqttMessage(msg.getBytes()));
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setId(payLoad.processOrder.getId());
		processOrder.setRobotUuid(sysRobot.getUuId()); // robot uuid in charge of process order
		processOrder.setStatus(1); // on progress
		processOrderService.updateProcessOrder(processOrder);
		robotService.updateRobotStatusByUuId(sysRobot.getUuId(), "1");
		return true;
	}
	
	/**
	 * Get a robot already to execute service
	 * 
	 * @param service
	 * @return
	 */
	public SysRobot getAvailableRobot(EServiceRobot serviceRobot) {
		SysRobot sysRobot = new SysRobot();
		sysRobot.setStatus("0");
		switch (serviceRobot) {
			case RECEIVE_CONT_FULL:
				sysRobot.setIsReceiveContFullOrder(true);
				break;
			case RECEIVE_CONT_EMPTY:
				sysRobot.setIsReceiveContEmptyOrder(true);
				break;
			case SEND_CONT_FULL:
				sysRobot.setIsSendContFullOrder(true);
				break;
			case SEND_CONT_EMPTY:
				sysRobot.setIsSendContEmptyOrder(true);
				break;
		}
		sysRobot.setDisabled(false);
		return robotService.findFirstRobot(sysRobot);
	}
	
	public void sendMessageToMcAppWindow(Long pickupHistoryId) throws MqttException {
		// Send message to mc on web
		Map<String, Object> map = new HashMap<>();
		map.put("pickupHistoryId", pickupHistoryId.toString());
		String msg = new Gson().toJson(map);
		this.publish(MqttConsts.MC_REQ_TOPIC, new MqttMessage(msg.getBytes()));
		
		// Get data from pickup history id to send req to mc on app
		PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(pickupHistoryId);
		
		// Set data
		ShipmentDetail shipmentDetail = pickupHistory.getShipmentDetail();
		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setTitle("ePort: Yêu cầu cấp tọa độ.");
		notificationReq.setMsg("Có yêu cầu tọa độ cho container: " + pickupHistory.getShipment().getOpeCode() +
				" - " + pickupHistory.getContainerNo() + " - " + shipmentDetail.getSztp() + " - " +
				shipmentDetail.getVslNm() + shipmentDetail.getVoyNo() + " - " + shipmentDetail.getDischargePort());
		notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
		notificationReq.setLink(sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
		notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
		Map<String, Object> data = new HashMap<>();
		data.put("id", pickupHistoryId);
		data.put("opeCode", pickupHistory.getShipment().getOpeCode());
		data.put("containerNo", pickupHistory.getContainerNo());
		data.put("wgt", shipmentDetail.getWgt());
		data.put("sztp", shipmentDetail.getSztp());
		data.put("userVoy", shipmentDetail.getVslNm()+shipmentDetail.getVoyNo());
		data.put("dischargePort", shipmentDetail.getDischargePort());
		data.put("cargoType", shipmentDetail.getCargoType());
		notificationReq.setData(data);
		
		String req = new Gson().toJson(notificationReq);
		this.publish(MqttConsts.NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
	}
}
