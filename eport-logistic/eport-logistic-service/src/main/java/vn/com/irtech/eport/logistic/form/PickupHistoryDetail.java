package vn.com.irtech.eport.logistic.form;

public class PickupHistoryDetail extends PickupHistoryForm {

    private String truckNo;

    private String chassisNo;

    private String yardPosition;

    private String gatePass;

    private Long batchId;

    private String sztp;

    private String consignee;

    private String address;

    private String mobileNumber;

    private String remark;

    private Integer status;

    public String getTruckNo() {
        return this.truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getChassisNo() {
        return this.chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getYardPosition() {
        return this.yardPosition;
    }

    public void setYardPosition(String yardPosition) {
        this.yardPosition = yardPosition;
    }

    public String getGatePass() {
        return this.gatePass;
    }

    public void setGatePass(String gatePass) {
        this.gatePass = gatePass;
    }

    public Long getBatchId() {
        return this.batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getSztp() {
        return this.sztp;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }

    public String getConsignee() {
        return this.consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}