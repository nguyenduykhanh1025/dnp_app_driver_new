package vn.com.irtech.eport.api.mqtt.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.DriverDataRes;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.PickupHistoryDataRes;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Component
public class CheckinHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(CheckinHandler.class);

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	// time wait mc input postion
	private static final Long TIME_OUT_WAIT_MC = 6000L;
	
	private static final Integer RETRY_WAIT_MC = 20;
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		DriverRes driverRes;

		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message content : " + messageContent);

		CheckinReq checkinReq = null;
		String gateId = topic.replace(MqttConsts.BASE_TOPIC + "/gate/", "").replace("/request", "");

		try {
			checkinReq = new Gson().fromJson(messageContent, CheckinReq.class);
		} catch (Exception ex) {
			logger.warn(ex.getMessage());
			responseSmartGate(gateId, BusinessConsts.FAIL);
			return;
		}

		try {
			checkinReq = new Gson().fromJson(messageContent, CheckinReq.class);

			driverRes = new DriverRes();
			driverRes.setStatus(BusinessConsts.IN_PROGRESS);
			responseDriver(driverRes, checkinReq.getSessionId());
			
			List<DriverDataRes> data = getDriverDataResponse(checkinReq);
			
			// TODO: Call and wait robot
			
			driverRes.setStatus(BusinessConsts.FINISH);
			driverRes.setData(data);
			responseDriver(driverRes, checkinReq.getSessionId());
			
			responseSmartGate(gateId, BusinessConsts.SUCCESS);
			
		} catch (Exception e) {
			driverRes = new DriverRes();
			driverRes.setStatus(BusinessConsts.FINISH);
			driverRes.setResult(BusinessConsts.FAIL);
			driverRes.setMsg(e.getMessage());
			responseDriver(driverRes, checkinReq.getSessionId());

			responseSmartGate(gateId, BusinessConsts.FAIL);
		}
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
	 * Request MC input position for container
	 * 
	 * @param pickupHistoryId
	 */
	private void requestMC(Long pickupHistoryId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("pickupHistoryId", pickupHistoryId.toString());
		String msg = new Gson().toJson(map);
		mqttService.sendMessageToMc(msg);
	}

	/**
	 * Get yard position from pickup
	 * @param pickupHistory
	 */
	private String getYardPostion(PickupHistory pickupHistory) {
		if (!StringUtils.isEmpty(pickupHistory.getArea())) {
			return pickupHistory.getArea();
		}
		
		String yardPosition = "";
		yardPosition += (pickupHistory.getBlock() != null)?pickupHistory.getBlock():StringUtils.EMPTY;
		yardPosition += "-" + ((pickupHistory.getBay() != null)?pickupHistory.getBay():StringUtils.EMPTY);
		yardPosition += "-" + ((pickupHistory.getLine() != null)?pickupHistory.getLine():StringUtils.EMPTY);
		yardPosition += "-" + ((pickupHistory.getTier() != null)?pickupHistory.getTier():StringUtils.EMPTY);
		return yardPosition;
	}
	
	/**
	 * get data response for driver
	 * @param checkinReq
	 * @return
	 * @throws Exception
	 */
	private List<DriverDataRes> getDriverDataResponse(CheckinReq checkinReq) throws Exception{
		List<DriverDataRes> result = new ArrayList<>();
		List<DriverDataRes> dataWithoutYardPostion = new ArrayList<>();
		
		for (PickupHistoryDataRes pickupHistoryDataRes : checkinReq.getData()) {
			PickupHistory pickupHistory = pickupHistoryService
					.selectPickupHistoryById(pickupHistoryDataRes.getPickupHistoryId());
			
			// pickup is not exist
			if (pickupHistory == null) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
			}
			
			DriverDataRes driverDataRes = new DriverDataRes();
			driverDataRes.setPickupHistoryId(pickupHistory.getId());
			driverDataRes.setContNo(pickupHistory.getContainerNo());
			driverDataRes.setSztp(pickupHistory.getShipmentDetail().getSztp());
			driverDataRes.setFe(pickupHistory.getShipmentDetail().getFe());
			driverDataRes.setTruckNo(pickupHistory.getTruckNo());
			driverDataRes.setChassisNo(pickupHistory.getChassisNo());
			

			// pickup has not postion
			if (pickupHistory.getStatus() == 0) {
				dataWithoutYardPostion.add(driverDataRes);
				
			}else {
				driverDataRes.setYardPosition(getYardPostion(pickupHistory));
			}
			
			result.add(driverDataRes);
		}
		
		if (!CollectionUtils.isEmpty(dataWithoutYardPostion)) {
			for (DriverDataRes data : dataWithoutYardPostion) {
				// Request MC input position
				requestMC(data.getPickupHistoryId());
			}
		} else {
			return result;
		}
		
		// wait MC
		for (int i = 1; i<= RETRY_WAIT_MC; i++) {
			logger.debug("Wait " + TIME_OUT_WAIT_MC  + " miliseconds");
			Thread.sleep(TIME_OUT_WAIT_MC);
			logger.debug("Check db");
			for (int j = 0; j < dataWithoutYardPostion.size(); j ++) {
				DriverDataRes data = dataWithoutYardPostion.get(j);
				PickupHistory pickupHistory = pickupHistoryService
						.selectPickupHistoryById(data.getPickupHistoryId());
				if (pickupHistory.getStatus() > 0) {
					data.setYardPosition(getYardPostion(pickupHistory));
					dataWithoutYardPostion.remove(j);
				}
			}
			
			if (dataWithoutYardPostion.size() == 0) {
				break;
			}
		}
		
		if (dataWithoutYardPostion.size() > 0) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0019));
		}
		
		return result;
	}
	
}
