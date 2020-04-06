package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.CarrierAccount;

/**
 * Carrier AccountMapper Interface
 * 
 * @author irtech
 * @date 2020-04-04
 */
public interface CarrierAccountMapper 
{
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
     * @return Result
     */
    public int insertCarrierAccount(CarrierAccount carrierAccount);

    /**
     * Update Carrier Account
     * 
     * @param carrierAccount Carrier Account
     * @return Result
     */
    public int updateCarrierAccount(CarrierAccount carrierAccount);

    /**
     * Delete Carrier Account
     * 
     * @param id Carrier AccountID
     * @return result
     */
    public int deleteCarrierAccountById(Long id);

    /**
     * Batch Delete Carrier Account
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteCarrierAccountByIds(String[] ids);

    /**
     * Select Carrier by email
     * 
     * @param email
     * @return
     */
    public CarrierAccount selectByEmail(String email);
    
    /**
	 * Check email unique
	 * 
	 * @param email
	 * @return
	 */
	public int checkEmailUnique(String email);
}
