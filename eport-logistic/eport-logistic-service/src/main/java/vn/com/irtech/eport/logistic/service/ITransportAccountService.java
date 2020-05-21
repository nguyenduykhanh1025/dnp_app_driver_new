package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.TransportAccount;

/**
 * Driver login infoService Interface
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
public interface ITransportAccountService 
{
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public TransportAccount selectTransportAccountById(Long id);

    /**
     * Get Driver login info List
     * 
     * @param transportAccount Driver login info
     * @return Driver login info List
     */
    public List<TransportAccount> selectTransportAccountList(TransportAccount transportAccount);

    /**
     * Add Driver login info
     * 
     * @param transportAccount Driver login info
     * @return result
     */
    public int insertTransportAccount(TransportAccount transportAccount);

    /**
     * Update Driver login info
     * 
     * @param transportAccount Driver login info
     * @return result
     */
    public int updateTransportAccount(TransportAccount transportAccount);

    /**
     * Batch Delete Driver login info
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteTransportAccountByIds(String ids);

    /**
     * Delete Driver login info
     * 
     * @param id Driver login infoID
     * @return result
     */
    public int deleteTransportAccountById(Long id);
	public int checkPhoneUnique(String phoneNumber);
}
