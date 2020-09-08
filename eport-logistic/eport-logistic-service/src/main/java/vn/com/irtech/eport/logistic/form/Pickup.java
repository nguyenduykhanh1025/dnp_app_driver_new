package vn.com.irtech.eport.logistic.form;

import java.io.Serializable;

public class Pickup implements Serializable {

	private static final long serialVersionUID = 5918642569027193881L;

	private Long pickupId;

    private Integer serviceType;

    private String containerNo;

    private String truckNo;

    private String chassisNo;

    private String sztp;

    private Long batchCode;

    private Integer status;
    
	/** Block */
	private String block;

	/** Bay */
	private String bay;

	/** Line */
	private String line;

	/** Tier */
	private String tier;

	/** Area */
	private String area;


    public Long getPickupId() {
        return this.pickupId;
    }

    public void setPickupId(Long pickupId) {
        this.pickupId = pickupId;
    }

    public Integer getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getTruckNo() {
        return this.truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getChassisNo() {
        return this.chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getSztp() {
        return this.sztp;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }

    public Long getBatchCode() {
        return this.batchCode;
    }

    public void setBatchCode(Long batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getBay() {
		return bay;
	}

	public void setBay(String bay) {
		this.bay = bay;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}