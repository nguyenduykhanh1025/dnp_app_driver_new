package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.eport.carrier.validation.annotation.AcceptedValues;

public class EdiDataReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String lineOper;

	@NotBlank
	private String consignee;

	@NotBlank
	private String secureCode;

	@NotBlank
	private String containerNo;

	@NotBlank
	private String releaseNo;

	@NotNull
	private Integer detFreeDays;

	@NotNull
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Date expiryTs;

	@NotBlank
	private String terOfMtReturn;

	@NotBlank
	private String modTransName;

	@NotBlank
	private String modTransVoyage;

	@NotBlank
	private String billOfLading;

	@NotBlank
	@AcceptedValues(values = {"N", "U", "D"}, message = "must be any of {'N', 'U', 'D'}")
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
