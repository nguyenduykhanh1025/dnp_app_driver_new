package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class SeparateHouseBillReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String houseBill;

	@NotBlank
	private String consignee2;

	@NotBlank
	private String consignee2TaxCode;

	@NotEmpty
	private List<Long> edoIds;
	
	@NotBlank
	private String orderNumber;

	public String getHouseBill() {
		return houseBill;
	}

	public void setHouseBill(String houseBill) {
		this.houseBill = houseBill;
	}

	public String getConsignee2() {
		return consignee2;
	}

	public void setConsignee2(String consignee2) {
		this.consignee2 = consignee2;
	}

	public String getConsignee2TaxCode() {
		return consignee2TaxCode;
	}

	public void setConsignee2TaxCode(String consignee2TaxCode) {
		this.consignee2TaxCode = consignee2TaxCode;
	}

	public List<Long> getEdoIds() {
		return edoIds;
	}

	public void setEdoIds(List<Long> edoIds) {
		this.edoIds = edoIds;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
}
