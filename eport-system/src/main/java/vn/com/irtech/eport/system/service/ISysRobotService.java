package vn.com.irtech.eport.system.service;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysRobot;

/**
 * Robot service interface
 * 
 * @author baohv
 * @date 2020-06-18
 */
public interface ISysRobotService {
	/**
	 * Get Robot by id
	 * 
	 * @param robotId
	 * @return robot
	 */
	public SysRobot selectRobotById(Long robotId);

	/**
	 * Search list robot
	 * 
	 * @param robot
	 * @return list robot
	 */
	public List<SysRobot> selectRobotList(SysRobot robot);
	
	/**
	 * Find first robot
	 * 
	 * @param robot
	 * @return robot
	 */
	public SysRobot findFirstRobot(SysRobot robot);

	/**
	 * Add Robot
	 * 
	 * @param robot
	 * @return Result
	 */
	public int insertRobot(SysRobot robot);

	/**
	 * Update robot
	 * 
	 * @param robot
	 * @return Result
	 */
	public int updateRobot(SysRobot robot);

	/**
	 * Delete robot by id
	 * 
	 * @param robotId
	 * @return result
	 */
	public int deleteRobotByIds(String ids);

	/**
	 * Delete robots by id
	 * 
	 * @param robotIds
	 * @return result
	 */
	public int deleteRobotById(Long robotId);
	
	/**
	 * Check uuid robot unique
	 * 
	 * @param uuid
	 * @return result
	 */
	public String checkUuidRobotUnique(String uuId);
	
	/**
	 * Update robot status by uuid
	 * 
	 * @param uuId
	 * @param status
	 * @return result
	 */
	public int updateRobotStatusByUuId(String uuId, String status);
	
	/**
	 * Get Robot by uuid
	 * 
	 * @param uuid
	 * @return robot
	 */
	public SysRobot selectRobotByUuId(String uuid);
	
	/**
	 * Update robot by uuid
	 * 
	 * @param robot
	 * @return Result
	 */
	public int updateRobotByUuId(SysRobot robot);
	
	/**
	 * Delete robots by uuid
	 * 
	 * @param robotIds
	 * @return result
	 */
	public int deleteRobotByUuId(String uuId);

	/**
     * Select robot list online (include available and busy)
     * 
     * @param robot
     * @return list robot
     */
    public List<SysRobot> selectRobotListOnline(SysRobot robot);
}