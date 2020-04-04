package vn.com.irtech.eport.shipping.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Shipping line account Object shipping_account
 * 
 * @author Irtech
 * @date 2020-04-03
 */
public class ShippingAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Email */
    @Excel(name = "Email")
    private String email;

    /** Password */
    private String password;

    /** Salt */
    private String salt;

    /** Status（0 Normal 1 Disabled） */
    @Excel(name = "Status", readConverterExp = "0=,N=ormal,1=,D=isabled")
    private String status;

    /** Shipping Line Code */
    @Excel(name = "Shipping Line Code")
    private String shippingCode;

    /** Shipping Line Name */
    @Excel(name = "Shipping Line Name")
    private String shippingName;

    /** Delete Flag (0 nomal 2 deleted) */
    private String delFlag;

    /** Login IP */
    private String loginIp;

    /** Login Date */
    private Date loginDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setShippingCode(String shippingCode) 
    {
        this.shippingCode = shippingCode;
    }

    public String getShippingCode() 
    {
        return shippingCode;
    }
    public void setShippingName(String shippingName) 
    {
        this.shippingName = shippingName;
    }

    public String getShippingName() 
    {
        return shippingName;
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
            .append("email", getEmail())
            .append("password", getPassword())
            .append("salt", getSalt())
            .append("status", getStatus())
            .append("shippingCode", getShippingCode())
            .append("shippingName", getShippingName())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
