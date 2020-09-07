package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysNotificationReceiverMapper;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.dto.NotificationRes;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * NotificationService Business Processing
 * 
 * @author Irtech
 * @date 2020-08-22
 */
@Service
public class SysNotificationReceiverServiceImpl implements ISysNotificationReceiverService 
{
    @Autowired
    private SysNotificationReceiverMapper sysNotificationReceiverMapper;

    /**
     * Get Notification
     * 
     * @param id NotificationID
     * @return Notification
     */
    @Override
    public SysNotificationReceiver selectSysNotificationReceiverById(Long id)
    {
        return sysNotificationReceiverMapper.selectSysNotificationReceiverById(id);
    }

    /**
     * Get Notification List
     * 
     * @param sysNotificationReceiver Notification
     * @return Notification
     */
    @Override
    public List<SysNotificationReceiver> selectSysNotificationReceiverList(SysNotificationReceiver sysNotificationReceiver)
    {
        return sysNotificationReceiverMapper.selectSysNotificationReceiverList(sysNotificationReceiver);
    }

    /**
     * Add Notification
     * 
     * @param sysNotificationReceiver Notification
     * @return result
     */
    @Override
    public int insertSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver)
    {
        sysNotificationReceiver.setCreateTime(DateUtils.getNowDate());
        return sysNotificationReceiverMapper.insertSysNotificationReceiver(sysNotificationReceiver);
    }

    /**
     * Update Notification
     * 
     * @param sysNotificationReceiver Notification
     * @return result
     */
    @Override
    public int updateSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver)
    {
        sysNotificationReceiver.setUpdateTime(DateUtils.getNowDate());
        return sysNotificationReceiverMapper.updateSysNotificationReceiver(sysNotificationReceiver);
    }

    /**
     * Delete Notification By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteSysNotificationReceiverByIds(String ids)
    {
        return sysNotificationReceiverMapper.deleteSysNotificationReceiverByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Notification
     * 
     * @param id NotificationID
     * @return result
     */
    @Override
    public int deleteSysNotificationReceiverById(Long id)
    {
        return sysNotificationReceiverMapper.deleteSysNotificationReceiverById(id);
    }
    
    /**
     * Get notification list
     * 
     * @param notificationRes
     * @return	List<NotificationRes>
     */
    @Override
    public List<NotificationRes> getNotificationList(SysNotificationReceiver sysNotificationReceiver) {
    	return sysNotificationReceiverMapper.getNotificationList(sysNotificationReceiver);
    }
    
    /**
     * Get notification list not sent yet
     * 
     * @param sysNotificationReceiver
     * @return List<SysNotificationReceiver
     */
    @Override
    public List<SysNotificationReceiver> getNotificationListNotSentYet(SysNotificationReceiver sysNotificationReceiver, Integer notificationNumber) {
    	return sysNotificationReceiverMapper.getNotificationListNotSentYet(sysNotificationReceiver, notificationNumber);
    }
}
