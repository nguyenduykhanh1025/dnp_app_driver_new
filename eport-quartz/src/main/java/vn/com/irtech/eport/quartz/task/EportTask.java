package vn.com.irtech.eport.quartz.task;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.framework.mail.service.MailService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("eportTask")
public class EportTask {

    @Autowired
    private IEdoService edoService;
    @Autowired
    private IEdoHistoryService edoHistoryService;
    @Autowired
    private IEdoAuditLogService edoAuditLogService;
    @Autowired
    private ICarrierGroupService carrierGroupService;
    @Autowired
    private MailService mailService;

    public void readFileFromFolder(String groupCode) throws IOException {
        System.out.print("Đọc file EDI from folder .... " + groupCode);
        CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
        if(carrierGroup == null)
        {
            return;
            //TODO Return error 
        }
        final File receiveFolder = new File(carrierGroup.getPathEdiReceive());
        if (!receiveFolder.exists()) {
            receiveFolder.mkdirs();
        }
        final File destinationFolder = edoService.getFolderUploadByTime(carrierGroup.getPathEdiBackup());
        List<Edo> listEdo = new ArrayList<>();
        for (final File fileEntry : receiveFolder.listFiles()) {
            String path = fileEntry.getAbsolutePath();
            String fileName = fileEntry.getName();
            if(edoHistoryService.selectEdoHistoryByFileName(fileName) != null)
            {
                fileEntry.delete();
                continue;
            }
            String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            content = content.replace("\n", "");
            content = content.replace("\r", "");
            String[] text = content.split("\\'");
            EdoHistory edoHistory = new EdoHistory();
            EdoAuditLog edoAuditLog = new EdoAuditLog();
            Date timeNow = new Date();
            listEdo = edoService.readEdi(text);
            for (Edo edo : listEdo) {
                edo.setCarrierId(carrierGroup.getId());
                edo.setCreateSource("serverFSTP");
                edoHistory.setBillOfLading(edo.getBillOfLading());
                edoHistory.setOrderNumber(edo.getOrderNumber());
                edoHistory.setCarrierCode(edo.getCarrierCode());
                edoHistory.setCarrierId(carrierGroup.getId());
                edoHistory.setEdiContent(content);
                edoHistory.setFileName(fileName);
                edoHistory.setCreateSource("serverFSTP");
                edoHistory.setContainerNumber(edo.getContainerNumber());
                edoHistory.setCreateBy(carrierGroup.getGroupCode());
               
                Edo edoCheck = edoService.checkContainerAvailable(edo.getContainerNumber(), edo.getBillOfLading());
                if (edoCheck != null) {
                    edo.setId(edoCheck.getId());
                    edo.setUpdateTime(timeNow);
                    edo.setUpdateBy(carrierGroup.getGroupCode());
                    edoService.updateEdo(edo); // TODO
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("update");
                    edoHistoryService.insertEdoHistory(edoHistory);
                    edoAuditLog.setEdoId(edo.getId());
                    addAuditLog(edo);
                } else {
                    edo.setCreateTime(timeNow);
                    edo.setCreateBy(carrierGroup.getGroupCode());
                    edoService.insertEdo(edo);
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("add");
                    edoHistoryService.insertEdoHistory(edoHistory);
                    edoAuditLog.setEdoId(edo.getId());
                    addAuditLogFirst(edo);
                }
            }

            // Move file to distination folder
            fileEntry.renameTo(new File(destinationFolder + fileEntry.getName()));
            fileEntry.delete();
        }
        System.out.print("Thành công .... " + groupCode);

    }

