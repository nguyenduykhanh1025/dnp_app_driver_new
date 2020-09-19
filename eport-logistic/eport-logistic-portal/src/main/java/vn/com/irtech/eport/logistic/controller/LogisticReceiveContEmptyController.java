package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.exception.file.InvalidExtensionException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.file.FileUploadUtils;
import vn.com.irtech.eport.common.utils.file.MimeTypeUtils;
import vn.com.irtech.eport.framework.web.service.ConfigService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.form.ContainerServiceForm;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.listener.MqttService.NotificationCode;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

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
    private IDriverAccountService driverAccountService;
    
    @Autowired
    private IPickupAssignService pickupAssignService;
    
    @Autowired
    private IProcessBillService processBillService;
    
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private IShipmentCommentService shipmentCommentService;

    // VIEW RECEIVE CONT EMPTY
    @GetMapping()
    public String receiveContEmpty() {
        return PREFIX + "/index";
    }

    // VIEW RECEIVE CONT EMPTY ATTACH IMAGE
    @GetMapping("/shipments/{shipmentId}/shipment-images")
    public String receiveContEmptyAttachImage(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {
        Shipment shipment = shipmentService.selectShipmentById(shipmentId);
        if (!verifyPermission(shipment.getLogisticGroupId())) {
            return PREFIX + "/shipmentImage";
        }

        List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImagesByShipmentId(shipment.getId());
        if (!CollectionUtils.isEmpty(shipmentImages)) {
            shipmentImages.forEach(image -> image.setPath(serverConfig.getUrl() + image.getPath()));
            mmap.put("shipmentImages", shipmentImages);
        }

        return PREFIX + "/shipmentImage";
    }

	// COUNT SHIPMENT IMAGE BY SHIPMENT ID
	@GetMapping("/shipments/{shipmentId}/shipment-images/count")
	@ResponseBody
	public AjaxResult countShipmentImage(@PathVariable("shipmentId") Long shipmentId) {
		AjaxResult ajaxResult = AjaxResult.success();
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			int numberOfShipmentImage = shipmentImageService.countShipmentImagesByShipmentId(shipment.getId());
			ajaxResult.put("numberOfShipmentImage", numberOfShipmentImage);
		}
		return ajaxResult;
	}

	// FORM ADD NEW SHIPMENT
	@GetMapping("/shipment/add")
	public String add(ModelMap mmap) {
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			oprCodeList = catosApiService.getOprCodeList();
			oprCodeList.add(0, "Chọn OPR");
			CacheUtils.put("oprCodeList", oprCodeList);
		}
		
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
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			oprCodeList = catosApiService.getOprCodeList();
			oprCodeList.add(0, "Chọn OPR");
			CacheUtils.put("oprCodeList", oprCodeList);
		}
		
		mmap.put("oprCodeList", oprCodeList);
        return PREFIX + "/edit";
	}

	// FORM CONFIRM LIST CONT TO VERIFY OTP
	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("taxCode", getGroup().getMst());
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	// FORM TO VERIFY OTP
	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}/{taxCode}/{shipmentId}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("shipmentId") Long shipmentId,
			@PathVariable("creditFlag") boolean creditFlag, @PathVariable("taxCode") String taxCode, ModelMap mmap) {
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
			shipmentDetailIds.substring(0, shipmentDetailIds.length()-1);
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("processBills", processBillService.selectProcessBillListByProcessOrderIds(processOrderIds));
		return PREFIX + "/paymentForm";
	}
	
	@GetMapping("/req/supply/confirmation")
	public String getSupplyConfirmationForm() {
		return PREFIX + "/confirmRequestCont";
	}

	// CHECK BOOKING NO IS UNIQUE
	@PostMapping("/unique/booking-no")
	@ResponseBody
	public AjaxResult checkBookingNoUnique(@RequestBody ContainerServiceForm inputForm) {
		String bookingNo = inputForm.getBookingNo();
		if(StringUtils.isAllBlank(bookingNo)) {
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
    @Transactional
    public AjaxResult addShipment(Shipment shipment) {

        MultipartFile[] images = shipment.getImages();
        if (Objects.nonNull(images) && images.length > 5) {
            return error("Chỉ được phép đính kèm tối đa 5 hình ảnh!");
        }

        if (StringUtils.isNotEmpty(shipment.getBookingNo())) {
        	shipment.setBookingNo(shipment.getBookingNo());
        }
        LogisticAccount user = getUser();
        shipment.setLogisticAccountId(user.getId());
        shipment.setLogisticGroupId(user.getGroupId());
        shipment.setCreateTime(new Date());
        shipment.setCreateBy(user.getFullName());
        shipment.setServiceType(Constants.RECEIVE_CONT_EMPTY);
        shipment.setStatus("1");
        shipment.setContSupplyStatus(0);
        if (shipmentService.insertShipment(shipment) == 1) {
            try {
                insertShipmentImages(shipment);
                return success("Thêm lô thành công");
            } catch (IOException | InvalidExtensionException e) {
                return error(e.getMessage());
           }
        }
        return error("Thêm lô thất bại");
    }

    // EDIT SHIPMENT WITH SHIPMENT ID
	@Log(title = "Chỉnh Sửa Lô", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment/{shipmentId}")
    @ResponseBody
    public AjaxResult editShipment(Shipment shipment) {
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			// Check container amount update need to greater or equal  curren amount
			// Or at least greater or equal the number of container has been declared
			if(shipment.getContainerAmount() < referenceShipment.getContainerAmount()) {
				ShipmentDetail shipmentSearch = new ShipmentDetail();
				shipmentSearch.setShipmentId(shipment.getId());
				long contNumber = shipmentDetailService.countShipmentDetailList(shipmentSearch);
				if(contNumber > shipment.getContainerAmount()) {
					return error("Không thể chỉnh sửa số lượng container nhỏ hơn danh sách khai báo.");
				}
			}
			
			referenceShipment.setRemark(shipment.getRemark());
			referenceShipment.setContainerAmount(shipment.getContainerAmount());
			referenceShipment.setUpdateBy(getUser().getUserName());
			
			if (EportConstants.SHIPMENT_STATUS_INIT.equals(referenceShipment.getStatus())) {
				if (StringUtils.isNotEmpty(shipment.getBookingNo())) {
					referenceShipment.setBookingNo(shipment.getBookingNo().toUpperCase());
				}
				referenceShipment.setSpecificContFlg(shipment.getSpecificContFlg());
				referenceShipment.setOpeCode(shipment.getOpeCode());
			} else if (EportConstants.SHIPMENT_STATUS_SAVE.equals(referenceShipment.getStatus())) {
				referenceShipment.setOpeCode(shipment.getOpeCode());
			}
			
			if (shipmentService.updateShipment(referenceShipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
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
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailListForSendFReceiveE(shipmentDetail);
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
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			Shipment shipment = new Shipment();
			shipment.setId(shipmentDetails.get(0).getShipmentId());
			boolean updateShipment = true;
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (shipmentDetail.getId() != null) {
					updateShipment = false;
					shipmentDetail.setUpdateBy(user.getFullName());
					shipmentDetail.setUpdateTime(new Date());
					if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				} else {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setStatus(1);
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setContSupplyStatus("N");
					shipmentDetail.setFe("E");
					shipmentDetail.setFinishStatus("N");
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				}
			}
			if (updateShipment) {
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipment.setStatus("2");
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
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId, @PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			shipmentDetailService.deleteShipmentDetailByIds(shipmentId, shipmentDetailIds);
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setStatus("1");
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
	public AjaxResult verifyOtp(@PathVariable("otp") String otp, String shipmentDetailIds, String taxCode, boolean creditFlag) {
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
			//Đổi opeCode operateCode -> groupCode. VD Hang tau CMA: CMA,CNC,APL.. -> CMA
			CarrierGroup carrierGroup = carrierService.getCarrierGroupByOpeCode(shipmentDetails.get(0).getOpeCode().toUpperCase());
			// Chi convert thanh OPE cua hang tau CHA khi ton tai (neu khong ton tai -> skip)
			if(carrierGroup != null) {
				for(ShipmentDetail shpDtl : shipmentDetails) {
					shpDtl.setOpeCode(carrierGroup.getGroupCode());
				}
			}
			
			// Create list req for order receive cont empty
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContEmpty(shipmentDetails, shipment, taxCode, creditFlag);
			
			// Check and create list process order create booking from list req receive empty
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
			ajaxResult = AjaxResult.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
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

    private void insertShipmentImages(Shipment shipment) throws IOException, InvalidExtensionException {
        Long shipmentId = shipment.getId();
        String timeNow = DateUtils.dateTimeNow();
        String basePath = String.format("%s/%s/%s", Global.getUploadPath(), shipment.getLogisticGroupId(), shipmentId);
        int imageIndex = 0;

        for (MultipartFile image : shipment.getImages()) {
            String imageName = String.format("img%d_%s.%s", ++imageIndex, timeNow, FileUploadUtils.getExtension(image));
            String imagePath = FileUploadUtils.upload(basePath, imageName, image, MimeTypeUtils.IMAGE_EXTENSION);

            ShipmentImage shipmentImage = new ShipmentImage();
            shipmentImage.setShipmentId(shipmentId);
            shipmentImage.setPath(imagePath);
            shipmentImage.setCreateTime(DateUtils.getNowDate());
            shipmentImage.setCreateBy(getUser().getFullName());
            shipmentImageService.insertShipmentImage(shipmentImage);
        }
    }

	@GetMapping("/berthplan/ope-code/list")
	@ResponseBody
	public AjaxResult getOpeCodeList() {
		AjaxResult ajaxResult = success();
		List<String> opeCodeList = catosApiService.selectOpeCodeListInBerthPlan();
		if(opeCodeList.size() > 0 ) {
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
		if(berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for(ShipmentDetail i : berthplanList) {
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
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
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}
			
			
			// Set up data to send app notificaton 
			String title = "ePort: Yêu cầu cấp container rỗng.";
			String msg = "Có yêu cầu cấp rỗng cho Booking: " + shipment.getBookingNo() + ", Hãng tàu: " + shipment.getOpeCode() + ", Trucker: " + getGroup().getGroupName() + ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_CONT, title, msg, configService.getKey("domain.admin.name") + EportConstants.URL_CONT_SUPPLIER, EportConstants.NOTIFICATION_PRIORITY_LOW);
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
		if(berthplanList.size() > 0) {
			List<String> vesselAndVoyages = new ArrayList<String>();
			for(ShipmentDetail i : berthplanList) {
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
