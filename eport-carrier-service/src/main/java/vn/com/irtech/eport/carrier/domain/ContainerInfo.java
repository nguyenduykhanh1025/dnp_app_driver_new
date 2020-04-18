package vn.com.irtech.eport.carrier.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Container Infomation Object container_info
 * 
 * @author Admin
 * @date 2020-04-16
 */
public class ContainerInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long cntrId;

    /** Container Number */
    @Excel(name = "Container Number")
    private String cntrNo;

    /** Size/type */
    @Excel(name = "Size/type")
    private String sztp2;

    /** F/E */
    @Excel(name = "F/E")
    private String fe;

    /** Operator */
    @Excel(name = "Operator")
    private String ptnrCode;

    /** yard location - Block */
    @Excel(name = "yard location - Block")
    private String block;

    /** yard location - Bay */
    @Excel(name = "yard location - Bay")
    private String bay;

    /** yard location - Row */
    @Excel(name = "yard location - Row")
    private String roww;

    /** yard location - Tier */
    @Excel(name = "yard location - Tier")
    private String tier;

    /** Weight */
    @Excel(name = "Weight")
    private Long wgt;

    /** Vessel name */
    @Excel(name = "Vessel name")
    private String vslNm;

    /** Shipper/consignee */
    @Excel(name = "Shipper/consignee")
    private String consignee;

    /** Booking Number */
    @Excel(name = "Booking Number")
    private String bookingNo;

    /** BL number */
    @Excel(name = "BL number")
    private String blNo;

    /** Seal Number */
    @Excel(name = "Seal Number")
    private String sealNo1;

    /** Gate mode for terminal in */
    @Excel(name = "Gate mode for terminal in")
    private String dispatchMode;

    /** Gate mode for terminal out */
    @Excel(name = "Gate mode for terminal out")
    private String dispatchMode2;

    /** Gate In Date */
    @Excel(name = "Gate In Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inDate;

    /** Gate Out Date */
    @Excel(name = "Gate Out Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outDate;

    /** VGM */
    @Excel(name = "VGM")
    private String vgm;

    /** Container State  (S: Stacking, D:Delivered) */
    @Excel(name = "Container State  (S: Stacking, D:Delivered)")
    private String cntrState;

    /** SEARCH GET-IN */
    private Date getInToDate;

    private Date getInFromDate;

    public void setGetInToDate(Date getInToDate) {
        this.getInToDate = getInToDate;
    }
    
    public Date getGetInToDate() {
        return this.getInToDate;
    }

    public void setGetInFromDate(Date getInFromDate) {
        this.getInFromDate = getInFromDate;
    }

    public Date getGetInFromDate() {
        return this.getInFromDate;
    }
    /** END SEARCH GET-IN */

    /** SEARCH GET-OUT */
     private Date getOutToDate;

     private Date getOutFromDate;
 
     public void setGetOutToDate(Date getOutToDate) {
         this.getOutToDate = getOutToDate;
     }
     
     public Date getGetOutToDate() {
         return this.getOutToDate;
     }
 
     public void setGetOutFromDate(Date getOutFromDate) {
         this.getOutFromDate = getOutFromDate;
     }
 
     public Date getGetOutFromDate() {
         return this.getOutFromDate;
     }
     /** END SEARCH GET-OUT */

    public void setCntrId(Long cntrId) 
    {
        this.cntrId = cntrId;
    }

    public Long getCntrId() 
    {
        return cntrId;
    }
    public void setCntrNo(String cntrNo) 
    {
        this.cntrNo = cntrNo;
    }

    public String getCntrNo() 
    {
        return cntrNo;
    }
    public void setSztp2(String sztp2) 
    {
        this.sztp2 = sztp2;
    }

    public String getSztp2() 
    {
        return sztp2;
    }
    public void setFe(String fe) 
    {
        this.fe = fe;
    }

    public String getFe() 
    {
        return fe;
    }
    public void setPtnrCode(String ptnrCode) 
    {
        this.ptnrCode = ptnrCode;
    }

    public String getPtnrCode() 
    {
        return ptnrCode;
    }
    public void setBlock(String block) 
    {
        this.block = block;
    }

    public String getBlock() 
    {
        return block;
    }
    public void setBay(String bay) 
    {
        this.bay = bay;
    }

    public String getBay() 
    {
        return bay;
    }
    public void setRoww(String roww) 
    {
        this.roww = roww;
    }

    public String getRoww() 
    {
        return roww;
    }
    public void setTier(String tier) 
    {
        this.tier = tier;
    }

    public String getTier() 
    {
        return tier;
    }
    public void setWgt(Long wgt) 
    {
        this.wgt = wgt;
    }

    public Long getWgt() 
    {
        return wgt;
    }
    public void setVslNm(String vslNm) 
    {
        this.vslNm = vslNm;
    }

    public String getVslNm() 
    {
        return vslNm;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
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
    public void setSealNo1(String sealNo1) 
    {
        this.sealNo1 = sealNo1;
    }

    public String getSealNo1() 
    {
        return sealNo1;
    }
    public void setDispatchMode(String dispatchMode) 
    {
        this.dispatchMode = dispatchMode;
    }

    public String getDispatchMode() 
    {
        return dispatchMode;
    }
    public void setDispatchMode2(String dispatchMode2) 
    {
        this.dispatchMode2 = dispatchMode2;
    }

    public String getDispatchMode2() 
    {
        return dispatchMode2;
    }
    public void setInDate(Date inDate) 
    {
        this.inDate = inDate;
    }

    public Date getInDate() 
    {
        return inDate;
    }
    public void setOutDate(Date outDate) 
    {
        this.outDate = outDate;
    }

    public Date getOutDate() 
    {
        return outDate;
    }
    public void setVgm(String vgm) 
    {
        this.vgm = vgm;
    }

    public String getVgm() 
    {
        return vgm;
    }
    public void setCntrState(String cntrState) 
    {
        this.cntrState = cntrState;
    }

    public String getCntrState() 
    {
        return cntrState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("cntrId", getCntrId())
            .append("cntrNo", getCntrNo())
            .append("sztp2", getSztp2())
            .append("fe", getFe())
            .append("ptnrCode", getPtnrCode())
            .append("block", getBlock())
            .append("bay", getBay())
            .append("roww", getRoww())
            .append("tier", getTier())
            .append("wgt", getWgt())
            .append("vslNm", getVslNm())
            .append("consignee", getConsignee())
            .append("bookingNo", getBookingNo())
            .append("blNo", getBlNo())
            .append("sealNo1", getSealNo1())
            .append("dispatchMode", getDispatchMode())
            .append("dispatchMode2", getDispatchMode2())
            .append("inDate", getInDate())
            .append("outDate", getOutDate())
            .append("vgm", getVgm())
            .append("cntrState", getCntrState())
            .append("remark", getRemark())
            .toString();
    }
}
