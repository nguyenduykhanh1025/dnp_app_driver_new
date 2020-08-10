package vn.com.irtech.eport.api.form;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.PickupHistory;

public class GateInFormData {

	private List<PickupHistory> pickupIn;
	
	private List<PickupHistory> pickupOut;
	
	private String gatePass;
	
	private String module;
	
	private String truckNo;
	
	private String chassisNo;
	
	private String wgt;
	
	private Integer contNumberIn;
	
	private Integer contNumberOut;
	
	private String gateId;

	private String sessionId;
	
	private Long receiptId;

	public List<PickupHistory> getPickupIn() {
		return pickupIn;
	}

	public void setPickupIn(List<PickupHistory> pickupIn) {
		this.pickupIn = pickupIn;
	}

	public List<PickupHistory> getPickupOut() {
		return pickupOut;
	}

	public void setPickupOut(List<PickupHistory> pickupOut) {
		this.pickupOut = pickupOut;
	}

	public String getGatePass() {
		return gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public Integer getContNumberIn() {
		return contNumberIn;
	}

	public void setContNumberIn(Integer contNumberIn) {
		this.contNumberIn = contNumberIn;
	}

	public Integer getContNumberOut() {
		return contNumberOut;
	}

	public void setContNumberOut(Integer contNumberOut) {
		this.contNumberOut = contNumberOut;
	}
	
	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public Long getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

}
