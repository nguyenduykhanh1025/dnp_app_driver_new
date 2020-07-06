package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.Notifications;

/**
 * 【请填写功能名称】Mapper Interface
 * 
 * @author ruoyi
 * @date 2020-07-06
 */
public interface NotificationsMapper 
{
    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public Notifications selectNotificationsById(Long id);

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param notifications 【请填写功能名称】
     * @return 【请填写功能名称】 List
     */
    public List<Notifications> selectNotificationsList(Notifications notifications);

    /**
     * Add 【请填写功能名称】
     * 
     * @param notifications 【请填写功能名称】
     * @return Result
     */
    public int insertNotifications(Notifications notifications);

    /**
     * Update 【请填写功能名称】
     * 
     * @param notifications 【请填写功能名称】
     * @return Result
     */
    public int updateNotifications(Notifications notifications);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    public int deleteNotificationsById(Long id);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteNotificationsByIds(String[] ids);
}
