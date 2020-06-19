package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.QueueOrder;

/**
 * queue orderMapper Interface
 * 
 * @author admin
 * @date 2020-06-19
 */
public interface QueueOrderMapper 
{
    /**
     * Get queue order
     * 
     * @param id queue orderID
     * @return queue order
     */
    public QueueOrder selectQueueOrderById(Long id);

    /**
     * Get queue order List
     * 
     * @param queueOrder queue order
     * @return queue order List
     */
    public List<QueueOrder> selectQueueOrderList(QueueOrder queueOrder);

    /**
     * Add queue order
     * 
     * @param queueOrder queue order
     * @return Result
     */
    public int insertQueueOrder(QueueOrder queueOrder);

    /**
     * Update queue order
     * 
     * @param queueOrder queue order
     * @return Result
     */
    public int updateQueueOrder(QueueOrder queueOrder);

    /**
     * Delete queue order
     * 
     * @param id queue orderID
     * @return result
     */
    public int deleteQueueOrderById(Long id);

    /**
     * Batch Delete queue order
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteQueueOrderByIds(String[] ids);
}
