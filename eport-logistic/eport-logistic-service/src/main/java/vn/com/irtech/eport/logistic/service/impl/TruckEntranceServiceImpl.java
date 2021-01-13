package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.TruckEntranceMapper;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Truck EntranceService Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-12-31
 */
@Service
public class TruckEntranceServiceImpl implements ITruckEntranceService 
{
    @Autowired
    private TruckEntranceMapper truckEntranceMapper;

    /**
     * Get Truck Entrance
     * 
     * @param id Truck EntranceID
     * @return Truck Entrance
     */
    @Override
    public TruckEntrance selectTruckEntranceById(Long id)
    {
        return truckEntranceMapper.selectTruckEntranceById(id);
    }

    /**
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance
     */
    @Override
    public List<TruckEntrance> selectTruckEntranceList(TruckEntrance truckEntrance)
    {
        return truckEntranceMapper.selectTruckEntranceList(truckEntrance);
    }

    /**
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance
     */
    @Override
    public List<TruckEntrance> selectTruckEntranceFollowTruckNoList(TruckEntrance truckEntrance)
    {
        return truckEntranceMapper.selectTruckEntranceFollowTruckNoList(truckEntrance);
    }

    /**
     * Get Truck Entrance List
     * 
     * @param truckEntrance Truck Entrance
     * @return Truck Entrance
     */
    @Override
    public List<TruckEntrance> selectTruckEntranceListOderByCreateTime(TruckEntrance truckEntrance)
    {
        return truckEntranceMapper.selectTruckEntranceListOderByCreateTime(truckEntrance);
    }
    
    /**
     * Add Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return result
     */
    @Override
    public int insertTruckEntrance(TruckEntrance truckEntrance)
    {
        truckEntrance.setCreateTime(DateUtils.getNowDate());
        return truckEntranceMapper.insertTruckEntrance(truckEntrance);
    }

    /**
     * Update Truck Entrance
     * 
     * @param truckEntrance Truck Entrance
     * @return result
     */
    @Override
    public int updateTruckEntrance(TruckEntrance truckEntrance)
    {
        truckEntrance.setUpdateTime(DateUtils.getNowDate());
        return truckEntranceMapper.updateTruckEntrance(truckEntrance);
    }

    /**
     * Delete Truck Entrance By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteTruckEntranceByIds(String ids)
    {
        return truckEntranceMapper.deleteTruckEntranceByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Truck Entrance
     * 
     * @param id Truck EntranceID
     * @return result
     */
    @Override
    public int deleteTruckEntranceById(Long id)
    {
        return truckEntranceMapper.deleteTruckEntranceById(id);
    }
}
