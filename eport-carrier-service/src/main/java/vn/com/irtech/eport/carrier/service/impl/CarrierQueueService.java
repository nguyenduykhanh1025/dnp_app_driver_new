package vn.com.irtech.eport.carrier.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.ICarrierQueueService;

@Service
public class CarrierQueueService implements ICarrierQueueService {

	/** Queue for request extend expired dem by edo */
	private BlockingQueue<Edo> EdoExtendExpiredDemQueue = new LinkedBlockingQueue<Edo>();

	public Edo getEdoExtendExpiredDem() {
		try {
			return EdoExtendExpiredDemQueue.take();
		} catch (Exception e) {
			return null;
		}
	}

	public void offerEdoExtendExpiredDem(Edo edo) {
		EdoExtendExpiredDemQueue.offer(edo);
	}

	public List<Edo> getAllEdoExtendExpiredDem() {
		List<Edo> edos = new ArrayList<>();
		EdoExtendExpiredDemQueue.drainTo(edos);
		return edos;
	}

	public Boolean checkEdoExtendExpiredDemQueueIsEmpty() {
		return EdoExtendExpiredDemQueue.isEmpty();
	}
}
