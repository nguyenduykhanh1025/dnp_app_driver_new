package vn.com.irtech.eport.logistic.service;

import java.util.Date;
import java.util.List;

import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.form.ShipmentForm;

/**
 * Shipment DetailsService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface IShipmentDetailService 
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
     * @return result
     */
    public int insertShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    public int updateShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Batch Delete Shipment Details
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentDetailByIds(String ids);

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    public int deleteShipmentDetailById(Long id);

    public List<ShipmentDetail> selectShipmentDetailByIds(String ids, Long logisticGroupId);

    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno);

    public List<String> getBlListByDoStatus(String keyString);

    public List<String> getBlLists(String keyString);

    public List<String> getBlListByPaymentStatus(String keyString);
    
    public long countShipmentDetailList(ShipmentDetail shipmentDetail);
    
    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * @return result
     */
    public List<ShipmentWaitExec> selectListShipmentWaitExec();

    public List<ShipmentDetail[][]> getContPosition(List<ShipmentDetail> coordinateOfList, List<ShipmentDetail> shipmentDetails);
    
    public List<ServiceSendFullRobotReq> calculateMovingCont(List<ShipmentDetail> coordinateOfList, List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails, Shipment shipment, Boolean isCredit);

    public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode, boolean creditFlag);

    public List<ServiceSendFullRobotReq> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode, boolean creditFlag);

    public ProcessOrder makeOrderSendCont(List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode, boolean creditFlag);
    
    public void updateProcessStatus(List<ShipmentDetail> shipmentDetail, String status, String invoiceNo, ProcessOrder processOrder);

//    public boolean checkCustomStatus(String userVoy,String cntrNo) throws IOException;

    public Shipment getGroupNameByTaxCode(String taxCode) throws Exception;

    public ProcessOrder getYearBeforeAfter(String vessel, String voyage);
    
    public List<String> checkContainerReserved(String containerNos);
    
    public List<String> getPODList();
    
    public List<String> getVesselCodeList();
    
    public List<String> getConsigneeList();
    
    /**
     * Get consignee list without tax code
     * 
     * @return List<String>
     */
    public List<String> getConsigneeListWithoutTaxCode();
    
    public List<String> getVoyageNoList(String vesselCode);
    
    public List<String> getOpeCodeList();
    
    public int getCountContByBlNo(String blNo);

    public List<ShipmentDetail> selectShipmentDetailByProcessIds (String processOrderIds);
    
//    public List<ShipmentDetail> getShipmentDetailsFromEDIByBlNo(String blNo);
    
    /**
     * Get shipment detail from house bill
     * 
     * @param houseBl
     * @return List<ShipmentDetail>
     */
//    public List<ShipmentDetail> getShipmentDetailFromHouseBill(String houseBl);

    public List<ShipmentDetail> getShipmentDetailListForAssign(ShipmentDetail shipmentDetail);

    // public List<ShipmentDetail> selectSendEmptyShipmentDetailByListCont(@Param("conts") String conts, @Param("shipmentId") Long shipmentId);

    public List <ShipmentDetail> selectContainerStatusList(ShipmentDetail shipmentDetail);
    
    public List<ShipmentDetail> getShipmentDetailList(ShipmentDetail shipmentDetail);

    /**
     * Count number of legal container
     * 
     * @param shipmentDetails
     * @param logisticGroupId
     * @return Integer
     */
    public Integer countNumberOfLegalCont(List<ShipmentDetail> shipmentDetails, Long logisticGroupId);
    
    public String getSSR(String sztp);
    
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
     * Get container with yard position
     * 
     * @param shipmentId
     * @return ShipmentDetail
     */
    public ShipmentDetail getContainerWithYardPosition(Long shipmentId);

    /**
     * Select shipment detail for driver shipment assign
     * 
     * @param shipmentId
     * @param driverId
     * @return List<PickupAssignForm>
     */
    public List<PickupAssignForm> selectShipmentDetailForDriverShipmentAssign(Long shipmentId, Long driverId);
    
    /**
     * Make change vessel order
     * 
     * @param shipmentDetails
     * @param vessel
     * @param voyage
     * @param groupId
     * @return ServiceSendFullRobotReq
     */
    public ServiceSendFullRobotReq makeChangeVesselOrder(List<ShipmentDetail>shipmentDetails, String[] vesselArr, Long groupId);

    /**
     * Make extension date order
     * 
     * @param shipmentDetails
     * @param expiredDem
     * @param groupId
     * @return ServiceSendFullRobotReq
     */
    public List<ServiceSendFullRobotReq> makeExtensionDateOrder(List<ShipmentDetail> shipmentDetails, Date expiredDem, Long groupId);
    
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
	public List<ShipmentDetail> getShipmentDetailFromHouseBill(String houseBill);
	
	/**
	 * Get shipment detail from edi by blNo
	 * 
	 * @param blNo
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> getShipmentDetailsFromEDIByBlNo(String blNo);
	
	/**
	 * Check and create booking if need
	 * 
	 * @param receiveEmptyReqs
	 * @return List<ProcessOrder>
	 */
    public List<ProcessOrder> createBookingIfNeed(List<ServiceSendFullRobotReq> receiveEmptyReqs);
    
    public List<ShipmentDetail> selectShipmentDetailListReport(ShipmentDetail shipmentDetail);
}
