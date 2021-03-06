package vn.com.irtech.eport.logistic.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Shipment Object shipment
 *
 * @author admin
 * @date 2020-05-07
 */
public class Shipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    private Long id;

    /** null */
    @Excel(name = "Logistics Account Id")
    private Long logisticAccountId;

    /** null */
    @Excel(name = "Logistics Group Id")
    private Long logisticGroupId;

    /** Dich Vu */
    @Excel(name = "Dich Vu")
    private Integer serviceType;

    /** Bill No */
    @Excel(name = "B/L No")
    private String blNo;

    /** Booking No */
    @Excel(name = "Booking No")
    private String bookingNo;

    /** Mã hãng tàu */
    @Excel(name = "Mã hãng tàu")
    private String opeCode;

    /** So Luong Container */
    @Excel(name = "So Luong Container")
    private Long containerAmount;

    /** EDO Flag (1,0) */
    @Excel(name = "EDO Flag (1,0)")
    private String edoFlg;

    /** Specific Cont Flag (1, 0) */
    @Excel(name = "Specific Cont Flag (1, 0)")
    private Integer specificContFlg;

    /** Cont Supply Status (0, 1) */
    private Integer contSupplyStatus;

    /** Shipment Status */
    @Excel(name = "Shipment Status")
    private String status;

    /** Ghi chu */
    @Excel(name = "Ghi chu")
    private String remak;

    /** Tên công ty logistic theo ID */
    private String logisticName;

    private String vslNm;

    private String voyNo;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fromDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date toDate;

    private MultipartFile[] images;

    private List<String> attachedImageUrls;
    
    /** order number nhận cont house bill */
    private String orderNumber;
    
    private String sztp;

    public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	/** House bill no */
    private String houseBill;
    
    /** Số lương process bill: dùng để ẩn button thanh toán om nếu null */
    private Integer amountBill;
    
    /** Chủ hàng join với detail*/
    private String consignee;
    
    /** Thời gian quét hải quan bi loi som nhat*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date customsScanTime;
    
    /** send cont empty type 0 ha vao cang, 1 ha len tau */
    private String sendContEmptyType;
    
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setLogisticAccountId(Long logisticAccountId) {
        this.logisticAccountId = logisticAccountId;
    }

    public Long getLogisticAccountId() {
        return logisticAccountId;
    }

    public void setLogisticGroupId(Long logisticGroupId) {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() {
        return logisticGroupId;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setOpeCode(String opeCode) {
        this.opeCode = opeCode;
    }

    public String getOpeCode() {
        return opeCode;
    }

//    public void setTaxCode(String taxCode) {
//        this.taxCode = taxCode;
//    }
//
//    public String getTaxCode() {
//        return taxCode;
//    }
//
//    public void setGroupName(String groupName) {
//        this.groupName = groupName;
//    }
//
//    public String getGroupName() {
//        return groupName;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public void setContainerAmount(Long containerAmount) {
        this.containerAmount = containerAmount;
    }

    public Long getContainerAmount() {
        return containerAmount;
    }

    public void setEdoFlg(String edoFlg) {
        this.edoFlg = edoFlg;
    }

    public String getEdoFlg() {
        return edoFlg;
    }

    public void setSpecificContFlg(Integer specificContFlg) {
        this.specificContFlg = specificContFlg;
    }

    public Integer getSpecificContFlg() {
        return specificContFlg;
    }

    public void setContSupplyStatus(Integer contSupplyStatus) {
        this.contSupplyStatus = contSupplyStatus;
    }

    public Integer getContSupplyStatus() {
        return contSupplyStatus;
    }

//    public void setReferenceNo(String referenceNo) {
//        this.referenceNo = referenceNo;
//    }
//
//    public String getReferenceNo() {
//        return referenceNo;
//    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setLogisticName(String logisticName) {
        this.logisticName = logisticName;
    }

    public String getLogisticName() {
        return logisticName;
    }

    public void setVslNm(String vslNm) {
        this.vslNm = vslNm;
    }

    public String getVslNm() {
        return vslNm;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(final MultipartFile[] images) {
        this.images = images;
    }

    public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getHouseBill() {
		return houseBill;
	}

	public void setHouseBill(String houseBill) {
		this.houseBill = houseBill;
	}

	public Integer getAmountBill() {
		return amountBill;
	}

	public void setAmountBill(Integer amountBill) {
		this.amountBill = amountBill;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Date getCustomsScanTime() {
		return customsScanTime;
	}

	public void setCustomsScanTime(Date customsScanTime) {
		this.customsScanTime = customsScanTime;
	}
	
	public String getSendContEmptyType() {
		return sendContEmptyType;
	}

	public void setSendContEmptyType(String sendContEmptyType) {
		this.sendContEmptyType = sendContEmptyType;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("logisticAccountId", getLogisticAccountId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("serviceType", getServiceType())
            .append("blNo", getBlNo())
            .append("bookingNo", getBookingNo())
            .append("opeCode", getOpeCode())
//            .append("taxCode", getTaxCode())
//            .append("groupName", getGroupName())
//            .append("address", getAddress())
            .append("containerAmount", getContainerAmount())
            .append("edoFlg", getEdoFlg())
            .append("specificContFlg", getSpecificContFlg())
            .append("contSupplyStatus", getContSupplyStatus())
//            .append("referenceNo", getReferenceNo())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("logisticName", getLogisticName())
            .append("vslNm", getVslNm())
            .append("voyNo", getVoyNo())
            .append("fromDate", getFromDate())
            .append("toDate", getToDate())
            .append("orderNumber", getOrderNumber())
            .append("houseBill",getHouseBill())
            .append("amountBill", getAmountBill())
            .append("consignee", getConsignee())
            .append("customsScanTime", getCustomsScanTime())
            .append("params", getParams())
            .append("sendContEmptyType", getSendContEmptyType())
            .toString();
    }
}
