package vn.com.irtech.eport.api.form;

import java.util.Date;

public class ShipmentForm {
	
	private Long id;
	
	private String blNo;
	
	private String bookingNo;
	
	private Integer serviceType;
	
	private String fe;
	
	private String taxCode;
	
	private Long containerAmount;
	
	private String edoFlg;
	
	private String status;
	
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getBookingNo() {
		return bookingNo;
	}

	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getFe() {
		return fe;
	}

	public void setFe(String fe) {
		this.fe = fe;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Long getContainerAmount() {
		return containerAmount;
	}

	public void setContainerAmount(Long containerAmount) {
		this.containerAmount = containerAmount;
	}

	public String getEdoFlg() {
		return edoFlg;
	}

	public void setEdoFlg(String edoFlg) {
		this.edoFlg = edoFlg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
