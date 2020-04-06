package vn.com.irtech.eport.system.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.system.mapper.EquipmentDoMapper;
import vn.com.irtech.eport.system.domain.EquipmentDo;
import vn.com.irtech.eport.system.service.IEquipmentDoService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Exchange Delivery OrderService Business Processing
 * 
 * @author ruoyi
 * @date 2020-04-06
 */
@Service
public class EquipmentDoServiceImpl implements IEquipmentDoService 
{
    @Autowired
    private EquipmentDoMapper equipmentDoMapper;

    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    @Override
    public EquipmentDo selectEquipmentDoById(Long id)
    {
        return equipmentDoMapper.selectEquipmentDoById(id);
    }

    /**
     * Get Exchange Delivery Order List
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return Exchange Delivery Order
     */
    @Override
    public List<EquipmentDo> selectEquipmentDoList(EquipmentDo equipmentDo)
    {
        return equipmentDoMapper.selectEquipmentDoList(equipmentDo);
    }

    /**
     * Add Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return result
     */
    @Override
    public int insertEquipmentDo(EquipmentDo equipmentDo)
    {
        equipmentDo.setCreateTime(DateUtils.getNowDate());
        return equipmentDoMapper.insertEquipmentDo(equipmentDo);
    }

    /**
     * Update Exchange Delivery Order
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return result
     */
    @Override
    public int updateEquipmentDo(EquipmentDo equipmentDo)
    {
        equipmentDo.setUpdateTime(DateUtils.getNowDate());
        return equipmentDoMapper.updateEquipmentDo(equipmentDo);
    }

    /**
     * Delete Exchange Delivery Order By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEquipmentDoByIds(String ids)
    {
        return equipmentDoMapper.deleteEquipmentDoByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    @Override
    public int deleteEquipmentDoById(Long id)
    {
        return equipmentDoMapper.deleteEquipmentDoById(id);
    }
}
