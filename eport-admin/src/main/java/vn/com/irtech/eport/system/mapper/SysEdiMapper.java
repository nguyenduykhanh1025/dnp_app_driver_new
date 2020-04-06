package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysEdi;

/**
 * 【请填写功能名称】Mapper Interface
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
public interface SysEdiMapper 
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
     * @return Result
     */
    public int insertSysEdi(SysEdi sysEdi);

    /**
     * Update 【请填写功能名称】
     * 
     * @param sysEdi 【请填写功能名称】
     * @return Result
     */
    public int updateSysEdi(SysEdi sysEdi);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return result
     */
    public int deleteSysEdiById(Long ediId);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ediIds IDs
     * @return result
     */
    public int deleteSysEdiByIds(String[] ediIds);
}
