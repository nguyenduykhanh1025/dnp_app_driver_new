package vn.com.irtech.eport.carrier.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Carrier Group Object carrier_group
 * 
 * @author irtech
 * @date 2020-04-06
 */
public class CarrierGroup extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Long id;

	/** Group Code */
	@Excel(name = "Group Code")
	private String groupCode;

	/** Group Name */
	@Excel(name = "Group Name")
	private String groupName;

	/** Operate Codes */
	@Excel(name = "Operate Codes")
	private String operateCode;

	/** Main Email */
	@Excel(name = "Main Email")
	private String mainEmail;

	/** 0: DO, 1:eDO */
	@Excel(name = "Type DO")
	private String doType;

	/** Do Permission */
	private String doFlag;

	/** Edo Permission */

	private String edoFlag;
	
	private String apiFlag;

//	private String pathEdiBackup;
//	private String pathEdiReceive;

	private String apiPublicKey;
	
	private String apiPrivateKey;


//	public void setPathEdiReceive(String pathEdiReceive) {
//		this.pathEdiReceive = pathEdiReceive;
//	}
//
//	public String getPathEdiReceive() {
//		return pathEdiReceive;
//	}
//
//	public void setPathEdiBackup(String pathEdiBackup) {
//		this.pathEdiBackup = pathEdiBackup;
//	}
//
//	public String getPathEdiBackup() {
//		return pathEdiBackup;
//	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}

	public String getMainEmail() {
		return mainEmail;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public String getDoType() {
		return doType;
	}

	public void setDoFlag(String doFlag) {
		this.doFlag = doFlag;
	}

	public String getDoFlag() {
		return doFlag;
	}

	public void setEdoFlag(String edoFlag) {
		this.edoFlag = edoFlag;
	}

	public String getEdoFlag() {
		return edoFlag;
	}

	public void setApiFlag(String apiFlag) {
		this.apiFlag = apiFlag;
	}

	public String getApiFlag() {
		return apiFlag;
	}

	public String getApiPrivateKey() {
		return apiPrivateKey;
	}

	public void setApiPrivateKey(String apiPrivateKey) {
		this.apiPrivateKey = apiPrivateKey;
	}

	public String getApiPublicKey() {
		return apiPublicKey;
	}

	public void setApiPublicKey(String apiPublicKey) {
		this.apiPublicKey = apiPublicKey;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId())
				.append("groupCode", getGroupCode())
				.append("groupName", getGroupName())
				.append("operateCode", getOperateCode())
				.append("mainEmail", getMainEmail())
				.append("doType", getDoType())
				.append("createBy", getCreateBy())
				.append("createTime", getCreateTime())
				.append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime())
				.append("doFlag", getDoFlag())
				.append("edoFlag", getEdoFlag())
				.append("apiFlag", getApiFlag())
				.toString();
	}
}
