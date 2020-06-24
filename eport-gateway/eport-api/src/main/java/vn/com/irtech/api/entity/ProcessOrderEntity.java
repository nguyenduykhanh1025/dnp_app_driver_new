package vn.com.irtech.api.entity;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ProcessOrderEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Mã Lô */
    private Long shipmentId;

    /** Loại dịch vụ (bốc, hạ, gate) */
    private Integer serviceType;

    /** Mã Tham Chiếu */
    private String referenceNo;

    /** PT thanh toán */
    private String payType;

    /** Kích thước cont */
    private String sztp;

    /** Loại lệnh */
    private String mode;

    /** Chủ hàng */
    private String consignee;

    /** MST-Tên cty */
    private String truckCo;

    /** MST */
    private String taxCode;

    /** Billing No */
    private String blNo;

    /** Booking no */
    private String bookingNo;

    /** Ngày bốc */
    private Date pickupDate;

    /** Tàu */
    private String vessel;

    /** Chuyến */
    private String voyage;

    /** Trước-Sau */
    private String beforeAfter;

    /** Năm */
    private String year;

    /** Số lượng container */
    private Integer contNumber;

    /** Trạng thái: 0 waiting, 1: processing, 2:done */
    private Integer status;

    /** Kết quả (F:Failed,S:Success) */
    private String result;

    /** Detail Data (Json) */
    private String data;

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
    public void setServiceType(Integer serviceType) 
    {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() 
    {
        return serviceType;
    }
    public void setReferenceNo(String referenceNo) 
    {
        this.referenceNo = referenceNo;
    }

    public String getReferenceNo() 
    {
        return referenceNo;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setSztp(String sztp) 
    {
        this.sztp = sztp;
    }

    public String getSztp() 
    {
        return sztp;
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
    public void setBlNo(String blNo) 
    {
        this.blNo = blNo;
    }

    public String getBlNo() 
    {
        return blNo;
    }
    public void setBookingNo(String bookingNo) 
    {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() 
    {
        return bookingNo;
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
    public void setBeforeAfter(String beforeAfter) 
    {
        this.beforeAfter = beforeAfter;
    }

    public String getBeforeAfter() 
    {
        return beforeAfter;
    }
    public void setYear(String year) 
    {
        this.year = year;
    }

    public String getYear() 
    {
        return year;
    }
    public void setContNumber(Integer contNumber) 
    {
        this.contNumber = contNumber;
    }

    public Integer getContNumber() 
    {
        return contNumber;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }
    public void setData(String data) 
    {
        this.data = data;
    }

    public String getData() 
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentId", getShipmentId())
            .append("serviceType", getServiceType())
            .append("referenceNo", getReferenceNo())
            .append("payType", getPayType())
            .append("sztp", getSztp())
            .append("mode", getMode())
            .append("consignee", getConsignee())
            .append("truckCo", getTruckCo())
            .append("taxCode", getTaxCode())
            .append("blNo", getBlNo())
            .append("bookingNo", getBookingNo())
            .append("pickupDate", getPickupDate())
            .append("vessel", getVessel())
            .append("voyage", getVoyage())
            .append("beforeAfter", getBeforeAfter())
            .append("year", getYear())
            .append("contNumber", getContNumber())
            .append("status", getStatus())
            .append("result", getResult())
            .append("data", getData())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
