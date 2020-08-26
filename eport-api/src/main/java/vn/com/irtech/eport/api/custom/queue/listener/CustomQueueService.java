package vn.com.irtech.eport.api.custom.queue.listener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

@Service
public class CustomQueueService {
	
	private BlockingQueue<ShipmentDetail> shipmentDetailQueue = new LinkedBlockingQueue<ShipmentDetail>();
	
	public ShipmentDetail getShipmentDetail() {
		try {
			return shipmentDetailQueue.take();
		} catch (Exception e) {
			return null;
		}
	}
	
	public void offerShipmentDetail(ShipmentDetail shipmentDetail) {
		shipmentDetailQueue.offer(shipmentDetail);
	}
}
