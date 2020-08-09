package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.UserDevices;

/**
 * 【请填写功能名称】Mapper Interface
 * 
 * @author irtech
 * @date 2020-07-06
 */
public interface UserDevicesMapper 
{
    /**
     * Get 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UserDevices selectUserDevicesById(Long id);

    /**
     * Get 【请填写功能名称】 List
     * 
     * @param userDevices 【请填写功能名称】
     * @return 【请填写功能名称】 List
     */
    public List<UserDevices> selectUserDevicesList(UserDevices userDevices);

    /**
     * Add 【请填写功能名称】
     * 
     * @param userDevices 【请填写功能名称】
     * @return Result
     */
    public int insertUserDevices(UserDevices userDevices);

    /**
     * Update 【请填写功能名称】
     * 
     * @param userDevices 【请填写功能名称】
     * @return Result
     */
    public int updateUserDevices(UserDevices userDevices);

    /**
     * Delete 【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return result
     */
    public int deleteUserDevicesById(Long id);

    /**
     * Batch Delete 【请填写功能名称】
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteUserDevicesByIds(String[] ids);

    public List<UserDevices> selectDeviceTokenList(String[] userTypeList);

    public int deleteUserDevicesByUserToken(String token);
}
