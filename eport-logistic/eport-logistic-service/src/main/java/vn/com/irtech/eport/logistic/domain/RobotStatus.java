package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Robot Status Object robot_status
 * 
 * @author admin
 * @date 2020-06-16
 */
public class RobotStatus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Dich Vu */
    @Excel(name = "Dich Vu")
    private Integer seviceId;

    /** Robot active */
    @Excel(name = "Robot active")
    private String activeFlag;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setSeviceId(Integer seviceId) 
    {
        this.seviceId = seviceId;
    }

    public Integer getSeviceId() 
    {
        return seviceId;
    }
    public void setActiveFlag(String activeFlag) 
    {
        this.activeFlag = activeFlag;
    }

    public String getActiveFlag() 
    {
        return activeFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("seviceId", getSeviceId())
            .append("activeFlag", getActiveFlag())
            .toString();
    }
}
