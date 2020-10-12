package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
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

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.annotation.RepeatSubmit;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.BerthPlanInfo;
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
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Controller
@RequestMapping("/logistic/send-cont-empty")
public class LogisticSendContEmptyController extends LogisticBaseController {

	private final String PREFIX = "logistic/sendContEmpty";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private IProcessBillService processBillService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private DictService dictService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private ServerConfig serverConfig;

	@GetMapping()
	public String sendContEmpty(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());
		return PREFIX + "/index";
	}

	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");

		mmap.put("taxCode", getGroup().getMst());
		mmap.put("oprCodeList", oprCodeList);
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
			mmap.put("taxCode", getGroup().getMst());
		}
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");

		mmap.put("oprCodeList", oprCodeList);
		return PREFIX + "/edit";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{shipmentId}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			@PathVariable("shipmentId") Long shipmentId, @PathVariable("creditFlag") boolean creditFlag,
			@PathVariable("taxCode") String taxCode, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getUser().getMobile());
		mmap.put("creditFlag", creditFlag);
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
			shipmentDetailIds.substring(0, shipmentDetailIds.length() - 1);
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("processBills", processBillService.selectProcessBillListByProcessOrderIds(processOrderIds));
		return PREFIX + "/paymentForm";
	}

	@GetMapping("/payment/napas")
	public String napasPaymentForm() {
		return PREFIX + "/napasPaymentForm";
	}

	@GetMapping("/req/cancel/confirmation")
	public String getCancelConfirmationForm() {
		return PREFIX + "/confirmRequestCancel";
	}

	@Log(title = "Tạo Lô Hạ Rỗng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
	@ResponseBody
	public AjaxResult addShipment(Shipment shipment) {
		if (StringUtils.isNotEmpty(shipment.getBlNo())) {
			shipment.setBlNo(shipment.getBlNo());
		}
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(Constants.SEND_CONT_EMPTY);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	@PostMapping("/unique/bl-no")
	@ResponseBody
	public AjaxResult checkBlNoUnique(@RequestBody ContainerServiceForm inputForm) {
		String blNo = inputForm.getBlNo();
		if (StringUtils.isAllBlank(blNo)) {
			return error("Hãy nhập B/L No");
		}
		Shipment shipment = new Shipment();
		shipment.setServiceType(Constants.SEND_CONT_EMPTY);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		shipment.setLogisticGroupId(getUser().getGroupId());
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error("B/L No này đã tồn tại trong hệ thống.");
	}

	@Log(title = "Sữa Lô Hạ Rỗng", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment/{shipmentId}")
	@ResponseBody
	public AjaxResult editShipment(Shipment shipment, @PathVariable Long shipmentId) {
		Shipment referenceShipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			// Check container amount update need to greater or equal curren amount
			// Or at least greater or equal the number of container has been declared
			if (shipment.getContainerAmount() < referenceShipment.getContainerAmount()) {
				ShipmentDetail shipmentSearch = new ShipmentDetail();
				shipmentSearch.setShipmentId(shipment.getId());
				long contNumber = shipmentDetailService.countShipmentDetailList(shipmentSearch);
				if (contNumber > shipment.getContainerAmount()) {
					return error("Không thể chỉnh sửa số lượng container nhỏ hơn danh sách khai báo.");
				}
			}
			LogisticAccount user = getUser();
			referenceShipment.setRemark(shipment.getRemark());
			referenceShipment.setContainerAmount(shipment.getContainerAmount());
			referenceShipment.setUpdateBy(user.getUserName());
			referenceShipment.setId(shipmentId);

			if (EportConstants.SHIPMENT_STATUS_INIT.equals(referenceShipment.getStatus())) {
				referenceShipment.setSendContEmptyType(shipment.getSendContEmptyType());
				referenceShipment.setOpeCode(shipment.getOpeCode());
				referenceShipment.setBlNo(shipment.getBlNo());
			} else if (EportConstants.SHIPMENT_STATUS_SAVE.equals(referenceShipment.getStatus())) {
				if (!referenceShipment.getOpeCode().equals(shipment.getOpeCode())
						|| !referenceShipment.getBlNo().equalsIgnoreCase(shipment.getBlNo())) {
					// Get shipment detail to update ope code
					ShipmentDetail shipmentDetailParam = new ShipmentDetail();
					shipmentDetailParam.setShipmentId(shipmentId);
					shipmentDetailParam.setLogisticGroupId(user.getGroupId());
					List<ShipmentDetail> shipmentDetails = shipmentDetailService
							.selectShipmentDetailList(shipmentDetailParam);
					String shipmentDtIds = "";
					// Get String ids shipment detail separated by comma to update
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						shipmentDtIds += shipmentDetail.getId() + ",";
					}
					ShipmentDetail shipmentDetailUpdate = new ShipmentDetail();
					shipmentDetailUpdate.setOpeCode(shipment.getOpeCode());
					shipmentDetailUpdate.setBlNo(shipment.getBlNo());
					shipmentDetailService.updateShipmentDetailByIds(shipmentDtIds, shipmentDetailUpdate);
				}
				referenceShipment.setOpeCode(shipment.getOpeCode());
				referenceShipment.setBlNo(shipment.getBlNo());
			}
			if (shipmentService.updateShipment(referenceShipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			List<ShipmentDetail> shipmentDetails = shipmentDetailService
					.getShipmentDetailListForSendFReceiveE(shipmentDetail);
			if (shipmentDetails != null) {
				ajaxResult.put("shipmentDetails", shipmentDetails);
			} else {
				ajaxResult = AjaxResult.error();
			}
		}
		return ajaxResult;
	}

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails,
			@PathVariable("shipmentId") Long shipmentId) {

		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			LogisticAccount user = getUser();
			if (shipmentId == null) {
				return error("Không tìm thấy lô cần thêm chi tiết.");
			}

			Shipment shipment = shipmentService.selectShipmentById(shipmentId);
			if (shipment == null || !verifyPermission(shipment.getLogisticGroupId())) {
				return error("Không tìm thấy lô, vui lòng kiểm tra lại thông tin.");
			}

			if (shipment.getOpeCode() == null) {
				return error("Không tìm thấy OPR chô lô này.");
			}

			boolean checkDoStatus = false;
			// Kiem tra B/L No co ton tai o cont Boc Full khong
			if (StringUtils.isNotEmpty(shipment.getBlNo())) {
				Shipment search = new Shipment();
				search.setBlNo(shipment.getBlNo());
				search.setLogisticGroupId(shipment.getLogisticGroupId());
				search.setServiceType(EportConstants.SERVICE_PICKUP_FULL);
				List<Shipment> receiveFullList = shipmentService.selectShipmentList(search);
				if (CollectionUtils.isNotEmpty(receiveFullList)) {

					// lay 1 shipment (thong thuong chi co 1)
					Shipment receiveFShipment = receiveFullList.get(0);

					// Đổi opeCode operateCode -> groupCode
					if (!shipment.getOpeCode().equals(receiveFShipment.getOpeCode())) {
						String parentOpr = dictService.getLabel("carrier_parent_child_list",
								receiveFShipment.getOpeCode());
						if (StringUtils.isEmpty(parentOpr) || !shipment.getOpeCode().equals(parentOpr)) {
							return error(
									"Mã OPR cho lô nhận container hàng và lô giao container rỗng đang khác nhau. Vui lòng kiểm tra lại.");
						}
					}
				} else {
					// bat co de OM kiem tra lai chung tu goc
					checkDoStatus = true;
				}
			} else {
				// bat co de OM kiem tra lai chung tu goc
				checkDoStatus = true;
			}

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {
				if (inputDetail.getId() != null) {
					// Case update
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());
					// Check permission
					if (!shipmentDetailReference.getLogisticGroupId().equals(user.getGroupId())) {
						return error("Không tìm thấy thông tin, vui lòng kiểm tra lại");
					}
					updateShipment = false;
					// Check status can update before user verify opt to make order
					if ("N".equalsIgnoreCase(shipmentDetailReference.getUserVerifyStatus())) {
						shipmentDetailReference.setContainerNo(inputDetail.getContainerNo());
						shipmentDetailReference.setSztp(inputDetail.getSztp());
						shipmentDetailReference.setSztpDefine(inputDetail.getSztpDefine());
						shipmentDetailReference.setConsignee(inputDetail.getConsignee());
						shipmentDetailReference.setDetFreeTime(inputDetail.getDetFreeTime());
						shipmentDetailReference.setEmptyExpiredDem(inputDetail.getEmptyExpiredDem());
						shipmentDetailReference.setEmptyDepotLocation(
								getEmptyDepotLocationName(shipment.getOpeCode(), inputDetail.getSztp()));
						if (EportConstants.DROP_EMPTY_TO_VESSEL.equals(shipment.getSendContEmptyType())) {
							shipmentDetailReference.setVslNm(inputDetail.getVslNm());
							shipmentDetailReference.setVoyNo(inputDetail.getVoyNo());
							shipmentDetailReference.setYear(inputDetail.getYear());
							shipmentDetailReference.setVslName(inputDetail.getVslName());
							shipmentDetailReference.setVoyCarrier(inputDetail.getVoyCarrier());
							shipmentDetailReference.setEta(inputDetail.getEta());
							shipmentDetailReference.setEtd(inputDetail.getEtd());
						}
					}
					shipmentDetailReference.setRemark(inputDetail.getRemark());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetailReference) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetailReference.getContainerNo());
					}
				} else {
					// Case insert
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setContainerNo(inputDetail.getContainerNo());
					shipmentDetail.setSztp(inputDetail.getSztp());
					shipmentDetail.setSztpDefine(inputDetail.getSztpDefine());
					shipmentDetail.setConsignee(inputDetail.getConsignee());
					shipmentDetail.setDetFreeTime(inputDetail.getDetFreeTime());
					shipmentDetail.setEmptyExpiredDem(inputDetail.getEmptyExpiredDem());
					shipmentDetail.setBlNo(shipment.getBlNo());
					shipmentDetail.setOpeCode(shipment.getOpeCode());
					shipmentDetail.setEmptyDepotLocation(
							getEmptyDepotLocationName(shipment.getOpeCode(), inputDetail.getSztp()));
					if (EportConstants.DROP_EMPTY_TO_VESSEL.equals(shipment.getSendContEmptyType())) {
						shipmentDetail.setVslNm(inputDetail.getVslNm());
						shipmentDetail.setVoyNo(inputDetail.getVoyNo());
						shipmentDetail.setYear(inputDetail.getYear());
						shipmentDetail.setVslName(inputDetail.getVslName());
						shipmentDetail.setVoyCarrier(inputDetail.getVoyCarrier());
						shipmentDetail.setEta(inputDetail.getEta());
						shipmentDetail.setEtd(inputDetail.getEtd());
					} else {
						shipmentDetail.setVslNm("EMTY");
						shipmentDetail.setVoyNo("0000");
					}
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setShipmentId(shipmentId);
					shipmentDetail.setStatus(1);
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("E");
					shipmentDetail.setCargoType("MT");
					shipmentDetail.setDischargePort("VNDAD");
					// Check do status
					if (checkDoStatus) {
						shipmentDetail.setDoStatus("N");
					}
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				}
			}
			if (updateShipment) {
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
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId,
			@PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds, getUser().getGroupId());
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

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult verifyOtp(@PathVariable("otp") String otp, String shipmentDetailIds, String taxCode,
			boolean creditFlag) {
		try {
			Long.parseLong(otp);
		} catch (Exception e) {
			return error("Mã OTP không hợp lệ. Vui lòng kiểm tra lại.");
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		AjaxResult validateResult = validateShipmentDetailList(shipmentDetails);
		Integer code = (Integer) validateResult.get("code");
		if (code != 0) {
			return validateResult;
		}
		// Check if shipment details is valid.
		if (CollectionUtils.isNotEmpty(shipmentDetails) && shipmentDetails.get(0) != null) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			// Set Shipment status to "PROCESSING"
			if (!EportConstants.SHIPMENT_STATUS_PROCESSING.equals(shipment.getStatus())) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			// Đổi opeCode operateCode -> groupCode. VD Hang tau CMA: CMA,CNC,APL.. -> CMA
			String oprParent = dictService.getLabel("carrier_parent_child_list", shipmentDetails.get(0).getOpeCode());
			if (StringUtils.isNotEmpty(oprParent)) {
				for (ShipmentDetail shpDtl : shipmentDetails) {
					shpDtl.setOpeCode(oprParent);
					shpDtl.setUpdateBy(getUser().getUserName());
				}
			}
			ProcessOrder processOrder = shipmentDetailService.makeOrderSendCont(shipmentDetails, shipment, taxCode,
					creditFlag);
			if (processOrder != null) {
				ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
				try {
					if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_EMPTY)) {
						ajaxResult = AjaxResult
								.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
						ajaxResult.put("processId", processOrder.getId());
						return ajaxResult;
					}
				} catch (Exception e) {
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}

				ajaxResult = AjaxResult
						.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processId", processOrder.getId());
				return ajaxResult;
			}
		}
		logger.error("Không tim thấy lô khi nhập OTP: " + shipmentDetailIds);
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	@Log(title = "Thanh Toán Hạ Rỗng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/payment/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult payment(@PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(3);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

	@PostMapping("/shipment/bl-no")
	@ResponseBody
	public AjaxResult checkShipmentInforByBlNo(@RequestBody ContainerServiceForm inputForm) {
		String blNo = inputForm.getBlNo();
		Shipment shipment = new Shipment();
		if (StringUtils.isAllBlank(blNo)) {
			return error("Hãy nhập B/L No");
		}
		// check bill unique
		shipment.setServiceType(Constants.SEND_CONT_EMPTY);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}
		return success();
	}

	@GetMapping("/containerNo/{containerNo}/sztp")
	@ResponseBody
	public AjaxResult getSztpByContainerNo(@PathVariable("containerNo") String containerNo) {
		String sztp = catosApiService.getSztpByContainerNo(containerNo);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("sztp", sztp);
		return ajaxResult;
	}

	private String getEmptyDepotLocationName(String opr, String sztp) {
		String emptyDepotLocation = "";
		String emptyDepotRule = dictService.getLabel("empty_depot_location", opr);
		if (StringUtils.isNotEmpty((emptyDepotRule))) {
			String[] emptyDepotArr = emptyDepotRule.split(",");
			int length = emptyDepotArr.length;
			for (int i = 0; i < length; i++) {
				if (sztp.equalsIgnoreCase(emptyDepotArr[i])) {
					emptyDepotLocation = emptyDepotArr[length - 1];
				}
			}
		}
		if (StringUtils.isEmpty(emptyDepotLocation)) {
			emptyDepotLocation = configService.getKey("danang.depot.name");
		}
		return emptyDepotLocation;
	}

	@GetMapping("/opr/{opr}/sztp/{sztp}/emptyDepotLocation")
	@ResponseBody
	public AjaxResult getEmptyDepotLocation(@PathVariable("opr") String opr, @PathVariable("sztp") String sztp) {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("emptyDepotLocation", getEmptyDepotLocationName(opr, sztp));
		return ajaxResult;
	}

	@GetMapping("/opr/{opr}/empty-expired-dem/require")
	@ResponseBody
	public AjaxResult checkRequireEmptyExpiredDem(@PathVariable("opr") String opr) {
		String oprRes = dictService.getLabel("empty_expired_dem_not_require_opr_list", opr);
		if (StringUtils.isNotEmpty(oprRes)) {
			return success();
		}
		return error();
	}

	@GetMapping("/berthplan/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageListWithoutOpeCode() {
		AjaxResult ajaxResult = success();
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlanWithoutOpe();
		if (berthplanList != null && berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for (ShipmentDetail i : berthplanList) {
				vesselAndVoyages.add(i.getVslAndVoy());
			}
			ajaxResult.put("berthplanList", berthplanList);
			ajaxResult.put("vesselAndVoyages", vesselAndVoyages);
			return ajaxResult;
		}
		return error();
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
		shipmentComment.setServiceType(EportConstants.SERVICE_DROP_EMPTY);
		shipmentComment.setCommentTime(new Date());
		shipmentComment.setSeenFlg(true);
		shipmentCommentService.insertShipmentComment(shipmentComment);

		// Send notification to om
		try {
			mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, shipmentComment.getTopic(),
					shipmentComment.getContent(), "", EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
		} catch (MqttException e) {
			logger.error("Fail to send message om notification app: " + e);
		}

		// Add id to make background grey (different from other comment)
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentId", shipmentComment.getId());
		return ajaxResult;
	}

	@PostMapping("/shipment-detail/validation")
	@ResponseBody
	public AjaxResult validateShipmentDetail(String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		AjaxResult validateResult = validateShipmentDetailList(shipmentDetails);
		return validateResult;
	}

	public AjaxResult validateShipmentDetailList(List<ShipmentDetail> shipmentDetails) {
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			return error("Không tìm thấy thông tin chi tiết lô đã chọn.");
		}

		// Container no string separated by comma
		String containerNos = "";

		// validate
		ShipmentDetail shipmentDetailReference = shipmentDetails.get(0);
		Shipment shipment = shipmentService.selectShipmentById(shipmentDetailReference.getShipmentId());
		// List sztp can register on eport get from dictionary
		// All sztp that not in this list is invalid
		List<String> sztps = dictService.getListTag("sys_size_container_eport");
		for (int i = 0; i < shipmentDetails.size(); i++) {
			if (StringUtils.isEmpty(shipmentDetails.get(i).getContainerNo())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa nhập số container!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getConsignee())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn chủ hàng!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getSztp())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn kích thước!");
			}
			if (!shipmentDetailReference.getConsignee().equals(shipmentDetails.get(i).getConsignee())) {
				return error("Tên chủ hàng không được khác nhau!");
			}
			if (shipment.getSendContEmptyType().equals(EportConstants.DROP_EMPTY_TO_VESSEL)) {
				if (StringUtils.isEmpty(shipmentDetails.get(i).getVslNm())) {
					return error("Hàng " + (i + 1) + ": Quý khách chưa chọn tàu!");
				}
				if (StringUtils.isEmpty(shipmentDetails.get(i).getVoyNo())) {
					return error("Hàng " + (i + 1) + ": Quý khách chưa chọn chuyến!");
				}
				if (!shipmentDetailReference.getVslNm().equals(shipmentDetails.get(i).getVslNm())) {
					return error("Tàu và Chuyến không được khác nhau!");
				}
				if (!shipmentDetailReference.getVoyNo().equals(shipmentDetails.get(i).getVoyNo())) {
					return error("Tàu và Chuyến không được khác nhau!");
				}
			}
			// Validate sztp
			if (!sztps.contains(shipmentDetails.get(i).getSztp())) {
				return error(
						"Kích thước " + shipmentDetails.get(i).getSztp() + " không được phép làm lệnh trên eport.");
			}
			containerNos += shipmentDetails.get(i).getContainerNo() + ",";
		}

		if (shipment.getSendContEmptyType().equals(EportConstants.DROP_EMPTY_TO_VESSEL)) {
			// Valide vslnm and voy no exist in catos
			// Get berth plan info
			BerthPlanInfo berthPlanInfoParam = new BerthPlanInfo();
			berthPlanInfoParam.setVslCd(shipmentDetailReference.getVslNm());
			berthPlanInfoParam.setCallSeq(shipmentDetailReference.getVoyNo());
			BerthPlanInfo berthPlanInfo = catosApiService.getBerthPlanInfo(berthPlanInfoParam);
			if (berthPlanInfo == null) {
				return error(
						"Tàu chuyến không tồn tại trong hệ thống, quý khách vui lòng chọn tàu chuyến từ danh sách.");
			}
		}

		// validate consignee exist in catos
		if (catosApiService.checkConsigneeExistInCatos(shipmentDetailReference.getConsignee()) == 0) {
			return error(
					"Tên chủ hàng quý khách nhập không đúng, vui lòng chọn tên chủ hàng từ trong danh sách của hệ thống gợi ý.");
		}

		// Validate container has job order no
		containerNos = containerNos.substring(0, containerNos.length() - 1);
		Map<String, ContainerInfoDto> ctnrMap = getContainerInfoFromCatos(containerNos);
		String containerHasOrderdEmpty = "";
		String containerHasOrderdFull = "";
		ContainerInfoDto ctnrInfoF = null;
		ContainerInfoDto ctnrInfoE = null;
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			// Get ctnr info by container no in catos
			// Get container info (F or E) by container no + FE(F or E)
			ctnrInfoF = ctnrMap.get(shipmentDetail.getContainerNo() + "F");
			if (ctnrInfoF != null) {
				// Container has job order no 2 => has order
				if (StringUtils.isNotEmpty(ctnrInfoF.getJobOdrNo())) {
					containerHasOrderdFull += shipmentDetail.getContainerNo() + ",";
				}
			}
			// Get container info (F or E) by container no + FE(F or E)
			ctnrInfoE = ctnrMap.get(shipmentDetail.getContainerNo() + "E");
			if (ctnrInfoE != null) {
				// Container has job order no 2 => has order
				if (StringUtils.isNotEmpty(ctnrInfoE.getJobOdrNo())) {
					containerHasOrderdFull += shipmentDetail.getContainerNo() + ",";
				}
			}
		}

		String errorMsg = "";
		if (StringUtils.isNotEmpty(containerHasOrderdEmpty)) {
			errorMsg += "Các container " + containerHasOrderdEmpty.substring(0, containerHasOrderdEmpty.length() - 1)
					+ " đã có lệnh hạ rỗng.<br>";
		}
		if (StringUtils.isNotEmpty(containerHasOrderdFull)) {
			errorMsg += "Các container " + containerHasOrderdFull.substring(0, containerHasOrderdFull.length() - 1)
					+ " đã có lệnh hạ hàng.";
		}

		if (StringUtils.isNotEmpty(errorMsg)) {
			return error(errorMsg);
		}

		return success();
	}

	@PostMapping("/order-cancel/shipment-detail")
	@ResponseBody
	public AjaxResult reqCancelOrderContainer(String shipmentDetailIds, String contReqRemark) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {

			// Validate before send req cancel order
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (!"Y".equalsIgnoreCase(shipmentDetail.getProcessStatus())) {
					return error("Container quý khách chọn chưa được làm lệnh.");
				}
			}

			// Update status req cancel order
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setProcessStatus(EportConstants.PROCESS_STATUS_SHIPMENT_DETAIL_DELETE);
				shipmentDetail.setUpdateBy(getUser().getUserName());
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}

			ShipmentDetail shipmentDetail = shipmentDetails.get(0);
			// Write comment for topic supply container
			if (StringUtils.isNotEmpty(contReqRemark)) {
				ShipmentComment shipmentComment = new ShipmentComment();
				shipmentComment.setLogisticGroupId(getUser().getGroupId());
				shipmentComment.setShipmentId(shipmentDetail.getShipmentId());
				shipmentComment.setUserId(getUserId());
				shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
				shipmentComment.setUserAlias(getGroup().getGroupName());
				shipmentComment.setCommentTime(new Date());
				shipmentComment.setContent(contReqRemark);
				shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CANCEL_DROP_EMPTY);
				shipmentComment.setServiceType(shipmentDetail.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}

			// Set up data to send app notificaton
			String title = "ePort: Yêu cầu huỷ lệnh giao container rỗng.";
			String msg = "Có yêu cầu huỷ lệnh giao container rỗng cho Bill: " + shipmentDetail.getBlNo()
					+ ", Hãng tàu: " + shipmentDetail.getOpeCode() + ", Trucker: " + getGroup().getGroupName()
					+ ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg,
						configService.getKey("domain.admin.name") + EportConstants.URL_CANCEL_ORDER_SUPPORT,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request cancel order drop empty: " + e);
			}
			return success("Đã yêu cầu hủy lệnh, quý khách vui lòng đợi bộ phận thủ tục xử lý.");
		}

		return error("Yêu cầu hủy lệnh thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
	}

	/**
	 * Get container info from catos
	 * 
	 * @param containerNos
	 * @return Map string object with key is container no and value is containerInfo
	 */
	private Map<String, ContainerInfoDto> getContainerInfoFromCatos(String containerNos) {
		List<ContainerInfoDto> containerInfoDtos = catosApiService.getContainerInfoDtoByContNos(containerNos);
		Map<String, ContainerInfoDto> containerInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
				containerInfoMap.put(containerInfoDto.getCntrNo() + containerInfoDto.getFe(), containerInfoDto);
			}
		}
		return containerInfoMap;
	}
}