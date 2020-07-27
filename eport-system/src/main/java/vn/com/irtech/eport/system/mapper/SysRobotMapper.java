package vn.com.irtech.eport.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
     * @param id
     * @return robot
     */
    public SysRobot selectRobotById(Long id);
    
    /**
     * Get Robot by uuId
     * 
     * @param uuId
     * @return robot
     */
    public SysRobot selectRobotByUuId(String uuId);

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
     * @param id
     * @return result
     */
    public int deleteRobotById(Long id);

    /**
     * Delete robots by ids
     * 
     * @param ids
     * @return result
     */
    public int deleteRobotByIds(String[] ids);
    
    /**
	 * Check uuid robot unique
	 * 
	 * @param uuid
	 * @return result
	 */
	public SysRobot checkUuidRobotUnique(String uuId);
	
	/**
     * Update status robot by uuid
     * 
     * @param uuId
     * @param status
     * @return Result
     */
    public int updateRobotStatusByUuId(@Param("uuId") String uuId, @Param("status") String status);
    
    /**
     * Update robot by uuid
     * 
     * @param robot
     * @return Result
     */
    public int updateRobotByUuId(SysRobot robot);
    
    /**
     * Delete robot by uuid
     * 
     * @param uuid
     * @return result
     */
    public int deleteRobotByUuId(String uuId);
    
    /**
	 * Find first robot
	 * 
	 * @param robot
	 * @return robot
	 */
    public SysRobot findFirstRobot(SysRobot robot);

    /**
     * Select robot list online (include available and busy)
     * 
     * @param robot
     * @return list robot
     */
    public List<SysRobot> selectRobotListOnline(SysRobot robot);
}