package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Process billing Object process_bill
 * 
 * @author admin
 * @date 2020-06-25
 */
public class ProcessBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Mã Lô */
    @Excel(name = "Mã Lô")
    private Long shipmentId;

    /** Process Order ID */
    @Excel(name = "Process Order ID")
    private Long processOrderId;

    /** Loại dịch vụ (bốc, hạ, gate) */
    @Excel(name = "Loại dịch vụ (bốc, hạ, gate)")
    private Integer serviceType;

    /** Mã Tham Chiếu */
    @Excel(name = "Mã Tham Chiếu")
    private String referenceNo;

    /** Size Type */
    @Excel(name = "Size Type")
    private String sztp;

    /** Phí giao nhận */
    @Excel(name = "Phí giao nhận")
    private Long exchangeFee;

    /** Tỉ lệ % thuế VAT */
    @Excel(name = "Tỉ lệ % thuế VAT")
    private Integer vatRate;

    /** Phí sau thuế VAT */
    @Excel(name = "Phí sau thuế VAT")
    private Long vatAfterFee;

    /** số container */
    @Excel(name = "số container")
    private String containerNo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShipmentId(Long shipmentId) 
    {
        this.shipmentId = shipmentId;
    }

    public Long getShipmentId() 
    {
        return shipmentId;
    }
    public void setProcessOrderId(Long processOrderId) 
    {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() 
    {
        return processOrderId;
    }
    public void setServiceType(Integer serviceType) 
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() 
    {
        return serviceType;
    }
    public void setReferenceNo(String referenceNo) 
    {
        this.referenceNo = referenceNo;
    }

    public String getReferenceNo() 
    {
        return referenceNo;
    }
    public void setSztp(String sztp) 
    {
        this.sztp = sztp;
    }

    public String getSztp() 
    {
        return sztp;
    }
    public void setExchangeFee(Long exchangeFee) 
    {
        this.exchangeFee = exchangeFee;
    }

    public Long getExchangeFee() 
    {
        return exchangeFee;
    }
    public void setVatRate(Integer vatRate) 
    {
        this.vatRate = vatRate;
    }

    public Integer getVatRate() 
    {
        return vatRate;
    }
    public void setVatAfterFee(Long vatAfterFee) 
    {
        this.vatAfterFee = vatAfterFee;
    }

    public Long getVatAfterFee() 
    {
        return vatAfterFee;
    }
    public void setContainerNo(String containerNo) 
    {
        this.containerNo = containerNo;
    }

    public String getContainerNo() 
    {
        return containerNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentId", getShipmentId())
            .append("processOrderId", getProcessOrderId())
            .append("serviceType", getServiceType())
            .append("referenceNo", getReferenceNo())
            .append("sztp", getSztp())
            .append("exchangeFee", getExchangeFee())
            .append("vatRate", getVatRate())
            .append("vatAfterFee", getVatAfterFee())
            .append("containerNo", getContainerNo())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
