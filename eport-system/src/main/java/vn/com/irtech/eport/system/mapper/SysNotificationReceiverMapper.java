package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;

/**
 * NotificationMapper Interface
 * 
 * @author Irtech
 * @date 2020-08-22
 */
public interface SysNotificationReceiverMapper 
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
     * @return Result
     */
    public int insertSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver);

    /**
     * Update Notification
     * 
     * @param sysNotificationReceiver Notification
     * @return Result
     */
    public int updateSysNotificationReceiver(SysNotificationReceiver sysNotificationReceiver);

    /**
     * Delete Notification
     * 
     * @param id NotificationID
     * @return result
     */
    public int deleteSysNotificationReceiverById(Long id);

    /**
     * Batch Delete Notification
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteSysNotificationReceiverByIds(String[] ids);
}
