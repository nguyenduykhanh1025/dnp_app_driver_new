package vn.com.irtech.eport.logistic.form;

public class PickupAssignForm {

    private Long pickupAssignId;

    private String containerNo;

    private String sztp;

    private Long wgt;

    public Long getPickupAssignId() {
        return this.pickupAssignId;
    }

    public void setPickupAssignId(Long pickupAssignId) {
        this.pickupAssignId = pickupAssignId;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getSztp() {
        return this.sztp;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }

    public Long getWgt() {
        return this.wgt;
    }

    public void setWgt(Long wgt) {
        this.wgt = wgt;
    }
    
}