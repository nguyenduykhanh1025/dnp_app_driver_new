/**
 * 
 */
package vn.com.irtech.eport.web.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysSyncQueue;
import vn.com.irtech.eport.system.service.ISysSyncQueueService;
import vn.com.irtech.eport.web.mqtt.MqttService;
import vn.com.irtech.eport.web.mqtt.MqttService.EServiceRobot;

/**
 * @author Trong Hieu
 *
 */
@Component("syncQueueTask")
public class SyncQueueTask {

	private static final Logger logger = LoggerFactory.getLogger(SyncQueueTask.class);

	@Autowired
	private ISysSyncQueueService sysSyncQueueService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private MqttService mqttService;

	public void synchronizeQueue(Integer delayTimeSeconds, Integer retryTimes) {
		logger.debug("====================Begin synchronize queue: delay time (second) " + delayTimeSeconds
				+ " retry times " + retryTimes);
		// delayDate = now - delayTimeMs
		Date delayDate = DateUtils.addSeconds(new Date(), (-1) * retryTimes);
		// Params to get SysSyncQueue match in retryTimes and delayTimeSeconds demand
		SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
		sysSyncQueueParam.setCreateTime(delayDate);
		sysSyncQueueParam.setRetryTimes(retryTimes);
		Map<String, Object> params = new HashMap<>();
		// To query sys sync queue having status waiting or error with the permitted
		// retry times
		params.put("waitingOrError", true);

		// Get list update expired dem
		sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DEM);
		List<SysSyncQueue> sysSyncQueuesDem = sysSyncQueueService
				.selectSysSyncQueueWithDelayTimeList(sysSyncQueueParam);
		if (CollectionUtils.isNotEmpty(sysSyncQueuesDem)) {
			handlingListDemRequestUpdate(sysSyncQueuesDem);
		}

		// Get list update det free time
		// TODO
	}

	/**
	 * Handling list expired dem request update
	 * 
	 * @param sysSyncQueues
	 */
	private void handlingListDemRequestUpdate(List<SysSyncQueue> sysSyncQueues) {
		// Sort sync queue list by order job order no
		Collections.sort(sysSyncQueues, new JobOrderNoComparator());
		// syncQueuesDem use to store sync queue with same job order no
		List<SysSyncQueue> syncQueuesDem = new ArrayList<>();
		String currentJobOrderNo = sysSyncQueues.get(0).getJobOdrNo();
		for (SysSyncQueue sysSyncQueue : sysSyncQueues) {
			if (!sysSyncQueue.getJobOdrNo().equalsIgnoreCase(currentJobOrderNo)) {
				// Case when the job order no of current edo is different
				// handle current list edo with same job added from previous process
				// Send to robot
				sendReqToRobot(syncQueuesDem);
				// Refresh list edo to add current edo (different job)
				syncQueuesDem = new ArrayList<>();
			}
			currentJobOrderNo = sysSyncQueue.getJobOdrNo();
			syncQueuesDem.add(sysSyncQueue);
		}
		sendReqToRobot(syncQueuesDem);
	}

	/**
	 * Create process order extend date, update expired dem in eport if exist, send
	 * process order to robot
	 * 
	 * @param sysSyncQueues
	 */
	private void sendReqToRobot(List<SysSyncQueue> sysSyncQueues) {
		SysSyncQueue syncReference = sysSyncQueues.get(0);

		// Create process order
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setPickupDate(syncReference.getExpiredDem());
		processOrder.setServiceType(EportConstants.SERVICE_EXTEND_DATE);
		processOrder.setOrderNo(syncReference.getJobOdrNo());
		processOrder.setContNumber(sysSyncQueues.size());
		// Map to get sync queue obj by container no
		// Use to get expired dem need to extend for each cont
		// When get list shipment detail list in eport, use container to get info
		// expired dem from this map
		Map<String, SysSyncQueue> syncQueueMap = new HashMap<String, SysSyncQueue>();
																						// container no
		// List container to update expired dem for each container with different
		// expired dem
		List<ShipmentDetail> shipmentDetails = new ArrayList<ShipmentDetail>();
		String syncQueueIds = "";
		String containers = "";
		for (SysSyncQueue syncQueue : sysSyncQueues) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setContainerNo(syncQueue.getCntrNo());
			shipmentDetail.setExpiredDem(syncQueue.getExpiredDem());
			shipmentDetails.add(shipmentDetail);
			containers += syncQueue.getCntrNo() + ",";
			syncQueueIds += syncQueue.getId() + ",";
			syncQueueMap.put(syncQueue.getCntrNo(), syncQueue);
		}
		// container no separated by comma use to query
		containers = containers.substring(0, containers.length() - 1);
		// String ids separated by comma use to update status for sync queue list by
		// batch (update where id in (??,??,??)
		syncQueueIds = syncQueueIds.substring(0, syncQueueIds.length() - 1);

		// Check case edo register on eport
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setOrderNo(syncReference.getJobOdrNo());
		Map<String, Object> params = new HashMap<>();
		params.put("containerNos", Convert.toStrArray(containers));
		List<ShipmentDetail> eportShipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isNotEmpty(eportShipmentDetails)) {
			for (ShipmentDetail shipmentDetail : eportShipmentDetails) {
				// Update expired dem for each container register on eport
				shipmentDetail.setExpiredDem(syncQueueMap.get(shipmentDetail.getContainerNo()).getExpiredDem());
				shipmentDetail.setUpdateBy(EportConstants.USER_NAME_SYSTEM);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
		}

		// - Set list container data with expiredem need to extend in to process json
		// data (field in Process Order obj)
		// - When all robot is busy, process order will be waiting to the next robot
		// available
		// - Info of expired dem need to update in catos will be store in here to get
		// from database
		ProcessJsonData processJsonData = new ProcessJsonData();
		processJsonData.setShipmentDetails(shipmentDetails);
		processJsonData.setContainers(containers);
		processOrder.setProcessData(new Gson().toJson(processJsonData));

		processOrderService.insertProcessOrder(processOrder);

		// Update status sync queue list
		SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
		sysSyncQueueUpdate.setStatus(EportConstants.SYNC_QUEUE_STATUS_PROGRESS);
		sysSyncQueueUpdate.setSyncType(EportConstants.SYNC_QUEUE_DEM);
		sysSyncQueueUpdate.setProcessOrderId(processOrder.getId());
		sysSyncQueueUpdate.setRetryTimes(syncReference.getRetryTimes() + 1);
		Map<String, Object> queueParams = new HashMap<>();
		queueParams.put("ids", Convert.toStrArray(syncQueueIds));
		sysSyncQueueUpdate.setParams(queueParams);
		sysSyncQueueService.updateSysSyncQueueWithCondition(sysSyncQueueUpdate);

		// Form a object with master data is process order data and sub data is list
		// container with specific expired dem need to extend
		ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.EXTENSION_DATE);
		} catch (MqttException e) {
			logger.debug("Failed to send extension date req to robot: " + new Gson().toJson(serviceRobotReq) + e);
		}
	}

	class JobOrderNoComparator implements Comparator<SysSyncQueue> {
		public int compare(SysSyncQueue syncQueue1, SysSyncQueue syncQueue2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return syncQueue1.getJobOdrNo().compareTo(syncQueue2.getJobOdrNo());
		}
	}
}
