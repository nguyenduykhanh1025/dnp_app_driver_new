package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.Edo;

public interface ICarrierQueueService {
	public Edo getEdoExtendExpiredDem();

	public void offerEdoExtendExpiredDem(Edo edo);

	public List<Edo> getAllEdoExtendExpiredDem();

	public Boolean checkEdoExtendExpiredDemQueueIsEmpty();
}
