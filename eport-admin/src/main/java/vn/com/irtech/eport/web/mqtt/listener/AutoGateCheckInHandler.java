package vn.com.irtech.eport.web.mqtt.listener;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.web.dto.GateInFormData;
import vn.com.irtech.eport.web.dto.GateNotificationCheckInReq;
import vn.com.irtech.eport.web.mqtt.BusinessConsts;
import vn.com.irtech.eport.web.mqtt.MqttService;

@Component
public class AutoGateCheckInHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(AutoGateCheckInHandler.class);

	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IGateDetectionService gateDetectionService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

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
					mqttService.sendProgressToGate(BusinessConsts.FINISH, BusinessConsts.FAIL,
							"Có lỗi xảy ra khi làm lệnh gate in.", topic.split("/")[3]);
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

		String msg = map.get("msg") == null ? null : map.get("msg").toString();

		String gateInData = map.get("gateInData") == null ? null : map.get("gateInData").toString();

		GateNotificationCheckInReq gateNotificationCheckInReq = new Gson().fromJson(gateInData,
				GateNotificationCheckInReq.class);
		if (gateNotificationCheckInReq == null) {
			return;
		}

		if ("accept".equals(result)) {
			// Set info from app gate to update for case detection info is incorrect
			// Get info from catos by container and set extra data to detection container
			GateDetection gateDetection = new GateDetection();
			gateDetection.setId(gateNotificationCheckInReq.getId());
			gateDetection.setTruckNo(gateNotificationCheckInReq.getTruckNo());
			gateDetection.setChassisNo(gateNotificationCheckInReq.getChassisNo());
			gateDetection.setContainerNo1(gateNotificationCheckInReq.getContainerSend1());
			gateDetection.setContainerNo2(gateNotificationCheckInReq.getContainerSend2());
			gateDetection.setGatepass(gateNotificationCheckInReq.getGatePass());
			gateDetection.setTotalWgt(Long.parseLong(gateNotificationCheckInReq.getWeight().toString()));
			gateDetection.setDeduct(Long.parseLong(gateNotificationCheckInReq.getDeduct().toString()));
			gateDetection.setGateNo(gateNotificationCheckInReq.getGateId());
			gateDetection.setRemark(gateNotificationCheckInReq.getRemark());
			gateDetection.setStatus("P");

			// Get container info from catos
			Map<String, ContainerInfoDto> cntrMap = getCntrMapFromCatos(gateDetection);

			// Set catos info to container 1 if exist
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
				ContainerInfoDto cntrInfo = cntrMap.get(gateDetection.getContainerNo1());
				if (cntrInfo != null) {
					gateDetection.setOpeCode1(cntrInfo.getPtnrCode());
					gateDetection.setWgt1(cntrInfo.getWgt());
					gateDetection.setSztp1(cntrInfo.getSztp());
					gateDetection.setCallSeq1(cntrInfo.getCallSeq());
					gateDetection.setVslCd1(cntrInfo.getVslCd());
					gateDetection.setCargoType1(cntrInfo.getCargoType());
					gateDetection.setPod1(cntrInfo.getPod());
					gateDetection.setFe1(cntrInfo.getFe());
				}
			}

			// Set catos info to container 2 if exist
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
				ContainerInfoDto cntrInfo = cntrMap.get(gateDetection.getContainerNo2());
				if (cntrInfo != null) {
					gateDetection.setOpeCode2(cntrInfo.getPtnrCode());
					gateDetection.setWgt2(cntrInfo.getWgt());
					gateDetection.setSztp2(cntrInfo.getSztp());
					gateDetection.setCallSeq2(cntrInfo.getCallSeq());
					gateDetection.setVslCd2(cntrInfo.getVslCd());
					gateDetection.setCargoType2(cntrInfo.getCargoType());
					gateDetection.setPod2(cntrInfo.getPod());
					gateDetection.setFe2(cntrInfo.getFe());
				}
			}
			gateDetectionService.updateGateDetection(gateDetection);

			sendGateInOrderToRobot(gateDetection);
		}
	}

	/**
	 * Get list container info from catos by string containers (container no
	 * separated by comma)
	 * 
	 * @param gateDetection
	 * @return
	 */
	private Map<String, ContainerInfoDto> getCntrMapFromCatos(GateDetection gateDetection) {
		String containers = "";
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
			containers += gateDetection.getContainerNo1();
		}
		if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
			containers += "," + gateDetection.getContainerNo2();
		}
		Map<String, ContainerInfoDto> cntrMap = new HashMap<>();
		if (StringUtils.isNotEmpty(containers)) {
			List<ContainerInfoDto> cntrInfoDtos = catosApiService.getContainerInfoDtoByContNos(containers);
			if (CollectionUtils.isNotEmpty(cntrInfoDtos)) {
				for (ContainerInfoDto cntr : cntrInfoDtos) {
					cntrMap.put(cntr.getCntrNo(), cntr);
				}
			}
		}
		return cntrMap;
	}

	/**
	 * Prepare data to make gate in order, find robot gate in available, send order
	 * to robot
	 * 
	 * @param gateNotificationCheckInReq
	 */
	private void sendGateInOrderToRobot(GateDetection gateDetection) {
		try {
			GateInFormData gateInFormData = new GateInFormData();
			// Set type gate in form data is beginning
			gateInFormData.setType(EportConstants.GATE_REQ_TYPE_BEGIN);
			List<PickupHistory> pickupIn = new ArrayList<>();
			String containerNos = "";

			// Container 1
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo1())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setId(gateDetection.getId() * 10 + 1);
				pickupHistory.setContainerNo(gateDetection.getContainerNo1());
				pickupHistory.setBlock("");
				pickupHistory.setArea("");
				pickupHistory.setLocationUpdate(false);
				pickupIn.add(pickupHistory);
				containerNos += gateDetection.getContainerNo1();
			}

			// Container 2
			if (StringUtils.isNotEmpty(gateDetection.getContainerNo2())) {
				PickupHistory pickupHistory = new PickupHistory();
				pickupHistory.setId(gateDetection.getId() * 10 + 2);
				pickupHistory.setContainerNo(gateDetection.getContainerNo2());
				pickupHistory.setBlock("");
				pickupHistory.setArea("");
				pickupHistory.setLocationUpdate(false);
				pickupIn.add(pickupHistory);
				containerNos += "," + gateDetection.getContainerNo2();
			}

			if (CollectionUtils.isNotEmpty(pickupIn)) {
				ProcessOrder processOrder = new ProcessOrder();
				processOrder.setServiceType(EportConstants.SERVICE_GATE_IN);
				processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
				processOrder.setRemark(gateDetection.getRemark());
				processOrderService.insertProcessOrder(processOrder);

				gateInFormData.setGateId(gateDetection.getGateNo());
				gateInFormData.setId(gateDetection.getId());
				gateInFormData.setPickupIn(pickupIn);
				gateInFormData.setModule("IN");
				gateInFormData.setContNumberIn(pickupIn.size());

				gateInFormData.setGatePass(gateDetection.getGatepass());
				gateInFormData.setTruckNo(gateDetection.getTruckNo());
				gateInFormData.setChassisNo(gateDetection.getChassisNo());
				if (StringUtils.isEmpty(gateInFormData.getChassisNo())) {
					gateInFormData.setChassisNo("");
				}
				Long gross = gateDetection.getTotalWgt() - gateDetection.getDeduct();
				gateInFormData.setWgt(gross.toString());
				gateInFormData.setGateId(gateDetection.getGateNo());
				gateInFormData.setReceiptId(processOrder.getId());
				gateInFormData
						.setIsPrint(checkPrint(containerNos, gateDetection.getTruckNo(), gateDetection.getChassisNo()));

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
					mqttService.sendProgressToGate(BusinessConsts.IN_PROGRESS, BusinessConsts.BLANK, message,
							gateInFormData.getGateId());
				} else {
					logger.debug("No GateRobot is available: " + msg);
				}
			}
		} catch (Exception e) {
			logger.error("Error when send order gate in: " + e);
		}
	}

	private boolean checkPrint(String containerNos, String truckNo, String chassisNo) {
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setTruckNo(truckNo);
		pickupHistoryParam.setChassisNo(chassisNo);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		Map<String, Object> params = new HashMap<>();
		String serviceTypes = EportConstants.SERVICE_DROP_EMPTY + "," + EportConstants.SERVICE_DROP_FULL;
		params.put("serviceTypes", Convert.toStrArray(serviceTypes));
		params.put("containerNos", Convert.toStrArray(containerNos));
		pickupHistoryParam.setParams(params);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		if (CollectionUtils.isNotEmpty(pickupHistories)) {
			return false;
		}
		return true;
	}
}