package vn.com.irtech.eport.logistic.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
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
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Controller
@RequiresPermissions("logistic:order")
@RequestMapping("/logistic/send-cont-full")
public class LogisticSendContFullController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticSendContFullController.class);

	private final String PREFIX = "logistic/sendContFull";

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

	@Autowired
	private DictService dictDataService;

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
			mmap.put("taxCode", getGroup().getMst());
		}
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentId(id);
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageListNotFileType(shipmentImage);
		mmap.put("shipmentFiles", shipmentImages);
		mmap.put("oprListBookingCheck", dictService.getListTag("opr_list_booking_check"));
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

	@GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
	public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {
		mmap.put("containerNo", containerNo);
		mmap.put("sztp", sztp);
		mmap.put("shipmentDetailId", shipmentDetailId);

		mmap.put("contCargoTypes", dictDataService.getType("cont_cargo_type"));
		mmap.put("contDangerousImos", dictDataService.getType("cont_dangerous_imo"));
		mmap.put("contDangerousUnnos", dictDataService.getType("cont_dangerous_unno"));

		mmap.put("shipmentDetail", this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId));

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		mmap.put("shipmentFiles", shipmentImages);

		return PREFIX + "/detail";

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
		if (StringUtils.isNotEmpty(shipment.getBookingNo())) {
			shipment.setBookingNo(shipment.getBookingNo().toUpperCase());

			Pattern p = Pattern.compile("\\w*");
			if (!p.matcher(shipment.getBookingNo()).matches()) {
				return error("Booking không được chứa ký tự đặc biệt, quý khách vui lòng kiểm tra lại.");
			}
		}
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceType(EportConstants.SERVICE_DROP_FULL);
		shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);

		boolean attachBooking = false;
		// List opr need to attach booking (domestic container)
		List<String> oprList = dictService.getListTag("opr_list_booking_check");
		if (oprList.contains(shipment.getOpeCode())) {
			attachBooking = true;
			if (StringUtils.isEmpty(shipment.getParams().get("ids").toString())) {
				return error("Bạn chưa đính kèm tệp booking.");
			}
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

	@PostMapping("/unique/booking-no")
	@ResponseBody
	public AjaxResult checkBookingNoUnique(@RequestBody ContainerServiceForm inputForm) {
		String bookingNo = inputForm.getBookingNo();
		if (StringUtils.isAllBlank(bookingNo)) {
			return error("Hãy nhập số Booking");
		}
		Shipment shipment = new Shipment();
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBookingNo(bookingNo);
		shipment.setServiceType(EportConstants.SERVICE_DROP_FULL);
		// if(catosApiService.checkBookingNoForSendFReceiveE(bookingNo, "F").intValue()
		// == 0) {
		// return error("Booking No này chưa có trong hệ thống. Vui lòng liên hệ OM để
		// tạo !");
		// }
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error(String.format("Số Booking '%s' đã tồn tại!", bookingNo));
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
					shipment.setBookingNo(input.getBookingNo().toUpperCase());
					Pattern p = Pattern.compile("\\w*");
					if (!p.matcher(shipment.getBookingNo()).matches()) {
						return error("Booking không được chứa ký tự đặc biệt, quý khách vui lòng kiểm tra lại.");
					}
				}
			} else if (shipment.getStatus().equals(EportConstants.SHIPMENT_STATUS_SAVE)) {
				if (StringUtils.isNotEmpty(input.getBookingNo())) {
					shipment.setBookingNo(input.getBookingNo().toUpperCase());
					Pattern p = Pattern.compile("\\w*");
					if (!p.matcher(shipment.getBookingNo()).matches()) {
						return error("Booking không được chứa ký tự đặc biệt, quý khách vui lòng kiểm tra lại.");
					}
				}
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
					shipmentDetailUpdate.setBookingNo(input.getBookingNo().toUpperCase());
					shipmentDetailService.updateShipmentDetailByIds(shipmentDtIds, shipmentDetailUpdate);
				}
				shipment.setOpeCode(input.getOpeCode());
				shipment.setBookingNo(input.getBookingNo());
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

			boolean attachBooking = false;
			// List opr need to attach booking (domestic container)
			List<String> oprList = dictService.getListTag("opr_list_booking_check");
			if (oprList.contains(shipment.getOpeCode())) {
				attachBooking = true;
			}

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {

				// validate if cont have status cont special REQUEST | DONE
				// if (shipmentDetailService.isHaveContSpacialRequest(inputDetail)
				// || shipmentDetailService.isHaveContSpacialYes(inputDetail)) {
				// 	return error("Không thể thay đổi thông tin các container đang chờ hoặc đã được yêu cầu xét duyệt.");
				// }

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

					shipmentDetailReference.setVgmChk(inputDetail.getVgmChk());
					shipmentDetailReference.setVgmMaxGross(inputDetail.getVgmMaxGross());
					shipmentDetailReference.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

					shipmentDetailReference.setOversizeBack(inputDetail.getOversizeBack());
					shipmentDetailReference.setOversizeFront(inputDetail.getOversizeFront());
					shipmentDetailReference.setOversizeLeft(inputDetail.getOversizeLeft());
					shipmentDetailReference.setOversizeRight(inputDetail.getOversizeRight());
					shipmentDetailReference.setOversizeTop(inputDetail.getOversizeTop());
					shipmentDetailReference.setOversizeType(inputDetail.getOversizeType());

					shipmentDetailReference.setDangerousImo(inputDetail.getDangerousImo());
					shipmentDetailReference.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
					shipmentDetailReference.setDangerousPacking(inputDetail.getDangerousPacking());
					shipmentDetailReference.setDangerousUnno(inputDetail.getDangerousUnno());

					shipmentDetailReference.setTemperature(inputDetail.getTemperature());
					shipmentDetailReference.setDaySetupTemperature(inputDetail.getDaySetupTemperature());
					shipmentDetailReference.setHumidity(inputDetail.getHumidity());
					shipmentDetailReference.setVentilation(inputDetail.getVentilation());
					shipmentDetailReference.setRemark(inputDetail.getRemark());

					// Lưu nếu là cont đặc biệt
					String sztp = inputDetail.getSztp().substring(2, 3);

					if (sztp.equals("R")) {
						if (StringUtils.isEmpty(inputDetail.getFrozenStatus())) {
							shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
						} else if (!(inputDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
								|| inputDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
							shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
						}

					} else {
						shipmentDetailReference.setFrozenStatus("");
					}

					if (StringUtils.isEmpty(inputDetail.getOversize())) {
						shipmentDetailReference.setOversize("");
					} else if (!(inputDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
							|| inputDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
						shipmentDetailReference.setOversize("");
					}

					if (StringUtils.isEmpty(inputDetail.getDangerous())) {
						shipmentDetailReference.setDangerous("");
					} else if (!(inputDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
							|| inputDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
						shipmentDetailReference.setDangerous("");
					}

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
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("F");

					shipmentDetail.setVgmChk(inputDetail.getVgmChk());
					shipmentDetail.setVgmMaxGross(inputDetail.getVgmMaxGross());
					shipmentDetail.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

					shipmentDetail.setOversizeBack(inputDetail.getOversizeBack());
					shipmentDetail.setOversizeFront(inputDetail.getOversizeFront());
					shipmentDetail.setOversizeLeft(inputDetail.getOversizeLeft());
					shipmentDetail.setOversizeRight(inputDetail.getOversizeRight());
					shipmentDetail.setOversizeTop(inputDetail.getOversizeTop());
					shipmentDetail.setOversizeType(inputDetail.getOversizeType());

					shipmentDetail.setDangerousImo(inputDetail.getDangerousImo());
					shipmentDetail.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
					shipmentDetail.setDangerousPacking(inputDetail.getDangerousPacking());
					shipmentDetail.setDangerousUnno(inputDetail.getDangerousUnno());

					shipmentDetail.setTemperature(inputDetail.getTemperature());
					shipmentDetail.setDaySetupTemperature(inputDetail.getDaySetupTemperature());

					shipmentDetail.setHumidity(inputDetail.getHumidity());
					shipmentDetail.setVentilation(inputDetail.getVentilation());
					shipmentDetail.setSupportStatus("P");
					
					// Lưu nếu là cont đặc biệt
					String sztp = inputDetail.getSztp().substring(2, 3);
					if (sztp.equals("R")) {
						// là cont lạnh
						shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
					}
//					if (sztp.equals("P")) {
//						// la cont qua kho
//						shipmentDetail.setOversize(EportConstants.CONT_SPECIAL_STATUS_INIT);
//					}

					if (attachBooking) {
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
		return

		error("Lưu khai báo thất bại");
	}
	
	
	
	
	@Log(title = "Lưu Khai Báo Cont Detail", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail-special")
	@ResponseBody
	public AjaxResult saveShipmentDetailSpecial(@RequestBody List<ShipmentDetail> shipmentDetails,
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

			boolean attachBooking = false;
			// List opr need to attach booking (domestic container)
			List<String> oprList = dictService.getListTag("opr_list_booking_check");
			if (oprList.contains(shipment.getOpeCode())) {
				attachBooking = true;
			}

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {

				// validate if cont have status cont special REQUEST | DONE
				// if (shipmentDetailService.isHaveContSpacialRequest(inputDetail)
				// || shipmentDetailService.isHaveContSpacialYes(inputDetail)) {
				// 	return error("Không thể thay đổi thông tin các container đang chờ hoặc đã được yêu cầu xét duyệt.");
				// }

				if (inputDetail.getId() != null) {

					// Case update
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());

					// Check permission
					if (!shipmentDetailReference.getLogisticGroupId().equals(user.getGroupId())) {
						return error("Không tìm thấy thông tin, vui lòng kiểm tra lại");
					}

					updateShipment = false;
					
					shipmentDetailReference.setDangerousImo(inputDetail.getDangerousImo());
					shipmentDetailReference.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
					shipmentDetailReference.setDangerousPacking(inputDetail.getDangerousPacking());
					shipmentDetailReference.setDangerousUnno(inputDetail.getDangerousUnno());
					
					

					shipmentDetailReference.setContainerNo(inputDetail.getContainerNo());
					shipmentDetailReference.setSztp(inputDetail.getSztp());
					shipmentDetailReference.setSztpDefine(inputDetail.getSztpDefine());
					shipmentDetailReference.setConsignee(inputDetail.getConsignee()); 
					 
					shipmentDetailReference.setYear(inputDetail.getYear());
					 
					shipmentDetailReference.setVoyCarrier(inputDetail.getVoyCarrier());
					shipmentDetailReference.setEta(inputDetail.getEta());
					shipmentDetailReference.setEtd(inputDetail.getEtd());
					shipmentDetailReference.setDischargePort(inputDetail.getDischargePort());
					shipmentDetailReference.setWgt(inputDetail.getWgt());
					shipmentDetailReference.setCargoType(inputDetail.getCargoType());
					shipmentDetailReference.setCommodity(inputDetail.getCommodity());
					shipmentDetailReference.setSealNo(inputDetail.getSealNo());

					shipmentDetailReference.setVgmChk(inputDetail.getVgmChk());
					shipmentDetailReference.setVgmMaxGross(inputDetail.getVgmMaxGross());
					shipmentDetailReference.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

					shipmentDetailReference.setOversizeBack(inputDetail.getOversizeBack());
					shipmentDetailReference.setOversizeFront(inputDetail.getOversizeFront());
					shipmentDetailReference.setOversizeLeft(inputDetail.getOversizeLeft());
					shipmentDetailReference.setOversizeRight(inputDetail.getOversizeRight());
					shipmentDetailReference.setOversizeTop(inputDetail.getOversizeTop());
					shipmentDetailReference.setOversizeType(inputDetail.getOversizeType());

					

					shipmentDetailReference.setTemperature(inputDetail.getTemperature());
					shipmentDetailReference.setDaySetupTemperature(inputDetail.getDaySetupTemperature());
					shipmentDetailReference.setHumidity(inputDetail.getHumidity());
					shipmentDetailReference.setVentilation(inputDetail.getVentilation());
					shipmentDetailReference.setRemark(inputDetail.getRemark());

					// Lưu nếu là cont đặc biệt
					String sztp = inputDetail.getSztp().substring(2, 3);

					if (sztp.equals("R")) {
						if (StringUtils.isEmpty(inputDetail.getFrozenStatus())) {
							shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
						} else if (!(inputDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
								|| inputDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
							shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
						}

					} else {
						shipmentDetailReference.setFrozenStatus("");
					}

					if (StringUtils.isEmpty(inputDetail.getOversize())) {
						shipmentDetailReference.setOversize("");
					} else if (!(inputDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
							|| inputDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
						shipmentDetailReference.setOversize("");
					}

					if (StringUtils.isEmpty(inputDetail.getDangerous())) {
						shipmentDetailReference.setDangerous("");
					} else if (!(inputDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)
							|| inputDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_YES))) {
						shipmentDetailReference.setDangerous("");
					}

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
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("F");

					shipmentDetail.setVgmChk(inputDetail.getVgmChk());
					shipmentDetail.setVgmMaxGross(inputDetail.getVgmMaxGross());
					shipmentDetail.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

					shipmentDetail.setOversizeBack(inputDetail.getOversizeBack());
					shipmentDetail.setOversizeFront(inputDetail.getOversizeFront());
					shipmentDetail.setOversizeLeft(inputDetail.getOversizeLeft());
					shipmentDetail.setOversizeRight(inputDetail.getOversizeRight());
					shipmentDetail.setOversizeTop(inputDetail.getOversizeTop());
					shipmentDetail.setOversizeType(inputDetail.getOversizeType());

					shipmentDetail.setDangerousImo(inputDetail.getDangerousImo());
					shipmentDetail.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
					shipmentDetail.setDangerousPacking(inputDetail.getDangerousPacking());
					shipmentDetail.setDangerousUnno(inputDetail.getDangerousUnno());

					shipmentDetail.setTemperature(inputDetail.getTemperature());
					shipmentDetail.setDaySetupTemperature(inputDetail.getDaySetupTemperature());

					shipmentDetail.setHumidity(inputDetail.getHumidity());
					shipmentDetail.setVentilation(inputDetail.getVentilation());
					
					// Lưu nếu là cont đặc biệt
					String sztp = inputDetail.getSztp().substring(2, 3);
					if (sztp.equals("R")) {
						// là cont lạnh
						shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
					}
//					if (sztp.equals("P")) {
//						// la cont qua kho
//						shipmentDetail.setOversize(EportConstants.CONT_SPECIAL_STATUS_INIT);
//					}

					if (attachBooking) {
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
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!EportConstants.SHIPMENT_STATUS_PROCESSING.equals(shipment.getStatus())) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			ProcessOrder processOrder = shipmentDetailService.makeOrderSendCont(shipmentDetails, shipment, taxCode,
					creditFlag);
			if (processOrder != null) {
				ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
				try {
					if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.SEND_CONT_FULL)) {
						ajaxResult = AjaxResult.warn("Yêu cầu đang được xử lý, xin vui lòng đợi trong giây lát.");
						ajaxResult.put("processId", processOrder.getId());
						return ajaxResult;
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}

				ajaxResult = AjaxResult.success("Yêu cầu đang được xử lý, xin vui lòng đợi trong giây lát.");
				ajaxResult.put("processId", processOrder.getId());
				return ajaxResult;
			}
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

	@GetMapping("/containerNo/{containerNo}/sztp")
	@ResponseBody
	public AjaxResult getSztpByContainerNo(@PathVariable("containerNo") String containerNo) {
		String sztp = catosApiService.getSztpByContainerNo(containerNo);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("sztp", sztp);
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
		shipmentComment.setServiceType(EportConstants.SERVICE_DROP_FULL);
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
		// List sztp can register on eport get from dictionary
		// All sztp that not in this list is invalid
		List<String> sztps = dictService.getListTag("sys_size_container_eport");
		for (int i = 0; i < shipmentDetails.size(); i++) {
			if (StringUtils.isEmpty(shipmentDetails.get(i).getContainerNo())) {
				return error("Hàng " + (i + 1) + ": Vui lòng nhập số container!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getConsignee())) {
				return error("Hàng " + (i + 1) + ": Vui lòng chọn chủ hàng từ danh sách!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getVslNm())) {
				return error("Hàng " + (i + 1) + ": Vui lòng chọn tàu - chuyến từ danh sách!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getSztp())) {
				return error("Hàng " + (i + 1) + ": Vui lòng chọn kích thước!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getCargoType())) {
				return error("Hàng " + (i + 1) + ": Vui lòng chọn loại hàng từ danh sách!");
			}
			if (shipmentDetails.get(i).getWgt() == null) {
				return error("Hàng " + (i + 1) + ": Vui lòng nhập trọng lượng (kg)!");
			}
			if (shipmentDetails.get(i).getWgt() < 1000) {
				return error("Hàng " + (i + 1) + ": Trọng lượng quá nhỏ (nhỏ hơn 1 tấn), vui lòng kiểm tra lại!");
			}
			if (shipmentDetails.get(i).getWgt() > 99999) {
				return error("Hàng " + (i + 1) + ": Trọng lượng quá lớn (hơn 100 tấn), vui lòng kiểm tra lại!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getDischargePort())) {
				return error("Hàng " + (i + 1) + ": Vui lòng chọn cảng dỡ hàng từ danh sách!");

			}
			if (!shipmentDetailReference.getConsignee().equals(shipmentDetails.get(i).getConsignee())) {
				return error("Tên chủ hàng không được khác nhau!");

			}
			// TODO Cần confirm lại với OM 1 số booking có làm được cho nhiều tàu-chuyến ko
			/*
			 * if
			 * (!shipmentDetailReference.getVslNm().equals(shipmentDetails.get(i).getVslNm()
			 * )) { return error("Tàu và Chuyến không được khác nhau!");
			 * 
			 * } if
			 * (!shipmentDetailReference.getVoyNo().equals(shipmentDetails.get(i).getVoyNo()
			 * )) { return error("Tàu và Chuyến không được khác nhau!");
			 * 
			 * }
			 */
			if (!shipmentDetailReference.getDischargePort().equals(shipmentDetails.get(i).getDischargePort())) {
				return error("Cảng dỡ hàng không được khác nhau!");
			}
			// Validate sztp
			if (!sztps.contains(shipmentDetails.get(i).getSztp())) {
				return error(
						"Kích thước " + shipmentDetails.get(i).getSztp() + " chưa được hỗ trợ làm lệnh trên ePort.");
			}
			// Validate dangrous cont
			containerNos += shipmentDetails.get(i).getContainerNo() + ",";
		}
		// Valide vslnm and voy no exist in catos
		// Get berth plan info
		BerthPlanInfo berthPlanInfoParam = new BerthPlanInfo();
		berthPlanInfoParam.setVslCd(shipmentDetailReference.getVslNm());
		berthPlanInfoParam.setCallSeq(shipmentDetailReference.getVoyNo());
		BerthPlanInfo berthPlanInfo = catosApiService.getBerthPlanInfo(berthPlanInfoParam);
		if (berthPlanInfo == null) {
			return error("Tàu chuyến không tồn tại trong hệ thống, vui lòng chọn tàu chuyến từ danh sách.");
		}

		// validate consignee exist in catos
		if (catosApiService.checkConsigneeExistInCatos(shipmentDetailReference.getConsignee()) == 0) {
			return error("Tên chủ hàng không đúng, vui lòng chọn chủ hàng từ danh sách.");
		}

		// // validate pod exist in catos
		// if
		// (catosApiService.checkPodExistIncatos(shipmentDetailReference.getDischargePort())
		// == 0) {
		// return error(String.format("Cảng dỡ '%s' không đúng, vui lòng chọn trong danh
		// sách gợi ý.", shipmentDetailReference.getDischargePort()));
		// }
		// validate POD in catos (POD: POD_NM)
		List<String> pods = catosApiService.getPODList(shipmentDetailReference);
		boolean podValidFlg = false;
		for (String pod : pods) {
			if (pod.startsWith(shipmentDetailReference.getDischargePort())) {
				podValidFlg = true;
				break;
			}
		}
		if (!podValidFlg) {
			return error(
					String.format("Cảng dỡ '%s' không đúng cho tàu chuyến '%s-%s', vui lòng chọn cảng từ danh sách.",
							shipmentDetailReference.getDischargePort(), shipmentDetailReference.getVslName(),
							shipmentDetailReference.getVoyCarrier()));
		}

		// Validate OPR in catos
		List<String> oprs = catosApiService.getOPRList(shipmentDetailReference);
		if (oprs != null && oprs.size() > 0 && !oprs.contains(shipmentDetailReference.getOpeCode())) {
			return error(
					String.format("OPR '%s' không đúng cho tàu chuyến:<br/>'%s-%s'. Vui lòng kiểm tra lại booking.",
							shipmentDetailReference.getOpeCode(), shipmentDetailReference.getVslName(),
							shipmentDetailReference.getVoyCarrier()));
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
				if (StringUtils.isNotEmpty(ctnrInfoE.getJobOdrNo())
						&& !EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(ctnrInfoE.getCntrState())
						&& !EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(ctnrInfoE.getCntrState())) {
					containerHasOrderdEmpty += shipmentDetail.getContainerNo() + ",";
				}
			}
		}

		String errorMsg = "";
		if (StringUtils.isNotEmpty(containerHasOrderdEmpty)) {
			errorMsg += "Các container: " + containerHasOrderdEmpty.substring(0, containerHasOrderdEmpty.length() - 1)
					+ " đã có lệnh hạ rỗng.<br>";
		}
		if (StringUtils.isNotEmpty(containerHasOrderdFull)) {
			errorMsg += "Các container: " + containerHasOrderdFull.substring(0, containerHasOrderdFull.length() - 1)
					+ " đã có lệnh hạ hàng.";
		}

		if (StringUtils.isNotEmpty(errorMsg)) {
			return error(errorMsg);
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

	@PostMapping("/request-special-cancel/shipment-detail")
	@ResponseBody
	public AjaxResult reqCancelRequestContSpecialContainer(String shipmentDetailIds, String contReqRemark) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {

			// Update status req cancel order
			for (ShipmentDetail shipmentDetail : shipmentDetails) {

				if (!StringUtils.isEmpty(shipmentDetail.getFrozenStatus())
						&& shipmentDetail.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
					shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT);
				}

				if (!StringUtils.isEmpty(shipmentDetail.getOversize())
						&& shipmentDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
					shipmentDetail.setOversize(EportConstants.CONT_SPECIAL_STATUS_INIT);
				}

				if (!StringUtils.isEmpty(shipmentDetail.getDangerous())
						&& shipmentDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_REQ)) {
					shipmentDetail.setDangerous(EportConstants.CONT_SPECIAL_STATUS_INIT);
				}

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
			String title = "ePort: Yêu cầu huỷ yêu cầu xác nhận container đặc biệt.";
			String msg = "Có yêu cầu huỷ yêu cầu xác nhận container hàng cho Booking: " + shipmentDetail.getBookingNo()
					+ ", Hãng tàu: " + shipmentDetail.getOpeCode() + ", Trucker: " + getGroup().getGroupName()
					+ ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg,
						configService.getKey("domain.admin.name") + EportConstants.URL_CANCEL_ORDER_SUPPORT,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request cancel order drop full: " + e);
			}
			return success("Đã yêu cầu hủy yêu cầu xác nhận, quý khách vui lòng đợi bộ phận thủ tục xử lý.");
		}

		return error(
				"Yêu cầu hủy yêu cầu xác nhận thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
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

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/{shipmentId}/shipment-detail/request-confirm")
	@ResponseBody
	public AjaxResult requestConfirmShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails,
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

			boolean attachBooking = false;
			// List opr need to attach booking (domestic container)
			List<String> oprList = dictService.getListTag("opr_list_booking_check");
			if (oprList.contains(shipment.getOpeCode())) {
				attachBooking = true;
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
					if ("N".equalsIgnoreCase(shipmentDetailReference.getUserVerifyStatus())) {
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

						shipmentDetailReference.setVgmChk(inputDetail.getVgmChk());
						shipmentDetailReference.setVgmMaxGross(inputDetail.getVgmMaxGross());
						shipmentDetailReference.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

						shipmentDetailReference.setOversizeBack(inputDetail.getOversizeBack());
						shipmentDetailReference.setOversizeFront(inputDetail.getOversizeFront());
						shipmentDetailReference.setOversizeLeft(inputDetail.getOversizeLeft());
						shipmentDetailReference.setOversizeRight(inputDetail.getOversizeRight());
						shipmentDetailReference.setOversizeTop(inputDetail.getOversizeTop());
						shipmentDetailReference.setOversizeType(inputDetail.getOversizeType());

						shipmentDetailReference.setDangerousImo(inputDetail.getDangerousImo());
						shipmentDetailReference.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
						shipmentDetailReference.setDangerousPacking(inputDetail.getDangerousPacking());
						shipmentDetailReference.setDangerousUnno(inputDetail.getDangerousUnno());

						shipmentDetailReference.setTemperature(inputDetail.getTemperature());
						shipmentDetailReference.setDaySetupTemperature(inputDetail.getDaySetupTemperature());

						String sztp = inputDetail.getSztp().substring(2, 3);

						if (!StringUtils.isEmpty(inputDetail.getDangerous())
								&& !inputDetail.getDangerous().equals(EportConstants.CONT_SPECIAL_STATUS_YES)) {
							shipmentDetailReference.setDangerous(EportConstants.CONTAINER_SUPPLY_STATUS_REQ);
						}

						if (!StringUtils.isEmpty(inputDetail.getOversize())
								&& !inputDetail.getOversize().equals(EportConstants.CONT_SPECIAL_STATUS_YES)) {
							shipmentDetailReference.setOversize(EportConstants.CONT_SPECIAL_STATUS_REQ);
						}

						if (sztp.equals("R") && (StringUtils.isEmpty(inputDetail.getFrozenStatus())
								|| (!StringUtils.isEmpty(inputDetail.getFrozenStatus()) && !inputDetail
										.getFrozenStatus().equals(EportConstants.CONT_SPECIAL_STATUS_YES)))) {
							shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_REQ);
						}

					}
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
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setFe("F");

					shipmentDetail.setVgmChk(inputDetail.getVgmChk());
					shipmentDetail.setVgmMaxGross(inputDetail.getVgmMaxGross());
					shipmentDetail.setVgmInspectionDepartment(inputDetail.getVgmInspectionDepartment());

					shipmentDetail.setOversizeBack(inputDetail.getOversizeBack());
					shipmentDetail.setOversizeFront(inputDetail.getOversizeFront());
					shipmentDetail.setOversizeLeft(inputDetail.getOversizeLeft());
					shipmentDetail.setOversizeRight(inputDetail.getOversizeRight());
					shipmentDetail.setOversizeTop(inputDetail.getOversizeTop());
					shipmentDetail.setOversizeType(inputDetail.getOversizeType());

					shipmentDetail.setDangerousImo(inputDetail.getDangerousImo());
					shipmentDetail.setDangerousNameProduct(inputDetail.getDangerousNameProduct());
					shipmentDetail.setDangerousPacking(inputDetail.getDangerousPacking());
					shipmentDetail.setDangerousUnno(inputDetail.getDangerousUnno());

					shipmentDetail.setTemperature(inputDetail.getTemperature());
					shipmentDetail.setDaySetupTemperature(inputDetail.getDaySetupTemperature());

					if (attachBooking) {
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
			return success("Yêu cầu xác nhận thành công");
		}
		return error("Yêu cầu xác nhận thất bại");
	}

	@PostMapping("/uploadFile")
	@ResponseBody
	public AjaxResult uploadFile(@RequestParam(value = "fileIds[]") String[] fileIds, String shipmentDetailId,
			Long shipmentId, String keyStatus) throws IOException, InvalidExtensionException {
		String KEY_STATUS_OVERSIZE = "oversize";
		String KEY_STATUS_DANGEROUS = "dangerous";
		String KEY_STATUS_ICE = "ice";

		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentId(shipmentId);
		shipmentImage.setShipmentDetailId(shipmentDetailId);

		// TODO: hỏi thống nhất status
		if (keyStatus.equals(KEY_STATUS_OVERSIZE)) {
			shipmentImage.setFileType("O");
		}
		if (keyStatus.equals(KEY_STATUS_DANGEROUS)) {
			shipmentImage.setFileType("D");
		}
		if (keyStatus.equals(KEY_STATUS_ICE)) {
			shipmentImage.setFileType("R");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("ids", fileIds);
		shipmentImage.setParams(map);
		shipmentImageService.updateShipmentImageByIds(shipmentImage);// them detail
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

	@PostMapping("/cont-special/file")
	@ResponseBody
	public AjaxResult saveContSpecialeFile(MultipartFile file) throws IOException, InvalidExtensionException {
		String basePath = String.format("%s/%s", Global.getUploadPath() + "/contSpecial", getUser().getGroupId());
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

		ajaxResult.put("file", filePath);
		return ajaxResult;
	}

	@DeleteMapping("/cont-special/file/{id}")
	@ResponseBody
	public AjaxResult deleteSendContFullFile(@PathVariable("id") Long id) throws IOException {
		if (id != 0) {
			ShipmentImage shipmentImageParam = new ShipmentImage();
			shipmentImageParam.setId(id);
			ShipmentImage shipmentImage = shipmentImageService.selectShipmentImageById(shipmentImageParam);
			String[] fileArr = shipmentImage.getPath().split("/");
			File file = new File(Global.getUploadPath() + "/contSpecial/" + getUser().getGroupId() + "/"
					+ fileArr[fileArr.length - 1]);
			if (file.delete()) {
				shipmentImageService.deleteShipmentImageById(id);
			}
			return success();
		}
		return error();
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

}