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
    @Excel(name = "Logistics Account Id")
    private Long logisticAccountId;

    /** null */
    @Excel(name = "Logistics Group Id")
    private Long logisticGroupId;

    /** Dich Vu */
    @Excel(name = "Dich Vu")
    private Integer serviceType;

    /** Bill No */
    @Excel(name = "B/L No")
    private String blNo;

    /** Booking No */
    @Excel(name = "Booking No")
    private String bookingNo;
    
    /** Mã hãng tàu */
    @Excel(name = "Mã hãng tàu")
    private String opeCode;

    /** MST */
    @Excel(name = "MST")
    private String taxCode;
    
    /** Tên cty theo MST */
    @Excel(name = "Tên công ty")
    private String groupName;

    /** So Luong Container */
    @Excel(name = "So Luong Container")
    private Long containerAmount;

    /** EDO Flag (1,0) */
    @Excel(name = "EDO Flag (1,0)")
    private String edoFlg;

    /** So tham chieu CATOS */
    @Excel(name = "Reference No")
    private String referenceNo;

    /** Ghi chu */
    @Excel(name = "Ghi chu")
    private String remak;

    private LogisticGroup logisticGroup;

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
    public void setServiceType(Integer serviceType) 
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() 
    {
        return serviceType;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }
    
    public void setOpeCode(String opeCode) 
    {
        this.opeCode = opeCode;
    }

    public String getOpeCode() 
    {
        return opeCode;
    }

    public void setTaxCode(String taxCode) 
    {
        this.taxCode = taxCode;
    }

    public String getTaxCode() 
    {
        return taxCode;
    }
    
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
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

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setLogisticGroup(LogisticGroup logisticGroup) {
        this.logisticGroup = logisticGroup;
    }

    public LogisticGroup getLogisticGroup() {
        if (logisticGroup == null) {
            logisticGroup = new LogisticGroup();
        }
        return logisticGroup;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticAccountId", getLogisticAccountId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("serviceType", getServiceType())
            .append("blNo", getBlNo())
            .append("bookingNo", getBookingNo())
            .append("opeCode", getOpeCode())
            .append("taxCode", getTaxCode())
            .append("groupName", getGroupName())
            .append("containerAmount", getContainerAmount())
            .append("edoFlg", getEdoFlg())
            .append("referenceNo", getReferenceNo())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("logisticGroup", getLogisticGroup())
            .toString();
    }
}
