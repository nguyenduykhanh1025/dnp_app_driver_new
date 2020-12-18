package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * @author ThanhD
 * @date 2020-07-28
 */
/**
 * @author KunlezIsme
 *
 */
public class ShipmentImage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long shipmentId;

    private String path;
    
    private String shipmentDetailId;
    
    private String fileType;
    
    private String sztp;
    
    private String oversizeTop;
    
    private String oversizeRight;
    
    private String oversizeLeft;
    
    private String truckNo;
    
	private String chassisNo;
     
	
    public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getOversizeTop() {
		return oversizeTop;
	}

	public void setOversizeTop(String oversizeTop) {
		this.oversizeTop = oversizeTop;
	}

	public String getOversizeRight() {
		return oversizeRight;
	}

	public void setOversizeRight(String oversizeRight) {
		this.oversizeRight = oversizeRight;
	}

	public String getOversizeLeft() {
		return oversizeLeft;
	}

	public void setOversizeLeft(String oversizeLeft) {
		this.oversizeLeft = oversizeLeft;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public String getShipmentDetailId() {
		return shipmentDetailId;
	}

	public void setShipmentDetailId(String shipmentDetailId) {
		this.shipmentDetailId = shipmentDetailId;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("shipmentId", getShipmentId()).append("path", getPath())
				.append("createBy", getCreateBy()).append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy()).append("updateTime", getUpdateTime())
				.append("shipmentDetailId", getShipmentDetailId()).append("params", getParams())
                .toString();
    }
}
