/**
 * 
 */
package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class PickupAssignInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String truckNo;

	private String gatePass;

	private String driverName;

	private String phoneNumber;

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getGatePass() {
		return gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
