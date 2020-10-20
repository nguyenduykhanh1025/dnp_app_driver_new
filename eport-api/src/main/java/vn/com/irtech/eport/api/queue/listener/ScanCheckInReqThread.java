package vn.com.irtech.eport.api.queue.listener;

import javax.annotation.PostConstruct;

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
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.system.dto.NotificationReq;

@Component
public class ScanCheckInReqThread {

	private final static Logger logger = LoggerFactory.getLogger(ScanCheckInReqThread.class);

	private static final Long TIME_OUT_WAIT_DETECTION = 1000L;

	private static final Integer RETRY_WAIT_DETECTION = 60;

	@Autowired
	private QueueService queueService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor taskExecutor;

	@PostConstruct
	public void executeAsynchronously() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Start: Check in request Listening...........................");
				while (true) {
					try {
						GateNotificationCheckInReq checkInReq = queueService.getCheckInReq();
						if (checkInReq != null) {
							GateDetection gateDetection = gateDetection = (GateDetection) CacheUtils
									.get("gate1" + "_" + EportConstants.CACHE_GATE_DETECTION_KEY);
							// Get detection info from cache
							// Waiting and retry get info on demading time
							for (int i = 1; i <= RETRY_WAIT_DETECTION; i++) {
								// If detection exist in cache => break loop
								if (gateDetection != null) {
									break;
								}
								logger.debug("Wait " + TIME_OUT_WAIT_DETECTION + " miliseconds");
								try {
									Thread.sleep(TIME_OUT_WAIT_DETECTION);
								} catch (InterruptedException e) {
									logger.error("Error sleep to wait detection info: " + e);
								}
								gateDetection = (GateDetection) CacheUtils
										.get("gate1" + "_" + EportConstants.CACHE_GATE_DETECTION_KEY);
							}

							// Check detection exist (true => check matching with check in req,
							// false => send failed notification for all req in queue
							if (gateDetection != null) {
								boolean isMatch = false;
								// Check truck no
								if (checkInReq.getTruckNo().equalsIgnoreCase(gateDetection.getTruckNo())) {
									isMatch = true;
								}
								// Check chassis no
								if (checkInReq.getChassisNo().equalsIgnoreCase(gateDetection.getChassisNo())) {
									isMatch = true;
								}

								String cont1 = checkInReq.getContainerSend1();
								String cont2 = checkInReq.getContainerSend2();

								// Check cont 1
								if (StringUtils.isNotEmpty(cont1)) {
									if (cont1.equalsIgnoreCase(gateDetection.getContainerNo1())
											|| cont2.equalsIgnoreCase(gateDetection.getContainerNo2())) {
										isMatch = true;
									}
								}

								// Check cont 2
								if (StringUtils.isNotEmpty(cont2)) {
									if (cont2.equalsIgnoreCase(gateDetection.getContainerNo2())
											|| cont2.equalsIgnoreCase(gateDetection.getContainerNo2())) {
										isMatch = true;
									}
								}

								// if match send notification to gate and driver
								// else send to failed notification to driver
								if (isMatch) {
									sendCheckInReqToGate(checkInReq);
								} else {
									sendFailedNotificationForDriver(checkInReq);
								}
							} else {
								// Get check in req in queue until queue is empty to send failed notification
								while (!queueService.checkInQueuIsEmpty()) {
									// Send notification and get next element
									sendFailedNotificationForDriver(checkInReq);
									checkInReq = queueService.getCheckInReq();
								}
								sendFailedNotificationForDriver(checkInReq);
							}
						}
					} catch (Exception e) {
						logger.error("Error when scan check in req " + e);
					}
				}
			}
		});
	}

	/**
	 * Send check in req to gate and accepted notification for driver
	 * 
	 * @param checkInReq
	 */
	private void sendCheckInReqToGate(GateNotificationCheckInReq checkInReq) {
		NotificationReq notificationReq = new NotificationReq();
		notificationReq.setTitle("ePort: Yêu cầu check in tại cổng.");
		notificationReq.setMsg("Có yêu cầu check in tại cổng xe " + checkInReq.getTruckNo());
		notificationReq.setType(EportConstants.APP_USER_TYPE_GATE);
		notificationReq.setLink("");
		notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_HIGH);
		notificationReq.setGateData(checkInReq);

		String msg = new Gson().toJson(notificationReq);
		try {
			logger.warn(">>>>>>>>>>>>>>>>> Send GATE Dialog: " + msg);
			mqttService.publish(MqttConsts.NOTIFICATION_GATE_TOPIC, new MqttMessage(msg.getBytes()));
		} catch (MqttException e) {
			logger.error("Error when try sending notification request check in for gate: " + e);
		}

		// Send accepted processing notification for driver
		DriverRes driverRes = new DriverRes();
		driverRes.setStatus(BusinessConsts.IN_PROGRESS);
		driverRes.setResult(BusinessConsts.BLANK);
		driverRes.setMsg("Yêu cầu gate in đang được xử lý...");
		try {
			String msgDriver = new Gson().toJson(driverRes);
			mqttService.publish(MqttConsts.DRIVER_RES_TOPIC.replace("+", checkInReq.getSessionId()),
					new MqttMessage(msgDriver.getBytes()));
		} catch (Exception e) {
			logger.error("Error send message to driver: " + e);
		}
	}

	/**
	 * Send failed notification to driver
	 * 
	 * @param checkInReq
	 */
	private void sendFailedNotificationForDriver(GateNotificationCheckInReq checkInReq) {
		DriverRes driverRes = new DriverRes();
		driverRes.setStatus(BusinessConsts.FAIL);
		driverRes.setResult(BusinessConsts.FINISH);
		driverRes.setMsg(
				"Yêu cầu vào cổng của quý khách không được chấp nhận. Không thể nhận diện được thông tin vào cổng.");
		try {
			String msg = new Gson().toJson(driverRes);
			mqttService.publish(MqttConsts.DRIVER_RES_TOPIC.replace("+", checkInReq.getSessionId()),
					new MqttMessage(msg.getBytes()));
		} catch (Exception e) {
			logger.error("Error send message to driver: " + e);
		}
	}
}
