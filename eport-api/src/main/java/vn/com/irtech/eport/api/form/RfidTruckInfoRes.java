/**
 * 
 */
package vn.com.irtech.eport.api.form;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class RfidTruckInfoRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String truckNo;

	private String chassisNo;

	private Long deduct;

	private String gatePass;

	private Long truckNoWgt;

	private Long chassisNoWgt;

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

	public Long getDeduct() {
		return deduct;
	}

	public void setDeduct(Long deduct) {
		this.deduct = deduct;
	}

	public String getGatePass() {
		return gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
	}

	public Long getTruckNoWgt() {
		return truckNoWgt;
	}

	public void setTruckNoWgt(Long truckNoWgt) {
		this.truckNoWgt = truckNoWgt;
	}

	public Long getChassisNoWgt() {
		return chassisNoWgt;
	}

	public void setChassisNoWgt(Long chassisNoWgt) {
		this.chassisNoWgt = chassisNoWgt;
	}

}
