/**
 * 
 */
package vn.com.irtech.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trong Hieu
 *
 */
public class EirGateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Container No */
	private String containerNo;

	/** Activity */
	private String activity;

	/** Checked In */
	private Date checkedIn;

	/** Checked Out */
	private Date checkedOut;

	/** Vsl Nm */
	private String vslNm;

	/** Voyage */
	private String voyage;

	/** Sztp */
	private String sztp;

	/** Cntr Type */
	private String cntrType;

	/** Operator */
	private String operator;

	/** Cont Status */
	private String contStatus;

	/** Reference */
	private String reference;

	/** Position */
	private String position;

	/** Area */
	private String area;

	/** Cargo Type */
	private String cargoType;

	/** Imdg */
	private String imdg;

	/** Seal1 */
	private String seal1;

	/** Seal2 */
	private String seal2;

	/** Truck No */
	private String truckNo;

	/** Trucker Tax No */
	private String truckerTaxNo;

	/** Payer Tax No */
	private String payerTaxNo;

	/** Remark */
	private String remark;

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(Date checkedIn) {
		this.checkedIn = checkedIn;
	}

	public Date getCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(Date checkedOut) {
		this.checkedOut = checkedOut;
	}

	public String getVslNm() {
		return vslNm;
	}

	public void setVslNm(String vslNm) {
		this.vslNm = vslNm;
	}

	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getCntrType() {
		return cntrType;
	}

	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContStatus() {
		return contStatus;
	}

	public void setContStatus(String contStatus) {
		this.contStatus = contStatus;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getImdg() {
		return imdg;
	}

	public void setImdg(String imdg) {
		this.imdg = imdg;
	}

	public String getSeal1() {
		return seal1;
	}

	public void setSeal1(String seal1) {
		this.seal1 = seal1;
	}

	public String getSeal2() {
		return seal2;
	}

	public void setSeal2(String seal2) {
		this.seal2 = seal2;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getTruckerTaxNo() {
		return truckerTaxNo;
	}

	public void setTruckerTaxNo(String truckerTaxNo) {
		this.truckerTaxNo = truckerTaxNo;
	}

	public String getPayerTaxNo() {
		return payerTaxNo;
	}

	public void setPayerTaxNo(String payerTaxNo) {
		this.payerTaxNo = payerTaxNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
