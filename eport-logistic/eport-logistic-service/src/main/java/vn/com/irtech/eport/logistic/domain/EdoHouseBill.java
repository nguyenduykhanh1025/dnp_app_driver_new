package vn.com.irtech.eport.logistic.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Master Bill Object edo_house_bill
 * 
 * @author irtech
 * @date 2020-08-10
 */
public class EdoHouseBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Logistic Group */
    @Excel(name = "Logistic Group")
    private Long logisticGroupId;

    /** Logistic Account */
    @Excel(name = "Logistic Account")
    private Long logisticAccountId;

    /** eDO ID */
    @Excel(name = "eDO ID")
    private Long edoId;

    /** Bill Of Lading */
    @Excel(name = "Bill Of Lading")
    private String billOfLading;

    /** Order Number */
    @Excel(name = "Order Number")
    private String orderNumber;

    /** Master Bill No */
    @Excel(name = "Master Bill No")
    private String masterBillNo;

    /** Consignee */
    @Excel(name = "Consignee")
    private String consignee;

    /** House Bill No */
    @Excel(name = "House Bill No")
    private String houseBillNo;

    /** House Bill No2 */
    @Excel(name = "House Bill No2")
    private String houseBillNo2;

    /** Consignee2 */
    @Excel(name = "Consignee2")
    private String consignee2;

    /** Container Number */
    @Excel(name = "Container Number")
    private String containerNumber;

    /** Size type */
    @Excel(name = "Size type")
    private String sztp;

    /** OPR */
    @Excel(name = "OPR")
    private String opr;

    /** Vessel */
    @Excel(name = "Vessel")
    private String vessel;

    /** Voyage No */
    @Excel(name = "Voyage No")
    private String voyNo;

    /** Carrier code */
    @Excel(name = "Carrier code")
    private String carrierCode;

    private Edo edo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setLogisticAccountId(Long logisticAccountId) 
    {
        this.logisticAccountId = logisticAccountId;
    }

    public Long getLogisticAccountId() 
    {
        return logisticAccountId;
    }
    public void setEdoId(Long edoId) 
    {
        this.edoId = edoId;
    }

    public Long getEdoId() 
    {
        return edoId;
    }
    public void setBillOfLading(String billOfLading) 
    {
        this.billOfLading = billOfLading;
    }

    public String getBillOfLading() 
    {
        return billOfLading;
    }
    public void setOrderNumber(String orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() 
    {
        return orderNumber;
    }
    public void setMasterBillNo(String masterBillNo) 
    {
        this.masterBillNo = masterBillNo;
    }

    public String getMasterBillNo() 
    {
        return masterBillNo;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setHouseBillNo(String houseBillNo) 
    {
        this.houseBillNo = houseBillNo;
    }

    public String getHouseBillNo() 
    {
        return houseBillNo;
    }
    public void setHouseBillNo2(String houseBillNo2) 
    {
        this.houseBillNo2 = houseBillNo2;
    }

    public String getHouseBillNo2() 
    {
        return houseBillNo2;
    }
    public void setConsignee2(String consignee2) 
    {
        this.consignee2 = consignee2;
    }

    public String getConsignee2() 
    {
        return consignee2;
    }
    public void setContainerNumber(String containerNumber) 
    {
        this.containerNumber = containerNumber;
    }

    public String getContainerNumber() 
    {
        return containerNumber;
    }
    public void setSztp(String sztp) 
    {
        this.sztp = sztp;
    }

    public String getSztp() 
    {
        return sztp;
    }
    public void setOpr(String opr) 
    {
        this.opr = opr;
    }

    public String getOpr() 
    {
        return opr;
    }
    public void setVessel(String vessel) 
    {
        this.vessel = vessel;
    }

    public String getVessel() 
    {
        return vessel;
    }
    public void setVoyNo(String voyNo) 
    {
        this.voyNo = voyNo;
    }

    public String getVoyNo() 
    {
        return voyNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public Edo getEdo() {
        return edo;
    }

    public void setEdo(Edo edo) {
        this.edo = edo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("logisticAccountId", getLogisticAccountId())
            .append("edoId", getEdoId())
            .append("billOfLading", getBillOfLading())
            .append("orderNumber", getOrderNumber())
            .append("masterBillNo", getMasterBillNo())
            .append("consignee", getConsignee())
            .append("houseBillNo", getHouseBillNo())
            .append("houseBillNo2", getHouseBillNo2())
            .append("consignee2", getConsignee2())
            .append("containerNumber", getContainerNumber())
            .append("sztp", getSztp())
            .append("opr", getOpr())
            .append("vessel", getVessel())
            .append("voyNo", getVoyNo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
