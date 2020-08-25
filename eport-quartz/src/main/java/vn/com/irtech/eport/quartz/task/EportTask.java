package vn.com.irtech.eport.quartz.task;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.framework.firebase.service.FirebaseService;
import vn.com.irtech.eport.framework.mail.service.MailService;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.framework.web.service.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.system.domain.SysNotification;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.domain.SysUserToken;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.system.service.ISysNotificationService;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.system.service.ISysUserTokenService;

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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessagingException;

@Component("eportTask")
public class EportTask {

    private static final Logger logger = LoggerFactory.getLogger(EportTask.class);

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

    @Autowired
    private ISysRobotService robotService;

    @Autowired
    private IProcessOrderService processOrderService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private ConfigService configService;
    
    @Autowired
    private FirebaseService firebaseService;
    
    @Autowired
    private ISysNotificationReceiverService sysNotificationReceiverService;
    
    @Autowired
    private ISysNotificationService sysNotificationrService;
    
    @Autowired
    private ISysUserTokenService sysUserTokenService;

    public void readFileFromFolder(String groupCode, String edoPath, String backupPath) throws IOException {
        System.out.print("Đọc file EDI from folder .... " + groupCode);
        CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(groupCode);
        if(carrierGroup == null)
        {
            return;
            //TODO Return error 
        }
//        final File receiveFolder = new File(carrierGroup.getPathEdiReceive());
        final File receiveFolder = new File(edoPath);
        if (!receiveFolder.exists()) {
            receiveFolder.mkdirs();
        }
        final File destinationFolder = edoService.getFolderUploadByTime(backupPath);
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
            String[] text = content.split("'");
            EdoHistory edoHistory = new EdoHistory();
            EdoAuditLog edoAuditLog = new EdoAuditLog();
            Date timeNow = new Date();
            listEdo = edoService.readEdi(text);
            for (Edo edo : listEdo) {
                edo.setCarrierId(carrierGroup.getId());
                edo.setCreateSource("serverFSTP");
                edoHistory.setBillOfLading(edo.getBillOfLading());
                edoHistory.setOrderNumber(edo.getOrderNumber());
                edoHistory.setCarrierCode(groupCode);
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
                    edoAuditLogService.updateAuditLog(edo);
                } else {
                    edo.setCreateTime(timeNow);
                    edo.setCreateBy(carrierGroup.getGroupCode());
                    edoService.insertEdo(edo);
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("add");
                    edoHistoryService.insertEdoHistory(edoHistory);
                    edoAuditLog.setEdoId(edo.getId());
                    edoAuditLogService.addAuditLogFirst(edo);
                }
            }
            // Move file to distination folder
            fileEntry.renameTo(new File(destinationFolder + File.separator + fileEntry.getName()));
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

    /**
     * check robot connection 
     * expiredTime (minute)
     * 
     * @param expiredTime
     */
    public void pingRobot(Integer expiredTime) {
        // Get all robot online (available or busy)
        List<SysRobot> robots = robotService.selectRobotListOnline(new SysRobot());
        if (!robots.isEmpty()) {

            Date now = new Date();
            now = DateUtils.addMinutes(now, expiredTime*(-1));

            for (SysRobot robot : robots) {

                List<ProcessOrder> processOrders = processOrderService.getProcessOrderByUuid(robot.getUuId());
                if (processOrders != null && !processOrders.isEmpty()) {
                    for (ProcessOrder processOrder : processOrders) {
                        processOrder.setStatus(0);
                        processOrderService.updateProcessOrder(processOrder);

                        // Send notification to logistics
                        AjaxResult ajaxResult= null;
                        ajaxResult = AjaxResult.error("Làm lệnh thất bại, quý khách vui lòng liên hệ với bộ phận OM để được hỗ trợ thêm.");
                        webSocketService.sendMessage("/" + processOrder.getId() + "/response", ajaxResult);

                        // Send notification to OM
                        try {
                            mqttService.sendNotification(NotificationCode.NOTIFICATION_OM, "Lỗi lệnh số " + processOrder.getId(), configService.getKey("domain.admin.name") + "/om/executeCatos/detail/" + processOrder.getId());
                        } catch (Exception e) {
                            logger.warn(e.getMessage());
                        }
                    }
                }

                // Response time exceed current time
                if (robot.getResponseTime().getTime() < now.getTime()) {
                    robot.setStatus("2");
                    robotService.updateRobot(robot);
                    
                    // Send notification to IT
                    try {
                        mqttService.sendNotification(NotificationCode.NOTIFICATION_IT, "Lỗi Robot " + robot.getUuId(), configService.getKey("domain.admin.name") + "/system/robot/edit/" + robot.getId());
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                } 
            }
        }
    }
   
    /**
     * Send Notification
     * 
     */
    public void sendNotification() {
    	SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
    	sysNotificationReceiver.setSentFlg(false);
    	SysNotification sysNotification = new SysNotification();
    	sysNotification.setStatus(1L);
    	sysNotificationReceiver.setSysNotification(sysNotification);
        List<SysNotificationReceiver> notificationReceivers = sysNotificationReceiverService.getNotificationListNotSentYet(sysNotificationReceiver);
        if (CollectionUtils.isNotEmpty(notificationReceivers)) {
        	for (SysNotificationReceiver sysNotificationReceiver2 : notificationReceivers) {
        		SysUserToken sysUserToken = new SysUserToken();
				sysUserToken.setUserId(sysNotificationReceiver2.getUserId());;
				List<String> sysUserTokens = sysUserTokenService.getListDeviceTokenByUserId(sysNotificationReceiver2.getUserId());
				if (CollectionUtils.isNotEmpty(sysUserTokens)) {
					try {
						firebaseService.sendNotification(sysNotification.getTitle(), sysNotification.getContent(), sysUserTokens);
						sysNotificationReceiver2.setSentFlg(true);
		        		sysNotificationReceiverService.updateSysNotificationReceiver(sysNotificationReceiver2);
					} catch (FirebaseMessagingException e) {
						logger.error("Error send notification: " + e);
					}
				}
        	}
        }
        
    }
}