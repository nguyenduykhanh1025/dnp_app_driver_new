package vn.com.irtech.api.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.dto.BerthPlanInfo;
import vn.com.irtech.api.dto.ContainerHoldInfo;
import vn.com.irtech.api.dto.ContainerInfoDto;
import vn.com.irtech.api.dto.PartnerInfoDto;
import vn.com.irtech.api.entity.ShipmentDetailEntity;
import vn.com.irtech.api.entity.ShipmentEntity;
import vn.com.irtech.api.form.BookingInfo;

@Mapper
public interface ShipmentDetailDao extends BaseMapper<ShipmentDetailEntity> {

	public List<ContainerInfoDto> selectShipmentDetailsByBLNo(String blNo);

	/**
	 * Select container info by bl no (container full can be already gate out)
	 * 
	 * @param blNo
	 * @return
	 */
	public List<ContainerInfoDto> selectContainerInfoByBLNo(String blNo);

	public ShipmentDetailEntity selectShipmentDetailByContNo(ShipmentDetailEntity shipmentDetailEntity);

	public List<ShipmentDetailEntity> selectCoordinateOfContainers(String blNo);

	public List<String> selectVesselCodeList();

	/**
	 * Get list of POD for Vessel
	 * @param shipmentDetailEntity: VSL_CD, YEAR, CALL_SEQ
	 * @return List of POD
	 */
	public List<String> selectPODList(ShipmentDetailEntity shipmentDetailEntity);
	
	/**
	 * Get all OPRs from vessel
	 * @param shipmentDetailEntity: VSL_CD, YEAR, CALL_SEQ
	 * @return List of OPR
	 */
	public List<String> selectOPRList(ShipmentDetailEntity shipmentDetailEntity);

	public List<String> selectConsigneeList();

	/**
	 * Select consignee list both has and has not taxcode
	 * 
	 * @return List<String>
	 */
	public List<String> selectConsigneeListWithoutTaxCode();

	public List<String> selectVoyageNoListByVesselCode(String vesselCode);

	public List<String> selectOpeCodeList();

	public PartnerInfoDto getGroupNameByTaxCode(String taxCode);

	public List<String> checkContReservedByContainerNos(String[] containerNos);

	/**
	 * Check container pickup information. If userVoy is not null then filter by
	 * USER_VOY also
	 * 
	 * @param containerNos Container number array
	 * @param userVoy
	 * @return CNTR_NO / IX_CD / USER_VOY / PTNR_CODE / JOB_ODR_NO2
	 */
	public List<String> checkPickupByContainerNos(@Param("containerNos") String[] containerNos,
			@Param("userVoy") String userVoy);

	public Integer getCountContByBlNo(String blNo);

	public ShipmentEntity getOpeCodeCatosByBlNo(String blNo);

	public Boolean checkCustomStatus(@Param("containerNo") String containerNo, @Param("voyNo") String voyNo);

	public List<String> selectVesselCodeBerthPlan(String opeCode);

	public String getYearByVslCodeAndVoyNo(@Param("vesselCode") String vesselCode, @Param("voyNo") String voyNo);

	public List<String> selectOpeCodeListInBerthPlan();

	public List<ShipmentDetailEntity> selectVesselVoyageBerthPlan(String opeCode);

	public Integer checkBookingNoForSendFReceiveE(@Param("bookingNo") String bookingNo, @Param("fe") String fe);

	public ShipmentDetailEntity getInforSendFReceiveE(ShipmentDetailEntity shipmentDetailEntity);

	public List<Date> getIndexContMasterForSSRByContainerNo(String containerNo);

	public List<ShipmentDetailEntity> getIndexBooking(ShipmentDetailEntity shipmentDetailEntity);

	public ShipmentDetailEntity getLocationForReceiveF(@Param("blNo") String blNo,
			@Param("containerNo") String containerNo);

	public String checkContainerStatus(ShipmentDetailEntity shipmentDetailEntity);

	public List<String> getBlockList(String keyword);

	public List<String> getAreaList(String keyword);

	public ShipmentDetailEntity checkContReserved(ShipmentDetailEntity shipmentDetailEntity);

	public Integer checkTheNumberOfContainersNotOrderedForSendContFull(@Param("bookingNo") String bookingNo,
			@Param("sztp") String sztp);

	/**
	 * Select Consignee And TaxCode List
	 * 
	 * @param partnerInfo
	 * @return List<PartnerInfoDto>
	 */
	public List<PartnerInfoDto> selectConsigneeTaxCode(PartnerInfoDto partnerInfo);

	/**
	 * Get sztp by container no from table TB_MASTER
	 * 
	 * @param containerNo
	 * @return String
	 */
	public String getSztpByContainerNoMaster(String containerNo);

