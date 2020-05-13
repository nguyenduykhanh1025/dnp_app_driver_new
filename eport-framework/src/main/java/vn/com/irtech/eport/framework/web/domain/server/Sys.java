package vn.com.irtech.eport.framework.web.domain.server;

/**
 * System related information
 * 
 * @author admin
 */
public class Sys
{
    /**
     * server nickname
     */
    private String computerName;

    /**
     * Server IP
     */
    private String computerIp;

    /**
     * Project path
     */
    private String userDir;

    /**
     * operating system
     */
    private String osName;

    /**
     * system structure
     */
    private String osArch;

    public String getComputerName()
    {
        return computerName;
    }

    public void setComputerName(String computerName)
    {
        this.computerName = computerName;
    }

    public String getComputerIp()
    {
        return computerIp;
    }

    public void setComputerIp(String computerIp)
    {
        this.computerIp = computerIp;
    }

    public String getUserDir()
    {
        return userDir;
    }

    public void setUserDir(String userDir)
    {
        this.userDir = userDir;
    }

    public String getOsName()
    {
        return osName;
    }

    public void setOsName(String osName)
    {
        this.osName = osName;
    }

    public String getOsArch()
    {
        return osArch;
    }

    public void setOsArch(String osArch)
    {
        this.osArch = osArch;
    }
}
