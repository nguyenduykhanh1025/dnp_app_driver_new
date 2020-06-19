package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.SysRobotMapper;
import vn.com.irtech.eport.system.domain.SysConfig;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Robot service
 * 
 * @author baohv
 * @date 2020-06-18
 */
@Service
public class SysRobotServiceImpl implements ISysRobotService {
	@Autowired
	private SysRobotMapper sysRobotMapper;

	/**
	 * Get Robot by id
	 * 
	 * @param robotId
	 * @return robot
	 */
	@Override
	public SysRobot selectRobotById(Long robotId) {
		return sysRobotMapper.selectRobotById(robotId);
	}

	/**
	 * Search list robot
	 * 
	 * @param robot
	 * @return list robot
	 */
	@Override
	public List<SysRobot> selectRobotList(SysRobot robot) {
		return sysRobotMapper.selectRobotList(robot);
	}

	/**
	 * Add Robot
	 * 
	 * @param robot
	 * @return Result
	 */
	@Override
	public int insertRobot(SysRobot robot) {
		robot.setCreateTime(DateUtils.getNowDate());
		return sysRobotMapper.insertRobot(robot);
	}

	/**
	 * Update robot
	 * 
	 * @param robot
	 * @return Result
	 */
	@Override
	public int updateRobot(SysRobot robot) {
		robot.setUpdateTime(DateUtils.getNowDate());
		return sysRobotMapper.updateRobot(robot);
	}

	/**
	 * Delete robot by id
	 * 
	 * @param robotId
	 * @return result
	 */
	@Override
	public int deleteRobotByIds(String ids) {
		return sysRobotMapper.deleteRobotByIds(Convert.toStrArray(ids));
	}

	/**
	 * Delete robots by ids
	 * 
	 * @param robotIds
	 * @return result
	 */
	@Override
	public int deleteRobotById(Long robotId) {
		return sysRobotMapper.deleteRobotById(robotId);
	}

	/**
	 * Check uuid robot unique
	 * 
	 * @param uuid
	 * @return result
	 */
	@Override
	public String checkUuidRobotUnique(String uuId) {
		SysRobot sysRobot = sysRobotMapper.checkUuidRobotUnique(uuId);
		if (sysRobot != null) {
			return UserConstants.CONFIG_KEY_NOT_UNIQUE;
		}
		return UserConstants.CONFIG_KEY_UNIQUE;
	}
}