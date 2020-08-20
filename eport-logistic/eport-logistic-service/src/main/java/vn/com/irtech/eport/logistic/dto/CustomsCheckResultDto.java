package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;

/**
 * @author GiapHD
 *
 */
public class CustomsCheckResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userVoy;

	private String cntrNo;

	private String customsStatus;

	private String customsAppNo;

	private String customsRemark;

	private String msgRecvContent;

	/**
	 * Check if customs is released
	 * 
	 * @return true: customsStatus ="TQ", false: otherwise
	 */
	public boolean isReleased() {
		return customsStatus != null && "TQ".equalsIgnoreCase(customsStatus);
	}

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getCustomsStatus() {
		return customsStatus;
	}

	public void setCustomsStatus(String customsStatus) {
		this.customsStatus = customsStatus;
	}

	public String getCustomsAppNo() {
		return customsAppNo;
	}

	public void setCustomsAppNo(String customsAppNo) {
		this.customsAppNo = customsAppNo;
	}

	public String getCustomsRemark() {
		return customsRemark;
	}

	public void setCustomsRemark(String customsRemark) {
		this.customsRemark = customsRemark;
	}

	public String getMsgRecvContent() {
		return msgRecvContent;
	}

	public void setMsgRecvContent(String msgRecvContent) {
		this.msgRecvContent = msgRecvContent;
	}

}
