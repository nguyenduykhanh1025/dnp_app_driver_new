package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Shipment Details Object shipment_detail
 * 
 * @author admin
 * @date 2020-05-07
 */
public class ShipmentDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Ma Lo */
    @Excel(name = "Ma Lo")
    private Long shipmentId;

    /** group id */
    private Long logisticGroupId;

    /** Ma Lenh */
    @Excel(name = "Ma Lenh")
    private Long processOrderId;

    /** Ma DK */
    @Excel(name = "Ma DK")
    private String registerNo;

    /** Container Number */
    @Excel(name = "Container Number")
    private String containerNo;

    /** Container Status (S,D) */
    @Excel(name = "Container Status (S,D)")
    private String containerStatus;

    /** Size Type */
    @Excel(name = "Size Type")
    private String sztp;

    /** FE */
    @Excel(name = "FE")
    private String fe;

    /** Booking Number */
    @Excel(name = "Booking Number")
    private String bookingNo;

    /** BL number */
    @Excel(name = "BL number")
    private String blNo;

    /** Seal Number */
    @Excel(name = "Seal Number")
    private String sealNo;

    /** Shipper/consignee */
    @Excel(name = "Shipper/consignee")
    private String consignee;

    /** Han Lenh */
    @Excel(name = "Han Lenh", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiredDem;

    /** Weight */
    @Excel(name = "Weight")
    private Long wgt;

    /** Vessel name */
    @Excel(name = "Vessel name")
    private String vslNm;

    /** Voyage */
    @Excel(name = "Voyage")
    private String voyNo;

    /** Operator Code */
    @Excel(name = "Operator Code")
    private String opeCode;

    /** Cang Chuyen Tai */
    @Excel(name = "Cang Chuyen Tai")
    private String loadingPort;

    /** Cang Dich */
    @Excel(name = "Cang Dich")
    private String dischargePort;

    /** Phuong Tien */
    @Excel(name = "Phuong Tien")
    private String transportType;

    /** Empty depot */
    @Excel(name = "Nơi Ha Vo")
    private String emptyDepot;

    /** Cargo Type */
    @Excel(name = "Loại Hang")
    private String cargoType;

    /** VGM Check */
    @Excel(name = "VGM Check")
    private Boolean vgmChk;

    /** VGM */
    @Excel(name = "VGM")
    private String vgm;

    /** VGM Person Info */
    @Excel(name = "VGM Person Info")
    private String vgmPersonInfo;

    /** Boc Chi Dinh (Y,N) */
    @Excel(name = "Boc Chi Dinh (Y,N)")
    private String preorderPickup;

    /** So Cont Dich Chuyen */
    @Excel(name = "So Cont Dich Chuyen")
    private Integer shiftingContNumber;

    /** Custom Status (H,R) */
    @Excel(name = "Custom Status (H,R)")
    private String customStatus;

    /** Payment Status (Y,N,W,E) */
    @Excel(name = "Payment Status (Y,N,W,E)")
    private String paymentStatus;

    /** Process Status(Y,N,E) */
    @Excel(name = "Process Status(Y,N,E)")
    private String processStatus;

    /** DO Status(Y,N) */
    @Excel(name = "DO Status(Y,N")
    private String doStatus;

    /** Ngay nhan DO goc */
    private Date doReceivedTime;

    /** Xac Thuc (Y,N) */
    @Excel(name = "Xac Thuc (Y,N)")
    private String userVerifyStatus;

    /** Status */
    @Excel(name = "Status")
    private Integer status;

    private String bay;

    private int roww;

    private int tier;

    private Integer driverAmount;

    private String payType;

    private Integer assignNumber;

    private Integer serviceType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setLogisticGroupId(Long logisticGroupId) {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() {
        return logisticGroupId;
    }

    public void setProcessOrderId(Long processOrderId) {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() {
        return processOrderId;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getRegisterNo() {
        return registerNo;
    }
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerStatus(String containerStatus) {
        this.containerStatus = containerStatus;
    }

    public String getContainerStatus() {
        return containerStatus;
    }

    public void setSztp(String sztp) {
        this.sztp = sztp;
    }

    public String getSztp() {
        return sztp;
    }

    public void setFe(String fe) {
        this.fe = fe;
    }

    public String getFe() {
        return fe;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setExpiredDem(Date expiredDem) {
        this.expiredDem = expiredDem;
    }

    public Date getExpiredDem() {
        return expiredDem;
    }

    public void setWgt(Long wgt) {
        this.wgt = wgt;
    }

    public Long getWgt() {
        return wgt;
    }
    public void setVslNm(String vslNm) {
        this.vslNm = vslNm;
    }

    public String getVslNm() {
        return vslNm;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setOpeCode(String opeCode) {
        this.opeCode = opeCode;
    }

    public String getOpeCode() {
        return opeCode;
    }
    public void setLoadingPort(String loadingPort) {
        this.loadingPort = loadingPort;
    }

    public String getLoadingPort() {
        return loadingPort;
    }

    public void setDischargePort(String dischargePort) {
        this.dischargePort = dischargePort;
    }

    public String getDischargePort() {
        return dischargePort;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setEmptyDepot(String emptyDepot) {
        this.emptyDepot = emptyDepot;
    }

    public String getEmptyDepot() {
        return emptyDepot;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setVgmChk(Boolean vgmChk) {
        this.vgmChk = vgmChk;
    }

    public Boolean getVgmChk() {
        return vgmChk;
    }

    public void setVgm(String vgm) {
        this.vgm = vgm;
    }

    public String getVgm() {
        return vgm;
    }

    public void setVgmPersonInfo(String vgmPersonInfo) {
        this.vgmPersonInfo = vgmPersonInfo;
    }

    public String getVgmPersonInfo() {
        return vgmPersonInfo;
    }

    public void setPreorderPickup(String preorderPickup) {
        this.preorderPickup = preorderPickup;
    }

    public String getPreorderPickup() {
        return preorderPickup;
    }

    public void setShiftingContNumber(Integer shiftingContNumber) {
        this.shiftingContNumber = shiftingContNumber;
    }

    public Integer getShiftingContNumber() {
        return shiftingContNumber;
    }

    public void setCustomStatus(String customStatus) {
        this.customStatus = customStatus;
    }

    public String getCustomStatus() {
        return customStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setDoStatus(String doStatus) {
        this.doStatus = doStatus;
    }
    public String getDoStatus() 
    {
        return doStatus;
    }

    public void setDoReceivedTime(Date doReceivedTime) {
        this. doReceivedTime = doReceivedTime;
    }

    public Date getDoReceivedTime() {
        return doReceivedTime;
    }

    public void setUserVerifyStatus(String userVerifyStatus) {
        this.userVerifyStatus = userVerifyStatus;
    }

    public String getUserVerifyStatus() {
        return userVerifyStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setBay(String bay) {
        this.bay = bay;
    }

    public String getBay() {
        return bay;
    }

    public void setRow(int roww) {
        this.roww = roww;
    }

    public int getRow() {
        return roww;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
    public Integer getDriverAmount() {
        return driverAmount;
    }

    public void setDriverAmount(Integer driverAmount) {
        this.driverAmount = driverAmount;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    } 

    public String getPayType() {
        return payType;
    }

    public void setAssignNumber(Integer assignNumber) {
        this.assignNumber = assignNumber;
    }

    public Integer getAssignNumber() {
        return assignNumber;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentId", getShipmentId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("processOrderId", getProcessOrderId())
            .append("registerNo", getRegisterNo())
            .append("containerNo", getContainerNo())
            .append("containerStatus", getContainerStatus())
            .append("sztp", getSztp())
            .append("fe", getFe())
            .append("bookingNo", getBookingNo())
            .append("blNo", getBlNo())
            .append("sealNo", getSealNo())
            .append("consignee", getConsignee())
            .append("expiredDem", getExpiredDem())
            .append("wgt", getWgt())
            .append("vslNm", getVslNm())
            .append("voyNo", getVoyNo())
            .append("opeCode", getOpeCode())
            .append("loadingPort", getLoadingPort())
            .append("dischargePort", getDischargePort())
            .append("transportType", getTransportType())
            .append("emptyDepot", getEmptyDepot())
            .append("cargoType", getCargoType())
            .append("vgmChk", getVgmChk())
            .append("vgm", getVgm())
            .append("vgmPersonInfo", getVgmPersonInfo())
            .append("preorderPickup", getPreorderPickup())
            .append("shiftingContNumber", getShiftingContNumber())
            .append("customStatus", getCustomStatus())
            .append("paymentStatus", getPaymentStatus())
            .append("processStatus", getProcessStatus())
            .append("doStatus", getDoStatus())
            .append("doReceivedTime", getDoReceivedTime())
            .append("userVerifyStatus", getUserVerifyStatus())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("bay", getBay())
            .append("roww", getRow())
            .append("tier", getTier())
            .append("driverAmount", getDriverAmount())
            .append("payType", getPayType())
            .append("assignNumber", getAssignNumber())
            .append("serviceType", getServiceType())
            .toString();
    }
}
