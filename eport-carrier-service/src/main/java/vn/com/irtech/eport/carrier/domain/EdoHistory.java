package vn.com.irtech.eport.carrier.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * eDO Action History Object edo_history
 * 
 * @author admin
 * @date 2020-06-26
 */
public class EdoHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID Nhan Vien Hang Tau */
    @Excel(name = "ID Nhan Vien Hang Tau")
    private Long carrierId;

    /** Ma Hang Tau */
    @Excel(name = "Ma Hang Tau")
    private String carrierCode;

    /** EDO ID */
    @Excel(name = "EDO ID")
    private Long edoId;

    /** So Lenh (Optional) */
    @Excel(name = "So Lenh (Optional)")
    private String orderNumber;

    /** So B/L */
    @Excel(name = "So B/L")
    private String billOfLading;

    /** So Cont */
    @Excel(name = "So Cont")
    private String containerNumber;

    /** Action(insert,update, delete) */
    @Excel(name = "Action(insert,update, delete)")
    private String action;

    /** $column.columnComment */
    @Excel(name = "Action(insert,update, delete)")
    private String ediContent;

   /**  0: chua send mail, 1 : da send mail */
   @Excel(name = " 0: chua send mail, 1 : da send mail")
   private String sendMailFlag;

     /** Ten file EDI */
    @Excel(name = "Ten file EDI")
    private String fileName;
    

    @Excel(name = "Thời gian tạo", dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** Nguon Tao: web, edi, api */
    @Excel(name = "Nguon Tao: web, edi, api")
    private String createSource;

    public void setCreateSource(String createSource) 
    {
        this.createSource = createSource;
    }

    public String getCreateSource() 
    {
        return createSource;
    }

    public void setCreateTime(Date createTime) 
    {
        this.createTime = createTime;
    }

    public Date getCreateTime() 
    {
        return createTime;
    }
   public void setSendMailFlag(String sendMailFlag) 
   {
       this.sendMailFlag = sendMailFlag;
   }

   public String getSendMailFlag() 
   {
       return sendMailFlag;
   }

   public void setFileName(String fileName) 
   {
       this.fileName = fileName;
   }

   public String getFileName() 
   {
       return fileName;
   }

    public void setId(Long id) 
    {
        this.id = id;
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
    public void setEdoId(Long edoId) 
    {
        this.edoId = edoId;
    }

    public Long getEdoId() 
    {
        return edoId;
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
    public void setContainerNumber(String containerNumber) 
    {
        this.containerNumber = containerNumber;
    }

    public String getContainerNumber() 
    {
        return containerNumber;
    }
    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getAction() 
    {
        return action;
    }
    public void setEdiContent(String ediContent) 
    {
        this.ediContent = ediContent;
    }

    public String getEdiContent() 
    {
        return ediContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carrierId", getCarrierId())
            .append("carrierCode", getCarrierCode())
            .append("edoId", getEdoId())
            .append("orderNumber", getOrderNumber())
            .append("billOfLading", getBillOfLading())
            .append("containerNumber", getContainerNumber())
            .append("action", getAction())
            .append("ediContent", getEdiContent())
            .append("fileName", getFileName())
            .append("sendMailFlag", getSendMailFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
