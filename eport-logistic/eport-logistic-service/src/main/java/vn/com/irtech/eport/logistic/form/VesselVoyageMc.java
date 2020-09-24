/**
 * 
 */
package vn.com.irtech.eport.logistic.form;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class VesselVoyageMc implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String vslNm;
	
	private String voyNo;
	
	private String sztp;
	
	private String dischargePort;
	
	private Integer containerAmount;

	public String getVslNm() {
		return vslNm;
	}

	public void setVslNm(String vslNm) {
		this.vslNm = vslNm;
	}

	public String getVoyNo() {
		return voyNo;
	}

	public void setVoyNo(String voyNo) {
		this.voyNo = voyNo;
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

	public Integer getContainerAmount() {
		return containerAmount;
	}

	public void setContainerAmount(Integer containerAmount) {
		this.containerAmount = containerAmount;
	}
	
}
