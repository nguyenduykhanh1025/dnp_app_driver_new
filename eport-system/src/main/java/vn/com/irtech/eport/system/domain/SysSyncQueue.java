package vn.com.irtech.eport.system.domain;


import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Object sys_sync_queue
 * 
 * @author Trong Hieu
 * @date 2020-11-06
 */
public class SysSyncQueue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** Sync type */
    @Excel(name = "Sync type")
    private String syncType;

    /** Bill no */
    @Excel(name = "Bill no")
    private String blNo;

    /** Container no */
    @Excel(name = "Container no")
    private String cntrNo;

    /** Job order no from catos */
    @Excel(name = "Job order no from catos")
    private String jobOdrNo;

    /** Process order id */
    @Excel(name = "Process order id")
    private Long processOrderId;

	/** Stauts (W: waiting, P: progress, S: success, E: error) */
    @Excel(name = "Stauts")
    private String status;

    /** Retry times */
    @Excel(name = "Retry times")
	private Integer retryTimes;

	/** Expired dem */
	@Excel(name = "Expired dem")
	private Date expiredDem;

	/** Det free time */
	@Excel(name = "Det free time")
	private String detFreeTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSyncType(String syncType) 
    {
        this.syncType = syncType;
    }

    public String getSyncType() 
    {
        return syncType;
    }
    public void setBlNo(String blNo) 
    {
        this.blNo = blNo;
    }

    public String getBlNo() 
    {
        return blNo;
    }
    public void setCntrNo(String cntrNo) 
    {
        this.cntrNo = cntrNo;
    }

    public String getCntrNo() 
    {
        return cntrNo;
    }
    public void setJobOdrNo(String jobOdrNo) 
    {
        this.jobOdrNo = jobOdrNo;
    }

    public String getJobOdrNo() 
    {
        return jobOdrNo;
    }
    public void setProcessOrderId(Long processOrderId) 
    {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() 
    {
        return processOrderId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

	public void setRetryTimes(Integer retryTimes)
    {
        this.retryTimes = retryTimes;
    }

	public Integer getRetryTimes() 
    {
        return retryTimes;
    }

	public Date getExpiredDem() {
		return expiredDem;
	}

	public void setExpiredDem(Date expiredDem) {
		this.expiredDem = expiredDem;
	}

	public String getDetFreeTime() {
		return detFreeTime;
	}

	public void setDetFreeTime(String detFreeTime) {
		this.detFreeTime = detFreeTime;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("syncType", getSyncType()).append("blNo", getBlNo())
				.append("cntrNo", getCntrNo()).append("jobOdrNo", getJobOdrNo())
				.append("processOrderId", getProcessOrderId()).append("status", getStatus())
				.append("retryTimes", getRetryTimes()).append("createTime", getCreateTime())
				.append("createTime", getCreateTime()).append("createTime", getCreateTime())
				.append("expiredDem", getExpiredDem()).append("detFreeTime", getDetFreeTime())
				.append("remark", getRemark()).append("params", getParams())
            .toString();
    }
}
