package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.CarrierAccount;

/**
 * Carrier AccountService Interface
 * 
 * @author irtech
 * @date 2020-04-04
 */
public interface ICarrierAccountService {
	/**
	 * Get Carrier Account
	 * 
	 * @param id Carrier AccountID
	 * @return Carrier Account
	 */
	public CarrierAccount selectCarrierAccountById(Long id);

	/**
	 * Get Carrier Account List
	 * 
	 * @param carrierAccount Carrier Account
	 * @return Carrier Account List
	 */
	public List<CarrierAccount> selectCarrierAccountList(CarrierAccount carrierAccount);

	/**
	 * Add Carrier Account
	 * 
	 * @param carrierAccount Carrier Account
	 * @return result
	 */
	public int insertCarrierAccount(CarrierAccount carrierAccount);

	/**
	 * Update Carrier Account
	 * 
	 * @param carrierAccount Carrier Account
	 * @return result
	 */
	public int updateCarrierAccount(CarrierAccount carrierAccount);

	/**
	 * Batch Delete Carrier Account
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
	public int deleteCarrierAccountByIds(String ids);

	/**
	 * Delete Carrier Account
	 * 
	 * @param id Carrier AccountID
	 * @return result
	 */
	public int deleteCarrierAccountById(Long id);

	/**
	 * Select Carrier by email for login
	 * 
	 * @param email
	 * @return
	 */
	public CarrierAccount selectByEmail(String email);

	/**
	 * Modify user password information
	 * 
	 * @param user
	 * @return
	 */
	public int resetUserPwd(CarrierAccount user);
}
