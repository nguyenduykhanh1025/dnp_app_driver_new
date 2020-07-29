package vn.com.irtech.eport.logistic.controller;

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
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.file.FileUploadUtils;
import vn.com.irtech.eport.framework.web.service.MqttService;
import vn.com.irtech.eport.framework.web.service.MqttService.EServiceRobot;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
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
		if (shipmentService.checkBillBookingNoUnique(shipment) == 0) {
			return success();
		}
		return error();
	}

    // ADD SHIPMENT
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
                return success("Thêm lô thành công");
            } catch (IOException e) {
                return error(e.getMessage());
            }
        }
        return error("Thêm lô thất bại");
    }

    // EDIT SHIPMENT WITH SHIPMENT ID
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
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (shipmentDetails != null) {
				ajaxResult.put("shipmentDetails", shipmentDetails);
			} else {
				ajaxResult = AjaxResult.error();
			}
		}
		return ajaxResult;
	}

	// SAVE OR EDIT SHIPMENT DETAIL
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
	@PostMapping("/otp/{otp}/verification/shipment-detail/{shipmentDetailIds}")
	@ResponseBody
	public AjaxResult verifyOtp(@PathVariable("otp") String otp, @PathVariable("shipmentDetailIds") String shipmentDetailIds, boolean creditFlag) {
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			if (!"3".equals(shipment.getStatus())) {
				shipment.setStatus("3");
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService.makeOrderReceiveContEmpty(shipmentDetails, shipment, creditFlag);
			if (serviceRobotReqs != null) {
				List<Long> processIds = new ArrayList<>();
				boolean robotBusy = false;
				// MAKE ORDER RECEIVE CONT EMPTY
				try {
					for (ServiceSendFullRobotReq serviceRobotReq : serviceRobotReqs) {
						processIds.add(serviceRobotReq.processOrder.getId());
						if (!mqttService.publishMessageToRobot(serviceRobotReq, EServiceRobot.RECEIVE_CONT_EMPTY)) {
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

	// PAYMENT AFTER SHOW BILL
	@PostMapping("/payment")
	@ResponseBody
	public AjaxResult payment(String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(3);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

    private void insertShipmentImages(Shipment shipment) throws IOException {
        Long shipmentId = shipment.getId();
        String timeNow = DateUtils.dateTimeNow();
        String basePath = String.format("%s/%s/%s", Global.getUploadPath(), shipment.getLogisticGroupId(), shipmentId);
        int imageIndex = 0;

        for (MultipartFile image : shipment.getImages()) {
            String imageName = String.format("img%d_%s.%s", ++imageIndex, timeNow, FileUploadUtils.getExtension(image));
            String imagePath = FileUploadUtils.upload(basePath, imageName, image);

            ShipmentImage shipmentImage = new ShipmentImage();
            shipmentImage.setShipmentId(shipmentId);
            shipmentImage.setPath(imagePath);
            shipmentImage.setCreateTime(DateUtils.getNowDate());
            shipmentImage.setCreateBy(getUser().getFullName());
            shipmentImageService.insertShipmentImage(shipmentImage);
        }
    }
}
