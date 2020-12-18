package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class GateNotificationCheckInReq  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String truckNo;
	
	private String chassisNo;
	
	private String gatePass;
	
	private Integer loadableWgt;
	
	private Integer weight;
	
	private Boolean sendOption;
	
	private Boolean receiveOption;
	
	private String containerSend1;
	
	private String containerSend2;
	
	private String containerReceive1;
	
	private String containerReceive2;
	
	private Boolean refFlg1;
	
	private Boolean refFlg2;
	
	private String refNo1;
	
	private String refNo2;
	
	private Integer deduct;
	
	private String sessionId;
	
	private String gateId;

	private String sendFE1;

	private String sendFE2;

	private String sendSztp1;

	private String sendSztp2;

	private String sendRemark1;

	private String sendRemark2;

	private String sealNo1;

	private String sealNo2;

	private String remark;

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

	public Integer getLoadableWgt() {
		return loadableWgt;
	}

	public void setLoadableWgt(Integer loadableWgt) {
		this.loadableWgt = loadableWgt;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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

	public Boolean getRefFlg1() {
		return refFlg1;
	}

	public void setRefFlg1(Boolean refFlg1) {
		this.refFlg1 = refFlg1;
	}

	public Boolean getRefFlg2() {
		return refFlg2;
	}

	public void setRefFlg2(Boolean refFlg2) {
		this.refFlg2 = refFlg2;
	}

	public String getRefNo1() {
		return refNo1;
	}

	public void setRefNo1(String refNo1) {
		this.refNo1 = refNo1;
	}

	public String getRefNo2() {
		return refNo2;
	}

	public void setRefNo2(String refNo2) {
		this.refNo2 = refNo2;
	}

	public Integer getDeduct() {
		return deduct;
	}

	public void setDeduct(Integer deduct) {
		this.deduct = deduct;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public String getSendFE1() {
		return sendFE1;
	}

	public void setSendFE1(String sendFE1) {
		this.sendFE1 = sendFE1;
	}

	public String getSendFE2() {
		return sendFE2;
	}

	public void setSendFE2(String sendFE2) {
		this.sendFE2 = sendFE2;
	}

	public String getSendSztp1() {
		return sendSztp1;
	}

	public void setSendSztp1(String sendSztp1) {
		this.sendSztp1 = sendSztp1;
	}

	public String getSendSztp2() {
		return sendSztp2;
	}

	public void setSendSztp2(String sendSztp2) {
		this.sendSztp2 = sendSztp2;
	}

	public String getSendRemark1() {
		return sendRemark1;
	}

	public void setSendRemark1(String sendRemark1) {
		this.sendRemark1 = sendRemark1;
	}

	public String getSendRemark2() {
		return sendRemark2;
	}

	public void setSendRemark2(String sendRemark2) {
		this.sendRemark2 = sendRemark2;
	}

	public String getSealNo1() {
		return sealNo1;
	}

	public void setSealNo1(String sealNo1) {
		this.sealNo1 = sealNo1;
	}

	public String getSealNo2() {
		return sealNo2;
	}

	public void setSealNo2(String sealNo2) {
		this.sealNo2 = sealNo2;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
