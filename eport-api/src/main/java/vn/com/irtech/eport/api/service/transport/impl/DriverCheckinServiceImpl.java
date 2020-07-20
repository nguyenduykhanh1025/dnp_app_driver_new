package vn.com.irtech.eport.api.service.transport.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.form.CheckinForm;
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
	public String checkin(CheckinForm req) throws Exception{
		
		
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
		}
		
		String qrString = new Gson().toJson(req) + "*";
		
		return qrString;
	}

}
