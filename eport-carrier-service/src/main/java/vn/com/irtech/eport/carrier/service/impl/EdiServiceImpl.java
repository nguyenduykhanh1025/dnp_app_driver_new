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
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.domain.SysSyncQueue;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysSyncQueueService;

@Service
public class EdiServiceImpl implements IEdiService {

	private static final String API = "API";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ISysSyncQueueService sysSyncQueueService;

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
		Edo odlEdo = edoService.selectFirstEdo(edo);

		if (odlEdo == null) {
			throw new BusinessException(String.format("Edo to update is not exist (containerNo='%s', billOfLading=%s)",
					ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
		}

		Edo edoUpdate = new Edo();
		this.settingEdoData(edoUpdate, ediDataReq, partnerCode, transactionId);
		edoUpdate.setUpdateBy(partnerCode);
		edoUpdate.setCarrierId(carrierGroupId);
		if (edoUpdate.getExpiredDem() != null) {
			Date setTimeUpdatExpicedDem = edoUpdate.getExpiredDem();
			setTimeUpdatExpicedDem.setHours(23);
			setTimeUpdatExpicedDem.setMinutes(59);
			setTimeUpdatExpicedDem.setSeconds(59);
			edoUpdate.setExpiredDem(setTimeUpdatExpicedDem);
		}

		// Get container info from catos
		Map<String, ContainerInfoDto> cntrMap = getContainerInfoMapFE(ediDataReq.getContainerNo());
		// Container full in catos
		ContainerInfoDto cntrFull = cntrMap.get(ediDataReq.getContainerNo() + "F");
		// Container empty in catos (if exists mean container has already been gate out)
		ContainerInfoDto cntrEmty = cntrMap.get(ediDataReq.getContainerNo() + "E");

		// check if expired dem has update
		if (edoUpdate.getExpiredDem() != null && odlEdo.getExpiredDem() != null
				&& odlEdo.getExpiredDem().compareTo(edoUpdate.getExpiredDem()) != 0) {
			if (cntrFull != null && StringUtils.isNotEmpty(cntrFull.getJobOdrNo2())) {
				// Get old request if exist, update else insert new request
				SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
				sysSyncQueueParam.setBlNo(edo.getBillOfLading());
				sysSyncQueueParam.setCntrNo(edo.getContainerNumber());
				sysSyncQueueParam.setJobOdrNo(cntrFull.getJobOdrNo2());
				sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DEM);
				sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
				List<SysSyncQueue> sysSyncQueues = sysSyncQueueService.selectSysSyncQueueList(sysSyncQueueParam);
				if (CollectionUtils.isNotEmpty(sysSyncQueues)) {
					// Case update request in queue
					SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
					sysSyncQueueUpdate.setId(sysSyncQueues.get(0).getId());
					sysSyncQueueUpdate.setExpiredDem(edoUpdate.getExpiredDem());
					sysSyncQueueService.updateSysSyncQueue(sysSyncQueueUpdate);
				} else {
					// Case insert new request in queue
					SysSyncQueue sysSyncQueue = new SysSyncQueue();
					sysSyncQueue.setSyncType(EportConstants.SYNC_QUEUE_DEM);
					sysSyncQueue.setExpiredDem(edoUpdate.getExpiredDem());
					sysSyncQueue.setBlNo(odlEdo.getBillOfLading());
					sysSyncQueue.setCntrNo(odlEdo.getContainerNumber());
					sysSyncQueue.setJobOdrNo(cntrFull.getJobOdrNo2());
					sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
					sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
				}
			}
		}

		// check if det free time has update
		// if true (old != new) check container condition to update (container has not
		// dropped cont empty yet)
		if (StringUtils.isNotEmpty(odlEdo.getDetFreeTime()) && StringUtils.isNotEmpty(edoUpdate.getDetFreeTime())
				&& odlEdo.getDetFreeTime().equalsIgnoreCase(edoUpdate.getDetFreeTime())) {
			if (cntrEmty != null && StringUtils.isNotEmpty(cntrEmty.getJobOdrNo())) {
				// Get old request if exist, update else insert new request
				SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
				sysSyncQueueParam.setBlNo(edo.getBillOfLading());
				sysSyncQueueParam.setCntrNo(edo.getContainerNumber());
				sysSyncQueueParam.setJobOdrNo(cntrEmty.getJobOdrNo());
				sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DET);
				sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
				List<SysSyncQueue> sysSyncQueues = sysSyncQueueService.selectSysSyncQueueList(sysSyncQueueParam);
				if (CollectionUtils.isNotEmpty(sysSyncQueues)) {
					// Case update request in queue
					SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
					sysSyncQueueUpdate.setId(sysSyncQueues.get(0).getId());
					sysSyncQueueUpdate.setDetFreeTime(edoUpdate.getDetFreeTime());
					sysSyncQueueUpdate
							.setRemark(getRemarkAfterUpdateDet(edoUpdate.getDetFreeTime(), cntrEmty.getRemark()));
					sysSyncQueueService.updateSysSyncQueue(sysSyncQueueUpdate);
				} else {
					// Case insert new request in queue
					SysSyncQueue sysSyncQueue = new SysSyncQueue();
					sysSyncQueue.setSyncType(EportConstants.SYNC_QUEUE_DET);
					sysSyncQueue.setDetFreeTime(edoUpdate.getDetFreeTime());
					sysSyncQueue.setRemark(getRemarkAfterUpdateDet(edoUpdate.getDetFreeTime(), cntrEmty.getRemark()));
					sysSyncQueue.setBlNo(edo.getBillOfLading());
					sysSyncQueue.setCntrNo(edo.getContainerNumber());
					sysSyncQueue.setJobOdrNo(cntrEmty.getJobOdrNo());
					sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
					sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
				}
			}
		}

