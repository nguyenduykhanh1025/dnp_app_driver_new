package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.CarrierEquipmentDo;

/**
 * Exchange Delivery OrderService Interface
 * 
 * @author ruoyi
 * @date 2020-04-06
 */
public interface CarrierIEquipmentDoService 
{
    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    public CarrierEquipmentDo selectEquipmentDoById(Long id);

    /**
     * Get Exchange Delivery Order List
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return Exchange Delivery Order List
     */
    public List<CarrierEquipmentDo> selectEquipmentDoList(CarrierEquipmentDo equipmentDo);

    public List<CarrierEquipmentDo> selectEquipmentDoListDo();

    /**
     * Add Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return result
     */
    public int insertEquipmentDo(CarrierEquipmentDo equipmentDo);

    /**
     * Update Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return result
     */
    public int updateEquipmentDo(CarrierEquipmentDo equipmentDo);

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