/**
 * 
 */
package vn.com.irtech.eport.logistic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.annotation.RepeatSubmit;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.exception.file.InvalidExtensionException;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.file.FileUploadUtils;
import vn.com.irtech.eport.common.utils.file.MimeTypeUtils;
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.form.ContainerServiceForm;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

/**
 * @author Trong Hieu
 *
 */
@Controller
@RequiresPermissions("logistic:order")
@RequestMapping("/logistic/special-service")
public class LogisticSpecialServiceController extends LogisticBaseController {
	private static final Logger logger = LoggerFactory.getLogger(LogisticSpecialServiceController.class);
	private final String PREFIX = "logistic/specialService";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private IProcessBillService processBillService;

	@Autowired
	private CustomQueueService customQueueService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private DictService dictService;

	@Autowired
	private ConfigService configService;

	@GetMapping()
	public String sendContFull(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());
		mmap.put("oprListBookingCheck", dictService.getListTag("opr_list_booking_check"));
		return PREFIX + "/index";
	}

	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");

		mmap.put("taxCode", getGroup().getMst());
		mmap.put("oprCodeList", oprCodeList);
		mmap.put("oprListBookingCheck", dictService.getListTag("opr_list_booking_check"));
		return PREFIX + "/add";
	}

	@GetMapping("/shipment/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
		}
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentId(id);
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageListNotFileType(shipmentImage);
		mmap.put("shipmentFiles", shipmentImages);
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

	@GetMapping("/payment/{shipmentDetailIds}")
	public String paymentForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			shipmentDetailIds = "";
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetailIds += shipmentDetail.getId() + ",";
			}
			shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length() - 1);
			mmap.put("shipmentDetailIds", shipmentDetailIds);

			// Get process bill
			ProcessBill processBillParam = new ProcessBill();
			processBillParam.setPaymentStatus("N");
			Map<String, Object> params = new HashMap<>();
			params.put("shipmentDetailIds", Convert.toStrArray(shipmentDetailIds));
			processBillParam.setParams(params);
			List<ProcessBill> processBills = processBillService.selectProcessBillList(processBillParam);
			mmap.put("processBills", processBills);
		}
		return PREFIX + "/paymentForm";
	}

	@GetMapping("/custom-status/{shipmentDetailIds}")
	public String checkCustomStatus(@PathVariable String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
				mmap.put("contList", shipmentDetails);
			}
		}
		return PREFIX + "/checkCustomStatus";
	}

	@GetMapping("/{shipmentDetailId}/attach")
	public String getAttachView(@PathVariable("shipmentDetailId") String shipmentDetailId, ModelMap mmap) {
		ShipmentImage shipmentImageParam = new ShipmentImage();
		shipmentImageParam.setShipmentDetailId(shipmentDetailId);
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImageParam);
		if (CollectionUtils.isNotEmpty(shipmentImages)) {
			for (ShipmentImage shipmentImage : shipmentImages) {
				shipmentImage.setPath(serverConfig.getUrl() + shipmentImage.getPath());
			}
		}
		mmap.put("shipmentDetailId", shipmentDetailId);
		mmap.put("files", shipmentImages);
		return PREFIX + "/attach";
	}

	@GetMapping("/req/cancel/confirmation")
	public String getCancelConfirmationForm() {
		return PREFIX + "/confirmRequestCancel";
	}

	@Log(title = "Tạo Lô Hạ Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
	@ResponseBody
	public AjaxResult addShipment(@RequestBody Shipment shipment) {
		LogisticAccount user = getUser();

		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(EportConstants.SERVICE_SPECIAL_SERVICE);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);

		boolean attachBooking = false;
		if (StringUtils.isNotEmpty(shipment.getParams().get("ids").toString())) {
			attachBooking = true;
		}

		if (shipmentService.insertShipment(shipment) == 1) {
			if (attachBooking) {
				ShipmentImage shipmentImage = new ShipmentImage();
				shipmentImage.setShipmentId(shipment.getId());
				Map<String, Object> map = new HashMap<>();
				map.put("ids", Convert.toStrArray(shipment.getParams().get("ids").toString()));
				shipmentImage.setParams(map);
				shipmentImageService.updateShipmentImageByIds(shipmentImage);
			}
			return success("Thêm lô thành công");
		}
		return error("Có lỗi khi thực hiện thêm lô, vui lòng thử lại");
	}

	@PostMapping("/shipment/{shipmentId}")
	@ResponseBody
	public AjaxResult editShipment(@RequestBody Shipment input, @PathVariable Long shipmentId) {
		LogisticAccount user = getUser();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			// Chi update cac item cho phep
			// Kiem tra co update so luong co giam -> kiem tra xem so luong details
			if (input.getContainerAmount() < shipment.getContainerAmount()) {
				ShipmentDetail shipmentSearch = new ShipmentDetail();
				shipmentSearch.setShipmentId(shipmentId);
				long contNumber = shipmentDetailService.countShipmentDetailList(shipmentSearch);
				if (contNumber > input.getContainerAmount()) {
					return error("Không thể chỉnh sửa số lượng container nhỏ hơn danh sách khai báo.");
				}
			}

			shipment.setContainerAmount(input.getContainerAmount());
			shipment.setRemark(input.getRemark());
			shipment.setUpdateBy(user.getFullName());
			// Update OPR, Booking when status is 1 or 2
			if (shipment.getStatus().equals(EportConstants.SHIPMENT_STATUS_INIT)) {
				shipment.setOpeCode(input.getOpeCode());
				if (StringUtils.isNotEmpty(input.getBookingNo())) {
					shipment.setBookingNo(input.getBookingNo());
					shipment.setBlNo("");
				}
				if (StringUtils.isNotEmpty(input.getBlNo())) {
					shipment.setBlNo(input.getBlNo());
					shipment.setBookingNo("");
				}
			} else if (shipment.getStatus().equals(EportConstants.SHIPMENT_STATUS_SAVE)) {
				if (!shipment.getOpeCode().equals(input.getOpeCode())
						|| !shipment.getBookingNo().equalsIgnoreCase(input.getBookingNo())) {
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
					shipmentDetailUpdate.setOpeCode(input.getOpeCode());
					if (StringUtils.isNotEmpty(input.getBookingNo())) {
						shipmentDetailUpdate.setBlNo("");
						shipmentDetailUpdate.setBookingNo(input.getBookingNo());
					}
					if (StringUtils.isNotEmpty(input.getBlNo())) {
						shipmentDetailUpdate.setBlNo(input.getBlNo());
						shipmentDetailUpdate.setBookingNo("");
					}

					shipmentDetailService.updateShipmentDetailByIds(shipmentDtIds, shipmentDetailUpdate);
				}
				shipment.setOpeCode(input.getOpeCode());
				if (StringUtils.isNotEmpty(input.getBookingNo())) {
					shipment.setBookingNo(input.getBookingNo());
					shipment.setBlNo("");
				}
				if (StringUtils.isNotEmpty(input.getBlNo())) {
					shipment.setBlNo(input.getBlNo());
					shipment.setBookingNo("");
				}
			}

			if (shipmentService.updateShipment(shipment) == 1) {
				ShipmentImage shipmentImage = new ShipmentImage();
				shipmentImage.setShipmentId(shipment.getId());
				Map<String, Object> map = new HashMap<>();
				map.put("ids", Convert.toStrArray(input.getParams().get("ids").toString()));
				shipmentImage.setParams(map);
				shipmentImageService.updateShipmentImageByIds(shipmentImage);
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
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
				ajaxResult = AjaxResult.error("Không tìm thấy thông tin lô hàng, vui lòng kiểm tra lại.");
			}
		} else {
			ajaxResult = AjaxResult.error("Lô không tồn tại, vui lòng kiểm tra lại.");
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

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {

				// validate if cont have status cont special REQUEST | DONE
				if (shipmentDetailService.isHaveContSpacialRequest(inputDetail)
						|| shipmentDetailService.isHaveContSpacialYes(inputDetail)) {
					return error("Không thể thay đổi thông tin các container đang chờ hoặc đã được yêu cầu xét duyệt.");
				}

				if (inputDetail.getId() != null) {

					// Case update
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());

					// Check permission
					if (!shipmentDetailReference.getLogisticGroupId().equals(user.getGroupId())) {
						return error("Không tìm thấy thông tin, vui lòng kiểm tra lại");
					}

					updateShipment = false;

					shipmentDetailReference.setContainerNo(inputDetail.getContainerNo());
					shipmentDetailReference.setSztp(inputDetail.getSztp());
					shipmentDetailReference.setSztpDefine(inputDetail.getSztpDefine());
					shipmentDetailReference.setConsignee(inputDetail.getConsignee());
					shipmentDetailReference.setVslNm(inputDetail.getVslNm());
					shipmentDetailReference.setVoyNo(inputDetail.getVoyNo());
					shipmentDetailReference.setYear(inputDetail.getYear());
					shipmentDetailReference.setVslName(inputDetail.getVslName());
					shipmentDetailReference.setVoyCarrier(inputDetail.getVoyCarrier());
					shipmentDetailReference.setEta(inputDetail.getEta());
					shipmentDetailReference.setEtd(inputDetail.getEtd());
					shipmentDetailReference.setDischargePort(inputDetail.getDischargePort());
					shipmentDetailReference.setWgt(inputDetail.getWgt());
					shipmentDetailReference.setCargoType(inputDetail.getCargoType());
					shipmentDetailReference.setCommodity(inputDetail.getCommodity());
					shipmentDetailReference.setSealNo(inputDetail.getSealNo());
					shipmentDetailReference.setLoadingPort(inputDetail.getLoadingPort());
					shipmentDetailReference.setDateReceipt(inputDetail.getDateReceipt());
					shipmentDetailReference.setRemark(inputDetail.getRemark());

					if (shipmentDetailService.updateShipmentDetail(shipmentDetailReference) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetailReference.getContainerNo());
					}
				} else {
					// Case insertShipmentDetail shipmentDetail = new ShipmentDetail();
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setContainerNo(inputDetail.getContainerNo());
					shipmentDetail.setSztp(inputDetail.getSztp());
					shipmentDetail.setSztpDefine(inputDetail.getSztpDefine());
					shipmentDetail.setConsignee(inputDetail.getConsignee());
					shipmentDetail.setVslNm(inputDetail.getVslNm());
					shipmentDetail.setVoyNo(inputDetail.getVoyNo());
					shipmentDetail.setYear(inputDetail.getYear());
					shipmentDetail.setVslName(inputDetail.getVslName());
					shipmentDetail.setVoyCarrier(inputDetail.getVoyCarrier());
					shipmentDetail.setEta(inputDetail.getEta());
					shipmentDetail.setEtd(inputDetail.getEtd());
					shipmentDetail.setDischargePort(inputDetail.getDischargePort());
					shipmentDetail.setWgt(inputDetail.getWgt());
					shipmentDetail.setCargoType(inputDetail.getCargoType());
					shipmentDetail.setCommodity(inputDetail.getCommodity());
					shipmentDetail.setSealNo(inputDetail.getSealNo());
					shipmentDetail.setOpeCode(shipment.getOpeCode());
					shipmentDetail.setBookingNo(shipment.getBookingNo());
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setShipmentId(shipmentId);
					shipmentDetail.setDateReceipt(inputDetail.getDateReceipt());
					shipmentDetail.setStatus(1);
					shipmentDetail.setDateReceiptStatus("N");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("F");
					shipmentDetail.setSpecialService(inputDetail.getSpecialService());
					shipmentDetail.setLoadingPort(inputDetail.getLoadingPort());
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
		return

		error("Lưu khai báo thất bại");
	}

	@Log(title = "Xóa Khai Báo Cont", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail/delete")
	@Transactional
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable("shipmentId") Long shipmentId,
			@RequestBody ContainerServiceForm inputForm) {
		// check shipment permission
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (!verifyPermission(shipment.getLogisticGroupId())) {
			return AjaxResult.error("Không tìm thấy lô, xin vui lòng kiểm tra lại");
		}
		String shipmentDetailIds = inputForm.getIds();
		// delete shipment details
		if (shipmentDetailIds != null) {
			// Get shipment detail list by shipment detail ids
			// Check if shipment detail qualify for delete (not verify otp is qualify)
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
					getUser().getGroupId());
			if (CollectionUtils.isEmpty(shipmentDetails)) {
				// No shipment detail was found => return error
				return error("không tìm thấy container cần xóa trong hệ thống, vui lòng kiểm tra lại.");
			}
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if ("Y".equals(shipmentDetail.getUserVerifyStatus())) {
					return error(
							"Những container hiện tại đã được xác nhận làm lệnh, do đó không thể xóa những container này.");
				}
			}

			// just delete shipmentIds for shipment has been verified before
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds, getUser().getGroupId());
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			// Reset status shipment to INIT if deleted all shipment details
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
				shipmentService.updateShipment(shipment);
			}
			return success("Xóa khai báo thành công");
		}
		return error("Xóa khai báo thất bại");
	}

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult verifyOtp(@PathVariable String otp, String shipmentDetailIds, String taxCode,
			boolean creditFlag) {
		try {
			Long.parseLong(otp);
		} catch (Exception e) {
			return error("Mã OTP nhập vào không hợp lệ!");
		}
		// TODO Un-support cash
		if (!creditFlag) {
			return error("Lỗi! Chưa hỗ trợ thanh toán trả trước (cash).");
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
			return error("Mã OTP không chính xác hoặc đã hết hạn, xin vui lòng kiểm tra lại.");
		}
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		AjaxResult validateResult = validateShipmentDetailList(shipmentDetails);
		Integer code = (Integer) validateResult.get("code");
		if (code != 0) {
			return validateResult;
		}
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = AjaxResult.success();
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!EportConstants.SHIPMENT_STATUS_PROCESSING.equals(shipment.getStatus())) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}

			// set shipment detail ids
			shipmentDetailIds = "";
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetailIds += shipmentDetail.getId() + ",";
			}

			shipmentDetailService.makeOrderSpecialService(shipmentDetails, shipment, taxCode, creditFlag);

			ajaxResult = AjaxResult.success("Yêu cầu đang được xử lý, xin vui lòng đợi trong giây lát.");
			return ajaxResult;
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	@Log(title = "Thanh Toán Hạ Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/payment/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult payment(@PathVariable String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(3); // TODO Chuyen thanh dung EportConstants
				shipmentDetail.setPaymentStatus("Y");
				if ("VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
					shipmentDetail.setStatus(4);
					shipmentDetail.setCustomStatus("Y");
				}
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

	@Log(title = "Khai Báo Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList,
			@RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		// FIXME chuyen params thanh POST REQUEST BODY
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
					getUser().getGroupId());
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declare no list to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetail.setCustomScanTime(new Date());
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					customQueueService.offerShipmentDetail(shipmentDetail);
				}
				return success();
			}
		}
		return error();
	}

	@GetMapping("/berthplan/ope-code/list")
	@ResponseBody
	public AjaxResult getOpeCodeList() {
		AjaxResult ajaxResult = success();
		List<String> opeCodeList = catosApiService.selectOpeCodeListInBerthPlan();
		if (opeCodeList.size() > 0) {
			ajaxResult.put("opeCodeList", opeCodeList);
			return ajaxResult;
		}
		return error();

	}

	@GetMapping("/berthplan/ope-code/{opeCode}/vessel-voyage/list")
	@ResponseBody
	public AjaxResult getVesselVoyageList(@PathVariable String opeCode) {
		AjaxResult ajaxResult = success();
		List<ShipmentDetail> berthplanList = catosApiService.selectVesselVoyageBerthPlan(opeCode);
		if (berthplanList.size() > 0) {
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

	@GetMapping("/containerNo")
	@ResponseBody
	public AjaxResult getContainerInfoByContainerNo(String containerNo, Long shipmentId) {
		ContainerInfoDto cntrInfo = null;
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		// Case get data reference from catos
		List<ContainerInfoDto> containerInfoDtos = catosApiService.getContainerInfoDtoByContNos(containerNo);
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
				if (EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(containerInfoDto.getCntrState())) {
					// Check mapping with bl no
					if ((StringUtils.isNotEmpty(shipment.getBlNo())
							&& shipment.getBlNo().equalsIgnoreCase(containerInfoDto.getBlNo()))
							|| StringUtils.isEmpty(containerInfoDto.getBlNo())) {
						cntrInfo = containerInfoDto;
					}
				}
			}
		}

		if (cntrInfo == null) {
			// Case make order drop full
			String sztp = catosApiService.getSztpByContainerNo(containerNo);
			cntrInfo = new ContainerInfoDto();
			cntrInfo.setSztp(sztp);
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("containerInfo", cntrInfo);
		return ajaxResult;
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
		shipmentComment.setServiceType(EportConstants.SERVICE_SPECIAL_SERVICE);
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

		String containerNo = "";
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (EportConstants.SPECIAL_SERVICE_SAMPLE == shipmentDetail.getSpecialService()) {
				containerNo += shipmentDetail.getContainerNo() + ",";
			}
		}

		if (StringUtils.isNotEmpty(containerNo)) {
			return error("Các container " + containerNo.substring(0, containerNo.length() - 1)
					+ " cần đính kèm tệp để có thể thực hiện dịch vụ.");
		}
		return success();
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

	@GetMapping("/shipments/{shipmentId}/shipment-images")
	@ResponseBody
	public AjaxResult getShipmentImages(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentImage shipmentImage = new ShipmentImage();
			shipmentImage.setShipmentId(shipmentId);
			List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageListNotFileType(shipmentImage);
			for (ShipmentImage shipmentImage2 : shipmentImages) {
				shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
			}
			ajaxResult.put("shipmentFiles", shipmentImages);
			return ajaxResult;
		}
		return error();
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
				shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CANCEL_DROP_FULL);
				shipmentComment.setServiceType(shipmentDetail.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}

			// Set up data to send app notificaton
			String title = "ePort: Yêu cầu huỷ lệnh giao container hàng.";
			String msg = "Có yêu cầu huỷ lệnh giao container hàng cho Booking: " + shipmentDetail.getBookingNo()
					+ ", Hãng tàu: " + shipmentDetail.getOpeCode() + ", Trucker: " + getGroup().getGroupName()
					+ ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg,
						configService.getKey("domain.admin.name") + EportConstants.URL_CANCEL_ORDER_SUPPORT,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request cancel order drop full: " + e);
			}
			return success("Đã yêu cầu hủy lệnh, quý khách vui lòng đợi bộ phận thủ tục xử lý.");
		}

		return error("Yêu cầu hủy lệnh thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
	}

	@GetMapping("/shipment/{shipmentId}/custom/notification")
	@ResponseBody
	public AjaxResult sendNotificationCustomError(@PathVariable("shipmentId") String shipmentId) {
		try {
			mqttService.sendNotification(NotificationCode.NOTIFICATION_OM_CUSTOM, "Lỗi hải quan lô " + shipmentId,
					configService.getKey("domain.admin.name") + "/om/support/custom/" + shipmentId);
		} catch (MqttException e) {
			logger.error("Gửi thông báo lỗi hải quan cho om: " + e);
		}
		return success();
	}

	@PostMapping("/file")
	@ResponseBody
	public AjaxResult saveFile(MultipartFile file) throws IOException, InvalidExtensionException {
		String basePath = String.format("%s/%s", Global.getUploadPath() + "/booking", getUser().getGroupId());
		String now = DateUtils.dateTimeNow();
		String fileName = String.format("file%s.%s", now, FileUploadUtils.getExtension(file));
		String filePath = FileUploadUtils.upload(basePath, fileName, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setPath(filePath);
		shipmentImage.setCreateTime(DateUtils.getNowDate());
		shipmentImage.setCreateBy(getUser().getFullName());
		shipmentImageService.insertShipmentImage(shipmentImage);

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentFileId", shipmentImage.getId());
		return ajaxResult;
	}

	@DeleteMapping("/booking/file")
	@ResponseBody
	public AjaxResult deleteFile(Long id) throws IOException {
		ShipmentImage shipmentImageParam = new ShipmentImage();
		shipmentImageParam.setId(id);
		ShipmentImage shipmentImage = shipmentImageService.selectShipmentImageById(shipmentImageParam);
		String[] fileArr = shipmentImage.getPath().split("/");
		File file = new File(
				Global.getUploadPath() + "/booking/" + getUser().getGroupId() + "/" + fileArr[fileArr.length - 1]);
		if (file.delete()) {
			shipmentImageService.deleteShipmentImageById(id);
		}
		return success();
	}

	@PostMapping("/shipment-detail/{shipmentDetailId}/file")
	@ResponseBody
	public AjaxResult saveShipmentDetailFile(MultipartFile file,
			@PathVariable("shipmentDetailId") Long shipmentDetailId) throws IOException, InvalidExtensionException {

		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		if (shipmentDetail == null) {
			return error("Không xác định được thông tin container cần đính kèm.");
		}

		String basePath = String.format("%s/%s",
				Global.getUploadPath() + "/" + getUser().getGroupId() + "/shipment-detail", shipmentDetailId);
		String now = DateUtils.dateTimeNow();
		String fileName = String.format("file%s.%s", now, FileUploadUtils.getExtension(file));
		String filePath = FileUploadUtils.upload(basePath, fileName, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setPath(filePath);
		shipmentImage.setShipmentId(shipmentDetail.getShipmentId());
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		shipmentImage.setCreateTime(DateUtils.getNowDate());
		shipmentImage.setCreateBy(getUser().getFullName());
		shipmentImageService.insertShipmentImage(shipmentImage);

		AjaxResult ajaxResult = AjaxResult.success();
		shipmentImage.setPath(serverConfig.getUrl() + shipmentImage.getPath());
		ajaxResult.put("shipmentFile", shipmentImage);
		return ajaxResult;
	}

	@DeleteMapping("/file")
	@ResponseBody
	public AjaxResult deleteShipmentDetailFile(Long id) throws IOException {
		// TODO: Validate permission before delete file

		ShipmentImage shipmentImageParam = new ShipmentImage();
		shipmentImageParam.setId(id);
		ShipmentImage shipmentImage = shipmentImageService.selectShipmentImageById(shipmentImageParam);
		String[] fileArr = shipmentImage.getPath().split("/");
		File file = new File(
				Global.getUploadPath() + "/booking/" + getUser().getGroupId() + "/" + fileArr[fileArr.length - 1]);
		if (file.delete()) {
			shipmentImageService.deleteShipmentImageById(id);
		}
		return success();
	}

	@PostMapping("/container/shifting")
	@ResponseBody
	public AjaxResult checkContainerNeedShifting(String containerNos, String blNo, String bookingNo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("containerNos", containerNos);
		map.put("blNo", blNo);
		map.put("bookingNo", bookingNo);
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerUnderShifting(map);
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			return error();
		}
		return success();
	}
}
