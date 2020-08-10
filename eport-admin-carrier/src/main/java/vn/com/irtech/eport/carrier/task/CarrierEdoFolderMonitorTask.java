package vn.com.irtech.eport.carrier.task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;

@Service
@Configurable
public class CarrierEdoFolderMonitorTask {

	private static final Logger logger = LoggerFactory.getLogger(CarrierEdoFolderMonitorTask.class);

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHistoryService edoHistoryService;

	@Autowired
	private IEdoAuditLogService edoAuditLogService;

	@Autowired
	private ICarrierGroupService carrierGroupService;

//	@Autowired
//	private ConfigService configService;

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
	public void init() throws Exception {
		if(enabled) {
			logger.info(String.format("---- Begin Monitor EDI [%s] ------", edoRootPath));
	        final File directory = new File(edoRootPath);
	        FileAlterationObserver fao = new FileAlterationObserver(directory);
	        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
	            @Override
	            public void onFileCreate(File file) {
	            	// "file" is the reference to the newly created file
                    logger.debug("Edi File created: "+ file.getAbsolutePath());
                    try {
						readEdiFile(file);
					} catch (IOException e) {
						e.printStackTrace();
						logger.error("Error while read EDI File: " + e.getMessage());
					}
	            }
	         
	            @Override
	            public void onFileDelete(File file) {
	                // code for processing deletion event
//	            	logger.info("File deleted: "+ file.getAbsolutePath());
	            }
	         
	            @Override
	            public void onFileChange(File file) {
	                // code for processing change event
//	            	logger.info("File Changed: "+ file.getAbsolutePath());
	            }
	        };
	        fao.addListener(listener);
	        monitor = new FileAlterationMonitor(interval);
	        monitor.addObserver(fao);
	        monitor.start();
		}
	}

	@PreDestroy
	public void stop() {
		if (enabled) {
			logger.info(String.format("---- Stop Monitor EDI [%s] ------", edoRootPath));
			try {
				monitor.stop();
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	private void readEdiFile(File ediFile) throws IOException {
		logger.info("Begin read edi file: " + ediFile.getAbsolutePath());
		String groupCode = getCarrierCode(ediFile);
		// Check if carrier code is not null
		if(groupCode == null) {
			logger.error("Error when read EDI File. Carrier is NULL");
			return;
		}
		// Query carrier group
		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
		if(carrierGroup == null) {
			logger.error("Error when read EDI File. Carrier Group is not exist: " + groupCode);
			return;
		}
		// Create folder for backup
		

		String content = FileUtils.readFileToString(ediFile, StandardCharsets.UTF_8);
		content = content.replaceAll("\r\n|\r|\n", "");
		String[] text = content.split("'");
		EdoHistory edoHistory = new EdoHistory();
		EdoAuditLog edoAuditLog = new EdoAuditLog();
		Date timeNow = new Date();
		List<Edo> listEdo = edoService.readEdi(text);
		for (Edo edo : listEdo) {
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
				edo.setId(edoCheck.getId());
				edo.setUpdateTime(timeNow);
				edo.setUpdateBy(carrierGroup.getGroupCode());
				edoService.updateEdo(edo); // TODO
				edoHistory.setEdoId(edo.getId());
				edoHistory.setAction("Update");
				edoHistoryService.insertEdoHistory(edoHistory);
				edoAuditLog.setEdoId(edo.getId());
				edoAuditLogService.updateAuditLog(edo);
			} else {
				edo.setCreateTime(timeNow);
				edo.setCreateBy(carrierGroup.getGroupCode());
				edoService.insertEdo(edo);
				edoHistory.setEdoId(edo.getId());
				edoHistory.setAction("Add");
				edoHistoryService.insertEdoHistory(edoHistory);
				edoAuditLog.setEdoId(edo.getId());
				edoAuditLogService.addAuditLogFirst(edo);
			}
		}
		logger.info("Finish read file: " + ediFile.getAbsolutePath());
	}

	/**
	 * Get Carrier Group Code base on folder path
	 * 
	 * @param path
	 * @return
	 */
	private String getCarrierCode(File f) {
		if(f != null && f.exists()) {
			return f.getParentFile().getName();
		}
		return null;
	}

//	public void readFileFromFolder(String groupCode) throws IOException {
//		logger.info("Begin read file");
//		// System.out.print("Đọc file EDI from folder .... " + groupCode);
//		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
//		if (carrierGroup == null) {
//			return;
//			// TODO Return error
//		}
//		final File receiveFolder = new File(carrierGroup.getPathEdiReceive());
//		if (!receiveFolder.exists()) {
//			receiveFolder.mkdirs();
//		}
//		final File destinationFolder = edoService.getFolderUploadByTime(carrierGroup.getPathEdiBackup());
//		List<Edo> listEdo = new ArrayList<>();
//		for (final File fileEntry : receiveFolder.listFiles()) {
//			String path = fileEntry.getAbsolutePath();
//			String fileName = fileEntry.getName();
//			if (edoHistoryService.selectEdoHistoryByFileName(fileName) != null) {
//				fileEntry.delete();
//				continue;
//			}
//			String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
//			content = content.replace("\n", "");
//			content = content.replace("\r", "");
//			String[] text = content.split("'");
//			EdoHistory edoHistory = new EdoHistory();
//			EdoAuditLog edoAuditLog = new EdoAuditLog();
//			Date timeNow = new Date();
//			listEdo = edoService.readEdi(text);
//			for (Edo edo : listEdo) {
//				edo.setCarrierId(carrierGroup.getId());
//				edo.setCreateSource("serverFSTP");
//				edoHistory.setBillOfLading(edo.getBillOfLading());
//				edoHistory.setOrderNumber(edo.getOrderNumber());
//				edoHistory.setCarrierCode(edo.getCarrierCode());
//				edoHistory.setCarrierId(carrierGroup.getId());
//				edoHistory.setEdiContent(content);
//				edoHistory.setFileName(fileName);
//				edoHistory.setCreateSource("serverFSTP");
//				edoHistory.setContainerNumber(edo.getContainerNumber());
//				edoHistory.setCreateBy(carrierGroup.getGroupCode());
//
//				Edo edoCheck = edoService.checkContainerAvailable(edo.getContainerNumber(), edo.getBillOfLading());
//				if (edoCheck != null) {
//					edo.setId(edoCheck.getId());
//					edo.setUpdateTime(timeNow);
//					edo.setUpdateBy(carrierGroup.getGroupCode());
//					edoService.updateEdo(edo); // TODO
//					edoHistory.setEdoId(edo.getId());
//					edoHistory.setAction("update");
//					edoHistoryService.insertEdoHistory(edoHistory);
//					edoAuditLog.setEdoId(edo.getId());
//					edoAuditLogService.updateAuditLog(edo);
//				} else {
//					edo.setCreateTime(timeNow);
//					edo.setCreateBy(carrierGroup.getGroupCode());
//					edoService.insertEdo(edo);
//					edoHistory.setEdoId(edo.getId());
//					edoHistory.setAction("add");
//					edoHistoryService.insertEdoHistory(edoHistory);
//					edoAuditLog.setEdoId(edo.getId());
//					edoAuditLogService.addAuditLogFirst(edo);
//				}
//			}
//			// Move file to distination folder
//			fileEntry.renameTo(new File(destinationFolder + File.separator + fileEntry.getName()));
//			fileEntry.delete();
//		}
//		System.out.print("Thành công .... " + groupCode);
//
//	}

}