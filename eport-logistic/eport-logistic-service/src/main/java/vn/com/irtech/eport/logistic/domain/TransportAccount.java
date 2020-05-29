package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Driver login info Object transport_account
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
public class TransportAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logistic Group */
    @Excel(name = "Logistic Group")
    private Long logisticGroupId;

    /** Bien So Xe */
    @Excel(name = "Bien So Xe")
    private String plateNumber;

    /** So DT */
    @Excel(name = "So DT")
    private String mobileNumber;

    /** Ho va Ten */
    @Excel(name = "Ho va Ten")
    private String fullName;

    /** Mat Khau */
    @Excel(name = "Mat Khau")
    private String password;

    /** Salt */
    @Excel(name = "Salt")
    private String salt;

    /** Trang Thai */
    @Excel(name = "Trang Thai")
    private String status;

    /** Delete Flag */
    private boolean delFlag;
    
    /** Thuê ngoài (0 nomal 1 rent) */
    @Excel(name = "Thuê ngoài (0 nomal 1 rent)")
    private String externalRentStatus;

    /** Hieu Luc Den */
    @Excel(name = "Hieu Luc Den", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validDate;
    
    private LogisticGroup logisticGroup;

    public LogisticGroup getLogisticGroup() {
    	if(logisticGroup == null) {
    		logisticGroup = new LogisticGroup();
    	}
		return logisticGroup;
	}

	public void setLogisticGroup(LogisticGroup logisticGroup) {
		this.logisticGroup = logisticGroup;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setPlateNumber(String plateNumber) 
    {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() 
    {
        return plateNumber;
    }
    public void setMobileNumber(String mobileNumber) 
    {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() 
    {
        return mobileNumber;
    }
    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setSalt(String salt) 
    {
        this.salt = salt;
    }

    public String getSalt() 
    {
        return salt;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(boolean delFlag) 
    {
        this.delFlag = delFlag;
    }

    public boolean getDelFlag() 
    {
        return delFlag;
    }
    public void setExternalRentStatus(String externalRentStatus) 
    {
        this.externalRentStatus = externalRentStatus;
    }

    public String getExternalRentStatus() 
    {
        return externalRentStatus;
    }
    public void setValidDate(Date validDate) 
    {
        this.validDate = validDate;
    }

    public Date getValidDate() 
    {
        return validDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("plateNumber", getPlateNumber())
            .append("mobileNumber", getMobileNumber())
            .append("fullName", getFullName())
            .append("password", getPassword())
            .append("salt", getSalt())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("externalRentStatus", getExternalRentStatus())
            .append("validDate", getValidDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
