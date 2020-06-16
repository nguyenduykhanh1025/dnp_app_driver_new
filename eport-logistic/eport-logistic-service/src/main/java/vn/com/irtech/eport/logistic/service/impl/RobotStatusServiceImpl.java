package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.RobotStatusMapper;
import vn.com.irtech.eport.logistic.domain.RobotStatus;
import vn.com.irtech.eport.logistic.service.IRobotStatusService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Robot StatusService Business Processing
 * 
 * @author admin
 * @date 2020-06-16
 */
@Service
public class RobotStatusServiceImpl implements IRobotStatusService 
{
    @Autowired
    private RobotStatusMapper robotStatusMapper;

    /**
     * Get Robot Status
     * 
     * @param id Robot StatusID
     * @return Robot Status
     */
    @Override
    public RobotStatus selectRobotStatusById(Integer id)
    {
        return robotStatusMapper.selectRobotStatusById(id);
    }

    /**
     * Get Robot Status List
     * 
     * @param robotStatus Robot Status
     * @return Robot Status
     */
    @Override
    public List<RobotStatus> selectRobotStatusList(RobotStatus robotStatus)
    {
        return robotStatusMapper.selectRobotStatusList(robotStatus);
    }

    /**
     * Add Robot Status
     * 
     * @param robotStatus Robot Status
     * @return result
     */
    @Override
    public int insertRobotStatus(RobotStatus robotStatus)
    {
        return robotStatusMapper.insertRobotStatus(robotStatus);
    }

    /**
     * Update Robot Status
     * 
     * @param robotStatus Robot Status
     * @return result
     */
    @Override
    public int updateRobotStatus(RobotStatus robotStatus)
    {
        return robotStatusMapper.updateRobotStatus(robotStatus);
    }

    /**
     * Delete Robot Status By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteRobotStatusByIds(String ids)
    {
        return robotStatusMapper.deleteRobotStatusByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Robot Status
     * 
     * @param id Robot StatusID
     * @return result
     */
    @Override
    public int deleteRobotStatusById(Integer id)
    {
        return robotStatusMapper.deleteRobotStatusById(id);
    }
}
