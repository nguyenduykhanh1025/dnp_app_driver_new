package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HouseBillRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String masterBillNo;

	private String houseBillNo;

	private Integer contNumber;

	private String consignee2;

	private String carrierCode;

	private String orderNumber;

	private String consignee2TaxCode;

	private String forwarderName;

	private Long logisticGroupId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public String getMasterBillNo() {
		return masterBillNo;
	}

	public void setMasterBillNo(String masterBillNo) {
		this.masterBillNo = masterBillNo;
	}

	public String getHouseBillNo() {
		return houseBillNo;
	}

	public void setHouseBillNo(String houseBillNo) {
		this.houseBillNo = houseBillNo;
	}

	public Integer getContNumber() {
		return contNumber;
	}

	public void setContNumber(Integer contNumber) {
		this.contNumber = contNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getConsignee2() {
		return consignee2;
	}

	public void setConsignee2(String consignee2) {
		this.consignee2 = consignee2;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getConsignee2TaxCode() {
		return consignee2TaxCode;
	}

	public void setConsignee2TaxCode(String consignee2TaxCode) {
		this.consignee2TaxCode = consignee2TaxCode;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

}
