package vn.com.irtech.eport.carrier.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdiDataReq;
import vn.com.irtech.eport.carrier.service.IEdiService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Service
public class EdiServiceImpl implements IEdiService {

	private static final String API = "API";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@Autowired
	private ICatosApiService catosApiService;

	@Override
	public void executeListEdi(List<EdiDataReq> ediDataReqs, String partnerCode, Long carrierGroupId, String transactionId) {
		for (EdiDataReq ediDataReq : ediDataReqs) {
			if (ediDataReq.getMsgFunc() == null) {
				continue;
			}

			switch (ediDataReq.getMsgFunc().toUpperCase()) {
			case "N": // insert
				this.insert(ediDataReq, partnerCode, carrierGroupId, transactionId);
				break;
			case "U": // update
				this.update(ediDataReq, partnerCode, carrierGroupId, transactionId);
				break;
			case "D": // delete
				this.delete(ediDataReq, partnerCode);
				break;
			default:
				break;
			}
		}
	}

	private int insert(EdiDataReq ediDataReq, String partnerCode, Long carrierGroupId, String transactionId) {
		Edo edo = new Edo();
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setCarrierCode(partnerCode);
		edo.setDelFlg(0);

		 if (edoService.selectFirstEdo(edo) != null) {
			 throw new BusinessException(String.format("Edo to insert was existed (containerNo='%s', billOfLading=%s)",
						ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		 }
		
		
		Edo edoInsert = new Edo();
		this.settingEdoData(edoInsert, ediDataReq, partnerCode, transactionId);
		edoInsert.setCarrierId(carrierGroupId);
		edoInsert.setCreateBy(partnerCode);
		edoInsert.setCreateSource(API);
		edoInsert.setDelFlg(0);
		// edoInsert.setCarrierId(Long.valueOf(1));
		Date setTimeUpdatExpicedDem = edoInsert.getExpiredDem();
		setTimeUpdatExpicedDem.setHours(23);
		setTimeUpdatExpicedDem.setMinutes(59);
		setTimeUpdatExpicedDem.setSeconds(59);
		edoInsert.setExpiredDem(setTimeUpdatExpicedDem);
		int statusInsert = edoService.insertEdo(edoInsert);
		edoAuditLogService.addAuditLogFirst(edoInsert);
		return statusInsert;
	}

	private int update(EdiDataReq ediDataReq, String partnerCode, Long carrierGroupId, String transactionId) {
		Edo edo = new Edo();
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setCarrierCode(partnerCode);
		edo.setDelFlg(0);

		Edo edoUpdate = edoService.selectFirstEdo(edo);
		if (edoUpdate == null || !edoUpdate.getCarrierCode().equals(partnerCode)) {
			throw new BusinessException(String.format("Edo to update is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}
		edoUpdate.setUpdateBy(partnerCode);
		edoUpdate.setCarrierId(carrierGroupId);
		this.settingEdoData(edoUpdate, ediDataReq, partnerCode, transactionId);
		Edo odlEdo = edoService.selectEdoById(edoUpdate.getId());
		if (edoUpdate.getExpiredDem() != null) {
			Date setTimeUpdatExpicedDem = edoUpdate.getExpiredDem();
			setTimeUpdatExpicedDem.setHours(23);
			setTimeUpdatExpicedDem.setMinutes(59);
			setTimeUpdatExpicedDem.setSeconds(59);
			edoUpdate.setExpiredDem(setTimeUpdatExpicedDem);
		}

		// Get container info from catos
		ContainerInfoDto cntrInfoParam = new ContainerInfoDto();
		cntrInfoParam.setBlNo(ediDataReq.getBillOfLading());
		cntrInfoParam.setCntrNo(ediDataReq.getContainerNo());
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoListByCondition(cntrInfoParam);

		// check if expired dem has update
		if (odlEdo.getExpiredDem().compareTo(edoUpdate.getExpiredDem()) == 0) {
			edoUpdate.setExpiredDem(null);
		}

		// check if det free time has update
		// if true (old != new) check container condition to update (container has not
		// dropped cont empty yet)
		if (odlEdo.getDetFreeTime() == edoUpdate.getDetFreeTime()) {
			edoUpdate.setDetFreeTime(null);
		} else {
			// Case has update => check condition
			if (CollectionUtils.isNotEmpty(cntrInfos)) {
				for (ContainerInfoDto cntrInfo : cntrInfos) {
					if ("EMTY".equalsIgnoreCase(cntrInfo.getVslCd())
							&& StringUtils.isNotEmpty(cntrInfo.getJobOdrNo())) {
						throw new BusinessException(String.format(
								"Empty container has already been in Da Nang port (containerNo='%s', billOfLading=%s)",
								ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
					}
				}
			}
		}

		// check if empty cotnainer depot has update
		// if true (old != new) => check contition to udpate (contaire has not dropped
		// cont empty yet)
		if (odlEdo.getEmptyContainerDepot().equals(edoUpdate.getEmptyContainerDepot())) {
			edoUpdate.setEmptyContainerDepot(null);
		} else {
			// Case has update => check condition
			if (CollectionUtils.isNotEmpty(cntrInfos)) {
				for (ContainerInfoDto cntrInfo : cntrInfos) {
					if ("EMTY".equalsIgnoreCase(cntrInfo.getVslCd())
							&& StringUtils.isNotEmpty(cntrInfo.getJobOdrNo())) {
						throw new BusinessException(String.format(
								"Empty container has already been in Da Nang port (containerNo='%s', billOfLading=%s)",
								ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
					}
				}
			}
		}
		int statusUpdate = edoService.updateEdo(edoUpdate);
		edoAuditLogService.updateAuditLog(edoUpdate);
		return statusUpdate;
	}

	private int delete(EdiDataReq ediDataReq, String partnerCode) {
		Edo edo = new Edo();
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setDelFlg(0);
		// Set container no into param to query edo with same container no (query use
		// '=' operator)
		// If set directly into containerNumber attribute => query will use like
		Map<String, Object> params = new HashMap<>();
		params.put("containerNumber", ediDataReq.getContainerNo());
		edo.setParams(params);
		// Get list edo (mostly return with one
		List<Edo> edos = edoService.selectEdoList(edo);

		// If empty => not found any edo to delete
		if (CollectionUtils.isEmpty(edos)) {
			throw new BusinessException(String.format("Edo to delete is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}
		Edo edoUpdate = edos.get(0);
		if (!edoUpdate.getCarrierCode().equals(partnerCode)) {
			throw new BusinessException(String.format("Edo to delete is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}
		// Check status attribute get from shipment detail eport
		// If status is not blank or null => edo has already been declared in eport
		if (StringUtils.isNotEmpty(edoUpdate.getStatus())) {
			throw new BusinessException(String.format(
					"Edo to delete has already been declared on eport (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}

		// Get info of edo in catos
		ContainerInfoDto cntrInfoParam = new ContainerInfoDto();
		cntrInfoParam.setCntrNo(ediDataReq.getContainerNo());
		cntrInfoParam.setBlNo(ediDataReq.getBillOfLading());
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoListByCondition(cntrInfoParam);
		// Check if not null & job order no 2 not null => had order in catos => can't
		// delete
		if (CollectionUtils.isNotEmpty(cntrInfos) && StringUtils.isNotEmpty(cntrInfos.get(0).getJobOdrNo2())) {
			throw new BusinessException(String.format(
					"Edo to delete has already been made order at Da Nang port (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}

		edoUpdate.setDelFlg(1);
		edoUpdate.setUpdateBy(partnerCode);
		return edoService.updateEdo(edoUpdate);
	}

	private void settingEdoData(Edo edo, EdiDataReq ediDataReq, String partnerCode, String transactionId) {
		edo.setCarrierCode(partnerCode);
		edo.setBusinessUnit(ediDataReq.getLineOper());
		edo.setConsignee(ediDataReq.getConsignee());
		edo.setSecureCode(ediDataReq.getSecureCode());
		edo.setOrderNumber(ediDataReq.getSecureCode());
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setReleaseNo(ediDataReq.getReleaseNo());
		edo.setDetFreeTime(ediDataReq.getDetFreeDays());
		edo.setExpiredDem(ediDataReq.getExpiryTs());
		edo.setEmptyContainerDepot(ediDataReq.getTerOfMtReturn());
		edo.setVessel(ediDataReq.getModTransName());
		edo.setVoyNo(ediDataReq.getModTransVoyage());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setTaxCode(ediDataReq.getConsigneeTaxcode());
		edo.setTransactionId(transactionId);
	}

}
