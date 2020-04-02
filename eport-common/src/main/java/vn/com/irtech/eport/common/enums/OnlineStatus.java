package vn.com.irtech.eport.common.enums;

/**
 * 用户会话
 * 
 * @author admin
 */
public enum OnlineStatus
{
    /** 用户状态 */
    on_line("Online"), off_line("Offline");

    private final String info;

    private OnlineStatus(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }
}
