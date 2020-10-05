package vn.com.irtech.api.dto;

import java.io.Serializable;
import java.util.Date;

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
	private String choldCheck;
	
	/**
	 * Terminal hold: Y/N
	 */
	private String tholdCheck;
	
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

	public String getCholdCheck() {
		return choldCheck;
	}

	public void setCholdCheck(String choldCheck) {
		this.choldCheck = choldCheck;
	}

	public String getTholdCheck() {
		return tholdCheck;
	}

	public void setTholdCheck(String tholdCheck) {
		this.tholdCheck = tholdCheck;
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
	
}