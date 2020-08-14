package vn.com.irtech.eport.logistic.domain;


import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Delegate Object logistic_delegated
 * 
 * @author Irtech
 * @date 2020-08-14
 */
public class LogisticDelegated extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logistic ID */
    @Excel(name = "Logistic ID")
    private Long logisticGroupId;

    /** MST Uy Quen */
    @Excel(name = "MST Uy Quen")
    private String delegateTaxCode;

    /** Ten Cty Duoc Uy Quen */
    @Excel(name = "Ten Cty Duoc Uy Quen")
    private String delegateCompany;

    /** Hieu Luc Tu Ngay */
    @Excel(name = "Hieu Luc Tu Ngay", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validFrom;

    /** Den Ngay */
    @Excel(name = "Den Ngay", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validUntil;

    /** Hieu Luc: 0:Invalid, 1:valid */
    @Excel(name = "Hieu Luc: 0:Invalid, 1:valid")
    private Long validFlg;

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
    public void setDelegateTaxCode(String delegateTaxCode) 
    {
        this.delegateTaxCode = delegateTaxCode;
    }

    public String getDelegateTaxCode() 
    {
        return delegateTaxCode;
    }
    public void setDelegateCompany(String delegateCompany) 
    {
        this.delegateCompany = delegateCompany;
    }

    public String getDelegateCompany() 
    {
        return delegateCompany;
    }
    public void setValidFrom(Date validFrom) 
    {
        this.validFrom = validFrom;
    }

    public Date getValidFrom() 
    {
        return validFrom;
    }
    public void setValidUntil(Date validUntil) 
    {
        this.validUntil = validUntil;
    }

    public Date getValidUntil() 
    {
        return validUntil;
    }
    public void setValidFlg(Long validFlg) 
    {
        this.validFlg = validFlg;
    }

    public Long getValidFlg() 
    {
        return validFlg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("delegateTaxCode", getDelegateTaxCode())
            .append("delegateCompany", getDelegateCompany())
            .append("validFrom", getValidFrom())
            .append("validUntil", getValidUntil())
            .append("validFlg", getValidFlg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
