package vn.com.irtech.eport.logistic.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

/**
 * Shipment DetailsService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface IShipmentDetailService {
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
	public int deleteShipmentDetailByIds(Long shipmentId, String shipmentDetailIds, Long logisticGroupId);

	/**
	 * Delete Shipment Details
	 * 
	 * @param id Shipment DetailsID
	 * @return result
	 */
	public int deleteShipmentDetailById(Long id);

	/**
	 * Select shipment details list by list of ID for logistic id
	 * 
	 * @param ids
	 * @param logisticGroupId
	 * @return
	 */
	public List<ShipmentDetail> selectShipmentDetailByIds(String ids, Long logisticGroupId);

	public long countShipmentDetailList(ShipmentDetail shipmentDetail);

	/**
	 * Select list shipment detail wait robot execute or robot can't be execute,
	 * group by shipment id
	 * 
	 * @return result
	 */
	public List<ShipmentWaitExec> selectListShipmentWaitExec();

	public List<ShipmentDetail[][]> getContPosition(String blNo, List<ShipmentDetail> shipmentDetails);

	public List<ServiceSendFullRobotReq> calculateMovingCont(List<ContainerInfoDto> coordinateOfList,
			List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails, Shipment shipment,
			Boolean isCredit);

	public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails,
			Shipment shipment, String taxCode, boolean creditFlag);

	public List<ServiceSendFullRobotReq> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails,
			Shipment shipment, String taxCode, boolean creditFlag);

	public ProcessOrder makeOrderSendCont(List<ShipmentDetail> shipmentDetails, Shipment shipment, String taxCode,
			boolean creditFlag);

	public void updateProcessStatus(List<ShipmentDetail> shipmentDetail, String status, String invoiceNo,
			ProcessOrder processOrder);

	public ProcessOrder getYearBeforeAfter(String vessel, String voyage);

	/**
	 * Get container list was reserved on Catos. Can not process booking again.
	 * 
	 * @param containerNos
	 * @return
	 */
	public List<String> checkContainerReserved(String containerNos);

	/**
	 * Get vessel code list from catos
	 * 
	 * @return
	 */
	public List<String> getVesselCodeList();

	/**
	 * Get consignee list from catos
	 * 
	 * @return
	 */
	public List<String> getConsigneeList();

	/**
	 * Get consignee list without tax code
	 * 
	 * @return List<String>
	 */
	public List<String> getConsigneeListWithoutTaxCode();

	/**
	 * Get voyage list by vessel from catos
	 * 
	 * @param vesselCode
	 * @return
	 */
	public List<String> getVoyageNoList(String vesselCode);

	/**
	 * Get OPE code list from Catos
	 * 
	 * @return
	 */
	public List<String> getOpeCodeList();

	/**
	 * Get number of container by B/L No
	 * 
	 * @param blNo
	 * @return
	 */
	public int getCountContByBlNo(String blNo);

	/**
	 * Get list of shipment details that processed by user.
	 * 
	 * @param processOrderIds
	 * @return
	 */
	public List<ShipmentDetail> selectShipmentDetailByProcessIds(String processOrderIds);

	/**
	 * Get list of shipment detail to assign truck
	 * 
	 * @param shipmentDetail
	 * @return
	 */
	public List<ShipmentDetail> getShipmentDetailListForAssign(ShipmentDetail shipmentDetail);

	public List<ShipmentDetail> selectContainerStatusList(ShipmentDetail shipmentDetail);

	public List<ShipmentDetail> getShipmentDetailList(ShipmentDetail shipmentDetail);

	/**
	 * Count number of legal container
	 * 
	 * @param shipmentDetails
	 * @param logisticGroupId
	 * @return Integer
	 */
	public Integer countNumberOfLegalCont(List<ShipmentDetail> shipmentDetails, Long logisticGroupId);

	/**
	 * Get SSR code by size type for robot to select while processing.
	 * 
	 * @param sztp
	 * @return
	 */
	public String getSsrCodeBySztp(String sztp);

	/***
	 * getShipmentDetail for SendContFull and receiveContEmpty
	 */
	public List<ShipmentDetail> getShipmentDetailListForSendFReceiveE(ShipmentDetail shipmentDetail);

	/***
	 * get process order id list by shipment id
	 */
	public List<Long> getProcessOrderIdListByShipment(ShipmentDetail shipmentDetail);

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
	public ServiceSendFullRobotReq makeChangeVesselOrder(List<ShipmentDetail> shipmentDetails, String vslNm,
			String voyNo, String vslName, String voyCarrier, Long groupId);

	/**
	 * Make extension date order
	 * 
	 * @param shipmentDetails
	 * @param expiredDem
	 * @param groupId
	 * @return ServiceSendFullRobotReq
	 */
	public List<ServiceSendFullRobotReq> makeExtensionDateOrder(List<ShipmentDetail> shipmentDetails, Date expiredDem,
			Long groupId);

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

	/**
	 * resetShipmentDetailProcessStatus is used for OM reset process status. Not use
	 * with another purpose
	 */
	public int resetShipmentDetailProcessStatus(ShipmentDetail shipmentDetail);

	/**
	 * Get List container with coordinate for carrier
	 * 
	 * @param ShipmentDetail
	 * @return List shipment detail with container, sztp, yard position include
	 */
	public ShipmentDetail[][] getListContainerForCarrier(ShipmentDetail shipmentDetail);

	/**
	 * Select shipment detail for driver send cont on app mobile
	 * 
	 * @param logisticGroupId
	 * @param pickupAssignForm
	 * @return list pickup assign form object
	 */
	public List<PickupAssignForm> selectShipmentDetailForDriverSendCont(@Param("logisticGroupId") Long logisticGroupId,
			@Param("pickUp") PickupAssignForm pickupAssignForm, @Param("serviceType") Integer serviceType);

	/**
	 * Update shipment detail by shipment detail id
	 * 
	 * @param shipmentDetailIds
	 * @param shipmentDetail
	 * @return int
	 */
	public int updateShipmentDetailByIds(String shipmentDetailIds, ShipmentDetail shipmentDetail);

	/**
	 * Reset custom status to null for shipmentId
	 * 
	 * @param shipmentId
	 */
	public void resetCustomStatus(Long shipmentId);

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

	/**
	 * Make order loading cargo
	 * 
	 * @param shipmentDetails
	 * @param shipment
	 * @param taxCode
	 * @param creditFlag
	 * @return List<ServiceSendFullRobotReq>
	 */
	public List<ServiceSendFullRobotReq> makeOrderLoadingCargo(List<ShipmentDetail> shipmentDetails, Shipment shipment,
			String taxCode, boolean creditFlag);
	
	/**
	 * Make order loading cargo
	 * 
	 * @param shipmentDetails
	 * @param shipment
	 * @param taxCode
	 * @param creditFlag
	 * @return List<ServiceSendFullRobotReq>
	 */
	public List<ServiceSendFullRobotReq> makeOrderUnloadingCargo(List<ShipmentDetail> shipmentDetails, Shipment shipment,
			String taxCode, boolean creditFlag);
}
