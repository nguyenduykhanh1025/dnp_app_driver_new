package vn.com.irtech.eport.framework.web.domain.server;

import vn.com.irtech.eport.common.utils.Arith;

/**
 * Memory related information
 * 
 * @author admin
 */
public class Mem
{
    /**
     * Total memory
     */
    private double total;

    /**
     * Used memory
     */
    private double used;

    /**
     * Remaining memory
     */
    private double free;

    public double getTotal()
    {
        return Arith.div(total, (1024 * 1024 * 1024), 2);
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public double getUsed()
    {
        return Arith.div(used, (1024 * 1024 * 1024), 2);
    }

    public void setUsed(long used)
    {
        this.used = used;
    }

    public double getFree()
    {
        return Arith.div(free, (1024 * 1024 * 1024), 2);
    }

    public void setFree(long free)
    {
        this.free = free;
    }

    public double getUsage()
    {
        return Arith.mul(Arith.div(used, total, 4), 100);
    }
}
