package vn.com.irtech.eport.system.service;

import java.util.List;

import vn.com.irtech.eport.system.domain.SysSyncQueue;

/**
 * SysSyncQueueService Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-06
 */
public interface ISysSyncQueueService 
{
    /**
	 * Get SysSyncQueue
	 * 
	 * @param id SysSyncQueue ID
	 * @return SysSyncQueue
	 */
    public SysSyncQueue selectSysSyncQueueById(Long id);

    /**
	 * Get SysSyncQueue List
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return SysSyncQueue List
	 */
    public List<SysSyncQueue> selectSysSyncQueueList(SysSyncQueue sysSyncQueue);

    /**
	 * Add SysSyncQueue
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return result
	 */
    public int insertSysSyncQueue(SysSyncQueue sysSyncQueue);

    /**
	 * Update SysSyncQueue
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return result
	 */
    public int updateSysSyncQueue(SysSyncQueue sysSyncQueue);

    /**
	 * Batch Delete SysSyncQueue
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
    public int deleteSysSyncQueueByIds(String ids);

    /**
	 * Delete SysSyncQueue
	 * 
	 * @param id SysSyncQueue ID
	 * @return result
	 */
    public int deleteSysSyncQueueById(Long id);

	/**
	 * Select SysSyncQueue list with with limit retry time and limit create time
	 * 
	 * @param sysSyncQueue
	 * @return List<SysSyncQueue>
	 */
	public List<SysSyncQueue> selectSysSyncQueueWithDelayTimeList(SysSyncQueue sysSyncQueue);

	/**
	 * Update SysSyncQueue with condition
	 * 
	 * @param sysSyncQueue
	 * @return int
	 */
	public int updateSysSyncQueueWithCondition(SysSyncQueue sysSyncQueue);
}
