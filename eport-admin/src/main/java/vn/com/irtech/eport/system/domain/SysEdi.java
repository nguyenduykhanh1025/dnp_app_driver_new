package vn.com.irtech.eport.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】 Object sys_edi
 * 
 * @author ruoyi
 * @date 2020-03-31
 */
public class SysEdi extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long ediId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String buildNo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String businessUnit;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String contNo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String orderNo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String releaseTo;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date validtoDay;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String emptycontDepot;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long haulage;

    public void setEdiId(Long ediId) 
    {
        this.ediId = ediId;
    }

    public Long getEdiId() 
    {
        return ediId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setBuildNo(String buildNo) 
    {
        this.buildNo = buildNo;
    }

    public String getBuildNo() 
    {
        return buildNo;
    }
    public void setBusinessUnit(String businessUnit) 
    {
        this.businessUnit = businessUnit;
    }

    public String getBusinessUnit() 
    {
        return businessUnit;
    }
    public void setContNo(String contNo) 
    {
        this.contNo = contNo;
    }

    public String getContNo() 
    {
        return contNo;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setReleaseTo(String releaseTo) 
    {
        this.releaseTo = releaseTo;
    }

    public String getReleaseTo() 
    {
        return releaseTo;
    }
    public void setValidtoDay(Date validtoDay) 
    {
        this.validtoDay = validtoDay;
    }

    public Date getValidtoDay() 
    {
        return validtoDay;
    }
    public void setEmptycontDepot(String emptycontDepot) 
    {
        this.emptycontDepot = emptycontDepot;
    }

    public String getEmptycontDepot() 
    {
        return emptycontDepot;
    }
    public void setHaulage(Long haulage) 
    {
        this.haulage = haulage;
    }

    public Long getHaulage() 
    {
        return haulage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ediId", getEdiId())
            .append("userId", getUserId())
            .append("buildNo", getBuildNo())
            .append("businessUnit", getBusinessUnit())
            .append("contNo", getContNo())
            .append("orderNo", getOrderNo())
            .append("releaseTo", getReleaseTo())
            .append("validtoDay", getValidtoDay())
            .append("emptycontDepot", getEmptycontDepot())
            .append("haulage", getHaulage())
            .toString();
    }
}
