package vn.com.irtech.eport.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】 Object notification_receiver
 * 
 * @author ruoyi
 * @date 2020-07-06
 */
public class NotificationReceiver extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long notificationId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userDeviceId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNotificationId(Long notificationId) 
    {
        this.notificationId = notificationId;
    }

    public Long getNotificationId() 
    {
        return notificationId;
    }
    public void setUserDeviceId(Long userDeviceId) 
    {
        this.userDeviceId = userDeviceId;
    }

    public Long getUserDeviceId() 
    {
        return userDeviceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("notificationId", getNotificationId())
            .append("userDeviceId", getUserDeviceId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
