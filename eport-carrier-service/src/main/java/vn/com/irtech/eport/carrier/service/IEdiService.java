package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.dto.EdiDataReq;

public interface IEdiService {
	public List<EdiDataReq> executeListEdi(List<EdiDataReq> ediDataReqs, CarrierAccount carrierAccount, String transactionId);
}
