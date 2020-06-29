package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdi;

public interface SysEdiMapper 
{
    /**
     * Get SysEdi by id
     *
     * @param ediId
     * @return Result
     */
    public SysEdi selectSysEdiById(Long ediId);

    /**
     * Get SysEdi List
     *
     * @param sysEdi 
     * @return Result
     */
    public List<SysEdi> selectSysEdiList(SysEdi sysEdi);

    /**
     * Add SysEdi
     *
     * @param sysEdi
     * @return Result
     */
    public int insertSysEdi(SysEdi sysEdi);

    /**
     * Update SysEdi
     *
     * @param sysEdi 
     * @return Result
     */
    public int updateSysEdi(SysEdi sysEdi);

    /**
     * Delete SysEdi
     *
     * @param ediId
     * @return result
     */
    public int deleteSysEdiById(Long ediId);

    /**
     * Batch Delete SysEdi
     * 
     * @param ediIds IDs
     * @return Result
     */
    public int deleteSysEdiByIds(String[] ediIds);
}
