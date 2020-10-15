package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.dto.DriverTruckInfo;
import vn.com.irtech.eport.logistic.form.DriverInfo;

/**
 * Driver login infoService Interface
 * 
 * @author irtech
 * @date 2020-05-19
 */
public interface IDriverAccountService 
{
    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    public DriverAccount selectDriverAccountById(Long id);

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
     * @return result
     */
    public int insertDriverAccount(DriverAccount driverAccount);

    /**
     * Update Driver login info
     * 
     * @param driverAccount Driver login info
     * @return result
     */
    public int updateDriverAccount(DriverAccount driverAccount);

    /**
     * Batch Delete Driver login info
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteDriverAccountByIds(String ids);

    /**
     * Delete Driver login info
     * 
     * @param id Driver login infoID
     * @return result
     */
    public int deleteDriverAccountById(Long id);

    public int checkPhoneUnique(String phoneNumber);
    
    public List<DriverAccount> getAssignedDrivers(Long[] ids);
    
    public int checkDriverOfLogisticGroup(List<PickupAssign> pickupAssigns);

    public DriverInfo selectDriverAccountInfoById(Long id);

	/**
	 * Select driver with truck no info
	 * 
	 * @param ids
	 * @return List<DriverTruckInfo>
	 */
	public List<DriverTruckInfo> selectDriverWithTruckNoInfo(String ids);
}
