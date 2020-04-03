package vn.com.irtech.eport.edo.service;

import java.util.List;
import vn.com.irtech.eport.edo.domain.EquipmentDo;

/**
 * Exchange Delivery OrderService Interface
 * 
 * @author Irtech
 * @date 2020-04-03
 */
public interface IEquipmentDoService 
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
}
