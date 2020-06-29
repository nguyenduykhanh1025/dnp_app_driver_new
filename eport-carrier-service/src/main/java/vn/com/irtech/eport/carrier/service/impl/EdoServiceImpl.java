package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.text.Convert;


/**
 * Exchange Delivery OrderService Business Processing
 * 
 * @author ruoyi
 * @date 2020-06-26
 */
@Service
public class EdoServiceImpl implements IEdoService 
{
    @Autowired
    private EdoMapper edoMapper;

    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    @Override
    public Edo selectEdoById(Long id)
    {
        return edoMapper.selectEdoById(id);
    }

    /**
     * Get Exchange Delivery Order List
     * 
     * @param edo Exchange Delivery Order
     * @return Exchange Delivery Order
     */
    @Override
    public List<Edo> selectEdoList(Edo edo)
    {
        return edoMapper.selectEdoList(edo);
    }

    /**
     * Add Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return result
     */
    @Override
    public int insertEdo(Edo edo)
    {
        edo.setCreateTime(DateUtils.getNowDate());
        return edoMapper.insertEdo(edo);
    }

    /**
     * Update Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return result
     */
    @Override
    public int updateEdo(Edo edo)
    {
        edo.setUpdateTime(DateUtils.getNowDate());
        return edoMapper.updateEdo(edo);
    }

    /**
     * Delete Exchange Delivery Order By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEdoByIds(String ids)
    {
        return edoMapper.deleteEdoByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    @Override
    public int deleteEdoById(Long id)
    {
        return edoMapper.deleteEdoById(id);
    }

    @Override
    public Edo checkContainerAvailable(@Param("container") String cont,@Param("billNo") String billNo)
    {
        return edoMapper.checkContainerAvailable(cont,billNo);
    }

    

}
