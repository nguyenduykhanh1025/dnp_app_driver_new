package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class DriverAccountForm  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String mobileNumber;

	private String fullName;

	private Boolean assignedFlg;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

	public Boolean getAssignedFlg() {
		return assignedFlg;
	}

	public void setAssignedFlg(Boolean assignedFlg) {
		this.assignedFlg = assignedFlg;
	}
	
}
