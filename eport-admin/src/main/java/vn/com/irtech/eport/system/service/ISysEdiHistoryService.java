package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdiHistory;

public interface ISysEdiHistoryService 
{
    /**
     * Get SysEdi history
     *
     * @param ediId 
     * @return SysEdi history
     */
    public SysEdiHistory selectSysEdiHistoryById(Long ediId);

    /**
     * Get SysEdi history list
     *
     * @param sysEdiHistory
     * @return SysEdi history list
     */
    public List<SysEdiHistory> selectSysEdiHistoryList(SysEdiHistory sysEdiHistory);

    /**
     * Add SysEdi history
     *
     * @param sysEdiHistory
     * @return result
     */
    public int insertSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Update SysEdi history
     *
     * @param sysEdiHistory
     * @return result
     */
    public int updateSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Batch Delete SysEdi history
     *
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysEdiHistoryByIds(String ids);

    /**
     * Delete SysEdi history
     *
     * @param ediId
     * @return result
     */
    public int deleteSysEdiHistoryById(Long ediId);
}
