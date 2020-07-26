package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.form.Pickup;
import vn.com.irtech.eport.logistic.form.PickupHistoryDetail;
import vn.com.irtech.eport.logistic.form.PickupHistoryForm;

/**
 * Pickup history Mapper Interface
 * 
 * @author baohv
 * @date 2020-06-27
 */
public interface PickupHistoryMapper 
{
    /**
     * Get Pickup history
     * 
     * @param id Pickup historyID
     * @return Pickup history
     */
    public PickupHistory selectPickupHistoryById(Long id);

    /**
     * Get Pickup history List
     * 
     * @param pickupHistory Pickup history
     * @return Pickup history List
     */
    public List<PickupHistory> selectPickupHistoryList(PickupHistory pickupHistory);
    
	/**
	 * Get list Pickup history without yard position
	 * 
	 * @return Pickup history List
	 */
	public List<PickupHistory> selectPickupHistoryWithoutYardPostion(
			@Param("searchParams") Map<String, String> searchParams);

	/**
	 * Get list Pickup history has yard position
	 * 
	 * @return Pickup history List
	 */
	public List<PickupHistory> selectPickupHistoryHasYardPostion(
			@Param("searchParams") Map<String, String> searchParams);
	
    /**
     * Add Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return Result
     */
    public int insertPickupHistory(PickupHistory pickupHistory);

    /**
     * Update Pickup history
     * 
     * @param pickupHistory Pickup history
     * @return Result
     */
    public int updatePickupHistory(PickupHistory pickupHistory);

    /**
     * Delete Pickup history
     * 
     * @param id Pickup historyID
     * @return result
     */
    public int deletePickupHistoryById(Long id);

    /**
     * Batch Delete Pickup history
     * 
     * @param ids IDs
     * @return result
     */
    public int deletePickupHistoryByIds(String[] ids);

    public List<PickupHistory> selectPickupHistoryListForReport(PickupHistory pickupHistory);

    public List<PickupHistory> selectPickupHistoryListForHistory(PickupHistory pickupHistory);

    public List<PickupHistory> selectPickupHistoryListForOmSupport(PickupHistory pickupHistory);

    public List<PickupHistoryForm> selectPickupHistoryForDriver(@Param("userId") Long userId);

    public List<Pickup> selectPickupListByDriverId(Long driverId);

    public PickupHistoryDetail selectPickupHistoryDetailById(@Param("driverId") Long driverId, @Param("pickupId") Long pickupId);
}