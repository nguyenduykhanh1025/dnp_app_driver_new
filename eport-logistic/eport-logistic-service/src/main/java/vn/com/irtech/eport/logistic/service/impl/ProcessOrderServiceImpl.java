package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.mapper.ProcessOrderMapper;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;

/**
 * Process orderService Business Processing
 * 
 * @author HieuNT
 * @date 2020-06-23
 */
@Service
public class ProcessOrderServiceImpl implements IProcessOrderService {
	@Autowired
	private ProcessOrderMapper processOrderMapper;

	/**
	 * Get Process order
	 * 
	 * @param id Process orderID
	 * @return Process order
	 */
	@Override
	public ProcessOrder selectProcessOrderById(Long id) {
		return processOrderMapper.selectProcessOrderById(id);
	}

	/**
	 * Get Process order List
	 * 
	 * @param processOrder Process order
	 * @return Process order
	 */
	@Override
	public List<ProcessOrder> selectProcessOrderList(ProcessOrder processOrder) {
		return processOrderMapper.selectProcessOrderList(processOrder);
	}

	/**
	 * Add Process order
	 * 
	 * @param processOrder Process order
	 * @return result
	 */
	@Override
	public int insertProcessOrder(ProcessOrder processOrder) {
		processOrder.setCreateTime(DateUtils.getNowDate());
		return processOrderMapper.insertProcessOrder(processOrder);
	}

	/**
	 * Update Process order
	 * 
	 * @param processOrder Process order
	 * @return result
	 */
	@Override
	public int updateProcessOrder(ProcessOrder processOrder) {
		return processOrderMapper.updateProcessOrder(processOrder);
	}

	/**
	 * Delete Process order By ID
	 * 
	 * @param ids Entity ID
	 * @return result
	 */
	@Override
	public int deleteProcessOrderByIds(String ids) {
		return processOrderMapper.deleteProcessOrderByIds(Convert.toStrArray(ids));
	}

	/**
	 * Delete Process order
	 * 
	 * @param id Process orderID
	 * @return result
	 */
	@Override
	public int deleteProcessOrderById(Long id) {
		return processOrderMapper.deleteProcessOrderById(id);
	}

	@Override
	@Transactional
	public boolean insertProcessOrderList(List<ProcessOrder> processOrders) {
		for (ProcessOrder processOrder : processOrders) {
			processOrderMapper.insertProcessOrder(processOrder);
		}
		return true;
	}

	@Override
	public List<ProcessOrder> selectOrderListForOmSupport(ProcessOrder processOrder) {
		return processOrderMapper.selectOrderListForOmSupport(processOrder);
	}

	@Override
	public int updateDoingProcessOrder(String[] processOrderIds) {
		return processOrderMapper.updateDoingProcessOrder(processOrderIds);
	}

	@Override
	public int updateCancelingProcessOrder(String[] processOrderIds) {
		return processOrderMapper.updateCancelingProcessOrder(processOrderIds);
	}

	@Override
	public int countProcessOrderDoing(String[] processOrderIds) {
		return processOrderMapper.countProcessOrderDoing(processOrderIds);
	}

	@Override
	public List<ProcessOrder> selectProcessOrderListForOmManagement(ProcessOrder processOrder) {
		return processOrderMapper.selectProcessOrderListForOmManagement(processOrder);
	}

	@Override
	public int checkLogisticOwnedProcessOrder(@Param("logisticGroupId") Long logisticGroupId,
			@Param("processOrderIds") String[] processOrderIds) {
		return processOrderMapper.checkLogisticOwnedProcessOrder(logisticGroupId, processOrderIds);
	}

	@Override
	public Long getShipmentIdByProcessOrderId(Long id) {
		return processOrderMapper.getShipmentIdByProcessOrderId(id);
	}

	/**
	 * Get process order by robot uuid
	 * 
	 * @param uuid
	 * @return Process Order
	 */
	public List<ProcessOrder> getProcessingProcessOrderByUuid(String uuid) {
		return processOrderMapper.getProcessOrderByUuid(uuid);
	}

	/**
	 * Find process order for robot
	 * 
	 * @param serviceTypes
	 * @return Process Order
	 */
	public ProcessOrder findProcessOrderForRobot(String serviceTypes) {
		return processOrderMapper.findProcessOrderForRobot(Convert.toStrArray(serviceTypes));
	}

	/**
	 * Select orders by shipment id
	 * 
	 * @param shipmentId
	 * @return List<ProcessOrder>
	 */
	public List<ProcessOrder> selectOrdersByShipmentId(ProcessOrder processOrder) {
		return processOrderMapper.selectOrdersByShipmentId(processOrder);
	}

	public List<ProcessOrder> selectProcessOrderListWithLogisticName(ProcessOrder processOrder) {
		return processOrderMapper.selectProcessOrderListWithLogisticName(processOrder);
	}

	public List<String> selectProcessOrderOnlyLogisticName(ProcessOrder processOrder) {
		return processOrderMapper.selectProcessOrderOnlyLogisticName(processOrder);
	}
	
	/**
     * Get number of order error need for om support for each service type
     * 
     * @return Map<String, Long>
     */
	@Override
    public Map<String, Long> getSupportNumberReportForOm() {
    	return processOrderMapper.getSupportNumberReportForOm();
    }
	
	/**
     * Get number of order error need for cont reefer support for each service type
     * 
     * @return Map<String, Long>
     */
	@Override
    public Map<String, Long> getSupportNumberReportForContReefer() {
    	return processOrderMapper.getSupportNumberReportForContReefer();
    }
	
	/**
     * Select process order list by ids
     * 
     * @param processOrderIds
     * @return List<ProcessOrder>
     */
	@Override
    public List<ProcessOrder> selectProcessOrderListByIds(String processOrderIds) {
		return processOrderMapper.selectProcessOrderListByIds(Convert.toStrArray(processOrderIds));
	}
	
	 /**
     * Make process order cancel order pickup empty by shipment details with same order job
     * 
     * @param shipmentDetails
     * @return ProcessOrder
     */
	@Override
    public ProcessOrder makeOrderCancelOrderPickupEmpty(List<ShipmentDetail> shipmentDetails) {
		List<Long> shipmentDetailIds = new ArrayList<>();
		for (ShipmentDetail shipmentDetail : shipmentDetails) {
			shipmentDetailIds.add(shipmentDetail.getId());
		}
		ShipmentDetail shipmentDetailReference = shipmentDetails.get(0);
		ProcessOrder processOrder = new ProcessOrder();
		processOrder.setServiceType(EportConstants.SERVICE_CANCEL_PICKUP_EMPTY);
		processOrder.setShipmentId(shipmentDetailReference.getShipmentId());
		processOrder.setOrderNo(shipmentDetailReference.getOrderNo());
		return processOrder;
	}
}
