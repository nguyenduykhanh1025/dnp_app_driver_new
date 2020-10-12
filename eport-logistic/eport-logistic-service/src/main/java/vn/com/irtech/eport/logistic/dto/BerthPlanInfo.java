package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HieuNT
 *
 */
public class BerthPlanInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Vessel */
	private String vslCd;

	/** Voyage of catos */
	private String callSeq;

	/** Year */
	private String callYear;

	/** Voyage of carrier */
	private String inVoy;

	/** Date vessel arrive */
	private Date longtermEta;

	/** Date vessel depart */
	private Date etd;

	/** User Voy */
	private String userVoy;

	/** Partner Code */
	private String ptnrCode;

	public String getVslCd() {
		return vslCd;
	}

	public void setVslCd(String vslCd) {
		this.vslCd = vslCd;
	}

	public String getCallSeq() {
		return callSeq;
	}

	public void setCallSeq(String callSeq) {
		this.callSeq = callSeq;
	}

	public String getCallYear() {
		return callYear;
	}

	public void setCallYear(String callYear) {
		this.callYear = callYear;
	}

	public String getInVoy() {
		return inVoy;
	}

	public void setInVoy(String inVoy) {
		this.inVoy = inVoy;
	}

	public Date getLongtermEta() {
		return longtermEta;
	}

	public void setLongtermEta(Date longtermEta) {
		this.longtermEta = longtermEta;
	}

	public Date getEtd() {
		return etd;
	}

	public void setEtd(Date etd) {
		this.etd = etd;
	}

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

	public String getPtnrCode() {
		return ptnrCode;
	}

	public void setPtnrCode(String ptnrCode) {
		this.ptnrCode = ptnrCode;
	}

}
