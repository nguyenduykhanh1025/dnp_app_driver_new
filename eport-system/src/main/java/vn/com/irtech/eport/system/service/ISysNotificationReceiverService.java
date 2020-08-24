package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.dto.NotificationRes;

/**
 * NotificationService Interface
 * 
 * @author Irtech
 * @date 2020-08-22
 */
public interface ISysNotificationReceiverService 
{
    /**
     * Get Notification
     * 
     * @param id NotificationID
     * @return Notification
     */
    public SysNotificationReceiver selectSysNotificationReceiverById(Long id);

    /**
     * Get Notification List
     * 
     * @param sysNotificationReceiver Notification
     * @return Notification List
     */
    public List<SysNotificationReceiver> selectSysNotificationReceiverList(SysNotificationReceiver sysNotificationReceiver);

    /**
     * Add Notification
     * 
     * @param sysNotificationReceiver Notification
     * @return result
     */
    public int insertSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver);

    /**
     * Update Notification
     * 
     * @param sysNotificationReceiver Notification
     * @return result
     */
    public int updateSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver);

    /**
     * Batch Delete Notification
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysNotificationReceiverByIds(String ids);

    /**
     * Delete Notification
     * 
     * @param id NotificationID
     * @return result
     */
    public int deleteSysNotificationReceiverById(Long id);
    
    /**
     * Get notification list
     * 
     * @param notificationRes
     * @return	List<NotificationRes>
     */
    public List<NotificationRes> getNotificationList(SysNotificationReceiver sysNotificationReceiver);
}
