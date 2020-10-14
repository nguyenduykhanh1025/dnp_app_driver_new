/**
 * 
 */
package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trong Hieu
 *
 */
public class EdoPublicRes implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Bill of lading */
	private String billOfLading;

	/** Container No */
	private String containerNumber;

	/** OPR */
	private String businessUnit;
	
	/** Gate-out Date */
	private Date gateOutDate;

	/** Gate-in Date */
	private Date gateInDate;

	/** Vessel */
	private String vessel;

	/** Voyage */
	private String voyNo;

	/** Expired Date */
	private Date expiredDem;

	/** Detention Date */
	private String detFreeTime;

	/** Empty Return Place */
	private String emptyContainerDepot;
	
	/** Yard position */
	private String location;

	/** Remark */
	private String remark;

	/**
	 * Container State: 
	 * 'R': 'Before Reconcile' 
	 * 'B': 'After Reconcile' 
	 * 'M': 'Manifested' 
	 * 'Y': 'Stacking' 
	 * 'G': 'In Progress Outgoing' 
	 * 'O': 'In Progress Incoming' 
	 * 'P': 'Assigned by Trucker' 
	 * 'Z': 'Under Shifting' 
	 * 'D': 'Delivered'
	 * 'E': 'Empty Gate Out'
	 */
	private String cntrState;

	public String getBillOfLading() {
		return billOfLading;
	}

	public void setBillOfLading(String billOfLading) {
		this.billOfLading = billOfLading;
	}

	public String getContainerNumber() {
		return containerNumber;
	}

	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public Date getGateOutDate() {
		return gateOutDate;
	}

	public void setGateOutDate(Date gateOutDate) {
		this.gateOutDate = gateOutDate;
	}

	public Date getGateInDate() {
		return gateInDate;
	}

	public void setGateInDate(Date gateInDate) {
		this.gateInDate = gateInDate;
	}

	public String getVessel() {
		return vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyNo() {
		return voyNo;
	}

	public void setVoyNo(String voyNo) {
		this.voyNo = voyNo;
	}

	public Date getExpiredDem() {
		return expiredDem;
	}

	public void setExpiredDem(Date expiredDem) {
		this.expiredDem = expiredDem;
	}

	public String getDetFreeTime() {
		return detFreeTime;
	}

	public void setDetFreeTime(String detFreeTime) {
		this.detFreeTime = detFreeTime;
	}

	public String getEmptyContainerDepot() {
		return emptyContainerDepot;
	}

	public void setEmptyContainerDepot(String emptyContainerDepot) {
		this.emptyContainerDepot = emptyContainerDepot;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCntrState() {
		return cntrState;
	}

	public void setCntrState(String cntrState) {
		this.cntrState = cntrState;
	}

}
