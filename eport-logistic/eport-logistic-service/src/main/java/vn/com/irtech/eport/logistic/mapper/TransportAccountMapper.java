package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.TransportAccount;

/**
 * Driver login infoMapper Interface
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
public interface TransportAccountMapper 
{
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public TransportAccount selectTransportAccountById(Long id);
    
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public TransportAccount selectTransportAccountByMobileNumber(String mobileNumber);

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
     * @return Result
     */
    public int insertTransportAccount(TransportAccount transportAccount);

    /**
     * Update Driver login info
     * 
     * @param transportAccount Driver login info
     * @return Result
     */
    public int updateTransportAccount(TransportAccount transportAccount);

    /**
     * Delete Driver login info
     * 
     * @param id Driver login infoID
     * @return result
     */
    public int deleteTransportAccountById(Long id);

    /**
     * Batch Delete Driver login info
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteTransportAccountByIds(String[] ids);
	public int checkPhoneUnique(String phoneNumber);
}