	/**
	 * Get sztp by container no from table TB_INVENTORY
	 * 
	 * @param containerNo
	 * @return
	 */
	public String getSztpByContainerNoInventory(String containerNo);

	/**
	 * Get tax code by snm group name
	 * 
	 * @param consignee
	 * @return String
	 */
	public String getTaxCodeBySnmGroupName(String consignee);

	/**
	 * Get bl no by job order no 2
	 * 
	 * @param jobOrdNo2
	 * @return String
	 */
	public String getblNoByJobOrderNo(String jobOrdNo2);

	/**
	 * Get consigne(e by tax code
	 * 
	 * @param taxCode
	 * @return ShipmentEntity
	 */
	public PartnerInfoDto getConsigneeByTaxCode(String taxCode);

	/**
	 * Get opr code list
	 * 
	 * @return
	 */
	public List<String> getOprCodeList();

	/**
	 * Select vessel voyage berth plan without ope
	 * 
	 * @return List<ShipmentDetailEntity
	 */
	public List<ShipmentDetailEntity> selectVesselVoyageBerthPlanWithoutOpe();

	/**
	 * Get booking info by booking no and user voy (ope code + voyage) Booking info
	 * include booking no, sztp, booking quantity(the number of sztp), booking
	 * quantity has been used to order
	 * 
	 * @param bookingNo
	 * @param userVoy
	 * @return List of booking info
	 */
	public List<BookingInfo> getBookingInfo(@Param("bookingNo") String bookingNo, @Param("userVoy") String userVoy);

	/**
	 * get OrderNo in Inventory by shipmentDetail
	 */
	public String getOrderNoInInventoryByShipmentDetail(ShipmentDetailEntity shipmentDetailEntity);

	/**
	 * get OrderNo in Reserve by shipmentDetail
	 */
	public String getOrderNoInReserveByShipmentDetail(ShipmentDetailEntity shipmentDetailEntity);

	/**
	 * 
	 * getCoordinateOfContainers for Carrier
	 */
	public List<ShipmentDetailEntity> selectCoordinateOfContainersByShipmentDetail(
			ShipmentDetailEntity shipmentDetailEntity);

	/**
	 * Select list block by sztp and ope code
	 * 
	 * @param sztp
	 * @param opeCode
	 * @return List string of block
	 */
	public List<String> selectListBlockBySztpOpeCode(ShipmentDetailEntity shipmentDetailEntity);

	/**
	 * Select list bay by sztp and ope code
	 * 
	 * @param sztp
	 * @param opeCode
	 * @return List string of block
	 */
	public List<String> selectListBayBySztpOpeCode(ShipmentDetailEntity shipmentDetailEntity);

	/**
	 * Get trucker by reg no
	 * 
	 * @param taxCode
	 * @return String
	 */
	public String getTruckerByRegNo(String taxCode);

	/**
	 * Get container list none termianl hold
	 * 
	 * @param containers
	 * @return List<String>
	 */
	public List<String> getContainerListHoldRelease(ContainerHoldInfo containerHoldInfo);

	/**
	 * Get number of consignee
	 * 
	 * @param consignee
	 * @return Integer number of consignee
	 */
	public Integer getNumberOfConsignee(String consignee);

	/**
	 * Get number of pod
	 * 
	 * @param pod
	 * @return Integer number of pod
	 */
	public Integer getNumberOfPod(String pod);

	/**
	 * Get container info stacking on depot by list container no String[]
	 * 
	 * @param containerNos
	 * @return List<ContainerInfoDto>
	 */
	public List<ContainerInfoDto> selectShipmentDetailByContainerNos(String[] containerNos);
	
	/**
	 * Get container info stacking and all in history by list container no String[]
	 * 
	 * @param containerNos
	 * @return List<ContainerInfoDto>
	 */
	public List<ContainerInfoDto> selectAllShipmentDetailByContainerNos(String[] containerNos);
	/**
	 * Get list of container info by JOB_ODR_NO from catos.<br>
	 * Using when check if JOB_ODR_NO is successful create in catos.
	 * 
	 * @param jobOdrNo
	 * @return List of Container in jobOdrNo
	 */
	public List<ContainerInfoDto> selectShipmentDetailByJobOdrNo(String jobOdrNo);

	/**
	 * Get berth plan information
	 * 
	 * @param berthPlanInfo
	 * @return BerthPlanInfo Object
	 */
	public BerthPlanInfo getBerthPlanInfo(BerthPlanInfo berthPlanInfo);

	/**
	 * Select container info by ContainerInfoDto object
	 * 
	 * @param cntrInfo
	 * @return List<ContainerInfoDto>
	 */
	public List<ContainerInfoDto> selectContainerInfoByCntrInfo(ContainerInfoDto cntrInfo);
}