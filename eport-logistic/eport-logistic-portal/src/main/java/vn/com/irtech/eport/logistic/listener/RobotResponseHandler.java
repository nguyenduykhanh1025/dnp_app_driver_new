package vn.com.irtech.eport.logistic.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ContainerHoldInfo;
import vn.com.irtech.eport.logistic.dto.ProcessJsonData;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
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

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ConfigService configService;
	
	@Autowired
	private IEdoService edoService;
	
	@Autowired
	private IShipmentService shipmentService;
	
	@Autowired
	private ICatosApiService catosApiService;
	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}
	
	private void processMessage(String topic, MqttMessage message) throws Exception {
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

		String orderNo = map.get("orderNo") == null ? null : map.get("orderNo").toString();
		String orderType = map.get("serviceType") == null ? null : map.get("serviceType").toString();
		Integer serviceType = Integer.parseInt(orderType);
		SysRobot sysRobot = robotService.selectRobotByUuId(uuId);

		if (sysRobot == null) {
			return;
		}

		String status = map.get("status") == null ? null : map.get("status").toString();
		String result = map.get("result") == null ? null : map.get("result").toString();
		String receiptId = map.get("receiptId") == null ? null : map.get("receiptId").toString();
		String invoiceNo = map.get("invoiceNo") == null ? "" : map.get("invoiceNo").toString(); 
		String msg = map.get("msg") == null ? "" : map.get("msg").toString(); 
		String errorImagePath = map.get("errorImagePath") == null ? "" : map.get("errorImagePath").toString(); 

		if (receiptId != null) {
			if (EportConstants.ROBOT_STATUS_AVAILABLE.equals(status)) {
				if (serviceType == EportConstants.SERVICE_PICKUP_FULL 
						|| serviceType == EportConstants.SERVICE_DROP_EMPTY 
						|| serviceType == EportConstants.SERVICE_PICKUP_EMPTY 
						|| serviceType == EportConstants.SERVICE_DROP_FULL 
						|| serviceType == EportConstants.SERVICE_SHIFTING) {
					this.updateShipmentDetail(result, receiptId, invoiceNo, uuId, orderNo, serviceType, msg, errorImagePath);
				}	
				switch (serviceType) {
					case EportConstants.SERVICE_CHANGE_VESSEL:
						this.updateChangeVesselOrder(result, receiptId, uuId);
						break;
					case EportConstants.SERVICE_CREATE_BOOKING:
						this.updateCreateBookingOrder(result, receiptId, uuId, msg);
						break;
					case EportConstants.SERVICE_EXTEND_DATE:
						this.updateExtensionDateOrder(result, receiptId, uuId);
						break;
					case EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD:
						this.updateTerminalCustomHold(result, receiptId, uuId);
						break;
					case EportConstants.SERVICE_CANCEL_DROP_FULL:
						break;
					case EportConstants.SERVICE_CANCEL_PICKUP_EMPTY:
						break;
					case EportConstants.SERVICE_EXPORT_RECEIPT:
						this.updateExportReceipt(result, receiptId, uuId);
						break;
					default:
						break;
				}
				this.sendMessageWebsocket(result, receiptId);
				status = this.assignNewProcessOrder(sysRobot);
			} else if (EportConstants.ROBOT_STATUS_BUSY.equals(status)) {
				// SAVE HISTORY ROBOT START MAKE-ORDER
				Long processOrderId = Long.parseLong(receiptId); 
				this.updateHistory(processOrderId, uuId, serviceType, "");
			}
		}

		robotService.updateRobotStatusByUuId(uuId, status);
		
	}
	
	/**
	 * update shipment detail
	 * 
	 * @param result:   "success/error"
	 * @param receiptId
	 */
	@Transactional
	private void updateShipmentDetail(String result, String receiptId, String invoiceNo, String robotUuId, String orderNo, Integer serviceType, String msgError, String errorImagePath) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);

		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if(processOrder == null) {
			// Co loi bat thuong xay ra. order khong ton tai
			throw new IllegalArgumentException("Process order not exist");
		}
		
		// Get the true name of the service to make message send to om
		String serviceName = "";
		String url = "";
		switch (processOrder.getServiceType()) {
			case EportConstants.SERVICE_PICKUP_FULL:
				serviceName = "bốc container hàng";
				url = EportConstants.URL_OM_RECEIVE_F_SUPPORT;
				break;
			case EportConstants.SERVICE_PICKUP_EMPTY:
				serviceName = "bốc container rỗng";
				url = EportConstants.URL_OM_RECEIVE_E_SUPPORT;
				break;
			case EportConstants.SERVICE_DROP_FULL:
				serviceName = "hạ container hàng";
				url = EportConstants.URL_OM_SEND_F_SUPPORT;
				break;
			case EportConstants.SERVICE_DROP_EMPTY:
				serviceName = "hạ container rỗng";
				url = EportConstants.URL_OM_SEND_E_SUPPORT;
				break;
			case EportConstants.SERVICE_SHIFTING:
				serviceName = "dịch chuyển container";
			default:
				break;
		}
		
		String title = "";
		String msg = "";
		String historyResult = "";
		if ("success".equalsIgnoreCase(result)) {
			// GET LIST SHIPMENT DETAIL BY PROCESS ORDER ID
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(Long.parseLong(receiptId));
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			
			processOrder.setOrderNo(orderNo);
			processOrder.setInvoiceNo(invoiceNo);
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);

			// SAVE BILL TO PROCESS BILL BY INVOICE NO
			if (invoiceNo != null && !invoiceNo.equals("")) {
				processBillService.saveProcessBillByInvoiceNo(processOrder);
			} else if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
				processBillService.saveProcessBillWithCredit(shipmentDetails, processOrder);
			} else if (processOrder.getProcessData() != null) {
				ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
				processBillService.saveShiftingBillWithCredit(processJsonData.getShipmentDetailIds(), processOrder);
				for (Long shipmentDetailId : processJsonData.getPrePickupContIds()) {
					ShipmentDetail prePickupShipmentDetail = new ShipmentDetail();
					prePickupShipmentDetail.setId(shipmentDetailId);
					prePickupShipmentDetail.setPrePickupPaymentStatus("Y");
					shipmentDetailService.updateShipmentDetail(prePickupShipmentDetail);
				}
			}

			// UPDATE STATUS OF SHIPMENT DETAIL AFTER MAKE ORDER SUCCESS
			if (processOrder.getServiceType() != EportConstants.SERVICE_SHIFTING) {
				shipmentDetailService.updateProcessStatus(shipmentDetails, "Y", invoiceNo, processOrder);
				Shipment shipment = shipmentService.selectShipmentById(processOrder.getShipmentId());
				if (processOrder.getServiceType() == EportConstants.SERVICE_PICKUP_FULL && "1".equals(shipment.getEdoFlg())) {
					for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
						Edo edo = new Edo();
						edo.setBillOfLading(shipment.getBlNo());
						edo.setContainerNumber(shipmentDetail2.getContainerNo());
						edo.setStatus("2"); // status process order has been made for this edo
						edoService.updateEdoByBlCont(edo);
					}
				}
			}
			
			// Send notification order success to om to check whether some data is wrong or not
