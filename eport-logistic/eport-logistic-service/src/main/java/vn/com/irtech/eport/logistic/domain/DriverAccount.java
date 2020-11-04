package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Driver login info Object transport_account
 * 
 * @author irtech
 * @date 2020-05-19
 */
public class DriverAccount extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Long id;

	/** Logistic Group */
	@Excel(name = "Logistic Group")
	private Long logisticGroupId;

	/** So DT */
	@Excel(name = "So DT")
	@Pattern(regexp = "^[0-9]{10,11}$")
	private String mobileNumber;

	/** Ho va Ten */
	@Excel(name = "Ho va Ten")
	private String fullName;

	/** CMND */
	@Excel(name = "CMND")
	private String identifyCardNo;

	/** Mat Khau */
	@Excel(name = "Mat Khau")
	@JsonIgnore
	@Min(6)
	private String password;

	/** Salt */
	@Excel(name = "Salt")
	@JsonIgnore
	private String salt;

	/** Trạng thái khóa (default 0) */
	@Excel(name = "Khóa tài khoản")
	private String status;

	/** Delete Flag */
	private boolean delFlag;

	/** Hieu Luc Den */
	@Excel(name = "Hieu Luc Den", width = 30, dateFormat = "dd-MM-yyyy")
	private Date validDate;

	/** Pickup assign id */
	private Long pickupAssignId;

	private LogisticGroup logisticGroup;

	public LogisticGroup getLogisticGroup() {
		if (logisticGroup == null) {
			logisticGroup = new LogisticGroup();
		}
		return logisticGroup;
	}

	public void setLogisticGroup(LogisticGroup logisticGroup) {
		this.logisticGroup = logisticGroup;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setIdentifyCardNo(String identifyCardNo) {
		this.identifyCardNo = identifyCardNo;
	}

	public String getIdentifyCardNo() {
		return identifyCardNo;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSalt() {
		return salt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setDelFlag(boolean delFlag) {
		this.delFlag = delFlag;
	}

	public boolean getDelFlag() {
		return delFlag;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getValidDate() {
		return validDate;
	}

	public Long getPickupAssignId() {
		return pickupAssignId;
	}

	public void setPickupAssignId(Long pickupAssignId) {
		this.pickupAssignId = pickupAssignId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("logisticGroupId", getLogisticGroupId()).append("mobileNumber", getMobileNumber())
				.append("fullName", getFullName()).append("identifyCardNo", getIdentifyCardNo())
				.append("password", getPassword()).append("salt", getSalt()).append("status", getStatus())
				.append("delFlag", getDelFlag()).append("validDate", getValidDate()).append("remark", getRemark())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime())
				.append("pickupAssignId", getPickupAssignId()).append("params", getParams()).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !(obj instanceof DriverAccount)) {
			return false;
		}
		DriverAccount that = (DriverAccount) obj;
		return that.getId().longValue() == this.getId().longValue();
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + id.hashCode();
		return result;
	}
}
