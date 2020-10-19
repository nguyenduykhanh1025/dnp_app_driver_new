package vn.com.irtech.eport.api.queue.listener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

@Service
public class QueueService {
	
	/** Queue for custom check */
	private BlockingQueue<ShipmentDetail> shipmentDetailQueue = new LinkedBlockingQueue<ShipmentDetail>();
	
	/** Queue for check in request */
	private BlockingQueue<GateNotificationCheckInReq> checkInReqQueue = new LinkedBlockingQueue<GateNotificationCheckInReq>();

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

	public GateNotificationCheckInReq getCheckInReq() {
		try {
			return checkInReqQueue.take();
		} catch (Exception e) {
			return null;
		}
	}

	public void offerCheckInReq(GateNotificationCheckInReq checkInReq) {
		checkInReqQueue.offer(checkInReq);
	}

	public Boolean checkInQueuIsEmpty() {
		return checkInReqQueue.isEmpty();
	}
}
