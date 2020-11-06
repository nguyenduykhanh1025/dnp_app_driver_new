package vn.com.irtech.eport.carrier.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.ICarrierQueueService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Service
@Configurable
public class CarrierEdoFolderMonitorTask {

	private static final Logger logger = LoggerFactory.getLogger(CarrierEdoFolderMonitorTask.class);

	private BlockingQueue<String> ediFileQueue = new LinkedBlockingQueue<String>();

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHistoryService edoHistoryService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ICarrierQueueService queueService;

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor taskExecutor;

	@Value("${eport.edi.rootPath}")
	private String edoRootPath;

	@Value("${eport.edi.backupPath}")
	private String edoBackupPath;

	@Value("${eport.edi.interval}")
	private long interval = 3000;

	@Value("${eport.edi.enabled}")
	private boolean enabled = false;

	private FileAlterationMonitor monitor;

	@PostConstruct
	private void init() throws Exception {
		if (enabled) {
			logger.info(String.format("---- Begin Monitor EDI [%s] ------", edoRootPath));
			final File directory = new File(edoRootPath);
			FileAlterationObserver fao = new FileAlterationObserver(directory);
			FileAlterationListener listener = new FileAlterationListenerAdaptor() {
				@Override
				public void onFileCreate(File file) {
					logger.debug("New Edi: " + file.getAbsolutePath());
					ediFileQueue.offer(file.getAbsolutePath());
				}

				@Override
				public void onFileDelete(File file) {
					// code for processing deletion event
//	            	logger.info("File deleted: "+ file.getAbsolutePath());
				}

				@Override
				public void onFileChange(File file) {
					logger.info("EDI Changed: " + file.getAbsolutePath());
				}
			};
			fao.addListener(listener);
			monitor = new FileAlterationMonitor(interval);
			monitor.addObserver(fao);
			monitor.start();

			// Start thread to queue process
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					logger.info("-------------Start Queue EDO File-------------");
					while (true) {
						try {
							String filePath = ediFileQueue.take();
							readEdiFile(new File(filePath));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Error while read EDI file", e);
						}
					}
				}
			});
		}
	}

	@PreDestroy
	private void stop() {
		if (enabled) {
			logger.info(String.format("---- Stop Monitor EDI [%s] ------", edoRootPath));
			try {
				monitor.stop();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	/**
	 * Put list file to file queue to process EDI
	 * 
	 * @param filePath
	 */
	public void offerQueue(String filePath) {
		ediFileQueue.offer(filePath);
	}

	private void readEdiFile(File ediFile) throws IOException {
		logger.debug("Begin read edi file: " + ediFile.getAbsolutePath());
		String groupCode = getCarrierCode(ediFile);
		// Check if carrier code is not null
		if (groupCode == null) {
			logger.error("Error when read EDI File. Carrier is NULL");
			return;
		}
		// Query carrier group
		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
		if (carrierGroup == null) {
			logger.error("Error when read EDI File. Carrier Group is not exist: " + groupCode);
			return;
		}
		// Read file content
		String content = FileUtils.readFileToString(ediFile, StandardCharsets.UTF_8);
		content = content.replaceAll("\r\n|\r|\n", "");
		String[] text = content.split("'");
		EdoHistory edoHistory = new EdoHistory();
		EdoAuditLog edoAuditLog = new EdoAuditLog();
		Date timeNow = new Date();

		// Read edi file and parse to edo object
		List<Edo> listEdo = edoService.readEdi(text);

		// can not read edo file
		if(listEdo == null || listEdo.size() == 0) {
			// create error folder
			moveToErrorFolder(groupCode, ediFile);
			logger.error("No Container found in EDI File: " + ediFile.getName() + "\nEDI Content:\n" + content);
			return;
		}

		// For get list containers string to get cntr info from catos
		String containers = "";
		for (Edo edo : listEdo) {
			containers += edo.getContainerNumber() + ",";
		}
		Map<String, ContainerInfoDto> cntrMap = getMapCntrInfoCatos(containers.substring(0, containers.length() - 1),
				listEdo.get(0).getBillOfLading());

		// loop and insert
		for (Edo edo : listEdo) {
			try {
				edo.setCarrierId(carrierGroup.getId());
				edo.setCreateSource("EDI");
				edoHistory.setBillOfLading(edo.getBillOfLading());
				edoHistory.setOrderNumber(edo.getOrderNumber());
				edoHistory.setCarrierCode(edo.getCarrierCode());
				edoHistory.setCarrierId(carrierGroup.getId());
				edoHistory.setEdiContent(content);
				edoHistory.setFileName(ediFile.getName());
				edoHistory.setCreateSource("EDI");
				edoHistory.setContainerNumber(edo.getContainerNumber());
				edoHistory.setCreateBy(carrierGroup.getGroupCode());
	
				Edo edoCheck = edoService.checkContainerAvailable(edo.getContainerNumber(), edo.getBillOfLading());
				if (edoCheck != null) {
					ContainerInfoDto cntrInfo = cntrMap.get(edoCheck.getContainerNumber());

					Edo edoUpdate = new Edo();
					edoUpdate.setId(edoCheck.getId());
					edoUpdate.setUpdateTime(timeNow);
					edoUpdate.setUpdateBy(carrierGroup.getGroupCode());

					edoUpdate.setConsignee(edo.getConsignee());
					edoUpdate.setDetFreeTime(edo.getDetFreeTime());
					edoUpdate.setEmptyContainerDepot(edo.getEmptyContainerDepot());
					edoUpdate.setTaxCode(edo.getTaxCode());
					edoUpdate.setExpiredDem(edo.getExpiredDem());
					// Validate edo field can update
					if (cntrInfo == null || StringUtils.isNotEmpty(cntrInfo.getJobOdrNo2())) {
						// Case container don't has job order no in catos
						// => can update
						edoUpdate.setCarrierCode(edo.getCarrierCode());
						edoUpdate.setBusinessUnit(edo.getBusinessUnit());
						edoUpdate.setOrderNumber(edo.getOrderNumber());
						edoUpdate.setContainerNumber(edo.getContainerNumber());
						edoUpdate.setReleaseNo(edo.getReleaseNo());
						edoUpdate.setVessel(edo.getVessel());
						edoUpdate.setVoyNo(edo.getVoyNo());
						edoUpdate.setBillOfLading(edo.getBillOfLading());
						edoUpdate.setSztp(edo.getSztp());
						edoUpdate.setPol(edo.getPol());
						edoUpdate.setPod(edo.getPod());
						edoUpdate.setConsignee(edo.getConsignee());
					}

					// validate expired dem if has update
					if (edoUpdate.getExpiredDem() != null && edoCheck.getExpiredDem() != null
							&& edoCheck.getExpiredDem().compareTo(edoUpdate.getExpiredDem()) != 0) {
						if (cntrInfo != null && StringUtils.isNotEmpty(cntrInfo.getJobOdrNo2())) {
							// container has job order no in catos
							// Check if expired dem need update
							edoUpdate.setJobOrderNo(cntrInfo.getJobOdrNo2());
							queueService.offerEdoExtendExpiredDem(edoUpdate);
						}
					}

					// Container has not been ordered to drop to danang port yet
					if (cntrInfo == null || StringUtils.isEmpty(cntrInfo.getJobOdrNo())) {
						edoUpdate.setTaxCode(edo.getTaxCode());
						edoUpdate.setEmptyContainerDepot(edo.getEmptyContainerDepot());
					}

					// Update eDO
					edoService.updateEdo(edoUpdate);
					// Update history
					edoHistory.setEdoId(edoUpdate.getId());
					edoHistory.setAction("Update");
					edoHistoryService.insertEdoHistory(edoHistory);
					edoAuditLog.setEdoId(edo.getId());
					edoAuditLogService.updateAuditLog(edoUpdate);
				} else {
					edo.setCreateTime(timeNow);
					edo.setCreateBy(carrierGroup.getGroupCode());
					// insert eDO
					edoService.insertEdo(edo);
					// insert history
					edoHistory.setEdoId(edo.getId());
					edoHistory.setAction("Add");
					edoHistoryService.insertEdoHistory(edoHistory);
					edoAuditLog.setEdoId(edo.getId());
					edoAuditLogService.addAuditLogFirst(edo);
				}
			} catch (Exception ex) {
				logger.error("Error while INSERT EDI", ex);
				moveToErrorFolder(groupCode, ediFile);
				return;
			}
		}
		// Move file to backup folder
		moveToBackupFolder(groupCode, ediFile);
		logger.debug("Finish read file: " + ediFile.getAbsolutePath());
	}

	/**
	 * Get Carrier Group Code base on folder path
	 * 
	 * @param path
	 * @return
	 */
	private String getCarrierCode(File f) {
		if (f != null && f.exists()) {
			// Folder structure /OPE0001/EDI/file.edi
			String folderName = f.getParentFile().getParentFile().getName(); // YML or YML0001
			// Get first 3 CHAR
			if (folderName != null) {
				if (folderName.length() > 3) {
					return folderName.substring(0, 3);
				}
				return folderName;
			}
		}
		return null;
	}
	
	private void moveToBackupFolder(String groupCode, File ediFile) throws IOException {
		// Create backup folder: $backupRootPath /{carrierCode}/YYYYMM
		String targetFolder = edoBackupPath + File.separator + groupCode + File.separator
				+ new SimpleDateFormat("yyyyMM").format(new Date());

		File targetDir = new File(targetFolder);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// backup file path: backupFolder / edi.file.backup
		File targetFile = new File(targetFolder + File.separator + ediFile.getName());
		// If previous version is exist, rename with timestamp
		if (targetFile.exists()) {
			targetFile = new File(targetFolder + File.separator + ediFile.getName() + "."
					+ String.valueOf(System.currentTimeMillis()));
		}
		// Move file to error folder
		Files.move(ediFile, targetFile);
	}

	private void moveToErrorFolder(String groupCode, File ediFile) throws IOException {
		// Create error folder: $backupRootPath /{carrierCode}/YYYYMM_Error
		String errorFolder =  edoBackupPath + File.separator + groupCode + File.separator
				+ new SimpleDateFormat("yyyyMM").format(new Date()) + "_Error";
		// create error folder
		File errorDir = new File(errorFolder);
		if (!errorDir.exists()) {
			errorDir.mkdirs();
		}
		// backup file path: backupFolder / edi.file.backup
		File errorFile = new File(errorFolder + File.separator + ediFile.getName());
		// If previous version is exist, rename with timestamp
		if (errorFile.exists()) {
			errorFile = new File(errorFolder + File.separator + ediFile.getName() + "."
					+ String.valueOf(System.currentTimeMillis()));
		}
		// Move file to error folder
		Files.move(ediFile, errorFile);
	}

	private Map<String, ContainerInfoDto> getMapCntrInfoCatos(String containers, String blNo) {
		List<ContainerInfoDto> containerInfoDtos = catosApiService.getContainerInfoDtoByContNos(containers);
		Map<String, ContainerInfoDto> containerInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
				if (StringUtils.isNotEmpty(containerInfoDto.getBlNo())
						&& containerInfoDto.getBlNo().equalsIgnoreCase(blNo)) {
					containerInfoMap.put(containerInfoDto.getCntrNo(), containerInfoDto);
				}
			}
		}
		return containerInfoMap;
	}
}