package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.dto.EdiDataReq;

public interface IEdiService {
	public void executeListEdi(List<EdiDataReq> ediDataReqs, String partnerCode, String transactionId);
}
