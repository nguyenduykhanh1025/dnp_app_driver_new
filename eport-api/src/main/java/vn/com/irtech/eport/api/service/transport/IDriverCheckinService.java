package vn.com.irtech.eport.api.service.transport;

import vn.com.irtech.eport.api.form.CheckinForm;

public interface IDriverCheckinService {
	
	/**
	 * Verify checkin info and return QR string
	 * @param req
	 * @return QR string
	 */
	public String checkin(CheckinForm req) throws Exception;
	
}
