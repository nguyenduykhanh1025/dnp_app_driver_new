package vn.com.irtech.eport.system.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author GiapHD
 *
 */
public class ContainerInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cntrNo;

	/** Container State:
	 * 'R': 'Before Reconcile'
		'B': 'After Reconcile'
		'M': 'Manifested'
		'Y': 'Stacking'
		'G': 'In Progress Outgoing'
		'O': 'In Progress Incoming'
		'P': 'Assigned by Trucker'
		'Z': 'Under Shifting'
		'D': 'Delivered'
		'E': 'Empty Gate Out'
	  */
    private String cntrState;

	private String vslCd;
	private String vslNm;
	private String callYear;
	private String callSeq;
	private String userVoy;
	/**
	 * Carrier voyage name: for booking select
	 */
	private String inVoy;
    /** Operator */
    private String ptnrCode;

    /** Operator Name*/
    private String ptnrName;
	/**
	 * Import/Export code
	 * 'X': 'Export'
       'I': 'Import'
       'V': 'Storage'
	 */
	private String ixCd;

	private String fe;

	private String sztp;
	
	private String location;
	
	private String area;

	private String pol;
	private String pod;
    /** Weight */
    private Long wgt;
    
    private String cargoType;

    /** Shipper/consignee */
    private String consignee;

    /** Booking Number */
    private String bookingNo;

    /** BL number */
    private String blNo;

    /** Import Seal Number (Vessel->Terminal)*/
    private String sealNo1;
    
    /** Export Seal Number (Terminal -> Vessel)*/
    private String sealNo3;

    /**
     * Job Order No (IN): Truck->Terminal
     */
	private String jobOdrNo;
	
	/**
     * Job Order No (OUT): Terminal->Truck
     */
	private String jobOdrNo2;
	
	/**
	 * Custom Hold: Y/N
	 */
	private String choldChk;
	
	/**
	 * Terminal hold: Y/N
	 */
	private String tholdChk;
	
	private String remark;
	
	/**
	 * Terminal in Date
	 */
	private Date inDate;
	
	/**
	 * Terminal out Date
	 */
	private Date outDate;
	
	/**
	 * Logistic tax code
	 */
	private String trucker;
	
	private String inLane;
	
	private String outLane;
	
	
	/**
	 * In/Out truck plate number
	 */
	private String truckNo;

	/**
	 * Imdg class no (char:4).
	 */
	private String imdg;
	/**
	 * Un no (char:4).
	 */
	private String unno;

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
	
	/** Set Temperature catos SET_TEMP */
	private String setTemp;
	
	/** Airvent catos AIRVENT */
	private String airvent;
	
	/** Params */
	private Map<String, Object> params;

	/** Block */
	private String block;

	/** Bay */
	private String bay;

	/** Row */
	private Integer roww;

	/** Tier */
	private Integer tier;

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getCntrState() {
		return cntrState;
	}

	public void setCntrState(String cntrState) {
		this.cntrState = cntrState;
	}

	public String getVslCd() {
		return vslCd;
	}

	public void setVslCd(String vslCd) {
		this.vslCd = vslCd;
	}

	public String getVslNm() {
		return vslNm;
	}

	public void setVslNm(String vslNm) {
		this.vslNm = vslNm;
	}

	public String getCallYear() {
		return callYear;
	}

	public void setCallYear(String callYear) {
		this.callYear = callYear;
	}

	public String getCallSeq() {
		return callSeq;
	}

	public void setCallSeq(String callSeq) {
		this.callSeq = callSeq;
	}

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

	public String getInVoy() {
		return inVoy;
	}

	public void setInVoy(String inVoy) {
		this.inVoy = inVoy;
	}

	public String getPtnrCode() {
		return ptnrCode;
	}

	public void setPtnrCode(String ptnrCode) {
		this.ptnrCode = ptnrCode;
	}

	public String getPtnrName() {
		return ptnrName;
	}

	public void setPtnrName(String ptnrName) {
		this.ptnrName = ptnrName;
	}

	public String getIxCd() {
		return ixCd;
	}

	public void setIxCd(String ixCd) {
		this.ixCd = ixCd;
	}

	public String getFe() {
		return fe;
	}

	public void setFe(String fe) {
		this.fe = fe;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public Long getWgt() {
		return wgt;
	}

	public void setWgt(Long wgt) {
		this.wgt = wgt;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
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

	public String getSealNo1() {
		return sealNo1;
	}

	public void setSealNo1(String sealNo1) {
		this.sealNo1 = sealNo1;
	}

	public String getSealNo3() {
		return sealNo3;
	}

	public void setSealNo3(String sealNo3) {
		this.sealNo3 = sealNo3;
	}

	public String getJobOdrNo() {
		return jobOdrNo;
	}

	public void setJobOdrNo(String jobOdrNo) {
		this.jobOdrNo = jobOdrNo;
	}

	public String getJobOdrNo2() {
		return jobOdrNo2;
	}

	public void setJobOdrNo2(String jobOdrNo2) {
		this.jobOdrNo2 = jobOdrNo2;
	}

	public String getCholdChk() {
		return choldChk;
	}

	public void setCholdChk(String choldChk) {
		this.choldChk = choldChk;
	}

	public String getTholdChk() {
		return tholdChk;
	}

	public void setTholdChk(String tholdChk) {
		this.tholdChk = tholdChk;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getTrucker() {
		return trucker;
	}

	public void setTrucker(String trucker) {
		this.trucker = trucker;
	}

	public String getInLane() {
		return inLane;
	}

	public void setInLane(String inLane) {
		this.inLane = inLane;
	}

	public String getOutLane() {
		return outLane;
	}

	public void setOutLane(String outLane) {
		this.outLane = outLane;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getImdg() {
		return imdg;
	}

	public void setImdg(String imdg) {
		this.imdg = imdg;
	}

	public String getUnno() {
		return unno;
	}

	public void setUnno(String unno) {
		this.unno = unno;
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

	public String getAirvent() {
		return airvent;
	}

	public void setAirvent(String airvent) {
		this.airvent = airvent;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
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

	public Integer getRoww() {
		return roww;
	}

	public void setRoww(Integer roww) {
		this.roww = roww;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}
	
}
