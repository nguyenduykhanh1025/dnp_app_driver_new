package vn.com.irtech.eport.framework.custom.queue.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Component
public class CheckingCustomThread {
	
	private final static Logger logger = LoggerFactory.getLogger(CheckingCustomThread.class);
	
	@Autowired
	private CustomQueueService customQueueService;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	@Qualifier("threadPoolTaskExecutor")
    private TaskExecutor taskExecutor;
	
	@PostConstruct
	public void executeAsynchronously() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
            	logger.info("Start............................");
            	while(true) {
        			try {
        				ShipmentDetail shipmentDetail = customQueueService.getShipmentDetail();
        				logger.info("Connec To Acis.");
        				AjaxResult ajaxResult;
        				if (true/*shipmentDetailService.checkCustomStatus(shipmentDetail.getVoyNo(),shipmentDetail.getContainerNo())*/) {
        					shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
        					shipmentDetail.setCustomStatus("R");
        					shipmentDetailService.updateShipmentDetail(shipmentDetail);
        				} 
        				ajaxResult = AjaxResult.success();
        				ajaxResult.put("shipmentDetail", shipmentDetail);
        				webSocketService.sendMessage("/" + shipmentDetail.getContainerNo() + "/response", ajaxResult);
        				Thread.sleep(1000);
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
            }
        });
    }
}
