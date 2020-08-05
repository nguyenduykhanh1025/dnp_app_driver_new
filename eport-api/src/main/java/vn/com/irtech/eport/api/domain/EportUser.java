package vn.com.irtech.eport.api.domain;

import vn.com.irtech.eport.api.consts.BusinessConsts;

public class EportUser {

	private Long userId;

	private String loginName;

	private String password;

	private String salt;

	private Boolean isLocked;

	private Boolean isDeleted;

	private EportUserType userType;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public EportUserType getUserType() {
		return userType;
	}

	public void setUserType(EportUserType userType) {
		this.userType = userType;
	}

	public String getSubject() {
		return this.loginName + BusinessConsts.BLANK + this.userType.value();
	}

}
