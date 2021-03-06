package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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

    public int updateDoingProcessOrder(String[] processOrderIds);

    public int updateCancelingProcessOrder(String[] processOrderIds);

    public int countProcessOrderDoing(String[] processOrderIds);

    public List<ProcessOrder> selectProcessOrderListForOmManagement(ProcessOrder processOrder);

    public int checkLogisticOwnedProcessOrder(@Param("logisticGroupId") Long logisticGroupId, @Param("processOrderIds") String[] processOrderIds);

    public Long getSumOfTotalBillList(String[] proccessOrderIds);

    /**
     * Get shipment id by process order id
     * 
     * @param id
     * @return
     */
    public Long getShipmentIdByProcessOrderId(Long id);

    /**
     * Get process order by robot uuid
     * 
     * @param uuid
     * @return Process Order
     */
    public List<ProcessOrder> getProcessOrderByUuid(String uuid);

    /**
     * Find process order for robot
     * 
     * @param serviceTypes
     * @return Process Order
     */
    public ProcessOrder findProcessOrderForRobot(String[] serviceTypes);
    
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
    
    /**
     * Get number of order error need for cont reefer support for each service type
     * 
     * @return Map<String, Long>
     */
    public Map<String, Long> getSupportNumberReportForContReefer();
    
    public Map<String, Long> getReportNumberForDutyLoadingCago(); 
    
    /**
     * Select process order list by ids
     * 
     * @param processOrderIds
     * @return List<ProcessOrder>
     */
    public List<ProcessOrder> selectProcessOrderListByIds(String[] processOrderIds);
}
