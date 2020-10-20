package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.annotation.Excel.Type;
import vn.com.irtech.eport.common.annotation.Excels;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Logistic account Object logistic_account
 * 
 * @author admin
 * @date 2020-05-07
 */
public class LogisticAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Master Account */
    @Excel(name = "Master Account")
    private Long groupId;

    /** Username */
    @Excel(name = "Username")
    private String userName;

    /** Email */
    @Excel(name = "Email")
    private String email;

    /** Password */
    @Excel(name = "Password")
    private String password;

    /** Salt */
    @Excel(name = "Salt")
    private String salt;

    /** Ho Va Ten */
    @Excel(name = "Ho Va Ten")
    private String fullName;

    /** Ho Va Ten */
    @Excel(name = "Mobile")
    private String mobile;

    /** Status（0 Normal 1 Disabled） */
    @Excel(name = "Status", readConverterExp = "0=,N=ormal,1=,D=isabled")
    private String status;

    /** Delete Flag (0 nomal 2 deleted) */
    private String delFlag;

    /** Login IP */
    @Excel(name = "Login IP")
    private String loginIp;

    /** Login Date */
    @Excel(name = "Login Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;
    
	/** Permission make order */
	@Excel(name = "Permission Make Order")
	private Boolean orderFlg;

	/** Permission separate bill */
	@Excel(name = "Permission Separate Bill")
	private Boolean fwdFlg;

	/** Permission Assign Driver */
	@Excel(name = "Permission Assign Driver")
	private Boolean transportFlg;

    @Excels({
        @Excel(name = "Group Code", targetAttr = "logisticGroup", type = Type.EXPORT)
    })
    private LogisticGroup logisticGroup;

    public LogisticGroup getLogisticGroup() {
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
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
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
    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
    }
    public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setLoginIp(String loginIp) 
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp() 
    {
        return loginIp;
    }
    public void setLoginDate(Date loginDate) 
    {
        this.loginDate = loginDate;
    }

    public Date getLoginDate() 
    {
        return loginDate;
    }

	public Boolean getOrderFlg() {
		return orderFlg;
	}

	public void setOrderFlg(Boolean orderFlg) {
		this.orderFlg = orderFlg;
	}

	public Boolean getFwdFlg() {
		return fwdFlg;
	}

	public void setFwdFlg(Boolean fwdFlg) {
		this.fwdFlg = fwdFlg;
	}

	public Boolean getTransportFlg() {
		return transportFlg;
	}

	public void setTransportFlg(Boolean transportFlg) {
		this.transportFlg = transportFlg;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("groupId", getGroupId()).append("userName", getUserName())
				.append("email", getEmail()).append("password", getPassword()).append("salt", getSalt())
				.append("fullName", getFullName()).append("status", getStatus()).append("delFlag", getDelFlag())
				.append("loginIp", getLoginIp()).append("loginDate", getLoginDate()).append("remark", getRemark())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime())
				.append("orderFlg", getOrderFlg()).append("fwdFlg", getFwdFlg())
				.append("transportFlg", getTransportFlg()).toString();
    }
}
