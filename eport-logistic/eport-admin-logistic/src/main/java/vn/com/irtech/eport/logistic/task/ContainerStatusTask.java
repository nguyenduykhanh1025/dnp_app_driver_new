package vn.com.irtech.eport.logistic.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Component("containerStatus")
public class ContainerStatusTask {

	private static final Logger logger = LoggerFactory.getLogger(ContainerStatusTask.class);

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor taskExecutor;
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private ICatosApiService catosApi;

	public void update(Integer records) throws IOException {
		logger.debug("Start update container status task by " + records);
		ShipmentDetail search = new ShipmentDetail();
		search.setFinishStatus("N");		// Chua finish
		search.setUserVerifyStatus("Y");	// Da xac nhan
		search.setProcessStatus("Y");		// Da lam lenh thanh cong
		// select list container for update status (finish_status = N)
		List<ShipmentDetail> details = shipmentDetailService.selectShipmentDetailList(search);
		if(details == null || details.size() == 0) {
			logger.debug("No container to update yet, skip!");
			return;
		}
		List<String> containers = new ArrayList<>();
		for(ShipmentDetail detail : details) {
			containers.add(detail.getContainerNo());
			if(containers.size() >= records) {
				// check and update container status
				checkAndUpdateStatus(containers, details);
				containers.clear();
			}
		}
		if(containers.size() > 0) {
			checkAndUpdateStatus(containers, details);
		}
	}

	private void checkAndUpdateStatus(List<String> containers, List<ShipmentDetail> details) {
		List<ContainerInfoDto> containerInfos = catosApi.getAllContainerInfoDtoByContNos(StringUtils.join(containers, ","));
		// skip if catos return nothing
		if(containerInfos == null || containerInfos.size() == 0) {
			logger.debug("Catos return nothing for list containers");
			return;
		}
		// convert container list to Map[CNTR_NO, {INFO}]
		Map<String, ContainerInfoDto> containerMap = new HashMap<>();
		for(ContainerInfoDto cntrInfo : containerInfos) {
			System.out.println(cntrInfo.getCntrNo() + "/" + cntrInfo.getFe() + "/"+cntrInfo.getUserVoy());
			containerMap.put(cntrInfo.getCntrNo() +cntrInfo.getFe(), cntrInfo);
		}
		// check list containers query
		ContainerInfoDto cntrInfo = null;
		for(ShipmentDetail detail : details) {
			// If container in list, then add to update list
			if(containers.contains(detail.getContainerNo())) {
				// Check catos status
				// cntrInfo = containerMap.get(detail.getContainerNo() + detail.getFe());
				cntrInfo = null;
				for(ContainerInfoDto cntr : containerInfos) {
					if (cntr.getCntrNo().equals(detail.getContainerNo())
							&& cntr.getFe().equalsIgnoreCase(detail.getFe())
							&& (StringUtils.nvl(cntr.getJobOdrNo(), "").equalsIgnoreCase(detail.getOrderNo())
									|| StringUtils.nvl(cntr.getJobOdrNo2(), "").equalsIgnoreCase(detail.getOrderNo()))) {
						cntrInfo = cntr;
						break;
					}
				}
				if(cntrInfo == null) {
					if("F".equalsIgnoreCase(detail.getFe())) {
						logger.debug(String.format("Container not found on catos (%s/%s/%s)", detail.getContainerNo(), detail.getFe(), detail.getVslNm() + detail.getVoyNo()));// Container not exist in catos
						detail.setFinishStatus("E"); // Error
						shipmentDetailService.updateShipmentDetail(detail);
					}
					continue; // next
				}
				// update flg
				boolean updateFlg = false;
				if(detail.getContainerStatus() == null || !detail.getContainerStatus().equalsIgnoreCase(cntrInfo.getCntrState())) {
					detail.setContainerStatus(cntrInfo.getCntrState());
					updateFlg = true;
				}
				if(!"R".equals(detail.getCustomStatus()) && "N".equals(cntrInfo.getCholdChk())) {
					detail.setCustomStatus("R");
					updateFlg = true;
				}
				// detail.setSealNo(cntrInfo.getSealNo1() + cntrInfo.getSealNo3());
				if(detail.getWgt() == null && cntrInfo.getWgt() != null) {
					detail.setWgt(cntrInfo.getWgt());
					updateFlg = true;
				}
				if(StringUtils.isEmpty(detail.getLocation()) && StringUtils.isNotEmpty(cntrInfo.getLocation())) {
					detail.setLocation(cntrInfo.getLocation());
					// updateFlg = true;
				}
				if("IX".contains(cntrInfo.getIxCd()) && "Delivered".equalsIgnoreCase(cntrInfo.getCntrState())) {
					detail.setFinishStatus("Y");
					updateFlg = true;
					// TODO find sendEmpty to evaluate empty return date
				} else if ("V".contains(cntrInfo.getIxCd())
						&& (cntrInfo.getJobOdrNo2() != null && "Delivered".equalsIgnoreCase(cntrInfo.getCntrState())
						|| cntrInfo.getJobOdrNo() != null && "Stacking".equalsIgnoreCase(cntrInfo.getCntrState()))) {
					// pickup empty / drop empty
					detail.setFinishStatus("Y");
					updateFlg = true;
				}
				if(updateFlg) {
					shipmentDetailService.updateShipmentDetail(detail);
				}
			}
		}
	}
}