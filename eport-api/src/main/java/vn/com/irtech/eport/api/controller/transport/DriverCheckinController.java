package vn.com.irtech.eport.api.controller.transport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.form.QrCodeReq;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.api.queue.listener.QueueService;
import vn.com.irtech.eport.api.service.transport.IDriverCheckinService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.dto.PickedContAvailableDto;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@RestController
@RequestMapping("/transport/checkin")
public class DriverCheckinController extends BaseController  {
	
	private final Logger logger = LoggerFactory.getLogger(DriverCheckinController.class);
	
	@Autowired
	private IDriverCheckinService driverCheckinService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private QueueService queueService;

	@Autowired
	private ITruckEntranceService truckEntranceService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private ILogisticTruckService logisticTruckService;
	
	private static final Long TIME_OUT_WAIT_DETECTION = 1000L;
	private static final Integer RETRY_WAIT_DETECTION = 60;
	
	@Log(title = "Tài Xế Check-in", businessType = BusinessType.UPDATE, operatorType = OperatorType.MOBILE)
	@PostMapping("")
	@ResponseBody
	public AjaxResult checkin(@Valid @RequestBody QrCodeReq req) throws Exception{
		
		// Update pickup history
		List<PickupHistory> pickupHistories = req.getPickupHistories();
		if (CollectionUtils.isNotEmpty(pickupHistories)) {
			if (pickupHistories.size() == 2) {
				PickupHistory pickup1 = pickupHistories.get(0);
				PickupHistory pickup2 = pickupHistories.get(1);
				if (pickup1.getId().equals(pickup2.getId())) {
					for (Long id : req.getPickupHistoryIds()) {
						if (!id.equals(pickup2.getId())) {
							pickup2.setId(id);
							break;
						}
					}
				}
			}
		}
		for (PickupHistory pickupHistory : pickupHistories) {
			pickupHistoryService.updatePickupHistory(pickupHistory);
		}

		AjaxResult ajaxResult = AjaxResult.success();
		
		String sessionId = getSession().getId();
		
		ajaxResult.put("sessionId", sessionId);
		
		ajaxResult.put("qrString", driverCheckinService.checkin(req, sessionId));
		
		sendCheckinReq(SecurityUtils.getCurrentUser().getUser().getUserId(), sessionId);

		return ajaxResult;
	}
	
