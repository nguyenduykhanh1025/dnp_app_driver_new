package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.RobotStatus;

/**
 * Robot StatusMapper Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface RobotStatusMapper 
{
    /**
     * Get Robot Status
     * 
     * @param id Robot StatusID
     * @return Robot Status
     */
    public RobotStatus selectRobotStatusById(Integer id);

    /**
     * Get Robot Status List
     * 
     * @param robotStatus Robot Status
     * @return Robot Status List
     */
    public List<RobotStatus> selectRobotStatusList(RobotStatus robotStatus);

    /**
     * Add Robot Status
     * 
     * @param robotStatus Robot Status
     * @return Result
     */
    public int insertRobotStatus(RobotStatus robotStatus);

    /**
     * Update Robot Status
     * 
     * @param robotStatus Robot Status
     * @return Result
     */
    public int updateRobotStatus(RobotStatus robotStatus);

    /**
     * Delete Robot Status
     * 
     * @param id Robot StatusID
     * @return result
     */
    public int deleteRobotStatusById(Integer id);

    /**
     * Batch Delete Robot Status
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteRobotStatusByIds(String[] ids);
}
