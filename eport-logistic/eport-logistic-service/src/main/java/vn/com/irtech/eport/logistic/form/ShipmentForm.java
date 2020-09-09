package vn.com.irtech.eport.logistic.form;

public class ShipmentForm {

    private Long batchId;

    private String blNo;

    private String bookingNo;

    private String fe;

    private Integer contAmount;
    
    private String consignee;

    public Long getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBlNo() {
        return this.blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBookingNo() {
        return this.bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getFe() {
        return this.fe;
    }

    public void setFe(String fe) {
        this.fe = fe;
    }

    public Integer getContAmount() {
        return this.contAmount;
    }

    public void setContAmount(Integer contAmount) {
        this.contAmount = contAmount;
    }

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
    
}