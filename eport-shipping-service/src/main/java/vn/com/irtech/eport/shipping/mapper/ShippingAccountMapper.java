package vn.com.irtech.eport.shipping.mapper;

import java.util.List;
import vn.com.irtech.eport.shipping.domain.ShippingAccount;

/**
 * Shipping line accountMapper Interface
 * 
 * @author Irtech
 * @date 2020-04-03
 */
public interface ShippingAccountMapper 
{
    /**
     * Get Shipping line account
     * 
     * @param id Shipping line accountID
     * @return Shipping line account
     */
    public ShippingAccount selectShippingAccountById(Long id);

    /**
     * Get Shipping line account List
     * 
     * @param shippingAccount Shipping line account
     * @return Shipping line account List
     */
    public List<ShippingAccount> selectShippingAccountList(ShippingAccount shippingAccount);

    /**
     * Add Shipping line account
     * 
     * @param shippingAccount Shipping line account
     * @return Result
     */
    public int insertShippingAccount(ShippingAccount shippingAccount);

    /**
     * Update Shipping line account
     * 
     * @param shippingAccount Shipping line account
     * @return Result
     */
    public int updateShippingAccount(ShippingAccount shippingAccount);

    /**
     * Delete Shipping line account
     * 
     * @param id Shipping line accountID
     * @return result
     */
    public int deleteShippingAccountById(Long id);

    /**
     * Batch Delete Shipping line account
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShippingAccountByIds(String[] ids);
}
