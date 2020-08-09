package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * otp Code Object otp_code
 * 
 * @author irtech
 * @date 2020-06-05
 */
public class OtpCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** OTP CODE */
    @Excel(name = "OTP CODE")
    private String optCode;

    /** transaction id */
    @Excel(name = "transaction id")
    private String transactionId;

    /** Loai OTP (1 - lamlenh, 2 quen mat khau) */
    @Excel(name = "Loai OTP (1 - lamlenh, 2 quen mat khau)")
    private String otpType;
    
    /** Số điện thoại nhận */
    @Excel(name = "Số điện thoại nhận")
    private String phoneNumber;

    
    /** Status send mess */
    @Excel(name = "Status send mess")
    private String msgStatus;

    /** Verify send mess */
    @Excel(name = "Verify send mess")
    private String verifyStatus;

    /** Expired Time */
    @Excel(name = "Expired Time", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiredTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOtpCode(String optCode) 
    {
        this.optCode = optCode;
    }

    public String getOtpCode() 
    {
        return optCode;
    }
    public void setTransactionId(String transactionId) 
    {
        this.transactionId = transactionId;
    }

    public String getTransactionId() 
    {
        return transactionId;
    }
    public void setOtpType(String otpType) 
    {
        this.otpType = otpType;
    }

    public String getOtpType() 
    {
        return otpType;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
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
    public void setExpiredTime(Date expiredTime) 
    {
        this.expiredTime = expiredTime;
    }

    public Date getExpiredTime() 
    {
        return expiredTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("optCode", getOtpCode())
            .append("transactionId", getTransactionId())
            .append("otpType", getOtpType())
            .append("msgStatus", getMsgStatus())
            .append("verifyStatus", getVerifyStatus())
            .append("expiredTime", getExpiredTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