    public void sendMailReportEdo(String groupCode) throws MessagingException
    {

        System.out.print("Send email  .... " + groupCode);
        CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
        if(carrierGroup == null)
        {
            return;
            //TODO Return error 
        }
        //set time scan EDI file not yet
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        Date toDate = Date.from(now);
        Date fromDate = Date.from(yesterday);
        EdoHistory edoHistory = new EdoHistory();
        edoHistory.setCarrierCode(groupCode);
        Map<String, Object> timeDefine = new HashMap<>();
        timeDefine.put("toDate", formatter.format(toDate).toString());
        timeDefine.put("fromDate", formatter.format(fromDate).toString());
        edoHistory.setParams(timeDefine);
        edoHistory.setSendMailFlag("0");
        List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
        if(edoHistories.size() == 0)
        {
            return;
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("edoHistory", edoHistories);
        variables.put("startDay", formatter.format(toDate));
        variables.put("endDay", formatter.format(fromDate));
        mailService.prepareAndSend("Lịch sử truy vấn file EDI",carrierGroup.getMainEmail(), variables, "reportMailCarrier"); 
        for(EdoHistory edoHistory2 : edoHistories)
        {
            edoHistory2.setSendMailFlag("1");
            edoHistoryService.updateEdoHistory(edoHistory2);
        }
        return;

    }

    private boolean addAuditLogFirst(Edo edo)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        EdoAuditLog edoAuditLog = new EdoAuditLog();
        edoAuditLog.setCarrierId(edo.getId());
        edoAuditLog.setCarrierCode(edo.getCarrierCode());
        edoAuditLog.setCreateBy(edo.getCarrierCode());
        edoAuditLog.setEdoId(edo.getId());
        edoAuditLog.setSeqNo((long) 1);
        edoAuditLog.setCreateTime(timeNow);
        edoAuditLog.setFieldName("Expired Dem");
        edoAuditLog.setNewValue(formatter.format(edo.getExpiredDem()).toString());
        edoAuditLogService.insertEdoAuditLogExpiredDem(edoAuditLog);
        edoAuditLog.setSeqNo((long) 2);
        edoAuditLog.setFieldName("Det Free Time");
        edoAuditLog.setNewValue(edo.getDetFreeTime().toString());
        edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        edoAuditLog.setSeqNo((long) 3);
        edoAuditLog.setFieldName("Empty Container Depot");
        edoAuditLog.setNewValue(edo.getEmptyContainerDepot().toString());
        edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        edoAuditLog.setSeqNo((long) 4);
        edoAuditLog.setFieldName("Consignee");
        edoAuditLog.setNewValue(edo.getConsignee().toString());
        edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        edoAuditLog.setSeqNo((long) 5);
        edoAuditLog.setFieldName("Vessel No");
        edoAuditLog.setNewValue(edo.getVesselNo().toString());
        edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        edoAuditLog.setSeqNo((long) 6);
        edoAuditLog.setFieldName("Voy No");
        edoAuditLog.setNewValue(edo.getVoyNo().toString());
        edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        return true;

    }

    private boolean addAuditLog(Edo edo) 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int segNo = 1;
        EdoAuditLog edoAuditLog = new EdoAuditLog();
        edoAuditLog.setCarrierId(edo.getId());
        edoAuditLog.setCarrierCode(edo.getCarrierCode());
        edoAuditLog.setCreateBy(edo.getCarrierCode());
        edoAuditLog.setEdoId(edo.getId());
        edoAuditLog.setCreateTime(timeNow);
        String maxSegNo = edoAuditLogService.selectEdoAuditLogByEdoId(edo.getId());
        if(edo.getExpiredDem() != null)
        {
            edoAuditLog.setFieldName("Expired Dem");
            EdoAuditLog edoAuditLogCheck = edoAuditLogService.selectEdoAuditLogByEdo(edoAuditLog);
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            edoAuditLog.setNewValue(formatter.format(edo.getExpiredDem()).toString());
            edoAuditLogService.insertEdoAuditLogExpiredDem(edoAuditLog);
            segNo += 1;
        }
        if(edo.getDetFreeTime() != null)
        {
            edoAuditLog.setFieldName("Det Free Time");
            EdoAuditLog edoAuditLogCheck = edoAuditLogService.selectEdoAuditLogByEdo(edoAuditLog);
            edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setNewValue(edo.getDetFreeTime().toString());
            edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
            segNo += 1;
        }
        if(edo.getEmptyContainerDepot() != null)
        {
            edoAuditLog.setFieldName("Empty Container Depot");
            EdoAuditLog edoAuditLogCheck = edoAuditLogService.selectEdoAuditLogByEdo(edoAuditLog);
            edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setNewValue(edo.getEmptyContainerDepot().toString());
            edoAuditLogService.insertEdoAuditLogDetFreeTime(edoAuditLog);
        }
        return true;
    }


   
}