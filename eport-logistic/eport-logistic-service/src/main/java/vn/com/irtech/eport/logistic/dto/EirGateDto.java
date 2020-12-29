/**
 * 
 */
package vn.com.irtech.eport.logistic.dto;

import java.util.Date;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * @author Trong Hieu
 *
 */
public class EirGateDto extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** Container No */
	@Excel(name = "Container No")
	private String containerNo;

	/** Activity */
	@Excel(name = "Hoạt Động")
	private String activity;

	/** Checked In */
	@Excel(name = "Checked In")
	private Date checkedIn;

	/** Checked Out */
	@Excel(name = "Checked Out")
	private Date checkedOut;

	/** Vsl Nm */
	@Excel(name = "Tàu")
	private String vslNm;

	/** Voyage */
	@Excel(name = "Chuyến")
	private String voyage;

	/** Sztp */
	@Excel(name = "Kích Thước")
	private String sztp;

	/** Cntr Type */
	@Excel(name = "Loại Container")
	private String cntrType;

	/** Operator */
	@Excel(name = "Operator")
	private String operator;

	/** Cont Status */
	@Excel(name = "Trạng Thái Container")
	private String contStatus;

	/** Reference */
	@Excel(name = "Tham Chiếu")
	private String reference;

	/** Position */
	@Excel(name = "Vị Trí")
	private String position;

	/** Area */
	@Excel(name = "Khu Vực")
	private String area;

	/** Cargo Type */
	@Excel(name = "Loại Hàng")
	private String cargoType;

	/** Imdg */
	@Excel(name = "Imdg")
	private String imdg;

	/** Seal1 */
	@Excel(name = "Seal 1")
	private String seal1;

	/** Seal2 */
	@Excel(name = "Seal 2")
	private String seal2;

	/** Truck No */
	@Excel(name = "Biển Số Xe")
	private String truckNo;

	/** Trucker Tax No */
	@Excel(name = "Mã Số Thuế Trucker")
	private String truckerTaxNo;

	/** Payer Tax No */
	@Excel(name = "Mã Số Thuế Payer")
	private String payerTaxNo;

	/** Remark */
	@Excel(name = "Ghi Chú")
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
