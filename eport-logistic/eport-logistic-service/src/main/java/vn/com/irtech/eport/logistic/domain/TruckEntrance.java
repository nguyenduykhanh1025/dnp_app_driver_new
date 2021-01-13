package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Truck Entrance Object truck_entrance
 * 
 * @author Trong Hieu
 * @date 2020-12-31
 */
public class TruckEntrance extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Long id;

	/** Rfid */
	@Excel(name = "Rfid")
	private String rfid;

	/** Truck No */
	@Excel(name = "Truck No")
	private String truckNo;

	/** Chassis No */
	@Excel(name = "Chassis No")
	private String chassisNo;

	/** Active 0: deactive 1: active */
	@Excel(name = "Active 0: deactive 1: active")
	private Boolean active;

	/** Logistics Group Id */
	@Excel(name = "Logistics Group Id")
	private Long logisticGroupId;

	/** Loadable Weight */
	@Excel(name = "Loadable Weight")
	private Long loadableWgt;

	/** Weight */
	@Excel(name = "Weight")
	private Long wgt;

	/** ContainerNo First */
	@Excel(name = "containerNoFirst")
	private String containerNoFirst;

	/** Container No Second */
	@Excel(name = "containerNoSecond")
	private String containerNoSecond;

	/** position first */
	@Excel(name = "position first")
	private String positionFirst;

	/** position second */
	@Excel(name = "position second")
	private String positionSecond;

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

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setLoadableWgt(Long loadableWgt) {
		this.loadableWgt = loadableWgt;
	}

	public Long getLoadableWgt() {
		return loadableWgt;
	}

	public void setWgt(Long wgt) {
		this.wgt = wgt;
	}

	public Long getWgt() {
		return wgt;
	}

	public String getContainerNoFirst() {
		return containerNoFirst;
	}

	public void setContainerNoFirst(String containerNoFirst) {
		this.containerNoFirst = containerNoFirst;
	}

	public String getContainerNoSecond() {
		return containerNoSecond;
	}

	public void setContainerNoSecond(String containerNoSecond) {
		this.containerNoSecond = containerNoSecond;
	}

	public String getPositionFirst() {
		return positionFirst;
	}

	public void setPositionFirst(String positionFirst) {
		this.positionFirst = positionFirst;
	}

	public String getPositionSecond() {
		return positionSecond;
	}

	public void setPositionSecond(String positionSecond) {
		this.positionSecond = positionSecond;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId()).append("rfid", getRfid())
				.append("truckNo", getTruckNo()).append("chassisNo", getChassisNo()).append("active", getActive())
				.append("createTime", getCreateTime()).append("createBy", getCreateBy())
				.append("updateTime", getUpdateTime()).append("updateBy", getUpdateBy())
				.append("logisticGroupId", getLogisticGroupId()).append("loadableWgt", getLoadableWgt())
				.append("wgt", getWgt()).append("params", getParams()).append("containerNoFirst", getContainerNoFirst())
				.append("containerNoSecond", getContainerNoSecond()).append("position first", getPositionFirst())
				.append("position second", getPositionSecond()).toString();
	}
}
