package vn.com.irtech.eport.api.controller.admin;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.form.RfidTruckInfoRes;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.RfidTruck;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IRfidTruckService;

@RestController
@RequestMapping("/admin")
public class AdminOmController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminOmController.class);

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private IGateDetectionService gateDetectionService;

	@Autowired
	private IRfidTruckService rfidTruckService;

	@PostMapping("/pickup/yard-position")
	public AjaxResult updateYardPosition(@RequestBody List<PickupHistory> pickupHistories) {
		if (CollectionUtils.isEmpty(pickupHistories)) {
			return error("Không tìm thấy dữ liệu.");
		}
		for (PickupHistory pickupHistory : pickupHistories) {
			if (pickupHistory.getId() == null) {
				return error("id không được để trống.");
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
				if (StringUtils.isNotEmpty(pickupHistory.getArea())) {
					gateDetectionUpdate.setLocation1(pickupHistory.getArea());
				} else {
					gateDetectionUpdate.setLocation1(pickupHistory.getBlock() + "-" + pickupHistory.getBay() + "-"
							+ pickupHistory.getLine() + "-" + pickupHistory.getTier());
				}
				gateDetectionService.updateGateDetection(gateDetectionUpdate);
			} else if (postFixId == 2) {
				// Case gate detection container 2
				GateDetection gateDetectionUpdate = new GateDetection();
				gateDetectionUpdate.setId(realId);
				if (StringUtils.isNotEmpty(pickupHistory.getArea())) {
					gateDetectionUpdate.setLocation2(pickupHistory.getArea());
				} else {
					gateDetectionUpdate.setLocation2(pickupHistory.getBlock() + "-" + pickupHistory.getBay() + "-"
							+ pickupHistory.getLine() + "-" + pickupHistory.getTier());
				}
				gateDetectionService.updateGateDetection(gateDetectionUpdate);
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
						deduct += rfidTruck.getWgt();
					}
					// Chassis no
				} else if ("C".equalsIgnoreCase(rfidTruck.getTruckType())) {
					rfidTruckInfoRes.setChassisNo(rfidTruck.getPlateNumber());
					if (rfidTruck.getWgt() != null) {
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

}
