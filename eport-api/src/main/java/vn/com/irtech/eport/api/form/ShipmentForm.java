package vn.com.irtech.eport.api.form;

import java.io.Serializable;
import java.util.Date;

public class ShipmentForm  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String blNo;
	
	private String bookingNo;
	
	private Integer serviceType;
	
	private Long containerAmount;
	
	private String edoFlg;
	
	private String opeCode;

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

	public String getOpeCode() {
		return opeCode;
	}

	public void setOpeCode(String opeCode) {
		this.opeCode = opeCode;
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
