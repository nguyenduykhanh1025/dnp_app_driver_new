package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;

public class EdiDataRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lineOper;

	private String consignee;

	private String secureCode;

	private String containerNo;

	private String releaseNo;

	private String detFreeDays;

	private String expiryTs;

	private String terOfMtReturn;

	private String modTransName;

	private String modTransVoyage;

	private String billOfLading;

	private String msgFunc;

	public String getLineOper() {
		return lineOper;
	}

	public void setLineOper(String lineOper) {
		this.lineOper = lineOper;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getSecureCode() {
		return secureCode;
	}

	public void setSecureCode(String secureCode) {
		this.secureCode = secureCode;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getReleaseNo() {
		return releaseNo;
	}

	public void setReleaseNo(String releaseNo) {
		this.releaseNo = releaseNo;
	}

	public String getDetFreeDays() {
		return detFreeDays;
	}

	public void setDetFreeDays(String detFreeDays) {
		this.detFreeDays = detFreeDays;
	}

	public String getExpiryTs() {
		return expiryTs;
	}

	public void setExpiryTs(String expiryTs) {
		this.expiryTs = expiryTs;
	}

	public String getTerOfMtReturn() {
		return terOfMtReturn;
	}

	public void setTerOfMtReturn(String terOfMtReturn) {
		this.terOfMtReturn = terOfMtReturn;
	}

	public String getModTransName() {
		return modTransName;
	}

	public void setModTransName(String modTransName) {
		this.modTransName = modTransName;
	}

	public String getModTransVoyage() {
		return modTransVoyage;
	}

	public void setModTransVoyage(String modTransVoyage) {
		this.modTransVoyage = modTransVoyage;
	}

	public String getBillOfLading() {
		return billOfLading;
	}

	public void setBillOfLading(String billOfLading) {
		this.billOfLading = billOfLading;
	}

	public String getMsgFunc() {
		return msgFunc;
	}

	public void setMsgFunc(String msgFunc) {
		this.msgFunc = msgFunc;
	}

}
