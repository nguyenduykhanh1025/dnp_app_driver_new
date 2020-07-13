package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 *LogisticTruck Object truck
 * 
 * @author admin
 * @date 2020-06-16
 */
public class LogisticTruck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logistic Group */
    @Excel(name = "Logistic Group")
    private Long logisticGroupId;

    /** Bien So Xe */
    @Excel(name = "Bien So Xe")
    private String plateNumber;

    /** 0:đầu kéo, 1:rơ mooc */
    @Excel(name = "type")
    private String type;

    /** Trọng tải bản thân */
    @Excel(name = "Trọng tải bản thân")
    private Integer selfWgt;

    /** Tải trọng cho phép chở */
    @Excel(name = "Tải trọng cho phép chở")
    private Integer wgt;

    /** Hạn đăng kiểm */
    @Excel(name = "Hạn đăng kiểm", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registryExpiryDate;

    /** Delete Flag(default 0) */
    private boolean delFlag;
    
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setPlateNumber(String plateNumber) 
    {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() 
    {
        return plateNumber;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setSelfWgt(Integer selfWgt){
        this.selfWgt = selfWgt;
    }

    public Integer getSelfWgt() 
    {
        return selfWgt;
    }
    public void setWgt(Integer wgt) 
    {
        this.wgt = wgt;
    }

    public Integer getWgt() 
    {
        return wgt;
    }
    public void setRegistryExpiryDate(Date registryExpiryDate) 
    {
        this.registryExpiryDate = registryExpiryDate;
    }

    public Date getRegistryExpiryDate() 
    {
        return registryExpiryDate;
    }

    public void setDelFlag(boolean delFlag) 
    {
        this.delFlag = delFlag;
    }

    public boolean getDelFlag() 
    {
        return delFlag;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("plateNumber", getPlateNumber())
            .append("type", getType())
            .append("selfWgt", getSelfWgt())
            .append("wgt", getWgt())
            .append("registryExpiryDate", getRegistryExpiryDate())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
