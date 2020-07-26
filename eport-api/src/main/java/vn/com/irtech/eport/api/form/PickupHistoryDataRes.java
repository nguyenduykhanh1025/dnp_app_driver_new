package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class PickupHistoryDataRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pickupHistoryId;

	private String contNo;

	private Long shipmentId;

	private Long shipmentDetailId;

	private String vessel;

	private String voyage;

	private String sztp;

	private String fe;

	private Integer serviceType;

	private Long weight;

	private String truckNo;

	private String chassisNo;

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

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getVessel() {
		return vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
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

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
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

	public Long getShipmentDetailId() {
		return shipmentDetailId;
	}

	public void setShipmentDetailId(Long shipmentDetailId) {
		this.shipmentDetailId = shipmentDetailId;
	}

}
