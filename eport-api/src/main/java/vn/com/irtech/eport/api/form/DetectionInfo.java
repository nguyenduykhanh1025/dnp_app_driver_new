package vn.com.irtech.eport.api.form;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class DetectionInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String gateId;
	
	private String containerNo1;
	
	private String containerNo2;
	
//	@NotEmpty
	private String truckNo;
	
//	@NotEmpty
	private String chassisNo;
	
	private Boolean detectionFlg = true;

	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public String getContainerNo1() {
		return containerNo1;
	}

	public void setContainerNo1(String containerNo1) {
		this.containerNo1 = containerNo1;
	}

	public String getContainerNo2() {
		return containerNo2;
	}

	public void setContainerNo2(String containerNo2) {
		this.containerNo2 = containerNo2;
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

	public Boolean getDetectionFlg() {
		return detectionFlg;
	}

	public void setDetectionFlg(Boolean detectionFlg) {
		this.detectionFlg = detectionFlg;
	}
}
