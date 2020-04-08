package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.CarrierGroup;

/**
 * Carrier GroupMapper Interface
 * 
 * @author irtech
 * @date 2020-04-06
 */
public interface CarrierGroupMapper 
{
    /**
     * Get Carrier Group
     * 
     * @param id Carrier GroupID
     * @return Carrier Group
     */
    public CarrierGroup selectCarrierGroupById(Long id);

    /**
     * Get Carrier Group List
     * 
     * @param carrierGroup Carrier Group
     * @return Carrier Group List
     */
    public List<CarrierGroup> selectCarrierGroupList(CarrierGroup carrierGroup);

    /**
     * Add Carrier Group
     * 
     * @param carrierGroup Carrier Group
     * @return Result
     */
    public int insertCarrierGroup(CarrierGroup carrierGroup);

    /**
     * Update Carrier Group
     * 
     * @param carrierGroup Carrier Group
     * @return Result
     */
    public int updateCarrierGroup(CarrierGroup carrierGroup);

    /**
     * Delete Carrier Group
     * 
     * @param id Carrier GroupID
     * @return result
     */
    public int deleteCarrierGroupById(Long id);

    /**
     * Batch Delete Carrier Group
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteCarrierGroupByIds(String[] ids);

    /**
     * Search carrier group by name
     * 
     * @param keyword 
     * @return result
     */
    public List<CarrierGroup> selectCarrierGroupListByCode(CarrierGroup carrierGroup);

    /**
	 * Check group code unique
	 * 
	 * @param groupCode
	 * @return
	 */
    public int checkGroupCodeUnique(String groupCode);
    
    /**
	 * Check main email unique
	 * 
	 * @param mainEmail
	 * @return
	 */
    public int checkMainEmailUnique(String mainEmail);
}
