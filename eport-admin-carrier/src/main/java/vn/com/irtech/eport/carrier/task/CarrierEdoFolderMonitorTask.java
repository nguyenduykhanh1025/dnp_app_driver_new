package vn.com.irtech.eport.carrier.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
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

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.domain.SysSyncQueue;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysDictDataService;
import vn.com.irtech.eport.system.service.ISysSyncQueueService;

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
	private ISysSyncQueueService sysSyncQueueService;

	@Autowired
	private ISysDictDataService dictDataService;

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
							// Check end delays for 5s with file create time
							checkAndDelay(filePath, 5000);
							readEdiFile(new File(filePath));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Error while read EDI file", e);
						}
					}
				}

				private void checkAndDelay(String filePath, int delayMs) {
					try {
						// read file create time
						BasicFileAttributes attr = Files.readAttributes(new File(filePath).toPath(),
								BasicFileAttributes.class);
						long fileTimeInMs = attr.creationTime().toMillis();
						long currentTimeInMs = System.currentTimeMillis();
						long diff = currentTimeInMs - fileTimeInMs;
						// Delay from 0~ delay mili-seconds
						if (diff > 0 && diff < delayMs) {
							Thread.sleep(delayMs - diff);
						}
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
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

		// Check file size diff => file upload not complete
		long fileSize = Files.size(ediFile.toPath());
		// Read file content
		String content = FileUtils.readFileToString(ediFile, StandardCharsets.UTF_8);
		content = content.replaceAll("\r\n|\r|\n", "");
		String[] text = content.split("'");
		EdoHistory edoHistory = new EdoHistory();
		EdoAuditLog edoAuditLog = new EdoAuditLog();
		Date timeNow = new Date();

		// Read edi file and parse to edo object
		List<Edo> listEdo = edoService.readEdi(text);
		// Check if filesize has changed
		if (Files.size(ediFile.toPath()) - fileSize != 0) {
			// move to queue to read again
			logger.debug("File has been changed, read again: " + ediFile.toPath());
			ediFileQueue.offer(ediFile.getAbsolutePath());
			return;
		}
		// can not read edo file
		if(listEdo == null || listEdo.size() == 0) {
			// create error folder
			moveToErrorFolder(groupCode, ediFile);
			logger.error("No Container found in EDI File: " + ediFile.getName() + "\nEDI Content:\n" + content);
			return;
		}

		// For get list containers string to get container info from catos
		String containers = "";
		for (Edo edo : listEdo) {
			containers += edo.getContainerNumber() + ",";
		}
		// remove last comma
		containers = containers.substring(0, containers.length() - 1);
		Map<String, ContainerInfoDto> cntrMap = getContainerInfoMapFE(containers);

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
					ContainerInfoDto cntrFull = cntrMap.get(edoCheck.getContainerNumber() + "F");
					ContainerInfoDto cntrEmty = cntrMap.get(edoCheck.getContainerNumber() + "E");

					Edo edoUpdate = new Edo();
					edoUpdate.setId(edoCheck.getId());
					edoUpdate.setUpdateTime(timeNow);
					edoUpdate.setUpdateBy(carrierGroup.getGroupCode());

					edoUpdate.setConsignee(edo.getConsignee());
					edoUpdate.setDetFreeTime(edo.getDetFreeTime());
					edoUpdate.setEmptyContainerDepot(edo.getEmptyContainerDepot());
					edoUpdate.setTaxCode(edo.getTaxCode());
					edoUpdate.setExpiredDem(edo.getExpiredDem());
					edoUpdate.setCarrierCode(edoCheck.getCarrierCode());
					// Validate edo field can update
					if (cntrFull == null || StringUtils.isEmpty(cntrFull.getJobOdrNo2())) {
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
						if (cntrFull != null && StringUtils.isNotEmpty(cntrFull.getJobOdrNo2())) {
							// Get old request if exist, update else insert new request
							SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
							sysSyncQueueParam.setBlNo(edoUpdate.getBillOfLading());
							sysSyncQueueParam.setCntrNo(edoUpdate.getContainerNumber());
							sysSyncQueueParam.setJobOdrNo(cntrFull.getJobOdrNo2());
							sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DEM);
							sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
							List<SysSyncQueue> sysSyncQueues = sysSyncQueueService
									.selectSysSyncQueueList(sysSyncQueueParam);
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
								sysSyncQueue.setBlNo(edoUpdate.getBillOfLading());
								sysSyncQueue.setCntrNo(edoUpdate.getContainerNumber());
								sysSyncQueue.setJobOdrNo(cntrFull.getJobOdrNo2());
								sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
								sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
							}
						}
					}

					// Check if carrier need update det free time
					if (dictDataService.selectDictLabel("carrier_code_update_detention",
							edo.getCarrierCode()) != null) {
						// Carrier need update det free time
						// Check det free time is updated
						if (edoUpdate.getDetFreeTime() != null && edoCheck.getDetFreeTime() != null
								&& !edoCheck.getDetFreeTime().equalsIgnoreCase(edoUpdate.getDetFreeTime())) {
							if (cntrEmty == null || StringUtils.isEmpty(cntrEmty.getJobOdrNo())) {
								// Get old request if exist, update else insert new request
								SysSyncQueue sysSyncQueueParam = new SysSyncQueue();
								sysSyncQueueParam.setBlNo(edoUpdate.getBillOfLading());
								sysSyncQueueParam.setCntrNo(edoUpdate.getContainerNumber());
								sysSyncQueueParam.setJobOdrNo(cntrEmty.getJobOdrNo());
								sysSyncQueueParam.setSyncType(EportConstants.SYNC_QUEUE_DET);
								sysSyncQueueParam.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
								List<SysSyncQueue> sysSyncQueues = sysSyncQueueService
										.selectSysSyncQueueList(sysSyncQueueParam);
								if (CollectionUtils.isNotEmpty(sysSyncQueues)) {
									// Case update request in queue
									SysSyncQueue sysSyncQueueUpdate = new SysSyncQueue();
									sysSyncQueueUpdate.setId(sysSyncQueues.get(0).getId());
									sysSyncQueueUpdate.setDetFreeTime(edoUpdate.getDetFreeTime());
									sysSyncQueueUpdate.setOldRemark(cntrEmty.getRemark());
									sysSyncQueueUpdate.setNewRemark(
											getRemarkAfterUpdateDet(edoUpdate.getDetFreeTime(), cntrEmty.getRemark()));
									sysSyncQueueService.updateSysSyncQueue(sysSyncQueueUpdate);
								} else {
									// Case insert new request in queue
									SysSyncQueue sysSyncQueue = new SysSyncQueue();
									sysSyncQueue.setSyncType(EportConstants.SYNC_QUEUE_DET);
									sysSyncQueue.setDetFreeTime(edoUpdate.getDetFreeTime());
									sysSyncQueue.setOldRemark(cntrEmty.getRemark());
									sysSyncQueue.setNewRemark(
											getRemarkAfterUpdateDet(edoUpdate.getDetFreeTime(), cntrEmty.getRemark()));
									sysSyncQueue.setBlNo(edoUpdate.getBillOfLading());
									sysSyncQueue.setCntrNo(edoUpdate.getContainerNumber());
									sysSyncQueue.setJobOdrNo(cntrEmty.getJobOdrNo());
									sysSyncQueue.setStatus(EportConstants.SYNC_QUEUE_STATUS_WAITING);
									sysSyncQueueService.insertSysSyncQueue(sysSyncQueue);
								}
							}
						}

					}

					// Container has not been ordered to drop to danang port yet
					if (cntrEmty == null || StringUtils.isEmpty(cntrEmty.getJobOdrNo())) {
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
		com.google.common.io.Files.move(ediFile, targetFile);
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
		com.google.common.io.Files.move(ediFile, errorFile);
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
	 * Update new remark Remove and append new detention info with 2 format number
	 * and date
	 * 
	 * @param detFreeTime
	 * @param currentRemark
	 * @return
	 */
	private String getRemarkAfterUpdateDet(String detFreeTime, String currentRemark) {
		String newRemark = "";
		boolean detIsNumber = false;
		try {
			Integer.parseInt(detFreeTime);
			detIsNumber = true;
			logger.debug("Detention format is number");
		} catch (Exception e) {
			logger.debug("Detention format is date");
		}
		if (detIsNumber) {
			if (StringUtils.isNotEmpty(currentRemark)) {
				String[] arrStr = currentRemark.split(" ");
				for (int i = 0; i < arrStr.length; i++) {
					// format remark: free xxx days
					if (arrStr[i].equalsIgnoreCase("free")) {
						i = i + 2; // Ignore next word (xxx days)
						continue;
					}
					// format remark: han dd/mm/yyyy
					if (arrStr[i].equalsIgnoreCase("han")) {
						i++; // Ignore next word (han dd/mm/yyyy)
						continue;
					}
					newRemark += arrStr[i] + " ";
				}
			}
			if (StringUtils.isNotEmpty(newRemark)) {
				newRemark += ", ";
			}
			newRemark += StringUtils.format("free {} days", detFreeTime);
		} else {
			if (StringUtils.isNotEmpty(currentRemark)) {
				String[] arrStr = currentRemark.split(" ");
				for (int i = 0; i < arrStr.length; i++) {
					// format remark: free xxx days
					if (arrStr[i].equalsIgnoreCase("free")) {
						i = i + 2; // Ignore next word (xxx days)
						continue;
					}
					// format remark: han dd/mm/yyyy
					if (arrStr[i].equalsIgnoreCase("han")) {
						i++; // Ignore next word (han dd/mm/yyyy)
						continue;
					}
					newRemark += arrStr[i] + " ";
				}
			}
			if (StringUtils.isNotEmpty(newRemark)) {
				newRemark += ", ";
			}
			newRemark += StringUtils.format("han {}", detFreeTime);
		}
		return newRemark;
	}
}