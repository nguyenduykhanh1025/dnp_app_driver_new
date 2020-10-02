/**
 * 
 */
package vn.com.irtech.api.dto;

import java.io.Serializable;

/**
 * @author Trong Hieu
 *
 */
public class ContainerHoldInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String[] containers;
	
	private String userVoy;
	
	private String holdType;
	
	private String holdCode;
	
	private String holdChk;

	public String[] getContainers() {
		return containers;
	}

	public void setContainers(String[] containers) {
		this.containers = containers;
	}

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

	public String getHoldType() {
		return holdType;
	}

	public void setHoldType(String holdType) {
		this.holdType = holdType;
	}

	public String getHoldCode() {
		return holdCode;
	}

	public void setHoldCode(String holdCode) {
		this.holdCode = holdCode;
	}

	public String getHoldChk() {
		return holdChk;
	}

	public void setHoldChk(String holdChk) {
		this.holdChk = holdChk;
	}
}
