package vn.com.irtech.eport.carrier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.carrier.domain.Edo;

/**
 * Exchange Delivery OrderMapper Interface
 * 
 * @author irtech
 * @date 2020-06-26
 */
public interface EdoMapper 
{
    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    public Edo selectEdoById(Long id);
    
    /**
     * Get first Exchange Delivery Order
     * @param orderNumber
     * @return
     */
    public Edo selectFirstEdo(Edo edo);

    /**
     * Get Exchange Delivery Order List
     * 
     * @param edo Exchange Delivery Order
     * @return Exchange Delivery Order List
     */
    public List<Edo> selectEdoList(Edo edo);

    /**
     * Add Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return Result
     */
    public int insertEdo(Edo edo);

    /**
     * Update Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return Result
     */
    public int updateEdo(Edo edo);

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    public int deleteEdoById(Long id);

    /**
     * Batch Delete Exchange Delivery Order
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoByIds(String[] ids);

    public Edo checkContainerAvailable(@Param("container") String cont,@Param("billNo") String billNo);
    
    public String getOpeCodeByBlNo(String blNo);
    
    public Long getCountContainerAmountByBlNo(String blNo);
    
    public List<Edo> selectEdoListByBlNo(String blNo);

    public List<Edo> selectEdoListByBillNo(Edo edo);

    public List<String> selectVesselNo(String keyString);

    public List<String> selectOprCode(String keyString);

    public List<String> selectVoyNo(String keyString);

    public List<String> selectVesselList(String keyString);

}
