package vn.com.irtech.eport.api.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.form.GateNotificationCheckInReq;
import vn.com.irtech.eport.api.form.RfidTruckInfoRes;
import vn.com.irtech.eport.api.mqtt.service.MqttService;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.RfidTruck;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IRfidTruckService;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.dto.NotificationReq;
import vn.com.irtech.eport.system.service.ISysConfigService;

@RestController
@RequestMapping("/admin")
public class AdminGateController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminGateController.class);

	public static final String NOTIFICATION_MC_TOPIC = "eport/notification/mc";

	@Autowired
	private IGateDetectionService gateDetectionService;

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IRfidTruckService rfidTruckService;

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private MqttService mqttService;

	@Autowired
	private ISysConfigService sysConfigService;

	@Autowired
	private ITruckEntranceService truckEntranceService;

	@PostMapping("/pickup/yard-position")
	public AjaxResult updateYardPosition(@RequestBody List<PickupHistory> pickupHistories) {
		if (CollectionUtils.isEmpty(pickupHistories)) {
			return error("Không tìm thấy dữ liệu.");
		}
		for (PickupHistory pickupHistory : pickupHistories) {
			if (pickupHistory.getId() == null) {
				return error("id không được để trống.");
			}
			
			String position ="";
			if (StringUtils.isNotEmpty(pickupHistory.getArea())) {
				position = pickupHistory.getArea();
			} else {
				position = pickupHistory.getBlock() + "-" + pickupHistory.getBay() + "-" + pickupHistory.getLine() + "-" + pickupHistory.getTier();
			}
			
			// Decode id
			// To differentiate with case real pickup history : 0,
			// gate detection container 1 : 1, gate detection container 2 : 2.
			Long postFixId = pickupHistory.getId() % 10;
			Long realId = pickupHistory.getId() / 10;
			if (postFixId == 0) {
				// Case real pickup history
				PickupHistory pickupHistoryOrigin = pickupHistoryService.selectPickupHistoryById(realId);
				if (pickupHistoryOrigin == null) {
					return error("id không tồn tại.");
				}
				pickupHistoryOrigin.setBlock(pickupHistory.getBlock());
				pickupHistoryOrigin.setBay(pickupHistory.getBay());
				pickupHistoryOrigin.setLine(pickupHistory.getLine());
				pickupHistoryOrigin.setTier(pickupHistory.getTier());
				pickupHistoryOrigin.setArea(pickupHistory.getArea());
				pickupHistoryService.updatePickupHistory(pickupHistoryOrigin);
			} else if (postFixId == 1) {
				// Case gate detection container 1
				GateDetection gateDetectionUpdate = new GateDetection();
				gateDetectionUpdate.setId(realId);
				gateDetectionUpdate.setLocation1(position);
				gateDetectionService.updateGateDetection(gateDetectionUpdate);
			} else if (postFixId == 2) {
				// Case gate detection container 2
				GateDetection gateDetectionUpdate = new GateDetection();
				gateDetectionUpdate.setId(realId);
				gateDetectionUpdate.setLocation2(position);
				gateDetectionService.updateGateDetection(gateDetectionUpdate);
			} else if (postFixId == 3) {
				// Case pre request position container 1
				PickupHistory pickupHistoryUpdate = new PickupHistory();
				pickupHistoryUpdate.setId(realId);
				pickupHistoryUpdate.setBlock(pickupHistory.getBlock());
				pickupHistoryUpdate.setBay(pickupHistory.getBay());
				pickupHistoryUpdate.setLine(pickupHistory.getLine());
				pickupHistoryUpdate.setTier(pickupHistory.getTier());
				pickupHistoryUpdate.setArea(pickupHistory.getArea());
				pickupHistoryService.updatePickupHistory(pickupHistoryUpdate);
			}
		}
		return success();
	}

	@PostMapping("/rfid/truck-info")
	public AjaxResult getTruckInfoByRfid(@RequestBody List<String> rfids) {
		if (CollectionUtils.isEmpty(rfids)) {
			throw new BusinessException("Thông tin rfid không được trống.");
		}

		RfidTruckInfoRes rfidTruckInfoRes = new RfidTruckInfoRes();
		// Deduct
		Long deduct = 0L;

		for (String rfid : rfids) {
			// Get info plate number from rfid
			RfidTruck rfidTruckParam = new RfidTruck();
			rfidTruckParam.setRfid(rfid);
			rfidTruckParam.setDisabled(false);
			List<RfidTruck> rfidTruckNos = rfidTruckService.selectRfidTruckList(rfidTruckParam);
			if (CollectionUtils.isNotEmpty(rfidTruckNos)) {
				RfidTruck rfidTruck = rfidTruckNos.get(0);
				// Truck no
				if ("T".equalsIgnoreCase(rfidTruck.getTruckType())) {
					rfidTruckInfoRes.setTruckNo(rfidTruck.getPlateNumber());
					rfidTruckInfoRes.setGatePass(rfidTruck.getGatePass());
					if (rfidTruck.getWgt() != null) {
						rfidTruckInfoRes.setTruckNoWgt(rfidTruck.getWgt());
						deduct += rfidTruck.getWgt();
					}
					// Chassis no
				} else if ("C".equalsIgnoreCase(rfidTruck.getTruckType())) {
					rfidTruckInfoRes.setChassisNo(rfidTruck.getPlateNumber());
					if (rfidTruck.getWgt() != null) {
						rfidTruckInfoRes.setChassisNoWgt(rfidTruck.getWgt());
						deduct += rfidTruck.getWgt();
					}
				}
			}
		}

		rfidTruckInfoRes.setDeduct(deduct);

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("truckInfo", rfidTruckInfoRes);

		// Put in cache
		CacheUtils.put("rfidTruckInfo", rfidTruckInfoRes);

		return ajaxResult;
	}

	@PostMapping("/seal-no")
	public AjaxResult getSealNo(@RequestBody GateNotificationCheckInReq gateNotificationCheckInReq) {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("data", getContInfo(gateNotificationCheckInReq));
		return ajaxResult;
	}

	/**
	 * Get container info from catos and set to gate notification check in req
	 * object
	 * 
	 * @param gateNotificationCheckInReq
	 * @return
	 */
	private GateNotificationCheckInReq getContInfo(GateNotificationCheckInReq gateNotificationCheckInReq) {
		String containerNos = "";
		if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getContainerSend1())) {
			containerNos += gateNotificationCheckInReq.getContainerSend1();
		}
		if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getContainerSend2())) {
			containerNos += "," + gateNotificationCheckInReq.getContainerSend2();
		}
		Map<String, ContainerInfoDto> contMap = getContainerInfoMap(containerNos);
		if (StringUtils.isEmpty(contMap)) {
			return gateNotificationCheckInReq;
		}

		if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getContainerSend1())) {
			ContainerInfoDto cntrInfo1 = contMap.get(gateNotificationCheckInReq.getContainerSend1());
			if (cntrInfo1 != null) {
				gateNotificationCheckInReq.setSendFE1(cntrInfo1.getFe());
				gateNotificationCheckInReq.setSendSztp1(cntrInfo1.getSztp());
				gateNotificationCheckInReq.setSendRemark1(cntrInfo1.getRemark());
				gateNotificationCheckInReq.setSealNo1(cntrInfo1.getSealNo3());
			}
		}

		if (StringUtils.isNotEmpty(gateNotificationCheckInReq.getContainerSend2())) {
			ContainerInfoDto cntrInfo2 = contMap.get(gateNotificationCheckInReq.getContainerSend2());
			if (cntrInfo2 != null) {
				gateNotificationCheckInReq.setSendFE2(cntrInfo2.getFe());
				gateNotificationCheckInReq.setSendSztp2(cntrInfo2.getSztp());
				gateNotificationCheckInReq.setSendRemark2(cntrInfo2.getRemark());
				gateNotificationCheckInReq.setSealNo2(cntrInfo2.getSealNo3());
			}
		}
		return gateNotificationCheckInReq;
	}

	/**
	 * Get list container info from catos and convert to cont map object with key is
	 * container no and value is containre info dto
	 * 
	 * @param containerNos
	 * @return
	 */
	private Map<String, ContainerInfoDto> getContainerInfoMap(String containerNos) {
		List<ContainerInfoDto> containerInfoDtos = catosApiService.getAllContainerInfoDtoByContNos(containerNos);
		Map<String, ContainerInfoDto> contMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			for (ContainerInfoDto cntrInfo : containerInfoDtos) {
				if (!EportConstants.CATOS_CONT_DELIVERED.equalsIgnoreCase(cntrInfo.getCntrState())
						&& !EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(cntrInfo.getCntrState())
						&& StringUtils.isNotEmpty(cntrInfo.getJobOdrNo())) {
					contMap.put(cntrInfo.getCntrNo(), cntrInfo);
				}
			}
		}
		return contMap;
	}

	@PostMapping("/rfid/entrance")
	public AjaxResult getRfidFromEntrance(@RequestBody List<String> rfids) {
		if (CollectionUtils.isEmpty(rfids)) {
			throw new BusinessException("Thông tin rfid không được trống.");
		}

		String truckNo = "";
		String chassisNo = "";
		Long logisticId = null;
		String rfidss = "";
		Long truckWgt = null;
		Long chassisWgt = null;
		Long loadableWgt = null;
		// Get truck no, chassis no, logistic id from rfid
		for (String rfid : rfids) {
			rfidss += rfid + ",";
			// Get info plate number from rfid
			RfidTruck rfidTruckParam = new RfidTruck();
			rfidTruckParam.setRfid(rfid);
			rfidTruckParam.setDisabled(false);
			List<RfidTruck> rfidTruckNos = rfidTruckService.selectRfidTruckList(rfidTruckParam);
			if (CollectionUtils.isNotEmpty(rfidTruckNos)) {
				RfidTruck rfidTruck = rfidTruckNos.get(0);
				logisticId = rfidTruck.getLogisticGroupId();
				// Truck no
				if ("T".equalsIgnoreCase(rfidTruck.getTruckType())) {
					truckNo = rfidTruck.getPlateNumber();
					truckWgt = rfidTruck.getWgt();
					loadableWgt = rfidTruck.getLoadableWgt();
				} else if ("C".equalsIgnoreCase(rfidTruck.getTruckType())) {
					// Chassis no
					chassisNo = rfidTruck.getPlateNumber();
					chassisWgt = rfidTruck.getWgt();
				}
			}
		}
		
		if (chassisWgt == null) {
			chassisWgt = 0L;
		}
		if (truckWgt == null) {
			truckWgt = 0L;
		}
		
		TruckEntrance truckEntrance = new TruckEntrance();
		truckEntrance.setActive(true);
		truckEntrance.setTruckNo(truckNo);
		truckEntrance.setChassisNo(chassisNo);
		truckEntrance.setCreateBy(EportConstants.USER_NAME_SYSTEM);
		truckEntrance.setWgt(truckWgt + chassisWgt);
		truckEntrance.setLoadableWgt(loadableWgt);
		truckEntrance.setLogisticGroupId(logisticId);
		if (StringUtils.isNotEmpty(rfidss)) {
			truckEntrance.setRfid(rfidss.substring(0, rfidss.length() - 1));
		}
		truckEntranceService.insertTruckEntrance(truckEntrance);

		// Search for pickup history available in eport
		PickupHistory pickupHistoryParam = new PickupHistory();
		pickupHistoryParam.setTruckNo(truckNo);
		pickupHistoryParam.setChassisNo(chassisNo);
		pickupHistoryParam.setLogisticGroupId(logisticId);
		pickupHistoryParam.setEntranceScan(false);
		pickupHistoryParam.setStatus(EportConstants.PICKUP_HISTORY_STATUS_WAITING);
		List<PickupHistory> pickupHistoriesTemp = pickupHistoryService.selectPickupHistoryList(pickupHistoryParam);
		List<PickupHistory> pickupHistories = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(pickupHistoriesTemp)) {
			for (PickupHistory pickupHistory : pickupHistoriesTemp) {
				if (EportConstants.SERVICE_DROP_EMPTY == pickupHistory.getServiceType()
						|| EportConstants.SERVICE_DROP_FULL == pickupHistory.getServiceType()) {
					pickupHistories.add(pickupHistory);
				}
				pickupHistory.setEntranceScan(true);
				pickupHistoryService.updatePickupHistory(pickupHistory);
			}
		}

		// If exists pickup history -> get container stacking in catos
		// -> request to mc
		if (CollectionUtils.isNotEmpty(pickupHistories)) {
			Map<String, ContainerInfoDto> mapContInfo = getContainerNoFromPickupAssignList(pickupHistories);
			List<PickupHistory> pickupHistoriesRequest = new ArrayList<>();
			if (mapContInfo != null) {
				for (PickupHistory pickupHistory : pickupHistories) {
					if (mapContInfo.get(pickupHistory.getContainerNo()) != null) {
						pickupHistoriesRequest.add(pickupHistory);
					} else {
						pickupHistory.setStatus(EportConstants.PICKUP_HISTORY_STATUS_FINISH);
						pickupHistoryService.updatePickupHistory(pickupHistory);
					}
				}
			}

			if (CollectionUtils.isNotEmpty(pickupHistoriesRequest)) {
				boolean sameVessel = false;
				Long id1 = pickupHistoriesRequest.get(0).getId();
				Long id2 = null;
				ContainerInfoDto cont1 = mapContInfo.get(pickupHistoriesRequest.get(0).getContainerNo());
				ContainerInfoDto cont2 = null;

				if (pickupHistoriesRequest.size() >= 2) {
					id2 = pickupHistoriesRequest.get(1).getId();
					cont2 = mapContInfo.get(pickupHistoriesRequest.get(1).getContainerNo());
				}

				if (cont2 != null && cont1.getVslCd().equalsIgnoreCase(cont2.getVslCd())
						&& cont1.getCallSeq().equalsIgnoreCase(cont2.getCallSeq())) {
					sameVessel = true;
				}

				if (sameVessel) {
					// Send 2 container to mc
					NotificationReq notificationReq = new NotificationReq();
					List<Map<String, Object>> list = new ArrayList<>();
					notificationReq.setTitle("ePort: Cấp tọa độ cho xe ở ngoài cổng.");
					notificationReq.setMsg("Có xe hạ container ngoài cổng cần cấp tọa độ: " + cont1.getPtnrCode()
							+ " - " + cont1.getCntrNo() + " - " + cont2.getCntrNo() + " - " + cont1.getSztp() + " - "
							+ cont1.getVslCd() + cont1.getCallSeq() + " - " + cont1.getPol());
					notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
					notificationReq.setLink(
							sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
					notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
					// Container 1
					Map<String, Object> data = new HashMap<>();
					// To differentiate with another pickup history
					// => Add post fix 3 with container 1
					data.put("id", id1 + "3");
					data.put("opeCode", cont1.getPtnrCode());
					data.put("containerNo", cont1.getCntrNo());
					data.put("wgt", cont1.getWgt());
					data.put("sztp", cont1.getSztp());
					data.put("userVoy", cont1.getVslCd() + cont1.getCallSeq());
					data.put("dischargePort", cont1.getPol());
					data.put("cargoType", cont1.getCargoType());
					data.put("fe", cont1.getFe());
					list.add(data);
					// Container 2
					data = new HashMap<>();
					// To differentiate with another pickup history
					// => Add post fix 4 with container 2
					data.put("id", id2 + "3");
					data.put("opeCode", cont2.getPtnrCode());
					data.put("containerNo", cont2.getCntrNo());
					data.put("wgt", cont2.getWgt());
					data.put("sztp", cont2.getSztp());
					data.put("userVoy", cont2.getVslCd() + cont2.getCallSeq());
					data.put("dischargePort", cont2.getPol());
					data.put("cargoType", cont2.getCargoType());
					data.put("fe", cont2.getFe());
					list.add(data);
					notificationReq.setData(list);
					String req = new Gson().toJson(notificationReq);
					try {
						mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
					} catch (MqttException e) {
						logger.error("Error when send req yard position for mc: " + req);
					}
				} else {
					// cont 1
					NotificationReq notificationReq = new NotificationReq();
					List<Map<String, Object>> list = new ArrayList<>();
					notificationReq.setTitle("ePort: Cấp tọa độ cho xe ở ngoài cổng.");
					notificationReq.setMsg("Có xe hạ container ngoài cổng cần cấp tọa độ: " + cont1.getPtnrCode()
							+ " - " + cont1.getCntrNo() + " - " + cont1.getSztp() + " - " + cont1.getVslCd()
							+ cont1.getCallSeq() + " - " + cont1.getPod());
					notificationReq.setType(EportConstants.APP_USER_TYPE_MC);
					notificationReq.setLink(
							sysConfigService.selectConfigByKey("domain.admin.name") + EportConstants.URL_POSITION_MC);
					notificationReq.setPriority(EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
					// Container 1
					Map<String, Object> data = new HashMap<>();
					// To differentiate with another pickup history
					// => Add post fix 1 with container 1
					data.put("id", id1 + "3");
					data.put("opeCode", cont1.getPtnrCode());
					data.put("containerNo", cont1.getCntrNo());
					data.put("wgt", cont1.getWgt());
					data.put("sztp", cont1.getSztp());
					data.put("userVoy", cont1.getVslCd() + cont1.getCallSeq());
					data.put("dischargePort", cont1.getPod());
					data.put("cargoType", cont1.getCargoType());
					data.put("fe", cont1.getFe());
					list.add(data);
					notificationReq.setData(list);
					String req = new Gson().toJson(notificationReq);
					try {
						mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req.getBytes()));
					} catch (MqttException e) {
						logger.error("Error when send req yard position for mc: " + req);
					}

					// cont 2 if exist
					if (cont2 != null) {
						NotificationReq notificationReq2 = new NotificationReq();
						List<Map<String, Object>> list2 = new ArrayList<>();
						notificationReq2.setTitle("ePort: Cấp tọa độ cho xe ở ngoài cổng.");
						notificationReq2.setMsg("Có xe hạ container ngoài cổng cần cấp tọa độ: " + cont2.getPtnrCode()
								+ " - " + cont2.getCntrNo() + " - " + cont2.getSztp() + " - " + cont2.getVslCd()
								+ cont2.getCallSeq() + " - " + cont2.getPod());
						notificationReq2.setType(EportConstants.APP_USER_TYPE_MC);
						notificationReq2.setLink(sysConfigService.selectConfigByKey("domain.admin.name")
								+ EportConstants.URL_POSITION_MC);
						notificationReq2.setPriority(EportConstants.NOTIFICATION_PRIORITY_MEDIUM);
						// Container 1
						Map<String, Object> data2 = new HashMap<>();
						// To differentiate with another pickup history
						// => Add post fix 2 with container 2
						data2.put("id", id2 + "3");
						data2.put("opeCode", cont2.getPtnrCode());
						data2.put("containerNo", cont2.getCntrNo());
						data2.put("wgt", cont2.getWgt());
						data2.put("sztp", cont2.getSztp());
						data2.put("userVoy", cont2.getVslCd() + cont2.getCallSeq());
						data2.put("dischargePort", cont2.getPod());
						data2.put("cargoType", cont2.getCargoType());
						data2.put("fe", cont2.getFe());
						list2.add(data2);
						notificationReq2.setData(list2);
						String req2 = new Gson().toJson(notificationReq2);
						try {
							mqttService.publish(NOTIFICATION_MC_TOPIC, new MqttMessage(req2.getBytes()));
						} catch (MqttException e) {
							logger.error("Error when send req yard position for mc: " + req2);
						}
					}
				}
			}
		}

		return success();
	}

	private Map<String, ContainerInfoDto> getContainerNoFromPickupAssignList(List<PickupHistory> pickupHistories) {
		String containerNos = "";
		for (PickupHistory pickupHistory : pickupHistories) {
			containerNos += pickupHistory.getContainerNo() + ",";
		}
		// Get list container info from catos
		List<ContainerInfoDto> containerInfoDtos = catosApiService
				.getAllContainerInfoDtoByContNos(containerNos.substring(0, containerNos.length() - 1));
		Map<String, ContainerInfoDto> contMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(containerInfoDtos)) {
			// Mapping container info object to map data
			for (ContainerInfoDto cntrInfo : containerInfoDtos) {
				if (EportConstants.CATOS_CONT_STACKING.equalsIgnoreCase(cntrInfo.getCntrState())
						&& StringUtils.isNotEmpty(cntrInfo.getJobOdrNo())) {
					contMap.put(cntrInfo.getCntrNo(), cntrInfo);
				}
			}
		}
		return contMap;
	}
}
