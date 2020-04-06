package vn.com.irtech.eport.equipment.mapper;

import java.util.List;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;

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
    public List<EquipmentDo> selectEquipmentDoList(EquipmentDo equipmentDo);


    public List<EquipmentDo> selectEquipmentDoListDo();
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

	
}
