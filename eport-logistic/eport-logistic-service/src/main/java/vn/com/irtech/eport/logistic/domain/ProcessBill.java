package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    
    /** Mã logistic */
    @Excel(name = "Mã logistic")
    private Long logisticGroupId;

    /** Process Order ID */
    @Excel(name = "Process Order ID")
    private Long processOrderId;

    private ProcessOrder processOrder;

    /** Loại dịch vụ (bốc, hạ, gate) */
    @Excel(name = "Loại dịch vụ (bốc, hạ, gate)")
    private Integer serviceType;

    /** Mã Tham Chiếu */
    @Excel(name = "Mã Tham Chiếu")
    private String invoiceNo;
    
    /** PT thanh toán */
    @Excel(name = "PT Thanh Toán")
    private String payType;

    /** Payment Status */
    @Excel(name = "Payment Status")
    private String paymentStatus;

    /** Update Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    private String blNo;

    private String bookingNo;

    private String taxCode;

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
    
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    
    public void setProcessOrderId(Long processOrderId) 
    {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() 
    {
        return processOrderId;
    }

    public void setProcessOrder(ProcessOrder processOrder) {
        this.processOrder = processOrder;
    }

    public ProcessOrder getProcessOrder() {
        if (processOrder == null) {
            processOrder = new ProcessOrder();
        }
        return processOrder;
    }

    public void setServiceType(Integer serviceType) 
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() 
    {
        return serviceType;
    }
    
    public void setPayType(String payType) {
    	this.payType = payType;
    }
    
    public String getPayType() {
    	return payType;
    }
    
    public void setPaymentStatus(String paymentStatus) {
    	this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentStatus() {
    	return paymentStatus;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setInvoiceNo(String invoiceNo) 
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() 
    {
        return invoiceNo;
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

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
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

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentId", getShipmentId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("processOrderId", getProcessOrderId())
            .append("processOrder", getProcessOrder())
            .append("serviceType", getServiceType())
            .append("payType", getPayType())
            .append("paymentStatus", getPaymentStatus())
            .append("invoiceNo", getInvoiceNo())
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
            .append("fromDate", getFromDate())
            .append("toDate", getToDate())
            .append("blNo", getBlNo())
            .append("bookingNo", getBookingNo())
            .append("taxCode", getTaxCode())
            .toString();
    }
}
