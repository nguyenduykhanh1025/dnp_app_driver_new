package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.UserDevicesMapper;
import vn.com.irtech.eport.system.domain.UserDevices;
import vn.com.irtech.eport.system.service.IUserDevicesService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * 【请填写功能名称】Service Business Processing
 * 
 * @author ruoyi
 * @date 2020-07-06
 */
@Service
public class UserDevicesServiceImpl implements IUserDevicesService 
{
    @Autowired
    private UserDevicesMapper userDevicesMapper;

    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public UserDevices selectUserDevicesById(Long id)
    {
        return userDevicesMapper.selectUserDevicesById(id);
    }

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param userDevices 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserDevices> selectUserDevicesList(UserDevices userDevices)
    {
        return userDevicesMapper.selectUserDevicesList(userDevices);
    }

    /**
     * Add 【请填写功能名称】
     * 
     * @param userDevices 【请填写功能名称】
     * @return result
     */
    @Override
    public int insertUserDevices(UserDevices userDevices)
    {
        userDevices.setCreateTime(DateUtils.getNowDate());
        return userDevicesMapper.insertUserDevices(userDevices);
    }

    /**
     * Update 【请填写功能名称】
     * 
     * @param userDevices 【请填写功能名称】
     * @return result
     */
    @Override
    public int updateUserDevices(UserDevices userDevices)
    {
        userDevices.setUpdateTime(DateUtils.getNowDate());
        return userDevicesMapper.updateUserDevices(userDevices);
    }

    /**
     * Delete 【请填写功能名称】 By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteUserDevicesByIds(String ids)
    {
        return userDevicesMapper.deleteUserDevicesByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    @Override
    public int deleteUserDevicesById(Long id)
    {
        return userDevicesMapper.deleteUserDevicesById(id);
    }
}
