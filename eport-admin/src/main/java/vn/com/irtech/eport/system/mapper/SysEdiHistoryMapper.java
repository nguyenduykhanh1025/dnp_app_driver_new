package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdiHistory;

public interface SysEdiHistoryMapper 
{
    /**
     * Get SysEdi history by ediId
     * 
     * @param ediId 
     * @return Result
     */
    public SysEdiHistory selectSysEdiHistoryById(Long ediId);

    /**
     * Get SysEdi history List
     * 
     * @param sysEdiHistory 
     * @return Result
     */
    public List<SysEdiHistory> selectSysEdiHistoryList(SysEdiHistory sysEdiHistory);

    /**
     * Add SysEdi history
     * 
     * @param sysEdiHistory 
     * @return Result
     */
    public int insertSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Update SysEdi history
     *
     * @param sysEdiHistory 
     * @return Result
     */
    public int updateSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Delete SysEdi
     *
     * @param ediId 
     * @return Result
     */
    public int deleteSysEdiHistoryById(Long ediId);

    /**
     * Batch Delete SysEdi history by ids
     * 
     * @param ediIds IDs
     * @return result
     */
    public int deleteSysEdiHistoryByIds(String[] ediIds);
}
