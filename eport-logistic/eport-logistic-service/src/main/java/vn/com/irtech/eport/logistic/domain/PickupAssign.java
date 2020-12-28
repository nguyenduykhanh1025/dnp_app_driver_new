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
    
    /** Đơn vị chủ quản ( thuê ngoài) */
    @Excel(name = "Đơn vị chủ quản")
    private String driverOwner;

    /** Số điện thoại (thuê ngoài) */
    @Excel(name = "Số điện thoại")
    private String phoneNumber;

    /** Họ và tên (thuê ngoài) */
    @Excel(name = "Họ và tên")
    private String fullName;
    
    /** Biển số xe đầu kéo (thuê ngoài) */
    @Excel(name = "Biển số xe đầu kéo (thuê ngoài)")
    private String truckNo;

    /** Biển số xe rơ mooc (thuê ngoài) */
    @Excel(name = "Biển số xe rơ mooc (thuê ngoài)")
    private String chassisNo;
    
    /** Địa chỉ giao/nhận */
    @Excel(name = "Địa chỉ giao")
    private String deliveryAddress;
    
    /** Số điện thoại giao/nhận */
    @Excel(name = "Số điện thoại giao")
    private String deliveryPhoneNumber;

	/** Service type */
	@Excel(name = "Service type")
	private Integer serviceType;

	/** Position */
	@Excel(name = "Position")
	private String position;

	/** Status 1: Init, 2: position, 3: complete */
	@Excel(name = "Status")
	private Integer status;

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
    
    public void setDriverOwner(String driverOwner) 
    {
        this.driverOwner = driverOwner;
    }

    public String getDriverOwner() 
    {
        return driverOwner;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }
    public void setFullName(String fullName) 
    {
        this.fullName = fullName;
    }

    public String getFullName() 
    {
        return fullName;
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

    public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryPhoneNumber() {
		return deliveryPhoneNumber;
	}

	public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
		this.deliveryPhoneNumber = deliveryPhoneNumber;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("logisticGroupId", getLogisticGroupId())
				.append("shipmentId", getShipmentId()).append("driverId", getDriverId())
				.append("shipmentDetailId", getShipmentDetailId()).append("externalFlg", getExternalFlg())
				.append("externalSecretCode", getExternalSecretCode()).append("driverOwner", getDriverOwner())
				.append("phoneNumber", getPhoneNumber()).append("fullName", getFullName())
				.append("truckNo", getTruckNo()).append("chassisNo", getChassisNo()).append("remark", getRemark())
				.append("deliveryAddress", getDeliveryAddress()).append("deliveryPhoneNumber", getDeliveryPhoneNumber())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime()).append("params", getParams())
				.append("serviceType", getServiceType()).append("position", getPosition()).append("status", getStatus())
				.toString();
    }
}
