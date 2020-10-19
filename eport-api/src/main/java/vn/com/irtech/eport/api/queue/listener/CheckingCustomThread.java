package vn.com.irtech.eport.api.queue.listener;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.common.constant.SystemConstants;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.CustomsCheckResultDto;
import vn.com.irtech.eport.logistic.service.ICustomCheckService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@Component
public class CheckingCustomThread {

	private final static Logger logger = LoggerFactory.getLogger(CheckingCustomThread.class);

	@Autowired
	private QueueService customQueueService;

	@Autowired
	private ICustomCheckService customerCheckServie;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor taskExecutor;

	@PostConstruct
	public void executeAsynchronously() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Start: ACCISS Listening...........................");
				while (true) {
					try {
						ShipmentDetail shipmentDetail = customQueueService.getShipmentDetail();
						logger.info("Connect To Acciss.");
						if (shipmentDetail != null) {
							CustomsCheckResultDto result = customerCheckServie.checkCustomStatus(
									shipmentDetail.getVslNm() + shipmentDetail.getVoyNo(),
									shipmentDetail.getContainerNo());
							boolean releasedFlg = false;
							if (result != null && result.isReleased()) {
								// Neu lenh BocHang -> 2, HaHang -> 4 //FIXME
								// Kiem tra số tờ khai có mapping với khai báo
								boolean mappingFlg = "1".equals(configService.selectConfigByKey(SystemConstants.ACCIS_CUSTOM_MAPPING_FLG_KEY));
								// da released
								releasedFlg = true;
								// neu check them so to khai
								if(mappingFlg) {
									String customsNo = shipmentDetail.getCustomsNo(); //.split(","); // split by comma (1,2,3)
									String returnCustomsNo = result.getCustomsAppNo(); //.split(",");
									releasedFlg = customsNo != null && customsNo.equalsIgnoreCase(returnCustomsNo);
								}
							}
							if(releasedFlg) {
								shipmentDetail.setStatus(shipmentDetail.getStatus() + 1); // Set status thong quan
								shipmentDetail.setCustomStatus("R");
								shipmentDetailService.updateShipmentDetail(shipmentDetail);
							} else {
								shipmentDetail.setCustomStatus("Y");
								shipmentDetailService.updateShipmentDetail(shipmentDetail);
							}
							Thread.sleep(100);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
