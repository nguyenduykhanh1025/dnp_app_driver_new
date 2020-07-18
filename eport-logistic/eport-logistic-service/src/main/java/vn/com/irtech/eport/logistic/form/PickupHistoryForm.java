package vn.com.irtech.eport.logistic.form;

import java.util.Date;

public class PickupHistoryForm {
    private static final long serialVersionUID = 1L;

    private Long pickupHistoryId;

    private String containerNo;

    private Date gateInDate;

    public Long getPickupHistoryId() {
        return this.pickupHistoryId;
    }

    public void setPickupHistoryId(Long pickupHistoryId) {
        this.pickupHistoryId = pickupHistoryId;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Date getGateInDate() {
        return this.gateInDate;
    }

    public void setGateInDate(Date gateInDate) {
        this.gateInDate = gateInDate;
    }

}
