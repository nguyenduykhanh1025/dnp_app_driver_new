package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysEdiMapper;
import vn.com.irtech.eport.system.domain.SysEdi;
import vn.com.irtech.eport.system.service.ISysEdiService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * 【请填写功能名称】Service Business Processing
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
@Service
public class SysEdiServiceImpl implements ISysEdiService 
{
    @Autowired
    private SysEdiMapper sysEdiMapper;

    /**
     * Get 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysEdi selectSysEdiById(Long ediId)
    {
        return sysEdiMapper.selectSysEdiById(ediId);
    }

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param sysEdi 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysEdi> selectSysEdiList(SysEdi sysEdi)
    {
        return sysEdiMapper.selectSysEdiList(sysEdi);
    }

    /**
     * Add 【请填写功能名称】
     * 
     * @param sysEdi 【请填写功能名称】
     * @return result
     */
    @Override
    public int insertSysEdi(SysEdi sysEdi)
    {
        return sysEdiMapper.insertSysEdi(sysEdi);
    }

    /**
     * Update 【请填写功能名称】
     * 
     * @param sysEdi 【请填写功能名称】
     * @return result
     */
    @Override
    public int updateSysEdi(SysEdi sysEdi)
    {
        return sysEdiMapper.updateSysEdi(sysEdi);
    }

    /**
     * Delete 【请填写功能名称】 By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysEdiByIds(String ids)
    {
        return sysEdiMapper.deleteSysEdiByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return result
     */
    @Override
    public int deleteSysEdiById(Long ediId)
    {
        return sysEdiMapper.deleteSysEdiById(ediId);
    }
}
