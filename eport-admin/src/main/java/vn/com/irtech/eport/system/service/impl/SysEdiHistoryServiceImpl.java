package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysEdiHistoryMapper;
import vn.com.irtech.eport.system.domain.SysEdiHistory;
import vn.com.irtech.eport.system.service.ISysEdiHistoryService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * 【请填写功能名称】Service Business Processing
 * 
 * @author ruoyi
 * @date 2020-04-03
 */
@Service
public class SysEdiHistoryServiceImpl implements ISysEdiHistoryService 
{
    @Autowired
    private SysEdiHistoryMapper sysEdiHistoryMapper;

    /**
     * Get 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysEdiHistory selectSysEdiHistoryById(Long ediId)
    {
        return sysEdiHistoryMapper.selectSysEdiHistoryById(ediId);
    }

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysEdiHistory> selectSysEdiHistoryList(SysEdiHistory sysEdiHistory)
    {
        return sysEdiHistoryMapper.selectSysEdiHistoryList(sysEdiHistory);
    }

    /**
     * Add 【请填写功能名称】
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return result
     */
    @Override
    public int insertSysEdiHistory(SysEdiHistory sysEdiHistory)
    {
        return sysEdiHistoryMapper.insertSysEdiHistory(sysEdiHistory);
    }

    /**
     * Update 【请填写功能名称】
     * 
     * @param sysEdiHistory 【请填写功能名称】
     * @return result
     */
    @Override
    public int updateSysEdiHistory(SysEdiHistory sysEdiHistory)
    {
        sysEdiHistory.setUpdateTime(DateUtils.getNowDate());
        return sysEdiHistoryMapper.updateSysEdiHistory(sysEdiHistory);
    }

    /**
     * Delete 【请填写功能名称】 By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysEdiHistoryByIds(String ids)
    {
        return sysEdiHistoryMapper.deleteSysEdiHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete 【请填写功能名称】
     * 
     * @param ediId 【请填写功能名称】ID
     * @return result
     */
    @Override
    public int deleteSysEdiHistoryById(Long ediId)
    {
        return sysEdiHistoryMapper.deleteSysEdiHistoryById(ediId);
    }
}
