package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.BerthPlanInfo;
import vn.com.irtech.eport.logistic.dto.ContainerHistoryDto;
import vn.com.irtech.eport.logistic.dto.ContainerHoldInfo;
import vn.com.irtech.eport.logistic.form.BookingInfo;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;

public interface ICatosApiService {

	public Shipment getOpeCodeCatosByBlNo(String blNo);

	public PartnerInfoDto getGroupNameByTaxCode(String taxCode);

	public ProcessOrder getYearBeforeAfter(String vessel, String voyage);

	public List<String> checkContainerReserved(String containerNos);

	/**
	 * Check container pickup information. If userVoy is not null then filter by
	 * USER_VOY also
	 * 
	 * @param containerNos Container number array
	 * @param userVoy
	 * @return CNTR_NO / IX_CD / USER_VOY / PTNR_CODE / JOB_ODR_NO2
	 */
	public List<ContainerInfoDto> getContainerPickup(String containerNos, String userVoy);

	public List<String> getPODList(ShipmentDetail shipmentDetail);
	
	public List<String> getOPRList(ShipmentDetail shipmentDetail);

	public List<String> getVesselCodeList();

	public List<String> getConsigneeList();

	public List<String> getOpeCodeList();

	public List<String> getVoyageNoList(String vesselCode);

	public int getCountContByBlNo(String blNo);

	public Boolean checkCustomStatus(String containerNo, String voyNo);

	public List<ShipmentDetail> getCoordinateOfContainers(String blNo);

	public List<ContainerInfoDto> selectShipmentDetailsByBLNo(String blNo);

	/**
	 * Get container info list by bl no
	 * 
	 * @param blNo
	 * @return List<ContainerInfoDto>
	 */
	public List<ContainerInfoDto> getContainerInfoListByBlNo(String blNo);

	public ShipmentDetail selectShipmentDetailByContNo(ShipmentDetail shipmentDetail);

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
	 * 
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
	 * input: keyword (empty = '') get block list
	 */
	public List<String> getBlockList(String keyword);

	/***
	 * input: keyword (empty = '') get Area list
	 */
	public List<String> getAreaList(String keyword);

	/***
	 * input: containerNo, bookingNo, vslNm, voyNo, sztp, opeCode
	 */
	public Boolean checkContReserved(ShipmentDetail shipmentDetail);

	/***
	 * check container amount have not ordered yet for sendContFull input:
	 * bookingNo, sztp
	 */
	public Integer checkTheNumberOfContainersNotOrderedForSendContFull(String bookingNo, String sztp);

	/**
	 * Get list consignee with tax code
	 * 
	 * @param shipment
	 * @return List<shipment>
	 */
	public List<PartnerInfoDto> getListConsigneeWithTaxCode(PartnerInfoDto shipment);

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
	public List<ContainerInfoDto> selectShipmentDetailByJobOrder(String jobOrder);

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
	public PartnerInfoDto getConsigneeNameByTaxCode(String taxCode);

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
	 * Get booking info from catos by booking no and user voy (ope code + voy no)
	 * 
	 * @param bookingNo
	 * @param userVoy
	 * @return List Booking info object
	 */
	public List<BookingInfo> getBookingInfo(String bookingNo, String userVoy);

	/**
	 * Get orderNo in Inventory from catos by shipmentDetail for OM support
	 * orderRegister
	 */
	public String getOrderNoInInventoryByShipmentDetail(ShipmentDetail shipmentDetail);

	/**
	 * Get orderNo in Reserve from catos by shipmentDetail for OM support
	 * orderRegister
	 */
	public String getOrderNoInReserveByShipmentDetail(ShipmentDetail shipmentDetail);

	/**
	 * Get InvoiceNo by OrderNo for OM support orderRegister
	 */
	public String getInvoiceNoByOrderNo(String orderNo);

	/**
	 * 
	 * getCoordinateOfContainers for Carrier
	 */
	public List<ShipmentDetail> selectCoordinateOfContainersByShipmentDetail(ShipmentDetail shipmentDetail);

	/**
	 * Get block list for carrier where container of carrier is exist in depot
	 * 
	 * @param shipmentDetail
	 * @return List string block
	 */
	public List<String> getBlocksForCarrier(ShipmentDetail shipmentDetail);

	/**
	 * Get bay list for carrier where container of carrier is exist in depot
	 * 
	 * @param shipmentDetail
	 * @return List string bay
	 */
	public List<String> getBaysForCarrier(ShipmentDetail shipmentDetail);

	/**
	 * Get trucker from column ptnr_code in catos by tax code (reg_no)
	 * 
	 * @param taxCode
	 * @return String
	 */
	public String getTruckerByTaxCode(String taxCode);

	/**
	 * Get list container no not hold terminal
	 * 
	 * @param containers
	 * @return List<String>
	 */
	public List<String> getContainerListHoldRelease(ContainerHoldInfo containerHoldInfo);

	/**
	 * Check if consignee is exist in catos
	 * 
	 * @param consignee
	 * @return number of consignee record in catos
	 */
	public Integer checkConsigneeExistInCatos(String consignee);

	/**
	 * Check if pod (discharge port) exist in catos
	 * 
	 * @param pod
	 * @return number of pod record in catos
	 */
	public Integer checkPodExistIncatos(String pod);

	/**
	 * Get container info by cont no list separated by comma
	 * 
	 * @param containerNo
	 * @return List<ContaienrInfoDto>
	 */
	public List<ContainerInfoDto> getContainerInfoDtoByContNos(String containerNos);

	/**
	 * Get container history info
	 * 
	 * @param containerHistory
	 * @return List<ContainerHistoryDto>
	 */
	public List<ContainerHistoryDto> getContainerHistory(ContainerHistoryDto containerHistory);

	/**
	 * Get berth plan information
	 * 
	 * @param berthPlanInfo
	 * @return BerthPlanInfo
	 */
	public BerthPlanInfo getBerthPlanInfo(BerthPlanInfo berthPlanInfo);
}
