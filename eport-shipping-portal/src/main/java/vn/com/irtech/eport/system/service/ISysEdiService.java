package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdi;

/**
 * 【请填写功能名称】Service Interface
 * 
 * @author ruoyi
 * @date 2020-03-31
 */
public interface ISysEdiService 
{
    /**
     * Get 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SysEdi selectSysEdiById(Long ediId);

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param sysEdi 【请填写功能名称】
     * @return 【请填写功能名称】 List
     */
    public List<SysEdi> selectSysEdiList(SysEdi sysEdi);

    /**
     * Add 【请填写功能名称】
     * 
     * @param sysEdi 【请填写功能名称】
     * @return result
     */
    public int insertSysEdi(SysEdi sysEdi);

    /**
     * Update 【请填写功能名称】
     * 
     * @param sysEdi 【请填写功能名称】
     * @return result
     */
    public int updateSysEdi(SysEdi sysEdi);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysEdiByIds(String ids);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return result
     */
    public int deleteSysEdiById(Long ediId);
}
