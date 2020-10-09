package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;

/**
 * Logistic accountService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface ILogisticAccountService 
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
     * @return result
     */
    public int insertLogisticAccount(LogisticAccount logisticAccount);

    /**
     * Update Logistic account
     * 
     * @param logisticAccount Logistic account
     * @return result
     */
    public int updateLogisticAccount(LogisticAccount logisticAccount);

    /**
     * Batch Delete Logistic account
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteLogisticAccountByIds(String ids);

    /**
     * Delete Logistic account
     * 
     * @param id Logistic accountID
     * @return result
     */
    public int deleteLogisticAccountById(Long id);

    /**
     * Get account by email
     * @param username
     * @return
     */
	public LogisticAccount selectByEmail(String email);
	public LogisticAccount selectByUserName(String username);
	public String checkEmailUnique(String email);
	public String checkUserNameUnique(String userName);
	public int updateDelFlagLogisticAccountByIds(String ids);
	public int updateDelFlagLogisticAccountByGroupIds(String groupIds);
	
	/**
	 * Update password
	 * 
	 * @param user
	 * @return updated record number
	 */
	public int resetUserPwd(LogisticAccount user);
}
