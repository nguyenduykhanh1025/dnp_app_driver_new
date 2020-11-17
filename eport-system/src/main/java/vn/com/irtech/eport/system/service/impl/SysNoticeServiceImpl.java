package vn.com.irtech.eport.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.system.domain.SysNotice;
import vn.com.irtech.eport.system.mapper.SysNoticeMapper;
import vn.com.irtech.eport.system.service.ISysNoticeService;

/**
 * Notification form Service Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService 
{
    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    /**
     * Get Notification form
     * 
     * @param noticeId Notification formID
     * @return Notification form
     */
    @Override
    public SysNotice selectSysNoticeById(Long noticeId)
    {
        return sysNoticeMapper.selectSysNoticeById(noticeId);
    }

    /**
     * Get Notification form List
     * 
     * @param sysNotice Notification form
     * @return Notification form
     */
    @Override
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice)
    {
        return sysNoticeMapper.selectSysNoticeList(sysNotice);
    }

    /**
     * Add Notification form
     * 
     * @param sysNotice Notification form
     * @return result
     */
    @Override
    public int insertSysNotice(SysNotice sysNotice)
    {
        sysNotice.setCreateTime(DateUtils.getNowDate());
        return sysNoticeMapper.insertSysNotice(sysNotice);
    }

    /**
     * Update Notification form
     * 
     * @param sysNotice Notification form
     * @return result
     */
    @Override
    public int updateSysNotice(SysNotice sysNotice)
    {
        sysNotice.setUpdateTime(DateUtils.getNowDate());
        return sysNoticeMapper.updateSysNotice(sysNotice);
    }

    /**
     * Delete Notification form By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysNoticeByIds(String ids)
    {
        return sysNoticeMapper.deleteSysNoticeByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Notification form
     * 
     * @param noticeId Notification formID
     * @return result
     */
    @Override
    public int deleteSysNoticeById(Long noticeId)
    {
        return sysNoticeMapper.deleteSysNoticeById(noticeId);
    }
}
