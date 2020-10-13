package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;

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
    public int deleteShipmentDetailByIds(@Param("shipmentId") Long shipmentId, @Param("shipmentDetailIds") String[] shipmentDetailIds, @Param("logisticGroupId") Long logisticGroupId);

    public List<ShipmentDetail> selectShipmentDetailByIds(@Param("shipmentDetailIds") String[] ids, @Param("logisticGroupId") Long logisticGroupId);

    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno);

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
    
    /***
     * get shipmentDetail for Print
     */
    public List<ShipmentDetail> getShipmentDetailForPrint(ShipmentDetail shipmentDetail);

    /**
     * Select shipment detail for driver shipment assign
     * 
     * @param shipmentId
     * @param driverId
     * @return List<PickupAssignForm>
     */
    public List<PickupAssignForm> selectShipmentDetailForDriverShipmentAssign(@Param("shipmentId") Long shipmentId, @Param("driverId") Long driverId);
    
    /**
     * Select consignee tax code by shipment id
     * 
     * @param shipmentId
     * @return String
     */
    public String selectConsigneeTaxCodeByShipmentId(Long shipmentId);
    
    /**
	 * Get Shipment detail from house bill
	 * 
	 * @param houseBill
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> selectHouseBillForShipment(String houseBill);
	
	/**
	 * Get shipment detail from edi by blNo
	 * 
	 * @param blNo
	 * @return List<ShipmentDetail>
	 */
    public List<ShipmentDetail> selectEdoListByBlNo(String blNo);
    
    public List<ShipmentDetail> selectShipmentDetailListReport(ShipmentDetail shipmentDetail);
    
    /**
     * resetShipmentDetailProcessStatus is used for OM reset process status. Not use with another purpose
     */
    public int resetShipmentDetailProcessStatus(ShipmentDetail shipmentDetail);
    
    /**
     * Select shipment detail for driver send cont on app mobile
     * 
     * @param driverId
     * @param pickupAssignForm
     * @return list pickup assign form object
     */
    public List<PickupAssignForm> selectShipmentDetailForDriverSendCont(@Param("driverId") Long driverId, @Param("pickUp") PickupAssignForm pickupAssignForm, @Param("serviceType") Integer serviceType);
    
    /**
     * Update shipment detail by shipment detail id
     * 
     * @param shipmentDetailIds
     * @param shipmentDetail
     * @return int
     */
    public int updateShipmentDetailByIds(@Param("shipmentDetailIds") String[] shipmentDetailIds, @Param("shipmentDetail") ShipmentDetail shipmentDetail);

    /**
     * Reset custom status to null
     * @param shipmentId shipmentId
     */
	public void resetCustomStatus(@Param("shipmentId")Long shipmentId);
    
	/**
	 * Delete shipment detail by condition
	 * 
	 * @param shipmentDetail
	 * @return int
	 */
	public int deleteShipmentDetailByCondition(ShipmentDetail shipmentDetail);

	/**
	 * Update shipment detail by condition
	 * 
	 * @param shipmentDetail
	 * @return int
	 */
	public int updateShipmentDetailByCondition(ShipmentDetail shipmentDetail);
}
