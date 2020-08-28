package vn.com.irtech.eport.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.google.firebase.database.annotations.NotNull;

public class GateInTestDataReq  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String truckNo;
	
	@NotBlank
	private String chassisNo;
	
	@NotBlank
	private String gatePass;
	
	@NotNull
	private Integer weight;
	
	private String containerSend1;
	
	private String containerSend2;
	
	private String blNo;
	
	private String refNo;
	
	private String containerReceive1;
	
	private String containerReceive2;
	
    private Integer containerAmount;
    
    private String containerFlg;
    
    private Boolean sendOption;
    
    private Boolean receiveOption;
    
    @NotNull
    private Long logisticGroupId;
    
    @NotNull
    private Long driverId;
    
    private String yardPosition1;
    
    private String yardPosition2;

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

	public String getGatePass() {
		return gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getContainerSend1() {
		return containerSend1;
	}

	public void setContainerSend1(String containerSend1) {
		this.containerSend1 = containerSend1;
	}

	public String getContainerSend2() {
		return containerSend2;
	}

	public void setContainerSend2(String containerSend2) {
		this.containerSend2 = containerSend2;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getContainerReceive1() {
		return containerReceive1;
	}

	public void setContainerReceive1(String containerReceive1) {
		this.containerReceive1 = containerReceive1;
	}

	public String getContainerReceive2() {
		return containerReceive2;
	}

	public void setContainerReceive2(String containerReceive2) {
		this.containerReceive2 = containerReceive2;
	}

	public Integer getContainerAmount() {
		return containerAmount;
	}

	public void setContainerAmount(Integer containerAmount) {
		this.containerAmount = containerAmount;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getContainerFlg() {
		return containerFlg;
	}

	public void setContainerFlg(String containerFlg) {
		this.containerFlg = containerFlg;
	}

	public Boolean getSendOption() {
		return sendOption;
	}

	public void setSendOption(Boolean sendOption) {
		this.sendOption = sendOption;
	}

	public Boolean getReceiveOption() {
		return receiveOption;
	}

	public void setReceiveOption(Boolean receiveOption) {
		this.receiveOption = receiveOption;
	}

	public String getYardPosition1() {
		return yardPosition1;
	}

	public void setYardPosition1(String yardPosition1) {
		this.yardPosition1 = yardPosition1;
	}

	public String getYardPosition2() {
		return yardPosition2;
	}

	public void setYardPosition2(String yardPosition2) {
		this.yardPosition2 = yardPosition2;
	}
	
}
