package vn.com.irtech.eport.logistic.form;

public class PickupAssignForm {

    private Long pickupAssignId;

    private Long batchCode;

    private String blNo;

    private String bookingNo;

    private String fe;

    private String sztp;

    public Long getPickupAssignId() {
        return this.pickupAssignId;
    }

    public void setPickupAssignId(Long pickupAssignId) {
        this.pickupAssignId = pickupAssignId;
    }

    public Long getBatchCode() {
        return this.batchCode;
    }

    public void setBatchCode(Long batchCode) {
        this.batchCode = batchCode;
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

    public String getSztp() {
        return this.sztp;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }
    
}