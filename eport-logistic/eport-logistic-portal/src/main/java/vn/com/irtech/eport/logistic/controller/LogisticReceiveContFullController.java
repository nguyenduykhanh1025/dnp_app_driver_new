package vn.com.irtech.eport.logistic.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
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
import vn.com.irtech.eport.framework.custom.queue.listener.CustomQueueService;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.OtpCode;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ReeferInfo;
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
import vn.com.irtech.eport.logistic.service.IOtpCodeService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IReeferInfoService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.logistic.service.impl.ReeferInfoServiceImpl;
import vn.com.irtech.eport.system.domain.ShipmentDetailHist;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.IShipmentDetailHistService;
import vn.com.irtech.eport.system.service.ISysConfigService;

@Controller
@RequiresPermissions("logistic:order")
@RequestMapping("/logistic/receive-cont-full")
public class LogisticReceiveContFullController extends LogisticBaseController {

	private final String PREFIX = "logistic/receiveContFull";

	private static final Logger logger = LoggerFactory.getLogger(LogisticReceiveContFullController.class);

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IShipmentImageService shipmentImageService;

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
	private IEdoHouseBillService edoHouseBillService;

	@Autowired
	private IShipmentCommentService shipmentCommentService;

	@Autowired
	private DictService dictService;

	@Autowired
	private ServerConfig serverConfig;

	@Autowired
	private IShipmentDetailHistService shipmentDetailHistService;

