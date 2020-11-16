package vn.com.irtech.eport.logistic.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Gate Detection Object gate_detection
 * 
 * @author Irtech
 * @date 2020-10-10
 */
public class GateDetection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Gate Number */
    @Excel(name = "Gate Number")
    private String gateNo;

    /** Truck No */
    @Excel(name = "Truck No")
    private String truckNo;

    /** Chassis No */
    @Excel(name = "Chassis No")
    private String chassisNo;

    /** Container 1 */
    @Excel(name = "Container 1")
    private String containerNo1;

    /** Container 2 */
    @Excel(name = "Container 2")
    private String containerNo2;

    /** Check Truck No */
    @Excel(name = "Check Truck No")
    private String chkTruckNo;

    /** Check Chassis No */
    @Excel(name = "Check Chassis No")
    private String chkChassisNo;

    /** Check Container 1 */
    @Excel(name = "Check Container 1")
    private String chkContainerNo1;

    /** Check Container 2 */
    @Excel(name = "Check Container 2")
    private String chkContainerNo2;

    /** Total Weight */
    @Excel(name = "Total Weight")
    private Long totalWgt;

    /** Deduct */
    @Excel(name = "Deduct")
    private Long deduct;

    /** Process Flag: Y/N */
    @Excel(name = "Process Flag: Y/N")
    private String processFlg;

	/** Partner Code 1 */
	private String opeCode1;

	/** Partner Code 1 */
	private String opeCode2;

	/** SZTP 1 */
	private String sztp1;

	/** SZTP 2 */
	private String sztp2;

	/** Vessel 1 */
	private String vslCd1;

	/** Vessel 2 */
	private String vslCd2;

	/** Voyage 1 */
	private String callSeq1;

	/** Voyage 2 */
	private String callSeq2;

	/** Cargo Type 1 */
	private String cargoType1;

	/** Cargo Type 2 */
	private String cargoType2;

	/** Discharge Port 1 */
	private String pod1;

	/** Discharge Port 2 */
	private String pod2;

	/** Weight 1 */
	private Long wgt1;

	/** Weight 2 */
	private Long wgt2;

	/** Location 1 */
	private String location1;

	/** Location 2 */
	private String location2;

	/** Process Order Id */
	private Long processOrderId;

	/** Gatepass */
	private String gatepass;

	/** Status W: waiting, P: progress, S: success, E: Error */
	private String status;

	/** FE 1 */
	private String fe1;

	/** FE 2 */
	private String fe2;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGateNo(String gateNo) 
    {
        this.gateNo = gateNo;
    }

    public String getGateNo() 
    {
        return gateNo;
    }
    public void setTruckNo(String truckNo) 
    {
        this.truckNo = truckNo;
    }

    public String getTruckNo() 
    {
        return truckNo;
    }
    public void setChassisNo(String chassisNo) 
    {
        this.chassisNo = chassisNo;
    }

    public String getChassisNo() 
    {
        return chassisNo;
    }
    public void setContainerNo1(String containerNo1) 
    {
        this.containerNo1 = containerNo1;
    }

    public String getContainerNo1() 
    {
        return containerNo1;
    }
    public void setContainerNo2(String containerNo2) 
    {
        this.containerNo2 = containerNo2;
    }

    public String getContainerNo2() 
    {
        return containerNo2;
    }
    public void setChkTruckNo(String chkTruckNo) 
    {
        this.chkTruckNo = chkTruckNo;
    }

    public String getChkTruckNo() 
    {
        return chkTruckNo;
    }
    public void setChkChassisNo(String chkChassisNo) 
    {
        this.chkChassisNo = chkChassisNo;
    }

    public String getChkChassisNo() 
    {
        return chkChassisNo;
    }
    public void setChkContainerNo1(String chkContainerNo1) 
    {
        this.chkContainerNo1 = chkContainerNo1;
    }

    public String getChkContainerNo1() 
    {
        return chkContainerNo1;
    }
    public void setChkContainerNo2(String chkContainerNo2) 
    {
        this.chkContainerNo2 = chkContainerNo2;
    }

    public String getChkContainerNo2() 
    {
        return chkContainerNo2;
    }
    public void setTotalWgt(Long totalWgt) 
    {
        this.totalWgt = totalWgt;
    }

    public Long getTotalWgt() 
    {
        return totalWgt;
    }
    public void setDeduct(Long deduct) 
    {
        this.deduct = deduct;
    }

    public Long getDeduct() 
    {
        return deduct;
    }

    public void setProcessFlg(String processFlg) 
    {
        this.processFlg = processFlg;
    }

    public String getProcessFlg() 
    {
        return processFlg;
    }

	public String getOpeCode1() {
		return opeCode1;
	}

	public void setOpeCode1(String opeCode1) {
		this.opeCode1 = opeCode1;
	}

	public String getOpeCode2() {
		return opeCode2;
	}

	public void setOpeCode2(String opeCode2) {
		this.opeCode2 = opeCode2;
	}

	public String getSztp1() {
		return sztp1;
	}

	public void setSztp1(String sztp1) {
		this.sztp1 = sztp1;
	}

	public String getSztp2() {
		return sztp2;
	}

	public void setSztp2(String sztp2) {
		this.sztp2 = sztp2;
	}

	public String getVslCd1() {
		return vslCd1;
	}

	public void setVslCd1(String vslCd1) {
		this.vslCd1 = vslCd1;
	}

	public String getVslCd2() {
		return vslCd2;
	}

	public void setVslCd2(String vslCd2) {
		this.vslCd2 = vslCd2;
	}

	public String getCallSeq1() {
		return callSeq1;
	}

	public void setCallSeq1(String callSeq1) {
		this.callSeq1 = callSeq1;
	}

	public String getCallSeq2() {
		return callSeq2;
	}

	public void setCallSeq2(String callSeq2) {
		this.callSeq2 = callSeq2;
	}

	public String getCargoType1() {
		return cargoType1;
	}

	public void setCargoType1(String cargoType1) {
		this.cargoType1 = cargoType1;
	}

	public String getCargoType2() {
		return cargoType2;
	}

	public void setCargoType2(String cargoType2) {
		this.cargoType2 = cargoType2;
	}

	public String getPod1() {
		return pod1;
	}

	public void setPod1(String pod1) {
		this.pod1 = pod1;
	}

	public String getPod2() {
		return pod2;
	}

	public void setPod2(String pod2) {
		this.pod2 = pod2;
	}

	public Long getWgt1() {
		return wgt1;
	}

	public void setWgt1(Long wgt1) {
		this.wgt1 = wgt1;
	}

	public Long getWgt2() {
		return wgt2;
	}

	public void setWgt2(Long wgt2) {
		this.wgt2 = wgt2;
	}

	public String getLocation1() {
		return location1;
	}

	public void setLocation1(String location1) {
		this.location1 = location1;
	}

	public String getLocation2() {
		return location2;
	}

	public void setLocation2(String location2) {
		this.location2 = location2;
	}

	public Long getProcessOrderId() {
		return processOrderId;
	}

	public void setProcessOrderId(Long processOrderId) {
		this.processOrderId = processOrderId;
	}

	public String getGatepass() {
		return gatepass;
	}

	public void setGatepass(String gatepass) {
		this.gatepass = gatepass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFe1() {
		return fe1;
	}

	public void setFe1(String fe1) {
		this.fe1 = fe1;
	}

	public String getFe2() {
		return fe2;
	}

	public void setFe2(String fe2) {
		this.fe2 = fe2;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
				.append("id", getId()).append("gateNo", getGateNo()).append("truckNo", getTruckNo())
				.append("chassisNo", getChassisNo()).append("containerNo1", getContainerNo1())
				.append("containerNo2", getContainerNo2()).append("chkTruckNo", getChkTruckNo())
				.append("chkChassisNo", getChkChassisNo()).append("chkContainerNo1", getChkContainerNo1())
				.append("chkContainerNo2", getChkContainerNo2()).append("totalWgt", getTotalWgt())
				.append("deduct", getDeduct()).append("processFlg", getProcessFlg()).append("createBy", getCreateBy())
				.append("createTime", getCreateTime()).append("updateBy", getUpdateBy())
				.append("opeCode1", getOpeCode1()).append("opeCode2", getOpeCode2())
				.append("sztp1", getSztp1()).append("sztp2", getSztp2()).append("vslCd1", getVslCd1())
				.append("vslCd2", getVslCd2()).append("callSeq1", getCallSeq1()).append("status", getStatus())
				.append("callSeq2", getCallSeq2()).append("cargoType1", getCargoType1())
				.append("cargoType2", getCargoType2()).append("pod1", getPod1()).append("pod2", getPod2())
				.append("wgt1", getWgt2()).append("wgt2", getWgt2()).append("location1", getLocation1())
				.append("location2", getLocation2()).append("processOrderId", getProcessOrderId())
				.append("gatepass", getGatepass()).append("updateTime", getUpdateTime()).append("fe1", getFe1())
				.append("params", getParams()).append("fe2", getFe2()).toString();
    }
}
