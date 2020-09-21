package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.system.dto.CustomDeclareResult;

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
	
	private String taxCode;
	
	private String companyName;
	
	private CustomDeclareResult customDeclareResult;

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

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public CustomDeclareResult getCustomDeclareResult() {
		return customDeclareResult;
	}

	public void setCustomDeclareResult(CustomDeclareResult customDeclareResult) {
		this.customDeclareResult = customDeclareResult;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
	        .append("containerNo", getCntrNo())
	        .append("userVoy", getUserVoy())
	        .append("appNo", getCustomsAppNo())
	        .append("status", getCustomsStatus())
	        .append("remark", getCustomsRemark())
	        .append("taxCode", getTaxCode())
	        .append("company", getCompanyName())
	        .append("customDeclareResult", getCustomDeclareResult())
	        .toString();
	}
	
	
}
