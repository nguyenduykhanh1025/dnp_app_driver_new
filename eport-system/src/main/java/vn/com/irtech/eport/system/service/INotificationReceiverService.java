package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.NotificationReceiver;

/**
 * 【请填写功能名称】Service Interface
 * 
 * @author irtech
 * @date 2020-07-06
 */
public interface INotificationReceiverService 
{
    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public NotificationReceiver selectNotificationReceiverById(Long id);

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return 【请填写功能名称】 List
     */
    public List<NotificationReceiver> selectNotificationReceiverList(NotificationReceiver notificationReceiver);

    /**
     * Add 【请填写功能名称】
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return result
     */
    public int insertNotificationReceiver(NotificationReceiver notificationReceiver);

    /**
     * Update 【请填写功能名称】
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return result
     */
    public int updateNotificationReceiver(NotificationReceiver notificationReceiver);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteNotificationReceiverByIds(String ids);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    public int deleteNotificationReceiverById(Long id);
}
