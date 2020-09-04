package vn.com.irtech.eport.carrier.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Booking Detail Object booking_detail
 * 
 * @author IRTech
 * @date 2020-09-04
 */
public class BookingDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Carrier Group */
    @Excel(name = "Carrier Group")
    private Long carrierGroupId;

    /** Booking */
    @Excel(name = "Booking")
    private Long bookingId;

    /** Container Number */
    @Excel(name = "Container Number")
    private String containerNo;

    /** Size Type */
    @Excel(name = "Size Type")
    private String sztp;

    /** User Voy */
    @Excel(name = "User Voy")
    private String userVoy;

    /** POL */
    @Excel(name = "POL")
    private String pol;

    /** POD */
    @Excel(name = "POD")
    private String pod;

    /** Cargo Type */
    @Excel(name = "Cargo Type")
    private String cargoType;

    /** Yard Position */
    @Excel(name = "Yard Position")
    private String yardPosition;

    /** Release Date */
    @Excel(name = "Release Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releaseDate;

    /** Expired Date */
    @Excel(name = "Expired Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiredDate;

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
    public void setBookingId(Long bookingId) 
    {
        this.bookingId = bookingId;
    }

    public Long getBookingId() 
    {
        return bookingId;
    }
    public void setContainerNo(String containerNo) 
    {
        this.containerNo = containerNo;
    }

    public String getContainerNo() 
    {
        return containerNo;
    }
    public void setSztp(String sztp) 
    {
        this.sztp = sztp;
    }

    public String getSztp() 
    {
        return sztp;
    }
    public void setUserVoy(String userVoy) 
    {
        this.userVoy = userVoy;
    }

    public String getUserVoy() 
    {
        return userVoy;
    }
    public void setPol(String pol) 
    {
        this.pol = pol;
    }

    public String getPol() 
    {
        return pol;
    }
    public void setPod(String pod) 
    {
        this.pod = pod;
    }

    public String getPod() 
    {
        return pod;
    }
    public void setCargoType(String cargoType) 
    {
        this.cargoType = cargoType;
    }

    public String getCargoType() 
    {
        return cargoType;
    }
    public void setYardPosition(String yardPosition) 
    {
        this.yardPosition = yardPosition;
    }

    public String getYardPosition() 
    {
        return yardPosition;
    }
    public void setReleaseDate(Date releaseDate) 
    {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDate() 
    {
        return releaseDate;
    }
    public void setExpiredDate(Date expiredDate) 
    {
        this.expiredDate = expiredDate;
    }

    public Date getExpiredDate() 
    {
        return expiredDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carrierGroupId", getCarrierGroupId())
            .append("bookingId", getBookingId())
            .append("containerNo", getContainerNo())
            .append("sztp", getSztp())
            .append("userVoy", getUserVoy())
            .append("pol", getPol())
            .append("pod", getPod())
            .append("cargoType", getCargoType())
            .append("yardPosition", getYardPosition())
            .append("releaseDate", getReleaseDate())
            .append("expiredDate", getExpiredDate())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
