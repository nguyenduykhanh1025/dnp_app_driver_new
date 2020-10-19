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

	private Long id;

	private String plateNumber;

	private String gatepass;

	private String fullName;

	private String mobileNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
