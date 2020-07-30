package vn.com.irtech.eport.logistic.form;

import java.util.Date;

public class NotificationForm {
    
    private Long notificationId;

    private String title;

    private String content;

    private Date createTime;

    public Long getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}