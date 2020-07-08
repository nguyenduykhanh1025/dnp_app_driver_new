package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EdiDataReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lineOper;

	private String consignee;

	private String secureCode;

	private String containerNo;

	private String releaseNo;

	private Integer detFreeDays;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Date expiryTs;

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

	public Integer getDetFreeDays() {
		return detFreeDays;
	}

	public void setDetFreeDays(Integer detFreeDays) {
		this.detFreeDays = detFreeDays;
	}

	public Date getExpiryTs() {
		return expiryTs;
	}

	public void setExpiryTs(Date expiryTs) {
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
