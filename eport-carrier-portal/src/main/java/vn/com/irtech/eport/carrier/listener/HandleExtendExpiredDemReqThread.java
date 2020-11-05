package vn.com.irtech.eport.carrier.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Component
public class HandleExtendExpiredDemReqThread {

	private final static Logger logger = LoggerFactory.getLogger(HandleExtendExpiredDemReqThread.class);

	@Autowired
	private QueueService queueService;

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
							sendReqToRobot(edosExtend);
						}
					} catch (Exception e) {
						logger.error("Error when  " + e);
					}
				}
			}
		});
	}

	private void filterDuplicateContainer(List<Edo> edos) {

	}

	private void sendReqToRobot(List<Edo> edos) {
		Edo edoReference = edos.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setPickupDate(edoReference.getExpiredDem());
		processOrder.setServiceType(EportConstants.SERVICE_EXTEND_DATE);
		processOrder.setOrderNo(edoReference.getJobOrderNo());
		processOrder.setContNumber(edos.size());
		String containers = "";
		for (Edo edo : edos) {
			containers += edo.getContainerNumber() + ",";
		}
		ProcessJsonData processJsonData = new ProcessJsonData();
		containers = containers.substring(0, containers.length() - 1);
		processJsonData.setContainers(containers);
		processOrder.setProcessData(new Gson().toJson(processJsonData));

		// Check case edo register on eport
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setOrderNo(edoReference.getJobOrderNo());
		Map<String, Object> params = new HashMap<>();
		params.put("containerNos", Convert.toStrArray(containers));
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {

		}

		processOrderService.insertProcessOrder(processOrder);
	}

	class JobOrderNoComparator implements Comparator<Edo> {
		public int compare(Edo edo1, Edo edo2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return edo1.getJobOrderNo().compareTo(edo2.getJobOrderNo());
		}
	}
}
