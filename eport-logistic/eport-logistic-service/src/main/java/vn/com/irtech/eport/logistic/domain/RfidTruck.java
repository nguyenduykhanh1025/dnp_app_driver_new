package vn.com.irtech.eport.logistic.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * RFID Truck Object rfid_truck
 * 
 * @author Trong Hieu
 * @date 2020-11-14
 */
public class RfidTruck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

	/** ID */
    private Long id;

	/** RFID */
	@Excel(name = "RFID")
    private String rfid;

	/** Plate Number */
	@Excel(name = "Plate Number")
    private String plateNumber;

	/** Truck Type (T: Truck No, C: Chassis No) */
	@Excel(name = "Truck Type")
    private String truckType;

	/** Gate Pass */
	@Excel(name = "Gate Pass")
    private String gatePass;

	/** Weight */
	@Excel(name = "Weight")
    private Long wgt;

	/** Logistic Group ID */
	@Excel(name = "Logistic Group ID")
    private Long logisticGroupId;

	/** Disabled Status */
	@Excel(name = "Disabled Status")
	private Boolean disabled;

	/** Loadable Weight */
	@Excel(name = "Loadable Weight")
	private Long loadableWgt;

	public void setId(Long id) {
        this.id = id;
    }

	public Long getId() {
        return id;
    }

	public void setRfid(String rfid) {
        this.rfid = rfid;
    }

	public String getRfid() {
        return rfid;
    }

	public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

	public String getPlateNumber() {
        return plateNumber;
    }

	public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

	public String getTruckType() {
        return truckType;
    }

	public void setGatePass(String gatePass) {
        this.gatePass = gatePass;
    }

	public String getGatePass() {
        return gatePass;
    }

	public void setWgt(Long wgt) {
        this.wgt = wgt;
    }

	public Long getWgt() {
        return wgt;
    }

	public void setLogisticGroupId(Long logisticGroupId) {
        this.logisticGroupId = logisticGroupId;
    }

	public Long getLogisticGroupId() {
        return logisticGroupId;
    }

	public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

	public Boolean getDisabled() {
        return disabled;
    }

	public Long getLoadableWgt() {
		return loadableWgt;
	}

	public void setLoadableWgt(Long loadableWgt) {
		this.loadableWgt = loadableWgt;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("rfid", getRfid()).append("plateNumber", getPlateNumber())
				.append("truckType", getTruckType()).append("gatePass", getGatePass()).append("wgt", getWgt())
				.append("logisticGroupId", getLogisticGroupId()).append("disabled", getDisabled())
				.append("createTime", getCreateTime()).append("createBy", getCreateBy())
				.append("updateTime", getUpdateTime()).append("updateBy", getUpdateBy()).append("remark", getRemark())
				.append("loadableWgt", getLoadableWgt()).toString();
    }
}
