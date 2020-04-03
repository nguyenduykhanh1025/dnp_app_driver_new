package vn.com.irtech.eport.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】 Object sys_edi_history
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
public class SysEdiHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ediId;

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
            .append("validtoDay", getValidtoDay())
            .append("emptycontDepot", getEmptycontDepot())
            .append("haulage", getHaulage())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
