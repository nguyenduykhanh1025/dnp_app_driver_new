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

	@Excel(name = "Is Gate in Order")
	private Boolean isGateInOrder;
	
	/** ip address */
	@Excel(name = "ip address")
	private String ipAddress;

	@Excel(name = "Is Shifting Cont Order")
	private Boolean isShiftingContOrder;

	@Excel(name = "Response Time")
	private Date responseTime;

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
	
	public Boolean getIsGateInOrder() {
		return isGateInOrder;
	}

	public void setIsGateInOrder(Boolean isGateInOrder) {
		this.isGateInOrder = isGateInOrder;
	}

	public Boolean getIsShiftingContOrder() {
		return this.isShiftingContOrder;
	}

	public void setIsShiftingContOrder(Boolean isShiftingContOrder) {
		this.isShiftingContOrder = isShiftingContOrder;
	}

	public Date getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("uuId", getUuId()).append("status", getStatus())
				.append("isReceiveContFullOrder", getIsReceiveContFullOrder())
				.append("isReceiveContEmptyOrder", getIsReceiveContEmptyOrder())
				.append("isSendContFullOrder", getIsSendContFullOrder())
				.append("isSendContEmptyOrder", getIsSendContEmptyOrder())
				.append("isGateInOrder", getIsGateInOrder())
				.append("ipAddress", getIpAddress())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("remark", getRemark())
				.append("isShiftingContOrder", getIsShiftingContOrder()).append("responseTime", getResponseTime())
				.toString();
	}
}