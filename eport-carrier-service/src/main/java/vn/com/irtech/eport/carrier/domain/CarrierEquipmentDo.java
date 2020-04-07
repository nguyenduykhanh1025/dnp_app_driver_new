package vn.com.irtech.eport.carrier.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Exchange Delivery Order Object equipment_do
 * 
 * @author ruoyi
 * @date 2020-04-06
 */
public class CarrierEquipmentDo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID Hang Tau */
    @Excel(name = "ID Hang Tau")
    private Long shippingLineId;

    /** Ma Hang Tau */
    @Excel(name = "Ma Hang Tau")
    private String shippingLineCode;

    /** Document */
    @Excel(name = "Document")
    private Long documentId;

    /** So Lenh */
    @Excel(name = "So Lenh")
    private String orderNumber;

    /** So B/L */
    @Excel(name = "So B/L")
    private String billOfLading;

    /** Don Vi Khai Thac */
    @Excel(name = "Don Vi Khai Thac")
    private String businessUnit;

    /** Chu Hang */
    @Excel(name = "Chu Hang")
    private String consignee;

    /** So Cont */
    @Excel(name = "So Cont")
    private String containerNumber;

    /** Han Lenh */
    @Excel(name = "Han Lenh", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiredDem;

    /** Noi Ha Rong */
    @Excel(name = "Noi Ha Rong")
    private String emptyContainerDepot;

    /** So Ngay Mien Luu Vo Cont */
    @Excel(name = "So Ngay Mien Luu Vo Cont")
    private Integer detFreeTime;

    /** Ma Bao Mat */
    @Excel(name = "Ma Bao Mat")
    private String secureCode;

    /** Ngay Release */
    @Excel(name = "Ngay Release", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releaseDate;

    /** Tau */
    @Excel(name = "Tau")
    private String vessel;

    /** Chuyen */
    @Excel(name = "Chuyen")
    private String voyNo;

    /** DO Status */
    @Excel(name = "DO Status")
    private String status;

    /** Process Status */
    @Excel(name = "Process Status")
    private String processStatus;

    /** Document Status */
    @Excel(name = "Document Status")
    private String documentStatus;

    /** Release Status */
    @Excel(name = "Release Status")
    private String releaseStatus;

    /** Nguon Tao: eport, edi, catos */
    @Excel(name = "Nguon Tao: eport, edi, catos")
    private String createSource;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShippingLineId(Long shippingLineId) 
    {
        this.shippingLineId = shippingLineId;
    }

    public Long getShippingLineId() 
    {
        return shippingLineId;
    }
    public void setShippingLineCode(String shippingLineCode) 
    {
        this.shippingLineCode = shippingLineCode;
    }

    public String getShippingLineCode() 
    {
        return shippingLineCode;
    }
    public void setDocumentId(Long documentId) 
    {
        this.documentId = documentId;
    }

    public Long getDocumentId() 
    {
        return documentId;
    }
    public void setOrderNumber(String orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() 
    {
        return orderNumber;
    }
    public void setBillOfLading(String billOfLading) 
    {
        this.billOfLading = billOfLading;
    }

    public String getBillOfLading() 
    {
        return billOfLading;
    }
    public void setBusinessUnit(String businessUnit) 
    {
        this.businessUnit = businessUnit;
    }

    public String getBusinessUnit() 
    {
        return businessUnit;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setContainerNumber(String containerNumber) 
    {
        this.containerNumber = containerNumber;
    }

    public String getContainerNumber() 
    {
        return containerNumber;
    }
    public void setExpiredDem(Date expiredDem) 
    {
        this.expiredDem = expiredDem;
    }

    public Date getExpiredDem() 
    {
        return expiredDem;
    }
    public void setEmptyContainerDepot(String emptyContainerDepot) 
    {
        this.emptyContainerDepot = emptyContainerDepot;
    }

    public String getEmptyContainerDepot() 
    {
        return emptyContainerDepot;
    }
    public void setDetFreeTime(Integer detFreeTime) 
    {
        this.detFreeTime = detFreeTime;
    }

    public Integer getDetFreeTime() 
    {
        return detFreeTime;
    }
    public void setSecureCode(String secureCode) 
    {
        this.secureCode = secureCode;
    }

    public String getSecureCode() 
    {
        return secureCode;
    }
    public void setReleaseDate(Date releaseDate) 
    {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() 
    {
        return releaseDate;
    }
    public void setVessel(String vessel) 
    {
        this.vessel = vessel;
    }

    public String getVessel() 
    {
        return vessel;
    }
    public void setVoyNo(String voyNo) 
    {
        this.voyNo = voyNo;
    }

    public String getVoyNo() 
    {
        return voyNo;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setProcessStatus(String processStatus) 
    {
        this.processStatus = processStatus;
    }

    public String getProcessStatus() 
    {
        return processStatus;
    }
    public void setDocumentStatus(String documentStatus) 
    {
        this.documentStatus = documentStatus;
    }

    public String getDocumentStatus() 
    {
        return documentStatus;
    }
    public void setReleaseStatus(String releaseStatus) 
    {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseStatus() 
    {
        return releaseStatus;
    }
    public void setCreateSource(String createSource) 
    {
        this.createSource = createSource;
    }

    public String getCreateSource() 
    {
        return createSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shippingLineId", getShippingLineId())
            .append("shippingLineCode", getShippingLineCode())
            .append("documentId", getDocumentId())
            .append("orderNumber", getOrderNumber())
            .append("billOfLading", getBillOfLading())
            .append("businessUnit", getBusinessUnit())
            .append("consignee", getConsignee())
            .append("containerNumber", getContainerNumber())
            .append("expiredDem", getExpiredDem())
            .append("emptyContainerDepot", getEmptyContainerDepot())
            .append("detFreeTime", getDetFreeTime())
            .append("secureCode", getSecureCode())
            .append("releaseDate", getReleaseDate())
            .append("vessel", getVessel())
            .append("voyNo", getVoyNo())
            .append("status", getStatus())
            .append("processStatus", getProcessStatus())
            .append("documentStatus", getDocumentStatus())
            .append("releaseStatus", getReleaseStatus())
            .append("createSource", getCreateSource())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}