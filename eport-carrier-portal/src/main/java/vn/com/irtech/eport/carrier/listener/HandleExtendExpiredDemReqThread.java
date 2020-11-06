package vn.com.irtech.eport.carrier.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.carrier.service.ICarrierQueueService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Component
public class HandleExtendExpiredDemReqThread {

	private final static Logger logger = LoggerFactory.getLogger(HandleExtendExpiredDemReqThread.class);

	private final static Long WAITING_THREAD_TIME = 180000L; // 3 minutes

	@Autowired
	private ICarrierQueueService queueService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor taskExecutor;

	@PostConstruct
	public void executeAsynchronously() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Start: Extend expired dem request Listening...........................");
				while (true) {
					try {
						// Get all edo
						List<Edo> edos = queueService.getAllEdoExtendExpiredDem();
						try {
							Thread.sleep(WAITING_THREAD_TIME);
						} catch (InterruptedException e) {
							logger.debug("Error when sleep thread " + e);
						}
						List<Edo> edos2 = queueService.getAllEdoExtendExpiredDem();
						if (CollectionUtils.isNotEmpty(edos2)) {
							edos.addAll(edos2);
						}
						if (CollectionUtils.isNotEmpty(edos)) {
							// Sort edos by job order no
							Collections.sort(edos, new JobOrderNoComparator());
							List<Edo> edosExtend = new ArrayList<>();
							String currentJobOrderNo = edos.get(0).getJobOrderNo();
							for (Edo edo : edos) {
								if (!edo.getJobOrderNo().equalsIgnoreCase(currentJobOrderNo)) {
									filterDuplicateContainer(edosExtend);
									edosExtend = new ArrayList<>();
								}
								currentJobOrderNo = edo.getJobOrderNo();
								edosExtend.add(edo);
							}
							filterDuplicateContainer(edosExtend);
						}
					} catch (Exception e) {
						logger.error("Error when  " + e);
					}
				}
			}
		});
	}

	/**
	 * Remove same edo with same container no, get one with last update time
	 * 
	 * @param edos
	 */
	private void filterDuplicateContainer(List<Edo> edos) {
		// Sort edos by container number
		Collections.sort(edos, new ContainerNoComparator());
		List<Edo> edosExtend = new ArrayList<Edo>();
		Date currentUpdateTime = null;
		String currentContainerNo = edos.get(0).getContainerNumber();
		for (Edo edo : edos) {
			if (!edo.getContainerNumber().equalsIgnoreCase(currentContainerNo)) {
				sendReqToRobot(edosExtend);
				edosExtend = new ArrayList<Edo>();
				currentUpdateTime = null;
			}
			currentContainerNo = edo.getContainerNumber();
			// When edo with same container no
			// current update time == null => there is no edo with same container has been
			// added to list yet
			// add to list update current update time
			if (currentUpdateTime == null) {
				edosExtend.add(edo);
				currentUpdateTime = edo.getUpdateTime();
			} else if (currentUpdateTime.getTime() < edo.getUpdateTime().getTime()) {
				// Case next edo with same container no has newer update time
				// => remove older update edo and add newer update edo
				edosExtend.remove(edosExtend.size() - 1);
				edosExtend.add(edo);
				currentUpdateTime = edo.getUpdateTime();
			}
		}
		sendReqToRobot(edosExtend);
	}

	/**
	 * Create process order extend date, update expired dem in eport if exist, send
	 * process order to robot
	 * 
	 * @param edos
	 */
	private void sendReqToRobot(List<Edo> edos) {
		Edo edoReference = edos.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setPickupDate(edoReference.getExpiredDem());
		processOrder.setServiceType(EportConstants.SERVICE_EXTEND_DATE);
		processOrder.setOrderNo(edoReference.getJobOrderNo());
		processOrder.setContNumber(edos.size());
		String containers = "";
		Map<String, Edo> edoMap = new HashMap<String, Edo>(); // Map to get edo object by container no
		// List container to update expired dem for each container with different
		// expired dem
		List<ShipmentDetail> shipmentDetails = new ArrayList<ShipmentDetail>();
		for (Edo edo : edos) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setContainerNo(edo.getContainerNumber());
			shipmentDetail.setExpiredDem(edo.getExpiredDem());
			shipmentDetails.add(shipmentDetail);
			containers += edo.getContainerNumber() + ",";
			edoMap.put(edo.getContainerNumber(), edo);
		}
		containers = containers.substring(0, containers.length() - 1);

		// Check case edo register on eport
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setOrderNo(edoReference.getJobOrderNo());
		Map<String, Object> params = new HashMap<>();
		params.put("containerNos", Convert.toStrArray(containers));
		List<ShipmentDetail> eportShipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isNotEmpty(eportShipmentDetails)) {
			for (ShipmentDetail shipmentDetail : eportShipmentDetails) {
				// Update expirede dem for each container register on eport
				shipmentDetail.setExpiredDem(edoMap.get(shipmentDetail.getContainerNo()).getExpiredDem());
				shipmentDetail.setUpdateBy(EportConstants.USER_NAME_SYSTEM);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
		}

		ProcessJsonData processJsonData = new ProcessJsonData();
		processJsonData.setShipmentDetails(shipmentDetails);
		processJsonData.setContainers(containers);
		processOrder.setProcessData(new Gson().toJson(processJsonData));

		processOrderService.insertProcessOrder(processOrder);

		// Send to robot
		ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.EXTENSION_DATE);
		} catch (MqttException e) {
			logger.debug("Failed to send extension date req to robot: " + new Gson().toJson(serviceRobotReq) + e);
		}
	}

	class JobOrderNoComparator implements Comparator<Edo> {
		public int compare(Edo edo1, Edo edo2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return edo1.getJobOrderNo().compareTo(edo2.getJobOrderNo());
		}
	}

	class ContainerNoComparator implements Comparator<Edo> {
		public int compare(Edo edo1, Edo edo2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return edo1.getContainerNumber().compareTo(edo2.getContainerNumber());
		}
	}
}
