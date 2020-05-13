package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Shipment Object shipment
 * 
 * @author admin
 * @date 2020-05-07
 */
public class Shipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** null */
    @Excel(name = "null")
    private Long logisticAccountId;

    /** null */
    @Excel(name = "null")
    private Long logisticGroupId;

    /** Dich Vu */
    @Excel(name = "Dich Vu")
    private Integer serviceId;

    /** MST */
    @Excel(name = "MST")
    private String taxCode;

    /** So Luong Container */
    @Excel(name = "So Luong Container")
    private Long containerAmount;

    /** EDO Flag (1,0) */
    @Excel(name = "EDO Flag (1,0)")
    private String edoFlg;

    /** Ghi chu */
    @Excel(name = "Ghi chu")
    private String remak;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticAccountId(Long logisticAccountId) 
    {
        this.logisticAccountId = logisticAccountId;
    }

    public Long getLogisticAccountId() 
    {
        return logisticAccountId;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setServiceId(Integer serviceId) 
    {
        this.serviceId = serviceId;
    }

    public Integer getServiceId() 
    {
        return serviceId;
    }
    public void setTaxCode(String taxCode) 
    {
        this.taxCode = taxCode;
    }

    public String getTaxCode() 
    {
        return taxCode;
    }
    public void setContainerAmount(Long containerAmount) 
    {
        this.containerAmount = containerAmount;
    }

    public Long getContainerAmount() 
    {
        return containerAmount;
    }
    public void setEdoFlg(String edoFlg) 
    {
        this.edoFlg = edoFlg;
    }

    public String getEdoFlg() 
    {
        return edoFlg;
    }
    public void setRemak(String remak) 
    {
        this.remak = remak;
    }

    public String getRemak() 
    {
        return remak;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticAccountId", getLogisticAccountId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("serviceId", getServiceId())
            .append("taxCode", getTaxCode())
            .append("containerAmount", getContainerAmount())
            .append("edoFlg", getEdoFlg())
            .append("remak", getRemak())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
