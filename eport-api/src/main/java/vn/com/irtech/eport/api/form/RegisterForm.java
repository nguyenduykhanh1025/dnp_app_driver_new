package vn.com.irtech.eport.api.form;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class RegisterForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = "^[0-9]{10,11}$")
	private String mobileNumber;

	private String fullName;

	@Min(6)
	private String password;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
