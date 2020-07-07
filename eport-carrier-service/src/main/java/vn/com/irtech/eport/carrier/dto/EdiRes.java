package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.List;

public class EdiRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean isError;

	private int errorCode;

	private String message;

	private String transactionId;

	private List<EdiDataRes> data;

	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public List<EdiDataRes> getData() {
		return data;
	}

	public void setData(List<EdiDataRes> data) {
		this.data = data;
	}

	public static EdiRes success(String message, String transactionId, List<EdiDataRes> data) {
		EdiRes ediRes = new EdiRes();
		ediRes.setIsError(false);
		ediRes.setTransactionId(transactionId);
		ediRes.setData(data);
		return ediRes;
	}
	
	public static EdiRes error(int errorCode, String message, String transactionId) {
		EdiRes ediRes = new EdiRes();
		ediRes.setIsError(true);
		ediRes.setMessage(message);
		ediRes.setTransactionId(transactionId);
		ediRes.setErrorCode(errorCode);
		return ediRes;
	}
}
