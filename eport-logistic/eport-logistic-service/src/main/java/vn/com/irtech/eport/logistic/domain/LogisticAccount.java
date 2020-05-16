package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.annotation.Excels;
import vn.com.irtech.eport.common.annotation.Excel.Type;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

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
    
    @Excels({
        @Excel(name = "Group Code", targetAttr = "logisticGroup", type = Type.EXPORT)
    })
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
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("email", getEmail())
            .append("password", getPassword())
            .append("salt", getSalt())
            .append("fullName", getFullName())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
