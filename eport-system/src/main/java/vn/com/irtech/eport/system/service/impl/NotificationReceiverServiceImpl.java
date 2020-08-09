package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.NotificationReceiverMapper;
import vn.com.irtech.eport.system.domain.NotificationReceiver;
import vn.com.irtech.eport.system.service.INotificationReceiverService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * 【请填写功能名称】Service Business Processing
 * 
 * @author irtech
 * @date 2020-07-06
 */
@Service
public class NotificationReceiverServiceImpl implements INotificationReceiverService 
{
    @Autowired
    private NotificationReceiverMapper notificationReceiverMapper;

    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public NotificationReceiver selectNotificationReceiverById(Long id)
    {
        return notificationReceiverMapper.selectNotificationReceiverById(id);
    }

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<NotificationReceiver> selectNotificationReceiverList(NotificationReceiver notificationReceiver)
    {
        return notificationReceiverMapper.selectNotificationReceiverList(notificationReceiver);
    }

    /**
     * Add 【请填写功能名称】
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return result
     */
    @Override
    public int insertNotificationReceiver(NotificationReceiver notificationReceiver)
    {
        notificationReceiver.setCreateTime(DateUtils.getNowDate());
        return notificationReceiverMapper.insertNotificationReceiver(notificationReceiver);
    }

    /**
     * Update 【请填写功能名称】
     * 
     * @param notificationReceiver 【请填写功能名称】
     * @return result
     */
    @Override
    public int updateNotificationReceiver(NotificationReceiver notificationReceiver)
    {
        notificationReceiver.setUpdateTime(DateUtils.getNowDate());
        return notificationReceiverMapper.updateNotificationReceiver(notificationReceiver);
    }

    /**
     * Delete 【请填写功能名称】 By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteNotificationReceiverByIds(String ids)
    {
        return notificationReceiverMapper.deleteNotificationReceiverByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    @Override
    public int deleteNotificationReceiverById(Long id)
    {
        return notificationReceiverMapper.deleteNotificationReceiverById(id);
    }
}
