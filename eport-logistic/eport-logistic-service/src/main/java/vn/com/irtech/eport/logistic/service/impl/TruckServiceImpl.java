package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.TruckMapper;
import vn.com.irtech.eport.logistic.domain.Truck;
import vn.com.irtech.eport.logistic.service.ITruckService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * TruckService Business Processing
 * 
 * @author admin
 * @date 2020-06-16
 */
@Service
public class TruckServiceImpl implements ITruckService 
{
    @Autowired
    private TruckMapper truckMapper;

    /**
     * Get Truck
     * 
     * @param id TruckID
     * @return Truck
     */
    @Override
    public Truck selectTruckById(Long id)
    {
        return truckMapper.selectTruckById(id);
    }

    /**
     * Get Truck List
     * 
     * @param truck Truck
     * @return Truck
     */
    @Override
    public List<Truck> selectTruckList(Truck truck)
    {
        return truckMapper.selectTruckList(truck);
    }

    /**
     * Add Truck
     * 
     * @param truck Truck
     * @return result
     */
    @Override
    public int insertTruck(Truck truck)
    {
        truck.setCreateTime(DateUtils.getNowDate());
        return truckMapper.insertTruck(truck);
    }

    /**
     * Update Truck
     * 
     * @param truck Truck
     * @return result
     */
    @Override
    public int updateTruck(Truck truck)
    {
        truck.setUpdateTime(DateUtils.getNowDate());
        return truckMapper.updateTruck(truck);
    }

    /**
     * Delete Truck By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteTruckByIds(String ids)
    {
        return truckMapper.deleteTruckByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Truck
     * 
     * @param id TruckID
     * @return result
     */
    @Override
    public int deleteTruckById(Long id)
    {
        return truckMapper.deleteTruckById(id);
    }

	@Override
	public int checkPlateNumberUnique(String plateNumber) {
		int count = truckMapper.checkPlateNumberUnique(plateNumber);
		if (count > 0) {
			return count;
		}
		return 0;
	}

	@Override
	public int updateDelFlagByIds(String ids) {
		return truckMapper.updateDelFlagByIds(Convert.toStrArray(ids));
	}
}
