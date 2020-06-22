package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * queue order Object queue_order
 * 
 * @author admin
 * @date 2020-06-19
 */
public class QueueOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Loại lệnh */
    @Excel(name = "Loại lệnh")
    private String mode;

    /** Chủ hàng */
    @Excel(name = "Chủ hàng")
    private String consignee;

    /** MST-Tên cty */
    @Excel(name = "MST-Tên cty")
    private String truckCo;

    /** MST */
    @Excel(name = "MST")
    private String taxCode;

    /** PT thanh toán */
    @Excel(name = "PT thanh toán")
    private String payType;

    /** Billing No */
    @Excel(name = "Billing No")
    private String blNo;

    /** Ngày bốc */
    @Excel(name = "Ngày bốc", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pickupDate;

    /** Vessel */
    @Excel(name = "Vessel")
    private String vessel;

    /** Voyage */
    @Excel(name = "Voyage")
    private String voyage;

    /** year */
    @Excel(name = "year")
    private String year;

    /** Loại hóa đơn */
    @Excel(name = "Loại hóa đơn")
    private String invoiceType;

    /** Mẫu hóa đơn */
    @Excel(name = "Mẫu hóa đơn")
    private String invoiceTemplate;

    /** Số container */
    @Excel(name = "Số container")
    private Integer contNumber;

    /** Trước-Sau */
    @Excel(name = "Trước-Sau")
    private String beforeAfter;

    /** Booking no */
    @Excel(name = "Booking no")
    private String bookingNo;

    /** Kích thước cont */
    @Excel(name = "Kích thước cont")
    private String sizeType;

    /** Dich vu */
    @Excel(name = "Dich vu")
    private String serviceId;
    
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMode(String mode) 
    {
        this.mode = mode;
    }

    public String getMode() 
    {
        return mode;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setTruckCo(String truckCo) 
    {
        this.truckCo = truckCo;
    }

    public String getTruckCo() 
    {
        return truckCo;
    }
    public void setTaxCode(String taxCode) 
    {
        this.taxCode = taxCode;
    }

    public String getTaxCode() 
    {
        return taxCode;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setBlNo(String blNo) 
    {
        this.blNo = blNo;
    }

    public String getBlNo() 
    {
        return blNo;
    }
    public void setPickupDate(Date pickupDate) 
    {
        this.pickupDate = pickupDate;
    }

    public Date getPickupDate() 
    {
        return pickupDate;
    }
    public void setVessel(String vessel) 
    {
        this.vessel = vessel;
    }

    public String getVessel() 
    {
        return vessel;
    }
    public void setVoyage(String voyage) 
    {
        this.voyage = voyage;
    }

    public String getVoyage() 
    {
        return voyage;
    }
    public void setYear(String year) 
    {
        this.year = year;
    }

    public String getYear() 
    {
        return year;
    }
    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }
    public void setInvoiceTemplate(String invoiceTemplate) 
    {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceTemplate() 
    {
        return invoiceTemplate;
    }
    public void setContNumber(Integer contNumber) 
    {
        this.contNumber = contNumber;
    }

    public Integer getContNumber() 
    {
        return contNumber;
    }
    public void setBeforeAfter(String beforeAfter) 
    {
        this.beforeAfter = beforeAfter;
    }

    public String getBeforeAfter() 
    {
        return beforeAfter;
    }
    public void setBookingNo(String bookingNo) 
    {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() 
    {
        return bookingNo;
    }
    public void setSizeType(String sizeType) 
    {
        this.sizeType = sizeType;
    }

    public String getSizeType() 
    {
        return sizeType;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mode", getMode())
            .append("consignee", getConsignee())
            .append("truckCo", getTruckCo())
            .append("taxCode", getTaxCode())
            .append("payType", getPayType())
            .append("blNo", getBlNo())
            .append("pickupDate", getPickupDate())
            .append("vessel", getVessel())
            .append("voyage", getVoyage())
            .append("year", getYear())
            .append("invoiceType", getInvoiceType())
            .append("invoiceTemplate", getInvoiceTemplate())
            .append("contNumber", getContNumber())
            .append("beforeAfter", getBeforeAfter())
            .append("bookingNo", getBookingNo())
            .append("sizeType", getSizeType())
            .append("serviceId", getServiceId())
            .toString();
    }
}
