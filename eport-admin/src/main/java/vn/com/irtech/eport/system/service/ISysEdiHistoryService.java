package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdiHistory;

/**
 * 【请填写功能名称】Service Interface
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
public interface ISysEdiHistoryService 
{
    /**
     * Get 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SysEdiHistory selectSysEdiHistoryById(Long ediId);

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return 【请填写功能名称】 List
     */
    public List<SysEdiHistory> selectSysEdiHistoryList(SysEdiHistory sysEdiHistory);

    /**
     * Add 【请填写功能名称】
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return result
     */
    public int insertSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Update 【请填写功能名称】
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return result
     */
    public int updateSysEdiHistory(SysEdiHistory sysEdiHistory);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysEdiHistoryByIds(String ids);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return result
     */
    public int deleteSysEdiHistoryById(Long ediId);
}
