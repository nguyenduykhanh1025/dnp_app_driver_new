package vn.com.irtech.eport.logistic.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.ProcessOrder;

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

    public int updateDoingProcessOrder(String[] processOrderIds);

    public int updateCancelingProcessOrder(String[] processOrderIds);

    public int countProcessOrderDoing(String[] processOrderIds);

    public List<ProcessOrder> selectProcessOrderListForOmManagement(ProcessOrder processOrder);

    /**
     * Count process order in process order id array owned by logistic group id
     * 
     * @param logisticGroupId
     * @param processOrderIds
     * @return
     */
    public int checkLogisticOwnedProcessOrder(@Param("logisticGroupId") Long logisticGroupId, @Param("processOrderIds") String[] processOrderIds);

    /**
     * Get shipment id by process order id
     * 
     * @param id
     * @return
     */
    public Long getShipmentIdByProcessOrderId(Long id);
    
    /**
     * Get processing process order that handling by robot uuid
     * 
     * @param uuid
     * @return Process Order
     */
    public List<ProcessOrder> getProcessingProcessOrderByUuid(String uuid);

    /**
     * Find process order for robot
     * 
     * @param serviceTypes
     * @return Process Order
     */
    public ProcessOrder findProcessOrderForRobot(String serviceTypes);
    
    /**
     * Select orders by shipment id
     * 
     * @param shipmentId
     * @return List<ProcessOrder>
     */
    public List<ProcessOrder> selectOrdersByShipmentId(ProcessOrder processOrder);

    public List<ProcessOrder> selectProcessOrderListWithLogisticName(ProcessOrder processOrder);
    
    public List<String> selectProcessOrderOnlyLogisticName(ProcessOrder processOrder);
    
    /**
     * Get number of order error need for om support for each service type
     * 
     * @return Map<String, Long>
     */
    public Map<String, Long> getSupportNumberReportForOm();
}
