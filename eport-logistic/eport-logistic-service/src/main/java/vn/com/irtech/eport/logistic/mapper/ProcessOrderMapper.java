package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;

/**
 * Process orderMapper Interface
 * 
 * @author HieuNT
 * @date 2020-06-23
 */
public interface ProcessOrderMapper 
{
    /**
     * Get Process order
     * 
     * @param id Process orderID
     * @return Process order
     */
    public ProcessOrder selectProcessOrderById(Long id);

    /**
     * Get Process order List
     * 
     * @param processOrder Process order
     * @return Process order List
     */
    public List<ProcessOrder> selectProcessOrderList(ProcessOrder processOrder);

    /**
     * Add Process order
     * 
     * @param processOrder Process order
     * @return Result
     */
    public int insertProcessOrder(ProcessOrder processOrder);

    /**
     * Update Process order
     * 
     * @param processOrder Process order
     * @return Result
     */
    public int updateProcessOrder(ProcessOrder processOrder);

    /**
     * Delete Process order
     * 
     * @param id Process orderID
     * @return result
     */
    public int deleteProcessOrderById(Long id);

    /**
     * Batch Delete Process order
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteProcessOrderByIds(String[] ids);

    public List<ProcessOrder> selectOrderListForOmSupport(ProcessOrder processOrder);

    public int updateProcessOrderStatusForOm(String[] processOrderIds);
}
