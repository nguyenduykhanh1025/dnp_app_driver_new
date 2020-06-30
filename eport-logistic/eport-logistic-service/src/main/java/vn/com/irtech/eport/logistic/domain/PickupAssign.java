package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Pickup Assign Object pickup_assign
 * 
 * @author admin
 * @date 2020-06-27
 */
public class PickupAssign extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logistic Group */
    @Excel(name = "Logistic Group")
    private Long logisticGroupId;

    /** Ma Lo */
    @Excel(name = "Ma Lo")
    private Long shipmentId;

    /** ID tài xế */
    @Excel(name = "ID tài xế")
    private Long driverId;

    /** Shipment Detail Id */
    @Excel(name = "Shipment Detail Id")
    private Long shipmentDetailId;

    /** Thue ngoai (0,1) */
    @Excel(name = "Thue ngoai (0,1)")
    private Long externalFlg;

    /** Mã nhận lệnh thuê ngoài */
    @Excel(name = "Mã nhận lệnh thuê ngoài")
    private String externalSecretCode;

    /** Biển số xe đầu kéo (thuê ngoài) */
    @Excel(name = "Biển số xe đầu kéo (thuê ngoài)")
    private String truckNo;

    /** Biển số xe rơ mooc (thuê ngoài) */
    @Excel(name = "Biển số xe rơ mooc (thuê ngoài)")
    private String chassisNo;

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
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setShipmentDetailId(Long shipmentDetailId) 
    {
        this.shipmentDetailId = shipmentDetailId;
    }

    public Long getShipmentDetailId() 
    {
        return shipmentDetailId;
    }
    public void setExternalFlg(Long externalFlg) 
    {
        this.externalFlg = externalFlg;
    }

    public Long getExternalFlg() 
    {
        return externalFlg;
    }
    public void setExternalSecretCode(String externalSecretCode) 
    {
        this.externalSecretCode = externalSecretCode;
    }

    public String getExternalSecretCode() 
    {
        return externalSecretCode;
    }
    public void setTruckNo(String truckNo) 
    {
        this.truckNo = truckNo;
    }

    public String getTruckNo() 
    {
        return truckNo;
    }
    public void setChassisNo(String chassisNo) 
    {
        this.chassisNo = chassisNo;
    }

    public String getChassisNo() 
    {
        return chassisNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("shipmentId", getShipmentId())
            .append("driverId", getDriverId())
            .append("shipmentDetailId", getShipmentDetailId())
            .append("externalFlg", getExternalFlg())
            .append("externalSecretCode", getExternalSecretCode())
            .append("truckNo", getTruckNo())
            .append("chassisNo", getChassisNo())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
