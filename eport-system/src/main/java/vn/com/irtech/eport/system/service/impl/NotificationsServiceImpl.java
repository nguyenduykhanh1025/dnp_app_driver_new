package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.NotificationsMapper;
import vn.com.irtech.eport.system.domain.Notifications;
import vn.com.irtech.eport.system.service.INotificationsService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * 【请填写功能名称】Service Business Processing
 * 
 * @author irtech
 * @date 2020-07-06
 */
@Service
public class NotificationsServiceImpl implements INotificationsService 
{
    @Autowired
    private NotificationsMapper notificationsMapper;

    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public Notifications selectNotificationsById(Long id)
    {
        return notificationsMapper.selectNotificationsById(id);
    }

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param notifications 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Notifications> selectNotificationsList(Notifications notifications)
    {
        return notificationsMapper.selectNotificationsList(notifications);
    }

    /**
     * Add 【请填写功能名称】
     * 
     * @param notifications 【请填写功能名称】
     * @return result
     */
    @Override
    public int insertNotifications(Notifications notifications)
    {
        notifications.setCreateTime(DateUtils.getNowDate());
        return notificationsMapper.insertNotifications(notifications);
    }

    /**
     * Update 【请填写功能名称】
     * 
     * @param notifications 【请填写功能名称】
     * @return result
     */
    @Override
    public int updateNotifications(Notifications notifications)
    {
        notifications.setUpdateTime(DateUtils.getNowDate());
        return notificationsMapper.updateNotifications(notifications);
    }

    /**
     * Delete 【请填写功能名称】 By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteNotificationsByIds(String ids)
    {
        return notificationsMapper.deleteNotificationsByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    @Override
    public int deleteNotificationsById(Long id)
    {
        return notificationsMapper.deleteNotificationsById(id);
    }

    @Override
    public List<Notifications> selectNotificationsDetailList(Notifications notifications) {
        return notificationsMapper.selectNotificationsDetailList(notifications);
    }
}
