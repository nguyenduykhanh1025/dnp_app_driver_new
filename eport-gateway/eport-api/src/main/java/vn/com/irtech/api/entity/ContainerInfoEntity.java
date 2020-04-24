package vn.com.irtech.api.entity;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Container Infomation Object container_info
 * 
 * @author Admin
 * @date 2020-04-16
 */
public class ContainerInfoEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long cntrId;
   
    /** Container Number */
    private String cntrNo;

    /** Size/type */
    private String sztp2;

    /** F/E */
    private String fe;

    /** Operator */
    private String ptnrCode;

    /** yard location - Block */
    private String yardPosition;

   /** AREA */
    private String area;

    /** Weight */
    private Long wgt;

    /** Vessel name */
    private String vslNm;

    /** Shipper/consignee */
    private String consignee;

    /** Booking Number */
    private String bookingNo;

    /** BL number */
    private String blNo;

    /** Seal Number */
    private String sealNo1;
    /** Seal Number */
    private String sealNo2;

    /** Gate mode for terminal in */
    private String gateMode;

    /** Gate mode for terminal out */
    private String dispatchMode2;

    /** Gate In Date */
    private String inDate;

    /** Gate Out Date */
    private String outDate;

    /** VGM */
    private String vgm;

    /** Container State  (S: Stacking, D:Delivered) */
    private String cntrState;

    /** remark */
    private String remark;

    /** Days */
    private int days;
    
    public int getDays()
    {
        return this.days;
    }
    
    public void setDays(int days)
    {
        this.days = days;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    /** SEARCH */
    private String toDate;

    private String fromDate;

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public String getFromDate() {
        return this.fromDate;
    }

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
    public void setYardPosition(String yardPosition) 
    {
        this.yardPosition = yardPosition;
    }

    public String getYardPosition() 
    {
        return yardPosition;
    }
    
    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
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
    public void setSealNo2(String sealNo2) 
    {
        this.sealNo2 = sealNo2;
    }

    public String getSealNo2() 
    {
        return sealNo2;
    }
    public void setGateMode(String gateMode) 
    {
        this.gateMode = gateMode;
    }

    public String getGateMode() 
    {
        return gateMode;
    }
    public void setDispatchMode2(String dispatchMode2) 
    {
        this.dispatchMode2 = dispatchMode2;
    }

    public String getDispatchMode2() 
    {
        return dispatchMode2;
    }
    public void setInDate(String inDate) 
    {
        this.inDate = inDate;
    }

    public String getInDate() 
    {
        return inDate;
    }
    public void setOutDate(String outDate) 
    {
        this.outDate = outDate;
    }

    public String getOutDate() 
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

    /** Class Mode */
    private String classMode;
    public void setClassMode(String classMode) 
    {
        this.classMode = classMode;
    }

    public String getClassMode() 
    {
        return classMode;
    }

    /** Trans Type In */
    private String transTypeIn;
    public void setTransTypeIn(String transTypeIn) 
    {
        this.transTypeIn = transTypeIn;
    }

    public String getTransTypeIn() 
    {
        return transTypeIn;
    }

    /** Trans Type Out */
    private String transTypeOut;
    public void setTransTypeOut(String transTypeOut) 
    {
        this.transTypeOut = transTypeOut;
    }

    public String getTransTypeOut() 
    {
        return transTypeOut;
    }


    /** Cargo Type */
    private String cargoType;
    public void setCargoType(String cargoType) 
    {
        this.cargoType = cargoType;
    }

    public String getCargoType() 
    {
        return cargoType;
    }

    
    /** Vessel Code */
    private String vesselCode;
    public void setVesselCode(String vesselCode) 
    {
        this.vesselCode = vesselCode;
    }

    public String getVesselCode() 
    {
        return vesselCode;
    }

    
    /** pol */
    private String pol;
    public void setPol(String pol) 
    {
        this.pol = pol;
    }

    public String getPol() 
    {
        return pol;
    }

   
    /** Pod */
    private String pod;
    public void setPod(String pod) 
    {
        this.pod = pod;
    }

    public String getPod() 
    {
        return pod;
    }
    
    /** PayerIn */
    private String payerIn;
    public void setPayerIn(String payerIn) 
    {
        this.payerIn = payerIn;
    }

    public String getPayerIn() 
    {
        return payerIn;
    }

    
    /** PayerOut */
    private String payerOut;
    public void setPayerOut(String payerOut) 
    {
        this.payerOut = payerOut;
    }

    public String getPayerOut() 
    {
        return payerOut;
    }

     /** Vessel Name */
     
     private String vesselName;
     public void setVesselName(String vesselName) 
     {
         this.vesselName = vesselName;
     }
 
     public String getVesselName() 
     {
         return vesselName;
     }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("cntrId", getCntrId())
            .append("cntrNo", getCntrNo())
            .append("sztp2", getSztp2())
            .append("fe", getFe())
            .append("ptnrCode", getPtnrCode())
            .append("yardPosition", getYardPosition())
            .append("wgt", getWgt())
            .append("vslNm", getVslNm())
            .append("consignee", getConsignee())
            .append("bookingNo", getBookingNo())
            .append("blNo", getBlNo())
            .append("sealNo1", getSealNo1())
            .append("gateMode", getGateMode())
            .append("dispatchMode2", getDispatchMode2())
            .append("inDate", getInDate())
            .append("outDate", getOutDate())
            .append("vgm", getVgm())
            .append("cntrState", getCntrState())
            .append("remark", getRemark())
            .append("classMode", getClassMode())
            .append("transTypeIn", getTransTypeIn())
            .append("transTypeOut", getTransTypeOut())
            .append("cargoType", getCargoType())
            .append("vesselCode", getVesselCode())
            .append("vesselName", getVesselName())
            .append("area", getArea())
            .append("pol", getPol())
            .append("pod", getPol())
            .append("payerIn", getPayerIn())
            .append("payerOut", getPayerOut())
            .toString();
    }
}
