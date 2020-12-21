package vn.com.irtech.api.entity;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Shipment Details Object shipment_detail
 * 
 * @author admin
 * @date 2020-06-05
 */
public class ShipmentDetailEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Ma Lo */
    private Long shipmentId;

    /** null */
    private Long logisticGroupId;

    /** Ma Lenh */
    private Long processOrderId;
    
    /** Ma DK */
    private String registerNo;

    /** Container Number */
    private String containerNo;

    /** Container Status (S,D) */
    private String containerStatus;

    /** Size Type */
    private String sztp;

    /** FE */
    private String fe;

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
    private Long wgt;
    
    /** Vessel name */
    private String vslName;

    /** Vessel code */
    private String vslNm;

    /** Voyage */
    private String voyNo;
    
    /** Voyage */
    private String voyCarrier;

    /** Operator Code */
    private String opeCode;

    /** Cang Chuyen Tai */
    private String loadingPort;

    /** Cang Dich */
    private String dischargePort;

    /** Phuong Tien */
    private String transportType;

    /** Noi Ha Vo */
    private String emptyDepot;

    /** VGM Check */
    private String vgmChk;

    /** VGM */
    private String vgm;

    /** VGM Person Info */
    private String vgmPersonInfo;

    /** Custom Status (N,C,H,R) */
    private String customStatus;

    /** Payment Status (Y,N,W,E) */
    private String paymentStatus;

    /** Process Status(Y,N,E) */
    private String processStatus;

    /** T.T DO Goc */
    private String doStatus;

    /** Ngay Nhan DO Goc */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date doReceivedTime;

    /** Xac Thuc (Y,N) */
    private String userVerifyStatus;

    /** Boc Chi Dinh (Y,N) */
    private String preorderPickup;

    /** So Cont Dich Chuyen */
    private Integer shiftingContNumber;

    /** Status */
    private Integer status;
    
    /** Block*/
    private String block;

    /** Bay*/
    private String bay;
    
    /** Roww*/
    private Integer roww;
    
    /** Tier*/
    private Integer tier;
    
    /** Cargo Type */
    private String cargoType;
    
    private String year;
    
    private String vslAndVoy;
    
    private String orderNo;
    
    /** Ngay tau den */
    private Date eta;
    
    /** Ngay tau di */
    private Date etd;
    
    /** Yard Position */
    private String location;
    
    /** Container Remark */
    private String containerRemark;
    
	/** Dangerous IMO */
	private String dangerousImo;

	/** Dangerous UNNO */
	private String dangerousUnno;

	/** Oversize catos OV_HEIGHT */
	private String ovHeight;

	/** Oversize catos OV_FORE */
	private String ovFore;

	/** Oversize catos OV_AFT */
	private String ovAft;

	/** Oversize catos OV_PORT */
	private String ovPort;

	/** Oversize catos OV_STBD */
	private String ovStbd;

	/** Oversize catos OS_HEIGHT */
	private String osHeight;

	/** Oversize catos OS_PORT */
	private String osPort;

	/** Oversize catos OS_STBD */
	private String osStbd;

	/** Oversize catos SET_TEMP */
	private String setTemp;
	
    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public String getCargoType() {
        return cargoType;
    }
    
    public void setProcessOrderId(Long processOrderId) {
        this.processOrderId = processOrderId;
    }

    public Long getProcessOrderId() {
        return processOrderId;
    }
    
    public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getBay() {
		return bay;
	}

	public void setBay(String bay) {
		this.bay = bay;
	}

	public Integer getRow() {
		return roww;
	}

	public void setRow(Integer roww) {
		this.roww = roww;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShipmentId(Long shipmentId) 
    {
        this.shipmentId = shipmentId;
    }

    public Long getShipmentId() 
    {
        return shipmentId;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setRegisterNo(String registerNo) 
    {
        this.registerNo = registerNo;
    }

    public String getRegisterNo() 
    {
        return registerNo;
    }
    public void setContainerNo(String containerNo) 
    {
        this.containerNo = containerNo;
    }

    public String getContainerNo() 
    {
        return containerNo;
    }
    public void setContainerStatus(String containerStatus) 
    {
        this.containerStatus = containerStatus;
    }

    public String getContainerStatus() 
    {
        return containerStatus;
    }
    public void setSztp(String sztp) 
    {
        this.sztp = sztp;
    }

    public String getSztp() 
    {
        return sztp;
    }
    public void setFe(String fe) 
    {
        this.fe = fe;
    }

    public String getFe() 
    {
        return fe;
    }
    public void setBookingNo(String bookingNo) 
    {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() 
    {
        return bookingNo;
    }
    public void setBlNo(String blNo) 
    {
        this.blNo = blNo;
    }

    public String getBlNo() 
    {
        return blNo;
    }
    public void setSealNo(String sealNo) 
    {
        this.sealNo = sealNo;
    }

    public String getSealNo() 
    {
        return sealNo;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setExpiredDem(Date expiredDem) 
    {
        this.expiredDem = expiredDem;
    }

    public Date getExpiredDem() 
    {
        return expiredDem;
    }
    public void setWgt(Long wgt) 
    {
        this.wgt = wgt;
    }

    public Long getWgt() 
    {
        return wgt;
    }
    
    public String getVslName() {
		return vslName;
	}

	public void setVslName(String vslName) {
		this.vslName = vslName;
	}

	public void setVslNm(String vslNm) 
    {
        this.vslNm = vslNm;
    }

    public String getVslNm() 
    {
        return vslNm;
    }
    public void setVoyNo(String voyNo) 
    {
        this.voyNo = voyNo;
    }

    public String getVoyNo() 
    {
        return voyNo;
    }
    
    public String getVoyCarrier() {
		return voyCarrier;
	}

	public void setVoyCarrier(String voyCarrier) {
		this.voyCarrier = voyCarrier;
	}

	public void setOpeCode(String opeCode) 
    {
        this.opeCode = opeCode;
    }

    public String getOpeCode() 
    {
        return opeCode;
    }
    public void setLoadingPort(String loadingPort) 
    {
        this.loadingPort = loadingPort;
    }

    public String getLoadingPort() 
    {
        return loadingPort;
    }
    public void setDischargePort(String dischargePort) 
    {
        this.dischargePort = dischargePort;
    }

    public String getDischargePort() 
    {
        return dischargePort;
    }
    public void setTransportType(String transportType) 
    {
        this.transportType = transportType;
    }

    public String getTransportType() 
    {
        return transportType;
    }
    public void setEmptyDepot(String emptyDepot) 
    {
        this.emptyDepot = emptyDepot;
    }

    public String getEmptyDepot() 
    {
        return emptyDepot;
    }
    
    public String getVgmChk() {
		return vgmChk;
	}

	public void setVgmChk(String vgmChk) {
		this.vgmChk = vgmChk;
	}

	public void setVgm(String vgm) 
    {
        this.vgm = vgm;
    }

    public String getVgm() 
    {
        return vgm;
    }
    public void setVgmPersonInfo(String vgmPersonInfo) 
    {
        this.vgmPersonInfo = vgmPersonInfo;
    }

    public String getVgmPersonInfo() 
    {
        return vgmPersonInfo;
    }
    public void setCustomStatus(String customStatus) 
    {
        this.customStatus = customStatus;
    }

    public String getCustomStatus() 
    {
        return customStatus;
    }
    public void setPaymentStatus(String paymentStatus) 
    {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() 
    {
        return paymentStatus;
    }
    public void setProcessStatus(String processStatus) 
    {
        this.processStatus = processStatus;
    }

    public String getProcessStatus() 
    {
        return processStatus;
    }
    public void setDoStatus(String doStatus) 
    {
        this.doStatus = doStatus;
    }

    public String getDoStatus() 
    {
        return doStatus;
    }
    public void setDoReceivedTime(Date doReceivedTime) 
    {
        this.doReceivedTime = doReceivedTime;
    }

    public Date getDoReceivedTime() 
    {
        return doReceivedTime;
    }
    public void setUserVerifyStatus(String userVerifyStatus) 
    {
        this.userVerifyStatus = userVerifyStatus;
    }

    public String getUserVerifyStatus() 
    {
        return userVerifyStatus;
    }
    public void setPreorderPickup(String preorderPickup) 
    {
        this.preorderPickup = preorderPickup;
    }

    public String getPreorderPickup() 
    {
        return preorderPickup;
    }
    public void setShiftingContNumber(Integer shiftingContNumber) {
        this.shiftingContNumber = shiftingContNumber;
    }

    public Integer getShiftingContNumber() {
        return shiftingContNumber;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getVslAndVoy() {
		return vslAndVoy;
	}

	public void setVslAndVoy(String vslAndVoy) {
		this.vslAndVoy = vslAndVoy;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getContainerRemark() {
		return containerRemark;
	}

	public void setContainerRemark(String containerRemark) {
		this.containerRemark = containerRemark;
	}

	public Integer getRoww() {
		return roww;
	}

	public void setRoww(Integer roww) {
		this.roww = roww;
	}

	public String getDangerousImo() {
		return dangerousImo;
	}

	public void setDangerousImo(String dangerousImo) {
		this.dangerousImo = dangerousImo;
	}

	public String getDangerousUnno() {
		return dangerousUnno;
	}

	public void setDangerousUnno(String dangerousUnno) {
		this.dangerousUnno = dangerousUnno;
	}

	public String getOvHeight() {
		return ovHeight;
	}

	public void setOvHeight(String ovHeight) {
		this.ovHeight = ovHeight;
	}

	public String getOvFore() {
		return ovFore;
	}

	public void setOvFore(String ovFore) {
		this.ovFore = ovFore;
	}

	public String getOvAft() {
		return ovAft;
	}

	public void setOvAft(String ovAft) {
		this.ovAft = ovAft;
	}

	public String getOvPort() {
		return ovPort;
	}

	public void setOvPort(String ovPort) {
		this.ovPort = ovPort;
	}

	public String getOvStbd() {
		return ovStbd;
	}

	public void setOvStbd(String ovStbd) {
		this.ovStbd = ovStbd;
	}

	public String getOsHeight() {
		return osHeight;
	}

	public void setOsHeight(String osHeight) {
		this.osHeight = osHeight;
	}

	public String getOsPort() {
		return osPort;
	}

	public void setOsPort(String osPort) {
		this.osPort = osPort;
	}

	public String getOsStbd() {
		return osStbd;
	}

	public void setOsStbd(String osStbd) {
		this.osStbd = osStbd;
	}
	
	public String getSetTemp() {
		return setTemp;
	}

	public void setSetTemp(String setTemp) {
		this.setTemp = setTemp;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("shipmentId", getShipmentId())
				.append("logisticGroupId", getLogisticGroupId()).append("processOrderId", getProcessOrderId())
				.append("registerNo", getRegisterNo()).append("containerNo", getContainerNo())
				.append("containerStatus", getContainerStatus()).append("sztp", getSztp()).append("fe", getFe())
				.append("bookingNo", getBookingNo()).append("blNo", getBlNo()).append("sealNo", getSealNo())
				.append("consignee", getConsignee()).append("expiredDem", getExpiredDem()).append("wgt", getWgt())
				.append("vslName", getVslName()).append("vslNm", getVslNm()).append("voyNo", getVoyNo())
				.append("voyCarrier", getVoyCarrier()).append("opeCode", getOpeCode())
				.append("loadingPort", getLoadingPort()).append("dischargePort", getDischargePort())
				.append("transportType", getTransportType()).append("emptyDepot", getEmptyDepot())
				.append("vgmChk", getVgmChk()).append("vgm", getVgm()).append("vgmPersonInfo", getVgmPersonInfo())
				.append("customStatus", getCustomStatus()).append("paymentStatus", getPaymentStatus())
				.append("processStatus", getProcessStatus()).append("doStatus", getDoStatus())
				.append("doReceivedTime", getDoReceivedTime()).append("userVerifyStatus", getUserVerifyStatus())
				.append("preorderPickup", getPreorderPickup()).append("shiftingContNumber", getShiftingContNumber())
				.append("status", getStatus()).append("block", getBlock()).append("bay", getBay())
				.append("roww", getRow()).append("tier", getTier()).append("year", getYear())
				.append("vslAndVoy", getVslAndVoy()).append("remark", getRemark()).append("createBy", getCreateBy())
				.append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
				.append("updateTime", getUpdateTime()).append("orderNo", getOrderNo()).append("eta", getEta())
				.append("etd", getEtd()).append("location", getLocation()).append("dangerousImo", getDangerousImo())
				.append("dangerousUnno", getDangerousUnno()).append("ovHeight", getOvHeight())
				.append("ovFore", getOvFore()).append("ovAft", getOvAft()).append("ovPort", getOvPort())
				.append("ovStbd", getOvStbd()).append("osHeight", getOsHeight()).append("osPort", getOsPort())
				.append("osStbd", getOsStbd()).append("containerRemark", getContainerRemark())
				.append("setTemp", getSetTemp()).toString();
    }
}
