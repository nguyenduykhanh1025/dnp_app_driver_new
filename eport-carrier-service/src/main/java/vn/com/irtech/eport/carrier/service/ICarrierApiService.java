package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.CarrierApi;

/**
 * carrier api Service Interface
 * 
 * @author Trong Hieu
 * @date 2020-09-28
 */
public interface ICarrierApiService 
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
     * @return result
     */
    public int insertCarrierApi(CarrierApi carrierApi);

    /**
     * Update carrier api
     * 
     * @param carrierApi carrier api
     * @return result
     */
    public int updateCarrierApi(CarrierApi carrierApi);

    /**
     * Batch Delete carrier api
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteCarrierApiByIds(String ids);

    /**
     * Delete carrier api
     * 
     * @param id carrier api ID
     * @return result
     */
    public int deleteCarrierApiById(Long id);
}
