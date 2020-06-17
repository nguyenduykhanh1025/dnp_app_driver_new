package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.RobotStatus;

/**
 * Robot StatusService Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface IRobotStatusService 
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
     * @return result
     */
    public int insertRobotStatus(RobotStatus robotStatus);

    /**
     * Update Robot Status
     * 
     * @param robotStatus Robot Status
     * @return result
     */
    public int updateRobotStatus(RobotStatus robotStatus);

    /**
     * Batch Delete Robot Status
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteRobotStatusByIds(String ids);

    /**
     * Delete Robot Status
     * 
     * @param id Robot StatusID
     * @return result
     */
    public int deleteRobotStatusById(Integer id);
}
