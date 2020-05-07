package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;

/**
 * Logistic accountMapper Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface LogisticAccountMapper 
{
    /**
     * Get Logistic account
     * 
     * @param id Logistic accountID
     * @return Logistic account
     */
    public LogisticAccount selectLogisticAccountById(Long id);

    /**
     * Get Logistic account List
     * 
     * @param logisticAccount Logistic account
     * @return Logistic account List
     */
    public List<LogisticAccount> selectLogisticAccountList(LogisticAccount logisticAccount);

    /**
     * Add Logistic account
     * 
     * @param logisticAccount Logistic account
     * @return Result
     */
    public int insertLogisticAccount(LogisticAccount logisticAccount);

    /**
     * Update Logistic account
     * 
     * @param logisticAccount Logistic account
     * @return Result
     */
    public int updateLogisticAccount(LogisticAccount logisticAccount);

    /**
     * Delete Logistic account
     * 
     * @param id Logistic accountID
     * @return result
     */
    public int deleteLogisticAccountById(Long id);

    /**
     * Batch Delete Logistic account
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteLogisticAccountByIds(String[] ids);
}
