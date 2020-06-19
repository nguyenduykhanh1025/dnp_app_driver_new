package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.QueueOrder;

/**
 * queue orderService Interface
 * 
 * @author admin
 * @date 2020-06-19
 */
public interface IQueueOrderService 
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
     * @return result
     */
    public int insertQueueOrder(QueueOrder queueOrder);

    /**
     * Update queue order
     * 
     * @param queueOrder queue order
     * @return result
     */
    public int updateQueueOrder(QueueOrder queueOrder);

    /**
     * Batch Delete queue order
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteQueueOrderByIds(String ids);

    /**
     * Delete queue order
     * 
     * @param id queue orderID
     * @return result
     */
    public int deleteQueueOrderById(Long id);
}
