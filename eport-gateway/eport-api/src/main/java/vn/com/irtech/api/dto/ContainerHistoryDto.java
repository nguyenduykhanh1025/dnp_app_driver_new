package vn.com.irtech.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GiapHD
 *
 */
public class ContainerHistoryDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date updateTime;

	private String cntrNo;

	private Integer historySeq;
	/**
	 * VSL_CD / CALL_YEAR / CALL_SEQ
	 */
	private String vesselInfo;

	private String dataField;

	private String oldValue;

	private String newValue;

	private String staffCd;
	
	private String vslCd;
	
	private String callYear;
	
	private String callSeq;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public Integer getHistorySeq() {
		return historySeq;
	}

	public void setHistorySeq(Integer historySeq) {
		this.historySeq = historySeq;
	}

	public String getVesselInfo() {
		return vesselInfo;
	}

	public void setVesselInfo(String vesselInfo) {
		this.vesselInfo = vesselInfo;
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

	public String getStaffCd() {
		return staffCd;
	}

	public void setStaffCd(String staffCd) {
		this.staffCd = staffCd;
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
}
