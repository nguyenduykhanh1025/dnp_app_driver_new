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

import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.annotation.RepeatSubmit;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
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
@RequestMapping("/logistic/receive-cont-empty")
public class LogisticReceiveContEmptyController extends LogisticBaseController {

	private static final Logger logger = LoggerFactory.getLogger(LogisticReceiveContEmptyController.class);

	private final String PREFIX = "logistic/receiveContEmpty";

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentImageService shipmentImageService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IOtpCodeService otpCodeService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ICarrierGroupService carrierService;

	@Autowired
	private IProcessBillService processBillService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private DictService dictService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	// VIEW RECEIVE CONT EMPTY
	@GetMapping()
	public String receiveContEmpty(@RequestParam(required = false) Long sId, ModelMap mmap) {
		if (sId != null) {
			mmap.put("sId", sId);
		}
		mmap.put("domain", serverConfig.getUrl());
		return PREFIX + "/index";
	}

//    // VIEW RECEIVE CONT EMPTY ATTACH IMAGE
//    @GetMapping("/shipments/{shipmentId}/shipment-images")
//    public String receiveContEmptyAttachImage(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {
//        Shipment shipment = shipmentService.selectShipmentById(shipmentId);
//        if (!verifyPermission(shipment.getLogisticGroupId())) {
//            return PREFIX + "/shipmentImage";
//        }
//
//        List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImagesByShipmentId(shipment.getId());
//        if (!CollectionUtils.isEmpty(shipmentImages)) {
//            shipmentImages.forEach(image -> image.setPath(serverConfig.getUrl() + image.getPath()));
//            mmap.put("shipmentImages", shipmentImages);
//        }
//
//        return PREFIX + "/shipmentImage";
//    }

	@GetMapping("/shipments/{shipmentId}/shipment-images")
	@ResponseBody
	public AjaxResult getShipmentImages(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			ShipmentImage shipmentImage = new ShipmentImage();
			shipmentImage.setShipmentId(shipmentId);
			List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
			for (ShipmentImage shipmentImage2 : shipmentImages) {
				shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
			}
			ajaxResult.put("shipmentFiles", shipmentImages);
			return ajaxResult;
		}
		return error();
	}

	// FORM ADD NEW SHIPMENT
	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		List<String> oprCodeList = catosApiService.getOprCodeList();
		oprCodeList.add(0, "Chọn OPR");
		mmap.put("oprCodeList", oprCodeList);
		mmap.put("taxCode", getGroup().getMst());
		return PREFIX + "/add";
	}

	// FORM EDIT SHIPMENT WITH ID
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
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		mmap.put("shipmentFiles", shipmentImages);

		mmap.put("oprCodeList", oprCodeList);
		return PREFIX + "/edit";
	}

	// FORM CONFIRM LIST CONT TO VERIFY OTP
	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	// FORM TO VERIFY OTP
	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{shipmentId}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			@PathVariable("shipmentId") Long shipmentId, @PathVariable("creditFlag") boolean creditFlag,
			@PathVariable("taxCode") String taxCode, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getUser().getMobile());
		mmap.put("shipmentId", "-");
		mmap.put("creditFlag", creditFlag);
		mmap.put("taxCode", taxCode);
		mmap.put("shipmentId", shipmentId);
		return PREFIX + "/verifyOtp";
	}

	// FORM SHOW BILL TO PAY
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

	@GetMapping("/req/supply/confirmation")
	public String getSupplyConfirmationForm() {
		return PREFIX + "/confirmRequestCont";
	}

	@GetMapping("/req/cancel/confirmation")
	public String getCancelConfirmationForm() {
		return PREFIX + "/confirmRequestCancel";
	}

	// CHECK BOOKING NO IS UNIQUE
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
		shipment.setServiceType(Constants.RECEIVE_CONT_EMPTY);
