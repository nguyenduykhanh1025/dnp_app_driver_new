package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;

/**
 * Shipment DetailsMapper Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface ShipmentDetailMapper 
{
    /**
     * Get Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return Shipment Details
     */
    public ShipmentDetail selectShipmentDetailById(Long id);

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details List
     */
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail);

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return Result
     */
    public int insertShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return Result
     */
    public int updateShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    public int deleteShipmentDetailById(Long id);

    /**
     * Batch Delete Shipment Details
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentDetailByIds(String[] ids);

    public List<ShipmentDetail> selectShipmentDetailByIds(@Param("shipmentDetailIds") String[] ids, @Param("logisticGroupId") Long logisticGroupId);

    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno);

    public List<String> getBlListByDoStatus(String keyString);

    public List<String> getBlLists(String keyString);

    public List<String> getBlListByPaymentStatus(String keyString);

    public long countShipmentDetailList(ShipmentDetail shipmentDetail);
    
    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * 
     * @return Shipment Details List
     */
    public List<ShipmentWaitExec> selectListShipmentWaitExec();

    public List<ShipmentDetail> selectShipmentDetailByProcessIds(String[] processOrderIds);

    // public List<ShipmentDetail> selectSendEmptyShipmentDetailByListCont(@Param("conts") String[] conts, @Param("shipmentId") Long shipmentId);

    public List<ShipmentDetail> getShipmentDetailListForAssign(ShipmentDetail shipmentDetail);

    public List <ShipmentDetail> selectContainerStatusList(ShipmentDetail shipmentDetail);
    
    public List<ShipmentDetail> getShipmentDetailList(ShipmentDetail shipmentDetail);

    /**
     * Count number of legal container
     * 
     * @param shipmentDetails
     * @param logisticGroupId
     * @return Integer
     */
    public Integer countNumberOfLegalCont(@Param("shipmentDetails") List<ShipmentDetail> shipmentDetails,@Param("logisticGroupId") Long logisticGroupId);

    
    /***
     * getShipmentDetail for SendContFull and receiveContEmpty
     */
    public List<ShipmentDetail> getShipmentDetailListForSendFReceiveE(ShipmentDetail shipmentDetail);
    
    /***
     * get command List of batch
     */
    public List<Long> getCommandListInBatch(ShipmentDetail shipmentDetail);
}