	private void sendCheckinReq(Long driverId, String sessionId) {
//		PickupHistory pickupHistoryParam = new PickupHistory();
//		pickupHistoryParam.setDriverId(driverId);
//		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
//		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
//		
//		if (CollectionUtils.isEmpty(pickupHistories)) {
//			String message = "Quý khách chưa đăng ký vận chuyển container ra/vào cảng.";
//			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
//					message);
//			throw new BusinessException(message);
//		}
//		
//		// Set pre data for gate notification check in request to notification app by gate user
//		// All flag and option of the object be false and will be true when meet condition
//		GateNotificationCheckInReq gateNotificationCheckInReq = new GateNotificationCheckInReq();
//		gateNotificationCheckInReq.setReceiveOption(false);
//		gateNotificationCheckInReq.setSendOption(false);
//		gateNotificationCheckInReq.setRefFlg1(false);
//		gateNotificationCheckInReq.setRefFlg2(false);
//		gateNotificationCheckInReq.setSessionId(sessionId);
//		
//		// Set general infor up all pickup history in list
//		// It's supposed to be same with all element in list
//		PickupHistory pickHistoryGeneral = pickupHistories.get(0);
//		gateNotificationCheckInReq.setGatePass(pickHistoryGeneral.getGatePass());
//		gateNotificationCheckInReq.setTruckNo(pickHistoryGeneral.getTruckNo());
//		gateNotificationCheckInReq.setChassisNo(pickHistoryGeneral.getChassisNo());
//		
//		// contSendCount variable to count the number of cont send to gate in
//		// same to contReceivecount variable, depend on the number set to container1 or
//		// container2 attribute
//		int contSendCount = 0;
//		int contReceiveCount = 0;
//
//		// Get weight and self weight by truck no
//		LogisticTruck logisticTruckParam = new LogisticTruck();
//		logisticTruckParam.setPlateNumber(pickHistoryGeneral.getTruckNo());
//		logisticTruckParam.setType(EportConstants.TRUCK_TYPE_TRUCK_NO);
//		List<LogisticTruck> truckNos = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
//
//		logisticTruckParam.setPlateNumber(pickHistoryGeneral.getChassisNo());
//		logisticTruckParam.setType(EportConstants.TRUCK_TYPE_CHASSIS_NO);
//		List<LogisticTruck> chassisNos = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
//
//		if (CollectionUtils.isNotEmpty(chassisNos)) {
//			if (CollectionUtils.isNotEmpty(truckNos)) {
//				try {
//					gateNotificationCheckInReq.setLoadableWgt(chassisNos.get(0).getWgt());
//					gateNotificationCheckInReq.setDeduct(truckNos.get(0).getSelfWgt() + chassisNos.get(0).getSelfWgt());
//				} catch (Exception ex) {
//					logger.warn(">>>>>>>>>>>>>>>>> Weight failed", ex);
//				}
//			}
//		} else {
//			logger.warn(">>>>>>>>>>>>>>>>> Khong tim thay ro-mooc");
//		}
//
//		// Begin interate pickup history list get by driver id
//		for (PickupHistory pickupHistory : pickupHistories) {
//
//			// Get service type of pickup history
//			Integer serviceType = pickupHistory.getShipment().getServiceType();
//
//			// Check if pickup is receive cont (out mode in gate)
//			// true then receive option of gate notification req true
//			// set other data for receive option
//			if (EportConstants.SERVICE_PICKUP_FULL == serviceType
//					|| EportConstants.SERVICE_PICKUP_EMPTY == serviceType) {
//				contReceiveCount++;
//				gateNotificationCheckInReq.setReceiveOption(true);
//
//				// Check if cont count is 1 then that should be the first container out
//				if (contReceiveCount == 1) {
//
//					// Check if is pickup by job order no or pickup by container
//					if (pickupHistory.getJobOrderFlg()) {
//						gateNotificationCheckInReq.setRefFlg1(true);
//						gateNotificationCheckInReq.setRefNo1(pickupHistory.getJobOrderNo());
//					} else {
//						gateNotificationCheckInReq.setContainerReceive1(pickupHistory.getContainerNo());
//					}
//
//					// Not 1 then that definitely 2 -> container 2 or job 2
//				} else {
//
//					// Check if is pickup by job order no or pickup by container
//					if (pickupHistory.getJobOrderFlg()) {
//						gateNotificationCheckInReq.setRefFlg2(true);
//						gateNotificationCheckInReq.setRefNo2(pickupHistory.getJobOrderNo());
//					} else {
//						gateNotificationCheckInReq.setContainerReceive2(pickupHistory.getContainerNo());
//					}
//				}
//				// the data will be always pickup or drop container so no need for check for
//				// service type
//				// If it not pickup full or empty then it definitely is drop if exception then
//				// the data is stored wrong
//				// that should be bad
//			} else {
//				contSendCount++;
//				gateNotificationCheckInReq.setSendOption(true);
//
//				// The count for this option will be always 1 or 2, so 1 container no should be
//				// set for 1
//				// If the exception happen then data is saved is wrong
//				if (contSendCount == 1) {
//					gateNotificationCheckInReq.setContainerSend1(pickupHistory.getContainerNo());
//				} else {
//					gateNotificationCheckInReq.setContainerSend2(pickupHistory.getContainerNo());
//				}
//			}
//		}
//		queueService.offerCheckInReq(gateNotificationCheckInReq);
	}

