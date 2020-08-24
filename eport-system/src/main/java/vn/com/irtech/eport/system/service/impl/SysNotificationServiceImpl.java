package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysNotificationMapper;
import vn.com.irtech.eport.system.domain.SysNotification;
import vn.com.irtech.eport.system.service.ISysNotificationService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * NotificationService Business Processing
 * 
 * @author Irtech
 * @date 2020-08-22
 */
@Service
public class SysNotificationServiceImpl implements ISysNotificationService 
{
    @Autowired
    private SysNotificationMapper sysNotificationMapper;

    /**
     * Get Notification
     * 
     * @param id NotificationID
     * @return Notification
     */
    @Override
    public SysNotification selectSysNotificationById(Long id)
    {
        return sysNotificationMapper.selectSysNotificationById(id);
    }

    /**
     * Get Notification List
     * 
     * @param sysNotification Notification
     * @return Notification
     */
    @Override
    public List<SysNotification> selectSysNotificationList(SysNotification sysNotification)
    {
        return sysNotificationMapper.selectSysNotificationList(sysNotification);
    }

    /**
     * Add Notification
     * 
     * @param sysNotification Notification
     * @return result
     */
    @Override
    public int insertSysNotification(SysNotification sysNotification)
    {
        sysNotification.setCreateTime(DateUtils.getNowDate());
        return sysNotificationMapper.insertSysNotification(sysNotification);
    }

    /**
     * Update Notification
     * 
     * @param sysNotification Notification
     * @return result
     */
    @Override
    public int updateSysNotification(SysNotification sysNotification)
    {
        sysNotification.setUpdateTime(DateUtils.getNowDate());
        return sysNotificationMapper.updateSysNotification(sysNotification);
    }

    /**
     * Delete Notification By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysNotificationByIds(String ids)
    {
        return sysNotificationMapper.deleteSysNotificationByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Notification
     * 
     * @param id NotificationID
     * @return result
     */
    @Override
    public int deleteSysNotificationById(Long id)
    {
        return sysNotificationMapper.deleteSysNotificationById(id);
    }
}
