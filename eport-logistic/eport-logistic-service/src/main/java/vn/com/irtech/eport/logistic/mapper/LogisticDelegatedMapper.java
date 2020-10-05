package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.LogisticDelegated;

/**
 * DelegateMapper Interface
 * 
 * @author Irtech
 * @date 2020-08-14
 */
public interface LogisticDelegatedMapper 
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
     * @return Result
     */
    public int insertLogisticDelegated(LogisticDelegated logisticDelegated);

    /**
     * Update Delegate
     * 
     * @param logisticDelegated Delegate
     * @return Result
     */
    public int updateLogisticDelegated(LogisticDelegated logisticDelegated);

    /**
     * Delete Delegate
     * 
     * @param id DelegateID
     * @return result
     */
    public int deleteLogisticDelegatedById(Long id);

    /**
     * Batch Delete Delegate
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteLogisticDelegatedByIds(String[] ids);
    
    /**
     * Update Del Flag By Group Ids
     * 
     * @param ids
     * @return
     */
    public int updateDelFlgByGroupIds(String[] ids);

    public LogisticDelegated selectLogisticDelegatedByTaxDelegated(String taxDelegated);

    public List<LogisticDelegated> selectLogisticDelegatedListForCheck(LogisticDelegated logisticDelegated);
}
