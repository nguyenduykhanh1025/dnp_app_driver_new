package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.LogisticDelegated;
import vn.com.irtech.eport.logistic.mapper.LogisticDelegatedMapper;
import vn.com.irtech.eport.logistic.service.ILogisticDelegatedService;

/**
 * DelegateService Business Processing
 * 
 * @author Irtech
 * @date 2020-08-14
 */
@Service
public class LogisticDelegatedServiceImpl implements ILogisticDelegatedService 
{
    @Autowired
    private LogisticDelegatedMapper logisticDelegatedMapper;

    /**
     * Get Delegate
     * 
     * @param id DelegateID
     * @return Delegate
     */
    @Override
    public LogisticDelegated selectLogisticDelegatedById(Long id)
    {
        return logisticDelegatedMapper.selectLogisticDelegatedById(id);
    }

    /**
     * Get Delegate List
     * 
     * @param logisticDelegated Delegate
     * @return Delegate
     */
    @Override
    public List<LogisticDelegated> selectLogisticDelegatedList(LogisticDelegated logisticDelegated)
    {
        return logisticDelegatedMapper.selectLogisticDelegatedList(logisticDelegated);
    }

    /**
     * Add Delegate
     * 
     * @param logisticDelegated Delegate
     * @return result
     */
    @Override
    public int insertLogisticDelegated(LogisticDelegated logisticDelegated)
    {
        logisticDelegated.setCreateTime(DateUtils.getNowDate());
        return logisticDelegatedMapper.insertLogisticDelegated(logisticDelegated);
    }

    /**
     * Update Delegate
     * 
     * @param logisticDelegated Delegate
     * @return result
     */
    @Override
    public int updateLogisticDelegated(LogisticDelegated logisticDelegated)
    {
        logisticDelegated.setUpdateTime(DateUtils.getNowDate());
        return logisticDelegatedMapper.updateLogisticDelegated(logisticDelegated);
    }

    /**
     * Delete Delegate By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteLogisticDelegatedByIds(String ids)
    {
        return logisticDelegatedMapper.deleteLogisticDelegatedByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Delegate
     * 
     * @param id DelegateID
     * @return result
     */
    @Override
    public int deleteLogisticDelegatedById(Long id)
    {
        return logisticDelegatedMapper.deleteLogisticDelegatedById(id);
    }
    
    /**
     * Update Del Flag By Group Ids
     * 
     * @param ids
     * @return
     */
    @Override
    public int updateDelFlgByGroupIds(String ids) {
    	return logisticDelegatedMapper.updateDelFlgByGroupIds(Convert.toStrArray(ids));
    }

    @Override
    public LogisticDelegated selectLogisticDelegatedByTaxDelegated(String taxDelegated)
    {
        return logisticDelegatedMapper.selectLogisticDelegatedByTaxDelegated(taxDelegated);
    }

    @Override
    public List<LogisticDelegated> selectLogisticDelegatedListForCheck(LogisticDelegated logisticDelegated)
    {
        return logisticDelegatedMapper.selectLogisticDelegatedListForCheck(logisticDelegated);
    }
}
