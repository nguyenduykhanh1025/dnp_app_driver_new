package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdi;

public interface ISysEdiService 
{
    /**
     * Get SysEdi
     *
     * @param ediId 
     * @return SysEdi
     */
    public SysEdi selectSysEdiById(Long ediId);

    /**
     * Get SysEdi list
     *
     * @param sysEdi
     * @return SysEdi list
     */
    public List<SysEdi> selectSysEdiList(SysEdi sysEdi);

    /**
     * Add SysEdi
     *
     * @param sysEdi 
     * @return result
     */
    public int insertSysEdi(SysEdi sysEdi);

    /**
     * Update SysEdi
     *
     * @param sysEdi 
     * @return result
     */
    public int updateSysEdi(SysEdi sysEdi);

    /**
     * Batch Delete SysEdi
     *
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysEdiByIds(String ids);

    /**
     * Delete SysEdi
     *
     * @param ediId 
     * @return result
     */
    public int deleteSysEdiById(Long ediId);
}
