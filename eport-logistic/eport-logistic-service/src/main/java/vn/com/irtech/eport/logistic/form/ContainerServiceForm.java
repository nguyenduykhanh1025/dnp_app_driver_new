package vn.com.irtech.eport.logistic.form;

import java.io.Serializable;
import java.util.Date;

/**
 * Form ha container (F/E)
 * @author GiapHD
 *
 */
public class ContainerServiceForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ids;
	
	private String customsNo;

    /** ID */
    private Long id;

    /** Ma Lo */
    private Long shipmentId;

    /** Container Number */
    private String containerNo;

    /** Size Type */
    private String sztp;
    
    /** Size Type define */
    private String sztpDefine;
    
    /** Temperature */
    private String temperature;
    
    /** Booking Number */
    private String bookingNo;

    /** BL number */
    private String blNo;

    /** Seal Number */
    private String sealNo;

    /** Shipper/consignee */
    private String consignee;

    /** Han Lenh */
    private Date expiredDem;

    /** Weight */
    private Integer detFreeTime;
    
    /** Weight */
    private Long wgt;
    
    /** Voyage */
    private String voyNo;
    
    /** Operator Code */
    private String opeCode;
    
    /** Cang Chuyen Tai */
    private String loadingPort;

    /** Cang Dich */
    private String dischargePort;

    /** Empty depot */
    private String emptyDepot;

    /** Han Lenh */
    private Date emptyExpiredDem;

    /** Empty depot */
    private String emptyDepotLocation;
    
    /** Cargo Type */
    private String cargoType;

    /** VGM */
    private String vgm;

    /** Custom Status (H,R) */
    private String customStatus;

    /** Payment Status (Y,N,W,E) */
    private String paymentStatus;

    /** Process Status(Y,N,E) */
    private String processStatus;

    /** DO Status(Y,N) */
    private String doStatus;

    /** Xac Thuc (Y,N) */
    private String userVerifyStatus;

    /** Finish Status(Y,N) */
    private String finishStatus;

    /** Status */
    private Integer status;

    /** Container supply status */
    private String contSupplyStatus;
    
    /** Planning date */
    private Date planningDate;
    
    /** Quality requirement */
    private String qualityRequirement;
    
    private String contSupplierName;
    
    /** Container supply remark */
    private String contSupplyRemark;

    private String payType;
    
    private String payer;
    
    private String payerName;

    private Integer serviceType;
    
    private String vslAndVoy;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCustomsNo() {
		return customsNo;
	}

	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getSztpDefine() {
		return sztpDefine;
	}

	public void setSztpDefine(String sztpDefine) {
		this.sztpDefine = sztpDefine;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getBookingNo() {
		return bookingNo;
	}

	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Date getExpiredDem() {
		return expiredDem;
	}

	public void setExpiredDem(Date expiredDem) {
		this.expiredDem = expiredDem;
	}

	public Integer getDetFreeTime() {
		return detFreeTime;
	}

	public void setDetFreeTime(Integer detFreeTime) {
		this.detFreeTime = detFreeTime;
	}

	public Long getWgt() {
		return wgt;
	}

	public void setWgt(Long wgt) {
		this.wgt = wgt;
	}

	public String getVoyNo() {
		return voyNo;
	}

	public void setVoyNo(String voyNo) {
		this.voyNo = voyNo;
	}

	public String getOpeCode() {
		return opeCode;
	}

	public void setOpeCode(String opeCode) {
		this.opeCode = opeCode;
	}

	public String getLoadingPort() {
		return loadingPort;
	}

	public void setLoadingPort(String loadingPort) {
		this.loadingPort = loadingPort;
	}

	public String getDischargePort() {
		return dischargePort;
	}

	public void setDischargePort(String dischargePort) {
		this.dischargePort = dischargePort;
	}

	public String getEmptyDepot() {
		return emptyDepot;
	}

	public void setEmptyDepot(String emptyDepot) {
		this.emptyDepot = emptyDepot;
	}

	public Date getEmptyExpiredDem() {
		return emptyExpiredDem;
	}

	public void setEmptyExpiredDem(Date emptyExpiredDem) {
		this.emptyExpiredDem = emptyExpiredDem;
	}

	public String getEmptyDepotLocation() {
		return emptyDepotLocation;
	}

	public void setEmptyDepotLocation(String emptyDepotLocation) {
		this.emptyDepotLocation = emptyDepotLocation;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getVgm() {
		return vgm;
	}

	public void setVgm(String vgm) {
		this.vgm = vgm;
	}

	public String getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(String customStatus) {
		this.customStatus = customStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getDoStatus() {
		return doStatus;
	}

	public void setDoStatus(String doStatus) {
		this.doStatus = doStatus;
	}

	public String getUserVerifyStatus() {
		return userVerifyStatus;
	}

	public void setUserVerifyStatus(String userVerifyStatus) {
		this.userVerifyStatus = userVerifyStatus;
	}

	public String getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContSupplyStatus() {
		return contSupplyStatus;
	}

	public void setContSupplyStatus(String contSupplyStatus) {
		this.contSupplyStatus = contSupplyStatus;
	}

	public Date getPlanningDate() {
		return planningDate;
	}

	public void setPlanningDate(Date planningDate) {
		this.planningDate = planningDate;
	}

	public String getQualityRequirement() {
		return qualityRequirement;
	}

	public void setQualityRequirement(String qualityRequirement) {
		this.qualityRequirement = qualityRequirement;
	}

	public String getContSupplierName() {
		return contSupplierName;
	}

	public void setContSupplierName(String contSupplierName) {
		this.contSupplierName = contSupplierName;
	}

	public String getContSupplyRemark() {
		return contSupplyRemark;
	}

	public void setContSupplyRemark(String contSupplyRemark) {
		this.contSupplyRemark = contSupplyRemark;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getVslAndVoy() {
		return vslAndVoy;
	}

	public void setVslAndVoy(String vslAndVoy) {
		this.vslAndVoy = vslAndVoy;
	}
    

}
