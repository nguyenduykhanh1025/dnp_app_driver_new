package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class DriverDataRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pickupHistoryId;
	private String contNo;
	private String yardPosition;
	private String sztp;
	private String fe;
	private String truckNo;
	private String chassisNo;
	private String wgt;

	public Long getPickupHistoryId() {
		return pickupHistoryId;
	}

	public void setPickupHistoryId(Long pickupHistoryId) {
		this.pickupHistoryId = pickupHistoryId;
	}

	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

	public String getYardPosition() {
		return yardPosition;
	}

	public void setYardPosition(String yardPosition) {
		this.yardPosition = yardPosition;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getFe() {
		return fe;
	}

	public void setFe(String fe) {
		this.fe = fe;
	}

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

	public String getWgt() {
		return wgt;
	}

	public void setWgt(String wgt) {
		this.wgt = wgt;
	}

}
