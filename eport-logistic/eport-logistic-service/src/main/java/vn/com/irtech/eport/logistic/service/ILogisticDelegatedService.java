package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.LogisticDelegated;

/**
 * DelegateService Interface
 * 
 * @author Irtech
 * @date 2020-08-14
 */
public interface ILogisticDelegatedService 
{
    /**
     * Get Delegate
     * 
     * @param id DelegateID
     * @return Delegate
     */
    public LogisticDelegated selectLogisticDelegatedById(Long id);

    /**
     * Get Delegate List
     * 
     * @param logisticDelegated Delegate
     * @return Delegate List
     */
    public List<LogisticDelegated> selectLogisticDelegatedList(LogisticDelegated logisticDelegated);

    /**
     * Add Delegate
     * 
     * @param logisticDelegated Delegate
     * @return result
     */
    public int insertLogisticDelegated(LogisticDelegated logisticDelegated);

    /**
     * Update Delegate
     * 
     * @param logisticDelegated Delegate
     * @return result
     */
    public int updateLogisticDelegated(LogisticDelegated logisticDelegated);

    /**
     * Batch Delete Delegate
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteLogisticDelegatedByIds(String ids);

    /**
     * Delete Delegate
     * 
     * @param id DelegateID
     * @return result
     */
    public int deleteLogisticDelegatedById(Long id);
}
