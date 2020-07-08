package vn.com.irtech.eport.framework.web.exception;

import vn.com.irtech.eport.carrier.dto.EdiRes;

public class EdiApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private EdiRes ediRes;

	public EdiRes getEdiRes() {
		return ediRes;
	}

	public void setEdiRes(EdiRes ediRes) {
		this.ediRes = ediRes;
	}

	public EdiApiException(EdiRes ediRes) {
		super();
		this.ediRes = ediRes;
	}

}
