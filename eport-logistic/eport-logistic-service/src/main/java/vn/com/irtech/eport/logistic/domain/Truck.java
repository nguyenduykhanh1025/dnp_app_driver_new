package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Truck Object truck
 * 
 * @author admin
 * @date 2020-06-16
 */
public class Truck extends BaseEntity
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

    /** 1:đầu kéo, 0:rơ mooc */
    @Excel(name = "1:đầu kéo, 0:rơ mooc")
    private String type;

    /** Weight */
    @Excel(name = "Weight")
    private Long wgt;

    /** Hạn đăng kiểm */
    @Excel(name = "Hạn đăng kiểm", width = 30, dateFormat = "yyyy-MM-dd")
    private Date registryExpiryDate;

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
    public void setWgt(Long wgt) 
    {
        this.wgt = wgt;
    }

    public Long getWgt() 
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("plateNumber", getPlateNumber())
            .append("type", getType())
            .append("wgt", getWgt())
            .append("registryExpiryDate", getRegistryExpiryDate())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
