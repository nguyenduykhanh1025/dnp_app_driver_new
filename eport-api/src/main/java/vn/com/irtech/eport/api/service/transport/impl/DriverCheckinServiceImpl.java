package vn.com.irtech.eport.api.service.transport.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.form.CheckinReq;
import vn.com.irtech.eport.api.form.CheckinRes;
import vn.com.irtech.eport.api.form.PickupHistoryDataRes;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.service.transport.IDriverCheckinService;
import vn.com.irtech.eport.api.util.SecurityUtils;
import vn.com.irtech.eport.common.exception.BusinessException;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;

@Service
public class DriverCheckinServiceImpl implements IDriverCheckinService{

	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@Override
	public String checkin(CheckinReq req, String sessionId) throws Exception{
		
		CheckinRes checkinRes = new CheckinRes();
		
		checkinRes.setSessionId(sessionId);
		
		// validate
		for (Long id : req.getPickupHistoryIds()) {
			PickupHistory pickupHistory = pickupHistoryService.selectPickupHistoryById(id);
			
			// pickup is not exist
			if (pickupHistory == null) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0007));
			}
			
			// pickup is not of driver
			if (pickupHistory.getDriverId() != SecurityUtils.getCurrentUser().getUser().getUserId()) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0001));
			}
			
			// pickup has been checked in
			if (pickupHistory.getStatus() >= 2) {
				throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0010));
			}
			
			PickupHistoryDataRes pickupHistoryDataRes = new PickupHistoryDataRes();
			pickupHistoryDataRes.setPickupHistoryId(id);
			pickupHistoryDataRes.setContNo(pickupHistory.getContainerNo());
			pickupHistoryDataRes.setShipmentId(pickupHistory.getShipmentId());
			pickupHistoryDataRes.setShipmentDetailId(pickupHistory.getShipmentDetailId());
			pickupHistoryDataRes.setVessel(pickupHistory.getShipmentDetail().getVslNm());
			pickupHistoryDataRes.setVoyage(pickupHistory.getShipmentDetail().getVoyNo());
			pickupHistoryDataRes.setSztp(pickupHistory.getShipmentDetail().getSztp());
			pickupHistoryDataRes.setFe(pickupHistory.getShipmentDetail().getFe());
			pickupHistoryDataRes.setServiceType(pickupHistory.getShipment().getServiceType());
			pickupHistoryDataRes.setWeight(pickupHistory.getShipmentDetail().getWgt());
			pickupHistoryDataRes.setChassisNo(pickupHistory.getChassisNo());
			pickupHistoryDataRes.setTruckNo(pickupHistory.getTruckNo());
			
			checkinRes.getData().add(pickupHistoryDataRes);
		}
		
		String qrString = new Gson().toJson(checkinRes) + "*";
		
		return qrString.replace("\"", "'");
	}

}
