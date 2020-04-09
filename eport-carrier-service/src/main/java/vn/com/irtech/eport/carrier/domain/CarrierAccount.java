package vn.com.irtech.eport.carrier.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import vn.com.irtech.eport.common.annotation.Excels;
import vn.com.irtech.eport.common.annotation.Excel.Type;
import java.util.Date;

/**
 * Carrier Account Object carrier_account
 * 
 * @author irtech
 * @date 2020-04-04
 */
public class CarrierAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Master Account */
    @Excel(name = "Master Account")
    private Long groupId;

    /** Shipping Line Code */
    @Excel(name = "Carrier Code")
    private String carrierCode;

    /** Email */
    @Excel(name = "Email")
    private String email;

    /** Password */
    @Excel(name = "Password")
    private String password;

    /** Salt */
    private String salt;

    /** Ho Va Ten */
    @Excel(name = "Ho Va Ten")
    private String fullName;

    /** Status（0 Normal 1 Disabled） */
    @Excel(name = "Status", readConverterExp = "0=Normal,1=Disabled")
    private String status;

    @Excels({
        @Excel(name = "Group Code", targetAttr = "carrierGroup", type = Type.EXPORT)
    })
    private CarrierGroup carrierGroup;

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
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    public void setCarrierCode(String carrierCode) 
    {
        this.carrierCode = carrierCode;
    }

    public String getCarrierCode() 
    {
        return carrierCode;
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

    public CarrierGroup getCarrierGroup() {
        if (carrierGroup == null)
        {
            carrierGroup = new CarrierGroup();
        }
        return carrierGroup;
    }

    public void setCarrierGroup(CarrierGroup carrierGroup) {
        this.carrierGroup = carrierGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("carrierCode", getCarrierCode())
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
            .append("carrierGroup", getCarrierGroup())
            .toString();
    }
}
