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
	private String consigneeTaxcode;

	@NotBlank
	private String orderNumber;

	@NotBlank
	private String containerNo;

	private String releaseNo;

	private String detFreeDays;

	@NotNull
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Date expiryTs;

	private String terOfMtReturn;

	@NotBlank
	private String modTransName;

	@NotBlank
	private String modTransVoyage;

	@NotBlank
	private String billOfLading;

	private String sztp;

	private String pol;

	private String pod;

	@NotBlank
	@AcceptedValues(values = {"N", "U", "D"}, message = "Must be any of {'N', 'U', 'D'}")
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

	public String getConsigneeTaxcode() {
		return consigneeTaxcode;
	}

	public void setConsigneeTaxcode(String consigneeTaxcode) {
		this.consigneeTaxcode = consigneeTaxcode;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getMsgFunc() {
		return msgFunc;
	}

	public void setMsgFunc(String msgFunc) {
		this.msgFunc = msgFunc;
	}

}
