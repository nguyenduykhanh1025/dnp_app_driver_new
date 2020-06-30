package vn.com.irtech.eport.web.controller.demo.domain;

import java.util.Date;
import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.annotation.Excel.Type;
import vn.com.irtech.eport.common.core.domain.BaseEntity;
import vn.com.irtech.eport.common.utils.DateUtils;

public class UserOperateModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private int userId;

    @Excel(name = "User ID")
    private String userCode;

    @Excel(name = "User Name")
    private String userName;

    @Excel(name = "User Gender", readConverterExp = "0=Male, 1=Female, 2=Unknown")
     private String userSex;

    @Excel(name = "User Phone")
    private String userPhone;

    @Excel(name = "User Mailbox")
    private String userEmail;

    @Excel(name = "User Balance")
    private double userBalance;

    @Excel(name = "User Status", readConverterExp = "0=normal, 1=disabled")
    private String status;

    @Excel(name = "Creation time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date createTime;

    public UserOperateModel()
    {

    }

    public UserOperateModel(int userId, String userCode, String userName, String userSex, String userPhone,
            String userEmail, double userBalance, String status)
    {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userBalance = userBalance;
        this.status = status;
        this.createTime = DateUtils.getNowDate();
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserSex()
    {
        return userSex;
    }

    public void setUserSex(String userSex)
    {
        this.userSex = userSex;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public double getUserBalance()
    {
        return userBalance;
    }

    public void setUserBalance(double userBalance)
    {
        this.userBalance = userBalance;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}