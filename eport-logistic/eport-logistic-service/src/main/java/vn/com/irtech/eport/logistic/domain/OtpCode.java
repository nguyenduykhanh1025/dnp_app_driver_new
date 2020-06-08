package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * otp Code Object otp_code
 * 
 * @author ruoyi
 * @date 2020-06-05
 */
public class OtpCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Shipment detailIds */
    @Excel(name = "Shipment detailIds")
    private String shipmentDetailids;

    /** Phone number */
    @Excel(name = "Phone number")
    private String phoneNumber;

    /** OTP CODE */
    @Excel(name = "OTP CODE")
    private Long optCode;

    /** Status send mess */
    @Excel(name = "Status send mess")
    private String msgStatus;

    /** Verify send mess */
    @Excel(name = "Verify send mess")
    private String verifyStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShipmentDetailids(String shipmentDetailids) 
    {
        this.shipmentDetailids = shipmentDetailids;
    }

    public String getShipmentDetailids() 
    {
        return shipmentDetailids;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }
    public void setOptCode(Long optCode) 
    {
        this.optCode = optCode;
    }

    public Long getOptCode() 
    {
        return optCode;
    }
    public void setMsgStatus(String msgStatus) 
    {
        this.msgStatus = msgStatus;
    }

    public String getMsgStatus() 
    {
        return msgStatus;
    }
    public void setVerifyStatus(String verifyStatus) 
    {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyStatus() 
    {
        return verifyStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentDetailids", getShipmentDetailids())
            .append("phoneNumber", getPhoneNumber())
            .append("optCode", getOptCode())
            .append("msgStatus", getMsgStatus())
            .append("verifyStatus", getVerifyStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
