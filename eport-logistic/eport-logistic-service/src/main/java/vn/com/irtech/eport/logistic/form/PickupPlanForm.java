/**
 * 
 */
package vn.com.irtech.eport.logistic.form;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trong Hieu
 *
 */
public class PickupPlanForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double distance;
	
	private String containerNo;
	
	private String vslNmVoyNo;
	
	private String sztp;
	
	private String dischargePort;
	
	private String logisticName;
	
	private String truckNo;
	
	private String gatePass;
	
	private Date updateLocationTime;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getVslNmVoyNo() {
		return vslNmVoyNo;
	}

	public void setVslNmVoyNo(String vslNmVoyNo) {
		this.vslNmVoyNo = vslNmVoyNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getDischargePort() {
		return dischargePort;
	}

	public void setDischargePort(String dischargePort) {
		this.dischargePort = dischargePort;
	}

	public String getLogisticName() {
		return logisticName;
	}

	public void setLogisticName(String logisticName) {
		this.logisticName = logisticName;
	}

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

	public Date getUpdateLocationTime() {
		return updateLocationTime;
	}

	public void setUpdateLocationTime(Date updateLocationTime) {
		this.updateLocationTime = updateLocationTime;
	}
	
}
