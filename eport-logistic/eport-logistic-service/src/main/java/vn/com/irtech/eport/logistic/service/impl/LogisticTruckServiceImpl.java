package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.LogisticTruckMapper;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 *LogisticTruckService Business Processing
 * 
 * @author admin
 * @date 2020-06-16
 */
@Service
public class LogisticTruckServiceImpl implements ILogisticTruckService 
{
    @Autowired
    private LogisticTruckMapper logisticTruckMapper;

    /**
     * GetLogisticTruck
     * 
     * @param idLogisticTruckID
     * @returnLogisticTruck
     */
    @Override
    public LogisticTruck selectLogisticTruckById(Long id)
    {
        return logisticTruckMapper.selectLogisticTruckById(id);
    }

    /**
     * GetLogisticTruck List
     * 
     * @param logisticTruck
     * @returnLogisticTruck
     */
    @Override
    public List<LogisticTruck> selectLogisticTruckList(LogisticTruck logisticTruck)
    {
        return logisticTruckMapper.selectLogisticTruckList(logisticTruck);
    }

    /**
     * AddLogisticTruck
     * 
     * @param logisticTruck
     * @return result
     */
    @Override
    public int insertLogisticTruck(LogisticTruck logisticTruck)
    {
        logisticTruck.setCreateTime(DateUtils.getNowDate());
        return logisticTruckMapper.insertLogisticTruck(logisticTruck);
    }

    /**
     * UpdateLogisticTruck
     * 
     * @param logisticTruck
     * @return result
     */
    @Override
    public int updateLogisticTruck(LogisticTruck logisticTruck)
    {
        logisticTruck.setUpdateTime(DateUtils.getNowDate());
        return logisticTruckMapper.updateLogisticTruck(logisticTruck);
    }

    /**
     * DeleteLogisticTruck By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteLogisticTruckByIds(String ids)
    {
        return logisticTruckMapper.deleteLogisticTruckByIds(Convert.toStrArray(ids));
    }

    /**
     * DeleteLogisticTruck
     * 
     * @param idLogisticTruckID
     * @return result
     */
    @Override
    public int deleteLogisticTruckById(Long id)
    {
        return logisticTruckMapper.deleteLogisticTruckById(id);
    }

	@Override
	public int checkPlateNumberUnique(String plateNumber) {
		int count = logisticTruckMapper.checkPlateNumberUnique(plateNumber);
		if (count > 0) {
			return count;
		}
		return 0;
	}

	@Override
	public int updateDelFlagByIds(String ids) {
		return logisticTruckMapper.updateDelFlagByIds(Convert.toStrArray(ids));
	}
}
