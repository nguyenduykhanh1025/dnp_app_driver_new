/**
 * 
 */
package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class DriverTruckInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String plateNumber;

	private String gatepass;

	private String full_name;

	private String mobileNumber;

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getGatepass() {
		return gatepass;
	}

	public void setGatepass(String gatepass) {
		this.gatepass = gatepass;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
