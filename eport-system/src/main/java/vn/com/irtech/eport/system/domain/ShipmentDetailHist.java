package vn.com.irtech.eport.system.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Shipment Detail History
 * 
 * @author Trong Hieu
 * @date 2020-10-06
 */
public class ShipmentDetailHist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** shipment_detail_id */
    private Long shipmentDetailId;

    /** vsl_cd */
    private String vslCd;

    /** data_field */
    private String dataField;

    /** old_value */
    private String oldValue;

    /** new_value */
    private String newValue;

    /** user_name */
    private String userName;

    /** user_type */
    private String userType;

    /** voy_no */
    private String voyNo;

    /** shipment_id */
    private Long shipmentId;

    /** hist_type */
    private String histType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setShipmentDetailId(Long shipmentDetailId) 
    {
        this.shipmentDetailId = shipmentDetailId;
    }

    public Long getShipmentDetailId() 
    {
        return shipmentDetailId;
    }
    public void setVslCd(String vslCd) 
    {
        this.vslCd = vslCd;
    }

    public String getVslCd() 
    {
        return vslCd;
    }
    public void setDataField(String dataField) 
    {
        this.dataField = dataField;
    }

    public String getDataField() 
    {
        return dataField;
    }
    public void setOldValue(String oldValue) 
    {
        this.oldValue = oldValue;
    }

    public String getOldValue() 
    {
        return oldValue;
    }
    public void setNewValue(String newValue) 
    {
        this.newValue = newValue;
    }

    public String getNewValue() 
    {
        return newValue;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }
    public void setVoyNo(String voyNo) 
    {
        this.voyNo = voyNo;
    }

    public String getVoyNo() 
    {
        return voyNo;
    }
    public void setShipmentId(Long shipmentId) 
    {
        this.shipmentId = shipmentId;
    }

    public Long getShipmentId() 
    {
        return shipmentId;
    }
    public void setHistType(String histType) 
    {
        this.histType = histType;
    }

    public String getHistType() 
    {
        return histType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shipmentDetailId", getShipmentDetailId())
            .append("vslCd", getVslCd())
            .append("dataField", getDataField())
            .append("oldValue", getOldValue())
            .append("newValue", getNewValue())
            .append("userName", getUserName())
            .append("userType", getUserType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("voyNo", getVoyNo())
            .append("shipmentId", getShipmentId())
            .append("histType", getHistType())
            .toString();
    }
}
