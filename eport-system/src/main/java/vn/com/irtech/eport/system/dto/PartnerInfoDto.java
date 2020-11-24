package vn.com.irtech.eport.system.dto;

import java.io.Serializable;

/**
 * @author GiapHD
 *
 */
public class PartnerInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taxCode;
	private String groupName;
	private String address;
	private String ptnrCode;
	private String ptnrType;

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPtnrCode() {
		return ptnrCode;
	}

	public void setPtnrCode(String ptnrCode) {
		this.ptnrCode = ptnrCode;
	}

	public String getPtnrType() {
		return ptnrType;
	}

	public void setPtnrType(String ptnrType) {
		this.ptnrType = ptnrType;
	}

}
