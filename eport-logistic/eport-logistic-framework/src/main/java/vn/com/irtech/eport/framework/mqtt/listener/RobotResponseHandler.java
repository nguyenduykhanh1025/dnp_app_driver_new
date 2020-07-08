package vn.com.irtech.eport.framework.mqtt.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Component
public class RobotResponseHandler implements IMqttMessageListener{
	
	private static final Logger logger = LoggerFactory.getLogger(RobotResponseHandler.class);
	
	@Autowired
	private ISysRobotService robotService;
	
	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private IProcessHistoryService processHistoryService;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		logger.info("Receive message subject : " + topic);
		String messageContent = new String(message.getPayload());
		logger.info("Receive message content : " + messageContent);
		Map<String, Object> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		String uuId = map.get("id") == null ? null : map.get("id").toString();
		if (uuId == null) {
			return;
		}

		SysRobot sysRobot = robotService.selectRobotByUuId(uuId);

		if (sysRobot == null) {
			return;
		}

		String status = map.get("status") == null ? null : map.get("status").toString();
		String result = map.get("result") == null ? null : map.get("result").toString();
		String receiptId = map.get("receiptId") == null ? null : map.get("receiptId").toString();
		String invoiceNo = map.get("invoiceNo") == null ? "" : map.get("invoiceNo").toString(); 
		
		robotService.updateRobotStatusByUuId(uuId, status);

		if (receiptId != null) {
			if ("0".equals(status)) {
				this.updateShipmentDetail(result, receiptId, invoiceNo, uuId);
				this.sendMessageWebsocket(result, receiptId);
			} else if ("1".equals(status)) {
				// SAVE HISTORY ROBOT START MAKE-ORDER
				this.updateHistory(receiptId, uuId);
			}
		}
	}
	
	/**
	 * update shipment detail
	 * 
	 * @param result:   "success/error"
	 * @param receiptId
	 */
	@Transactional
	private void updateShipmentDetail(String result, String receiptId, String invoiceNo, String uuId) {
		// INIT PROCESS HISTORY
		Long id = Long.parseLong(receiptId);
		ProcessHistory processHistory = new ProcessHistory();
		processHistory.setProcessOrderId(id);
		processHistory.setRobotUuid(uuId);
		processHistory.setStatus(2); // FINISH

		ProcessOrder processOrder;

		if ("success".equalsIgnoreCase(result)) {
			// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(Long.parseLong(receiptId));
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			
			// GET PROCESS ORDER AND SET NEW STATUS TO UPDATE
			processOrder = processOrderService.selectProcessOrderById(id);
			processOrder.setReferenceNo(invoiceNo);
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);

			// SAVE BILL TO PROCESS BILL BY INVOICE NO
			if (invoiceNo != null && invoiceNo.equals("")) {
				processBillService.saveProcessBillByInvoiceNo(processOrder);
			} else {
				processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
			}

			// UPDATE STATUS OF SHIPMENT DETAIL AFTER MAKE ORDER SUCCESS
			shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", invoiceNo, processOrder);

			// SET RESULT FOR HISTORY SUCCESS
			processHistory.setResult("S");
		} else {
			// INIT PROCESS ORDER TO UPDATE
			processOrder = new ProcessOrder();
			processOrder.setId(id);
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			processHistory.setResult("F");
		}
		processHistory.setCreateTime(new Date());
		processHistoryService.insertProcessHistory(processHistory);
	}

	private void updateHistory(String receiptId, String uuId) {
		Long id = Long.parseLong(receiptId);
		ProcessHistory processHistory = new ProcessHistory();
		processHistory.setProcessOrderId(id);
		processHistory.setRobotUuid(uuId);
		processHistory.setStatus(1); // START
		processHistory.setResult("S"); // RESULT SUCCESS
		processHistory.setCreateTime(new Date());
		processHistoryService.insertProcessHistory(processHistory);
	}
	
	private void sendMessageWebsocket(String result, String receiptId) {
		AjaxResult ajaxResult= null;
		if ("success".equalsIgnoreCase(result)) {
			ajaxResult = AjaxResult.success("Làm lệnh thành công!");
		} else {
			ajaxResult = AjaxResult.error("Làm lệnh thất bại, quý khách vui lòng liên hệ với bộ phận OM để được hỗ trợ thêm.");
		}
		
		webSocketService.sendMessage("/" + receiptId + "/response", ajaxResult);
	}

}
