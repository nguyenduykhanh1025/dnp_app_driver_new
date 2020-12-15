package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.LogisticTruck;

/**
 *LogisticTruckService Interface
 * 
 * @author admin
 * @date 2020-06-16
 */
public interface ILogisticTruckService 
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
     * @param logisticTruck
     * @returnLogisticTruck List
     */
    public List<LogisticTruck> selectLogisticTruckList(LogisticTruck logisticTruck);

    /**
     * AddLogisticTruck
     * 
     * @param logisticTruck
     * @return result
     */
    public int insertLogisticTruck(LogisticTruck logisticTruck);

    /**
     * UpdateLogisticTruck
     * 
     * @param logisticTruck
     * @return result
     */
    public int updateLogisticTruck(LogisticTruck logisticTruck);

    /**
     * Batch DeleteLogisticTruck
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteLogisticTruckByIds(String ids);

    /**
     * DeleteLogisticTruck
     * 
     * @param idLogisticTruckID
     * @return result
     */
    public int deleteLogisticTruckById(Long id);

    public int checkPlateNumberUnique(String plateNumber);

    public int updateDelFlagByIds(String ids);

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
