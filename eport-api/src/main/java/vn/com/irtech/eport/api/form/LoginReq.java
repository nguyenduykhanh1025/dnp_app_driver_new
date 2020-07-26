package vn.com.irtech.eport.api.form;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class LoginReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userName;

	private String passWord;

	@NotBlank
	private String deviceToken;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

}
