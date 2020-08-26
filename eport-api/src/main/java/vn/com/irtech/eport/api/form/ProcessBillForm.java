package vn.com.irtech.eport.api.form;

public class ProcessBillForm {

	private Long id;

    private String invoiceNo;

    private String sztp;

    private Long exchangeFee;

    private Integer vatRate;

    private Long vatAfterFee;

    private String containerNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public Long getExchangeFee() {
		return exchangeFee;
	}

	public void setExchangeFee(Long exchangeFee) {
		this.exchangeFee = exchangeFee;
	}

	public Integer getVatRate() {
		return vatRate;
	}

	public void setVatRate(Integer vatRate) {
		this.vatRate = vatRate;
	}

	public Long getVatAfterFee() {
		return vatAfterFee;
	}

	public void setVatAfterFee(Long vatAfterFee) {
		this.vatAfterFee = vatAfterFee;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}
    
}
