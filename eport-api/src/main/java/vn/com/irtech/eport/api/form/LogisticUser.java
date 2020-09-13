package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class LogisticUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	
	private String email;

	private String mobile;
	
	private String fullName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
