package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.logistic.mapper.QueueOrderMapper;
import vn.com.irtech.eport.logistic.domain.QueueOrder;
import vn.com.irtech.eport.logistic.service.IQueueOrderService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * queue orderService Business Processing
 * 
 * @author admin
 * @date 2020-06-19
 */
@Service
public class QueueOrderServiceImpl implements IQueueOrderService 
{
    @Autowired
    private QueueOrderMapper queueOrderMapper;

    /**
     * Get queue order
     * 
     * @param id queue orderID
     * @return queue order
     */
    @Override
    public QueueOrder selectQueueOrderById(Long id)
    {
        return queueOrderMapper.selectQueueOrderById(id);
    }

    /**
     * Get queue order List
     * 
     * @param queueOrder queue order
     * @return queue order
     */
    @Override
    public List<QueueOrder> selectQueueOrderList(QueueOrder queueOrder)
    {
        return queueOrderMapper.selectQueueOrderList(queueOrder);
    }

    /**
     * Add queue order
     * 
     * @param queueOrder queue order
     * @return result
     */
    @Override
    public int insertQueueOrder(QueueOrder queueOrder)
    {
        return queueOrderMapper.insertQueueOrder(queueOrder);
    }

    /**
     * Update queue order
     * 
     * @param queueOrder queue order
     * @return result
     */
    @Override
    public int updateQueueOrder(QueueOrder queueOrder)
    {
        return queueOrderMapper.updateQueueOrder(queueOrder);
    }

    /**
     * Delete queue order By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteQueueOrderByIds(String ids)
    {
        return queueOrderMapper.deleteQueueOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete queue order
     * 
     * @param id queue orderID
     * @return result
     */
    @Override
    public int deleteQueueOrderById(Long id)
    {
        return queueOrderMapper.deleteQueueOrderById(id);
    }

    @Override
    @Transactional
    public boolean insertQueueOrderList(List<QueueOrder> queueOrders) {
        for (QueueOrder queueOrder : queueOrders) {
            queueOrderMapper.insertQueueOrder(queueOrder);
        }
        return true;
    }
}
