package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.CarrierGroupMapper;
import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Carrier GroupService Business Processing
 * 
 * @author irtech
 * @date 2020-04-06
 */
@Service
public class CarrierGroupServiceImpl implements ICarrierGroupService 
{
    @Autowired
    private CarrierGroupMapper carrierGroupMapper;

    /**
     * Get Carrier Group
     * 
     * @param id Carrier GroupID
     * @return Carrier Group
     */
    @Override
    public CarrierGroup selectCarrierGroupById(Long id)
    {
        return carrierGroupMapper.selectCarrierGroupById(id);
    }

    /**
     * Get Carrier Group List
     * 
     * @param carrierGroup Carrier Group
     * @return Carrier Group
     */
    @Override
    public List<CarrierGroup> selectCarrierGroupList(CarrierGroup carrierGroup)
    {
        return carrierGroupMapper.selectCarrierGroupList(carrierGroup);
    }

    /**
     * Add Carrier Group
     * 
     * @param carrierGroup Carrier Group
     * @return result
     */
    @Override
    public int insertCarrierGroup(CarrierGroup carrierGroup)
    {
        carrierGroup.setCreateTime(DateUtils.getNowDate());
        return carrierGroupMapper.insertCarrierGroup(carrierGroup);
    }

    /**
     * Update Carrier Group
     * 
     * @param carrierGroup Carrier Group
     * @return result
     */
    @Override
    public int updateCarrierGroup(CarrierGroup carrierGroup)
    {
        carrierGroup.setUpdateTime(DateUtils.getNowDate());
        return carrierGroupMapper.updateCarrierGroup(carrierGroup);
    }

    /**
     * Delete Carrier Group By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteCarrierGroupByIds(String ids)
    {
        return carrierGroupMapper.deleteCarrierGroupByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Carrier Group
     * 
     * @param id Carrier GroupID
     * @return result
     */
    @Override
    public int deleteCarrierGroupById(Long id)
    {
        return carrierGroupMapper.deleteCarrierGroupById(id);
    }

    /**
     * Search carrier group by name
     * 
     * @param keyword 
     * @return result
     */
    @Override
    public List<CarrierGroup> selectCarrierGroupListByName(CarrierGroup carrierGroup) {
        return carrierGroupMapper.selectCarrierGroupListByName(carrierGroup);
    }
}
