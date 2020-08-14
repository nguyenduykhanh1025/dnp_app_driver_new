package vn.com.irtech.eport.logistic.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * Logistic Group Object logistic_group
 * 
 * @author irtech
 * @date 2020-05-26
 */
public class LogisticGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Tên doanh nghiệp */
    @Excel(name = "Tên doanh nghiệp")
    private String groupName;

    /** Địa chỉ liên hệ */
    @Excel(name = "Địa chỉ liên hệ")
    private String address;

    /** Mã số thuế doanh nghiệp */
    @Excel(name = "Mã số thuế doanh nghiệp")
    private String mst;

    /** Mã số thuế doanh nghiệp */
    @Excel(name = "Tên doanh nghiệp")
    private String companyName;

    /** Điện thoại cố định */
    @Excel(name = "Điện thoại cố định")
    private String phone;

    /** Điện thoại di động */
    @Excel(name = "Điện thoại di động")
    private String mobilePhone;
    
    /** Credit Card (1:có,0:không(default))) */
    @Excel(name = "Credit Card")
    private String creditFlag;

    /** Fax */
    @Excel(name = "Fax")
    private String fax;
    
    /** Delete Flag (0 nomal 1 deleted) */
    private String delFlag;

    /** Giấy đăng ký kinh doanh */
    @Excel(name = "Giấy đăng ký kinh doanh")
    private String businessRegistrationCertificate;

    /** Ngày cấp giấy đăng ký */
    @Excel(name = "Ngày cấp giấy đăng ký", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfIssueRegistration;

    /** Nơi cấp giấy đăng ký */
    @Excel(name = "Nơi cấp giấy đăng ký")
    private String placeOfIssueRegistration;

    /** Đại diện có thẩm quyền */
    @Excel(name = "Đại diện có thẩm quyền")
    private String authorizedRepresentative;

    /** Chức vụ */
    @Excel(name = "Chức vụ")
    private String representativePosition;

    /** Theo bản ủy quyền số */
    @Excel(name = "Theo bản ủy quyền số")
    private String followingAuthorizationFormNo;

    /** Ngày ký */
    @Excel(name = "Ngày ký", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signDate;

    /** của ... */
    @Excel(name = "của ...")
    private String owned;

    /** Số chứng minh thư */
    @Excel(name = "Số chứng minh thư")
    private String identifyCardNo;

    /** Ngày cấp chứng minh */
    @Excel(name = "Ngày cấp chứng minh", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dateOfIssueIdentify;

    /** Nơi cấp chứng minh */
    @Excel(name = "Nơi cấp chứng minh")
    private String placeOfIssueIdentify;

    /** email */
    @Excel(name = "email")
    private String email;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setMst(String mst) 
    {
        this.mst = mst;
    }

    public String getMst() 
    {
        return mst;
    }
    public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setMobilePhone(String mobilePhone) 
    {
        this.mobilePhone = mobilePhone;
    }

    public String getMobilePhone() 
    {
        return mobilePhone;
    }
    
    public void setCreditFlag(String creditFlag) 
    {
        this.creditFlag = creditFlag;
    }

    public String getCreditFlag() 
    {
        return creditFlag;
    }
    
    public void setFax(String fax) 
    {
        this.fax = fax;
    }

    public String getFax() 
    {
        return fax;
    }
    
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setBusinessRegistrationCertificate(String businessRegistrationCertificate) 
    {
        this.businessRegistrationCertificate = businessRegistrationCertificate;
    }

    public String getBusinessRegistrationCertificate() 
    {
        return businessRegistrationCertificate;
    }
    public void setDateOfIssueRegistration(Date dateOfIssueRegistration) 
    {
        this.dateOfIssueRegistration = dateOfIssueRegistration;
    }

    public Date getDateOfIssueRegistration() 
    {
        return dateOfIssueRegistration;
    }
    public void setPlaceOfIssueRegistration(String placeOfIssueRegistration) 
    {
        this.placeOfIssueRegistration = placeOfIssueRegistration;
    }

    public String getPlaceOfIssueRegistration() 
    {
        return placeOfIssueRegistration;
    }
    public void setAuthorizedRepresentative(String authorizedRepresentative) 
    {
        this.authorizedRepresentative = authorizedRepresentative;
    }

    public String getAuthorizedRepresentative() 
    {
        return authorizedRepresentative;
    }
    public void setRepresentativePosition(String representativePosition) 
    {
        this.representativePosition = representativePosition;
    }

    public String getRepresentativePosition() 
    {
        return representativePosition;
    }
    public void setFollowingAuthorizationFormNo(String followingAuthorizationFormNo) 
    {
        this.followingAuthorizationFormNo = followingAuthorizationFormNo;
    }

    public String getFollowingAuthorizationFormNo() 
    {
        return followingAuthorizationFormNo;
    }
    public void setSignDate(Date signDate) 
    {
        this.signDate = signDate;
    }

    public Date getSignDate() 
    {
        return signDate;
    }
    public void setOwned(String owned) 
    {
        this.owned = owned;
    }

    public String getOwned() 
    {
        return owned;
    }
    public void setIdentifyCardNo(String identifyCardNo) 
    {
        this.identifyCardNo = identifyCardNo;
    }

    public String getIdentifyCardNo() 
    {
        return identifyCardNo;
    }
    public void setDateOfIssueIdentify(Date dateOfIssueIdentify) 
    {
        this.dateOfIssueIdentify = dateOfIssueIdentify;
    }

    public Date getDateOfIssueIdentify() 
    {
        return dateOfIssueIdentify;
    }
    public void setPlaceOfIssueIdentify(String placeOfIssueIdentify) 
    {
        this.placeOfIssueIdentify = placeOfIssueIdentify;
    }

    public String getPlaceOfIssueIdentify() 
    {
        return placeOfIssueIdentify;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("mst", getMst())
            .append("companyName", getCompanyName())
            .append("address", getAddress())
            .append("email", getEmail())
            .append("phone", getPhone())
            .append("mobilePhone", getMobilePhone())
            .append("creditFlag", getCreditFlag())
            .append("fax", getFax())
            .append("delFlag", getDelFlag())
            .append("businessRegistrationCertificate", getBusinessRegistrationCertificate())
            .append("dateOfIssueRegistration", getDateOfIssueRegistration())
            .append("placeOfIssueRegistration", getPlaceOfIssueRegistration())
            .append("authorizedRepresentative", getAuthorizedRepresentative())
            .append("representativePosition", getRepresentativePosition())
            .append("followingAuthorizationFormNo", getFollowingAuthorizationFormNo())
            .append("signDate", getSignDate())
            .append("owned", getOwned())
            .append("identifyCardNo", getIdentifyCardNo())
            .append("dateOfIssueIdentify", getDateOfIssueIdentify())
            .append("placeOfIssueIdentify", getPlaceOfIssueIdentify())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
