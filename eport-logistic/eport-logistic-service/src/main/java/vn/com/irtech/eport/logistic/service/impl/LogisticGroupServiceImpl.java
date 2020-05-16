package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.LogisticGroupMapper;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Logistic GroupService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class LogisticGroupServiceImpl implements ILogisticGroupService 
{
    @Autowired
    private LogisticGroupMapper logisticGroupMapper;

    @Override
	public List<LogisticGroup> selectLogisticGroupListByName(LogisticGroup logisticGroup) {
		return logisticGroupMapper.selectLogisticGroupListByName(logisticGroup);
	}

	/**
     * Get Logistic Group
     * 
     * @param id Logistic GroupID
     * @return Logistic Group
     */
    @Override
    public LogisticGroup selectLogisticGroupById(Long id)
    {
        return logisticGroupMapper.selectLogisticGroupById(id);
    }

    /**
     * Get Logistic Group List
     * 
     * @param logisticGroup Logistic Group
     * @return Logistic Group
     */
    @Override
    public List<LogisticGroup> selectLogisticGroupList(LogisticGroup logisticGroup)
    {
        return logisticGroupMapper.selectLogisticGroupList(logisticGroup);
    }

    /**
     * Add Logistic Group
     * 
     * @param logisticGroup Logistic Group
     * @return result
     */
    @Override
    public int insertLogisticGroup(LogisticGroup logisticGroup)
    {
        logisticGroup.setCreateTime(DateUtils.getNowDate());
        return logisticGroupMapper.insertLogisticGroup(logisticGroup);
    }

    /**
     * Update Logistic Group
     * 
     * @param logisticGroup Logistic Group
     * @return result
     */
    @Override
    public int updateLogisticGroup(LogisticGroup logisticGroup)
    {
        logisticGroup.setUpdateTime(DateUtils.getNowDate());
        return logisticGroupMapper.updateLogisticGroup(logisticGroup);
    }

    /**
     * Delete Logistic Group By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteLogisticGroupByIds(String ids)
    {
        return logisticGroupMapper.deleteLogisticGroupByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Logistic Group
     * 
     * @param id Logistic GroupID
     * @return result
     */
    @Override
    public int deleteLogisticGroupById(Long id)
    {
        return logisticGroupMapper.deleteLogisticGroupById(id);
    }
}