	@Autowired
	private IReeferInfoService reeferInfoService;

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
		mmap.put("domain", serverConfig.getUrl());
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
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			mmap.put("shipmentId", shipmentDetails.get(0).getShipmentId());
			mmap.put("contList", shipmentDetails);
		}
		return PREFIX + "/checkCustomStatus";
	}

	@GetMapping("/otp/cont-list/confirmation/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
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
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds,
			@PathVariable("shipmentId") Long shipmentId, @PathVariable("creditFlag") boolean creditFlag,
			@PathVariable("isSendContEmpty") boolean isSendContEmpty, @PathVariable("taxCode") String taxCode,
			ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
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
			shipmentDetailIds.substring(0, shipmentDetailIds.length() - 1);
		}
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("processBills", processBillService.selectProcessBillListByProcessOrderIds(processOrderIds));
		return PREFIX + "/paymentForm";
	}

	@GetMapping("/paymentPowerDropForm/{reeferInfoId}")
	public String paymentPowerDropForm(@PathVariable("reeferInfoId") Long reeferInfoId, ModelMap mmap) {

		ReeferInfo reeferInfo = this.reeferInfoService.selectReeferInfoById(reeferInfoId);
		ShipmentDetail shipmentDetailFromDB = this.shipmentDetailService
				.selectShipmentDetailByDetailId(reeferInfo.getShipmentDetailId().toString());

		ProcessBill processBill = new ProcessBill();
		processBill.setShipmentId(shipmentDetailFromDB.getShipmentId());
		processBill.setLogisticGroupId(shipmentDetailFromDB.getLogisticGroupId());
		processBill.setProcessOrderId(shipmentDetailFromDB.getProcessOrderId());
		processBill.setServiceType(shipmentDetailFromDB.getServiceType());
		processBill.setSztp(shipmentDetailFromDB.getSztp());
		processBill.setContainerNo(shipmentDetailFromDB.getContainerNo());
		processBill.setBlNo(shipmentDetailFromDB.getBlNo());
		processBill.setBookingNo(shipmentDetailFromDB.getBookingNo());
		processBill.setPayType(reeferInfo.getPayerType());
		processBill.setPaymentStatus("N");
		processBill.setShipmentDetailId(shipmentDetailFromDB.getId());

		Long diff = (reeferInfo.getDateGetPower().getTime() - reeferInfo.getDateSetPower().getTime())
				/ (1000 * 60 * 60);
		Long billPowerNumber = 0L;
		List<SysDictData> billPowers = dictService.getType("bill_power");
		for (SysDictData billPower : billPowers) {
			if (billPower.getDictLabel().equals(shipmentDetailFromDB.getSztp().substring(0, 2))) {
				billPowerNumber = Long.parseLong(billPower.getDictValue());
				break;
			}
		}
		processBill.setVatAfterFee(billPowerNumber * diff);
		processBill.setExchangeFee(0L);

		List<ProcessBill> processBillsResult = new ArrayList<>();
		processBillsResult.add(processBill);
		mmap.put("processBills", processBillsResult);
		mmap.put("shipmentDetailIds", shipmentDetailFromDB.getId());

		return PREFIX + "/paymentPowerDropForm";
	}

	@GetMapping("/req/cancel/confirmation")
	public String getCancelConfirmationForm() {
		return PREFIX + "/confirmRequestCancel";
	}

	@Log(title = "Thêm Lô Bốc Hàng", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment")
	@Transactional
	@ResponseBody
	public AjaxResult addShipment(Shipment shipmentInput) {
		Shipment shipment = new Shipment();
		// Check case house bill
		if (StringUtils.isNotEmpty(shipmentInput.getHouseBill())) {
			if (edoHouseBillService.getContainerAmountWithOrderNumber(shipmentInput.getHouseBill(),
					shipmentInput.getOrderNumber()) != 0) {
				// Shipment is house bill case => edo
				shipment.setEdoFlg(EportConstants.DO_TYPE_CARRIER_EDO);
			}
		} else if (edoService.getContainerAmountWithOrderNumber(shipmentInput.getBlNo(),
				shipmentInput.getOrderNumber()) != 0) {
			// Case edo with master bill
			shipment.setEdoFlg(EportConstants.DO_TYPE_CARRIER_EDO);
		} else {
			// If shipmen is not house bill or master bill with valid order number => DO
			// type
			shipment.setEdoFlg(EportConstants.DO_TYPE_CARRIER_DO);
		}

		LogisticAccount user = getUser();
		shipment.setEdoFlg(shipmentInput.getEdoFlg());
		shipment.setOpeCode(shipmentInput.getOpeCode());
		shipment.setOrderNumber(shipmentInput.getOrderNumber());
		shipment.setBlNo(shipmentInput.getBlNo());
		shipment.setHouseBill(shipmentInput.getHouseBill());
		shipment.setContainerAmount(shipmentInput.getContainerAmount());
		shipment.setRemark(shipmentInput.getRemark());
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
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
		// check if current user own shipment
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			// Chi update cac item cho phep
			referenceShipment.setUpdateBy(user.getFullName());
			referenceShipment.setRemark(shipment.getRemark());

			// Can change bill of lading when status = initialize => another item change
			// accordingly
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

	@Log(title = "Khai Báo Cont", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail")
	@Transactional
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			String dnPortName = configService.selectConfigByKey("danang.port.name");
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
			Shipment shipmentSendEmpty = new Shipment();
			boolean needCreateSendE = false;
			boolean updateShipment = true;
			// FIXME sai item, tao them item de luu gia tri
			boolean isSendEmpty = firstDetail.getVgmChk();
			// Bốc hàng chọn kết hợp trả rỗng
			if (dnPortName.equals(firstDetail.getEmptyDepot()) && isSendEmpty) {
				shipmentSendEmpty.setBlNo(firstDetail.getBlNo());
				shipmentSendEmpty.setServiceType(Constants.SEND_CONT_EMPTY);
				List<Shipment> shipments = shipmentService.selectShipmentList(shipmentSendEmpty);
				// create if not exist // if exist then skip
				if (CollectionUtils.isEmpty(shipments)) {
					// create send empty shipment
					shipmentSendEmpty.setContainerAmount(Long.valueOf(firstDetail.getTier()));
					shipmentSendEmpty.setLogisticAccountId(user.getId());
					// Convert ope code child to parent (example: WHA -> WHL)
					String oprParent = dictService.getLabel("carrier_parent_child_list", shipment.getOpeCode());
					if (StringUtils.isNotEmpty(oprParent)) {
						shipmentSendEmpty.setOpeCode(oprParent);
					} else {
						shipmentSendEmpty.setOpeCode(shipment.getOpeCode());
					}
					shipmentSendEmpty.setLogisticGroupId(user.getGroupId());
					shipmentSendEmpty.setCreateTime(new Date());
					shipmentSendEmpty.setStatus(EportConstants.SHIPMENT_STATUS_INIT);
					// insert to db
					shipmentService.insertShipment(shipmentSendEmpty);
					needCreateSendE = true;
				}
			}
			// eDO Map to replace when save info
			Map<String, ShipmentDetail> edoDetailMap = new HashMap<>();
			if ("1".equals(shipment.getEdoFlg())) {
				// get infor from edo
				List<ShipmentDetail> edoDetails = null;
				if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
					edoDetails = shipmentDetailService.getShipmentDetailFromHouseBill(shipment.getHouseBill());
				} else {
					// get infor from edi
					edoDetails = shipmentDetailService.getShipmentDetailsFromEDIByBlNo(shipment.getBlNo());
				}
				for (ShipmentDetail sd : edoDetails) {
					edoDetailMap.put(sd.getContainerNo(), sd);
				}
			}
			// lay consignee taxcode tu catos
			String taxCode = catosApiService.getTaxCodeBySnmGroupName(firstDetail.getConsignee());
			if (StringUtils.isBlank(taxCode)) {
				return error(String.format("Không tìm thấy chủ hàng '%s', <br/>Vui lòng chọn chủ hàng từ danh sách.",
						firstDetail.getConsignee()));
			}
			// ShipmentDetail catosSearch = new ShipmentDetail();
			// catosSearch.setBlNo(shipment.getBlNo());
			// create to search infor from catos

			// Get container list for BL from catos
			Map<String, ContainerInfoDto> catosMap = getCatosShipmentDetail(shipment.getBlNo());
			ContainerInfoDto ctnrInfo = null;
			ShipmentDetail shipmentDetail = null;
			for (ShipmentDetail inputDetail : shipmentDetails) {
				shipmentDetail = new ShipmentDetail();
				// search catos infor for specified container and replace infor
				ctnrInfo = catosMap.get(inputDetail.getContainerNo());
				// New record
				if (inputDetail.getId() == null) {
					// Setting from input screen
					shipmentDetail.setContainerNo(inputDetail.getContainerNo());
					shipmentDetail.setConsignee(inputDetail.getConsignee());
					shipmentDetail.setEmptyDepot(inputDetail.getEmptyDepot());
					shipmentDetail.setExpiredDem(inputDetail.getExpiredDem());
					shipmentDetail.setDetFreeTime(inputDetail.getDetFreeTime());
					// default value
					shipmentDetail.setShipmentId(shipmentId);
					shipmentDetail.setBlNo(shipment.getBlNo());
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setFe("F");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setUserVerifyStatus("N");
					shipmentDetail.setProcessStatus("N");
					if (EportConstants.DO_TYPE_CARRIER_DO.equals(shipment.getEdoFlg())) {
						shipmentDetail.setDoStatus("N");
					}
					shipmentDetail.setPreorderPickup("N");
					shipmentDetail.setFinishStatus("N");

					if (ctnrInfo != null) {
						shipmentDetail.setSztp(ctnrInfo.getSztp());

						// nhatlv add status ban đầu

						if ("R".equalsIgnoreCase(ctnrInfo.getSztp().substring(2, 3))) { // cont lạnh
							shipmentDetail.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT); // I
						}
						// set dangerous info
						shipmentDetail.setDangerousImo(ctnrInfo.getImdg());
						shipmentDetail.setDangerousUnno(ctnrInfo.getUnno());
						// Check is dangerous
						// k can check dangerous
						/*
						 * if (StringUtils.isNotEmpty(shipmentDetail.getDangerousImo()) ||
						 * StringUtils.isNotEmpty(shipmentDetail.getDangerousUnno())) {
						 * shipmentDetail.setDangerous(EportConstants.CONT_SPECIAL_STATUS_INIT);// I }
						 */
						// end
						// Set oversize info
						shipmentDetail.setOversizeTop(ctnrInfo.getOvHeight());
						shipmentDetail.setOversizeFront(ctnrInfo.getOvFore());
						shipmentDetail.setOversizeBack(ctnrInfo.getOvAft());
						// frozen_status
						shipmentDetail.setOversizeLeft(ctnrInfo.getOvPort());
						shipmentDetail.setOversizeRight(ctnrInfo.getOvStbd());

						// Check is oversize
						if (StringUtils.isNotEmpty(shipmentDetail.getOvHeight())
								|| StringUtils.isNotEmpty(shipmentDetail.getOvFore())
								|| StringUtils.isNotEmpty(shipmentDetail.getOvAft())
								|| StringUtils.isNotEmpty(shipmentDetail.getOvPort())
								|| StringUtils.isNotEmpty(shipmentDetail.getOvStbd())) {
							shipmentDetail.setOversize(EportConstants.CONT_SPECIAL_STATUS_INIT);// I
						}

						// shipmentDetail.setSztpDefine(catos.getSztpDefine()); // TODO
						shipmentDetail.setCarrierName(ctnrInfo.getPtnrName());
						shipmentDetail.setVslName(ctnrInfo.getVslNm());
						shipmentDetail.setVslNm(ctnrInfo.getVslCd());
						shipmentDetail.setVoyNo(ctnrInfo.getCallSeq());
						shipmentDetail.setVslAndVoy(ctnrInfo.getUserVoy());
						shipmentDetail.setOpeCode(ctnrInfo.getPtnrCode());
						shipmentDetail.setSealNo(ctnrInfo.getSealNo1());
						shipmentDetail.setWgt(ctnrInfo.getWgt());
						shipmentDetail.setVoyCarrier(ctnrInfo.getInVoy());
						shipmentDetail.setLoadingPort(ctnrInfo.getPol());
						shipmentDetail.setDischargePort(ctnrInfo.getPod());
						shipmentDetail.setCargoType(ctnrInfo.getCargoType());
						shipmentDetail.setLocation(
								ctnrInfo.getLocation() != null ? ctnrInfo.getLocation() : ctnrInfo.getArea());
						shipmentDetail.setContainerRemark(ctnrInfo.getRemark());
						shipmentDetail.setYear(ctnrInfo.getCallYear());
					}
					// edo
					if ("1".equals(shipment.getEdoFlg())) {
						// TODO khai bao bien lai gon hon
						if (edoDetailMap.get(inputDetail.getContainerNo()) != null) {
							shipmentDetail
									.setExpiredDem(edoDetailMap.get(inputDetail.getContainerNo()).getExpiredDem());
							shipmentDetail
									.setDetFreeTime(edoDetailMap.get(inputDetail.getContainerNo()).getDetFreeTime());
						}
					}
					// Hàng cont nội
					if ("VN".equalsIgnoreCase(shipmentDetail.getLoadingPort().substring(0, 2))) {
						shipmentDetail.setCustomStatus(null);
						shipmentDetail.setStatus(2);
						shipmentDetail.setTaxCode(taxCode);
						shipmentDetail.setConsigneeByTaxCode(firstDetail.getConsignee());
					} else {
						shipmentDetail.setCustomStatus("N");
						shipmentDetail.setStatus(1);
						// set null to taxcode and consignee, update when custom process
						shipmentDetail.setTaxCode(null);
						shipmentDetail.setConsigneeByTaxCode(null);
					}
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
					// Save shipment details for SendE
					if (needCreateSendE) {
						shipmentDetail.setShipmentId(shipmentSendEmpty.getId());
						shipmentDetail.setCustomStatus(null);
						shipmentDetail.setFe("E");
						shipmentDetail.setCargoType("MT");
						shipmentDetail.setDischargePort("VNDAD");
						shipmentDetail.setVslNm("EMTY");
						shipmentDetail.setVoyNo("0000");
						shipmentDetail.setLocation(null);
						shipmentDetail.setOpeCode(shipmentSendEmpty.getOpeCode());
						shipmentDetail.setEmptyDepotLocation(
								getEmptyDepotLocation(shipmentDetail.getSztp(), shipmentDetail.getOpeCode()));
						shipmentDetail.setStatus(1);
						shipmentDetailService.insertShipmentDetail(shipmentDetail);
					}
					// Update case
				} else {
					// set null to taxcode and consignee
					ShipmentDetail shipmentDetailReference = shipmentDetailService
							.selectShipmentDetailById(inputDetail.getId());
					// validate shipment detail, in-case edit ID from client
					if (!shipmentDetailReference.getLogisticGroupId().equals(user.getGroupId())) {
						return error("Không tìm thấy thông tin, vui lòng kiểm tra lại");
					}
					// Update khi nguoi dung chua lam lenh.
					// chưa làm được trường hợp nếu lô đó vừa lạnh, vừa nguy hiểm vì 3 trường khác
					// nhau nên vào 1 if đầu
					if ("N".equals(shipmentDetailReference.getUserVerifyStatus())) {
						updateShipment = false;
						shipmentDetailReference.setUpdateBy(user.getFullName());
						shipmentDetailReference.setConsignee(inputDetail.getConsignee());
						shipmentDetailReference.setEmptyDepot(inputDetail.getEmptyDepot());
						// T/h la container domestic, update taxcode, consignee theo thong tin nguoi
						// dung nhap
						if ("VN".equalsIgnoreCase(shipmentDetailReference.getLoadingPort().substring(0, 2))) {
							shipmentDetailReference.setTaxCode(taxCode);
							shipmentDetailReference.setConsigneeByTaxCode(inputDetail.getConsignee());
						}
						// check if not eDO
						if (!"1".equals(shipment.getEdoFlg())) {
							shipmentDetailReference.setExpiredDem(inputDetail.getExpiredDem());
							shipmentDetailReference.setDetFreeTime(inputDetail.getDetFreeTime());
						}
						shipmentDetailReference.setUpdateTime(new Date());
						// nhatlv add status ban đầu khi update
						// cont lạnh
						// nếu sztp = R và (frozenStatus !R (chờ phê duyệt) hoặc frozenStatus !=Y(đã phê
						// duyệt) hoặc frozenStatus !=C(từ chối))
						if ("R".equalsIgnoreCase(shipmentDetailReference.getSztp().substring(2, 3))) {
							if (!"R".equalsIgnoreCase(shipmentDetailReference.getFrozenStatus())
									&& !"Y".equalsIgnoreCase(shipmentDetailReference.getFrozenStatus())
									&& !"C".equalsIgnoreCase(shipmentDetailReference.getFrozenStatus())) {
								shipmentDetailReference.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_INIT); // I
							}
						}
						// cont nguy hiểm
						if (StringUtils.isNotEmpty(shipmentDetailReference.getDangerousImo())
								|| StringUtils.isNotEmpty(shipmentDetailReference.getDangerousUnno())) {
							if (!"R".equalsIgnoreCase(shipmentDetailReference.getDangerous())
									&& !"Y".equalsIgnoreCase(shipmentDetailReference.getDangerous())
									&& !"C".equalsIgnoreCase(shipmentDetailReference.getDangerous())) {
								shipmentDetailReference.setDangerous(EportConstants.CONT_SPECIAL_STATUS_INIT);// I
							}
						}

						/*
						 * shipmentDetailReference.setOversizeTop(ctnrInfo.getOvHeight());
						 * shipmentDetailReference.setOversizeFront(ctnrInfo.getOvFore());
						 * shipmentDetailReference.setOversizeBack(ctnrInfo.getOvAft()); //frozen_status
						 * shipmentDetailReference.setOversizeLeft(ctnrInfo.getOvPort());
						 * shipmentDetailReference.setOversizeRight(ctnrInfo.getOvStbd());
						 */

						if (StringUtils.isNotEmpty(shipmentDetailReference.getOversizeBack())
								|| StringUtils.isNotEmpty(shipmentDetailReference.getOversizeFront())
								|| StringUtils.isNotEmpty(shipmentDetailReference.getOversizeLeft())
								|| StringUtils.isNotEmpty(shipmentDetailReference.getOversizeRight())
								|| StringUtils.isNotEmpty(shipmentDetailReference.getOversizeTop())) {
							if (!"R".equalsIgnoreCase(shipmentDetailReference.getOversize())
									&& !"Y".equalsIgnoreCase(shipmentDetailReference.getOversize())
									&& !"C".equalsIgnoreCase(shipmentDetailReference.getOversize())) {
								shipmentDetailReference.setOversize(EportConstants.CONT_SPECIAL_STATUS_INIT);// I
							}
						}
						// end

						if (shipmentDetailService.updateShipmentDetail(shipmentDetailReference) != 1) {
							return error("Lưu khai báo thất bại từ container: " + inputDetail.getContainerNo());
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
	public AjaxResult deleteShipmentDetail(@PathVariable Long shipmentId,
			@PathVariable("shipmentDetailIds") String shipmentDetailIds) {
		if (shipmentDetailIds != null) {
			// kiem tra co the xoa hay khong. Sau khi lam lenh thi khong the khai bao
			List<ShipmentDetail> details = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
					getUser().getGroupId());
			for (ShipmentDetail detail : details) {
				// Check if user is verified. delete when user not verified
				if (!"Y".equals(detail.getUserVerifyStatus())) {
					shipmentDetailService.deleteShipmentDetailById(detail.getId());
				}
			}
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			if (shipmentDetailService.countShipmentDetailList(shipmentDetail) == 0) {
				Shipment shipment = new Shipment();
				shipment.setId(shipmentId);
				shipment.setStatus("1");
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@PostMapping("/shipment-detail/bl-no/cont/info")
	@ResponseBody
	public ShipmentDetail getContInfo(@RequestBody ShipmentDetail shipmentDetail) {
		if (StringUtils.isNotEmpty(shipmentDetail.getBlNo())
				&& StringUtils.isNotEmpty(shipmentDetail.getContainerNo())) {
			// TODO su dung form tra ve gia tri can thiet, khong tra ve het
			ShipmentDetail shipmentDetailResult = catosApiService.selectShipmentDetailByContNo(shipmentDetail);
			return shipmentDetailResult;
		}
		return null;
	}

	@Log(title = "Xác Nhận OTP", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/otp/{otp}/verification")
	@ResponseBody
	@RepeatSubmit
	public AjaxResult verifyOtp(String shipmentDetailIds, @PathVariable("otp") String otp, String taxCode,
			boolean creditFlag, boolean isSendContEmpty) {
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
			return error("Mã OTP không chính xác, hoặc đã hết hiệu lực!");
		}
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
				getUser().getGroupId());
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			AjaxResult ajaxResult = null;
			Shipment shipment = shipmentService.selectShipmentById(shipmentDetails.get(0).getShipmentId());
			// update shipment for lock update
			if (!"3".equals(shipment.getStatus())) {
				shipment.setStatus("3");
				shipment.setUpdateTime(new Date());
				shipment.setUpdateBy(getUser().getFullName());
				shipmentService.updateShipment(shipment);
			}
			List<ServiceSendFullRobotReq> serviceRobotReqs = shipmentDetailService
					.makeOrderReceiveContFull(shipmentDetails, shipment, taxCode, creditFlag);
			AjaxResult validateResult = validateShipmentDetailList(shipmentDetails);
			Integer code = (Integer) validateResult.get("code");
			if (code != 0) {
				return validateResult;
			}
			if (serviceRobotReqs != null) {
				List<Long> processIds = new ArrayList<>();
				boolean robotBusy = false;

				// MAKE ORDER SEND CONT EMPTY
				// if (isSendContEmpty) {
				// shipment.setId(null);
				// List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
				// if (shipments != null && shipments.size() > 0) {
				// String conts = "";
				// for (ShipmentDetail shipmentDt: shipmentDetails) {
				// conts += shipmentDt.getContainerNo() + ",";
				// }
				// conts = conts.substring(0, conts.length()-1);
				// List<ShipmentDetail> shipmentDetails2 =
				// shipmentDetailService.selectSendEmptyShipmentDetailByListCont(conts,
				// shipments.get(0).getId());
				// ProcessOrder processOrder =
				// shipmentDetailService.makeOrderSendCont(shipmentDetails2, shipments.get(0),
				// creditFlag);
				// ServiceRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder,
				// shipmentDetails2);
				// try {
				// mqttService.publishMessageToRobot(serviceRobotReq,
				// EServiceRobot.SEND_CONT_EMPTY);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }
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
						ajaxResult = AjaxResult
								.warn("Yêu cầu đang được chờ xử lý, quý khách vui lòng đợi trong giây lát.");
						ajaxResult.put("processIds", processIds);
						ajaxResult.put("orderNumber", serviceRobotReqs.size());
						return ajaxResult;
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
					return error("Có lỗi xảy ra trong quá trình xác thực!");
				}
				ajaxResult = AjaxResult
						.success("Yêu cầu của quý khách đang được xử lý, quý khách vui lòng đợi trong giây lát.");
				ajaxResult.put("processIds", processIds);
				ajaxResult.put("orderNumber", serviceRobotReqs.size());
				return ajaxResult;
			}
		}
		return error("Có lỗi xảy ra trong quá trình xác thực!");
	}

	/**
	 * Get all consignee in catos and load in grid view
	 * 
	 * @return
	 */
	@GetMapping("/consignees")
	@ResponseBody
	public AjaxResult getField() {
		AjaxResult ajaxResult = success();
		List<String> listConsignee = shipmentDetailService.getConsigneeList();
		ajaxResult.put("consigneeList", listConsignee);
		return ajaxResult;
	}

	/**
	 * Check B/L no is exist for current logistic
	 * 
	 * @param inputForm
	 * @return
	 */
	@PostMapping("/shipment/bl-no")
	@ResponseBody
	public AjaxResult checkShipmentInforByBlNo(@RequestBody ContainerServiceForm inputForm) {
		String blNo = inputForm.getBlNo();
		if (StringUtils.isNotEmpty(blNo)) {
			blNo = blNo.toUpperCase();
		}
		AjaxResult ajaxResult = new AjaxResult();
		Shipment shipment = new Shipment();
		// check bill unique
		shipment.setServiceType(Constants.RECEIVE_CONT_FULL);
		shipment.setLogisticGroupId(getUser().getGroupId());
		shipment.setBlNo(blNo);
		if (shipmentService.checkBillBookingNoUnique(shipment) != 0) {
			return error("Số bill đã tồn tại");
		}

		// check opeCode
		String opeCode = edoService.getOpeCodeByBlNo(blNo);
		// Long containerAmount = edoService.getCountContainerAmountByBlNo(blNo);

		// Check edo with master bill
		if (opeCode != null) {
			// Check if carrier group support edo depend on do type defining in carrier
			// group admin
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
				// Check if carrier group support edo depend on do type defining in carrier
				// group admin
				if (EportConstants.DO_TYPE_CARRIER_EDO
						.equalsIgnoreCase(carrierGroupService.getDoTypeByOpeCode(edoHouseBill.getCarrierCode()))) {
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
			if (edoFlg == null) {
				return error(
						"Mã hãng tàu:" + shipCatos.getOpeCode() + " không có trong hệ thống. Vui lòng liên hệ Cảng!");
			}
			// if(edoFlg.equals("1")){
			// return error("Bill này là eDO nhưng không có dữ liệu trong eport. Vui lòng
			// liên hệ Cảng!");
			// }
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
			containerAmount = edoHouseBillService.getContainerAmountWithOrderNumber(shipment.getHouseBill(),
					shipment.getOrderNumber());
		} else {
			// Check for master bill case
			containerAmount = edoService.getContainerAmountWithOrderNumber(shipment.getBlNo(),
					shipment.getOrderNumber());
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
		if (shipment == null || !shipment.getLogisticGroupId().equals(getUser().getGroupId())) {
			return error("Lô không tồn tai, vui lòng kiểm tra lại.");
		}
		// check DO or eDO (0 is DO no need to validate
		// Get tax code of consignee own this shipment
		String taxCode = shipmentDetailService.selectConsigneeTaxCodeByShipmentId(shipmentId);
		// taxcode from HQ null -> chua thong quan, cont nội: chưa chọn chủ hàng
		if (taxCode == null) {
			return error("Không xác định được chủ hàng, vui lòng kiểm tra lại.");
		}
		// if logistic is consignee -> pass
		String myTaxcode = getGroup().getMst();
		if (taxCode.equalsIgnoreCase(myTaxcode)) {
			// Pass
			return success();
		}
		// Check if logistic can make order for this shipment
		if (logisticGroupService.checkDelegatePermission(taxCode, myTaxcode,
				EportConstants.DELEGATE_PERMISSION_PROCESS) > 0) {
			return success();
		}
		return error(
				"Bạn chưa có ủy quyền từ chủ hàng để thực hiện lô hàng này. Hãy liên hệ với Cảng để thêm ủy quyền.");
	}

	@GetMapping("/shipment/{shipmentId}/custom/notification")
	@ResponseBody
	public AjaxResult sendNotificationCustomError(@PathVariable("shipmentId") String shipmentId) {
		try {
			mqttService.sendNotification(NotificationCode.NOTIFICATION_OM_CUSTOM, "Lỗi hải quan lô " + shipmentId,
					configService.selectConfigByKey("domain.admin.name") + "/om/support/custom/" + shipmentId);
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

	private AjaxResult validateShipmentDetailList(List<ShipmentDetail> shipmentDetails) {
		if (CollectionUtils.isEmpty(shipmentDetails)) {
			return error("Không tìm thấy thông tin chi tiết lô đã chọn.");
		}
		// validate
		ShipmentDetail shipmentDetailReference = shipmentDetails.get(0);
		String containerNos = "";
		String userVoy = shipmentDetailReference.getVslAndVoy();
		// List sztp can register on eport get from dictionary
		// All sztp that not in this list is invalid
		List<String> sztps = dictService.getListTag("sys_size_container_eport");
		for (int i = 0; i < shipmentDetails.size(); i++) {
			if (StringUtils.isEmpty(shipmentDetails.get(i).getContainerNo())) {
				return error("Hàng " + (i + 1) + ": Chưa nhập số container!");
			}
			if (shipmentDetailReference.getExpiredDem() == null) {
				return error("Hàng " + (i + 1) + ": Chưa nhập hạn lệnh!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getConsignee())) {
				return error("Hàng " + (i + 1) + ": Chưa chọn chủ hàng!");
			}
			if (StringUtils.isEmpty(shipmentDetails.get(i).getEmptyDepot())) {
				return error("Hàng " + (i + 1) + ": Chưa chọn nơi hạ vỏ!");
			}
			if (!shipmentDetailReference.getConsignee().equals(shipmentDetails.get(i).getConsignee())) {
				return error("Tên chủ hàng không được khác nhau!");
			}
			// Validate sztp
			if (!sztps.contains(shipmentDetails.get(i).getSztp())) {
				return error(
						"Kích thước " + shipmentDetails.get(i).getSztp() + " không được phép làm lệnh trên eport.");
			}

			if (shipmentDetails.get(i).getSztp().substring(2, 3).equals("R")) {
				ShipmentDetail shipmentDetailCurent = shipmentDetails.get(i);
				List<ReeferInfo> reeferInfosFromDB = reeferInfoService
						.selectReeferInfoListByIdShipmentDetail(shipmentDetailCurent.getId());
				if (reeferInfosFromDB.get(0).getPayType().equals("Credit") && !reeferInfosFromDB.get(0)
						.getPaymentStatus().equals(EportConstants.CONT_REEFER_PAYMENT_SUCCESS)) {
					return error("Không thể làm lệnh do tiền điện chưa được thanh toán trả trước.");
				}
			}

			containerNos += shipmentDetails.get(i).getContainerNo() + ",";
		}
		// trim last ','
		if (containerNos.length() > 0) {
			containerNos = containerNos.substring(0, containerNos.length() - 1);
		}
		// validate consignee exist in catos
		if (catosApiService.checkConsigneeExistInCatos(shipmentDetailReference.getConsignee()) == 0) {
			return error(
					"Tên chủ hàng quý khách nhập không đúng, vui lòng chọn tên chủ hàng từ trong danh sách của hệ thống gợi ý.");
		}
		// kiem tra uy quyen
		if (logisticGroupService.checkDelegatePermission(shipmentDetailReference.getTaxCode(), getGroup().getMst(),
				EportConstants.DELEGATE_PERMISSION_PROCESS) == 0) {
			return error(
					"Bạn chưa có ủy quyền từ chủ hàng để thực hiện lô hàng này. Hãy liên hệ với Cảng để thêm ủy quyền.");
		}
		// kiem tra container da duoc lam lenh trong catos
		List<ContainerInfoDto> pickupResult = catosApiService.getContainerPickup(containerNos, userVoy);
		String pickuped = "";
		if (pickupResult != null && pickupResult.size() > 0) {
			for (ContainerInfoDto dto : pickupResult) {
				pickuped += dto.getCntrNo() + ", ";
			}
			// trim last ','
			pickuped = StringUtils.substring(pickuped, 0, -2);
			return error("Các container sau đã làm lệnh nâng hạ.<br/>Vui lòng kiểm tra lại:<br>" + pickuped);
		}

		return success();
	}

	/**
	 * Create Map of ContainerNumber-> ShipmentDetail from catos
	 * 
	 * @param blNo
	 * @return
	 */
	private Map<String, ContainerInfoDto> getCatosShipmentDetail(String blNo) {
		// get infor from catos
		List<ContainerInfoDto> shipmentDetailsCatos = catosApiService.selectShipmentDetailsByBLNo(blNo);
		Map<String, ContainerInfoDto> detailMap = new HashMap<>();
		// Get opecode, sealNo, wgt, pol,pod
		if (shipmentDetailsCatos != null) {
			for (ContainerInfoDto detail : shipmentDetailsCatos) {
				if ("F".equals(detail.getFe())) {
					detailMap.put(detail.getCntrNo(), detail);
				}
			}
		}
		return detailMap;
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
				shipmentComment.setTopic(EportConstants.TOPIC_COMMENT_CANCEL_PICKUP_FULL);
				shipmentComment.setServiceType(shipmentDetail.getServiceType());
				shipmentCommentService.insertShipmentComment(shipmentComment);
			}

			// Set up data to send app notificaton
			String title = "ePort: Yêu cầu huỷ lệnh nhận container hàng.";
			String msg = "Có yêu cầu huỷ lệnh nhận container hàng cho Bill: " + shipmentDetail.getBlNo()
					+ ", Hãng tàu: " + shipmentDetail.getOpeCode() + ", Trucker: " + getGroup().getGroupName()
					+ ", Chủ hàng: " + shipmentDetails.get(0).getConsignee();
			try {
				mqttService.sendNotificationApp(NotificationCode.NOTIFICATION_OM, title, msg,
						configService.selectConfigByKey("domain.admin.name") + EportConstants.URL_CANCEL_ORDER_SUPPORT,
						EportConstants.NOTIFICATION_PRIORITY_LOW);
			} catch (MqttException e) {
				logger.error("Error when push request cancel order pickup full: " + e);
			}
			return success("Đã yêu cầu hủy lệnh, quý khách vui lòng đợi bộ phận thủ tục xử lý.");
		}

		return error("Yêu cầu hủy lệnh thất bại, quý khách vui lòng liên hệ bộ phận thủ tục để được hỗ trợ thêm.");
	}

	// delete file
	/**
	 * @param id
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	@DeleteMapping("/delete_file")
	@ResponseBody
	public AjaxResult deleteFile(Long id, String filePath) throws IOException {
		if (id != null) {
			ShipmentImage shipmentImageParam = new ShipmentImage();
			shipmentImageParam.setId(id);
			ShipmentImage shipmentImage = shipmentImageService.selectShipmentImageById(shipmentImageParam);
			String[] fileArr = shipmentImage.getPath().split("/");
			File file = new File(Global.getUploadPath() + "/receiveContFull/" + getUser().getGroupId() + "/"
					+ fileArr[fileArr.length - 1]);
			// if (file.delete()) {
			shipmentImageService.deleteShipmentImageById(id);
			// }
			return success();
		} else {
			String[] fileArr = filePath.split("/");
			File file = new File(Global.getUploadPath() + "/receiveContFull/" + getUser().getGroupId() + "/"
					+ fileArr[fileArr.length - 1]);
			if (file.delete()) {
				return success();
			}
			return error("Lỗi Xóa File");
		}

	}

	@DeleteMapping("/cont-special/file/{id}")
	@ResponseBody
	public AjaxResult deleteSendContFullFile(@PathVariable("id") Long id) throws IOException {
		if (id != 0) {
			ShipmentImage shipmentImageParam = new ShipmentImage();
			shipmentImageParam.setId(id);
			ShipmentImage shipmentImage = shipmentImageService.selectShipmentImageById(shipmentImageParam);
			String[] fileArr = shipmentImage.getPath().split("/");
			File file = new File(Global.getUploadPath() + "/reeferInfo/" + getUser().getGroupId() + "/"
					+ fileArr[fileArr.length - 1]);
			if (file.delete()) {
				shipmentImageService.deleteShipmentImageById(id);
			}
			return success();
		}
		return error();
	}

	/**
	 * @param declareNoList
	 * @param shipmentDetailIds
	 * @return
	 */
	@Log(title = "Check Hải Quan", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/custom-status/shipment-detail")
	@ResponseBody
	public AjaxResult checkCustomStatus(@RequestParam(value = "declareNos") String declareNoList,
			@RequestParam(value = "shipmentDetailIds") String shipmentDetailIds) {
		if (StringUtils.isNotEmpty(declareNoList)) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds,
					getUser().getGroupId());
			// flag mapping custom No
			if (CollectionUtils.isNotEmpty(shipmentDetails)) {
				// boolean customsNoMappingFlg =
				// "1".equals(configService.selectConfigByKey(SystemConstants.ACCIS_CUSTOM_MAPPING_FLG_KEY));
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					// Save declareNoList to shipment detail
					shipmentDetail.setCustomsNo(declareNoList);
					shipmentDetail.setCustomScanTime(new Date());
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// Neu bat buoc check to khai thi phai goi lai acciss
					// if (!customsNoMappingFlg &&
					// catosApiService.checkCustomStatus(shipmentDetail.getContainerNo(),
					// shipmentDetail.getVoyNo())) {
					// if (shipmentDetail.getStatus() == 1) {
					// shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
					// }
					// shipmentDetail.setCustomStatus("R");
					// shipmentDetailService.updateShipmentDetail(shipmentDetail);
					// AjaxResult ajaxResult = AjaxResult.success();
					// ajaxResult.put("shipmentDetail", shipmentDetail);
					// webSocketService.sendMessage("/" + shipmentDetail.getContainerNo() +
					// "/response", ajaxResult);
					// } else {
					customQueueService.offerShipmentDetail(shipmentDetail);
					// }
				}
				return success();
			}
		}
		return error();
	}

	/**
	 * @param shipmentDetailIds
	 * @return
	 */

	@Log(title = "Yêu cầu xác nhận", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/shipment-detail/request-confirm")
	@ResponseBody
	public AjaxResult CheckShipmentDetail(String shipmentDetailIds) {
		ShipmentDetail shipmentDetailUpdate = new ShipmentDetail();

		// List<ShipmentDetail> shipmentDetails = shipmentDetailService
		// .selectConfirmShipmentDetailByIds(shipmentDetailIds);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService
				.selectConfirmShipmentDetailByshipmentDetailIds(shipmentDetailIds);
		// List<ShipmentImage> shipmentImages =
		// shipmentImageService.selectShipmentImagesByshipmentDetailIds
		// (shipmentDetailIds) ;

		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			if (!"R".equalsIgnoreCase(shipmentDetail.getSztp().substring(2, 3))) {
				if (shipmentDetail.getPath() == "" || shipmentDetail.getPath() == null) {
					return error("Bạn chưa đính kèm file. Vui lòng đính kèm file trước khi yê cầu xác nhận");
				}
				if (shipmentDetail.getChassisNo() == "" || shipmentDetail.getChassisNo() == null) {
					return error("Bạn chưa khai báo biển số xe rơ móc");
				}
				if (shipmentDetail.getTruckNo() == "" || shipmentDetail.getTruckNo() == null) {
					return error("Bạn chưa khai báo biển số xe đầu kéo");
				}
			}
			// cont lanh
			if ("R".equalsIgnoreCase(shipmentDetail.getSztp().substring(2, 3))) {
				shipmentDetailUpdate.setFrozenStatus(EportConstants.CONT_SPECIAL_STATUS_REQ); // R
			}
			// cont qua kho
			if (StringUtils.isNotEmpty(shipmentDetail.getOversizeLeft())
					|| StringUtils.isNotEmpty(shipmentDetail.getOversizeRight())
					|| StringUtils.isNotEmpty(shipmentDetail.getOversizeTop())) {
				shipmentDetailUpdate.setOversize(EportConstants.CONT_SPECIAL_STATUS_REQ);// R
			}
			shipmentDetailUpdate.setId(shipmentDetail.getId());
			// shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds,
			// shipmentDetailUpdate);
			shipmentDetailService.updateShipmentDetail(shipmentDetailUpdate);
		}

		return success("Yêu cầu xác nhận thành công");
	}

	// end yêu cầu xác nhận cont

	@GetMapping("/shipment/{shipmentId}/shipment-detail")
	@ResponseBody
	public AjaxResult listShipmentDetail(@PathVariable Long shipmentId) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentId);
		// check if shipment exist and allow permission
		if (shipment == null || !verifyPermission(shipment.getLogisticGroupId())) {
			return error("Không tìm thấy lô");
		}
		// get list shipment detail by Id
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailList(shipmentDetail);

		// auto load containers detail for eDO for first time
		if ("1".equals(shipment.getEdoFlg()) && shipmentDetails.size() == 0) {
			if (StringUtils.isNotEmpty(shipment.getHouseBill())) {
				shipmentDetails = shipmentDetailService.getShipmentDetailFromHouseBill(shipment.getHouseBill());
			} else {
				// get infor from edi
				shipmentDetails = shipmentDetailService.getShipmentDetailsFromEDIByBlNo(shipment.getBlNo());
			}
			// get infor from catos
			// List<ShipmentDetail> shipmentDetailsCatos =
			// catosApiService.selectShipmentDetailsByBLNo(shipment.getBlNo());
			Map<String, ContainerInfoDto> catosDetailMap = getCatosShipmentDetail(shipment.getBlNo());
			// Get opecode, sealNo, wgt, pol, pod
			ContainerInfoDto catos = null;

			for (ShipmentDetail detail : shipmentDetails) {
				catos = catosDetailMap.get(detail.getContainerNo());
				if (catos != null) {
					// Overwrite information from CATOS
					detail.setSztp(catos.getSztp());
					// Block-Bay-Row-Tier
					detail.setLocation(catos.getLocation());
					detail.setContainerRemark(catos.getRemark());
					detail.setOpeCode(catos.getPtnrCode() + ": " + catos.getPtnrName());
					detail.setVslNm(catos.getVslCd() + ": " + catos.getVslNm());// overwrite VSL_CD:VSL_NM from CATOS
					detail.setVoyCarrier(catos.getInVoy()); // Carrier voyage name on booking
					detail.setVoyNo(catos.getUserVoy());
					detail.setSealNo(catos.getSealNo1());
					detail.setWgt(catos.getWgt());
					detail.setLoadingPort(catos.getPol()); // overwrite from CATOS
					detail.setDischargePort(catos.getPod()); // overwrite from CATOS
					detail.setDangerousImo(catos.getImdg()); // overwrite from catos IMDG
					detail.setDangerousUnno(catos.getUnno()); //
					detail.setOvHeight(catos.getOsHeight());
					detail.setOsPort(catos.getOsPort());
					detail.setOsStbd(catos.getOsStbd());
					detail.setOvAft(catos.getOvAft());
					detail.setOvFore(catos.getOvFore());
					detail.setOvHeight(catos.getOvHeight());
					detail.setOvPort(catos.getOvPort());
					detail.setOvStbd(catos.getOvStbd());
				}
			}
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentDetails", shipmentDetails);
		return ajaxResult;
	}
	// nhat

	/**
	 * @param file
	 * @param fileType
	 * @return
	 * @throws IOException
	 * @throws InvalidExtensionException
	 */
	@PostMapping("/file/file-type/{fileType}")
	// @PostMapping("/file/file-type")
	@ResponseBody
	public AjaxResult saveFile(MultipartFile file, @PathVariable("fileType") String fileType)
			throws IOException, InvalidExtensionException {
		String basePath = String.format("%s/%s", Global.getUploadPath() + "/receiveContFull", getUser().getGroupId());
		String now = DateUtils.dateTimeNow();
		String fileName = String.format("file%s.%s", now, FileUploadUtils.getExtension(file));
		String filePath = FileUploadUtils.upload(basePath, fileName, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setPath(filePath);
		shipmentImage.setCreateTime(DateUtils.getNowDate());
		shipmentImage.setCreateBy(getUser().getFullName());
		shipmentImage.setFileType(fileType);
		shipmentImageService.insertShipmentImage(shipmentImage);
		AjaxResult ajaxResult = AjaxResult.success();

		ajaxResult.put("shipmentFileId", shipmentImage.getId());
		ajaxResult.put("file", filePath);
		ajaxResult.put("fileType", fileType);

		ajaxResult.put("fileId", shipmentImage.getId());

		return ajaxResult;
	}
	// nhat

	// details when pressing detail
	/**
	 * @param shipmentDetailId
	 * @param containerNo
	 * @param sztp
	 * @param mmap
	 * @return
	 */
	@GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
	public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
			@PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {

		ShipmentDetail shipmentDetailFromDB = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		Map<String, ContainerInfoDto> catosDetailMap = getCatosShipmentDetail(shipmentDetailFromDB.getBlNo());

		// get temp
		ContainerInfoDto catos = catosDetailMap.get(shipmentDetailFromDB.getContainerNo());
		if (catos != null) {
			if (StringUtils.isEmpty(shipmentDetailFromDB.getTemperature())) {
				boolean isChange = false;
				if (catos.getSetTemp() != null) {
					isChange = true;
					shipmentDetailFromDB.setTemperature(catos.getSetTemp());
				}
				if (catos.getAirvent() != null) {
					isChange = true;
					shipmentDetailFromDB.setVentilation(catos.getAirvent());
				}

				if (isChange) {
					shipmentDetailService.updateShipmentDetail(shipmentDetailFromDB);
				}
			}
		}

		mmap.put("containerNo", containerNo);
		mmap.put("sztp", sztp);
		mmap.put("shipmentDetailId", shipmentDetailId);
		mmap.put("shipmentDetail", shipmentDetailFromDB);
		ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		mmap.put("shipmentFiles", shipmentImages);

		ShipmentDetailHist shipmentDetailHist = new ShipmentDetailHist();
		shipmentDetailHist.setDataField("Power Draw Date");
		shipmentDetailHist.setShipmentDetailId(shipmentDetailId);
		mmap.put("powerDropDate", shipmentDetailHistService.selectShipmentDetailHistList(shipmentDetailHist));

		mmap.put("reeferInfos", reeferInfoService.selectReeferInfoListByIdShipmentDetail(shipmentDetailId));
		mmap.put("oprlistBookingCheck", dictService.getType("opr_list_booking_check"));
		mmap.put("creditFlag", getGroup().getCreditFlag());
		mmap.put("billPowers", dictService.getType("bill_power"));

		mmap.put("shipment", shipmentService.selectShipmentById(shipmentDetailFromDB.getShipmentId()));

		return PREFIX + "/detail";
	}
	// save file in detail

	/**
	 * @param filePaths
	 * @param fileType
	 * @param shipmentDetailId
	 * @param shipmentId
	 * @param shipmentSztp
	 * @param shipmentDangerous
	 * @return
	 * @throws IOException
	 * @throws InvalidExtensionException
	 */
	@PostMapping("/saveFileImage")
	@ResponseBody
	public AjaxResult uploadFile(@RequestParam(value = "filePaths[]", required = false) String[] filePaths,
			@RequestParam(value = "fileType[]", required = false) String[] fileType,
			@RequestParam(value = "fileIds[]", required = false) String[] fileIds, String shipmentDetailId,
			Long shipmentId, String shipmentSztp) throws IOException, InvalidExtensionException {

		if (filePaths.length > 0) {
			for (int i = 0; i < filePaths.length; i++) {
				ShipmentImage shipmentImage = new ShipmentImage();
				shipmentImage.setPath(filePaths[i]);
				shipmentImage.setShipmentId(shipmentId);
				shipmentImage.setShipmentDetailId(shipmentDetailId);
				shipmentImage.setFileType(fileType[i]);
				// Map<String, Object> map = new HashMap<>();
				// map.put("ids", fileIds[i]);
				// shipmentImage.setParams(map);
				shipmentImage.setId(Long.valueOf(fileIds[i]));

				// shipmentImageService.updateShipmentImageByIds(shipmentImage);// them detail

				shipmentImageService.updateShipmentImageByIdsReceive(shipmentImage);// them detail

				// shipmentImageService.insertShipmentImage(shipmentImage);// them detail
			}

		}

		return success();
		// return null;
	}

	// insert powerDrawDate

	/**
	 * @param shipmentDetailId
	 * @param shipmentId
	 * @param shipmentSztp
	 * @param powerDrawDate
	 * @return
	 * @throws IOException
	 * @throws InvalidExtensionException
	 */

	@PostMapping("/saveDate")
	@ResponseBody
	public AjaxResult saveDate(
			// String shipmentDetailId, Long shipmentId, String shipmentSztp,
			// Date powerDrawDate
			@RequestBody ShipmentDetail detail) throws IOException, InvalidExtensionException {
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(detail.getId());
		Date powerDrawDateOldFromDB = shipmentDetail.getPowerDrawDate();
		String shipmentDetailId = detail.getId().toString();

		shipmentDetail.setPowerDrawDate(detail.getPowerDrawDate());

		ReeferInfo reeferInfo = new ReeferInfo();
		reeferInfo.setDateGetPower(detail.getPowerDrawDate());
		reeferInfo.setDateSetPower(shipmentDetail.getDaySetupTemperature());
		reeferInfo.setShipmentDetailId(shipmentDetail.getId());
		reeferInfo.setStatus("S");

		List<SysDictData> oprListBookingCheck = dictService.getType("opr_list_booking_check");
		String creditFlag = getGroup().getCreditFlag();

		// la khach hàng trả
		if (creditFlag.equals("0")) {
			reeferInfo.setPayerType("Customer");
			reeferInfo.setPayType("Credit");
		} else {
			reeferInfo.setPayerType("Customer");
			reeferInfo.setPayType("Cash");
		}

		for (SysDictData sysDictData : oprListBookingCheck) {
			// la hang tau thanh toan
			if (shipmentDetail.getOpeCode().equals(sysDictData.getDictValue())) {
				reeferInfo.setPayerType("Carriers");
			}
		}

		List<ReeferInfo> infosFromDB = this.reeferInfoService
				.selectReeferInfoListByIdShipmentDetail(shipmentDetail.getId());

		if ("R".equalsIgnoreCase(shipmentDetail.getSztp().substring(2, 3))) {
			if (powerDrawDateOldFromDB == null && infosFromDB.size() == 0) {
				reeferInfoService.insertReeferInfo(reeferInfo);
			} else {
				ReeferInfo reeferInfoFromDB = infosFromDB.get(0);
				reeferInfo.setId(reeferInfoFromDB.getId());
				reeferInfo.setUpdateBy(getUser().getUserName());
				reeferInfoService.updateReeferInfo(reeferInfo);
			}
		}

		shipmentDetail.setTruckNo(detail.getTruckNo());
		shipmentDetail.setChassisNo(detail.getChassisNo());
		shipmentDetailService.updateShipmentDetailByIds(shipmentDetailId, shipmentDetail);

		return AjaxResult.success(shipmentDetail);
	}

	@PostMapping("/extendPowerDrawDate")
	@ResponseBody
	public AjaxResult extendPowerDrawDate(@RequestBody ShipmentDetail detail) {
		ReeferInfo reeferInfo = new ReeferInfo();
		List<ReeferInfo> infos = reeferInfoService.selectReeferInfoListByIdShipmentDetail(detail.getId());
		ShipmentDetail detailFromDB = shipmentDetailService.selectShipmentDetailById(detail.getId());

		reeferInfo.setStatus("P");
		reeferInfo.setDateGetPower(detail.getPowerDrawDate());
		reeferInfo.setDateSetPower(detailFromDB.getPowerDrawDate());
		reeferInfo.setShipmentDetailId(detail.getId());
		reeferInfo.setPaymentStatus(EportConstants.CONT_REEFER_PAYMENT_PROCESS);

		List<SysDictData> oprListBookingCheck = dictService.getType("opr_list_booking_check");
		String creditFlag = getGroup().getCreditFlag();

		// la khach hàng trả
		if (creditFlag.equals("0")) {
			reeferInfo.setPayerType("Customer");
			reeferInfo.setPayType("Credit");
		} else {
			reeferInfo.setPayerType("Customer");
			reeferInfo.setPayType("Cash");
		}

		for (SysDictData sysDictData : oprListBookingCheck) {
			// la hang tau thanh toan
			if (detailFromDB.getOpeCode().equals(sysDictData.getDictValue())) {
				reeferInfo.setPayerType("Carriers");
			}
		}

		detail.setUpdateBy(getUser().getFullName());
		detail.setPowerDrawDateStatus("P");
		detail.setPowerDrawDate(null);

		shipmentDetailService.updateShipmentDetailByIds(detail.getId().toString(), detail);

		reeferInfoService.insertReeferInfo(reeferInfo);
		return AjaxResult.success(reeferInfoService.selectReeferInfoListByIdShipmentDetail(detail.getId()));
	}

	@PostMapping("/cancelDateDrop")
	@ResponseBody
	public AjaxResult cancelDateDrop(@RequestBody ReeferInfo info) {
		reeferInfoService.deleteReeferInfoById(info.getId());
		ShipmentDetail detail = shipmentDetailService.selectShipmentDetailById(info.getShipmentDetailId());
		detail.setUpdateBy(getUser().getFullName());
		detail.setPowerDrawDateStatus("S");
		shipmentDetailService.updateShipmentDetailByIds(detail.getId().toString(), detail);
		return AjaxResult.success(reeferInfoService.selectReeferInfoListByIdShipmentDetail(info.getShipmentDetailId()));
	}

	@PostMapping("/reefer-info/file")
	@ResponseBody
	public AjaxResult saveContSpecialeFile(MultipartFile file) throws IOException, InvalidExtensionException {
		String basePath = String.format("%s/%s", Global.getUploadPath() + "/reeferInfo", getUser().getGroupId());
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
}
