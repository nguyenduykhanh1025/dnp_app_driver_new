package vn.com.irtech.eport.logistic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
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
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.file.FileUploadUtils;
import vn.com.irtech.eport.common.utils.file.MimeTypeUtils;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.listener.MqttService;
import vn.com.irtech.eport.logistic.listener.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/receive-cont-empty")
public class LogisticReceiveContEmptyController extends LogisticBaseController {

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
		mmap.put("taxCode", getGroup().getMst());
		return PREFIX + "/add";
	}

	// FORM EDIT SHIPMENT WITH ID
	@GetMapping("/shipment/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
			mmap.put("taxCode", getGroup().getMst());
		}
        return PREFIX + "/edit";
	}

	// FORM CONFIRM LIST CONT TO VERIFY OTP
	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds, getUser().getGroupId());
		mmap.put("creditFlag", getGroup().getCreditFlag());
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return PREFIX + "/checkContListBeforeVerify";
	}

	// FORM TO VERIFY OTP
	@GetMapping("/otp/verification/{shipmentDetailIds}/{creditFlag}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, @PathVariable("creditFlag") boolean creditFlag, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		mmap.put("creditFlag", creditFlag);
		return PREFIX + "/verifyOtp";
	}

	// FORM SHOW BILL TO PAY
	@GetMapping("paymentForm/{shipmentDetailIds}")
	public String paymentForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return PREFIX + "/paymentForm";
	}

	// CHECK BOOKING NO IS UNIQUE
	@GetMapping("/unique/booking-no/{bookingNo}")
	@ResponseBody
	public AjaxResult checkBookingNoUnique(@PathVariable String bookingNo) {
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
        //check MST
        if (shipment.getTaxCode() != null) {
            String groupName = catosApiService.getGroupNameByTaxCode(shipment.getTaxCode()).getGroupName();
            if (groupName == null) {
                return error("Mã số thuế không tồn tại");
            }
        }

        MultipartFile[] images = shipment.getImages();
        if (Objects.nonNull(images) && images.length > 5) {
            return error("Chỉ được phép đính kèm tối đa 5 hình ảnh!");
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
    			// assign driver default
    			PickupAssign pickupAssign = new PickupAssign();
    			pickupAssign.setLogisticGroupId(getUser().getGroupId());
    			pickupAssign.setShipmentId(shipment.getId());
    			//list driver
    			DriverAccount driverAccount = new DriverAccount();
    			driverAccount.setLogisticGroupId(getUser().getGroupId());
    			driverAccount.setDelFlag(false);
    			driverAccount.setStatus("0");
    			List<DriverAccount> driverAccounts = driverAccountService.selectDriverAccountList(driverAccount);
    			if(driverAccounts.size() > 0) {
    				for(DriverAccount i : driverAccounts) {
    					pickupAssign.setDriverId(i.getId());
    					pickupAssign.setFullName(i.getFullName());
    					pickupAssign.setPhoneNumber(i.getMobileNumber());
    					pickupAssign.setCreateBy(getUser().getFullName());
    					pickupAssignService.insertPickupAssign(pickupAssign);
    				}
    			}
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
		//check MST
		if(shipment.getTaxCode() != null){
			String groupName = catosApiService.getGroupNameByTaxCode(shipment.getTaxCode()).getGroupName();
			if(groupName == null){
				return error("Mã số thuế không tồn tại");
			}
		}
		LogisticAccount user = getUser();
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			shipment.setUpdateTime(new Date());
			shipment.setUpdateBy(user.getFullName());
			if (shipmentService.updateShipment(shipment) == 1) {
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
			shipmentDetailService.deleteShipmentDetailByIds(shipmentDetailIds);
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
	@PostMapping("/otp/{otp}/verification/shipment-detail/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable("otp") String otp, @PathVariable("shipmentDetailIds") String shipmentDetailIds, boolean creditFlag) {
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
			// Ne khong phai status la "Dang lam lenh" thi update thanh dang lam lenh
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
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContEmpty(shipmentDetails, shipment, creditFlag);
			
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
				shipmentDetail.setStatus(3);
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
}
