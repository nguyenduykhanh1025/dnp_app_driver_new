package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Payment History Object payment_history
 * 
 * @author HieuNT
 * @date 2020-07-22
 */
public class PaymentHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** USER ID */
    @Excel(name = "USER ID")
    private Long userId;

    /** PROCESS ORDER IDS */
    @Excel(name = "PROCESS ORDER IDS")
    private String processOrderIds;

    /** SHIPMENT ID */
    @Excel(name = "SHIPMENT ID")
    private Long shipmentId;
    
    /** ORDER ID */
    @Excel(name = "ORDER ID")
    private String orderId;

    /** AMOUNT */
    @Excel(name = "AMOUNT")
    private Long amount;
    
    /** STATUS */
    @Excel(name = "STATUS")
    private String status;

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProcessOrderIds() {
		return processOrderIds;
	}

	public void setProccessOrderIds(String processOrderIds) {
		this.processOrderIds = processOrderIds;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** EXPIRE TIME */
    @Excel(name = "EXPIRE TIME")
    private Date expireTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("processOrderIds", getProcessOrderIds())
            .append("shipmentId", getShipmentId())
            .append("orderId", getOrderId())
            .append("amount", getAmount())
            .append("status", getStatus())
            .append("expireTime", getExpireTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
