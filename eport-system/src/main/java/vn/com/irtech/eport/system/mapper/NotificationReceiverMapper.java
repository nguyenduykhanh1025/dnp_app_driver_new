package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.NotificationReceiver;

/**
 * 【请填写功能名称】Mapper Interface
 * 
 * @author ruoyi
 * @date 2020-07-06
 */
public interface NotificationReceiverMapper 
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
     * @return Result
     */
    public int insertNotificationReceiver(NotificationReceiver notificationReceiver);

    /**
     * Update 【请填写功能名称】
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return Result
     */
    public int updateNotificationReceiver(NotificationReceiver notificationReceiver);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    public int deleteNotificationReceiverById(Long id);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteNotificationReceiverByIds(String[] ids);
}
