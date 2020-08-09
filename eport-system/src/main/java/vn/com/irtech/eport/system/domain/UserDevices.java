package vn.com.irtech.eport.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】 Object user_devices
 * 
 * @author irtech
 * @date 2020-07-06
 */
public class UserDevices extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String userToken;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String deviceToken;

    /** 1: Logistic, 2: Driver, 3: Staff */
    @Excel(name = "1: Logistic, 2: Driver, 3: Staff")
    private Long userType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserToken(String userToken) 
    {
        this.userToken = userToken;
    }

    public String getUserToken() 
    {
        return userToken;
    }
    public void setDeviceToken(String deviceToken) 
    {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() 
    {
        return deviceToken;
    }
    public void setUserType(Long userType) 
    {
        this.userType = userType;
    }

    public Long getUserType() 
    {
        return userType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userToken", getUserToken())
            .append("deviceToken", getDeviceToken())
            .append("userType", getUserType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
