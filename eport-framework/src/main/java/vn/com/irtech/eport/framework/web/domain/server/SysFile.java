package vn.com.irtech.eport.framework.web.domain.server;

/**
 * Information about system files
 * 
 * @author admin
 */
public class SysFile
{
    /**
     * Drive path
     */
    private String dirName;

    /**
     * Drive letter type
     */
    private String sysTypeName;

    /**
     * file type
     */
    private String typeName;

    /**
     * Total size
     */
    private String total;

    /**
     * Remaining size
     */
    private String free;

    /**
     * Used amount
     */
    private String used;

    /**
     * Resource usage
     */
    private double usage;

    public String getDirName()
    {
        return dirName;
    }

    public void setDirName(String dirName)
    {
        this.dirName = dirName;
    }

    public String getSysTypeName()
    {
        return sysTypeName;
    }

    public void setSysTypeName(String sysTypeName)
    {
        this.sysTypeName = sysTypeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }

    public String getFree()
    {
        return free;
    }

    public void setFree(String free)
    {
        this.free = free;
    }

    public String getUsed()
    {
        return used;
    }

    public void setUsed(String used)
    {
        this.used = used;
    }

    public double getUsage()
    {
        return usage;
    }

    public void setUsage(double usage)
    {
        this.usage = usage;
    }
}