//			title = "ePort: Thông báo làm lệnh " + serviceName + " thành công bởi robot " + robotUuId + ".";
//			msg = "Robot làm lệnh " + serviceName + " thành công cho mã lô " + processOrder.getShipmentId() + " Job Order No " + processOrder.getOrderNo();
			
			// SET RESULT FOR HISTORY SUCCESS
			historyResult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
			if (EportConstants.SERVICE_PICKUP_FULL == processOrder.getServiceType()) {
				this.sendProcessOrderHoldTerminal(processOrder, shipmentDetails);
			}
		} else {
			
			// Send notification order success to om to check whether some data is wrong or not
			title = "ePort: Thông báo làm lệnh " + serviceName + " bị lỗi bởi robot " + robotUuId + ".";
			msg = "Robot làm lệnh " + serviceName + " thất bại cho mã lô " + processOrder.getShipmentId() + " Job Order No " + processOrder.getOrderNo() + ": " + msgError;
			
			// INIT PROCESS ORDER TO UPDATE
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setMsg(msgError); // Set message error from robot
			processOrder.setErrorImagePath(errorImagePath); // Set error image path for om check
			if (orderNo != null) {
				processOrder.setOrderNo(orderNo);
			}
			processOrderService.updateProcessOrder(processOrder);
			
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(Long.parseLong(receiptId));
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
				shipmentDetail2.setProcessStatus("E");
				shipmentDetailService.updateShipmentDetail(shipmentDetail2);
			}
			// SET RESULT FOR HISTORY FAILED
			historyResult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
			
			// Send notification for om
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg, configService.getKey("domain.admin.name") + url, EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		
		// Update history
		updateHistory(processOrderId, robotUuId, serviceType, historyResult);
	}

	private void updateHistory(Long processOrderId, String robotUuId, Integer serviceType, String result) {
		ProcessHistory history = processHistoryService.selectProcessingHistory(processOrderId, robotUuId, serviceType);
		if(history != null) {
			history.setResult(result); // RESULT SUCCESS
			history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_FINISHED);
			history.setFinishTime(new Date());
			processHistoryService.updateProcessHistory(history);
		} else {
			// insert new record
			history = new ProcessHistory();
			history.setCreateTime(new Date());
			history.setRobotUuid(robotUuId);
			history.setServiceType(serviceType);
			history.setStatus(EportConstants.PROCESS_HISTORY_STATUS_START);
			history.setProcessOrderId(processOrderId);
			processHistoryService.insertProcessHistory(history);
		}
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

	public String assignNewProcessOrder(SysRobot robot) throws MqttException {
		// Get service types of robot
		String serviceTypes = "";

		if (robot.getIsReceiveContFullOrder()) {
			serviceTypes += EportConstants.SERVICE_PICKUP_FULL + ",";
		}
		if (robot.getIsReceiveContEmptyOrder()) {
			serviceTypes += EportConstants.SERVICE_PICKUP_EMPTY + ",";
		}
		if (robot.getIsSendContFullOrder()) {
			serviceTypes += EportConstants.SERVICE_DROP_FULL + ",";
		}
		if (robot.getIsSendContEmptyOrder()) {
			serviceTypes += EportConstants.SERVICE_DROP_EMPTY + ",";
		}
		if (robot.getIsShiftingContOrder()) {
			serviceTypes += EportConstants.SERVICE_SHIFTING + ",";
		}
		if (robot.getIsChangeVesselOrder()) {
			serviceTypes += EportConstants.SERVICE_CHANGE_VESSEL + ",";
		}
		if (robot.getIsCreateBookingOrder()) {
			serviceTypes += EportConstants.SERVICE_CREATE_BOOKING + ",";
		}
		if (robot.getIsExtensionDateOrder()) {
			serviceTypes += EportConstants.SERVICE_EXTEND_DATE + ",";
		}
		if (robot.getIsChangeTerminalCustomHold()) {
			serviceTypes += EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD + ",";
		}
		if (robot.getIsCancelSendContFullOrder()) {
			serviceTypes += EportConstants.SERVICE_CANCEL_DROP_FULL + ",";
		}
		if (robot.getIsCancelReceiveContEmptyOrder()) {
			serviceTypes += EportConstants.SERVICE_CANCEL_PICKUP_EMPTY + ",";
		}
		if (robot.getIsExportReceipt()) {
			serviceTypes += EportConstants.SERVICE_EXPORT_RECEIPT + ",";
		}

		if (serviceTypes.length() > 0) {
			serviceTypes = serviceTypes.substring(0, serviceTypes.length()-1);
		}

		// Find process order for robot
		ProcessOrder reqProcessOrder = processOrderService.findProcessOrderForRobot(serviceTypes);
		if (reqProcessOrder != null) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(reqProcessOrder.getId());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(reqProcessOrder, shipmentDetails);

			// Send order to robot
			switch (reqProcessOrder.getServiceType()) {
				case EportConstants.SERVICE_PICKUP_FULL:
					mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_FULL, robot.getUuId());
					break;
				case EportConstants.SERVICE_DROP_EMPTY:
					mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_EMPTY, robot.getUuId());
					break;
				case EportConstants.SERVICE_PICKUP_EMPTY:
					mqttService.publicMessageToDemandRobot(req, EServiceRobot.RECEIVE_CONT_EMPTY, robot.getUuId());
					break;
				case EportConstants.SERVICE_DROP_FULL:
					mqttService.publicMessageToDemandRobot(req, EServiceRobot.SEND_CONT_FULL, robot.getUuId());
					break;
				case EportConstants.SERVICE_SHIFTING:
					sendShiftingOrderToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_CHANGE_VESSEL:
					sendChangeVesselOrderToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_CREATE_BOOKING:
					mqttService.publicOrderToDemandRobot(reqProcessOrder, EServiceRobot.CREATE_BOOKING, robot.getUuId());
					break;
				case EportConstants.SERVICE_EXTEND_DATE:
					sendExtendDateOrderToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD:
					sendTerminalCustomHoldToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_CANCEL_DROP_FULL:
					sendCancelDropFullOrderToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_CANCEL_PICKUP_EMPTY:
					sendCancelPickupEmptyOrderToRobot(reqProcessOrder, robot.getUuId());
					break;
				case EportConstants.SERVICE_EXPORT_RECEIPT:
					sendExportReceiptToRobot(reqProcessOrder, robot.getUuId());
					break;
			}
			return "1";
		}
		return "0";
	}
	
	/**
	 * handle result from change vessel order
	 * 
	 * @param result
	 * @param receiptId
	 * @param uuId
	 */
	@Transactional
	private void updateChangeVesselOrder(String result, String receiptId, String uuId) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);
		String hresult = null;
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if ("success".equalsIgnoreCase(result)) {
			ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
			for (Long shipmentDetailId : processJsonData.getShipmentDetailIds()) {
				ShipmentDetail shipmentDetail = new ShipmentDetail();
				shipmentDetail.setId(shipmentDetailId);
				shipmentDetail.setVslNm(processOrder.getVessel());
				shipmentDetail.setVoyNo(processOrder.getVoyage());
				shipmentDetail.setVslName(processJsonData.getVslName());
				shipmentDetail.setVoyCarrier(processJsonData.getVoyCarrier());
				
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);
			hresult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
		} else {
			
			// INIT PROCESS ORDER TO UPDATE
			processOrder = new ProcessOrder();
			processOrder.setId(processOrderId);
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setRunnable(false);
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			hresult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
			ProcessOrder processOrderRes = processOrderService.selectProcessOrderById(processOrderId);
			ProcessJsonData processJsonData = new Gson().fromJson(processOrderRes.getProcessData(), ProcessJsonData.class);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderId.toString());
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					if (!catosApiService.checkContReserved(shipmentDetail)) {
						shipmentDetail.setVslNm(processOrder.getVessel());
						shipmentDetail.setVoyNo(processOrder.getVoyage());
						shipmentDetail.setVslName(processJsonData.getVslName());
						shipmentDetail.setVoyCarrier(processJsonData.getVoyCarrier());
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}
				}
			}
		}
		updateHistory(processOrderId, uuId, EportConstants.SERVICE_CHANGE_VESSEL, hresult);
	}
	
	/**
	 * Handle result for create booking
	 * 
	 * @param result
	 * @param receiptId
	 * @param uuId
	 */
	private void updateCreateBookingOrder(String result, String receiptId, String uuId, String msgError) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);
		String hresult = null;
		// Get process order by id
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		
		// Get the order receive empty mapping with create booking
		ProcessOrder receiveEmptyOrder = processOrderService.selectProcessOrderById(processOrder.getPostProcessId());
		
		// Result success update create booking status, send receive empty order to robot
		if ("success".equalsIgnoreCase(result)) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(receiveEmptyOrder.getId());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			ServiceSendFullRobotReq serviceReceiveEmptyReq = new ServiceSendFullRobotReq(receiveEmptyOrder, shipmentDetails);
			receiveEmptyOrder.setRunnable(true);
			processOrderService.updateProcessOrder(receiveEmptyOrder);
			try {
				mqttService.publishMessageToRobot(serviceReceiveEmptyReq, EServiceRobot.RECEIVE_CONT_EMPTY);
			} catch (MqttException e) {
				logger.error("Error send order to robot when create booking success: " + e);
			}
			
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);
			hresult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
		} else {
			
			String title = "ePort: Thông báo tạo/cập nhật booking bị lỗi bởi robot " + uuId + ".";
			String msg = "Robot làm lệnh bốc rỗng thất bại tại bước tạo/cập nhật booking cho mã lô " + processOrder.getShipmentId();
			
			receiveEmptyOrder.setResult(EportConstants.PROCESS_ORDER_RESULT_FAILED);
			receiveEmptyOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);
			receiveEmptyOrder.setMsg(msgError);
			receiveEmptyOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_FINISHED);
			receiveEmptyOrder.setResult(EportConstants.PROCESS_ORDER_RESULT_SUCCESS);
			processOrderService.updateProcessOrder(receiveEmptyOrder);
			
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setProcessOrderId(Long.parseLong(receiptId));
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
				shipmentDetail2.setProcessStatus("E");
				shipmentDetailService.updateShipmentDetail(shipmentDetail2);
			}
			
			// Send notification for om about receive empty order mapping with create booking order
			receiveEmptyOrder.setResult("F");
			processOrderService.updateProcessOrder(receiveEmptyOrder);
			
			// Send notification for om
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg, configService.getKey("domain.admin.name") + EportConstants.URL_OM_RECEIVE_E_SUPPORT, EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}

			// INIT PROCESS ORDER TO UPDATE
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setRunnable(false);
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			hresult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
		}
		updateHistory(processOrderId, uuId, EportConstants.SERVICE_CREATE_BOOKING, hresult);
	}
	
	private void updateExtensionDateOrder(String result, String receiptId, String uuId) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);
		String hresult = null;
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if ("success".equalsIgnoreCase(result)) {
			ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
			for (Long shipmentDetailId : processJsonData.getShipmentDetailIds()) {
				ShipmentDetail shipmentDetail = new ShipmentDetail();
				shipmentDetail.setId(shipmentDetailId);
				shipmentDetail.setExpiredDem(processOrder.getPickupDate());
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);
			hresult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
		} else {

			// INIT PROCESS ORDER TO UPDATE
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setRunnable(false);
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			hresult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
		}
		updateHistory(processOrderId, uuId, EportConstants.SERVICE_EXTEND_DATE, hresult);
	}
	
	/**
	 * Update terminal custom hold status
	 * 
	 * @param result
	 * @param receiptId
	 * @param uuId
	 */
	private void updateTerminalCustomHold(String result, String receiptId, String uuId) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);
		String hresult = null;
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if ("success".equalsIgnoreCase(result)) {
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);
			hresult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
		} else {

			// INIT PROCESS ORDER TO UPDATE
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setRunnable(false);
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			hresult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
		}
		updateHistory(processOrderId, uuId, EportConstants.SERVICE_EXTEND_DATE, hresult);
	}
	
	/**
	 * Update export receipt status
	 * 
	 * @param result
	 * @param receiptId
	 * @param uuId
	 */
	private void updateExportReceipt(String result, String receiptId, String uuId) {
		// INIT PROCESS HISTORY
		Long processOrderId = Long.parseLong(receiptId);
		String hresult = null;
		ProcessOrder processOrder = processOrderService.selectProcessOrderById(processOrderId);
		if ("success".equalsIgnoreCase(result)) {
			processOrder.setStatus(2); // FINISH		
			processOrder.setResult("S"); // RESULT SUCESS	
			processOrderService.updateProcessOrder(processOrder);
			hresult = EportConstants.PROCESS_HISTORY_RESULT_SUCCESS;
		} else {

			// INIT PROCESS ORDER TO UPDATE
			processOrder.setResult("F"); // RESULT FAILED
			processOrder.setStatus(0); // BACK TO WAITING STATUS FOR OM HANDLE
			processOrder.setRunnable(false);
			processOrderService.updateProcessOrder(processOrder);

			// SET RESULT FOR HISTORY FAILED
			hresult = EportConstants.PROCESS_HISTORY_RESULT_FAILED;
		}
		updateHistory(processOrderId, uuId, EportConstants.SERVICE_EXTEND_DATE, hresult);
	}
	
	/**
	 * Send process order change vessel that in queue waiting to execute
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendChangeVesselOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.CHANGE_VESSEL, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting change vessel order to robot: " + e);
		}
	}
	
	/**
	 * Send shifting order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendShiftingOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.SHIFTING_CONT, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting shifting order to robot: " + e);
		}
	}
	
	/**
	 * Send extend date order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendExtendDateOrderToRobot(ProcessOrder processOrder, String uuid) {
		ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
		String shipmentDetailIds = StringUtils.join(processJsonData.getShipmentDetailIds(), ",");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, null);
		ServiceSendFullRobotReq req = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
		try {
			mqttService.publicMessageToDemandRobot(req, EServiceRobot.EXTENSION_DATE, uuid);
		} catch (MqttException e) {
			logger.error("Error when send waiting extend date order to robot: " + e);
		}
	}
	
	/**
	 * Send terminal custom hold to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendTerminalCustomHoldToRobot(ProcessOrder processOrder, String uuid) {
		try {
			// parse data from process data
			ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
			Map<String, Object> params = new HashMap<>();
			params.put("containers", processJsonData.getContainers());
			processOrder.setParams(params);
			mqttService.publicOrderToDemandRobot(processOrder, EServiceRobot.TERMINAL_CUSTOM_HOLD, uuid);
		} catch (MqttException e) {
			logger.error("Error when send terminal hold to robot: " + e);
		}
	}
	
	/**
	 * Send cancel drop full order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendCancelDropFullOrderToRobot(ProcessOrder processOrder, String uuid) {
		try {
			mqttService.publicOrderToDemandRobot(processOrder, EServiceRobot.CANCEL_DROP_FULL, uuid);
		} catch (MqttException e) {
			logger.error("Error when send drop full to robot: " + e);
		}
	}
	
	/**
	 * Send cancel pickup empty order to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendCancelPickupEmptyOrderToRobot(ProcessOrder processOrder, String uuid) {
		try {
			mqttService.publicOrderToDemandRobot(processOrder, EServiceRobot.CANCEL_PICKUP_EMPTY, uuid);
		} catch (MqttException e) {
			logger.error("Error when send pickup empty to robot: " + e);
		}
	}
	
	/**
	 * Send export receipt to robot
	 * 
	 * @param processOrder
	 * @param uuid
	 */
	public void sendExportReceiptToRobot(ProcessOrder processOrder, String uuid) {
		try {
			mqttService.publicOrderToDemandRobot(processOrder, EServiceRobot.EXPORT_RECEIPT, uuid);
		} catch (MqttException e) {
			logger.error("Error when send export receipt to robot: " + e);
		}
	}
	
	private void sendProcessOrderHoldTerminal(ProcessOrder pickupFullOrder, List<ShipmentDetail> shipmentDetails) {
		// Get list container for shipmentDetails
		String containers = "";
		List<String> containerHolds = new ArrayList<>();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			containers += shipmentDetail.getContainerNo() + ",";
			containerHolds.add(shipmentDetail.getContainerNo());
		}
		containers = containers.substring(0, containers.length()-1);
		// Get list container terminal hold already
		ContainerHoldInfo containerHoldInfo = new ContainerHoldInfo();
		containerHoldInfo.setContainers(Convert.toStrArray(containers));
		containerHoldInfo.setHoldChk("Y");
		containerHoldInfo.setHoldType(EportConstants.HOLD_TYPE_TERMINAL);
		containerHoldInfo.setUserVoy(pickupFullOrder.getVessel() + pickupFullOrder.getVoyage());
		List<String> containerList = catosApiService.getContainerListHoldRelease(containerHoldInfo);
		
		// 
		if (CollectionUtils.isNotEmpty(containerList)) {
			for (String containerStr : containerList) {
				if (containerHolds.contains(containerStr)) {
					containerHolds.remove(containerStr);
				}
			}
		}
		
		// Send list container not check terminal hold to robot
		if (CollectionUtils.isNotEmpty(containerHolds)) {
			logger.debug("Create process order to send terminal hold.");
			ProcessOrder processOrder = new ProcessOrder();
			processOrder.setServiceType(EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD);
			processOrder.setShipmentId(pickupFullOrder.getShipmentId());
			processOrder.setLogisticGroupId(pickupFullOrder.getLogisticGroupId());
			processOrder.setContNumber(containerHolds.size());
			processOrder.setModee(EportConstants.MODE_TERMINAL_HOLD);
			processOrder.setBlNo(pickupFullOrder.getBlNo());
			Map<String, Object> processData = new HashMap<>();
			processData.put("containers", org.apache.commons.lang3.StringUtils.join(containerHolds, ","));
			processOrder.setProcessData(new Gson().toJson(processData));
			processOrder.setHoldFlg(true);
			processOrder.setServiceType(EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD);
			processOrder.setRemark("Chua nhan DO goc");
			processOrder.setRunnable(true);
			processOrderService.insertProcessOrder(processOrder);
			
			// parse data from process data
			ProcessJsonData processJsonData = new Gson().fromJson(processOrder.getProcessData(), ProcessJsonData.class);
			Map<String, Object> params = new HashMap<>();
			params.put("containers", processJsonData.getContainers());
			processOrder.setParams(params);
			
			logger.debug("Find robot terminal hold available.");
			SysRobot sysRobot = new SysRobot();
			sysRobot.setIsChangeTerminalCustomHold(true);
			sysRobot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
			sysRobot.setDisabled(false);
			SysRobot robot = robotService.findFirstRobot(sysRobot);
			try {
				mqttService.publicOrderToDemandRobot(processOrder, EServiceRobot.TERMINAL_CUSTOM_HOLD, robot.getUuId());
			} catch (MqttException e) {
				logger.error("Error when send pickup empty to robot: " + e);
			}
		}
		
	}
}
