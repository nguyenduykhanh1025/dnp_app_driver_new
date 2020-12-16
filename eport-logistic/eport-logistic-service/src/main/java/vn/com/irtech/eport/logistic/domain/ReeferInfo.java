package vn.com.irtech.eport.logistic.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Reefer Object reefer_info
 * 
 * @author Khanh
 * @date 2020-12-07
 */
public class ReeferInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** Ngày cài nhiệt */
    @Excel(name = "Ngày cài nhiệt", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateSetPower;

    /** Ngày rút nhiệt */
    @Excel(name = "Ngày rút nhiệt", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateGetPower;

    /** Số giờ cắm */
    @Excel(name = "Số giờ cắm")
    private Long hourNumber;

    /** Loại thanh toán */
    @Excel(name = "Loại thanh toán")
    private String payType;

    /** Loại người thanh toán */
    @Excel(name = "Loại người thanh toán")
    private String payerType;

    /** Số tiền trả */
    @Excel(name = "Số tiền trả")
    private Float moneyNumber;

    /** Trạng thái */
    @Excel(name = "Trạng thái")
    private String status;

    /** shipment detail id */
    @Excel(name = "shipment detail id")
    private Long shipmentDetailId;

    /** logistic group iid */
    @Excel(name = "logistic group iid")
    private Long logisticGroupId;
    
    @Excel(name = "Payment status")
    private String paymentStatus;
    
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDateSetPower(Date dateSetPower) 
    {
        this.dateSetPower = dateSetPower;
    }

    public Date getDateSetPower() 
    {
        return dateSetPower;
    }
    public void setDateGetPower(Date dateGetPower) 
    {
        this.dateGetPower = dateGetPower;
    }

    public Date getDateGetPower() 
    {
        return dateGetPower;
    }
    public void setHourNumber(Long hourNumber) 
    {
        this.hourNumber = hourNumber;
    }

    public Long getHourNumber() 
    {
        return hourNumber;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setPayerType(String payerType) 
    {
        this.payerType = payerType;
    }

    public String getPayerType() 
    {
        return payerType;
    }
    public void setMoneyNumber(Float moneyNumber) 
    {
        this.moneyNumber = moneyNumber;
    }

    public Float getMoneyNumber() 
    {
        return moneyNumber;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setShipmentDetailId(Long shipmentDetailId) 
    {
        this.shipmentDetailId = shipmentDetailId;
    }

    public Long getShipmentDetailId() 
    {
        return shipmentDetailId;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }

    
    public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dateSetPower", getDateSetPower())
            .append("dateGetPower", getDateGetPower())
            .append("hourNumber", getHourNumber())
            .append("payType", getPayType())
            .append("payerType", getPayerType())
            .append("moneyNumber", getMoneyNumber())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("shipmentDetailId", getShipmentDetailId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("paymentStatus", getPaymentStatus())
            .toString();
    }
}