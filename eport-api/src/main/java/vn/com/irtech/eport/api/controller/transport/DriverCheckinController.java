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
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.BusinessConsts;
import vn.com.irtech.eport.api.consts.MqttConsts;
import vn.com.irtech.eport.api.form.DriverRes;
import vn.com.irtech.eport.api.form.GateInFormData;
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
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.dto.PickedContAvailableDto;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.service.ISysDictDataService;
import vn.com.irtech.eport.system.service.ISysRobotService;

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
	private ISysDictDataService dictDataService;

	@Autowired
	private IProcessOrderService processOrderService;

	@Autowired
	private ISysRobotService robotService;

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
			pickupHistory.setJobOrderFlg(false);
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
		// Check loadable wgt
		// Get Pickup history
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(driverId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		Map<String, Object> params = new HashMap<>();
		params.put("params.serviceTypes",
				Convert.toStrArray(EportConstants.SERVICE_PICKUP_FULL + "," + EportConstants.SERVICE_PICKUP_EMPTY));
		pickupHistoryParam.setParams(params);
		List<PickupHistory> pickupHistories = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);

		if (CollectionUtils.isEmpty(pickupHistories)) {
			String message = "Quý khách chưa đăng ký vận chuyển container ra/vào cảng.";
			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
					message);
			throw new BusinessException(message);
		}

		Integer loadableWgt = pickupHistories.get(0).getLoadableWgt();
		Integer containerWgt = 0;
		for (PickupHistory pickupHistory : pickupHistories) {
			ContainerInfoDto containerInfoDtoParam = new ContainerInfoDto();
			containerInfoDtoParam.setCntrState("Y"); // stacking
			containerInfoDtoParam.setCntrNo(pickupHistory.getContainerNo());
			if (EportConstants.SERVICE_PICKUP_FULL == pickupHistory.getServiceType()) {
				containerInfoDtoParam.setFe("F");
				containerInfoDtoParam.setBlNo(pickupHistory.getBlNo());
			} else if (EportConstants.SERVICE_PICKUP_EMPTY == pickupHistory.getServiceType()) {
				containerInfoDtoParam.setFe("E");
				containerInfoDtoParam.setBookingNo(pickupHistory.getBookingNo());
			}

			Map<String, Object> contParams = new HashMap<>();
			containerInfoDtoParam.setParams(contParams);
			// get list with position
			List<ContainerInfoDto> containerInfoDtos = catosApiService
					.getContainerInfoListByCondition(containerInfoDtoParam);
			if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
				ContainerInfoDto containerInfoDto = containerInfoDtos.get(0);
				if (containerInfoDto.getWgt() != null) {
					containerWgt += containerInfoDto.getWgt().intValue();
				}
			}
		}

		if (loadableWgt < containerWgt) {
			String message = "Trọng tải của rơ moóc (" + loadableWgt + ") không đủ để chở container với trọng lượng "
					+ containerWgt;
			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
					message);
			throw new BusinessException(message);
		}

		// Check hạn đăng kiểm
		// Truck no
		Integer truckWgt = 0;
		PickupHistory pickupHistoryRef = pickupHistories.get(0);
		LogisticTruck logisticTruckParam = new LogisticTruck();
		logisticTruckParam.setPlateNumber(pickupHistoryRef.getTruckNo());
		logisticTruckParam.setLogisticGroupId(pickupHistoryRef.getLogisticGroupId());
		List<LogisticTruck> logisticTrucks = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
		if (CollectionUtils.isEmpty(logisticTrucks)) {
			String message = "Biển số xe " + pickupHistoryRef.getTruckNo() + " không tồn tại, vui lòng kiểm tra lại.";
			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
					message);
			throw new BusinessException(message);
		}
		Date registryDate = logisticTrucks.get(0).getRegistryExpiryDate();
		Integer wgt = logisticTrucks.get(0).getSelfWgt();
		if (wgt != null) {
			truckWgt += wgt;
		}
		if (registryDate != null) {
			registryDate.setHours(23);
			registryDate.setMinutes(59);
			registryDate.setSeconds(59);
			if (registryDate == null || registryDate.getTime() < new Date().getTime()) {
				String message = "Hạn đăng kiểm đăng ký cho xe " + pickupHistoryRef.getTruckNo()
						+ " đã hết hạn hoặc không tồn tại, vui lòng kiểm tra lại.";
				mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
						message);
				throw new BusinessException(message);
			}
		}

		// Chassis no
		logisticTruckParam = new LogisticTruck();
		logisticTruckParam.setPlateNumber(pickupHistoryRef.getChassisNo());
		logisticTrucks = logisticTruckService.selectLogisticTruckList(logisticTruckParam);
		if (CollectionUtils.isEmpty(logisticTrucks)) {
			String message = "Biển số xe " + pickupHistoryRef.getTruckNo() + " không tồn tại, vui lòng kiểm tra lại.";
			mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
					message);
			throw new BusinessException(message);
		}
		registryDate = logisticTrucks.get(0).getRegistryExpiryDate();
		wgt = logisticTrucks.get(0).getSelfWgt();
		if (wgt != null) {
			truckWgt += wgt;
		}
		if (registryDate != null) {
			registryDate.setHours(23);
			registryDate.setMinutes(59);
			registryDate.setSeconds(59);
			if (registryDate == null || registryDate.getTime() < new Date().getTime()) {
				String message = "Hạn đăng kiểm đăng ký cho xe " + pickupHistoryRef.getChassisNo()
						+ " đã hết hạn hoặc không tồn tại, vui lòng kiểm tra lại.";
				mqttService.sendNotificationOfProcessForDriver(BusinessConsts.FINISH, BusinessConsts.FAIL, sessionId,
						message);
				throw new BusinessException(message);
			}
		}

		try {
			GateInFormData gateInFormData = new GateInFormData();
			// Set type gate in form data is beginning
			gateInFormData.setType(EportConstants.GATE_REQ_TYPE_BEGIN);
			List<PickupHistory> pickupOut = new ArrayList<>();
			String containerNos = "";

			for (PickupHistory pickupHistory : pickupHistories) {
				pickupOut.add(pickupHistory);
				containerNos += pickupHistory.getContainerNo() + ",";
			}

			containerNos = containerNos.substring(0, containerNos.length() - 1);

			if (CollectionUtils.isNotEmpty(pickupOut)) {
				ProcessOrder processOrder = new ProcessOrder();
				processOrder.setServiceType(EportConstants.SERVICE_GATE_OUT);
				processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_NEW);
				processOrderService.insertProcessOrder(processOrder);

				gateInFormData.setGateId("gate1");
				gateInFormData.setId(processOrder.getId());
				gateInFormData.setPickupOut(pickupOut);
				gateInFormData.setModule("OUT");
				gateInFormData.setContNumberOut(pickupOut.size());

				gateInFormData.setGatePass(pickupHistoryRef.getGatePass());
				gateInFormData.setTruckNo("." + pickupHistoryRef.getTruckNo());
				gateInFormData.setChassisNo(pickupHistoryRef.getChassisNo());
				if (StringUtils.isEmpty(gateInFormData.getChassisNo())) {
					gateInFormData.setChassisNo("");
				}
				gateInFormData.setSessionId(sessionId);
				gateInFormData.setWgt("333333");
				gateInFormData.setGateId("gate1");
				gateInFormData.setReceiptId(processOrder.getId());
				gateInFormData.setIsPrint(false);

				String msg = new Gson().toJson(gateInFormData);
				SysRobot robot = new SysRobot();
				robot.setStatus(EportConstants.ROBOT_STATUS_AVAILABLE);
				robot.setIsGateOutOrder(true);
				robot.setDisabled(false);
				SysRobot sysRobot = robotService.findFirstRobot(robot);
				if (sysRobot != null) {
					processOrder.setStatus(EportConstants.PROCESS_ORDER_STATUS_PROCESSING);
					processOrder.setRobotUuid(sysRobot.getUuId());
					processOrder.setProcessData(msg);
					processOrderService.updateProcessOrder(processOrder);
					robotService.updateRobotStatusByUuId(sysRobot.getUuId(), EportConstants.ROBOT_STATUS_BUSY);
					logger.debug("Send request to robot: " + sysRobot.getUuId() + ", content: " + msg);
					mqttService.sendMessageToRobot(msg, sysRobot.getUuId());

					// Send accepted processing notification for driver
					DriverRes driverRes = new DriverRes();
					driverRes.setStatus(BusinessConsts.IN_PROGRESS);
					driverRes.setResult(BusinessConsts.BLANK);
					driverRes.setMsg("Đang thực hiện làm lệnh gate in");
					String msgDriver = new Gson().toJson(driverRes);
					mqttService.publish(MqttConsts.DRIVER_RES_TOPIC.replace("+", sessionId),
							new MqttMessage(msgDriver.getBytes()));
				} else {
					logger.debug("No GateRobot is available: " + msg);
				}
			}
		} catch (Exception e) {
			logger.error("Error when send order gate in: " + e);
		}
	}

	@GetMapping("/cont-available")
	public AjaxResult getListContainerAvailable() {
		Long driverId = SecurityUtils.getCurrentUser().getUser().getUserId();
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setDriverId(driverId);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		Map<String, Object> params = new HashMap<>();
		params.put("serviceTypes",
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
						.addAll(handlePositionDataContainer(containerInfoDtos, 2, shipmentDetailMap, pickup1.getId(),
								pickup1.getJobOrderNo()));
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
						.addAll(handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap1, pickup1.getId(),
								pickup1.getJobOrderNo()));

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
						.addAll(handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap2, pickup2.getId(),
								pickup2.getJobOrderNo()));
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
					handlePositionDataContainer(containerInfoDtos, 1, shipmentDetailMap, pickupHistoryRef.getId(),
							pickupHistoryRef.getJobOrderNo()));
		}

		if (CollectionUtils.isNotEmpty(pickedContAvailableDtos)
				&& pickedContAvailableDtos.size() >= pickupHistories.size()) {
			pickedContAvailableDtos.get(0).setChecked(true);
			if (pickupHistories.size() >= 2) {
				pickedContAvailableDtos.get(1).setChecked(true);
			}
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("listContAvailable", pickedContAvailableDtos);
		return ajaxResult;
	}

	private List<PickedContAvailableDto> handlePositionDataContainer(List<ContainerInfoDto> containerInfoDtos,
			int number, Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId, String jobOrderNo) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by block
		Collections.sort(containerInfoDtos, new BlockComparator());
		List<ContainerInfoDto> containerInfoDtosBlock = new ArrayList<>();
		String currentBlock = containerInfoDtos.get(0).getBlock();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentBlock.equalsIgnoreCase(containerInfoDto.getBlock())) {
				pickedContAvailableDtos.addAll(
						handlePositionByBay(containerInfoDtosBlock, number, shipmentDetailMap, pickupHistoryId,
								jobOrderNo));
				currentBlock = containerInfoDto.getBlock();
				containerInfoDtosBlock = new ArrayList<>();
			}
			containerInfoDtosBlock.add(containerInfoDto);
		}
		pickedContAvailableDtos
				.addAll(handlePositionByBay(containerInfoDtosBlock, number, shipmentDetailMap, pickupHistoryId,
						jobOrderNo));

		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> handlePositionByBay(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId, String jobOrderNo) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by bay
		Collections.sort(containerInfoDtos, new BayComparator());
		String rulePick = dictDataService.selectDictLabel("rule_pickup_cont_block",
				containerInfoDtos.get(0).getBlock());
		// rule 1: rtg (pick from above) rule 2: rictaker (pick from above from
		// right->left rule 3: rictaker left->right)
		int ruleId = 1; // default rtg
		if (StringUtils.isNotEmpty(rulePick) && rulePick.contains("RICTAKER")) {
			if (rulePick.contains("LR")) {
				ruleId = 3;
			} else if (rulePick.contains("RL")) {
				ruleId = 2;
			}
		}

		List<ContainerInfoDto> containerInfoDtosBay = new ArrayList<>();
		String currentBay = containerInfoDtos.get(0).getBay();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentBay.equalsIgnoreCase(containerInfoDto.getBay())) {
				pickedContAvailableDtos
						.addAll(handlePositionBytier(containerInfoDtosBay, number, shipmentDetailMap, pickupHistoryId,
								ruleId, jobOrderNo));
				currentBay = containerInfoDto.getBay();
				containerInfoDtosBay = new ArrayList<>();
			}
			containerInfoDtosBay.add(containerInfoDto);
		}
		pickedContAvailableDtos
				.addAll(handlePositionBytier(containerInfoDtosBay, number, shipmentDetailMap, pickupHistoryId, ruleId,
						jobOrderNo));

		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> handlePositionBytier(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId, int ruleId, String jobOrderNo) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by roww
		Collections.sort(containerInfoDtos, new RowwComparator());
		List<ContainerInfoDto> containerInfoDtosTier = new ArrayList<>();
		Integer currentRow = containerInfoDtos.get(0).getRoww();
		for (ContainerInfoDto containerInfoDto : containerInfoDtos) {
			if (!currentRow.equals(containerInfoDto.getRoww())) {
				pickedContAvailableDtos
						.addAll(addContainer(containerInfoDtosTier, number, shipmentDetailMap, pickupHistoryId,
								jobOrderNo));
				currentRow = containerInfoDto.getRoww();
				containerInfoDtosTier = new ArrayList<>();
			}
			containerInfoDtosTier.add(containerInfoDto);
		}
		pickedContAvailableDtos
				.addAll(addContainer(containerInfoDtosTier, number, shipmentDetailMap, pickupHistoryId, jobOrderNo));
		if (pickedContAvailableDtos.size() < number) {
			number--;
		}
		if (ruleId == 3) {
			return pickedContAvailableDtos.subList(0, number);
		}
		if (ruleId == 2) {
			return pickedContAvailableDtos.subList(pickedContAvailableDtos.size() - number,
					pickedContAvailableDtos.size());
		}
		return pickedContAvailableDtos;
	}

	private List<PickedContAvailableDto> addContainer(List<ContainerInfoDto> containerInfoDtos, int number,
			Map<String, ShipmentDetail> shipmentDetailMap, Long pickupHistoryId, String jobOrderNo) {
		List<PickedContAvailableDto> pickedContAvailableDtos = new ArrayList<>();
		// Sort container info by tier
		Collections.sort(containerInfoDtos, new TierComparator());
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
			pickedContAvailableDto.setJobOrderNo(jobOrderNo);
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
			pickedContAvailableDto.setJobOrderNo(jobOrderNo);
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
	class RowwComparator implements Comparator<ContainerInfoDto> {
		public int compare(ContainerInfoDto containerInfoDto1, ContainerInfoDto containerInfoDto2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return containerInfoDto1.getRoww().compareTo(containerInfoDto2.getRoww());
		}
	}

	// Compare container info dto by tier
	class TierComparator implements Comparator<ContainerInfoDto> {
		public int compare(ContainerInfoDto containerInfoDto1, ContainerInfoDto containerInfoDto2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return containerInfoDto1.getTier().compareTo(containerInfoDto2.getTier()) * -1;
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
