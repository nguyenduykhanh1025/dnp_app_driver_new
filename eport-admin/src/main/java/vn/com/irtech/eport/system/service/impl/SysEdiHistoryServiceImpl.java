package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysEdiHistoryMapper;
import vn.com.irtech.eport.system.domain.SysEdiHistory;
import vn.com.irtech.eport.system.service.ISysEdiHistoryService;
import vn.com.irtech.eport.common.core.text.Convert;

@Service
public class SysEdiHistoryServiceImpl implements ISysEdiHistoryService 
{
    @Autowired
    private SysEdiHistoryMapper sysEdiHistoryMapper;

    /**
     * Get SysEdi history by id
     *
     * @param ediId 
     * @return SysEdi history
     */
    @Override
    public SysEdiHistory selectSysEdiHistoryById(Long ediId)
    {
        return sysEdiHistoryMapper.selectSysEdiHistoryById(ediId);
    }

    /**
     * Get SysEdi history List
     *
     * @param sysEdiHistory
     * @return SysEdi history list
     */
    @Override
    public List<SysEdiHistory> selectSysEdiHistoryList(SysEdiHistory sysEdiHistory)
    {
        return sysEdiHistoryMapper.selectSysEdiHistoryList(sysEdiHistory);
    }

    /**
     * Add SysEdi history
     *
     * @param sysEdiHistory
     * @return Result
     */
    @Override
    public int insertSysEdiHistory(SysEdiHistory sysEdiHistory)
    {
        return sysEdiHistoryMapper.insertSysEdiHistory(sysEdiHistory);
    }

    /**
     * Update SysEdi history
     *
     * @param sysEdiHistory 
     * @return result
     */
    @Override
    public int updateSysEdiHistory(SysEdiHistory sysEdiHistory)
    {
        sysEdiHistory.setUpdateTime(DateUtils.getNowDate());
        return sysEdiHistoryMapper.updateSysEdiHistory(sysEdiHistory);
    }

    /**
     * Delete SysEdi history By ID
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
     * Delete SysEdi history
     *
     * @param ediId 
     * @return result
     */
    @Override
    public int deleteSysEdiHistoryById(Long ediId)
    {
        return sysEdiHistoryMapper.deleteSysEdiHistoryById(ediId);
    }
}
