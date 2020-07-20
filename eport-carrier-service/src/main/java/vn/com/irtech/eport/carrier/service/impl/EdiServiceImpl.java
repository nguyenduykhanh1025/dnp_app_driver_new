package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdiDataReq;
import vn.com.irtech.eport.carrier.service.IEdiService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.exception.BusinessException;

@Service
public class EdiServiceImpl implements IEdiService {

	private static final String API = "API";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@Override
	public void executeListEdi(List<EdiDataReq> ediDataReqs, String partnerCode, String transactionId) {
		for (EdiDataReq ediDataReq : ediDataReqs) {
			if (ediDataReq.getMsgFunc() == null) {
				continue;
			}

			switch (ediDataReq.getMsgFunc().toUpperCase()) {
			case "N": // insert
				this.insert(ediDataReq, partnerCode, transactionId);
				break;
			case "U": // update
				this.update(ediDataReq, partnerCode, transactionId);
				break;
			case "D": // delete
				this.delete(ediDataReq);
				break;
			default:
				break;
			}
		}
	}

	private int insert(EdiDataReq ediDataReq, String partnerCode, String transactionId) {
		Edo edo = new Edo();
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setDelFlg(0);

		 if (edoService.selectFirstEdo(edo) != null) {
			 throw new BusinessException(String.format("Edo to insert was existed (containerNo='%s', billOfLading=%s)",
						ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		 }
		
		
		Edo edoInsert = new Edo();
		this.settingEdoData(edoInsert, ediDataReq, partnerCode, transactionId);
		edoInsert.setCreateBy(API);
		edoInsert.setDelFlg(0);
		edoInsert.setCarrierId(Long.valueOf(1));
		int statusInsert = edoService.insertEdo(edoInsert);
		edoAuditLogService.addAuditLogFirst(edoInsert);
		return statusInsert;
	}

	private int update(EdiDataReq ediDataReq, String partnerCode, String transactionId) {
		Edo edo = new Edo();
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setDelFlg(0);

		Edo edoUpdate = edoService.selectFirstEdo(edo);
		if (edoUpdate == null) {
			throw new BusinessException(String.format("Edo to update is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}

		edoUpdate.setUpdateBy(API);
		this.settingEdoData(edoUpdate, ediDataReq, partnerCode, transactionId);
		int statusUpdate = edoService.updateEdo(edoUpdate);
		edoAuditLogService.updateAuditLog(edoUpdate);
		return statusUpdate;
	}

	private int delete(EdiDataReq ediDataReq) {
		Edo edo = new Edo();
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setDelFlg(0);
		Edo edoUpdate = edoService.selectFirstEdo(edo);
		if (edoUpdate == null) {
			throw new BusinessException(String.format("Edo to delete is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}

		edoUpdate.setDelFlg(1);
		edoUpdate.setUpdateBy(API);
		return edoService.updateEdo(edoUpdate);
	}

	private void settingEdoData(Edo edo, EdiDataReq ediDataReq, String partnerCode, String transactionId) {
		edo.setCarrierCode(partnerCode);
		edo.setBusinessUnit(ediDataReq.getLineOper());
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
