package vn.com.irtech.eport.equipment.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;

/**
 * Exchange Delivery OrderService Interface
 * 
 * @author irtech
 * @date 2020-04-04
 */
public interface IEquipmentDoService {
	/**
	 * Get Exchange Delivery Order
	 * 
	 * @param id Exchange Delivery OrderID
	 * @return Exchange Delivery Order
	 */
	public EquipmentDo selectEquipmentDoById(Long id);

	/**
	 * Get Exchange Delivery Order List
	 * 
	 * @param equipmentDo Exchange Delivery Order
	 * @return Exchange Delivery Order List
	 */
	public List<EquipmentDo> selectEquipmentDoListPagingAdmin(EquipmentDoPaging EquipmentDo);

	public List<EquipmentDo> selectEquipmentDoListPagingCarrier(EquipmentDoPaging EquipmentDo);

	public List<EquipmentDo> selectEquipmentDoList(EquipmentDo EquipmentDo);

	public List<EquipmentDo> selectEquipmentDoVoByBillNo(String blNo);

	/**
	 * GetlistDO
	 * 
	 * @param page Exchange Delivery Order
	 * @return result
	 */
	public List<EquipmentDo> selectEquipmentDoListTest(EquipmentDo EquipmentDo, @Param("page") int page);

	/**
	 * Add Exchange Delivery Order
	 * 
	 * @param equipmentDo Exchange Delivery Order
	 * @return result
	 */
	public int insertEquipmentDo(EquipmentDo equipmentDo);

	/**
	 * Update Exchange Delivery Order
	 * 
	 * @param equipmentDo Exchange Delivery Order
	 * @return result
	 */
	public int updateEquipmentDo(EquipmentDo equipmentDo);

	/**
	 * Batch Delete Exchange Delivery Order
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
	public int deleteEquipmentDoByIds(String ids);

	/**
	 * Delete Exchange Delivery Order
	 * 
	 * @param id Exchange Delivery OrderID
	 * @return result
	 */
	public int deleteEquipmentDoById(Long id);

	public String getContainerNumberById(Long id);

	public List<String> getContainerNumberListByIds(String ids);

	// Check DocumentReceiptDate has value
	public Date getDocumentReceiptDate(Long id);

	// Check Do Status check
	public String getStatus(Long id);

	public List<EquipmentDo> getContainerListByIds(String ids);

	// getTotalPages
	public Long getTotalPagesCont(String billOfLading);

	public List<EquipmentDo> selectEquipmentDoListExclusiveBill(EquipmentDo equipmentDo);

	public String countContainerNumber(String billOfLading);

	// Chang Do status
	public int updateBillOfLading(EquipmentDo equipmentDo);

	public List<EquipmentDo> selectEquipmentDoDetails(EquipmentDo equipmentDo);

	public int updateEquipmentDoExpiredDem(EquipmentDo equipmentDo);

	/**
	 * Report for carrier group
	 * 
	 * @param operator codes
	 */
	public Map<String, String> getReportByCarrierGroup(String[] codes);

	/**
	 * Get report for admin page
	 * 
	 * @return
	 */
	public Map<String, String> getReportForAdmin();

	// countDOStatus
	public int countDOStatusYes(String billOfLading);

	// countDocmentStatus
	public int countDocmentStatusYes(String billOfLading);

	/**
	 * Get Bill of lading info
	 * 
	 * @param blNo
	 * @return
	 */
	public EquipmentDo getBillOfLadingInfo(String blNo);

	public List<String> getConsignee(Long id);

	public List<String> getVessel(Long id);

	public List<String> getEmptyContainerDepot(Long id);

}
