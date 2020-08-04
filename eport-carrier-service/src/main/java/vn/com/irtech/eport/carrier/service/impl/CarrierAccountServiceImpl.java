package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.mapper.CarrierAccountMapper;
import vn.com.irtech.eport.carrier.service.ICarrierAccountService;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;

/**
 * Carrier AccountService Business Processing
 * 
 * @author irtech
 * @date 2020-04-04
 */
@Service
public class CarrierAccountServiceImpl implements ICarrierAccountService 
{
    @Autowired
    private CarrierAccountMapper carrierAccountMapper;

    /**
     * Get Carrier Account
     * 
     * @param id Carrier AccountID
     * @return Carrier Account
     */
    @Override
    public CarrierAccount selectCarrierAccountById(Long id)
    {
        return carrierAccountMapper.selectCarrierAccountById(id);
    }

    /**
     * Get Carrier Account List
     * 
     * @param carrierAccount Carrier Account
     * @return Carrier Account
     */
    @Override
    public List<CarrierAccount> selectCarrierAccountList(CarrierAccount carrierAccount)
    {
        return carrierAccountMapper.selectCarrierAccountList(carrierAccount);
    }

    /**
     * Add Carrier Account
     * 
     * @param carrierAccount Carrier Account
     * @return result
     */
    @Override
    public int insertCarrierAccount(CarrierAccount carrierAccount)
    {
        carrierAccount.setCreateTime(DateUtils.getNowDate());
        return carrierAccountMapper.insertCarrierAccount(carrierAccount);
    }

    /**
     * Update Carrier Account
     * 
     * @param carrierAccount Carrier Account
     * @return result
     */
    @Override
    public int updateCarrierAccount(CarrierAccount carrierAccount)
    {
        carrierAccount.setUpdateTime(DateUtils.getNowDate());
        return carrierAccountMapper.updateCarrierAccount(carrierAccount);
    }

    /**
     * Delete Carrier Account By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteCarrierAccountByIds(String ids)
    {
        return carrierAccountMapper.deleteCarrierAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Carrier Account
     * 
     * @param id Carrier AccountID
     * @return result
     */
    @Override
    public int deleteCarrierAccountById(Long id)
    {
        return carrierAccountMapper.deleteCarrierAccountById(id);
    }

	@Override
	public CarrierAccount selectByEmail(String email) {
		return carrierAccountMapper.selectByEmail(email);
	}

	@Override
	public int resetUserPwd(CarrierAccount user) {
		return updateCarrierAccount(user);
    }
    
    /**
	 * Check email unique
	 * 
	 * @param email
	 * @return
	 */
    @Override
	public boolean checkEmailExist(String email) {
        return carrierAccountMapper.checkEmailUnique(email) > 0;
    }
}
