package vn.com.irtech.eport.logistic.dto;

import java.util.Date;

public class ContainerHistoryDto {
    
	private String cntrId;
	
	private Long historySeq;
	
	private String cntrUid;
	
	private String vslCd;
	
	private String callYear;
	
	private String callSeq;
	
	private String cntrNo;
	
	private String staffCd;
	
	private Date updateTime;
	
	private String tableName;
	
	private String cntrSeq;
	
	private String dataField;
	
	private String oldValue;
	
	private String newValue;
	
	private String cntrState;
	
	private String modeChangeType;
	
    public String getCntrId() {
		return cntrId;
	}

	public void setCntrId(String cntrId) {
		this.cntrId = cntrId;
	}

	public Long getHistorySeq() {
		return historySeq;
	}

	public void setHistorySeq(Long historySeq) {
		this.historySeq = historySeq;
	}

	public String getCntrUid() {
		return cntrUid;
	}

	public void setCntrUid(String cntrUid) {
		this.cntrUid = cntrUid;
	}

	public String getVslCd() {
		return vslCd;
	}

	public void setVslCd(String vslCd) {
		this.vslCd = vslCd;
	}

	public String getCallYear() {
		return callYear;
	}

	public void setCallYear(String callYear) {
		this.callYear = callYear;
	}

	public String getCallSeq() {
		return callSeq;
	}

	public void setCallSeq(String callSeq) {
		this.callSeq = callSeq;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getStaffCd() {
		return staffCd;
	}

	public void setStaffCd(String staffCd) {
		this.staffCd = staffCd;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCntrSeq() {
		return cntrSeq;
	}

	public void setCntrSeq(String cntrSeq) {
		this.cntrSeq = cntrSeq;
	}

	public String getDataField() {
		return dataField;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getCntrState() {
		return cntrState;
	}

	public void setCntrState(String cntrState) {
		this.cntrState = cntrState;
	}

	public String getModeChangeType() {
		return modeChangeType;
	}

	public void setModeChangeType(String modeChangeType) {
		this.modeChangeType = modeChangeType;
	}
}
