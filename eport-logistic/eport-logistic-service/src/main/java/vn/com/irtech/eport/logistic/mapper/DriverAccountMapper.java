package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.DriverAccount;

/**
 * Driver login infoMapper Interface
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
public interface DriverAccountMapper 
{
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public DriverAccount selectDriverAccountById(Long id);
    
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public DriverAccount selectDriverAccountByMobileNumber(String mobileNumber);

    /**
     * Get Driver login info List
     * 
     * @param driverAccount Driver login info
     * @return Driver login info List
     */
    public List<DriverAccount> selectDriverAccountList(DriverAccount driverAccount);

    /**
     * Add Driver login info
     * 
     * @param driverAccount Driver login info
     * @return Result
     */
    public int insertDriverAccount(DriverAccount driverAccount);

    /**
     * Update Driver login info
     * 
     * @param driverAccount Driver login info
     * @return Result
     */
    public int updateDriverAccount(DriverAccount driverAccount);

    /**
     * Delete Driver login info
     * 
     * @param id Driver login infoID
     * @return result
     */
    public int deleteDriverAccountById(Long id);

    /**
     * Batch Delete Driver login info
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteDriverAccountByIds(String[] ids);

    public int checkPhoneUnique(String phoneNumber);
    
    public List<DriverAccount> getAssignedDrivers(Long[] ids);
}
