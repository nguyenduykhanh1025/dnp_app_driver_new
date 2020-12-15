package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.LogisticTruck;

/**
 *LogisticTruckMapper Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface LogisticTruckMapper 
{
    /**
     * GetLogisticTruck
     * 
     * @param idLogisticTruckID
     * @returnLogisticTruck
     */
    public LogisticTruck selectLogisticTruckById(Long id);

    /**
     * GetLogisticTruck List
     * 
     * @param truckLogistic
     * @returnLogisticTruck List
     */
    public List<LogisticTruck> selectLogisticTruckList(LogisticTruck logisticTruck);

    /**
     * AddLogisticTruck
     * 
     * @param truckLogistic
     * @return Result
     */
    public int insertLogisticTruck(LogisticTruck logisticTruck);

    /**
     * UpdateLogisticTruck
     * 
     * @param truckLogistic
     * @return Result
     */
    public int updateLogisticTruck(LogisticTruck logisticTruck);

    /**
     * DeleteLogisticTruck
     * 
     * @param idLogisticTruckID
     * @return result
     */
    public int deleteLogisticTruckById(Long id);

    /**
     * Batch DeleteLogisticTruck
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteLogisticTruckByIds(String[] ids);

    public int checkPlateNumberUnique(String plateNumber);

    public int updateDelFlagByIds(String[] ids);

    public List<String> selectListTruckNoByDriverId(Long driverId);
    
    public List<String> selectListChassisNoByDriverId(Long driverId); 

	/**
	 * Select logistic truck list with rfid
	 * 
	 * @param logisticTruck
	 * @return
	 */
	public List<LogisticTruck> selectLogisticTruckListWithRfid(LogisticTruck logisticTruck);

	/**
	 * Select logistic truck by id with rfid
	 * 
	 * @param id
	 * @return
	 */
	public LogisticTruck selectLogisticTruckByIdWithRfid(Long id);
}
