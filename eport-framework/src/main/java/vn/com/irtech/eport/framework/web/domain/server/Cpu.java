package vn.com.irtech.eport.framework.web.domain.server;

import vn.com.irtech.eport.common.utils.Arith;

/**
 * CPU related information
 * 
 * @author admin
 */
public class Cpu
{
    /**
     * Number of cores
     */
    private int cpuNum;

    /**
     * CPU usage
     */
    private double total;

    /**
     * CPU system usage
     */
    private double sys;

    /**
     * CPU user usage
     */
    private double used;

    /**
     * CPU current waiting rate
     */
    private double wait;

    /**
     * CPU current idle rate
     */
    private double free;

    public int getCpuNum()
    {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum)
    {
        this.cpuNum = cpuNum;
    }

    public double getTotal()
    {
        return Arith.round(Arith.mul(total, 100), 2);
    }

    public void setTotal(double total)
    {
        this.total = total;
    }

    public double getSys()
    {
        return Arith.round(Arith.mul(sys / total, 100), 2);
    }

    public void setSys(double sys)
    {
        this.sys = sys;
    }

    public double getUsed()
    {
        return Arith.round(Arith.mul(used / total, 100), 2);
    }

    public void setUsed(double used)
    {
        this.used = used;
    }

    public double getWait()
    {
        return Arith.round(Arith.mul(wait / total, 100), 2);
    }

    public void setWait(double wait)
    {
        this.wait = wait;
    }

    public double getFree()
    {
        return Arith.round(Arith.mul(free / total, 100), 2);
    }

    public void setFree(double free)
    {
        this.free = free;
    }
}