	@GetMapping("/cont-available")
	public AjaxResult getListContainerAvailable() {
		Long driverId = SecurityUtils.getCurrentUser().getUser().getUserId();
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(driverId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		Map<String, Object> params = new HashMap<>();
		params.put("params.serviceTypes",
				Convert.toStrArray(EportConstants.SERVICE_PICKUP_FULL + "," + EportConstants.SERVICE_PICKUP_EMPTY));
		pickupHistoryParam.setParams(params);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		if (CollectionUtils.isEmpty(pickupHistories)) {
			return error("Bạn chưa đăng ký nhận container ra khỏi cảng, không có container khả dụng để bốc.");
		}

		// Get truck infor scan from entrance
		// if not exists => return error (not allow to gate in by app)
		PickupHistory pickupHistoryRef = pickupHistories.get(0);
		// Check if pickup history has truck no and chassis no
		if (StringUtils.isEmpty(pickupHistoryRef.getTruckNo()) || StringUtils.isEmpty(pickupHistoryRef.getChassisNo())) {
			return error("Thông tin đăng ký nhận container không có biển số xe đầu kéo hoặc rơ moóc. Quý khách vui lòng kiểm tra lại.");
		}
		TruckEntrance truckEntranceParam = new TruckEntrance();
		truckEntranceParam.setTruckNo(pickupHistoryRef.getTruckNo());
		truckEntranceParam.setChassisNo(pickupHistoryRef.getChassisNo());
		truckEntranceParam.setActive(true);
		params = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -23);
		params.put("expireTime", c.getTime());
		truckEntranceParam.setParams(params);
		List<TruckEntrance> truckEntrances = truckEntranceService.selectTruckEntranceList(truckEntranceParam);
		if (CollectionUtils.isEmpty(truckEntrances)) {
			return error("Không nhận diện được biển số xe qua cổng bảo vệ, không cho phép làm lệnh vào cổng trên app.");
		}

		// Get shipment detail id .
		
		// Result for driver to pick
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Get container available for pickup
		// Case 2 cont
		if (pickupHistories.size() >= 2) {
			PickupHistory pickup1 = pickupHistoryRef;
			PickupHistory pickup2 = pickupHistories.get(1);
			// Check if same sztp and bl no or booking no
			boolean same = false;
			if (pickup1.getServiceType() == pickup2.getServiceType()
					&& pickup1.getSztp().substring(0, 1).equals(pickup2.getSztp().substring(0, 1))) {
				if (EportConstants.SERVICE_PICKUP_FULL == pickup1.getServiceType()
						&& pickup1.getBlNo().equalsIgnoreCase(pickup2.getBlNo())) {
					same = true;
				}
				if (EportConstants.SERVICE_PICKUP_EMPTY == pickup1.getServiceType()
						&& pickup1.getBookingNo().equalsIgnoreCase(pickup2.getBookingNo())) {
					same = true;
				}
			}
			// Case same
			if (same) {
				// Get shipment detail map
				Map<String, ShipmentDetail> shipmentDetailMap = getShipmentDetailMap(pickup1.getShipmentId());

				ContainerInfoDto containerInfoDtoParam = new ContainerInfoDto();
				containerInfoDtoParam.setCntrState("Y"); // stacking
				Map<String, Object> paramsCont = new HashMap<>();
				paramsCont.put("sztp", pickup1.getSztp().substring(0, 1));
				// Case cont full -> bl no
				if (EportConstants.SERVICE_PICKUP_FULL == pickup1.getServiceType()) {
					containerInfoDtoParam.setBlNo(pickup1.getBlNo());
					containerInfoDtoParam.setFe("F");
				}
				// Case cont empty -> booking no
				if (EportConstants.SERVICE_PICKUP_EMPTY == pickup1.getServiceType()) {
					containerInfoDtoParam.setBookingNo(pickup1.getBookingNo());
					containerInfoDtoParam.setFe("E");
				}
				containerInfoDtoParam.setParams(paramsCont);
				// get list with position
				List<ContainerInfoDto> containerInfoDtos = catosApiService
						.getContainerInfoListByCondition(containerInfoDtoParam);
				if (CollectionUtils.isEmpty(containerInfoDtos)) {
					return error("Không tìm thấy container khả dụng để bốc cho lô " + pickup1.getShipmentId());
				}
				pickedContAvailableDtos
						.addAll(handlePositionDataContainer(containerInfoDtos, 2, shipmentDetailMap, pickup1.getId()));
			} else {
				// Case not same
				// pickup 1
				// Get shipment detail map
				Map<String, ShipmentDetail> shipmentDetailMap1 = getShipmentDetailMap(pickup1.getShipmentId());
				ContainerInfoDto containerInfoDtoParam = new ContainerInfoDto();
				containerInfoDtoParam.setCntrState("Y"); // stacking
				Map<String, Object> paramsCont = new HashMap<>();
				paramsCont.put("sztp", pickup1.getSztp().substring(0, 1));
				// Case cont full -> bl no
				if (EportConstants.SERVICE_PICKUP_FULL == pickup1.getServiceType()) {
					containerInfoDtoParam.setBlNo(pickup1.getBlNo());
					containerInfoDtoParam.setFe("F");
				}
				// Case cont empty -> booking no
				if (EportConstants.SERVICE_PICKUP_EMPTY == pickup1.getServiceType()) {
					containerInfoDtoParam.setBookingNo(pickup1.getBookingNo());
					containerInfoDtoParam.setFe("E");
				}
				containerInfoDtoParam.setParams(paramsCont);
				// get list with position
				List<ContainerInfoDto> containerInfoDtos = catosApiService
						.getContainerInfoListByCondition(containerInfoDtoParam);
				if (CollectionUtils.isEmpty(containerInfoDtos)) {
					return error("Không tìm thấy container khả dụng để bốc cho lô " + pickup1.getShipmentId());
				}
				pickedContAvailableDtos
						.addAll(handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap1, pickup1.getId()));

				// pickup 2
				// Get shipment detail map
				Map<String, ShipmentDetail> shipmentDetailMap2 = getShipmentDetailMap(pickup2.getShipmentId());
				containerInfoDtoParam = new ContainerInfoDto();
				containerInfoDtoParam.setCntrState("Y"); // stacking
				paramsCont = new HashMap<>();
				paramsCont.put("sztp", pickup2.getSztp().substring(0, 1));
				// Case cont full -> bl no
				if (EportConstants.SERVICE_PICKUP_FULL == pickup2.getServiceType()) {
					containerInfoDtoParam.setBlNo(pickup2.getBlNo());
					containerInfoDtoParam.setFe("F");
				}
				// Case cont empty -> booking no
				if (EportConstants.SERVICE_PICKUP_EMPTY == pickup2.getServiceType()) {
					containerInfoDtoParam.setBookingNo(pickup2.getBookingNo());
					containerInfoDtoParam.setFe("E");
				}
				containerInfoDtoParam.setParams(paramsCont);
				// get list with position
				containerInfoDtos = catosApiService.getContainerInfoListByCondition(containerInfoDtoParam);
				if (CollectionUtils.isEmpty(containerInfoDtos)) {
					return error("Không tìm thấy container khả dụng để bốc cho lô " + pickup2.getShipmentId());
				}
				pickedContAvailableDtos
						.addAll(handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap2, pickup2.getId()));
			}
		}

		// Case 1 cont
		if (pickupHistories.size() == 1) {
			// Get shipment detail map
			Map<String, ShipmentDetail> shipmentDetailMap = getShipmentDetailMap(pickupHistoryRef.getShipmentId());
			ContainerInfoDto containerInfoDtoParam = new ContainerInfoDto();
			containerInfoDtoParam.setCntrState("Y"); // stacking
			Map<String, Object> paramsCont = new HashMap<>();
			paramsCont.put("sztp", pickupHistoryRef.getSztp().substring(0, 1));
			// Case cont full -> bl no
			if (EportConstants.SERVICE_PICKUP_FULL == pickupHistoryRef.getServiceType()) {
				containerInfoDtoParam.setBlNo(pickupHistoryRef.getBlNo());
				containerInfoDtoParam.setFe("F");
			}
			// Case cont empty -> booking no
			if (EportConstants.SERVICE_PICKUP_EMPTY == pickupHistoryRef.getServiceType()) {
				containerInfoDtoParam.setBookingNo(pickupHistoryRef.getBookingNo());
				containerInfoDtoParam.setFe("E");
			}
			containerInfoDtoParam.setParams(paramsCont);
			// get list with position
			List<ContainerInfoDto> containerInfoDtos = catosApiService
					.getContainerInfoListByCondition(containerInfoDtoParam);
			if (CollectionUtils.isEmpty(containerInfoDtos)) {
				return error("Không tìm thấy container khả dụng để bốc cho lô " + pickupHistoryRef.getShipmentId());
			}
			pickedContAvailableDtos.addAll(
					handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap, pickupHistoryRef.getId()));
		}
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("listContAvailable", pickedContAvailableDtos);
		return ajaxResult;
	}

	private List<PickedContAvailableDto> handlePositionDataContainer(List<ContainerInfoDto> containerInfoDtos,
			int number, Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by block
		Collections.sort(containerInfoDtos, new BlockComparator());
		List<ContainerInfoDto> containerInfoDtosBlock = new ArrayList<>();
		String currentBlock = containerInfoDtos.get(0).getBlock();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentBlock.equalsIgnoreCase(containerInfoDto.getBlock())) {
				pickedContAvailableDtos.addAll(
						handlePositionByBay(containerInfoDtosBlock, number, shipmentDetailMap, pickupHistoryId));
				currentBlock = containerInfoDto.getBlock();
				containerInfoDtosBlock = new ArrayList<>();
			}
			containerInfoDtosBlock.add(containerInfoDto);
		}
		pickedContAvailableDtos
				.addAll(handlePositionByBay(containerInfoDtosBlock, number, shipmentDetailMap, pickupHistoryId));

		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> handlePositionByBay(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by bay
		Collections.sort(containerInfoDtos, new BayComparator());
		List<ContainerInfoDto> containerInfoDtosBay = new ArrayList<>();
		String currentBay = containerInfoDtos.get(0).getBay();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentBay.equalsIgnoreCase(containerInfoDto.getBlock())) {
				pickedContAvailableDtos
						.addAll(handlePositionBytier(containerInfoDtosBay, number, shipmentDetailMap, pickupHistoryId));
				currentBay = containerInfoDto.getBay();
				containerInfoDtosBay = new ArrayList<>();
			}
			containerInfoDtosBay.add(containerInfoDto);
		}
		pickedContAvailableDtos
				.addAll(handlePositionBytier(containerInfoDtosBay, number, shipmentDetailMap, pickupHistoryId));

		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> handlePositionBytier(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by tier
		Collections.sort(containerInfoDtos, new TierComparator());
		List<ContainerInfoDto> containerInfoDtosTier = new ArrayList<>();
		Integer currentTier = containerInfoDtos.get(0).getTier();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentTier.equals(containerInfoDto.getTier())) {
				pickedContAvailableDtos
						.addAll(addContainer(containerInfoDtosTier, number, shipmentDetailMap, pickupHistoryId));
				currentTier = containerInfoDto.getTier();
				containerInfoDtosTier = new ArrayList<>();
			}
			containerInfoDtosTier.add(containerInfoDto);
		}
		pickedContAvailableDtos.addAll(addContainer(containerInfoDtosTier, number, shipmentDetailMap, pickupHistoryId));
		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> addContainer(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			ContainerInfoDto containerInfoDto = containerInfoDtos.get(0);
			PickedContAvailableDto pickedContAvailableDto = new PickedContAvailableDto();
			pickedContAvailableDto.setContainerNo(containerInfoDto.getCntrNo());
			pickedContAvailableDto.setSztp(containerInfoDto.getSztp());
			ShipmentDetail shipmentDetail = shipmentDetailMap.get(containerInfoDto.getCntrNo());
			if (shipmentDetail != null) {
				pickedContAvailableDto.setShipmentDetailId(shipmentDetail.getId());
			}
			pickedContAvailableDto.setPickupHistoryId(pickupHistoryId);
			pickedContAvailableDto.setChecked(false);
			pickedContAvailableDto.setJobOrderNo(containerInfoDto.getJobOdrNo2());
			pickedContAvailableDto.setBlock(containerInfoDto.getBlock());
			pickedContAvailableDto.setBay(containerInfoDto.getBay());
			pickedContAvailableDto.setRow(containerInfoDto.getRoww());
			pickedContAvailableDto.setTier(containerInfoDto.getTier());
			pickedContAvailableDtos.add(pickedContAvailableDto);
		}
		if (number == 2 && containerInfoDtos.size() >= 2) {
			ContainerInfoDto containerInfoDto = containerInfoDtos.get(1);
			PickedContAvailableDto pickedContAvailableDto = new PickedContAvailableDto();
			pickedContAvailableDto.setContainerNo(containerInfoDto.getCntrNo());
			pickedContAvailableDto.setSztp(containerInfoDto.getSztp());
			ShipmentDetail shipmentDetail = shipmentDetailMap.get(containerInfoDto.getCntrNo());
			if (shipmentDetail != null) {
				pickedContAvailableDto.setShipmentDetailId(shipmentDetail.getId());
			}
			pickedContAvailableDto.setPickupHistoryId(pickupHistoryId);
			pickedContAvailableDto.setChecked(false);
			pickedContAvailableDto.setJobOrderNo(containerInfoDto.getJobOdrNo2());
			pickedContAvailableDto.setBlock(containerInfoDto.getBlock());
			pickedContAvailableDto.setBay(containerInfoDto.getBay());
			pickedContAvailableDto.setRow(containerInfoDto.getRoww());
			pickedContAvailableDto.setTier(containerInfoDto.getTier());
			pickedContAvailableDtos.add(pickedContAvailableDto);
		}
		return pickedContAvailableDtos;
	}

	// Compare container info dto by block
	class BlockComparator implements Comparator<ContainerInfoDto> {
		public int compare(ContainerInfoDto containerInfoDto1, ContainerInfoDto containerInfoDto2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return containerInfoDto1.getBlock().compareTo(containerInfoDto2.getBlock());
		}
	}

	// Compare container info dto by bay
	class BayComparator implements Comparator<ContainerInfoDto> {
		public int compare(ContainerInfoDto containerInfoDto1, ContainerInfoDto containerInfoDto2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return containerInfoDto1.getBay().compareTo(containerInfoDto2.getBay());
		}
	}

	// Compare container info dto by tier
	class TierComparator implements Comparator<ContainerInfoDto> {
		public int compare(ContainerInfoDto containerInfoDto1, ContainerInfoDto containerInfoDto2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return containerInfoDto1.getBay().compareTo(containerInfoDto2.getBay());
		}
	}

	private Map<String, ShipmentDetail> getShipmentDetailMap(Long shipmentId) {
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setShipmentId(shipmentId);
		shipmentDetailParam.setPaymentStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		Map<String, ShipmentDetail> shipmentDetailMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetailMap.put(shipmentDetail.getContainerNo(), shipmentDetail);
			}
		}
		return shipmentDetailMap;
	}
}
