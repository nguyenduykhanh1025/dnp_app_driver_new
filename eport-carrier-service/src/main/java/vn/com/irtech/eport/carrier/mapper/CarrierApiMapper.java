package vn.com.irtech.eport.carrier.mapper;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.CarrierApi;

/**
 * 
 * @author Trong Hieu
 * @date 2020-09-28
 */
public interface CarrierApiMapper 
{
    /**
     * Get carrier api
     * 
     * @param id carrier api ID
     * @return carrier api
     */
    public CarrierApi selectCarrierApiById(Long id);

    /**
     * Get carrier api List
     * 
     * @param carrierApi carrier api
     * @return carrier api List
     */
    public List<CarrierApi> selectCarrierApiList(CarrierApi carrierApi);

    /**
     * Add carrier api
     * 
     * @param carrierApi carrier api
     * @return Result
     */
    public int insertCarrierApi(CarrierApi carrierApi);

    /**
     * Update carrier api
     * 
     * @param carrierApi carrier api
     * @return Result
     */
    public int updateCarrierApi(CarrierApi carrierApi);

    /**
     * Delete carrier api
     * 
     * @param id carrier api ID
     * @return result
     */
    public int deleteCarrierApiById(Long id);

    /**
     * Batch Delete carrier api
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteCarrierApiByIds(String[] ids);

    /**
     * Check if one OPR exist in API
     * @param oprCode
     * @return
     */
	public int checkOprCodeExist(String oprCode);

	/**
	 * Select one OPR Code in API
	 * 
	 * @param oprCode
	 * @return
	 */
	public CarrierApi selectCarrierApiByOprCode(String oprCode);
}
