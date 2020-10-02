package vn.com.irtech.eport.logistic.domain;


import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Shipment Comment Object shipment_comment
 * 
 * @author IRTech
 * @date 2020-09-06
 */
public class ShipmentComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Shipment ID */
    @Excel(name = "Shipment ID")
    private Long shipmentId;

    /** Logistic ID */
    @Excel(name = "Logistic ID")
    private Long logisticGroupId;

    /** Commentor ID */
    @Excel(name = "Commentor ID")
    private Long userId;

    /** User Type:S: DNP StaftL: LogisticC:CarrierD:Driver */
    @Excel(name = "User Type:S: DNP StaftL: LogisticC:CarrierD:Driver")
    private String userType;

    /** Commentor Name */
    @Excel(name = "Commentor Name")
    private String userName;

    /** User Alias Name */
    @Excel(name = "User Alias Name")
    private String userAlias;

    /** Comment Time */
    @Excel(name = "Comment Time", width = 30, dateFormat = "yyyy-MM-dd")
    private Date commentTime;

    /** Topic */
    @Excel(name = "Topic")
    private String topic;

    /** Content */
    @Excel(name = "Content")
    private String content;
    
    /** Service type */
    private Integer serviceType;
    
    /** Seen flag */
    private Boolean seenFlg;
    
    /** Resolved flag */
    private Boolean resolvedFlg;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShipmentId(Long shipmentId) 
    {
        this.shipmentId = shipmentId;
    }

    public Long getShipmentId() 
    {
        return shipmentId;
    }
    public void setLogisticGroupId(Long logisticGroupId) 
    {
        this.logisticGroupId = logisticGroupId;
    }

    public Long getLogisticGroupId() 
    {
        return logisticGroupId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setUserAlias(String userAlias) 
    {
        this.userAlias = userAlias;
    }

    public String getUserAlias() 
    {
        return userAlias;
    }
    public void setCommentTime(Date commentTime) 
    {
        this.commentTime = commentTime;
    }

    public Date getCommentTime() 
    {
        return commentTime;
    }
    public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Boolean getSeenFlg() {
		return seenFlg;
	}

	public void setSeenFlg(Boolean seenFlg) {
		this.seenFlg = seenFlg;
	}

	public Boolean getResolvedFlg() {
		return resolvedFlg;
	}

	public void setResolvedFlg(Boolean resolvedFlg) {
		this.resolvedFlg = resolvedFlg;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentId", getShipmentId())
            .append("logisticGroupId", getLogisticGroupId())
            .append("userId", getUserId())
            .append("userType", getUserType())
            .append("userName", getUserName())
            .append("userAlias", getUserAlias())
            .append("commentTime", getCommentTime())
            .append("content", getContent())
            .append("serviceType", getServiceType())
            .append("seenFlg", getSeenFlg())
            .append("resolvedFlg", getResolvedFlg())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("params", getParams())
            .toString();
    }
}
