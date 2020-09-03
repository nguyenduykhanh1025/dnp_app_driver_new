package vn.com.irtech.eport.logistic.domain;


import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private Long logisticGroupId;

    /** MST Uy Quen */
    @Excel(name = "MST Uy Quen")
    @NotBlank
    private String delegateTaxCode;

    /** Ten Cty Duoc Uy Quen */
    @Excel(name = "Ten Cty Duoc Uy Quen")
    @NotBlank
    private String delegateCompany;
    
    /** Loai Uy Quyen */
    @Excel(name = "Loai Uy Quyen")
    private String delegateType;

    /** Hieu Luc Tu Ngay */
    @Excel(name = "Hieu Luc Tu Ngay", width = 30, dateFormat = "yyyy-MM-dd")
    @NotNull
    private Date validFrom;

    /** Den Ngay */
    @Excel(name = "Den Ngay", width = 30, dateFormat = "yyyy-MM-dd")
    @NotNull
    private Date validUntil;

    /** Hieu Luc: 0:Invalid, 1:valid */
    @Excel(name = "Hieu Luc: 0:Invalid, 1:valid")
    private Long validFlg;

    /** Del Flag: 0: Not delete, 1: delete */
    @Excel(name = "Del Flag: 0: Not delete, 1: delete")
    private Integer delFlg;

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
    
    public String getDelegateType() {
		return delegateType;
	}

	public void setDelegateType(String delegateType) {
		this.delegateType = delegateType;
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

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("delegateTaxCode", getDelegateTaxCode())
            .append("delegateCompany", getDelegateCompany())
            .append("delegateType", getDelegateType())
            .append("validFrom", getValidFrom())
            .append("validUntil", getValidUntil())
            .append("validFlg", getValidFlg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlg", getDelFlg())
            .toString();
    }
}
