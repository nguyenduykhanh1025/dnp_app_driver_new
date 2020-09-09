package vn.com.irtech.api.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ShipmentEntity {
    private Long logisticAccountId;

    private Long logisticGroupId;

    /** Dich Vu */
    private Integer serviceType;

    /** Bill No */
    private String blNo;

    /** Booking No */
    private String bookingNo;
    
    /** Mã hãng tàu */
    private String opeCode;

    /** MST */
    private String taxCode;
    
    /** Tên cty theo MST */
    private String groupName;

    /** Địa chỉ theo MST */
    private String address;
    
    /** So Luong Container */
    private Long containerAmount;

    /** EDO Flag (1,0) */
    private String edoFlg;

    /** So tham chieu CATOS */
    private String referenceNo;

	public Long getLogisticAccountId() {
		return logisticAccountId;
	}

	public void setLogisticAccountId(Long logisticAccountId) {
		this.logisticAccountId = logisticAccountId;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
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

	public String getOpeCode() {
		return opeCode;
	}

	public void setOpeCode(String opeCode) {
		this.opeCode = opeCode;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("logisticAccountId", getLogisticAccountId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("serviceType", getServiceType())
            .append("blNo", getBlNo())
            .append("bookingNo", getBookingNo())
            .append("opeCode", getOpeCode())
            .append("taxCode", getTaxCode())
            .append("groupName", getGroupName())
            .append("address", getAddress())
            .append("containerAmount", getContainerAmount())
            .append("edoFlg", getEdoFlg())
            .append("referenceNo", getReferenceNo())
            .toString();
    }
}
