package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.LogisticAccountMapper;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Logistic accountService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class LogisticAccountServiceImpl implements ILogisticAccountService 
{
    @Autowired
    private LogisticAccountMapper logisticAccountMapper;

    /**
     * Get Logistic account
     * 
     * @param id Logistic accountID
     * @return Logistic account
     */
    @Override
    public LogisticAccount selectLogisticAccountById(Long id)
    {
        return logisticAccountMapper.selectLogisticAccountById(id);
    }

    /**
     * Get Logistic account List
     * 
     * @param logisticAccount Logistic account
     * @return Logistic account
     */
    @Override
    public List<LogisticAccount> selectLogisticAccountList(LogisticAccount logisticAccount)
    {
        return logisticAccountMapper.selectLogisticAccountList(logisticAccount);
    }

    /**
     * Add Logistic account
     * 
     * @param logisticAccount Logistic account
     * @return result
     */
    @Override
    public int insertLogisticAccount(LogisticAccount logisticAccount)
    {
        logisticAccount.setCreateTime(DateUtils.getNowDate());
        return logisticAccountMapper.insertLogisticAccount(logisticAccount);
    }

    /**
     * Update Logistic account
     * 
     * @param logisticAccount Logistic account
     * @return result
     */
    @Override
    public int updateLogisticAccount(LogisticAccount logisticAccount)
    {
        logisticAccount.setUpdateTime(DateUtils.getNowDate());
        return logisticAccountMapper.updateLogisticAccount(logisticAccount);
    }

    /**
     * Delete Logistic account By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteLogisticAccountByIds(String ids)
    {
        return logisticAccountMapper.deleteLogisticAccountByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Logistic account
     * 
     * @param id Logistic accountID
     * @return result
     */
    @Override
    public int deleteLogisticAccountById(Long id)
    {
        return logisticAccountMapper.deleteLogisticAccountById(id);
    }

	@Override
	public LogisticAccount selectByEmail(String email) {
		return logisticAccountMapper.selectByEmail(email);
	}

	@Override
	public LogisticAccount selectByUserName(String username) {
		return logisticAccountMapper.selectByUserName(username);
	}

	@Override
	public String checkEmailUnique(String email) {
        int count = logisticAccountMapper.checkEmailUnique(email);
        if (count > 0)
        {
            return "1";
        }
        return "0";
	}

	@Override
	public String checkUserNameUnique(String userName) {
		int count = logisticAccountMapper.checkUserNameUnique(userName);
		if(count > 0) {
			return "1";
		}
		return "0";
	}

	@Override
	public int updateDelFlagLogisticAccountByIds(String ids) {
		return logisticAccountMapper.updateDelFlagLogisticAccountByIds(Convert.toStrArray(ids));
	}

	@Override
	public int updateDelFlagLogisticAccountByGroupIds(String groupIds) {
		return logisticAccountMapper.updateDelFlagLogisticAccountByGroupIds(Convert.toStrArray(groupIds));
	}

	@Override
	public int resetUserPwd(LogisticAccount user) {
		return logisticAccountMapper.updateLogisticAccount(user);
	}
}
