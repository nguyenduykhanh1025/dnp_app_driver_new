package vn.com.irtech.eport.logistic.form;

public class Pickup {

    private Long pickupId;

    private Integer serviceType;

    private String containerNo;

    private String truckNo;

    private String chassisNo;

    private String sztp;

    private Long batchCode;

    public Long getPickupId() {
        return this.pickupId;
    }

    public void setPickupId(Long pickupId) {
        this.pickupId = pickupId;
    }

    public Integer getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

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

    public String getSztp() {
        return this.sztp;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }

    public Long getBatchCode() {
        return this.batchCode;
    }

    public void setBatchCode(Long batchCode) {
        this.batchCode = batchCode;
    }

}