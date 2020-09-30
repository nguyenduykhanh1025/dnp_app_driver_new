package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.carrier.domain.CarrierApi;
import vn.com.irtech.eport.carrier.mapper.CarrierApiMapper;
import vn.com.irtech.eport.carrier.service.ICarrierApiService;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;

/**
 * 
 * @author Trong Hieu
 * @date 2020-09-28
 */
@Service
public class CarrierApiServiceImpl implements ICarrierApiService 
{
    @Autowired
    private CarrierApiMapper carrierApiMapper;

    /**
     * Get carrier api
     * 
     * @param id carrier api ID
     * @return carrier api
     */
    @Override
    public CarrierApi selectCarrierApiById(Long id)
    {
        return carrierApiMapper.selectCarrierApiById(id);
    }

    /**
     * Get carrier api List
     * 
     * @param carrierApi carrier api
     * @return carrier api
     */
    @Override
    public List<CarrierApi> selectCarrierApiList(CarrierApi carrierApi)
    {
        return carrierApiMapper.selectCarrierApiList(carrierApi);
    }

    /**
     * Add carrier api
     * 
     * @param carrierApi carrier api
     * @return result
     */
    @Override
    public int insertCarrierApi(CarrierApi carrierApi)
    {
        carrierApi.setCreateTime(DateUtils.getNowDate());
        return carrierApiMapper.insertCarrierApi(carrierApi);
    }

    /**
     * Update carrier api
     * 
     * @param carrierApi carrier api
     * @return result
     */
    @Override
    public int updateCarrierApi(CarrierApi carrierApi)
    {
        carrierApi.setUpdateTime(DateUtils.getNowDate());
        return carrierApiMapper.updateCarrierApi(carrierApi);
    }

    /**
     * Delete carrier api By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteCarrierApiByIds(String ids)
    {
        return carrierApiMapper.deleteCarrierApiByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete carrier api
     * 
     * @param id carrier api ID
     * @return result
     */
    @Override
    public int deleteCarrierApiById(Long id)
    {
        return carrierApiMapper.deleteCarrierApiById(id);
    }

	@Override
	public boolean checkOprCodeExist(String oprCode) {
		return carrierApiMapper.checkOprCodeExist(oprCode) > 0;
	}

	@Override
	public CarrierApi selectCarrierApiByOprCode(String oprCode) {
		return carrierApiMapper.selectCarrierApiByOprCode(oprCode);
	}
}
