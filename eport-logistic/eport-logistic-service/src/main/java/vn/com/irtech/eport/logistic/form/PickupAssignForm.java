package vn.com.irtech.eport.logistic.form;

public class PickupAssignForm {

    private Long pickupAssignId;

    private String containerNo;

    private String sztp;

    private Long wgt;

    private Long shipmentDetailId;

    private Boolean clickable = true;

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
    
    public Long getShipmentDetailId() {
        return this.shipmentDetailId;
    }

    public void setShipmentDetailId(Long shipmentDetailId) {
        this.shipmentDetailId = shipmentDetailId;
    }

    public Boolean getClickable() {
        return this.clickable;
    }

    public void setClickable(Boolean clickable) {
        this.clickable = clickable;
    }
}