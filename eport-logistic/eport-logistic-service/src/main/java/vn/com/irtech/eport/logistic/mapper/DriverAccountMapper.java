package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.dto.DriverTruckInfo;
import vn.com.irtech.eport.logistic.form.DriverInfo;

/**
 * Driver login infoMapper Interface
 * 
 * @author irtech
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

    public int checkDriverOfLogisticGroup(List<PickupAssign> pickupAssigns);

    /**
     * Get driver info
     * 
     * @param id
     * @return
     */
    public DriverInfo selectDriverAccountInfoById(Long id);

	/**
	 * Select driver with truck no info
	 * 
	 * @param ids
	 * @return List<DriverTruckInfo>
	 */
	public List<DriverTruckInfo> selectDriverWithTruckNoInfoByIds(String[] ids);

	/**
	 * Select assigned driver list
	 * 
	 * @param pickupAssign
	 * @return List<DriverAccount>
	 */
	public List<DriverAccount> selectAssignedDriverList(PickupAssign pickupAssign);

	/**
	 * Select driver account by ids
	 * 
	 * @param driverAccount
	 * @return List<DriverAccount>
	 */
	public List<DriverAccount> selectDriverListByIds(DriverAccount driverAccount);
}
