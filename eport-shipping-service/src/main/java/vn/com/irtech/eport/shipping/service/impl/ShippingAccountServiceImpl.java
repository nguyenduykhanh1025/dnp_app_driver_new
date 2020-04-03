package vn.com.irtech.eport.shipping.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.shipping.mapper.ShippingAccountMapper;
import vn.com.irtech.eport.shipping.domain.ShippingAccount;
import vn.com.irtech.eport.shipping.service.IShippingAccountService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Shipping line accountService Business Processing
 * 
 * @author Irtech
 * @date 2020-04-03
 */
@Service
public class ShippingAccountServiceImpl implements IShippingAccountService 
{
    @Autowired
    private ShippingAccountMapper shippingAccountMapper;

    /**
     * Get Shipping line account
     * 
     * @param id Shipping line accountID
     * @return Shipping line account
     */
    @Override
    public ShippingAccount selectShippingAccountById(Long id)
    {
        return shippingAccountMapper.selectShippingAccountById(id);
    }

    /**
     * Get Shipping line account List
     * 
     * @param shippingAccount Shipping line account
     * @return Shipping line account
     */
    @Override
    public List<ShippingAccount> selectShippingAccountList(ShippingAccount shippingAccount)
    {
        return shippingAccountMapper.selectShippingAccountList(shippingAccount);
    }

    /**
     * Add Shipping line account
     * 
     * @param shippingAccount Shipping line account
     * @return result
     */
    @Override
    public int insertShippingAccount(ShippingAccount shippingAccount)
    {
        shippingAccount.setCreateTime(DateUtils.getNowDate());
        return shippingAccountMapper.insertShippingAccount(shippingAccount);
    }

    /**
     * Update Shipping line account
     * 
     * @param shippingAccount Shipping line account
     * @return result
     */
    @Override
    public int updateShippingAccount(ShippingAccount shippingAccount)
    {
        shippingAccount.setUpdateTime(DateUtils.getNowDate());
        return shippingAccountMapper.updateShippingAccount(shippingAccount);
    }

    /**
     * Delete Shipping line account By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShippingAccountByIds(String ids)
    {
        return shippingAccountMapper.deleteShippingAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipping line account
     * 
     * @param id Shipping line accountID
     * @return result
     */
    @Override
    public int deleteShippingAccountById(Long id)
    {
        return shippingAccountMapper.deleteShippingAccountById(id);
    }
}
