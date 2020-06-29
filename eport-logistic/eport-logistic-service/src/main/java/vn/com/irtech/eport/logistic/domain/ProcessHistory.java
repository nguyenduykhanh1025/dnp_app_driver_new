package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Process order history Object process_history
 * 
 * @author HieuNT
 * @date 2020-06-27
 */
public class ProcessHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Process Order ID */
    @Excel(name = "Process Order ID")
    private Long processOrderId;

    /** User ID (OM) */
    @Excel(name = "User ID (OM)")
    private Long sysUserId;

    /** Robot UUID */
    @Excel(name = "Robot UUID")
    private String robotUuid;

    /** Kết Quả (S:Success, F:Failed) */
    @Excel(name = "Kết Quả (S:Success, F:Failed)")
    private String result;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProcessOrderId(Long processOrderId) 
    {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() 
    {
        return processOrderId;
    }
    public void setSysUserId(Long sysUserId) 
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId() 
    {
        return sysUserId;
    }
    public void setRobotUuid(String robotUuid) 
    {
        this.robotUuid = robotUuid;
    }

    public String getRobotUuid() 
    {
        return robotUuid;
    }
    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("processOrderId", getProcessOrderId())
            .append("sysUserId", getSysUserId())
            .append("robotUuid", getRobotUuid())
            .append("result", getResult())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
