package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class EdiReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String partnerCode;

	@NotBlank
	private String hashCode;

	@NotEmpty
	private List<@Valid EdiDataReq> data;

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
