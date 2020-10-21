package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;

public class HouseBillSearchReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String keyWord;

	private String fromDate;

	private String toDate;

	private Long logisticGroupId;
	
	private String masterBill;
	
	private String houseBill;

	private Boolean releaseFlg;

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

	public String getMasterBill() {
		return masterBill;
	}

	public void setMasterBill(String masterBill) {
		this.masterBill = masterBill;
	}

	public String getHouseBill() {
		return houseBill;
	}

	public void setHouseBill(String houseBill) {
		this.houseBill = houseBill;
	}

	public Boolean getReleaseFlg() {
		return releaseFlg;
	}

	public void setReleaseFlg(Boolean releaseFlg) {
		this.releaseFlg = releaseFlg;
	}

}
