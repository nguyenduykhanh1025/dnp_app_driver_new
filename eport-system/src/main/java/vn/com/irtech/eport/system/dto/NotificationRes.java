package vn.com.irtech.eport.system.dto;

import java.io.Serializable;
import java.sql.Date;

public class NotificationRes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private String notifyLink;
	
	private Date createTime;
	
	private String notificationType;
	
	private Boolean seenFlg;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Boolean getSeenFlg() {
		return seenFlg;
	}

	public void setSeenFlg(Boolean seenFlg) {
		this.seenFlg = seenFlg;
	}
	
}
