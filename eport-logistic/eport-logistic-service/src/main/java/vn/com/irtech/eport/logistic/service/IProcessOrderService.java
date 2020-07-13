package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.dto.ServiceRobotReq;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;

/**
 * Process orderService Interface
 * 
 * @author HieuNT
 * @date 2020-06-23
 */
public interface IProcessOrderService 
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
     * @return result
     */
    public int insertProcessOrder(ProcessOrder processOrder);

    /**
     * Update Process order
     * 
     * @param processOrder Process order
     * @return result
     */
    public int updateProcessOrder(ProcessOrder processOrder);

    /**
     * Batch Delete Process order
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteProcessOrderByIds(String ids);

    /**
     * Delete Process order
     * 
     * @param id Process orderID
     * @return result
     */
    public int deleteProcessOrderById(Long id);

    public boolean insertProcessOrderList(List<ProcessOrder> processOrders);

    public List<ProcessOrder> selectOrderListForOmSupport(ProcessOrder processOrder);

    public int updateDoingProcessOrder(String processOrderIds);

    public int updateCancelingProcessOrder(String processOrderIds);

}
