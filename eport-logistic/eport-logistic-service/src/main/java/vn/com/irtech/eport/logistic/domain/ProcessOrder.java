package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Process order Object process_order
 * 
 * @author HieuNT
 * @date 2020-06-23
 */
public class ProcessOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Mã Lô */
    @Excel(name = "Mã Lô")
    private Long shipmentId;

    /** logistics group id */
    @Excel(name = "Logistic Group")
    private Long logisticGroupId;

    /** Loại dịch vụ (bốc, hạ, gate) */
    @Excel(name = "Loại dịch vụ (bốc, hạ, gate)")
    private Integer serviceType;

    /** Mã Tham Chiếu */
    @Excel(name = "Mã Tham Chiếu")
    private String invoiceNo;

    /** PT thanh toán */
    @Excel(name = "PT thanh toán")
    private String payType;

    /** Kích thước cont */
    @Excel(name = "Kích thước cont")
    private String sztp;

    /** Loại lệnh */
    @Excel(name = "Loại lệnh")
    private String modee;

    /** Chủ hàng */
    @Excel(name = "Chủ hàng")
    private String consignee;

    /** MST-Tên cty */
    @Excel(name = "MST-Tên cty")
    private String truckCo;

    /** MST */
    @Excel(name = "MST")
    private String taxCode;

    /** Billing No */
    @Excel(name = "Billing No")
    private String blNo;

    /** Booking no */
    @Excel(name = "Booking no")
    private String bookingNo;

    /** Ngày bốc */
    @Excel(name = "Ngày bốc", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pickupDate;

    /** Tàu */
    @Excel(name = "Tàu")
    private String vessel;

    /** Chuyến */
    @Excel(name = "Chuyến")
    private String voyage;

    /** Trước-Sau */
    @Excel(name = "Trước-Sau")
    private String beforeAfter;

    /** Năm */
    @Excel(name = "Năm")
    private String year;

    /** Số lượng container */
    @Excel(name = "Số lượng container")
    private Integer contNumber;

    /** Trạng thái: 0 waiting, 1: processing, 2:done */
    @Excel(name = "Trạng thái: 0 waiting, 1: processing, 2:done")
    private Integer status;

    /** Kết quả (F:Failed,S:Success) */
    @Excel(name = "Kết quả (F:Failed,S:Success)")
    private String result;

    /** Detail Data (Json) */
    @Excel(name = "Detail Data (Json)")
    private String data;

    /** Robot UUID */
    @Excel(name = "Robot UUID")
    private String robotUuid;

    /** SSR Code */
    @Excel(name = "SSR Code")
    private String ssrCode;

    /** SSR Code */
    @Excel(name = "Sys User Id")
    private Long sysUserId;
    
    /** SSR Code */
    @Excel(name = "postProcessId")
    private Long postProcessId;

    /** SSR Code */
    @Excel(name = "Runnable")
    private Boolean runnable;

    /** Order No */
    private String orderNo;

    private String groupName;

    private ShipmentDetail shipmentDetail;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
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
    public void setInvoiceNo(String invoiceNo) 
    {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() 
    {
        return invoiceNo;
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
    public void setModee(String modee) 
    {
        this.modee = modee;
    }

    public String getModee() 
    {
        return modee;
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

    public String getRobotUuid() {
        return this.robotUuid;
    }

    public void setRobotUuid(String robotUuid) {
        this.robotUuid = robotUuid;
    }

    public String getSsrCode() {
        return this.ssrCode;
    }

    public void setSsrCode(String ssrCode) {
        this.ssrCode = ssrCode;
    }

    public Long getSysUserId() {
        return this.sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getPostProcessId() {
        return this.postProcessId;
    }

    public void setPostProcessId(Long postProcessId) {
        this.postProcessId = postProcessId;
    }

    public Boolean getRunnable() {
        return this.runnable;
    }

    public void setRunnable(Boolean runnable) {
        this.runnable = runnable;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setShipmentDetail(ShipmentDetail shipmentDetail) {
        this.shipmentDetail = shipmentDetail;
    }

    public ShipmentDetail getShipmentDetail() {
        return shipmentDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("shipmentId", getShipmentId())
            .append("serviceType", getServiceType())
            .append("invoiceNo", getInvoiceNo())
            .append("payType", getPayType())
            .append("sztp", getSztp())
            .append("modee", getModee())
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
            .append("robotUuid", getRobotUuid())
            .append("ssrCode", getSsrCode())
            .append("sysUserId", getSysUserId())
            .append("postProcessId", getPostProcessId())
            .append("runnable", getRunnable())
            .append("orderNo", getOrderNo())
            .append("groupName", getGroupName())
            .append("shipmentDetail", getShipmentDetail())
            .toString();
    }
}
