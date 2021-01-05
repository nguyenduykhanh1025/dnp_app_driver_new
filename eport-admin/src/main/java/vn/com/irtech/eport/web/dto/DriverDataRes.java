package vn.com.irtech.eport.web.dto;

import java.io.Serializable;

public class DriverDataRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pickupHistoryId;
	private String containerNo;
	private String msg;
	private String result;
	private String serviceName;
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
	
	public String getContainerNo() {
		return containerNo;
	}
	
	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
