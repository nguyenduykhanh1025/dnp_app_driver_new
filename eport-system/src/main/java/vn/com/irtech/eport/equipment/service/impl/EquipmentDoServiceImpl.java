package vn.com.irtech.eport.equipment.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;
import vn.com.irtech.eport.equipment.domain.EquipmentDoPaging;
import vn.com.irtech.eport.equipment.mapper.EquipmentDoMapper;
import vn.com.irtech.eport.equipment.service.IEquipmentDoService;

/**
 * Exchange Delivery OrderService Business Processing
 * 
 * @author irtech
 * @date 2020-04-04
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


    @Override
    public List<EquipmentDo> selectEquipmentDoListPagingAdmin(EquipmentDoPaging EquipmentDo) {
        return equipmentDoMapper.selectEquipmentDoListPagingAdmin(EquipmentDo);
    }
    @Override
    public List<EquipmentDo> selectEquipmentDoListPagingCarrier(EquipmentDoPaging EquipmentDo) {
        return equipmentDoMapper.selectEquipmentDoListPagingCarrier(EquipmentDo);
    }
  
      /**
     * Get Exchange Delivery Order List
     * 
     * @param equipmentDo Exchange Delivery Order
     * @return Exchange Delivery Order
     */
    @Override
    public List<EquipmentDo> selectEquipmentDoList(EquipmentDo EquipmentDo)
    {
        return equipmentDoMapper.selectEquipmentDoList(EquipmentDo);
    }
  
    @Override
    public List<EquipmentDo> selectEquipmentDoListTest(EquipmentDo EquipmentDo,@Param("page") int page) {

        return equipmentDoMapper.selectEquipmentDoListTest(EquipmentDo,page);
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

    @Override
    public String getContainerNumberById(Long id) {
      return equipmentDoMapper.getContainerNumberById(id);
    }
    
    @Override
    public List<String> getContainerNumberListByIds(String ids) {
      return equipmentDoMapper.getContainerNumberListByIds(Convert.toStrArray(ids));
    }
    
    @Override
    public  Date getDocumentReceiptDate(Long id)
    {
        return equipmentDoMapper.getDocumentReceiptDate(id);
    }

    @Override
    public String getStatus(Long id)
    {
        return equipmentDoMapper.getStatus(id);
    }

   
}