//		if(catosApiService.checkBookingNoForSendFReceiveE(bookingNo , "E").intValue() == 0) {
//			return error("Booking No này chưa có trong hệ thống. Vui lòng liên hệ OM để tạo !");
//		}
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error("Số book đã tồn tại!");
	}

	// ADD SHIPMENT
	@Log(title = "Thêm Lô Bốc Rỗng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@ResponseBody
	public AjaxResult addShipment(@RequestBody Shipment shipment) {
		// validate shipment input
		if (StringUtils.isAllBlank(shipment.getBookingNo()) || StringUtils.isAllBlank(shipment.getOpeCode())
				|| shipment.getSpecificContFlg() == null || shipment.getContainerAmount() == null
				|| shipment.getContainerAmount() == 0) {
			return error("Hãy nhập các trường bắt buộc.");
		}
		LogisticAccount user = getUser();
		Shipment newShipment = new Shipment();
		newShipment.setLogisticAccountId(user.getId());
		newShipment.setLogisticGroupId(user.getGroupId());
		newShipment.setCreateBy(user.getFullName());
		newShipment.setServiceType(Constants.RECEIVE_CONT_EMPTY);
		newShipment.setStatus("1");
		newShipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_FINISH);
		// Update booking
		newShipment.setBookingNo(shipment.getBookingNo().trim().toUpperCase());
		newShipment.setOpeCode(shipment.getOpeCode());
		newShipment.setContainerAmount(shipment.getContainerAmount());
		newShipment.setSpecificContFlg(shipment.getSpecificContFlg());
		newShipment.setRemark(shipment.getRemark());

		String imageIds = shipment.getParams().get("ids").toString();
		if (StringUtils.isEmpty(imageIds)) {
			return error("Bạn chưa đính kèm tệp booking.");
		}

		// insert to DB
		if (shipmentService.insertShipment(newShipment) == 1) {
			ShipmentImage shipmentImage = new ShipmentImage();
			shipmentImage.setShipmentId(newShipment.getId());
			Map<String, Object> map = new HashMap<>();
			map.put("ids", Convert.toStrArray(imageIds));
			shipmentImage.setParams(map);
			shipmentImageService.updateShipmentImageByIds(shipmentImage);
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	// EDIT SHIPMENT WITH SHIPMENT ID
	@Log(title = "Chỉnh Sửa Lô", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment/{shipmentId}")
	@Transactional
	@ResponseBody
	public AjaxResult editShipment(@RequestBody Shipment shipment) {
		// validate shipment input
		if (StringUtils.isAllBlank(shipment.getBookingNo()) || StringUtils.isAllBlank(shipment.getOpeCode())
				|| shipment.getSpecificContFlg() == null || shipment.getContainerAmount() == null
				|| shipment.getContainerAmount() == 0) {
			return error("Hãy nhập các trường bắt buộc.");
		}
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		if (!verifyPermission(referenceShipment.getLogisticGroupId())) {
			return error("Không tim thấy lô");
		}
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

		referenceShipment.setId(shipment.getId());
		referenceShipment.setRemark(shipment.getRemark());
		referenceShipment.setContainerAmount(shipment.getContainerAmount());
		referenceShipment.setUpdateBy(getUser().getUserName());
		// flag to update details if change bookingNo or OPR
		boolean updateDetailFlg = !shipment.getOpeCode().trim().equalsIgnoreCase(referenceShipment.getOpeCode())
				|| !shipment.getBookingNo().trim().equalsIgnoreCase(referenceShipment.getBookingNo());
		if (EportConstants.SHIPMENT_STATUS_INIT.equals(referenceShipment.getStatus())) {
			referenceShipment.setBookingNo(shipment.getBookingNo().trim().toUpperCase());
			referenceShipment.setSpecificContFlg(shipment.getSpecificContFlg());
			referenceShipment.setOpeCode(shipment.getOpeCode());
		} else if (EportConstants.SHIPMENT_STATUS_SAVE.equals(referenceShipment.getStatus())) {
			referenceShipment.setOpeCode(shipment.getOpeCode());
			referenceShipment.setBookingNo(shipment.getBookingNo());
		}

		if (shipmentService.updateShipment(referenceShipment) == 1) {
			// update shipment details incase update bookingNo/opeCode
			if (updateDetailFlg) {
				ShipmentDetail searchDetail = new ShipmentDetail();
				searchDetail.setShipmentId(referenceShipment.getId());
				searchDetail.setLogisticGroupId(getUser().getGroupId());
				List<ShipmentDetail> details = shipmentDetailService.selectShipmentDetailList(searchDetail);
				for (ShipmentDetail detail : details) {
					detail.setBookingNo(shipment.getBookingNo().trim().toUpperCase());
					detail.setOpeCode(shipment.getOpeCode().trim().toUpperCase());
					shipmentDetailService.updateShipmentDetail(detail);
				}
			}
			ShipmentImage shipmentImage = new ShipmentImage();
			shipmentImage.setShipmentId(shipment.getId());
			Map<String, Object> map = new HashMap<>();
			map.put("ids", Convert.toStrArray(shipment.getParams().get("ids").toString()));
			shipmentImage.setParams(map);
			shipmentImageService.updateShipmentImageByIds(shipmentImage);
			return success("Chỉnh sửa lô thành công");
		}
		return error("Chỉnh sửa lô thất bại");
	}

	// GET SHIPMENT DETAIL FROM SHIPMENT ID
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

	// SAVE OR EDIT SHIPMENT DETAIL
	@Log(title = "Lưu Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			LogisticAccount user = getUser();
			ShipmentDetail firstDetail = shipmentDetails.get(0);
			Long shipmentId = firstDetail.getShipmentId();
			if (shipmentId == null) {
				return error("Không tìm thấy lô cần thêm chi tiết.");
			}
			Shipment shipment = shipmentService.selectShipmentById(shipmentId);
			if (shipment == null || !verifyPermission(shipment.getLogisticGroupId())) {
				return error("Không tìm thấy lô, vui lòng kiểm tra lại thông tin.");
			}

			// container no list string separated by comma
			String containerNos = "";
			for (ShipmentDetail inputDetail : shipmentDetails) {
				containerNos += inputDetail.getContainerNo() + ",";
			}
			if (StringUtils.isNotEmpty(containerNos)) {
				containerNos = containerNos.substring(0, containerNos.length() - 1);
			}

			// Get map container container ContainerInfoDto mapping with containerNo
			Map<String, ContainerInfoDto> containerMap = getContainerInfoFromCatos(containerNos);
			ContainerInfoDto ctnrInfo = null;

			boolean updateShipment = true; // if true => need to update status shipment from init to save
			for (ShipmentDetail inputDetail : shipmentDetails) {
				if (inputDetail.getId() != null) {
					updateShipment = false;
					// Case: update
					// Get record from catos
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());
					// Check permission
					if (!shipmentDetailReference.getLogisticGroupId().equals(user.getGroupId())) {
						return error("Không tìm thấy thông tin, vui lòng kiểm tra lại");
					}

					// Check status can update before user verify opt to make order
					if ("N".equalsIgnoreCase(shipmentDetailReference.getUserVerifyStatus())) {
						if (EportConstants.CONTAINER_SUPPLY_STATUS_HOLD
								.equals(shipmentDetailReference.getContSupplyStatus())) {
							shipmentDetailReference.setSztp(inputDetail.getSztp());
							shipmentDetailReference.setSztpDefine(inputDetail.getSztpDefine());
							shipmentDetailReference.setPlanningDate(inputDetail.getPlanningDate());
							shipmentDetailReference.setCargoType(inputDetail.getCargoType());
							shipmentDetailReference.setQualityRequirement(inputDetail.getQualityRequirement());
							// shipment carrier supply can update container no
							if (shipment.getSpecificContFlg() == 1) {
								shipmentDetailReference.setContainerNo(inputDetail.getContainerNo());
								ctnrInfo = containerMap.get(inputDetail.getContainerNo());
								if (ctnrInfo != null) {
									shipmentDetailReference
											.setLocation(ctnrInfo.getLocation() != null ? ctnrInfo.getLocation()
													: ctnrInfo.getArea());
								}
							}
						}
						shipmentDetailReference.setExpiredDem(inputDetail.getExpiredDem());
						shipmentDetailReference.setConsignee(inputDetail.getConsignee());
						shipmentDetailReference.setVslNm(inputDetail.getVslNm());
						shipmentDetailReference.setVoyNo(inputDetail.getVoyNo());
						shipmentDetailReference.setYear(inputDetail.getYear());
						shipmentDetailReference.setVslName(inputDetail.getVslName());
						shipmentDetailReference.setVoyCarrier(inputDetail.getVoyCarrier());
						shipmentDetailReference.setEta(inputDetail.getEta());
						shipmentDetailReference.setEtd(inputDetail.getEtd());
						shipmentDetailReference.setDischargePort(inputDetail.getDischargePort());
					}
					shipmentDetailReference.setRemark(inputDetail.getRemark());
					shipmentDetailReference.setUpdateBy(user.getFullName());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetailReference) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetailReference.getContainerNo());
					}
				} else {
					// New shipment detail
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setShipmentId(shipmentId);
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setStatus(1); // Status init
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setContSupplyStatus("N");
					shipmentDetail.setFe("E");
					shipmentDetail.setFinishStatus("N");
					shipmentDetail.setDoStatus("N");
					// shipment carrier supply can save container no
					if (shipment.getSpecificContFlg() == 1) {
						shipmentDetail.setContainerNo(inputDetail.getContainerNo());
						ctnrInfo = containerMap.get(inputDetail.getContainerNo());
						if (ctnrInfo != null) {
							shipmentDetail.setLocation(
									ctnrInfo.getLocation() != null ? ctnrInfo.getLocation() : ctnrInfo.getArea());
						}
					}
					shipmentDetail.setOpeCode(shipment.getOpeCode());
					shipmentDetail.setBookingNo(shipment.getBookingNo());
					shipmentDetail.setSztp(inputDetail.getSztp());
					shipmentDetail.setSztpDefine(inputDetail.getSztpDefine());
					shipmentDetail.setExpiredDem(inputDetail.getExpiredDem());
					shipmentDetail.setConsignee(inputDetail.getConsignee());
					shipmentDetail.setPlanningDate(inputDetail.getPlanningDate());
					shipmentDetail.setCargoType(inputDetail.getCargoType());
					shipmentDetail.setQualityRequirement(inputDetail.getQualityRequirement());
					shipmentDetail.setVslNm(inputDetail.getVslNm());
					shipmentDetail.setVoyNo(inputDetail.getVoyNo());
					shipmentDetail.setYear(inputDetail.getYear());
					shipmentDetail.setVslName(inputDetail.getVslName());
					shipmentDetail.setVoyCarrier(inputDetail.getVoyCarrier());
					shipmentDetail.setEta(inputDetail.getEta());
					shipmentDetail.setEtd(inputDetail.getEtd());
					shipmentDetail.setDischargePort(inputDetail.getDischargePort());
					shipmentDetail.setRemark(inputDetail.getRemark());
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

	// DELETE SHIPMENT DETAIL
	@Log(title = "Xóa Khai Báo Cont", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
	@DeleteMapping("/shipment/{shipmentId}/shipment-detail/{shipmentDetailIds}")
	@Transactional
	@ResponseBody
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId,
			@PathVariable("shipmentDetailIds") String shipmentDetailIds) {
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

			// Begin delete
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds, getUser().getGroupId());
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			// Set status for shipment when all container has been deleted
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
				shipment.setContSupplyStatus(0);
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	// VALIDATE OTP IS CORRECT THEN MAKE ORDER TO ROBOT
	@Log(title = "Xác Nhận OTP", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult verifyOtp(@PathVariable("otp") String otp, String shipmentDetailIds, String taxCode,
			boolean creditFlag) {
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		AjaxResult validateResult = validateShipmentDetailList(shipmentDetails);
		Integer code = (Integer) validateResult.get("code");
		if (code != 0) {
			return validateResult;
		}
		if (!CollectionUtils.isEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			// Neu khong phai status la "Dang lam lenh" thi update thanh dang lam lenh
			if (!EportConstants.SHIPMENT_STATUS_PROCESSING.equals(shipment.getStatus())) {
				shipment.setStatus(EportConstants.SHIPMENT_STATUS_PROCESSING);
				shipment.setUpdateTime(new Date());
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

			// Create list req for order receive cont empty
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService
					.makeOrderReceiveContEmpty(shipmentDetails, shipment, taxCode, creditFlag);

			// Check and create list process order create booking from list req receive
			// empty
			List<ProcessOrder> processOrders = shipmentDetailService.createBookingIfNeed(serviceRobotReqs);

			List<Long> processIds = new ArrayList<>();
			boolean robotBusy = false;
			// MAKE ORDER RECEIVE CONT EMPTY
			try {

				for (ProcessOrder processOrder : processOrders) {
					mqttService.publishBookingOrderToRobot(processOrder, EServiceRobot.CREATE_BOOKING);
				}

				for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
					processIds.add(serviceRobotReq.processOrder.getId());
					if (serviceRobotReq.processOrder.getRunnable()) {
						if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.RECEIVE_CONT_EMPTY)) {
							robotBusy = true;
						}
					}
				}
				if (robotBusy) {
					ajaxResult = AjaxResult.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
					ajaxResult.put("processIds", processIds);
					ajaxResult.put("orderNumber", serviceRobotReqs.size());
					return ajaxResult;
				}
			} catch (Exception e) {
				return error("Có lỗi xảy ra trong quá trình xác thực!");
			}
			ajaxResult = AjaxResult
					.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
			ajaxResult.put("processIds", processIds);
			ajaxResult.put("orderNumber", serviceRobotReqs.size());
			return ajaxResult;
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	// PAYMENT AFTER SHOW BILL
	@Log(title = "Thanh Toán Bốc Rỗng Napas", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/payment")
	@ResponseBody
	public AjaxResult payment(String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (!CollectionUtils.isEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(4);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

//    private void insertShipmentImages(Shipment shipment) throws IOException, InvalidExtensionException {
//        Long shipmentId = shipment.getId();
//        String timeNow = DateUtils.dateTimeNow();
//        String basePath = String.format("%s/%s/%s", Global.getUploadPath(), shipment.getLogisticGroupId(), shipmentId);
//        int imageIndex = 0;
//
//        for (MultipartFile image : shipment.getImages()) {
//            String imageName = String.format("img%d_%s.%s", ++imageIndex, timeNow, FileUploadUtils.getExtension(image));
//            String imagePath = FileUploadUtils.upload(basePath, imageName, image, MimeTypeUtils.IMAGE_EXTENSION);
//
//            ShipmentImage shipmentImage = new ShipmentImage();
//            shipmentImage.setShipmentId(shipmentId);
//            shipmentImage.setPath(imagePath);
//            shipmentImage.setCreateTime(DateUtils.getNowDate());
//            shipmentImage.setCreateBy(getUser().getFullName());
//            shipmentImageService.insertShipmentImage(shipmentImage);
//        }
//    }

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

	@PostMapping("/cont-req/shipment-detail")
	@ResponseBody
	public AjaxResult reqSupplyContainer(String shipmentDetailIds, String contReqRemark) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			shipment.setContSupplyStatus(EportConstants.SHIPMENT_SUPPLY_STATUS_WAITING);
			shipmentService.updateShipment(shipment);
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setContSupplyStatus(EportConstants.CONTAINER_SUPPLY_STATUS_REQ);
				shipmentDetail.setStatus(1);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}

			// Write comment for topic supply container
			if (StringUtils.isNotEmpty(contReqRemark)) {
				ShipmentDetail shipmentDetail = shipmentDetails.get(0);
				ShipmentComment shipmentComment = new ShipmentComment();
				shipmentComment.setLogisticGroupId(getUser().getGroupId());
				shipmentComment.setShipmentId(shipmentDetail.getShipmentId());
				shipmentComment.setUserId(getUserId());
				shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
				shipmentComment.setUserAlias(getGroup().getGroupName());
				shipmentComment.setCommentTime(new Date());
				shipmentComment.setContent(contReqRemark);
				shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CONT_SUPPLIER);
				shipmentComment.setServiceType(shipmentDetail.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}

			// Set up data to send app notificaton
			String title = "ePort: Yêu cầu cấp container rỗng.";
			String msg = "Có yêu cầu cấp rỗng cho Booking: " + shipment.getBookingNo() + ", Hãng tàu: "
					+ shipment.getOpeCode() + ", Trucker: " + getGroup().getGroupName() + ", Chủ hàng: "
					+ shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_CONT, title, msg,
						configService.getKey("domain.admin.name") + EportConstants.URL_CONT_SUPPLIER,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request container supply: " + e);
			}
			return success("Đã yêu cầu cấp container, quý khách vui lòng đợi bộ phận cấp container xử lý.");
		}

		return error("Yêu cầu cấp rỗng thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
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
		shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
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
				return error("Hàng " + (i + 1) + ": Quý khách chưa nhập số container!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getSztp())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn kích thước!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getConsignee())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn chủ hàng!");
			}
			if (shipmentDetailReference.getPlanningDate() == null) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa nhập ngày dự kiến bốc!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getCargoType())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn loại hàng!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getVslNm())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn tàu!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getVoyNo())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn chuyến!");
			}

			if (StringUtils.isEmpty(shipmentDetails.get(i).getDischargePort())) {
				return error("Hàng " + (i + 1) + ": Quý khách chưa chọn cảng dỡ hàng!");
			}
			if (!shipmentDetailReference.getConsignee().equals(shipmentDetails.get(i).getConsignee())) {
				return error("Tên chủ hàng không được khác nhau!");

			}
			if (!shipmentDetailReference.getVslNm().equals(shipmentDetails.get(i).getVslNm())) {
				return error("Tàu và Chuyến không được khác nhau!");

			}
			if (!shipmentDetailReference.getVoyNo().equals(shipmentDetails.get(i).getVoyNo())) {
				return error("Tàu và Chuyến không được khác nhau!");

			}
			if (!shipmentDetailReference.getDischargePort().equals(shipmentDetails.get(i).getDischargePort())) {
				return error("Cảng dỡ hàng không được khác nhau!");
			}
			// Validate sztp
			if (!sztps.contains(shipmentDetails.get(i).getSztp())) {
				return error(
						"Kích thước " + shipmentDetails.get(i).getSztp() + " không được phép làm lệnh trên eport.");
			}
			containerNos += shipmentDetails.get(i).getContainerNo() + ",";
		}

		// Valide vslnm and voy no exist in catos
		// Get berth plan info
		BerthPlanInfo berthPlanInfoParam = new BerthPlanInfo();
		berthPlanInfoParam.setVslCd(shipmentDetailReference.getVslNm());
		berthPlanInfoParam.setCallSeq(shipmentDetailReference.getVoyNo());
		BerthPlanInfo berthPlanInfo = catosApiService.getBerthPlanInfo(berthPlanInfoParam);
		if (berthPlanInfo == null) {
			return error("Tàu chuyến không tồn tại trong hệ thống, quý khách vui lòng chọn tàu chuyến từ danh sách.");
		}

		// validate consignee exist in catos
		if (catosApiService.checkConsigneeExistInCatos(shipmentDetailReference.getConsignee()) == 0) {
			return error(
					"Tên chủ hàng quý khách nhập không đúng, vui lòng chọn tên chủ hàng từ trong danh sách của hệ thống gợi ý.");
		}

		// validate pod exist in catos
		if (catosApiService.checkPodExistIncatos(shipmentDetailReference.getDischargePort()) == 0) {
			return error(
					"Cảng dỡ hàng quý khách nhập không đúng, vui lòng chọn cảng từ trong dánh sách của hệ thống gợi ý.");
		}

		// Validate container has job order no
		containerNos = containerNos.substring(0, containerNos.length() - 1);
		Map<String, ContainerInfoDto> ctnrMap = getContainerInfoFromCatos(containerNos);
		String containerHasOrderdEmpty = "";
		String containerHasOrderdFull = "";
		ContainerInfoDto ctnrInfo = null;
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			// Get ctnr info by contaienr no in catos
			// return error if null
			ctnrInfo = ctnrMap.get(shipmentDetail.getContainerNo());
			if (ctnrInfo == null) {
				return error("Không tìm thấy thông tin container " + shipmentDetail.getContainerNo()
						+ " trong hệ thống của cảng.");
			}
			// Container has job order no 2 => has order
			if (StringUtils.isNotEmpty(ctnrInfo.getJobOdrNo2())) {
				if ("F".equalsIgnoreCase(ctnrInfo.getFe())) {
					// Pickup full
					containerHasOrderdFull += shipmentDetail.getContainerNo() + ",";
				} else {
					// Pickup empty
					containerHasOrderdEmpty += shipmentDetail.getContainerNo() + ",";
				}
			}
		}

		String errorMsg = "";
		if (StringUtils.isNotEmpty(containerHasOrderdEmpty)) {
			errorMsg += "Các container " + containerHasOrderdEmpty.substring(0, containerHasOrderdEmpty.length() - 1)
					+ " đã có lệnh bốc rỗng.<br>";
		}
		if (StringUtils.isNotEmpty(containerHasOrderdFull)) {
			errorMsg += "Các container " + containerHasOrderdFull.substring(0, containerHasOrderdFull.length() - 1)
					+ " đã có lệnh bốc hàng.";
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
				shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CANCEL_PICKUP_EMPTY);
				shipmentComment.setServiceType(shipmentDetail.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}

			// Set up data to send app notificaton
			String title = "ePort: Yêu cầu huỷ lệnh nhận container rỗng.";
			String msg = "Có yêu cầu huỷ lệnh nhận container rỗng cho Booking: " + shipmentDetail.getBookingNo()
					+ ", Hãng tàu: " + shipmentDetail.getOpeCode() + ", Trucker: " + getGroup().getGroupName()
					+ ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg,
						configService.getKey("domain.admin.name") + EportConstants.URL_CANCEL_ORDER_SUPPORT,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request cancel order pickup empty: " + e);
			}
			return success("Đã yêu cầu hủy lệnh, quý khách vui lòng đợi bộ phận thủ tục xử lý.");
		}

		return error("Yêu cầu hủy lệnh thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
	}

	@GetMapping("/containerNo/{containerNo}/sztp")
	@ResponseBody
	public AjaxResult getSztpByContainerNo(@PathVariable("containerNo") String containerNo) {
		String sztp = catosApiService.getSztpByContainerNo(containerNo);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("sztp", sztp);
		return ajaxResult;
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
				if ("E".equals(containerInfoDto.getFe())) {
					containerInfoMap.put(containerInfoDto.getCntrNo(), containerInfoDto);
				}
			}
		}
		return containerInfoMap;
	}
}
