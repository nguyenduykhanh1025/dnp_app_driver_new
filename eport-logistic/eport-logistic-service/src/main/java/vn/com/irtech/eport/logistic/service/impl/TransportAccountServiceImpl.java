package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.TransportAccountMapper;
import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.service.ITransportAccountService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Driver login infoService Business Processing
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
@Service
public class TransportAccountServiceImpl implements ITransportAccountService 
{
    @Autowired
    private TransportAccountMapper transportAccountMapper;

    /**
     * Get Driver login info
     * 
     * @param id Driver login infoID
     * @return Driver login info
     */
    @Override
    public TransportAccount selectTransportAccountById(Long id)
    {
        return transportAccountMapper.selectTransportAccountById(id);
    }

    /**
     * Get Driver login info List
     * 
     * @param transportAccount Driver login info
     * @return Driver login info
     */
    @Override
    public List<TransportAccount> selectTransportAccountList(TransportAccount transportAccount)
    {
        return transportAccountMapper.selectTransportAccountList(transportAccount);
    }

    /**
     * Add Driver login info
     * 
     * @param transportAccount Driver login info
     * @return result
     */
    @Override
    public int insertTransportAccount(TransportAccount transportAccount)
    {
        transportAccount.setCreateTime(DateUtils.getNowDate());
        return transportAccountMapper.insertTransportAccount(transportAccount);
    }

    /**
     * Update Driver login info
     * 
     * @param transportAccount Driver login info
     * @return result
     */
    @Override
    public int updateTransportAccount(TransportAccount transportAccount)
    {
        transportAccount.setUpdateTime(DateUtils.getNowDate());
        return transportAccountMapper.updateTransportAccount(transportAccount);
    }

    /**
     * Delete Driver login info By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteTransportAccountByIds(String ids)
    {
        return transportAccountMapper.deleteTransportAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Driver login info
     * 
     * @param id Driver login infoID
     * @return result
     */
    @Override
    public int deleteTransportAccountById(Long id)
    {
        return transportAccountMapper.deleteTransportAccountById(id);
    }
}
