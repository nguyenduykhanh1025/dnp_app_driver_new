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

    /** Weight */
    @Excel(name = "Weight")
    private Long wgt;

    /** Process Flag: Y/N */
    @Excel(name = "Process Flag: Y/N")
    private String processFlg;

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
    public void setWgt(Long wgt) 
    {
        this.wgt = wgt;
    }

    public Long getWgt() 
    {
        return wgt;
    }
    public void setProcessFlg(String processFlg) 
    {
        this.processFlg = processFlg;
    }

    public String getProcessFlg() 
    {
        return processFlg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("gateNo", getGateNo())
            .append("truckNo", getTruckNo())
            .append("chassisNo", getChassisNo())
            .append("containerNo1", getContainerNo1())
            .append("containerNo2", getContainerNo2())
            .append("chkTruckNo", getChkTruckNo())
            .append("chkChassisNo", getChkChassisNo())
            .append("chkContainerNo1", getChkContainerNo1())
            .append("chkContainerNo2", getChkContainerNo2())
            .append("totalWgt", getTotalWgt())
            .append("deduct", getDeduct())
            .append("wgt", getWgt())
            .append("processFlg", getProcessFlg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
