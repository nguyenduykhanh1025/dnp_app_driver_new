package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;

public class HouseBillSearchReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyWord;

	private String fromDate;

	private String toDate;

	private Long logisticGroupId;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

}
