package vn.com.irtech.eport.logistic.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ShipmentDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * Ma Lo
     */
    @Excel(name = "Ma Lo")
    private Long shipmentId;

    /**
     * group id
     */
    private Long logisticGroupId;

    /**
     * Ma Lenh
     */
    @Excel(name = "Ma Lenh")
    private Long processOrderId;

    /**
     * Ma DK
     */
    @Excel(name = "Ma DK")
    private String registerNo;

    /**
     * Container Number
     */
    @Excel(name = "Container Number")
    private String containerNo;

    /**
     * Container Status (S,D)
     */
    @Excel(name = "Container Status (S,D)")
    private String containerStatus;

    /**
     * Size Type
     */
    @Excel(name = "Size Type")
    private String sztp;

    /**
     * Size Type define
     */
    @Excel(name = "Size Type Define")
    private String sztpDefine;

    /**
     * Temperature
     */
    @Excel(name = "Temperature")
    private String temperature;

    /**
     * FE
     */
    @Excel(name = "FE")
    private String fe;

    /**
     * Booking Number
     */
    @Excel(name = "Booking Number")
    private String bookingNo;

    /**
     * BL number
     */
    @Excel(name = "BL number")
    private String blNo;

    /**
     * Seal Number
     */
    @Excel(name = "Seal Number")
    private String sealNo;

    /**
     * Shipper/consignee
     */
    @Excel(name = "Shipper/consignee")
    private String consignee;

    /**
     * Han Lenh
     */
    @Excel(name = "Han Lenh", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiredDem;

    /**
     * Weight
     */
    @Excel(name = "Depot Free Time")
    private Integer detFreeTime;

    /**
     * Weight
     */
    @Excel(name = "Weight")
    private Long wgt;

    /**
     * Vessel name
     */
    @Excel(name = "Vessel name")
    private String vslName;

    /**
     * Vessel code
     */
    @Excel(name = "Vessel code")
    private String vslNm;

    /**
     * Voyage
     */
    @Excel(name = "Voyage")
    private String voyNo;

    /**
     * Voyage carrier
     */
    @Excel(name = "Voyage carrier")
    private String voyCarrier;

    /**
     * Operator Code
     */
    @Excel(name = "Operator Code")
    private String opeCode;

    /**
     * Carrier Name
     */
    @Excel(name = "Carrier name")
    private String carrierName;

    /**
     * Cang Chuyen Tai
     */
    @Excel(name = "Cang Chuyen Tai")
    private String loadingPort;

    /**
     * Cang Dich
     */
    @Excel(name = "Cang Dich")
    private String dischargePort;

    /**
     * Phuong Tien
     */
    @Excel(name = "Phuong Tien")
    private String transportType;

    /**
     * Empty depot
     */
    @Excel(name = "Nơi Ha Vo")
    private String emptyDepot;

    /**
     * Han Lenh
     */
    @Excel(name = "Han Tra Vo", width = 30, dateFormat = "yyyy-MM-dd")
    private Date emptyExpiredDem;

    /**
     * Empty depot
     */
    @Excel(name = "Bai Ha Vo")
    private String emptyDepotLocation;

    /**
     * Cargo Type
     */
    @Excel(name = "Loại Hang")
    private String cargoType;

    /**
     * VGM Check
     */
    @Excel(name = "VGM Check")
    private Boolean vgmChk;

    /**
     * VGM
     */
    @Excel(name = "VGM")
    private String vgm;

    /**
     * VGM Person Info
     */
    @Excel(name = "VGM Person Info")
    private String vgmPersonInfo;

    /**
     * Boc Chi Dinh (Y,N)
     */
    @Excel(name = "Boc Chi Dinh (Y,N)")
    private String preorderPickup;

    /**
     * Trang Thai Thanh Toan Phi Dich Chuyen
     */
    @Excel(name = "Trang Thai Thanh Toan Phi Dich Chuyen (Y, N)")
    private String prePickupPaymentStatus;

    /**
     * Custom Status (H,R)
     */
    @Excel(name = "Custom Status (H,R)")
    private String customStatus;

    /**
     * Payment Status (Y,N,W,E)
     */
    @Excel(name = "Payment Status (Y,N,W,E)")
    private String paymentStatus;

    /**
     * Process Status(Y,N,E)
     */
    @Excel(name = "Process Status(Y,N,E)")
    private String processStatus;

    /**
     * DO Status(Y,N)
     */
    @Excel(name = "DO Status(Y,N")
    private String doStatus;

    /**
     * Ngay nhan DO goc
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date doReceivedTime;

    /**
     * Xac Thuc (Y,N)
     */
    @Excel(name = "Xac Thuc (Y,N)")
    private String userVerifyStatus;

    /**
     * Finish Status(Y,N)
     */
    @Excel(name = "Finish Status(Y,N")
    private String finishStatus;

    /**
     * Status
     */
    @Excel(name = "Status")
    private Integer status;

    /**
     * Order No
     */
    @Excel(name = "Order No")
    private String orderNo;

    /**
     * Tax Code
     */
    @Excel(name = "Consignee Tax Code")
    private String taxCode;

    /**
     * Consignee By Tax Code
     */
    @Excel(name = "Consignee By Tax Code")
    private String consigneeByTaxCode;

    /**
     * Consignee By Tax Code
     */
    @Excel(name = "Customs Numbers")
    private String customsNo;

    /**
     * Create time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date customScanTime;

    /**
     * Container supply status
     */
    @Excel(name = "Cont Supply Status")
    private String contSupplyStatus;

    /**
     * Planning date
     */
    @Excel(name = "Planning Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planningDate;

    /**
     * Quality requirement
     */
    @Excel(name = "Quality Requirement")
    private String qualityRequirement;

    @Excel(name = "Container Supplier Name")
    private String contSupplierName;

    /**
     * Container supply remark
     */
    @Excel(name = "Cont Supply Remark")
    private String contSupplyRemark;

    @Excel(name = "Payment Type(Cash/Credit)")
    private String payType;

    @Excel(name = "Payer Taxcode")
    private String payer;

    @Excel(name = "Payer Name")
    private String payerName;

    @Excel(name = "Delivery Address")
    private String deliveryAddress;

    @Excel(name = "Deliver Mobile")
    private String deliveryMobile;

    @Excel(name = "Delivery Remark")
    private String deliveryRemark;

    private String block;

    private String bay;

    private int roww;

    private int tier;

    private Integer driverAmount;

    private Integer assignNumber;

    private Integer serviceType;

    private String year;

    private String vslAndVoy;

    private String invoiceNo;

    private Long vatAfterFee;

    /**
     * info for robot to know the index of record in catos
     */
    private Integer index;

    /**
     * Ngay tau den theo tau va chuyen
     */
    private Date eta;

    /**
     * Ngay tau di (plan)
     */
    private Date etd;

    /**
     * Ten mat hang: Khai bao khi ha bai cho xuat
     */
    private String commodity;

    /**
     * Vị trí
     */
    private String location;

    /**
     * Tinh trang container
     */
    private String containerRemark;

    /**
     * Tinh trang container
     */
    private String contSpecialStatus;

    /**
     * vgm Đơn vị kiểm định
     */
    private String vgmInspectionDepartment;

    /**
     * vgm Max gross
     */
    private String vgmMaxGross;

    /**
     * Ngày cài nhiệt độ
     */
    private Date daySetupTemperature;

    /**
     * T: là cont quá khổ | F: không phải là công quá khổ
     */
    private String oversize;

    /**
     * Loại cont quá khổ
     */
    private String oversizeType;

    /**
     * Quá khổ bên trên
     */
    private String oversizeTop;

    /**
     * Quá khổ bên phải
     */
    private String oversizeRight;

    /**
     * Quá khổ bên trái
     */
    private String oversizeLeft;

    /**
     * Quá khổ phía trước
     */
    private String oversizeFront;

    /**
     * Quá khổ phía sau
     */
    private String oversizeBack;

    /**
     * T : là cont dangerous | F: ko là cont dangerous | 2 : là cont dangerous chờ xét duyệt | 3 :  la cont dangerous đã được xét duyệt | 4 từ chối
     */
    private String dangerous;

    /**
     * Chỉ số nguy hiểm IMO
     */
    private String dangerousImo;

    /**
     * Chỉ số nguy hiểm UNNO
     */
    private String dangerousUnno;

    /**
     * Tên hàng hóa cont nguy hiểm
     */
    private String dangerousNameProduct;

    /**
     * Quy cách đóng gói cont nguy hiểm
     */
    private String dangerousPacking;
    // add cont sizeover
    private String osHeight; 
    private String osPort;
    private String osStbd;
    private String ovAft;
    private String ovFore;
    private String ovHeight;
    private String ovPort;
    private String ovStbd; 
    // cont lạnh
    private String frozenStatus; 
    
    private String powerDrawDate; 
      
    public String getPowerDrawDate() {
		return powerDrawDate;
	}

	public void setPowerDrawDate(String powerDrawDate) {
		this.powerDrawDate = powerDrawDate;
	}

	public String getFrozenStatus() {
		return frozenStatus;
	}

	public void setFrozenStatus(String frozenStatus) {
		this.frozenStatus = frozenStatus;
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

	public String getOvAft() {
		return ovAft;
	}

	public void setOvAft(String ovAft) {
		this.ovAft = ovAft;
	}

	public String getOvFore() {
		return ovFore;
	}

	public void setOvFore(String ovFore) {
		this.ovFore = ovFore;
	}

	public String getOvHeight() {
		return ovHeight;
	}

	public void setOvHeight(String ovHeight) {
		this.ovHeight = ovHeight;
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

	public String getContSpecialStatus() {
        return contSpecialStatus;
    }

    public void setContSpecialStatus(String contSpecialStatus) {
        this.contSpecialStatus = contSpecialStatus;
    }

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

    public Integer getDetFreeTime() {
        return detFreeTime;
    }

    public void setDetFreeTime(Integer detFreeTime) {
        this.detFreeTime = detFreeTime;
    }

    public void setWgt(Long wgt) {
        this.wgt = wgt;
    }

    public Long getWgt() {
        return wgt;
    }

    public String getVslName() {
        return vslName;
    }

    public void setVslName(String vslName) {
        this.vslName = vslName;
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

    public String getVoyCarrier() {
        return voyCarrier;
    }

    public void setVoyCarrier(String voyCarrier) {
        this.voyCarrier = voyCarrier;
    }

    public void setOpeCode(String opeCode) {
        this.opeCode = opeCode;
    }

    public String getOpeCode() {
        return opeCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
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

    public void setPrePickupPaymentStatus(String prePickupPaymentStatus) {
        this.prePickupPaymentStatus = prePickupPaymentStatus;
    }

    public String getPrePickupPaymentStatus() {
        return prePickupPaymentStatus;
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

    public String getDoStatus() {
        return doStatus;
    }

    public void setDoReceivedTime(Date doReceivedTime) {
        this.doReceivedTime = doReceivedTime;
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

    public String getFinishStatus() {
        return this.finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryMobile() {
        return deliveryMobile;
    }

    public void setDeliveryMobile(String deliveryMobile) {
        this.deliveryMobile = deliveryMobile;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
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

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getVatAfterFee() {
        return vatAfterFee;
    }

    public void setVatAfterFee(Long vatAfterFee) {
        this.vatAfterFee = vatAfterFee;
    }

    public String getConsigneeByTaxCode() {
        return consigneeByTaxCode;
    }

    public void setConsigneeByTaxCode(String consigneeByTaxCode) {
        this.consigneeByTaxCode = consigneeByTaxCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCustomsNo() {
        return customsNo;
    }

    public void setCustomsNo(String customsNo) {
        this.customsNo = customsNo;
    }

    public Date getCustomScanTime() {
        return customScanTime;
    }

    public void setCustomScanTime(Date customScanTime) {
        this.customScanTime = customScanTime;
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

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getRoww() {
        return roww;
    }

    public void setRoww(int roww) {
        this.roww = roww;
    }

    public String getVgmInspectionDepartment() {
        return vgmInspectionDepartment;
    }

    public void setVgmInspectionDepartment(String vgmInspectionDepartment) {
        this.vgmInspectionDepartment = vgmInspectionDepartment;
    }

    public String getVgmMaxGross() {
        return vgmMaxGross;
    }

    public void setVgmMaxGross(String vgmMaxGross) {
        this.vgmMaxGross = vgmMaxGross;
    }

    public Date getDaySetupTemperature() {
        return daySetupTemperature;
    }

    public void setDaySetupTemperature(Date daySetupTemperature) {
        this.daySetupTemperature = daySetupTemperature;
    }

    public String getOversize() {
        return oversize;
    }

    public void setOversize(String oversize) {
        this.oversize = oversize;
    }

    public String getOversizeType() {
        return oversizeType;
    }

    public void setOversizeType(String oversizeType) {
        this.oversizeType = oversizeType;
    }

    public String getOversizeTop() {
        return oversizeTop;
    }

    public void setOversizeTop(String oversizeTop) {
        this.oversizeTop = oversizeTop;
    }

    public String getOversizeRight() {
        return oversizeRight;
    }

    public void setOversizeRight(String oversizeRight) {
        this.oversizeRight = oversizeRight;
    }

    public String getOversizeLeft() {
        return oversizeLeft;
    }

    public void setOversizeLeft(String oversizeLeft) {
        this.oversizeLeft = oversizeLeft;
    }

    public String getOversizeFront() {
        return oversizeFront;
    }

    public void setOversizeFront(String oversizeFront) {
        this.oversizeFront = oversizeFront;
    }

    public String getOversizeBack() {
        return oversizeBack;
    }

    public void setOversizeBack(String oversizeBack) {
        this.oversizeBack = oversizeBack;
    }

    public String getDangerous() {
        return dangerous;
    }

    public void setDangerous(String dangerous) {
        this.dangerous = dangerous;
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

    public String getDangerousNameProduct() {
        return dangerousNameProduct;
    }

    public void setDangerousNameProduct(String dangerousNameProduct) {
        this.dangerousNameProduct = dangerousNameProduct;
    }

    public String getDangerousPacking() {
        return dangerousPacking;
    }

    public void setDangerousPacking(String dangerousPacking) {
        this.dangerousPacking = dangerousPacking;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("shipmentId", getShipmentId())
                .append("logisticGroupId", getLogisticGroupId())
                .append("processOrderId", getProcessOrderId())
                .append("registerNo", getRegisterNo())
                .append("containerNo", getContainerNo())
                .append("containerStatus", getContainerStatus())
                .append("sztp", getSztp())
                .append("sztpDefine", getSztpDefine())
                .append("temperature", getTemperature())
                .append("fe", getFe())
                .append("bookingNo", getBookingNo())
                .append("blNo", getBlNo())
                .append("sealNo", getSealNo())
                .append("consignee", getConsignee())
                .append("expiredDem", getExpiredDem())
                .append("detFreeTime", getDetFreeTime())
                .append("wgt", getWgt())
                .append("vslName", getVslName())
                .append("vslNm", getVslNm())
                .append("voyNo", getVoyNo())
                .append("voyCarrier", getVoyCarrier())
                .append("opeCode", getOpeCode())
                .append("carrierName", getCarrierName())
                .append("loadingPort", getLoadingPort())
                .append("dischargePort", getDischargePort())
                .append("transportType", getTransportType())
                .append("emptyDepot", getEmptyDepot())
                .append("cargoType", getCargoType())
                .append("commodity", getCommodity())
                .append("vgmChk", getVgmChk())
                .append("vgm", getVgm())
                .append("vgmPersonInfo", getVgmPersonInfo())
                .append("preorderPickup", getPreorderPickup())
                .append("prePickupPaymentStatus", getPrePickupPaymentStatus())
                .append("customStatus", getCustomStatus())
                .append("paymentStatus", getPaymentStatus())
                .append("processStatus", getProcessStatus())
                .append("doStatus", getDoStatus())
                .append("doReceivedTime", getDoReceivedTime())
                .append("userVerifyStatus", getUserVerifyStatus())
                .append("finishStatus", getFinishStatus())
                .append("status", getStatus())
                .append("orderNo", getOrderNo())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("block", getBlock())
                .append("bay", getBay())
                .append("roww", getRow())
                .append("tier", getTier())
                .append("driverAmount", getDriverAmount())
                .append("payType", getPayType())
                .append("assignNumber", getAssignNumber())
                .append("serviceType", getServiceType())
                .append("year", getYear())
                .append("vslAndVoy", getVslAndVoy())
                .append("invoiceNo", getInvoiceNo())
                .append("vatAfterFee", getVatAfterFee())
                .append("taxCode", getTaxCode())
                .append("consigneeByTaxCode", getConsigneeByTaxCode())
                .append("customsNo", getCustomsNo())
                .append("contSupplyStatus", getContSupplyStatus())
                .append("planningDate", getPlanningDate())
                .append("qualityRequirement", getQualityRequirement())
                .append("contSupplyRemark", getContSupplyRemark())
                .append("customScanTime", getCustomScanTime())
                .append("eta", getEta())
                .append("etd", getEtd())
                .append("location", getLocation())
                .append("containerRemark", getContainerRemark())
                .append("vgmInspectionDepartment", getVgmInspectionDepartment())
                .append("vgmMaxGross", getVgmMaxGross())
                .append("daySetupTemperature", getDaySetupTemperature())
                .append("oversize", getOversize())
                .append("oversizeType", getOversizeType())
                .append("oversizeTop", getOversizeTop())
                .append("oversizeRight", getOversizeRight())
                .append("oversizeLeft", getOversizeLeft())
                .append("oversizeFront", getOversizeFront())
                .append("oversizeBack", getOversizeBack())
                .append("dangerous", getDangerous())
                .append("dangerousImo", getDangerousImo())
                .append("dangerousUnno", getDangerousUnno())
                .append("dangerousNameProduct", getDangerousNameProduct())
                .append("dangerousPacking", getDangerousPacking())
                .append("contSpecialStatus", getContSpecialStatus())
                .toString();
    }
}
