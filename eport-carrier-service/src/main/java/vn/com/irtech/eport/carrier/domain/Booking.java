package vn.com.irtech.eport.carrier.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Booking Object booking
 * 
 * @author Irtech
 * @date 2020-09-04
 */
public class Booking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Carrier Group */
    @Excel(name = "Carrier Group")
    private Long carrierGroupId;

    /** Carrier Account */
    @Excel(name = "Carrier Account")
    private Long carrierAccountId;

    /** OPR */
    @Excel(name = "OPR")
    private String opr;

    /** Booking No */
    @Excel(name = "Booking No")
    private String bookingNo;

    /** Book Quantity */
    @Excel(name = "Book Quantity")
    private Long bookQty;

    /** Consignee */
    @Excel(name = "Consignee")
    private String consignee;

    /** Consignee Tax Code */
    @Excel(name = "Consignee Tax Code")
    private String consigneeTaxcode;

    /** Status */
    @Excel(name = "Status")
    private String bookStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCarrierGroupId(Long carrierGroupId) 
    {
        this.carrierGroupId = carrierGroupId;
    }

    public Long getCarrierGroupId() 
    {
        return carrierGroupId;
    }
    public void setCarrierAccountId(Long carrierAccountId) 
    {
        this.carrierAccountId = carrierAccountId;
    }

    public Long getCarrierAccountId() 
    {
        return carrierAccountId;
    }
    public void setOpr(String opr) 
    {
        this.opr = opr;
    }

    public String getOpr() 
    {
        return opr;
    }
    public void setBookingNo(String bookingNo) 
    {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() 
    {
        return bookingNo;
    }
    public void setBookQty(Long bookQty) 
    {
        this.bookQty = bookQty;
    }

    public Long getBookQty() 
    {
        return bookQty;
    }
    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }
    public void setConsigneeTaxcode(String consigneeTaxcode) 
    {
        this.consigneeTaxcode = consigneeTaxcode;
    }

    public String getConsigneeTaxcode() 
    {
        return consigneeTaxcode;
    }
    public void setBookStatus(String bookStatus) 
    {
        this.bookStatus = bookStatus;
    }

    public String getBookStatus() 
    {
        return bookStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carrierGroupId", getCarrierGroupId())
            .append("carrierAccountId", getCarrierAccountId())
            .append("opr", getOpr())
            .append("bookingNo", getBookingNo())
            .append("bookQty", getBookQty())
            .append("consignee", getConsignee())
            .append("consigneeTaxcode", getConsigneeTaxcode())
            .append("bookStatus", getBookStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
