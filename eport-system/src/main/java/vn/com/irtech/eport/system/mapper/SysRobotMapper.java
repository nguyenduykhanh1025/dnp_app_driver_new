package vn.com.irtech.eport.system.mapper;

import java.util.List;
import vn.com.irtech.eport.system.domain.SysRobot;

/**
 * Robot mapper interface
 * 
 * @author baohv
 * @date 2020-06-18
 */
public interface SysRobotMapper 
{
    /**
     * Get Robot by id
     * 
     * @param robotId
     * @return robot
     */
    public SysRobot selectRobotById(Long robotId);

    /**
     * Search  list robot
     * 
     * @param robot
     * @return list robot
     */
    public List<SysRobot> selectRobotList(SysRobot robot);

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
    public int deleteRobotById(Long robotId);

    /**
     * Delete robots by ids
     * 
     * @param robotIds
     * @return result
     */
    public int deleteRobotByIds(String[] robotIds);
    
    /**
	 * Check uuid robot unique
	 * 
	 * @param uuid
	 * @return result
	 */
	public SysRobot checkUuidRobotUnique(String uuId);
}