package vn.com.irtech.eport.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】 Object notifications
 * 
 * @author irtech
 * @date 2020-07-06
 */
public class Notifications extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String title;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    private Integer seenAmount;

    private Integer totalReceiver;

    NotificationReceiver notificationReceiver;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setNotificationReceiver(NotificationReceiver notificationReceiver) {
        this.notificationReceiver = notificationReceiver;
    }

    public NotificationReceiver getNotificationReceiver() {
        return notificationReceiver;
    }

    public void setSeenAmount(Integer seenAmount) {
        this.seenAmount = seenAmount;
    }

    public Integer getSeenAmount() {
        return seenAmount;
    }

    public void setTotalReceiver(Integer totalReceiver) {
        this.totalReceiver = totalReceiver;
    }

    public Integer getTotalReceiver() {
        return totalReceiver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("notificationReceiver", getNotificationReceiver())
            .append("seenAmount", getSeenAmount())
            .append("totalReceiver", getTotalReceiver())
            .toString();
    }
}
