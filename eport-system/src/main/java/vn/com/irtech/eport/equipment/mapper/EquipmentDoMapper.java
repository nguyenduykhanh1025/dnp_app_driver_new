package vn.com.irtech.eport.equipment.mapper;

import java.util.Date;

import java.util.List;

import org.apache.ibatis.annotations.Param;


import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;

/**
 * Exchange Delivery OrderMapper Interface
 * 
 * @author irtech
 * @date 2020-04-04
 */
public interface EquipmentDoMapper 
{
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
    public List<EquipmentDo> selectEquipmentDoListAdmin(EquipmentDoPaging EquipmentDo);
    
    public List<EquipmentDo> selectEquipmentDoListTest(EquipmentDo EquipmentDo,@Param("page") int page);

    public List<EquipmentDo> selectEquipmentDoList(EquipmentDo EquipmentDo);
    /**
     * Add Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return Result
     */
    public int insertEquipmentDo(EquipmentDo equipmentDo);

    /**
     * Update Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return Result
     */
    public int updateEquipmentDo(EquipmentDo equipmentDo);

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    public int deleteEquipmentDoById(Long id);

    /**
     * Batch Delete Exchange Delivery Order
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEquipmentDoByIds(String[] ids);

	public List<EquipmentDo> selectEquipmentDoListTest(int page, EquipmentDo equipmentDo);

	public List<EquipmentDo> selectEquipmentDoListPagingAdmin(EquipmentDoPaging equipmentDo);

	
    public List<EquipmentDo> selectEquipmentDoListPagingCarrier(EquipmentDoPaging equipmentDo);
	
  public String getContainerNumberById(Long id);

  public List<String> getContainerNumberListByIds(String[] ids);
  
  public Date getDocumentReceiptDate(long id);

  public String getStatus(Long id);

  public List<EquipmentDo> getContainerListByIds(String[] ids);
  //getPages billOfLading detail
  public Long getTotalPagesCont(String billOfLading);

  public List<EquipmentDo> selectEquipmentDoListExclusiveBill(EquipmentDo equipmentDo);
  
  public String countContainerNumber(String billOfLading);
  //Chang Do status
  public int updateBillOfLading(EquipmentDo equipmentDo);

  public List<EquipmentDo> selectEquipmentDoDetails(EquipmentDo equipmentDo);

  public int updateEquipmentDoExpiredDem(EquipmentDo equipmentDo);
	
}
