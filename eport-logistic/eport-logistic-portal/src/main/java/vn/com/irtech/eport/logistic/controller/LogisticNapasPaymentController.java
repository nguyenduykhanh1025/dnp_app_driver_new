package vn.com.irtech.eport.logistic.controller;

import java.util.Base64;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

@Controller
@RequestMapping("/")
public class LogisticNapasPaymentController extends LogisticBaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(LogisticNapasPaymentController.class);
    
    private final String PREFIX = "logistic";
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private IProcessOrderService processOrderService;
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private ISysRobotService robotService;

	@Autowired
	private IPaymentHistoryService paymentHistoryService;

	@Log(title = "Thanh Toán Napas", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@RequestMapping(value="/payment/result", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	public String getPaymentResult(@RequestParam("napasResult") String result, ModelMap mmap) {
		JSONObject json = JSONObject.parseObject(result);
		String dataBase64 = json.getString("data");
		
		boolean isError = true;
		
		//checksum
		String  checksumString = json.getString("checksum");
		if (checksumString.equalsIgnoreCase(DigestUtils.sha256Hex(dataBase64+configService.getKey("napas.client.secret")))) {
			
			//decode base
			JSONObject decodeData = JSONObject.parseObject(new String(Base64.getDecoder().decode(dataBase64)));
			
			// result (Success or Failed)
			String resultStatus = decodeData.getJSONObject("paymentResult").getString("result");
			
			if ("SUCCESS".equalsIgnoreCase(resultStatus)) {
				// order id
				String orderId = decodeData.getJSONObject("paymentResult").getJSONObject("order").getString("id");
				PaymentHistory paymentHistoryParam = new PaymentHistory();
				paymentHistoryParam.setOrderId(orderId);
				List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
				if (!paymentHistories.isEmpty()) {
					PaymentHistory paymentHistory = paymentHistories.get(0);

					// Update payment history
					// paymentHistory.setUpdateBy(getUser().getFullName());	 // NULL?
					paymentHistory.setStatus(EportConstants.PAYMENT_HISTORY_SUCCESS);
					if (paymentHistoryService.updatePaymentHistory(paymentHistory) == 1) {
						// Update shipment detail
						List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(paymentHistory.getProcessOrderIds());
						for (ShipmentDetail shipmentDetail : shipmentDetails) {
							shipmentDetail.setPaymentStatus("Y");
							shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
							if (shipmentDetail.getCustomStatus() != null && "N".equals(shipmentDetail.getCustomStatus()) && 
							shipmentDetail.getDischargePort() != null && shipmentDetail.getDischargePort().length() > 2 && 
							"VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
								shipmentDetail.setCustomStatus("R");
								shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
							}
							shipmentDetailService.updateShipmentDetail(shipmentDetail);
						}

						// Update bill
						processBillService.updateBillListByProcessOrderIds(paymentHistory.getProcessOrderIds());
						
						List<ProcessOrder> processOrders = processOrderService.selectProcessOrderListByIds(paymentHistory.getProcessOrderIds());
						sendExportReceiptToRobot(processOrders);
					}
					isError = false;
				} else {
					logger.error("[NAPAS] Receive SUCCESS but OrderId Not Found: " + decodeData);
				}
			} else {
				logger.debug("[NAPAS] PAYMENT ERROR:" + decodeData);
			}
		}
		if (isError) {
			logger.debug("[NAPAS] Receive ERROR: " + json);
			mmap.put("result", "ERROR");
		} else {
			mmap.put("result", "SUCCESS");
		}
		return PREFIX + "/napas/resultForm";
	}

	@Log(title = "Thanh Toán Napas", businessType = BusinessType.INSERT, operatorType = OperatorType.MOBILE)
	@RequestMapping(value="/payment/mobile/result", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	@Transactional
	public String getPaymentMobileResult(@RequestParam("napasResult") String result, ModelMap mmap) {
		JSONObject json = JSONObject.parseObject(result);
		String dataBase64 = json.getString("data");

		boolean isError = true;
		
		//checksum
		String  checksumString = json.getString("checksum");
		if (checksumString.equalsIgnoreCase(DigestUtils.sha256Hex(dataBase64+configService.getKey("napas.client.secret")))) {
			
			//decode base
			JSONObject decodeData = JSONObject.parseObject(new String(Base64.getDecoder().decode(dataBase64)));
			
			// result (Success or Failed)
			String resultStatus = decodeData.getJSONObject("paymentResult").getString("result");
			
			if ("SUCCESS".equalsIgnoreCase(resultStatus)) {
				// order id
				String orderId = decodeData.getJSONObject("paymentResult").getJSONObject("order").getString("id");
				PaymentHistory paymentHistoryParam = new PaymentHistory();
				paymentHistoryParam.setOrderId(orderId);
				List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
				if (!paymentHistories.isEmpty()) {
					PaymentHistory paymentHistory = paymentHistories.get(0);

					// Update payment history
//					paymentHistory.setUpdateBy(getUser().getFullName());
					paymentHistory.setStatus("1");
					paymentHistoryService.updatePaymentHistory(paymentHistory);

					// Update shipment detail
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(paymentHistory.getProcessOrderIds());
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDetail.setPaymentStatus("Y");
						shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						if (shipmentDetail.getCustomStatus() != null && "N".equals(shipmentDetail.getCustomStatus()) && 
						shipmentDetail.getDischargePort() != null && shipmentDetail.getDischargePort().length() > 2 && 
						"VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
							shipmentDetail.setCustomStatus("R");
							shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						}
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}

					// Update bill
					processBillService.updateBillListByProcessOrderIds(paymentHistory.getProcessOrderIds());

					isError = false;
				}
			}
		}
		if (isError) {
			mmap.put("result", "ERROR");
		} else {
			mmap.put("result", "SUCCESS");
		}
		return PREFIX + "/napas/resultMobiletForm";
	}

	@RequestMapping(value="/payment/shifting/result", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
	@Transactional
	public String getPaymentShiftingResult(@RequestParam("napasResult") String result, ModelMap mmap) {
		JSONObject json = JSONObject.parseObject(result);
		String dataBase64 = json.getString("data");

		boolean isError = true;
		
		//checksum
		String  checksumString = json.getString("checksum");
		if (checksumString.equalsIgnoreCase(DigestUtils.sha256Hex(dataBase64+configService.getKey("napas.client.secret")))) {
			
			//decode base
			JSONObject decodeData = JSONObject.parseObject(new String(Base64.getDecoder().decode(dataBase64)));
			
			// result (Success or Failed)
			String resultStatus = decodeData.getJSONObject("paymentResult").getString("result");
			
			if ("SUCCESS".equalsIgnoreCase(resultStatus)) {
				// order id
				String orderId = decodeData.getJSONObject("paymentResult").getJSONObject("order").getString("id");
				PaymentHistory paymentHistoryParam = new PaymentHistory();
				paymentHistoryParam.setOrderId(orderId);
				List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryList(paymentHistoryParam);
				if (CollectionUtils.isNotEmpty(paymentHistories)) {
					PaymentHistory paymentHistory = paymentHistories.get(0);

					// Update payment history
//					paymentHistory.setUpdateBy(getUser().getFullName());
					paymentHistory.setStatus("1");
					paymentHistoryService.updatePaymentHistory(paymentHistory);

					// Update shipment detail
					ShipmentDetail shipmentDetailParam = new ShipmentDetail();
					shipmentDetailParam.setShipmentId(paymentHistory.getShipmentId());
					shipmentDetailParam.setPreorderPickup("Y");
					List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDetail.setPrePickupPaymentStatus("Y");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
					}

					// Update bill
					processBillService.updateBillListByProcessOrderIds(paymentHistory.getProcessOrderIds());

					isError = false;
				}
			}
		}
		if (isError) {
			mmap.put("result", "ERROR");
		} else {
			mmap.put("result", "SUCCESS");
		}
		return PREFIX + "/napas/resultForm";
	}
	
	private void sendExportReceiptToRobot(List<ProcessOrder> processOrders) {
		if (CollectionUtils.isNotEmpty(processOrders)) {
			for(ProcessOrder processOrder : processOrders) {
				ProcessOrder exportReceiptOrder = new ProcessOrder();
				exportReceiptOrder.setInvoiceNo(processOrder.getInvoiceNo());
				exportReceiptOrder.setOrderNo(processOrder.getOrderNo());
				exportReceiptOrder.setPickupDate(processOrder.getCreateTime());
				exportReceiptOrder.setShipmentId(processOrder.getShipmentId());
				exportReceiptOrder.setServiceType(EportConstants.SERVICE_EXPORT_RECEIPT);
				exportReceiptOrder.setLogisticGroupId(processOrder.getLogisticGroupId());
				exportReceiptOrder.setRunnable(true);
				processOrderService.insertProcessOrder(exportReceiptOrder);
				logger.debug("Find robot export receipt available.");
				SysRobot sysRobot = new SysRobot();
				sysRobot.setIsExportReceipt(true);
				sysRobot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
				sysRobot.setDisabled(false);
				SysRobot robot = robotService.findFirstRobot(sysRobot);
				
				if (robot == null) {
					return;
				}
				
				try {
					mqttService.publicOrderToDemandRobot(exportReceiptOrder, EServiceRobot.EXPORT_RECEIPT, robot.getUuId());
				} catch (MqttException e) {
					logger.error("Error when send export receipt request to robot: " + e);
				}
			}
		}
	}
}
