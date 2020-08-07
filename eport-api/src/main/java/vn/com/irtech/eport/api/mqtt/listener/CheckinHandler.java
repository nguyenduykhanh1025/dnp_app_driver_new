package vn.com.irtech.eport.api.mqtt.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.DriverDataRes;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateInFormData;
import vn.com.irtech.eport.api.form.MeasurementDataReq;
import vn.com.irtech.eport.api.form.PickupHistoryDataRes;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class CheckinHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(CheckinHandler.class);

	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired ISysRobotService robotService;

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
			
			// Response to driver for waiting in progress
			driverRes = new DriverRes();
			driverRes.setStatus(BusinessConsts.IN_PROGRESS);
			driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0020));
			responseDriver(driverRes, checkinReq.getSessionId());
			
			// Compare contNo, truckNo, chassisNo, weight 
			if (!validateDataWithInput(checkinReq)) {
				driverRes = new DriverRes();
				driverRes.setStatus(BusinessConsts.FINISH);
				driverRes.setResult(BusinessConsts.FAIL);
				driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
				responseDriver(driverRes, checkinReq.getSessionId());

				responseSmartGate(gateId, BusinessConsts.FAIL);
			} else {
				// Get yard position or area, request to mc if not
				List<DriverDataRes> data = getDriverDataResponse(checkinReq);
				
				// Call and wait robot
				if (CollectionUtils.isNotEmpty(data)) {
					data.get(0).setWgt(checkinReq.getInput().get(0).getWeight());
					if (!sendGateInOrderToRobot(data, checkinReq.getSessionId(), gateId)) {
						driverRes = new DriverRes();
						driverRes.setStatus(BusinessConsts.FINISH);
						driverRes.setResult(BusinessConsts.FAIL);
						driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
						responseDriver(driverRes, checkinReq.getSessionId());

						responseSmartGate(gateId, BusinessConsts.FAIL);
					}
				} else {
					driverRes = new DriverRes();
					driverRes.setStatus(BusinessConsts.FINISH);
					driverRes.setResult(BusinessConsts.FAIL);
					driverRes.setMsg(MessageHelper.getMessage(MessageConsts.E0021));
					responseDriver(driverRes, checkinReq.getSessionId());
				}
			}
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
			

			// pickup has not position
			if (!checkPickupHistoryHasPosition(pickupHistory)) {
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
				if (checkPickupHistoryHasPosition(pickupHistory)) {
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
	
	/**
	 * Validate data input from gate
	 * 
	 * @param checkinReq
	 * @return Boolean
	 */
	private Boolean validateDataWithInput(CheckinReq checkinReq) {
		int contValidate = checkinReq.getData().size();
		boolean validTruckNo = false;
		boolean validChasissNo = false;
		// TODO Check weight
		for (PickupHistoryDataRes pickupHistoryDataRes : checkinReq.getData()) {
			for (MeasurementDataReq measurementDataReq : checkinReq.getInput()) {
				if (pickupHistoryDataRes.getContNo().equalsIgnoreCase(measurementDataReq.getContNo())) {
					contValidate--;
				}
				if (pickupHistoryDataRes.getTruckNo().equalsIgnoreCase(measurementDataReq.getTruckNo())) {
					validTruckNo = true;
				}
				if (pickupHistoryDataRes.getChassisNo().equalsIgnoreCase(measurementDataReq.getChassisNo())) {
					validChasissNo = true;
				}
			}
		}
		if (contValidate <= 0 && validTruckNo && validChasissNo) {
			return true;
		}
		return false;
	}
	
	/**
	 * Send gate in order to robot
	 * 
	 * @param driverDatareses
	 * @param sessionId
	 * @param gateId
	 * @return Boolean
	 */
	@Transactional
	private Boolean sendGateInOrderToRobot(List<DriverDataRes> driverDatareses, String sessionId, String gateId) {
		// Send gate in order to robot
		try {
			GateInFormData gateInFormData = new GateInFormData();
			List<PickupHistory> pickupIn = new ArrayList<>();
			List<PickupHistory> pickupOut = new ArrayList<>();
			PickupHistory pickupTemp = null;
			Long wgt = 0L;
			for (DriverDataRes driverDataRes : driverDatareses) {
				pickupTemp = pickupHistoryService.selectPickupHistoryById(driverDataRes.getPickupHistoryId());
				if (pickupTemp.getShipment().getServiceType()%2 == 1) {
					pickupOut.add(pickupTemp);
				} else {
					pickupIn.add(pickupTemp);
				}
				wgt += Long.parseLong(driverDataRes.getWgt());
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
			DriverDataRes driverData = driverDatareses.get(0);
			gateInFormData.setGatePass(driverData.getTruckNo());
			gateInFormData.setTruckNo(driverData.getTruckNo());
			gateInFormData.setChassiNo(driverData.getChassisNo());
			gateInFormData.setWgt(wgt.toString());
			gateInFormData.setSessionId(sessionId);
			gateInFormData.setGateId(gateId);
			if (!checkGateOrderDoable(gateInFormData)) {
				return false;
			}
			ProcessOrder processOrder = new ProcessOrder();
			processOrder.setShipmentId(pickupTemp.getShipmentId());
			processOrder.setServiceType(8);
			processOrder.setLogisticGroupId(pickupTemp.getLogisticGroupId());
			processOrder.setStatus(1);
			processOrder.setRobotUuid(gateId);
			processOrderService.insertProcessOrder(processOrder);
			gateInFormData.setReceiptId(processOrder.getId());
			for (DriverDataRes driverDataRes : driverDatareses) {
				PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(driverDataRes.getPickupHistoryId());
				pickupHistory.setProcessOrderId(processOrder.getId());
				pickupHistoryService.updatePickupHistory(pickupHistory);
			}
			String msg = new Gson().toJson(gateInFormData);
			SysRobot robot = new SysRobot();
			robot.setStatus("0");
			robot.setIsGateInOrder(true);
			SysRobot sysRobot = robotService.findFirstRobot(robot);
			if (sysRobot == null) {
				return false;
			}
			robotService.updateRobotStatusByUuId(sysRobot.getUuId(), "1");
			mqttService.sendMessageToRobot(msg, sysRobot.getUuId());
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Check pickup history has position
	 * 
	 * @param pickupHistory
	 * @return Boolean
	 */
	private Boolean checkPickupHistoryHasPosition(PickupHistory pickupHistory) {
		if (pickupHistory.getArea() != null) {
			return true;
		}
		if (pickupHistory.getBlock() != null && pickupHistory.getBay() != null
			&& pickupHistory.getLine() != null && pickupHistory.getTier() != null) {
				return true;
			}
		return false;
	}
	
	private Boolean checkGateOrderDoable(GateInFormData gateInFormData) {
		// TODO : Validate on the things need to make a gate-in order
		return true;
	}
}
