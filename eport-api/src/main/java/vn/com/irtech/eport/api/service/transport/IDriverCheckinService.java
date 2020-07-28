package vn.com.irtech.eport.api.service.transport;

import vn.com.irtech.eport.api.form.CheckinReq;

public interface IDriverCheckinService {
	
	/**
	 * Verify checkin info and return QR string
	 * @param req
	 * @param sessionId
	 * @return QR string
	 */
	public String checkin(CheckinReq req, String sessionId) throws Exception;
	
}
