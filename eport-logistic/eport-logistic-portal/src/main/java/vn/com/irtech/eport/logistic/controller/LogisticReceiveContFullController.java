package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.annotation.RepeatSubmit;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.constant.SystemConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.framework.web.service.WebSocketService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.form.ContainerServiceForm;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@Controller
@RequestMapping("/logistic/receive-cont-full")
public class LogisticReceiveContFullController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/receiveContFull";

	private static final Logger logger = LoggerFactory.getLogger(LogisticReceiveContFullController.class);

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired 
	private IOtpCodeService otpCodeService;

	@Autowired
	private IProcessBillService processBillService;
	
	@Autowired
	private CustomQueueService customQueueService;

	@Autowired
	private ISysConfigService configService;
	
	@Autowired
	private MqttService mqttService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IEdoService edoService;

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@Autowired
	private WebSocketService webSocketService;
	
	@Autowired
	private IEdoHouseBillService edoHouseBillService;
	
	@Autowired
	private IShipmentCommentService shipmentCommentService;
	
	@Autowired
	private DictService dictService;
	
	@GetMapping()
	public String receiveContFull(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		List<String> emptyDepots = new ArrayList<>();
		String danangPortName = configService.selectConfigByKey("danang.port.name");
		if (danangPortName != null) {
			emptyDepots.add(danangPortName);
		}
		emptyDepots.add("Cảng Khác");
		mmap.put("emptyDepots", emptyDepots);
		return PREFIX + "/index";
	}

	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		mmap.put("taxCode", getGroup().getMst());
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
			mmap.put("taxCode", getGroup().getMst());
		}
		return PREFIX + "/edit";
	}

	@GetMapping("/custom-status/{shipmentDetailIds}")
	public String checkCustomStatus(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
 		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
			mmap.put("contList", shipmentDetails);
		}
		return PREFIX + "/checkCustomStatus";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			if (("Cảng Tiên Sa").equals(shipmentDetails.get(0).getEmptyDepot())) {
				mmap.put("sendContEmpty", true);
			}
			mmap.put("shipmentDetails", shipmentDetails);
		} else {
			mmap.put("sendContEmpty", false);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{isSendContEmpty}/{shipmentId}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("shipmentId") Long shipmentId,
			@PathVariable("creditFlag") boolean creditFlag, @PathVariable("isSendContEmpty") boolean isSendContEmpty, 
			@PathVariable("taxCode") String taxCode, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("shipmentId", "-");
		mmap.put("numberPhone", getUser().getMobile());
		mmap.put("creditFlag", creditFlag);
		mmap.put("isSendContEmpty", isSendContEmpty);
		mmap.put("taxCode", taxCode);
		mmap.put("shipmentId", shipmentId);
		return PREFIX + "/verifyOtp";
	}

	@GetMapping("/payment/{processOrderIds}")
	public String paymentForm(@PathVariable("processOrderIds") String processOrderIds, ModelMap mmap) {
		String shipmentDetailIds = "";
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByProcessIds(processOrderIds);
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds += shipmentDetail.getId() + ",";
		}
		if (!"".equalsIgnoreCase(shipmentDetailIds)) {
			shipmentDetailIds.substring(0, shipmentDetailIds.length()-1);
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("processBills", processBillService.selectProcessBillListByProcessOrderIds(processOrderIds));
		return PREFIX + "/paymentForm";
	}

	@Log(title = "Thêm Lô Bốc Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
	@ResponseBody
	public AjaxResult addShipment(Shipment shipment) {
		
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			if (edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber()) == 0) {
				return error("Thêm lô thất bại");
			} else {
				if (edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber()) == 0) {
					return error("Thêm lô thất bại");
				}
			}
		} 
		
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateTime(new Date());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	@Log(title = "Chỉnh Sửa Lô", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment/{shipmentId}")
	@ResponseBody
	public AjaxResult editShipment(Shipment shipment, @PathVariable Long shipmentId) {
		LogisticAccount user = getUser();
		Shipment referenceShipment = shipmentService.selectShipmentById(shipmentId);
		//check if current user own shipment
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			// Chi update cac item cho phep
			referenceShipment.setUpdateBy(user.getFullName());
			referenceShipment.setRemark(shipment.getRemark());
			referenceShipment.setId(shipment.getId());

			// Can change bill of lading when status = initialize => another item change accordingly
			if (EportConstants.SHIPMENT_STATUS_INIT.equals(referenceShipment.getStatus())) {
				if (StringUtils.isNotEmpty(shipment.getBlNo())) {
					referenceShipment.setBlNo(shipment.getBlNo().toUpperCase());
					referenceShipment.setContainerAmount(shipment.getContainerAmount());
				}
				
				if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
					referenceShipment.setHouseBill(shipment.getHouseBill().toUpperCase());
				}
				referenceShipment.setOpeCode(shipment.getOpeCode());
				referenceShipment.setEdoFlg(shipment.getEdoFlg());
			}
			
			if (shipmentService.updateShipment(referenceShipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		AjaxResult ajaxResult = AjaxResult.success();
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailList(shipmentDetail);
			// auto load containers detail for eDO for first time
			if (shipment.getEdoFlg().equals("1") && shipmentDetails.size() == 0) {
				if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
					shipmentDetails = shipmentDetailService.getShipmentDetailFromHouseBill(shipment.getHouseBill());
				} else {
					//get infor from edi
					shipmentDetails = shipmentDetailService.getShipmentDetailsFromEDIByBlNo(shipment.getBlNo());
				}
				//get infor from catos
				List<ShipmentDetail> shipmentDetailsCatos = catosApiService.selectShipmentDetailsByBLNo(shipment.getBlNo());
				//Get opecode, sealNo, wgt, pol,pod
				if(shipmentDetailsCatos != null) {
					for(ShipmentDetail detail : shipmentDetails) {
						// FIXME vi sao set 2 cai nay cho nhau lam gi
						detail.setVoyNo(detail.getVoyCarrier());
						for(ShipmentDetail catosDetail : shipmentDetailsCatos) {
							if(detail.getContainerNo().equalsIgnoreCase(catosDetail.getContainerNo())) {
								// Overwrite information from CATOS
								detail.setSztp(catosDetail.getSztp());
								detail.setSztpDefine(catosDetail.getSztpDefine());
								// Block-Bay-Row-Tier
								detail.setLocation(catosDetail.getLocation());
								detail.setContainerRemark(catosDetail.getContainerRemark());
								detail.setOpeCode(catosDetail.getOpeCode());
								detail.setVslNm(catosDetail.getVslNm());					// overwrite VSL_CD:VSL_NM from CATOS
								detail.setVoyNo(catosDetail.getVoyNo());
								detail.setSealNo(catosDetail.getSealNo());
								detail.setWgt(catosDetail.getWgt());
								detail.setLoadingPort(catosDetail.getLoadingPort());		// overwrite from CATOS
								detail.setDischargePort(catosDetail.getDischargePort());	// overwrite from CATOS
								detail.setYear(catosDetail.getYear());
							}
						}
					}
				}
			}
			ajaxResult.put("shipmentDetails", shipmentDetails);
		}
		return ajaxResult;
	}

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null && shipmentDetails.size() > 0){
			String dnPortName = configService.selectConfigByKey("danang.port.name");
			LogisticAccount user = getUser();
			ShipmentDetail shipmentDt = shipmentDetails.get(0);
			Shipment shipmentSendCont = new Shipment();
			boolean isCreated = true;
			Long shipmentId = shipmentDetails.get(0).getShipmentId();
			if (shipmentId == null) {
				return error("Không tìm thấy lô cần thêm chi tiết.");
			}
			Shipment shipment = shipmentService.selectShipmentById(shipmentId);
			if (!verifyPermission(shipment.getLogisticGroupId())) {
				return error("Mã lô quý khách muốn lưu thông tin chi  tiết <br>không hợp lệ.");
			}
			boolean updateShipment = true;
			boolean isSendEmpty = shipmentDt.getVgmChk();
			if (dnPortName.equals(shipmentDt.getEmptyDepot()) && isSendEmpty) {
				shipmentSendCont.setBlNo(shipmentDt.getBlNo());
				shipmentSendCont.setServiceType(Constants.SEND_CONT_EMPTY);
				List<Shipment> shipments = shipmentService.selectShipmentList(shipmentSendCont);
				if (shipments == null || shipments.size() == 0) {
					shipmentSendCont.setContainerAmount(Long.valueOf(shipmentDt.getTier()));
					shipmentSendCont.setLogisticAccountId(user.getId());
					shipmentSendCont.setOpeCode(shipment.getOpeCode());
					shipmentSendCont.setLogisticGroupId(user.getGroupId());
					shipmentSendCont.setCreateTime(new Date());
					shipmentSendCont.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
					shipmentService.insertShipment(shipmentSendCont);
					isCreated = false;
				}
			}
			String taxCode = catosApiService.getTaxCodeBySnmGroupName(shipmentDt.getConsignee());
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setProcessStatus(null);
				shipmentDetail.setCustomStatus(null);
				shipmentDetail.setVgmChk(null);
				if (shipmentDetail.getId() == null) {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setFe("F");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setDoStatus("N");
					shipmentDetail.setPreorderPickup("N");
					shipmentDetail.setFinishStatus("N");
					// set consignee Taxcode
					shipmentDetail.setTaxCode(taxCode);
					shipmentDetail.setConsigneeByTaxCode(shipmentDetail.getConsignee());
					if ("VN".equalsIgnoreCase(shipmentDetail.getLoadingPort().substring(0, 2))) {
						shipmentDetail.setCustomStatus("R");
						shipmentDetail.setStatus(2);
					} else {
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setStatus(1);
					}
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
					if (dnPortName.equals(shipmentDt.getEmptyDepot()) && !isCreated && isSendEmpty) {
						shipmentDetail.setShipmentId(shipmentSendCont.getId());
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setFe("E");
						shipmentDetail.setCargoType("MT");
						shipmentDetail.setDischargePort("VNDAD");
						shipmentDetail.setVslNm("EMTY");
						shipmentDetail.setVoyNo("0000");
						shipmentDetail.setOpeCode(shipment.getOpeCode());
						shipmentDetail.setEmptyDepotLocation(getEmptyDepotLocation(shipmentDetail.getSztp(), shipmentDetail.getOpeCode()));
						shipmentDetail.setStatus(1);
						shipmentDetailService.insertShipmentDetail(shipmentDetail);
					}
					
				} else {
					ShipmentDetail shipmentDetailReference = shipmentDetailService.selectShipmentDetailById(shipmentDetail.getId());
					if ("N".equals(shipmentDetailReference.getUserVerifyStatus())) {
						updateShipment = false;
						shipmentDetail.setUpdateBy(user.getFullName());
						shipmentDetail.setTaxCode(taxCode);
						shipmentDetail.setConsigneeByTaxCode(shipmentDetail.getConsignee());
						shipmentDetail.setUpdateTime(new Date());
						if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
							return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
						}
					}
				}
			}
			if (updateShipment && shipment != null && shipment.getId() != null) {
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_SAVE);
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@Log(title = "Xóa Khai Báo Cont", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
	@DeleteMapping("/shipment/{shipmentId}/shipment-detail/{shipmentDetailIds}")
	@Transactional
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId, @PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds);
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setStatus("1");
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@PostMapping("/shipment-detail/bl-no/cont/info")
	@ResponseBody
	public ShipmentDetail getContInfo(@RequestBody ShipmentDetail shipmentDetail) {
		if (StringUtils.isNotEmpty(shipmentDetail.getBlNo()) && StringUtils.isNotEmpty(shipmentDetail.getContainerNo())) {
			ShipmentDetail shipmentDetailResult = catosApiService.selectShipmentDetailByContNo(shipmentDetail);
			return shipmentDetailResult ;
		} else {
			return null;
		}
	}

	@Log(title = "Check Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList, @RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
			boolean customsNoMappingFlg = "1".equals(configService.selectConfigByKey(SystemConstants.ACCIS_CUSTOM_MAPPING_FLG_KEY));
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declareNoList to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetail.setCustomScanTime(new Date());
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// Neu bat buoc check to khai thi phai goi lai acciss
					if (!customsNoMappingFlg && catosApiService.checkCustomStatus(shipmentDetail.getContainerNo(), shipmentDetail.getVoyNo())) {
						if (shipmentDetail.getStatus() == 1) {
							shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
						}
						shipmentDetail.setCustomStatus("R");
						shipmentDetailService.updateShipmentDetail(shipmentDetail);
						AjaxResult ajaxResult = AjaxResult.success();
						ajaxResult.put("shipmentDetail", shipmentDetail);
						webSocketService.sendMessage("/" + shipmentDetail.getContainerNo() + "/response", ajaxResult);
					} else {
						customQueueService.offerShipmentDetail(shipmentDetail);
					}
				}
				return success();
			}
		}
		return error();
	}

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult verifyOtp(String shipmentDetailIds, @PathVariable("otp") String otp, String taxCode, boolean creditFlag, boolean isSendContEmpty) {
		try {
			Long.parseLong(otp);
		} catch (Exception e) {
			return error("Mã OTP nhập vào không hợp lệ!");
		}
		OtpCode otpCode = new OtpCode();
		otpCode.setTransactionId(shipmentDetailIds);
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -5);
		otpCode.setCreateTime(cal.getTime());
		otpCode.setOtpCode(otp);
		if (otpCodeService.verifyOtpCodeAvailable(otpCode) != 1) {
			return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
		}
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!"3".equals(shipment.getStatus())) {
				shipment.setStatus("3");
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContFull(shipmentDetails, shipment, taxCode, creditFlag);
			if (serviceRobotReqs != null) {
				List<Long> processIds = new ArrayList<>();
				boolean robotBusy = false;

				// MAKE ORDER SEND CONT EMPTY
				// if (isSendContEmpty) {
				// 	shipment.setId(null);
				// 	List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
				// 	if (shipments != null && shipments.size() > 0) {
				// 		String conts = "";
				// 		for (ShipmentDetail shipmentDt: shipmentDetails) {
				// 			conts += shipmentDt.getContainerNo() + ",";
				// 		}
				// 		conts = conts.substring(0, conts.length()-1);
				// 		List<ShipmentDetail> shipmentDetails2 = shipmentDetailService.selectSendEmptyShipmentDetailByListCont(conts, shipments.get(0).getId());
				// 		ProcessOrder processOrder = shipmentDetailService.makeOrderSendCont(shipmentDetails2, shipments.get(0), creditFlag);
				// 		ServiceRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails2);
				// 		try {
				// 			mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_EMPTY);
				// 		} catch (Exception e) {
				// 			e.printStackTrace();
				// 		}
				// 	}
				// }

				// MAKE ORDER RECEIVE CONT FULL
				try {
					for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
						processIds.add(serviceRobotReq.processOrder.getId());
						if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.RECEIVE_CONT_FULL)) {
							robotBusy = true;
						}
					}
					if (robotBusy) {
						ajaxResult = AjaxResult.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
						ajaxResult.put("processIds", processIds);
						ajaxResult.put("orderNumber", serviceRobotReqs.size());
						return ajaxResult;
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}
				ajaxResult = AjaxResult.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processIds", processIds);
				ajaxResult.put("orderNumber", serviceRobotReqs.size());
				return ajaxResult;
			}
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	@GetMapping("/consignees")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		List<String> listConsignee = shipmentDetailService.getConsigneeList();
		ajaxResult.put("consigneeList", listConsignee);
		return ajaxResult;
	}
	
	@PostMapping("/shipment/bl-no")
	@ResponseBody
	public AjaxResult checkShipmentInforByBlNo(@RequestBody ContainerServiceForm inputForm) {
		String blNo = inputForm.getBlNo();
		if (StringUtils.isNotEmpty(blNo)) {
			blNo = blNo.toUpperCase();
		}
		AjaxResult ajaxResult = new AjaxResult();
		Shipment shipment = new Shipment();
		//check bill unique
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}
		
		//check opeCode
		String opeCode = edoService.getOpeCodeByBlNo(blNo);
		// Long containerAmount = edoService.getCountContainerAmountByBlNo(blNo);
		
		// Check edo with master bill
		if(opeCode != null) {
			// Check if carrier group support edo depend on do type defining in carrier group admin
			if (EportConstants.DO_TYPE_CARRIER_EDO.equalsIgnoreCase(carrierGroupService.getDoTypeByOpeCode(opeCode))) {
				shipment.setEdoFlg("1");
				shipment.setOpeCode(opeCode);
				// shipment.setContainerAmount(containerAmount);
				ajaxResult = success();
				ajaxResult.put("shipment", shipment);
				return ajaxResult;
			}
		} else {
			// Check edo with house bill
			EdoHouseBill edoHouseBill = edoHouseBillService.getEdoHouseBillByBlNo(blNo);
			if (edoHouseBill != null) {
				// Check if carrier group support edo depend on do type defining in carrier group admin
				if (EportConstants.DO_TYPE_CARRIER_EDO.equalsIgnoreCase(carrierGroupService.getDoTypeByOpeCode(edoHouseBill.getCarrierCode()))) {
					shipment.setEdoFlg("1");
					shipment.setOpeCode(edoHouseBill.getCarrierCode());
					shipment.setHouseBill(blNo);
					shipment.setBlNo(edoService.getBlNoByHouseBillId(edoHouseBill.getId()));
					// shipment.setContainerAmount(containerAmount);
					ajaxResult = success();
					ajaxResult.put("shipment", shipment);
					return ajaxResult;
				}
			}
		}
		
		// check do
		Shipment shipCatos = null;
		try {
			shipCatos = catosApiService.getOpeCodeCatosByBlNo(blNo);
		} catch (Exception e) {
			logger.error("Error when get ope code catos by bl no: " + e);
		}
		if (shipCatos != null) {
			String edoFlg = carrierGroupService.getDoTypeByOpeCode(shipCatos.getOpeCode());
			if(edoFlg == null){
				return error("Mã hãng tàu:"+ shipCatos.getOpeCode() +" không có trong hệ thống. Vui lòng liên hệ Cảng!");
			}
//			if(edoFlg.equals("1")){
//				return error("Bill này là eDO nhưng không có dữ liệu trong eport. Vui lòng liên hệ Cảng!");
//			}
			shipment.setEdoFlg(edoFlg);
			ajaxResult = success();
			shipment.setOpeCode(shipCatos.getOpeCode());
			shipment.setContainerAmount(shipCatos.getContainerAmount());
			ajaxResult.put("shipment", shipment);
			return ajaxResult;
		}
		
		ajaxResult = error("Số bill không tồn tại!");
		return ajaxResult;
	}

	/**
	 * Check order number match with bl no for edo (master bill, house bill)
	 * 
	 * @param shipment
	 * @return AjaxResult
	 */
	@PostMapping("/orderNumber/check")
	@ResponseBody
	public AjaxResult checkOrderNumber(@RequestBody Shipment shipment) {
		int containerAmount = 0;
		// Check for house bill case
		if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
			containerAmount = edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(), shipment.getOrderNumber());
		} else {
			// Check for master bill case
			containerAmount = edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(), shipment.getOrderNumber());
		}
		if (containerAmount != 0) {
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("containerAmount", containerAmount);
			return ajaxResult;
		}
		return error("Mã nhận container không chính xác.");
	}
	
	@GetMapping("/shipment/{shipmentId}/delegate/permission")
	@ResponseBody
	public AjaxResult checkDelegatePermission(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		// check DO or eDO (0 is DO no need to validate 
		// Get tax code of consignee own this shipment
		String taxCode = shipmentDetailService.selectConsigneeTaxCodeByShipmentId(shipmentId);
		if (taxCode == null) {
			return error();
		}
		
		// Check if logistic can make order for this shipment
		if (logisticGroupService.checkDelegatePermission(taxCode, getGroup().getMst(), EportConstants.DELEGATE_PERMISSION_PROCESS) > 0) {
			return success();
		}
		return error();
	}
	
	@GetMapping("/shipment/{shipmentId}/custom/notification")
	@ResponseBody
	public AjaxResult sendNotificationCustomError(@PathVariable("shipmentId") String shipmentId) {
		try {
			mqttService.sendNotification(NotificationCode.NOTIFICATION_OM_CUSTOM, "Lỗi hải quan lô " + shipmentId, configService.selectConfigByKey("domain.admin.name") + "/om/support/custom/" + shipmentId);
		} catch (MqttException e) {
			logger.error("Gửi thông báo lỗi hải quan cho om: " + e);
		}
		return success();
	}
	
	/**
	 * Get empty depot location base on sztp and opr in dictionary
	 * 
	 * @param sztp
	 * @param opr
	 * @return empty depot location string (DANALOG 01, DANALOG 02, Tiên Sa)
	 */
	private String getEmptyDepotLocation(String sztp, String opr) {
		String emptyDepotRule = dictService.getLabel("empty_depot_location", opr);
		if (StringUtils.isNotEmpty((emptyDepotRule))) {
			String[] emptyDepotArr = emptyDepotRule.split(",");
			int length = emptyDepotArr.length;
			for (int i = 0; i < length; i++) {
				if (sztp.equalsIgnoreCase(emptyDepotArr[i])) {
					String emptyDepotLocation = emptyDepotArr[length - 1];
					return emptyDepotLocation;
				}
			}
		}
		String danangDepotName = configService.selectConfigByKey("danang.depot.name");
		return danangDepotName;
	}
	
	@PostMapping("/shipment/comment")
	@ResponseBody
	public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
		LogisticAccount user = getUser();
		shipmentComment.setCreateBy(user.getUserName());
		shipmentComment.setLogisticGroupId(user.getGroupId());
		shipmentComment.setUserId(getUserId());
		shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
		shipmentComment.setUserAlias(getGroup().getGroupName());
		shipmentComment.setUserName(user.getUserName());
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_FULL);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setSeenFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);
		
		// Send notification to om
		try {
			mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, shipmentComment.getTopic(), shipmentComment.getContent(), "", EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
		} catch (MqttException e) {
			logger.error("Fail to send message om notification app: " + e);
		}
		
		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}
}

