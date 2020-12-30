/**
 * 
 */
package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class PickedContAvailableDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pickupHistoryId;

	private String containerNo;

	private String sztp;

	private Long shipmentDetailId;

	private Boolean checked = false;

	private String jobOrderNo;

	private String block;

	private String bay;

	private Integer row;

	private Integer tier;

	public Long getPickupHistoryId() {
		return pickupHistoryId;
	}

	public void setPickupHistoryId(Long pickupHistoryId) {
		this.pickupHistoryId = pickupHistoryId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public Long getShipmentDetailId() {
		return shipmentDetailId;
	}

	public void setShipmentDetailId(Long shipmentDetailId) {
		this.shipmentDetailId = shipmentDetailId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getJobOrderNo() {
		return jobOrderNo;
	}

	public void setJobOrderNo(String jobOrderNo) {
		this.jobOrderNo = jobOrderNo;
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

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

}
