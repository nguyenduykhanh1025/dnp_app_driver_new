package vn.com.irtech.eport.system.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Notification Object sys_notification_receiver
 * 
 * @author Irtech
 * @date 2020-08-22
 */
public class SysNotificationReceiver extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** User ID */
    @Excel(name = "User ID")
    private Long userId;

    /** User Type */
    @Excel(name = "User Type")
    private Long userType;

    /** Notify ID */
    @Excel(name = "Notify ID")
    private Long notificationId;

    /** Notify Type */
    @Excel(name = "Notify Type")
    private String notificationType;

    /** Seen Flag */
    @Excel(name = "Seen Flag")
    private Boolean seenFlg;

    /** Seen Time */
    @Excel(name = "Seen Time", width = 30, dateFormat = "yyyy-MM-dd")
    private Date seenTime;

    /** Sent Flag */
    @Excel(name = "Sent Flag")
    private Boolean sentFlg;

    private String title;
    
    private String content;
    
    private String notifyLink;
    
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserType(Long userType) 
    {
        this.userType = userType;
    }

    public Long getUserType() 
    {
        return userType;
    }
    public void setNotificationId(Long notificationId) 
    {
        this.notificationId = notificationId;
    }

    public Long getNotificationId() 
    {
        return notificationId;
    }
    public void setNotificationType(String notificationType) 
    {
        this.notificationType = notificationType;
    }

    public String getNotificationType() 
    {
        return notificationType;
    }
    public void setSeenFlg(Boolean seenFlg) 
    {
        this.seenFlg = seenFlg;
    }

    public Boolean getSeenFlg() 
    {
        return seenFlg;
    }
    public void setSeenTime(Date seenTime) 
    {
        this.seenTime = seenTime;
    }

    public Date getSeenTime() 
    {
        return seenTime;
    }
    public void setSentFlg(Boolean sentFlg) 
    {
        this.sentFlg = sentFlg;
    }

    public Boolean getSentFlg() 
    {
        return sentFlg;
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNotifyLink() {
		return notifyLink;
	}

	public void setNotifyLink(String notifyLink) {
		this.notifyLink = notifyLink;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userType", getUserType())
            .append("notificationId", getNotificationId())
            .append("notificationType", getNotificationType())
            .append("seenFlg", getSeenFlg())
            .append("seenTime", getSeenTime())
            .append("sentFlg", getSentFlg())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("title", getTitle())
            .append("content", getContent())
            .append("notifyLink", getNotifyLink())
            .toString();
    }
}
