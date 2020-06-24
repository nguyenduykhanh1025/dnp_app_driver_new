package vn.com.irtech.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UnitBillEntity {

	private String invNo;
	
	private Double netAmount;
	
	private Double vatRate;
	
	private Double vatAmount;
	
	private Double amount;
	
	private String containerNo;
	
	private String sztp;

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getVatRate() {
		return vatRate;
	}

	public void setVatRate(Double vatRate) {
		this.vatRate = vatRate;
	}

	public Double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(Double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
	            .append("invNo", getInvNo())
	            .append("netAmount", getNetAmount())
	            .append("vatRate", getVatRate())
	            .append("vatAmount", getVatAmount())
	            .append("amount", getAmount())
	            .append("containerNo", getContainerNo())
	            .append("sztp", getSztp())
	            .toString();
	}
	
}
