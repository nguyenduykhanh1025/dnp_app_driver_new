package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysEdiMapper;
import vn.com.irtech.eport.system.domain.SysEdi;
import vn.com.irtech.eport.system.service.ISysEdiService;
import vn.com.irtech.eport.common.core.text.Convert;

@Service
public class SysEdiServiceImpl implements ISysEdiService 
{
    @Autowired
    private SysEdiMapper sysEdiMapper;

    /**
     * Get SysEdi
     *
     * @param ediId
     * @return SysEdi
     */
    @Override
    public SysEdi selectSysEdiById(Long ediId)
    {
        return sysEdiMapper.selectSysEdiById(ediId);
    }

    /**
     * Get SysEdi List
     *
     * @param sysEdi 
     * @return SysEdi list
     */
    @Override
    public List<SysEdi> selectSysEdiList(SysEdi sysEdi)
    {
        return sysEdiMapper.selectSysEdiList(sysEdi);
    }

    /**
     * Add SysEdi
     *
     * @param sysEdi 
     * @return result
     */
    @Override
    public int insertSysEdi(SysEdi sysEdi)
    {
        return sysEdiMapper.insertSysEdi(sysEdi);
    }

    /**
     * Update SysEdi
     *
     * @param sysEdi
     * @return result
     */
    @Override
    public int updateSysEdi(SysEdi sysEdi)
    {
        return sysEdiMapper.updateSysEdi(sysEdi);
    }

    /**
     * Delete SysEdi By ID
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
     * Delete SysEdi
     *
     * @param ediId 
     * @return result
     */
    @Override
    public int deleteSysEdiById(Long ediId)
    {
        return sysEdiMapper.deleteSysEdiById(ediId);
    }
}
