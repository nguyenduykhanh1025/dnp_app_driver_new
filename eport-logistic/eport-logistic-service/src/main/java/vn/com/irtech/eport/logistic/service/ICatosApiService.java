package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.BookingInfo;

public interface ICatosApiService {

	public Shipment getOpeCodeCatosByBlNo(String blNo);
	
	public Shipment getGroupNameByTaxCode(String taxCode);
	
	public ProcessOrder getYearBeforeAfter(String vessel, String voyage);
	
	public List<String> checkContainerReserved(String containerNos);
	
	public List<String> getPODList(ShipmentDetail shipmentDetail);
	
	public List<String> getVesselCodeList();
	
	public List<String> getConsigneeList();
	
	public List<String> getOpeCodeList();
	
	public List<String> getVoyageNoList(String vesselCode);
	
	public int getCountContByBlNo(String blNo);
	
	public Boolean checkCustomStatus(String containerNo, String voyNo);
	
	public List<ShipmentDetail> getCoordinateOfContainers(String blNo);
	
	public List<ShipmentDetail> selectShipmentDetailsByBLNo(String blNo);
	
	public ShipmentDetail selectShipmentDetailByContNo(String blNo, String containerNo);
	
	public List<String> selectVesselCodeBerthPlan(String opeCode);
	
	public String getYearByVslCodeAndVoyNo(String vslCode, String voyNo);
	
	public List<String> selectOpeCodeListInBerthPlan();
	
	public List<ShipmentDetail> selectVesselVoyageBerthPlan(String opeCode);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForReserve(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForInventory(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForReceiveSSR(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForSendSSR(ShipmentDetail shipmentDetail);
	
	public Integer checkBookingNoForSendFReceiveE(String bookingNo, String fe);
	
	public ShipmentDetail getInforSendFReceiveE(ShipmentDetail shipmentDetail);
	
	public Integer getIndexContForSsrByContainerNo(String containerNo);
	
	/***
	 * input: vslNm, voyNo, year, sztp,booking
	 * @param shipmentDetail
	 * @return
	 */
	public Integer getIndexBooking(ShipmentDetail shipmentDetail);
	
	/***
	 * input: blNo, containerNo
	 */
	public PickupHistory getLocationForReceiveF(PickupHistory pickupHistory);
	
	/***
	 * input: containerNo, blNo, bookingNo, vslNm, voyNo, sztp, opeCode, fe
	 */
	public String checkContainerStatus(ShipmentDetail shipmentDetail);
	
	/***
	 * input: keyword (empty = '')
	 * get block list
	 */
	public List<String> getBlockList(String keyword);
	
	/***
	 * input: keyword (empty = '')
	 * get Area list
	 */
	public List<String> getAreaList(String keyword);
	
	/***
	 * input: containerNo, bookingNo, vslNm, voyNo, sztp, opeCode
	 */
	public Boolean checkContReserved(ShipmentDetail shipmentDetail);
	
	/***
	 * check container amount have not ordered yet for sendContFull
	 * input: bookingNo, sztp
	 */
	public Integer checkTheNumberOfContainersNotOrderedForSendContFull(String bookingNo, String sztp);
	
	/**
	 * Get list consignee with tax code
	 * 
	 * @param shipment
	 * @return	List<shipment>
	 */
	public List<Shipment> getListConsigneeWithTaxCode(Shipment shipment);
	
	/**
	 * Get sztp by container no
	 * 
	 * @param containerNo
	 * @return String
	 */
	public String getSztpByContainerNo(String containerNo);
	
	/**
	 * Get tax code by snm group name
	 * 
	 * @param consignee
	 * @return String
	 */
	public String getTaxCodeBySnmGroupName(String consignee);
	
	/**
	 * Get Coordinate Of Containers list by job order no
	 * 
	 * @param jobOrder
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> getCoordinateOfContainersByJobOrderNo(String jobOrder);
	
	/**
	 * Select shipment detail by job order
	 * 
	 * @param jobOrder
	 * @return List<ShipmentDetail>
	 */
	public List<ShipmentDetail> selectShipmentDetailByJobOrder(String jobOrder);
	
	/**
	 * Get bl no by order job no
	 * 
	 * @param jobOrder
	 * @return
	 */
	public String getBlNoByOrderJobNo(String jobOrder);
	
	/**
	 * Get consignee name by tax code
	 * 
	 * @param taxCode
	 * @return Shipment
	 */
	public Shipment getConsigneeNameByTaxCode(String taxCode);
	
	/**
	 * Get opr code list
	 * 
	 * @return
	 */
	public List<String> getOprCodeList();
	
	/**
	 * Select vessel voyage berth plan without ope code
	 * 
	 * @return
	 */
	public List<ShipmentDetail> selectVesselVoyageBerthPlanWithoutOpe();
	
	/**
	 * Get booking info from catos by booking no
	 * and user voy (ope code + voy no)
	 * 
	 * @param bookingNo
	 * @param userVoy
	 * @return List Booking info object
	 */
	public List<BookingInfo> getBookingInfo(String bookingNo, String userVoy);
}
