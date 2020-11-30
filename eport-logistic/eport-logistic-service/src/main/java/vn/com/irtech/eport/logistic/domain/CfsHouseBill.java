package vn.com.irtech.eport.logistic.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * CFS House Bill Object cfs_house_bill
 * 
 * @author Trong Hieu
 * @date 2020-11-23
 */
public class CfsHouseBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** logistics group id */
    @Excel(name = "logistics group id")
    private Long logisticGroupId;

    /** shipment detail id */
    @Excel(name = "shipment detail id")
    private Long shipmentDetailId;

    /** house bill */
    @Excel(name = "house bill")
    private String houseBill;

    /** master bill */
    @Excel(name = "master bill")
    private String masterBill;

    /** Số lượng */
    @Excel(name = "Số lượng")
    private Integer quantity;

    /** Loại bao bì */
    @Excel(name = "Loại bao bì")
    private String packagingType;

    /** Số KGS */
    @Excel(name = "Số KGS")
    private String weight;

    /** Số khối */
    @Excel(name = "Số khối")
    private String cubicMeter;

    /** Nhãn / Ký hiệu */
    @Excel(name = "Nhãn / Ký hiệu")
    private String marks;

    /** Forwarder ghi chú */
    @Excel(name = "Forwarder ghi chú")
    private String forwarderRemark;

    /** Chủ hàng */
    @Excel(name = "Chủ hàng")
    private String consignee;

    /** Trạng thái */
    @Excel(name = "Trạng thái")
    private String status;

    /** Lưu kho từ ngày */
    @Excel(name = "Lưu kho từ ngày", width = 30, dateFormat = "yyyy-MM-dd")
    private Date storageFromDate;

    /** Lưu kho đến ngày */
    @Excel(name = "Lưu kho đến ngày", width = 30, dateFormat = "yyyy-MM-dd")
    private Date storageToDate;

    /** Động tác xếp dỡ */
    @Excel(name = "Động tác xếp dỡ")
    private String handingMovement;

    /** Loại dịch vụ: đóng hàng (L : loading), rút hàng (U: unloading) */
    @Excel(name = "Loại dịch vụ: đóng hàng (L : loading), rút hàng (U: unloading)")
    private String serviceType;

    /** Forwarder */
    @Excel(name = "Forwarder")
    private String forwarder;

    /** Loại hàng */
    @Excel(name = "Loại hàng")
    private String cargoDescription;

    /** Nơi giao nhận */
    @Excel(name = "Nơi giao nhận")
    private String placeReceipt;

    /** Ca */
    @Excel(name = "Ca")
    private String shiftReceipt;

    /** Ngày giao nhận */
    @Excel(name = "Ngày giao nhận", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateReceipt;

    /** Thiết bị sử dụng */
    @Excel(name = "Thiết bị sử dụng")
    private String equipment;

     /** Thiết bị sử dụng */
     @Excel(name = "Ngày lưu kho")
     private Date storeFromDate;

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
    public void setShipmentDetailId(Long shipmentDetailId) 
    {
        this.shipmentDetailId = shipmentDetailId;
    }

    public Long getShipmentDetailId() 
    {
        return shipmentDetailId;
    }
    public void setHouseBill(String houseBill) 
    {
        this.houseBill = houseBill;
    }

    public String getHouseBill() 
    {
        return houseBill;
    }
    public void setMasterBill(String masterBill) 
    {
        this.masterBill = masterBill;
    }

    public String getMasterBill() 
    {
        return masterBill;
    }
    public void setQuantity(Integer quantity) 
    {
        this.quantity = quantity;
    }

    public Integer getQuantity() 
    {
        return quantity;
    }
    public void setPackagingType(String packagingType) 
    {
        this.packagingType = packagingType;
    }

    public String getPackagingType() 
    {
        return packagingType;
    }
    public void setWeight(String weight) 
    {
        this.weight = weight;
    }

    public String getWeight() 
    {
        return weight;
    }
    public void setCubicMeter(String cubicMeter) 
    {
        this.cubicMeter = cubicMeter;
    }

    public String getCubicMeter() 
    {
        return cubicMeter;
    }
    public void setMarks(String marks) 
    {
        this.marks = marks;
    }

    public String getMarks() 
    {
        return marks;
    }
    public void setForwarderRemark(String forwarderRemark) 
    {
        this.forwarderRemark = forwarderRemark;
    }

    public String getForwarderRemark() 
    {
        return forwarderRemark;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setStorageFromDate(Date storageFromDate) 
    {
        this.storageFromDate = storageFromDate;
    }

    public Date getStorageFromDate() 
    {
        return storageFromDate;
    }
    public void setStorageToDate(Date storageToDate) 
    {
        this.storageToDate = storageToDate;
    }

    public Date getStorageToDate() 
    {
        return storageToDate;
    }
    public void setHandingMovement(String handingMovement) 
    {
        this.handingMovement = handingMovement;
    }

    public String getHandingMovement() 
    {
        return handingMovement;
    }
    public void setServiceType(String serviceType) 
    {
        this.serviceType = serviceType;
    }

    public String getServiceType() 
    {
        return serviceType;
    }
    public void setForwarder(String forwarder) 
    {
        this.forwarder = forwarder;
    }

    public String getForwarder() 
    {
        return forwarder;
    }
    public void setCargoDescription(String cargoDescription) 
    {
        this.cargoDescription = cargoDescription;
    }

    public String getCargoDescription() 
    {
        return cargoDescription;
    }
    public void setPlaceReceipt(String placeReceipt) 
    {
        this.placeReceipt = placeReceipt;
    }

    public String getPlaceReceipt() 
    {
        return placeReceipt;
    }
    public void setShiftReceipt(String shiftReceipt) 
    {
        this.shiftReceipt = shiftReceipt;
    }

    public String getShiftReceipt() 
    {
        return shiftReceipt;
    }
    public void setDateReceipt(Date dateReceipt) 
    {
        this.dateReceipt = dateReceipt;
    }

    public Date getDateReceipt() 
    {
        return dateReceipt;
    }
    public void setEquipment(String equipment) 
    {
        this.equipment = equipment;
    }

    public String getEquipment() 
    {
        return equipment;
    }
    public void setStoreFromDate(Date storeFromDate) 
    {
        this.storeFromDate = storeFromDate;
    }

    public Date getStoreFromDate() 
    {
        return storeFromDate;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("shipmentDetailId", getShipmentDetailId())
            .append("houseBill", getHouseBill())
            .append("masterBill", getMasterBill())
            .append("quantity", getQuantity())
            .append("packagingType", getPackagingType())
            .append("weight", getWeight())
            .append("cubicMeter", getCubicMeter())
            .append("marks", getMarks())
            .append("forwarderRemark", getForwarderRemark())
            .append("consignee", getConsignee())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("status", getStatus())
            .append("storageFromDate", getStorageFromDate())
            .append("storageToDate", getStorageToDate())
            .append("handingMovement", getHandingMovement())
            .append("serviceType", getServiceType())
            .append("forwarder", getForwarder())
            .append("cargoDescription", getCargoDescription())
            .append("placeReceipt", getPlaceReceipt())
            .append("shiftReceipt", getShiftReceipt())
            .append("dateReceipt", getDateReceipt())
            .append("equipment", getEquipment())
            .append("storeFromDate", getStoreFromDate())
            .toString();
    }
}
