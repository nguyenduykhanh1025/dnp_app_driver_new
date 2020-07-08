package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.List;

public class EdiReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String siteId;

	private String partnerCode;

	private String hashCode;

	private List<EdiDataReq> data;

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	public List<EdiDataReq> getData() {
		return data;
	}

	public void setData(List<EdiDataReq> data) {
		this.data = data;
	}

}
