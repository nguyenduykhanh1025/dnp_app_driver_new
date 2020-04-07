package vn.com.irtech.eport.equipment.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Exchange Delivery Order Object equipment_do
 * 
 * @author irtech
 * @date 2020-04-04
 */
public class EquipmentDoPaging extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int page;
    /** ID */
    private Long id;

    /** ID Nhan Vien Hang Tau */
    @Excel(name = "ID Hang Tau")
    private Long carrierId;

    /** Ma Hang Tau */
    @Excel(name = "Ma Hang Tau")
    private String carrierCode;

    /** So Lenh (Optional) */
    private String orderNumber;

    /** So B/L */
    @Excel(name = "So B/L")
    private String billOfLading;

    /** Don Vi Khai Thac (Optional) */
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
    @Excel(name = "So Ngay Mien Luu")
    private Integer detFreeTime;

    /** Ma Bao Mat (optional) */
    private String secureCode;

    /** Ngay Release */
    private Date releaseDate;

    /** Tau */
    @Excel(name = "Tau")
    private String vessel;

    /** Chuyen */
    @Excel(name = "Chuyen")
    private String voyNo;

    /** DO Type */
    private String doType;

    /** DO Status */
    private String status;

    /** Process Status */
    private String processStatus;

    /** Document Status */
    private String documentStatus;

    /** Document Receipt Date */
    private Date documentReceiptDate;

    /** Release Status */
    private String releaseStatus;

    /** Nguon Tao: eport, edi, catos */
    private String createSource;

    public void setPage(int page) 
    {
        this.page = page;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public int page() 
    {
        return page;
    }
    public Long getId() 
    {
        return id;
    }
    public void setCarrierId(Long carrierId) 
    {
        this.carrierId = carrierId;
    }

    public Long getCarrierId() 
    {
        return carrierId;
    }
    public void setCarrierCode(String carrierCode) 
    {
        this.carrierCode = carrierCode;
    }

    public String getCarrierCode() 
    {
        return carrierCode;
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
    public void setDoType(String doType) 
    {
        this.doType = doType;
    }

    public String getDoType() 
    {
        return doType;
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
    public void setDocumentReceiptDate(Date documentReceiptDate) 
    {
        this.documentReceiptDate = documentReceiptDate;
    }

    public Date getDocumentReceiptDate() 
    {
        return documentReceiptDate;
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
            .append("carrierId", getCarrierId())
            .append("carrierCode", getCarrierCode())
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
            .append("doType", getDoType())
            .append("status", getStatus())
            .append("processStatus", getProcessStatus())
            .append("documentStatus", getDocumentStatus())
            .append("documentReceiptDate", getDocumentReceiptDate())
            .append("releaseStatus", getReleaseStatus())
            .append("createSource", getCreateSource())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
