package vn.com.irtech.eport.carrier.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdiDataReq;
import vn.com.irtech.eport.carrier.service.IEdiService;
import vn.com.irtech.eport.carrier.service.IEdoService;

@Service
public class EdiServiceImpl implements IEdiService {

	@Autowired
	private IEdoService edoService;

	@Override
	@Transactional
	public List<EdiDataReq> executeListEdi(List<EdiDataReq> ediDataReqs, CarrierAccount carrierAccount,
			String transactionId) {
		List<EdiDataReq> ediDataReqsSuccess = new ArrayList<EdiDataReq>();

		for (EdiDataReq ediDataReq : ediDataReqs) {
			if (ediDataReq.getMsgFunc() == null) {
				continue;
			}

			switch (ediDataReq.getMsgFunc().toUpperCase()) {
			case "N": // insert
				if (this.insert(ediDataReq, carrierAccount, transactionId) == 1) {
					ediDataReqsSuccess.add(ediDataReq);
				}
				break;
			case "U": // update
				if (this.update(ediDataReq, carrierAccount, transactionId) == 1) {
					ediDataReqsSuccess.add(ediDataReq);
				}
				break;
			case "D": // delete
				if (this.delete(ediDataReq, carrierAccount) == 1) {
					ediDataReqsSuccess.add(ediDataReq);
				}
				break;
			default:
				break;
			}
		}
		return ediDataReqsSuccess;
	}

	private int insert(EdiDataReq ediDataReq, CarrierAccount carrierAccount, String transactionId) {
		Edo edo = new Edo();
		this.settingEdoData(edo, ediDataReq, carrierAccount, transactionId);
		edo.setCreateBy(carrierAccount.getFullName());
		return edoService.insertEdo(edo);
	}

	private int update(EdiDataReq ediDataReq, CarrierAccount carrierAccount, String transactionId) {
		Edo edo = edoService.selectEdoByOrderNumber(ediDataReq.getReleaseNo());
		if (edo == null) {
			return 0;
		}
		
		edo.setUpdateBy(carrierAccount.getFullName());
		this.settingEdoData(edo, ediDataReq, carrierAccount, transactionId);
		return edoService.updateEdo(edo);
	}

	private int delete(EdiDataReq ediDataReq, CarrierAccount carrierAccount) {
		Edo edo = edoService.selectEdoByOrderNumber(ediDataReq.getReleaseNo());
		if (edo == null) {
			return 0;
		}
		Edo edoUpdate = new Edo();
		edoUpdate.setId(edo.getId());
		edoUpdate.setDelFlg(1);
		edoUpdate.setUpdateBy(carrierAccount.getFullName());
		return edoService.updateEdo(edoUpdate);
	}

	private void settingEdoData(Edo edo, EdiDataReq ediDataReq, CarrierAccount carrierAccount, String transactionId) {
		edo.setCarrierId(carrierAccount.getId());
		edo.setCarrierCode(ediDataReq.getLineOper());
		edo.setConsignee(ediDataReq.getConsignee());
		edo.setSecureCode(ediDataReq.getSecureCode());
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setOrderNumber(ediDataReq.getReleaseNo());
		edo.setDetFreeTime(ediDataReq.getDetFreeDays());
		edo.setExpiredDem(ediDataReq.getExpiryTs());
		edo.setEmptyContainerDepot(ediDataReq.getTerOfMtReturn());
		edo.setVessel(ediDataReq.getModTransName());
		edo.setVoyNo(ediDataReq.getModTransVoyage());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setTransactionId(transactionId);
	}

}
