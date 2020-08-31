package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class MeasurementDataReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String contNo;

	private String weight;

	private String truckNo;

	private String chassisNo;
	
	public String getContNo() {
		return contNo;
	}

	public void setContNo(String contNo) {
		this.contNo = contNo;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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
	
}