		// check if empty cotnainer depot has update
		// if true (old != new) => check contition to udpate (container has not dropped
		// cont empty yet)
		if (StringUtils.isNotEmpty(edoUpdate.getEmptyContainerDepot())
				&& !edoUpdate.getEmptyContainerDepot().equalsIgnoreCase(odlEdo.getEmptyContainerDepot())) {
			if (cntrEmty != null && StringUtils.isNotEmpty(cntrEmty.getJobOdrNo())) {
				throw new BusinessException(String.format(
						"Empty container has already been in Da Nang port (containerNo='%s', billOfLading=%s)",
						ediDataReq.getContainerNo(), ediDataReq.getBillOfLading()));
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
		edo.setOrderNumber(ediDataReq.getOrderNumber());
		edo.setContainerNumber(ediDataReq.getContainerNo());
		edo.setReleaseNo(ediDataReq.getReleaseNo());
		edo.setDetFreeTime(ediDataReq.getDetFreeDays());
		edo.setExpiredDem(ediDataReq.getExpiryTs());
		edo.setEmptyContainerDepot(ediDataReq.getTerOfMtReturn());
		edo.setVessel(ediDataReq.getModTransName());
		edo.setVoyNo(ediDataReq.getModTransVoyage());
		edo.setBillOfLading(ediDataReq.getBillOfLading());
		edo.setSztp(ediDataReq.getSztp());
		edo.setPol(ediDataReq.getPol());
		edo.setPod(ediDataReq.getPod());
		edo.setTaxCode(ediDataReq.getConsigneeTaxcode());
		edo.setTransactionId(transactionId);
	}

	/**
	 * Get list container by containerNos (separated by comma) and convert to map
	 * container no - container info obj differentiate by fe
	 * 
	 * @param containerNos
	 * @return Map<String, ContainerInfoDto>
	 */
	private Map<String, ContainerInfoDto> getContainerInfoMapFE(String containerNos) {
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoDtoByContNos(containerNos);
		Map<String, ContainerInfoDto> cntrInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			for (ContainerInfoDto cntrInfo : cntrInfos) {
				if ("E".equalsIgnoreCase(cntrInfo.getFe())
						&& !EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntrInfo.getCntrState())
						&& !EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(cntrInfo.getCntrState())) {
					cntrInfoMap.put(cntrInfo.getCntrNo() + cntrInfo.getFe(), cntrInfo);
				} else if ("F".equalsIgnoreCase(cntrInfo.getFe())
						&& !EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntrInfo.getCntrState())) {
					cntrInfoMap.put(cntrInfo.getCntrNo() + cntrInfo.getFe(), cntrInfo);
				}
			}
		}
		return cntrInfoMap;
	}

	/**
	 * Replace det free time remark in catos if has remark or append new remark
	 * 
	 * @param detFreeTime
	 * @param currentRemark
	 * @return
	 */
	private String getRemarkAfterUpdateDet(String detFreeTime, String currentRemark) {
		boolean isAppend = true;
		if (StringUtils.isNotEmpty(currentRemark)) {
			String[] arrStr = currentRemark.split(" ");
			for (int i = 0; i < arrStr.length; i++) {
				// format remark free xxx days
				// current word is free => next will be det free time
				// change next word
				if (arrStr[i].equalsIgnoreCase("free")) {
					arrStr[i + 1] = detFreeTime;
					isAppend = false;
					currentRemark = String.join(" ", arrStr);
					break;
				}
			}
		} else {
			currentRemark = "";
		}
		if (isAppend) {
			currentRemark += StringUtils.format(", free {} days", detFreeTime);
		}
		return currentRemark;
	}
}
