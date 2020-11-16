package vn.com.irtech.eport.system.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Entity robot
 * 
 * @author baohv
 * @date 2020-06-18
 */
public class SysRobot extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** Robot ID */
	private Long id;

	/** UUID */
	@NotBlank(message = "Uuid là trường bắt buộc")
	@Excel(name = "UUID")
	private String uuId;

	/** Status（0 Available 1 BUSY 2 OFFLINE） */
	@Excel(name = "Status")
	private String status;

	@Excel(name = "Is Receive Cont Full Order")
	private Boolean isReceiveContFullOrder;

	@Excel(name = "Is Receive Cont Empty Order")
	private Boolean isReceiveContEmptyOrder;

	@Excel(name = "Is Send Cont Full Order")
	private Boolean isSendContFullOrder;

	@Excel(name = "Is Send Cont Full Order")
	private Boolean isSendContEmptyOrder;

	@Excel(name = "Is Shifting Cont Order")
	private Boolean isShiftingContOrder;

	@Excel(name = "Is Change Vessel Order")
	private Boolean isChangeVesselOrder;

	@Excel(name = "Is Create Booking Order")
	private Boolean isCreateBookingOrder;

	@Excel(name = "Is Gate in Order")
	private Boolean isGateInOrder;
	
	@Excel(name = "Extension Date Order")
	private Boolean isExtensionDateOrder;
	
	@Excel(name = "Change Terminal Custom hold")
	private Boolean isChangeTerminalCustomHold;
	
	@Excel(name = "Cancel Send Cont Full Order")
	private Boolean isCancelSendContFullOrder;
	
	@Excel(name = "Cancel Receive Cont Empty Order")
	private Boolean isCancelReceiveContEmptyOrder;
	
	@Excel(name = "Export Receipt")
	private Boolean isExportReceipt;

	@Excel(name = "Extension Det Order")
	private Boolean isExtensionDetOrder;

	/** ip address */
	@Excel(name = "ip address")
	private String ipAddress;

	@Excel(name = "Response Time")
	private Date responseTime;

	private Integer serviceType;
	
	private Boolean disabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Boolean getIsReceiveContFullOrder() {
		return isReceiveContFullOrder;
	}

	public void setIsReceiveContFullOrder(Boolean isReceiveContFullOrder) {
		this.isReceiveContFullOrder = isReceiveContFullOrder;
	}

	public Boolean getIsSendContEmptyOrder() {
		return isSendContEmptyOrder;
	}

	public void setIsSendContEmptyOrder(Boolean isSendContEmptyOrder) {
		this.isSendContEmptyOrder = isSendContEmptyOrder;
	}

	public Boolean getIsReceiveContEmptyOrder() {
		return isReceiveContEmptyOrder;
	}

	public void setIsReceiveContEmptyOrder(Boolean isReceiveContEmptyOrder) {
		this.isReceiveContEmptyOrder = isReceiveContEmptyOrder;
	}

	public Boolean getIsSendContFullOrder() {
		return isSendContFullOrder;
	}

	public void setIsSendContFullOrder(Boolean isSendContFullOrder) {
		this.isSendContFullOrder = isSendContFullOrder;
	}
	
	public Boolean getIsShiftingContOrder() {
		return this.isShiftingContOrder;
	}

	public void setIsShiftingContOrder(Boolean isShiftingContOrder) {
		this.isShiftingContOrder = isShiftingContOrder;
	}

	public Boolean getIsChangeVesselOrder() {
		return this.isChangeVesselOrder;
	}

	public void setIsChangeVesselOrder(Boolean isChangeVesselOrder) {
		this.isChangeVesselOrder = isChangeVesselOrder;
	}

	public Boolean getIsCreateBookingOrder() {
		return this.isCreateBookingOrder;
	}

	public void setIsCreateBookingOrder(Boolean isCreateBookingOrder) {
		this.isCreateBookingOrder = isCreateBookingOrder;
	}

	public Boolean getIsGateInOrder() {
		return isGateInOrder;
	}

	public void setIsGateInOrder(Boolean isGateInOrder) {
		this.isGateInOrder = isGateInOrder;
	}
	
	public Boolean getIsExtensionDateOrder() {
		return isExtensionDateOrder;
	}

	public void setIsExtensionDateOrder(Boolean isExtensionDateOrder) {
		this.isExtensionDateOrder = isExtensionDateOrder;
	}

	public Boolean getIsChangeTerminalCustomHold() {
		return isChangeTerminalCustomHold;
	}

	public void setIsChangeTerminalCustomHold(Boolean isChangeTerminalCustomHold) {
		this.isChangeTerminalCustomHold = isChangeTerminalCustomHold;
	}

	public Boolean getIsCancelSendContFullOrder() {
		return isCancelSendContFullOrder;
	}

	public void setIsCancelSendContFullOrder(Boolean isCancelSendContFullOrder) {
		this.isCancelSendContFullOrder = isCancelSendContFullOrder;
	}

	public Boolean getIsCancelReceiveContEmptyOrder() {
		return isCancelReceiveContEmptyOrder;
	}

	public void setIsCancelReceiveContEmptyOrder(Boolean isCancelReceiveContEmptyOrder) {
		this.isCancelReceiveContEmptyOrder = isCancelReceiveContEmptyOrder;
	}

	public Date getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public Integer getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getIsExportReceipt() {
		return isExportReceipt;
	}

	public void setIsExportReceipt(Boolean isExportReceipt) {
		this.isExportReceipt = isExportReceipt;
	}

	public Boolean getIsExtensionDetOrder() {
		return isExtensionDetOrder;
	}

	public void setIsExtensionDetOrder(Boolean isExtensionDetOrder) {
		this.isExtensionDetOrder = isExtensionDetOrder;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("uuId", getUuId()).append("status", getStatus())
				.append("isReceiveContFullOrder", getIsReceiveContFullOrder())
				.append("isReceiveContEmptyOrder", getIsReceiveContEmptyOrder())
				.append("isSendContFullOrder", getIsSendContFullOrder())
				.append("isSendContEmptyOrder", getIsSendContEmptyOrder())
				.append("isShiftingContOrder", getIsShiftingContOrder())
				.append("isChangeVesselOrder", getIsChangeVesselOrder())
				.append("isCreateBookingOrder", getIsCreateBookingOrder())
				.append("isGateInOrder", getIsGateInOrder())
				.append("isExtensionDateOrder", getIsExtensionDateOrder())
				.append("isChangeTerminalCustomHold", getIsChangeTerminalCustomHold())
				.append("isCancelSendContFullOrder", getIsCancelSendContFullOrder())
				.append("isCancelReceiveContEmptyOrder", getIsCancelReceiveContEmptyOrder())
				.append("isExportReceipt", getIsExportReceipt())
				.append("isExtensionDetOrder", getIsExtensionDetOrder())
				.append("ipAddress", getIpAddress())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.append("responseTime", getResponseTime())
				.toString();
	}
}