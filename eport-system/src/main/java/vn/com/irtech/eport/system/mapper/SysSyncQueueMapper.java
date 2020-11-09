package vn.com.irtech.eport.system.mapper;

import java.util.List;

import vn.com.irtech.eport.system.domain.SysSyncQueue;

/**
 * Mapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-06
 */
public interface SysSyncQueueMapper 
{
    /**
	 * Get SysSyncQueue by id
	 * 
	 * @param id SYS_SYNC_QUEUE ID
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
	 * @return Result
	 */
    public int insertSysSyncQueue(SysSyncQueue sysSyncQueue);

    /**
	 * Update SysSyncQueue
	 * 
	 * @param sysSyncQueue SysSyncQueue
	 * @return Result
	 */
    public int updateSysSyncQueue(SysSyncQueue sysSyncQueue);

    /**
	 * Delete SysSyncQueue
	 * 
	 * @param id SysSyncQueue ID
	 * @return result
	 */
    public int deleteSysSyncQueueById(Long id);

    /**
	 * Batch Delete SysSyncQueue
	 * 
	 * @param ids IDs
	 * @return result
	 */
    public int deleteSysSyncQueueByIds(String[] ids);

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
