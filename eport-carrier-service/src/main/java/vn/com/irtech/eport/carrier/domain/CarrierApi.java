package vn.com.irtech.eport.carrier.domain;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * 
 * @author Trong Hieu
 * @date 2020-09-28
 */
public class CarrierApi extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** Group Id */
    @Excel(name = "Group Id")
    private Long groupId;

    /** Opr Code */
    @Excel(name = "Opr Code")
    private String oprCode;

    /** Api Public Key */
    @Excel(name = "Api Public Key")
    private String apiPublicKey;

    /** Api Private Key */
    @Excel(name = "Api Private Key")
    private String apiPrivateKey;

    /** Blocked Flg */
    @Excel(name = "Blocked Flag")
    private Long blockedFlg;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    public void setOprCode(String oprCode) 
    {
        this.oprCode = oprCode;
    }

    public String getOprCode() 
    {
        return oprCode;
    }
    public void setApiPublicKey(String apiPublicKey) 
    {
        this.apiPublicKey = apiPublicKey;
    }

    public String getApiPublicKey() 
    {
        return apiPublicKey;
    }
    public void setApiPrivateKey(String apiPrivateKey) 
    {
        this.apiPrivateKey = apiPrivateKey;
    }

    public String getApiPrivateKey() 
    {
        return apiPrivateKey;
    }
    public void setBlockedFlg(Long blockedFlg) 
    {
        this.blockedFlg = blockedFlg;
    }

    public Long getBlockedFlg() 
    {
        return blockedFlg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupId", getGroupId())
            .append("oprCode", getOprCode())
            .append("apiPublicKey", getApiPublicKey())
            .append("apiPrivateKey", getApiPrivateKey())
            .append("blockedFlg", getBlockedFlg())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
