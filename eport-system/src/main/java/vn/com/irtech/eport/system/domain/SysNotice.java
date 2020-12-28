package vn.com.irtech.eport.system.domain;


import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Notification form Object sys_notice
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
public class SysNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Announcement primary key seq_sys_notice.nextval */
    private Long noticeId;

    /** Announcement title */
    @Excel(name = "Announcement title")
    private String noticeTitle;

	/** Announcement type (C carrier L logistic A all) */
	@Excel(name = "Announcement type (C information L warning A all)")
    private String noticeType;

	/** Active */
	@Excel(name = "Active")
	private Integer active;

	/** Date Start */
	@Excel(name = "Date Start", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateStart;

	/** Date Finish */
	@Excel(name = "Date Finish", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateFinish;

	/** Notice Content */
	@Excel(name = "Notice Content")
	private String noticeContent;

    public void setNoticeId(Long noticeId) 
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId() 
    {
        return noticeId;
    }
    public void setNoticeTitle(String noticeTitle) 
    {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTitle() 
    {
        return noticeTitle;
    }
    public void setNoticeType(String noticeType) 
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType() 
    {
        return noticeType;
    }

	public void setActive(Integer active)
    {
        this.active = active;
    }

	public Integer getActive() 
    {
        return active;
    }
    public void setDateStart(Date dateStart) 
    {
        this.dateStart = dateStart;
    }

    public Date getDateStart() 
    {
        return dateStart;
    }
    public void setDateFinish(Date dateFinish) 
    {
        this.dateFinish = dateFinish;
    }

    public Date getDateFinish() 
    {
        return dateFinish;
    }

	public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

	public String getNoticeContent() 
    {
        return noticeContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("active", getActive())
            .append("dateStart", getDateStart())
            .append("dateFinish", getDateFinish())
            .append("noticeContent", getNoticeContent())
            .toString();
    }
}
