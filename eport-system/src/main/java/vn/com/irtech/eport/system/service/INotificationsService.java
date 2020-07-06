package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.Notifications;

/**
 * 【请填写功能名称】Service Interface
 * 
 * @author ruoyi
 * @date 2020-07-06
 */
public interface INotificationsService 
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
     * @return result
     */
    public int insertNotifications(Notifications notifications);

    /**
     * Update 【请填写功能名称】
     * 
     * @param notifications 【请填写功能名称】
     * @return result
     */
    public int updateNotifications(Notifications notifications);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteNotificationsByIds(String ids);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    public int deleteNotificationsById(Long id);
}
