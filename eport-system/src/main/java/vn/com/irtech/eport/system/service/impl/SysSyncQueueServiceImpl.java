package vn.com.irtech.eport.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.system.domain.SysSyncQueue;
import vn.com.irtech.eport.system.mapper.SysSyncQueueMapper;
import vn.com.irtech.eport.system.service.ISysSyncQueueService;

/**
 * SysSyncQueue Service Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-11-06
 */
@Service
public class SysSyncQueueServiceImpl implements ISysSyncQueueService 
{
    @Autowired
    private SysSyncQueueMapper sysSyncQueueMapper;

    /**
	 * Get SysSyncQueue
	 * 
	 * @param id SysSyncQueue ID
	 * @return SysSyncQueue
	 */
    @Override
    public SysSyncQueue selectSysSyncQueueById(Long id)
    {
        return sysSyncQueueMapper.selectSysSyncQueueById(id);
    }

    /**
	 * Get SysSyncQueue List
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return SysSyncQueue
	 */
    @Override
    public List<SysSyncQueue> selectSysSyncQueueList(SysSyncQueue sysSyncQueue)
    {
        return sysSyncQueueMapper.selectSysSyncQueueList(sysSyncQueue);
    }

    /**
	 * Add SysSyncQueue
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return result
	 */
    @Override
    public int insertSysSyncQueue(SysSyncQueue sysSyncQueue)
    {
        sysSyncQueue.setCreateTime(DateUtils.getNowDate());
        return sysSyncQueueMapper.insertSysSyncQueue(sysSyncQueue);
    }

    /**
	 * Update SysSyncQueue
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return result
	 */
    @Override
    public int updateSysSyncQueue(SysSyncQueue sysSyncQueue)
    {
        return sysSyncQueueMapper.updateSysSyncQueue(sysSyncQueue);
    }

    /**
	 * Delete SysSyncQueue By ID
	 * 
	 * @param ids Entity ID
	 * @return result
	 */
    @Override
    public int deleteSysSyncQueueByIds(String ids)
    {
        return sysSyncQueueMapper.deleteSysSyncQueueByIds(Convert.toStrArray(ids));
    }

    /**
	 * Delete SysSyncQueue
	 * 
	 * @param id SysSyncQueue ID
	 * @return result
	 */
    @Override
    public int deleteSysSyncQueueById(Long id)
    {
        return sysSyncQueueMapper.deleteSysSyncQueueById(id);
    }

	/**
	 * Select SysSyncQueue list with with limit retry time and limit create time
	 * 
	 * @param sysSyncQueue
	 * @return List<SysSyncQueue>
	 */
	@Override
	public List<SysSyncQueue> selectSysSyncQueueWithDelayTimeList(SysSyncQueue sysSyncQueue) {
		return sysSyncQueueMapper.selectSysSyncQueueWithDelayTimeList(sysSyncQueue);
	}

	/**
	 * Update SysSyncQueue with condition
	 * 
	 * @param sysSyncQueue
	 * @return int
	 */
	@Override
	public int updateSysSyncQueueWithCondition(SysSyncQueue sysSyncQueue) {
		return sysSyncQueueMapper.updateSysSyncQueueWithCondition(sysSyncQueue);
	}
}
