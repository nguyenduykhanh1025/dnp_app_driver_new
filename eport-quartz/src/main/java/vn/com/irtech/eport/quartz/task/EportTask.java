package vn.com.irtech.eport.quartz.task;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.framework.mail.service.MailService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private ICarrierGroupService carrierGroupService;
    @Autowired
    private MailService mailService;

    public void readFileFromFolder(String groupCode) throws IOException {
        System.out.print("Đọc file EDI from folder .... " + groupCode);
        CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
        if(carrierGroup == null)
        {
            return;
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
            String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] text = content.split("'");
            EdoHistory edoHistory = new EdoHistory();
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
                } else {
                    edo.setCreateTime(timeNow);
                    edo.setCreateBy(carrierGroup.getGroupCode());
                    edoService.insertEdo(edo);
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("add");
                    edoHistoryService.insertEdoHistory(edoHistory);
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
        }
        //set time scan EDI file not yet
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        EdoHistory edoHistory = new EdoHistory();
        edoHistory.setCarrierCode(groupCode);
        Map<String, Object> timeDefine = new HashMap<>();
        timeDefine.put("startDay", yesterday);
        timeDefine.put("endDay", now);
        edoHistory.setParams(timeDefine);
        List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
        Map<String, Object> variables = new HashMap<>();
        variables.put("edoHistory", edoHistories);
        variables.put("startDay", yesterday);
        variables.put("endDay", now);
        mailService.prepareAndSend("Lịch sử truy vấn file EDI",carrierGroup.getMainEmail(), variables, "reportMailCarrier"); 
    }




   
}